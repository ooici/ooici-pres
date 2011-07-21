package cilogon;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * <p>Created by Jeff Gaynor<br>
 * on Aug 11, 2010 at  10:11:13 AM
 */
public class FailureServlet extends PortalAbstractServlet {
    protected void doIt(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable {
        clearCookie(httpServletRequest, httpServletResponse); // clear out old session info
        httpServletResponse.setContentType("text/html");
        PrintWriter printWriter = httpServletResponse.getWriter();
        printWriter.println("<html>\n" +
                "<head><title>Failure</title></head>\n" +
                "<body><h1>Authentication Failure</h1>" +
                "<p>There was an error processing your CILogon authentication request.</p>" + 
                "<form name=\"input\" action=\"");
        printWriter.println(httpServletRequest.getContextPath() + "/\" method=\"get\">");
        printWriter.println("Click to go back to the main page<br><br>\n" +
                "<input type=\"submit\" value=\"Submit\" />\n" +
                "</form>\n" +
                "  </body>\n" +
                "</html>");
    }
}
