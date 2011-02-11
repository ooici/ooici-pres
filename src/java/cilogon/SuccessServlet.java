package cilogon;

import ooici.pres.BootstrapIONService;

import org.cilogon.portal.CILogonService;
import org.cilogon.portal.util.PortalCredentials;
import org.cilogon.util.SecurityUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

import ion.core.messaging.IonMessage;
import ion.core.messaging.MessagingName;

import javax.security.auth.x500.X500Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Created by Jeff Gaynor<br>
 * on Jul 31, 2010 at  3:29:09 PM
 */
public class SuccessServlet extends PortalAbstractServlet {
    protected void doIt(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable {
        String SYSNAME = System.getProperty("ioncore.sysname","spasco");
        MessagingName identRegSvc = new MessagingName(SYSNAME, "identity_service");

        String identifier = clearCookie(httpServletRequest, httpServletResponse);
        if (identifier == null) {
            throw new ServletException("Error: No identifier for this delegation request was found. ");
        }
        CILogonService cis = new CILogonService(getPortalEnvironment());
        PortalCredentials credential = cis.getCredential(identifier);

        // Authenticate user
        Map<String, Object> content = new HashMap<String, Object>();
        content.put("user_cert", getSecurityUtil().toPEM(credential.getX509Certificate()));
        content.put("user_private_key", getSecurityUtil().toPEM(credential.getPrivateKey()));

        // Create and send request message
        IonMessage msgin = BootstrapIONService.baseProcess.rpcSend(identRegSvc, "register_user_credentials", content);
        assert(!msgin.isErrorMessage());
        Object ooid = msgin.getContent();
        BootstrapIONService.baseProcess.ackMessage(msgin);

        // Programmatically add credential for principal
        X500Principal principal = credential.getX509Certificate().getSubjectX500Principal();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                      principal, new GrantedAuthority[] { new GrantedAuthorityImpl("ROLE_ADMIN") });
        SecurityContextHolder.getContext().setAuthentication(token);

        httpServletResponse.setContentType("text/html");
        PrintWriter pw = httpServletResponse.getWriter();
        /* Put the key and certificate in the result, but allow them to be initially hidden. */
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
