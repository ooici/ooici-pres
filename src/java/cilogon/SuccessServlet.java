package cilogon;

import grails.converters.JSON;
import ion.integration.ais.AppIntegrationService.RequestType;

import java.io.PrintWriter;
import java.net.URI;
import java.security.cert.X509Certificate;
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
import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.codehaus.groovy.grails.web.json.JSONObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * <p>Created by Jeff Gaynor<br>
 * on Jul 31, 2010 at  3:29:09 PM
 * 
 * Update by Tom Lennan March 2011
 * 
 * On successful return from CILogon, this servlet will call into ION Core to
 * register the user credentials with the Identity Registry.  The Registry will
 * return an OOI_ID.  This OOI_ID is stashed in a cookie named IONCOREOOIID.
 * The cookie expiry is set to the expiry of the X.509 certificate.  Finally,
 * the servlet constructs a security token to indicate to the Spring security
 * core that this user has been authenticated.
 */
public class SuccessServlet extends PortalAbstractServlet {

	protected void doIt(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable {
    	
        httpServletResponse.setContentType("text/html");
        PrintWriter pw = httpServletResponse.getWriter();

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
        	String certificateString = getSecurityUtil().toPEM(certificate);
        	certificateString = certificateString.substring(0, certificateString.lastIndexOf("\n"));
        	certificateString = certificateString.replace("\r", "");
        	certificateString = certificateString.replace("\n", "\\n");
        	
        	String privateKeyString = getSecurityUtil().toPEM(credential.getPrivateKey());
        	privateKeyString = privateKeyString.replace("\r", "");
        	privateKeyString = privateKeyString.replace("\n", "\\n");
        	
    		String request = "{\"certificate\": \"" + certificateString + "\",";
    		request += "\"rsa_private_key\": \"" + privateKeyString + "\"}";
    		
        	String result = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(request, RequestType.REGISTER_USER, "ANONYMOUS", "0");

        	if (result != null && result.length() > 0) {
        		HttpSession session = httpServletRequest.getSession(true);

    			String ooi_id = "";
    			boolean userIsAdmin;
    			boolean userIsEarlyAdopter;
    			boolean userIsDataProvider;
    			boolean userIsMarineOperator;
    			boolean userIsAlreadyRegistered;

        		try {
        			// Extract values from Json result
        			JSONObject valueMap = (JSONObject)JSON.parse(result);

        			ooi_id = (String)valueMap.get(BootstrapIONService.OOI_ID_KEY);
        			userIsAdmin = (Boolean)valueMap.get(BootstrapIONService.USER_IS_ADMIN_KEY);
        			userIsEarlyAdopter = (Boolean)valueMap.get(BootstrapIONService.USER_IS_EARY_ADOPTER_KEY);
        			userIsDataProvider = (Boolean)valueMap.get(BootstrapIONService.USER_IS_DATA_PROVIDER_KEY);
        			userIsMarineOperator = (Boolean)valueMap.get(BootstrapIONService.USER_IS_MARINE_OPERATOR_KEY);
        			userIsAlreadyRegistered = (Boolean)valueMap.get(BootstrapIONService.USER_ALREADY_REGISTERED_KEY);
        		}
        		catch (Exception e) {
                	String y = "<html>\n" +
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

                    pw.println(y);
                    pw.flush();
                    return;
        		}

        		// Set cookie with max age equal to certificate expiration time
        		int expiry = (int)((expirationDateMS - currentDateMS)/1000);

        		session.setAttribute("IONCOREOOIID", ooi_id);
        		session.setAttribute("IONCOREEXPIRY", "" + expirationDateMS/1000);
        		
//        		Cookie cookie = new Cookie("IONCOREOOIID", ooi_id);
//        		cookie.setMaxAge(expiry);
//        		httpServletResponse.addCookie(cookie);
//
//        		cookie = new Cookie("IONCOREEXPIRY", "" + expirationDateMS/1000);
//        		cookie.setMaxAge(expiry);
//        		httpServletResponse.addCookie(cookie);

        		// Programmatically add credential for principal (OOI_ID)
        		String authorityRole = "ROLE_USER";
        		if (userIsAdmin) {
        			authorityRole = "ROLE_ADMIN";
            		session.setAttribute("ROLE_ADMIN", true);
        		}
        		else {
            		session.setAttribute("ROLE_ADMIN", false);
        		}

        		if (userIsEarlyAdopter) {
            		session.setAttribute("EARLY_ADOPTER", true);
        		}
        		else {
            		session.setAttribute("EARLY_ADOPTER", false);
        		}

        		if (userIsDataProvider) {
            		session.setAttribute("DATA_PROVIDER", true);
        		}
        		else {
            		session.setAttribute("DATA_PROVIDER", false);
        		}

        		if (userIsMarineOperator) {
            		session.setAttribute("MARINE_OPERATOR", true);
        		}
        		else {
            		session.setAttribute("MARINE_OPERATOR", false);
        		}

        		PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(
        				ooi_id, new GrantedAuthority[] { new GrantedAuthorityImpl(authorityRole) });
        		token.setAuthenticated(true);
        		SecurityContextHolder.getContext().setAuthentication(token);
        		
    			Map map = ConfigurationHolder.getConfig().flatten();
        		String mainUrl = (String)map.get("ioncore.mainurl");
        			
        		if (mainUrl == null) {
        			mainUrl = "/main";
        		}
            	if (!userIsAlreadyRegistered) {
            		mainUrl = mainUrl + "?register";
        		}
        			
        		URI redirectUri = new URI(httpServletRequest.getContextPath() + mainUrl);
        		httpServletResponse.sendRedirect(redirectUri.toString());
//
//        		String output = "<html>\n" +
//        		"<head>\n" +
//        		"	<script type=\"text/javascript\">\n" +
//        		"		OOI_ROLE=[\"admin\"; \"early adopter\"];\n" +
//        		"	</script>\n" +
//        		"</head>\n" +
//        		"<body>\n" +
//        		"</body>\n" +
//        		"</html>";

//
//        		/* Put the key and certificate in the result, but allow them to be initially hidden. */
//        		y = "<html>\n" +
//        		"<style type=\"text/css\">\n" +
//        		".hidden { display: none; }\n" +
//        		".unhidden { display: block; }\n" +
//        		"</style>\n" +
//        		"<script type=\"text/javascript\">\n" +
//        		"function unhide(divID) {\n" +
//        		"    var item = document.getElementById(divID);\n" +
//        		"    if (item) {\n" +
//        		"        item.className=(item.className=='hidden')?'unhidden':'hidden';\n" +
//        		"    }\n" +
//        		"}\n" +
//        		"</script>\n" +
//        		"<body>\n" +
//        		"<h1>Success!</h1>\n" +
//        		"<p>You have successfully requested a certificate from the service.</p>\n" +
//        		"<h1>Identity Service OOI_ID</h1>" +
//        		"<p>" + ooid + "</p>" + 
//        		"<ul>\n" +
//        		"    <li><a href=\"javascript:unhide('showCert');\">Show/Hide certificate</a></li>\n" +
//        		"    <div id=\"showCert\" class=\"unhidden\">\n" +
//        		"        <p><pre>" + getSecurityUtil().toPEM(credential.getX509Certificate()) + "</pre>\n" +
//        		"    </div>\n" +
//        		"    <li><a href=\"javascript:unhide('showKey');\">Show/Hide private key</a></li>\n" +
//        		"    <div id=\"showKey\" class=\"hidden\">\n" +
//        		"        <p><pre>" + getSecurityUtil().toPEM(credential.getPrivateKey()) + "</pre>\n" +
//        		"    </div>\n" +
//        		"\n" +
//        		"</ul>\n" +
//        		"<form name=\"input\" action=" + httpServletRequest.getContextPath() + "/ method=\"get\">\n" +
//        		"   <input type=\"submit\" value=\"Return to portal\" />\n" +
//        		"</form>" +
//        		"</body>\n" +
//        		"</html>";
        	}
        	else {
        		int status = BootstrapIONService.appIntegrationService.getStatus();
        		String errorMessage = BootstrapIONService.appIntegrationService.getErrorMessage();
        		
    			System.out.println("Error received on findResources\nRequest message: " + request + "\nStatus: " + status + "\nError message: " + errorMessage);
        		
            	String y = "<html>\n" +
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
            	"<h1>But the attempt to register to the Identity Service failed.</h1>" +
            	"<p>Status Code: " + status + "</p>" + 
            	"<p>Error Message: " + errorMessage + "</p>" + 
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

                pw.println(y);
                pw.flush();
        	}
        }
        else {
        	// Report certificate time issue
        	String y = "<html>\n" +
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

        	pw.println(y);
            pw.flush();
        }
    }

  public SecurityUtil getSecurityUtil() {
	  if (securityUtil == null) {
		  securityUtil = new SecurityUtil();
	  }
	  return securityUtil;
  }

  SecurityUtil securityUtil;


}
