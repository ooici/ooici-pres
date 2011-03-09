package cilogon;

import ion.core.messaging.IonMessage;
import ion.core.messaging.MessagingName;

import java.io.PrintWriter;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ooici.pres.BootstrapIONService;

import org.cilogon.portal.CILogonService;
import org.cilogon.portal.util.PortalCredentials;
import org.cilogon.util.SecurityUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * <p>Created by Jeff Gaynor<br>
 * on Jul 31, 2010 at  3:29:09 PM
 * 
 * Update by TomLennan March 2011
 * 
 * On successful return from CILogon, this servlet will call into ION Core to
 * register the user credentials with the Identity Registry.  The Registry will
 * return an OOID.  This OOID is stashed in a cookie named IONCOREOOID.
 * The cookie expiry is set to the expiry of the X.509 certificate.  Finally,
 * the servlet constructs a security token to indicate to the Spring security
 * core that this user has been authenticated.
 */
public class SuccessServlet extends PortalAbstractServlet {
    protected void doIt(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable {
        httpServletResponse.setContentType("text/html");
        PrintWriter pw = httpServletResponse.getWriter();
        String y;

        String identifier = clearCookie(httpServletRequest, httpServletResponse);
        if (identifier == null) {
            throw new ServletException("Error: No identifier for this delegation request was found. ");
        }
        CILogonService cis = new CILogonService(getPortalEnvironment());
        PortalCredentials credential = cis.getCredential(identifier);
        
        X509Certificate certificate = credential.getX509Certificate();
        long startDateMS = certificate.getNotBefore().getTime();
        long expirationDateMS = certificate.getNotAfter().getTime();
        long currentDateMS = System.currentTimeMillis();

        // Ensure certificate is currently valid
        if (startDateMS < currentDateMS && currentDateMS < expirationDateMS) {
        	// Authenticate user with ION
        	Map<String, Object> content = new HashMap<String, Object>();
        	content.put("user_cert", getSecurityUtil().toPEM(certificate));
        	content.put("user_private_key", getSecurityUtil().toPEM(credential.getPrivateKey()));

        	// Create and send request message
        	String SYSNAME = System.getProperty("ioncore.sysname","Tom");
        	MessagingName identRegSvc = new MessagingName(SYSNAME, "identity_service");
        	IonMessage msgin = BootstrapIONService.baseProcess.rpcSend(identRegSvc, "register_user_credentials", content);
        	assert(!msgin.isErrorMessage());
        	String ooid = (String)msgin.getContent();
        	BootstrapIONService.baseProcess.ackMessage(msgin);

        	HttpSession session = httpServletRequest.getSession();

        	if (session == null) {
        		session = httpServletRequest.getSession(true);
        	}
        	session.setAttribute("IONCOREOOID", ooid);

        	// Set cookie with max age equal to certificate expiration time
        	int expiry = (int)((expirationDateMS - currentDateMS)/1000);

        	Cookie cookie = new Cookie("IONCOREOOID", ooid);
        	cookie.setMaxAge(expiry);
        	httpServletResponse.addCookie(cookie);

        	// Programmatically add credential for principal (OOID)
        	PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(
        			ooid, new GrantedAuthority[] { new GrantedAuthorityImpl("ROLE_ADMIN") });
        	token.setAuthenticated(true);
        	SecurityContextHolder.getContext().setAuthentication(token);

        	/* Put the key and certificate in the result, but allow them to be initially hidden. */
        	y = "<html>\n" +
        	"<style type=\"text/css\">\n" +
        	".hidden { display: none; }\n" +
        	".unhidden { display: block; }\n" +
        	"</style>\n" +
        	"<script type=\"text/javascript\">\n" +
        	"function unhide(divID) {\n" +
        	"    var item = document.getElementById(divID);\n" +
        	"    if (item) {\n" +
        	"        item.className=(item.className=='hidden')?'unhidden':'hidden';\n" +
        	"    }\n" +
        	"}\n" +
        	"</script>\n" +
        	"<body>\n" +
        	"<h1>Success!</h1>\n" +
        	"<p>You have successfully requested a certificate from the service.</p>\n" +
        	"<h1>Identity Service OOID</h1>" +
        	"<p>" + ooid + "</p>" + 
        	"<ul>\n" +
        	"    <li><a href=\"javascript:unhide('showCert');\">Show/Hide certificate</a></li>\n" +
        	"    <div id=\"showCert\" class=\"unhidden\">\n" +
        	"        <p><pre>" + getSecurityUtil().toPEM(credential.getX509Certificate()) + "</pre>\n" +
        	"    </div>\n" +
        	"    <li><a href=\"javascript:unhide('showKey');\">Show/Hide private key</a></li>\n" +
        	"    <div id=\"showKey\" class=\"hidden\">\n" +
        	"        <p><pre>" + getSecurityUtil().toPEM(credential.getPrivateKey()) + "</pre>\n" +
        	"    </div>\n" +
        	"\n" +
        	"</ul>\n" +
        	"<form name=\"input\" action=" + httpServletRequest.getContextPath() + "/ method=\"get\">\n" +
        	"   <input type=\"submit\" value=\"Return to portal\" />\n" +
        	"</form>" +
        	"</body>\n" +
        	"</html>";
        }
        else {
        	// Report certificate time issue
        	y = "<html>\n" +
        	"<style type=\"text/css\">\n" +
        	".hidden { display: none; }\n" +
        	".unhidden { display: block; }\n" +
        	"</style>\n" +
        	"<script type=\"text/javascript\">\n" +
        	"function unhide(divID) {\n" +
        	"    var item = document.getElementById(divID);\n" +
        	"    if (item) {\n" +
        	"        item.className=(item.className=='hidden')?'unhidden':'hidden';\n" +
        	"    }\n" +
        	"}\n" +
        	"</script>\n" +
        	"<body>\n" +
        	"<h1>Failure!</h1>\n" +
        	"<p>Current time is outside range of certificate not before time or not after time.</p>\n" +
        	"<ul>\n" +
        	"    <li><a href=\"javascript:unhide('showCert');\">Show/Hide certificate</a></li>\n" +
        	"    <div id=\"showCert\" class=\"unhidden\">\n" +
        	"        <p><pre>" + getSecurityUtil().toPEM(credential.getX509Certificate()) + "</pre>\n" +
        	"    </div>\n" +
        	"    <li><a href=\"javascript:unhide('showKey');\">Show/Hide private key</a></li>\n" +
        	"    <div id=\"showKey\" class=\"hidden\">\n" +
        	"        <p><pre>" + getSecurityUtil().toPEM(credential.getPrivateKey()) + "</pre>\n" +
        	"    </div>\n" +
        	"\n" +
        	"</ul>\n" +
        	"<form name=\"input\" action=" + httpServletRequest.getContextPath() + "/ method=\"get\">\n" +
        	"   <input type=\"submit\" value=\"Return to portal\" />\n" +
        	"</form>" +
        	"</body>\n" +
        	"</html>";

        }
        pw.println(y);
        pw.flush();
    }

  public SecurityUtil getSecurityUtil() {
	  if (securityUtil == null) {
		  securityUtil = new SecurityUtil();
	  }
	  return securityUtil;
  }

  SecurityUtil securityUtil;


}
