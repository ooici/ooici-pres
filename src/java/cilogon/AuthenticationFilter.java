package cilogon;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.groovy.grails.commons.ConfigurationHolder;

/**
 * Servlet filter facilitating the redirection to the CILogon authentication
 * flow. For any URL not within the CILogon flow, a comparison is made to
 * see if it is a protected URL.  If so, this filter checks for existence of
 * the special IONCOREOOID cookie.  This cookie is created as the result of a
 * successful authentication via CILogon.  The cookie contains the OOID returned
 * from the ION Core Identity Repository service.  The expiry of the cookie is
 * set to the expiry on the X.509 certificate.
 * 
 * @author tomlennan
 *
 */
public class AuthenticationFilter implements Filter {
	private boolean initialized = false;
	private String[] cilogonignoreurls;
	private String cilogonstarturl;
	private String[] userauthorityfilterurls;
	private String[] adminauthorityfilterurls;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;

		if (!initialized) {
			initialize();
		}

		String path = request.getServletPath();
		// If the URL paths of any of the CILogon delegation servlets change,
		// these values need to be modified in the config file
		if (urlMatches(path, cilogonignoreurls)) {
			chain.doFilter(req, res);
			return;
		}

		// If URL requires authentication
		if ((urlMatches(path, userauthorityfilterurls)) || (urlMatches(path, adminauthorityfilterurls))) {
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
					// Stash the originating URL
					HttpSession session = request.getSession();
					session.setAttribute("IONCOREORIGINIATINGURL", path);

					URI redirectUri = new URI(request.getContextPath() + cilogonstarturl);
					response.sendRedirect(redirectUri.toString());
				}
				catch(Exception e) {

				}
			}
			else {
				chain.doFilter(req, res);
			}
		}
		else {
			chain.doFilter(req, res);
		}
	}

	private boolean urlMatches(String url, String[] filterurls) {
		for (int i = 0; i < filterurls.length; i++) {
			String filterurl = filterurls[i];

			// Check for relative match
			if (filterurl.endsWith("*")) {
				if (url.startsWith(filterurl.substring(0, filterurl.length() - 2))) {
					return true;
				}
			}
			// Check for exact match
			else {
				if (url.equals(filterurl)) {
					return true;
				}
			}
		}
		return false;
	}

	private void initialize() {
		Map map = ConfigurationHolder.getConfig().flatten();

		String prop = (String)map.get("ioncore.cilogonignoreurls");
		cilogonignoreurls = prop.split(" ");

		cilogonstarturl = (String)map.get("ioncore.cilogonstarturl");

		prop = (String)map.get("ioncore.userauthorityfilterurls");
		userauthorityfilterurls = prop.split(" ");

		prop = (String)map.get("ioncore.adminauthorityfilterurls");
		adminauthorityfilterurls = prop.split(" ");

		initialized = true;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}

}
