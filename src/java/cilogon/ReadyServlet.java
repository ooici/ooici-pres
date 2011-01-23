package cilogon;

import net.oauth.OAuth;
import net.oauth.OAuthMessage;
import org.cilogon.portal.CILogonService;
import org.cilogon.portal.util.CreateCertRequestThread;
import org.cilogon.portal.util.OAuthUtilities;
import org.cilogon.portal.util.PortalTransaction;
import org.cilogon.util.Benchmarker;
import org.cilogon.util.CILogonProperties;
import org.cilogon.util.SecurityUtil;
import org.cilogon.util.exceptions.CILogonException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

import static net.oauth.OAuth.*;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.cilogon.portal.CILogonService.MAX_CERT_REQUEST_THREAD_WAIT;
import static org.cilogon.portal.util.CreateCertRequestThread.*;
import static org.cilogon.util.CILogon.*;

/**
 * Servlet that is the target of the callback.
 * <p>Created by Jeff Gaynor<br>
 * on Apr 27, 2010 at  3:23:08 PM
 */
public class ReadyServlet extends PortalAbstractServlet {

    protected void doIt(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        // This servlet is being invoked as a callback from the service.
        // Its first job is to store the verifier and return with an success code.
        try {
            Benchmarker bm = new Benchmarker(this);
            debug("6.b. Callback from" + httpServletRequest.getRequestURI());
            String tc = OAuth.decodePercent(httpServletRequest.getParameter(OAuth.OAUTH_TOKEN));
            URI tempCred = URI.create(tc);
            //debug("Got temp cred = " + tempCred);
            PortalTransaction t = (PortalTransaction) getStore().getByTempCred(tempCred);
            bm.msg("got portal transaction = " + t);
            if (t == null) {
                debug("NO TRANSACTION for this temp cred!");
                httpServletResponse.setStatus(SC_NOT_FOUND);
                return;
            }
            String v = OAuth.decodePercent(httpServletRequest.getParameter(OAuth.OAUTH_VERIFIER));
            t.setVerifier(URI.create(v));
            t.save();
            bm.msg("6.b. Saved transaction");
            httpServletResponse.setStatus(SC_OK);
            // now we have to get the rest of the chain started.
            // This servlet now will do calls to get the access token then get the assests from the service.
            DoRestThread doRestThread = new DoRestThread(t);
            doRestThread.start();
            bm.msg("6.c. Successfully completed callback.");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error", e);
        }
    }

    public SecurityUtil getSecurityUtil() {
        if (securityUtil == null) {
            securityUtil = new SecurityUtil();
        }
        return securityUtil;
    }

    SecurityUtil securityUtil;

    public class DoRestThread extends Thread {
        public DoRestThread(PortalTransaction portalTransaction) {
            this.portalTransaction = portalTransaction;
        }

        public PortalTransaction getPortalTransaction() {
            return portalTransaction;
        }

        public void setPortalTransaction(PortalTransaction portalTransaction) {
            this.portalTransaction = portalTransaction;
        }

        PortalTransaction portalTransaction;

        @Override
        public void run() {

            try {
                doRest();
            } catch (Exception e) {
                e.printStackTrace();
                throw new CILogonException("Error completing rest of transaction", e);
            }

        }

        protected void doRest() throws Exception {
            Benchmarker bm = new Benchmarker(ReadyServlet.this);
            PortalTransaction t = getPortalTransaction();
            // Exchange the temp cred for access tokens
            debug("6.d. starting temp cred and access token exchange.");
            CILogonProperties props = new CILogonProperties();
            props.setURI(OAUTH_VERIFIER, t.getVerifier());
            props.setURI(OAUTH_TOKEN, t.getTempCred());
            props.setURI(OAUTH_TOKEN_SECRET, t.getTempCredSS());
            OAuthUtilities oAuthUtilities = new OAuthUtilities(getPortalEnvironment());
            OAuthMessage message = oAuthUtilities.getAccessToken(props, CILOGON_ACCESS_TOKEN_URI);
            long currentTime = System.currentTimeMillis();
            t.setAccessToken(URI.create(message.getParameter(OAUTH_TOKEN)));
            t.setAccessTokenSS(URI.create(message.getParameter(OAUTH_TOKEN_SECRET)));
            t.save();
            bm.msg("6.e. Got & saved access token");

            // Next bit of logic gets the cert request from the thread that created it.
            // It is *possible* that the thread is not quite done, albeit unlikely.
            // This checks that.
            if (CILogonService.hasPendingCertRequest(t.getIdentifier())) {
                CreateCertRequestThread c = CILogonService.getPendingRequest(t.getIdentifier());
                switch (c.getStatus()) {
                    case DONE:
                        // Most likely case. The private key and cert request are ready and waiting.
                        CILogonService.removePendingCertRequest(t.getIdentifier());
                        break;

                    case PENDING:
                        // Really unlikely, but if this happens, log it.
                        debug(t.getIdentifier() + " is pending");
                        // wait for it, but not too long
                        debug("Waiting for cert request to finish");
                        // if not done, not much else to do but join the threads and wait....
                        c.join(MAX_CERT_REQUEST_THREAD_WAIT);
                        if (c.isAlive()) {
                            throw new CILogonException("Error: could not create cert request -- request timed out before completion");
                        }
                        log("finished waiting");
                        if (c.getStatus() != DONE) {
                            throw new CILogonException("Error: could not create cert request");
                        }
                        break;
                    case ERROR:
                        throw new CILogonException("Error: There was an error creating the cert request", c.getStoredException());
                    case NOT_STARTED:
                        // Also really unlikely, but log it just in case.
                        debug(t.getIdentifier() + " is not started");

                        // do it manually.
                        c.createCertRequest();
                        CILogonService.removePendingCertRequest(t.getIdentifier());
                        break;
                    default:
                        throw new CILogonException("Error: cert request in unknown state. Cannot continue");
                }
            }
            byte[] derencodedCertReq = t.getCertReqBytes();
            props = new CILogonProperties();
            props.setBytes(CERT_REQUEST_PARAMETER, derencodedCertReq);
            // say("bytes encoded as " + props.getString(CERT_REQUEST_PARAMETER));
            props.setURI(OAUTH_TOKEN, t.getAccessToken());
            props.setURI(OAUTH_TOKEN_SECRET, t.getAccessTokenSS());
            currentTime = System.currentTimeMillis();
            bm.msg("6.f. Stored transaction, got cert request. Preparing to get assets");
            message = oAuthUtilities.getAssets(props, CILOGON_X509_URI);
            bm.msg("6.j Got cert, starting to save it");
            t.setX509Certificate(getSecurityUtil().fromPEM(message.getBodyAsStream()));
            t.setComplete(true);
            t.save();
            bm.msg("6.j. Cert saved.");
        }

    }

}
