package ooici.pres;

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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * Servlet filter facilitating the redirection to the CILogon authentication
 * flow. For any URL not within the CILogon flow, a comparison is made to
 * see if it is a protected URL.  If so, this filter checks for existence of
 * the special IONCOREOOIID cookie.  This cookie is created as the result of a
 * successful authentication via CILogon.  The cookie contains the OOI_ID returned
 * from the ION Core Identity Repository service.  The expiry of the cookie was
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
	private String ooi_id;
	private String roleStr;
	private String expiryStr;
	private TestMode testMode = TestMode.DISABLED;

	private enum TestMode { DISABLED, FORCE, URL };

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		HttpSession session = request.getSession(true);

		if (!initialized) {
			initialize();
		}
		
		String path = request.getServletPath();
		if (testMode != TestMode.DISABLED) {
//			System.out.println("In AuthenticationFilter.doFilter.  Request Path: " + path);
		}

		// If the URL paths of any of the CILogon delegation servlets change,
		// these values need to be modified in the config file
		if (urlMatches(path, cilogonignoreurls)) {
			chain.doFilter(req, res);
			return;
		}

		switch(testMode) {
		case FORCE:
//			System.out.println("In AuthenticationFilter.doFilter.  Forcing authentication");
			boolean ooiidFound = session.getAttribute("IONCOREOOIID") != null;
			boolean expiryFound = session.getAttribute("IONCOREEXPIRY") != null;
//			Cookie[] cookies = request.getCookies();
//			boolean ooiidFound = false;
//			boolean expiryFound = false;
//			if (cookies != null) {
//				for (int i = 0; i < cookies.length; i++) {
//					if (cookies[i].getName().equals("IONCOREOOIID")) {
//						ooiidFound = true;
//					}
//					if (cookies[i].getName().equals("IONCOREEXPIRY")) {
//						expiryFound = true;
//					}
//				}
//			}
			if (!ooiidFound || !expiryFound) {
				testModeAuthenticate(request, response);
			}
			chain.doFilter(req, res);
			break;

		case URL:
			// If URL based test mode authentication is enabled, check URL for trigger value
			Map params = request.getParameterMap();
			if (params.containsKey("Test")) {
//				System.out.println("In AuthenticationFilter.doFilter.  Setting authentication due to param match");
				ooiidFound = session.getAttribute("IONCOREOOIID") != null;
				expiryFound = session.getAttribute("IONCOREEXPIRY") != null;
//				cookies = request.getCookies();
//				ooiidFound = false;
//				expiryFound = false;
//				if (cookies != null) {
//					for (int i = 0; i < cookies.length; i++) {
//						if (cookies[i].getName().equals("IONCOREOOIID")) {
//							ooiidFound = true;
//						}
//						if (cookies[i].getName().equals("IONCOREEXPIRY")) {
//							expiryFound = true;
//						}
//					}
//				}
				if (!ooiidFound || !expiryFound) {
					testModeAuthenticate(request, response);

					try {
						// Redirect user to URL they requested
						URI redirectUri = new URI(request.getContextPath() + path);
						response.sendRedirect(redirectUri.toString());
						return;
					}
					catch(Exception e) {
						// TODO report error
					}
				}
			}
			// Fall through to delegate the CILogon

		default:
			// If URL requires authentication
			if ((urlMatches(path, userauthorityfilterurls)) || (urlMatches(path, adminauthorityfilterurls))) {
				// See if our cookie exists.  If not, we'll delegate to CILogon
				ooiidFound = session.getAttribute("IONCOREOOIID") != null;
				expiryFound = session.getAttribute("IONCOREEXPIRY") != null;
//				cookies = request.getCookies();
//				ooiidFound = false;
//				expiryFound = false;
//				if (cookies != null) {
//					for (int i = 0; i < cookies.length; i++) {
//						if (cookies[i].getName().equals("IONCOREOOIID")) {
//							ooiidFound = true;
//						}
//						if (cookies[i].getName().equals("IONCOREEXPIRY")) {
//							expiryFound = true;
//						}
//					}
//				}
				if (!ooiidFound || !expiryFound) {
					try {
						// Stash the originating URL
						session.setAttribute("IONCOREORIGINIATINGURL", path);

						URI redirectUri = new URI(request.getContextPath() + cilogonstarturl);
						response.sendRedirect(redirectUri.toString());
					}
					catch(Exception e) {
						// TODO report error
					}
				}
				else {
					chain.doFilter(req, res);
				}
			}
			else {
				chain.doFilter(req, res);
			}
			break;
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

		String testmodeStr = (String)map.get("ioncore.testmode");

		if (testmodeStr != null && testmodeStr.equalsIgnoreCase("force") || testmodeStr.equalsIgnoreCase("url")) {
			if (testmodeStr.equalsIgnoreCase("force")) {
				testMode = TestMode.FORCE;
			}
			else {
				testMode = TestMode.URL;
			}
			ooi_id = (String)map.get("ioncore.testmode.ooiid");
			if (ooi_id == null) {
				// Configuration error, reset test mode to DISABLED
				testMode = TestMode.DISABLED;
			}
			else {
				roleStr = (String)map.get("ioncore.testmode.authorityrole");
				expiryStr = (String)map.get("ioncore.testmode.expiry");
			}
		}

		initialized = true;
	}

	private void testModeAuthenticate(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);

		// Set cookie with max age equal to certificate expiration time
		Cookie cookie = new Cookie("IONCOREOOIID", ooi_id);

		int expiry = 43200;
		if (expiryStr != null) {
			try {
				expiry = new Integer(expiryStr);
			}
			catch(NumberFormatException e) {
				// ignore
			}
		}
		cookie.setMaxAge(expiry);
		response.addCookie(cookie);

		long currentDateMS = System.currentTimeMillis();
		cookie = new Cookie("IONCOREEXPIRY", "" + (currentDateMS/1000 + expiry));
		cookie.setMaxAge(expiry);
		response.addCookie(cookie);

		session.setAttribute("IONCOREOOIID", ooi_id);
		session.setAttribute("IONCOREEXPIRY", "" + (currentDateMS/1000 + expiry));

		// Programmatically add credential for principal (OOI_ID)
		String authorityRole = "ROLE_USER";
		if (roleStr != null && roleStr.equalsIgnoreCase("admin")) {
			authorityRole = "ROLE_ADMIN";
		}
		PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(
				ooi_id, new GrantedAuthority[] { new GrantedAuthorityImpl(authorityRole) });
		token.setAuthenticated(true);
		SecurityContextHolder.getContext().setAuthentication(token);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}

}
