package cilogon;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * <p>Created by Jeff Gaynor<br>
 * on Aug 11, 2010 at  10:15:29 AM
 */
public class WelcomeServlet extends PortalAbstractServlet {
	
    protected void doIt(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable {
        PrintWriter writer = httpServletResponse.getWriter();
        httpServletResponse.setContentType("text/html");
        writer.println("<html><head><title>Sample Java Delegation Portal</title></head>");
        writer.println(" <body><H1>A Sample Java Delegation Portal</h1>");
        // Next line is the important one. Just set the context path and point this to the the startRequest servlet
        writer.println("<form name=\"input\" action=\"" + httpServletRequest.getContextPath() + "/startRequest\" method=\"get\">");
        writer.println("Click to request a credential<br><br><input type=\"submit\" value=\"Submit\" />");
        writer.println("</form></body></html>");
    }
}
