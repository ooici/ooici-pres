package cilogon;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.URI;

/**
 * <p>Created by Jeff Gaynor<br>
 * on Aug 11, 2010 at  10:11:13 AM
 */
public class FailureServlet extends PortalAbstractServlet {
	private static String CANCEL = "cancel";

    protected void doIt(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable {
        clearCookie(httpServletRequest, httpServletResponse); // clear out old session info
        String reason = (String)httpServletRequest.getParameter("reason");
        if (reason != null && reason.equals(CANCEL)) {
			URI redirectUri = new URI(httpServletRequest.getContextPath() + "/index.html");
			httpServletResponse.sendRedirect(redirectUri.toString());
        }
        else {
        	httpServletResponse.setContentType("text/html");
        	PrintWriter printWriter = httpServletResponse.getWriter();
        	printWriter.println("<html>\n" +
        			"<head><title>Failure</title></head>\n" +
        			"<body><h1>Authentication Failure</h1>" +
        			"<p>There was an error processing your CILogon authentication request.</p>");
        	if (reason != null) {
        		printWriter.println("<p>Reason: " + reason + "</p>");
        	}
        	printWriter.println("<form name=\"input\" action=\"");
        	printWriter.println(httpServletRequest.getContextPath() + "/\" method=\"get\">");
        	printWriter.println("Click to go back to the main page<br><br>\n" +
        			"<input type=\"submit\" value=\"Submit\" />\n" +
        			"</form>\n" +
        			"  </body>\n" +
        	"</html>");
        }
    }
}
