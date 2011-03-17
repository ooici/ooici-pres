package cilogon;

import java.io.IOException;
import java.net.URI;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet filter facilitating the redirection to the CILogon authentication flow.
 * For any url not within the CILogon flow, this filter checks for existence of
 * the special IONCOREOOID cookie.  This cookie is created as the result of a
 * successful authentication via CILogon.  The cookie contains the OOID returned
 * from the ION Core Identity Repository service.  The expiry of the cookie was
 * set to the expiry on the X.509 certificate.
 * 
 * @author tomlennan
 *
 */
public class AuthenticationFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		
		String path = request.getServletPath();
		// TODO if the URL paths of any of the CILogon delegation servlets change,
		// these values need to be modified to match the updated path
		if (path.equals("/StartRequest") || path.equals("/ready") || path.equals("/SuccessServlet")) {
			chain.doFilter(req, res);
			return;
		}

		// See if our cookie exists.  If not, we'll delegate to CILogon
		Cookie[] cookies = request.getCookies();
		boolean found = false;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("IONCOREOOID")) {
					found = true;
					break;
				}
			}
		}

		if (!found) {
	        try {
	        	URI redirectUri = new URI(request.getContextPath() + "/StartRequest");
	        	response.sendRedirect(redirectUri.toString());
	        }
	        catch(Exception e) {
	        	
	        }
		}
		else {
			chain.doFilter(req, res);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
