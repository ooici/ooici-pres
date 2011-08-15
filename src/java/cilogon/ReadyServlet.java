package cilogon;

import edu.uiuc.ncsa.csd.delegation.client.request.CallbackRequest;
import edu.uiuc.ncsa.csd.delegation.client.request.CallbackResponse;
import edu.uiuc.ncsa.csd.delegation.client.request.DelegatedAssetRequest;
import edu.uiuc.ncsa.csd.delegation.client.request.DelegatedAssetResponse;
import edu.uiuc.ncsa.csd.delegation.client.server.CallbackServer;
import edu.uiuc.ncsa.csd.delegation.storage.impl.ClientTransaction;
import edu.uiuc.ncsa.csd.util.Benchmarker;
import org.apache.commons.codec.binary.Base64;
import org.cilogon.portal.CILogonService;
import org.cilogon.portal.util.CreateCertRequestThread;
import org.cilogon.util.exceptions.CILogonException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.cilogon.portal.CILogonService.MAX_CERT_REQUEST_THREAD_WAIT;
import static org.cilogon.portal.util.CreateCertRequestThread.*;
import static org.cilogon.util.CILogon.CERT_REQUEST_PARAMETER;

/**
 * Servlet that is the target of the callback.
 * <p>Created by Jeff Gaynor<br>
 * on Apr 27, 2010 at  3:23:08 PM
 */
public class ReadyServlet extends PortalAbstractServlet {

    protected void doIt(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        // This servlet is being invoked as a callback from the server.
        // Its first job is to store the verifier and return with an success code.
        try {
            info("Got callback request, processing...");
            CallbackServer callbackServer = getPortalEnvironment().getCallbackServer();
            CallbackRequest cbReq = new CallbackRequest(httpServletRequest);
            // send this to the callback server which will process the request.
            CallbackResponse cResp = (CallbackResponse) callbackServer.process(cbReq);
            ClientTransaction t = (ClientTransaction) getStore().get(cResp.getAuthorizationGrant());
            if (t == null) {
                debug("NO TRANSACTION for authz grant =\"" + cResp.getAuthorizationGrant() + "\"");
                httpServletResponse.setStatus(SC_NOT_FOUND);
                return;
            }

            t.setVerifier(cResp.getVerifier());
            info("Saving verifier: " + cResp.getVerifier());
            getStore().save(t);
            httpServletResponse.setStatus(SC_OK);

            // now we have to get the rest of the chain started.
            // This servlet now will do calls to get the access token then get the assests from the server.
            DoRestThread doRestThread = new DoRestThread(t);
            doRestThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error", e);
        }
    }


    public class DoRestThread extends Thread {
        public DoRestThread(ClientTransaction clientTransaction) {
            this.clientTransaction = clientTransaction;
        }

        public ClientTransaction getClientTransaction() {
            return clientTransaction;
        }

        public void setClientTransaction(ClientTransaction clientTransaction) {
            this.clientTransaction = clientTransaction;
        }

        ClientTransaction clientTransaction;

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
            ClientTransaction t = getClientTransaction();
            debug("6.d. starting temp cred and access token exchange.");

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
            HashMap props = new HashMap();
            props.put(CERT_REQUEST_PARAMETER, Base64.encodeBase64String(t.getCertReqBytes()));

            DelegatedAssetRequest daReq = new DelegatedAssetRequest();
            daReq.setClient(getPortalEnvironment().getClient());
            daReq.setAuthorizationGrant(t.getAuthorizationGrant());
            daReq.setVerifier(t.getVerifier());
            daReq.setAssetParameters(props);

            DelegatedAssetResponse daResp = (DelegatedAssetResponse) getDelegationService().process(daReq);

            t.setProtectedAsset(daResp.getProtectedAsset());
            t.setComplete(true);
            getStore().save(t);
            bm.msg("6.j. Cert saved.");
        }

    }

}
