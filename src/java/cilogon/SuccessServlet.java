package cilogon;

import ion.core.messaging.IonMessage;
import ion.core.messaging.MessagingName;
import ion.integration.ais.AppIntegrationService;

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

import net.ooici.integration.ais.registerUser.RegisterUser.RegisterIonUser;
import ooici.pres.BootstrapIONService;

import org.cilogon.portal.CILogonService;
import org.cilogon.portal.util.PortalCredentials;
import org.cilogon.util.SecurityUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.google.protobuf.JsonFormat;

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
    	String OOID_PREFIX = "{\"ooi_id\": ";
    	
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
        	// Unfortunately, the strings returned from the util need a bit of massaging
        	// to make them valid Json to pass to ION
        	String str = certificate.toString();
        	String certificateString = getSecurityUtil().toPEM(certificate);
        	certificateString = certificateString.substring(0, certificateString.lastIndexOf("\n"));
        	certificateString = certificateString.replace("\r", "");
        	certificateString = certificateString.replace("\n", "\\n");
        	
        	String privateKeyString = getSecurityUtil().toPEM(credential.getPrivateKey());
        	privateKeyString = privateKeyString.replace("\r", "");
        	privateKeyString = privateKeyString.replace("\n", "\\n");
        	
    		String request = "{\"certificate\": \"" + certificateString + "\",";
    		request += "\"rsa_private_key\": \"" + privateKeyString + "\"}";

        	String SYSNAME = System.getProperty("ioncore.sysname","Tom");
        	AppIntegrationService ais = new AppIntegrationService(SYSNAME, BootstrapIONService.baseProcess);
        	String result = ais.sendReceiveUIRequest(request, AppIntegrationService.RequestType.REGISTER_USER, "ANONYMOUS", "0");

        	if (result != null && result.length() > 0 && result.startsWith(OOID_PREFIX)) {
        		// Extract OOID string from Json result
        		String ooid = result.substring(OOID_PREFIX.length() + 1, result.length() - 2);

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
            	"<p>You have successfully requested a certificate from the service.</p>\n" +
            	"<h1>But the attempt to register to the Identity Service failed</h1>" +
            	"<p>" + result + "</p>" + 
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
