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
 * the special IONCOREOOIID session attribute.  This attribute is created on
 * successful authentication via CILogon.  The attribute contains the OOI_ID returned
 * from the ION Core Identity Repository service.  The expiry of the certificate
 * is also stored in the session.
 *  
 * @author tomlennan
 *
 */
public class AuthenticationFilter implements Filter {
	// Constants
	final String OOI_ID_KEY = "IONCOREOOIID";
	final String EXPIRY_KEY = "IONCOREEXPIRY";
	final String USER_ALREADY_REGISTERED_KEY = "user_already_registered";


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
		}

		// If the URL paths of any of the CILogon delegation servlets change,
		// these values need to be modified in the config file
		if (urlMatches(path, cilogonignoreurls)) {
			chain.doFilter(req, res);
			return;
		}

		switch(testMode) {
		case FORCE:
			boolean ooiidFound = session.getAttribute(OOI_ID_KEY) != null;
			boolean expiryFound = session.getAttribute(EXPIRY_KEY) != null;

			if (!ooiidFound || !expiryFound) {
				testModeAuthenticate(request, response);
			}
			chain.doFilter(req, res);
			break;

		case URL:
			// If URL based test mode authentication is enabled, check URL for trigger value
			Map params = request.getParameterMap();
			
			if (params.containsKey("Test")) {
				ooiidFound = session.getAttribute(OOI_ID_KEY) != null;
				expiryFound = session.getAttribute(EXPIRY_KEY) != null;
				
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
				ooiidFound = session.getAttribute(OOI_ID_KEY) != null;
				expiryFound = session.getAttribute(EXPIRY_KEY) != null;
				
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
		if (prop != null) {
			cilogonignoreurls = prop.split(" ");
		}

		cilogonstarturl = (String)map.get("ioncore.cilogonstarturl");
		if (cilogonstarturl == null) {
			cilogonstarturl = "/StartRequest";
		}

		prop = (String)map.get("ioncore.userauthorityfilterurls");
		if (prop != null) {
			userauthorityfilterurls = prop.split(" ");
		}

		prop = (String)map.get("ioncore.adminauthorityfilterurls");
		if (prop != null) {
			adminauthorityfilterurls = prop.split(" ");
		}

		String testmodeStr = (String)map.get("ioncore.testmode");

		if (testmodeStr != null && (testmodeStr.equalsIgnoreCase("force") || testmodeStr.equalsIgnoreCase("url"))) {
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

		int expiry = 43200;
		if (expiryStr != null) {
			try {
				expiry = new Integer(expiryStr);
			}
			catch(NumberFormatException e) {
				// ignore
			}
		}

		long currentDateMS = System.currentTimeMillis();

		session.setAttribute(OOI_ID_KEY, ooi_id);
		session.setAttribute(EXPIRY_KEY, "" + (currentDateMS/1000 + expiry));
		session.setAttribute(USER_ALREADY_REGISTERED_KEY,true);

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
