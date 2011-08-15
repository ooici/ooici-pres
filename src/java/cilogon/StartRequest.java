package cilogon;

import edu.uiuc.ncsa.csd.util.Benchmarker;

import org.cilogon.portal.CILogonService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

import static org.cilogon.util.CILogon.CERT_REQUEST_ID;

/**
 * A very simple sample servlet showing how a portal can start delegation.
 * <p>Created by Jeff Gaynor<br>
 * on Jun 18, 2010 at  2:10:58 PM
 */
public class StartRequest extends PortalAbstractServlet{
    // An accession number. 
    protected void doIt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
        Benchmarker bm = new Benchmarker(this);
        bm.msg("2.a. Starting request");
        String identifier = request.getParameter("identifier");
        if(identifier == null){
            identifier = "id-" +  System.nanoTime();
        }
        // Set the cookie
        debug("2.a. Adding cookie for identifier = " + identifier);
        Cookie cookie = new Cookie(CERT_REQUEST_ID, identifier);
        response.addCookie(cookie);
        CILogonService cis = new CILogonService(getPortalEnvironment());
        URI redirectUri = cis.requestCredential(identifier);
        bm.msg("2.d. Got redirect uri");
        response.sendRedirect(redirectUri.toString());
    }

}
