package cilogon;

import grails.converters.JSON;
import ion.integration.ais.AppIntegrationService.RequestType;

import java.net.URI;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.servlet.ServletException;
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
	// Constants
	final String OOI_ID_KEY = "IONCOREOOIID";
	final String EXPIRY_KEY = "IONCOREEXPIRY";
	final String USER_ID_KEY = "ooi_id";
	final String USER_IS_ADMIN_KEY = "user_is_admin";
	final String USER_IS_EARY_ADOPTER_KEY = "user_is_early_adopter";
	final String USER_IS_DATA_PROVIDER_KEY = "user_is_data_provider";
	final String USER_IS_MARINE_OPERATOR_KEY = "user_is_marine_operator";
	final String USER_ALREADY_REGISTERED_KEY = "user_already_registered";

	protected void doIt(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable {

		httpServletResponse.setContentType("text/html");

		String identifier = clearCookie(httpServletRequest, httpServletResponse);
		if (identifier == null) {
			throw new ServletException("Error: No identifier for this delegation request was found. ");
		}
		CILogonService cis = new CILogonService(getPortalEnvironment());
		PortalCredentials credential = cis.getCredential(identifier);

		X509Certificate certificate = credential.getX509Certificate();

		long expirationDateMS = certificate.getNotAfter().getTime();
		long currentDateMS = System.currentTimeMillis();

		// For debugging purposes
		Principal principal = certificate.getSubjectDN();
		String subjectDN = principal.getName();

		System.out.println("SuccessServlet: received authentication request for <" + subjectDN + ">");

		// Ensure certificate is currently valid
		// TODO worry about the start date?
		if (currentDateMS < expirationDateMS) {
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
			
			System.out.println("Request string for register user: " + request);

			String result = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(request, RequestType.REGISTER_USER, "ANONYMOUS", "0");

			if (result != null && result.length() > 0) {
				HttpSession session = httpServletRequest.getSession(true);

				String ooi_id = "";
				boolean userIsAdmin = false;
				boolean userIsEarlyAdopter = false;
				boolean userIsDataProvider = false;
				boolean userIsMarineOperator = false;
				boolean userIsAlreadyRegistered = false;

				try {
					// Extract values from Json result
					JSONObject valueMap = (JSONObject)JSON.parse(result);

					ooi_id = (String)valueMap.get(USER_ID_KEY);
					userIsAdmin = (Boolean)valueMap.get(USER_IS_ADMIN_KEY);
					userIsEarlyAdopter = (Boolean)valueMap.get(USER_IS_EARY_ADOPTER_KEY);
					userIsDataProvider = (Boolean)valueMap.get(USER_IS_DATA_PROVIDER_KEY);
					userIsMarineOperator = (Boolean)valueMap.get(USER_IS_MARINE_OPERATOR_KEY);
					userIsAlreadyRegistered = (Boolean)valueMap.get(USER_ALREADY_REGISTERED_KEY);
				}
				catch (Exception e) {
					System.out.println("Exception extracting ooi id or user roles from user registration response");
				}

				if (ooi_id.length() != 0) {
					session.setAttribute(OOI_ID_KEY, ooi_id);
					session.setAttribute(EXPIRY_KEY, "" + expirationDateMS/1000);
				}
				System.out.println("SuccessServlet: OOI ID for <" + subjectDN + ">: " + ooi_id);

				String authorityRole = "ROLE_USER";
				if (userIsAdmin) {
					authorityRole = "ROLE_ADMIN";
					session.setAttribute(USER_IS_ADMIN_KEY, true);
					System.out.println("SuccessServlet: <" + subjectDN + "> isAdmin: True");
				}
				else {
					session.setAttribute(USER_IS_ADMIN_KEY, false);
					System.out.println("SuccessServlet: <" + subjectDN + "> isAdmin: False");
				}

				if (userIsEarlyAdopter) {
					session.setAttribute(USER_IS_EARY_ADOPTER_KEY, true);
					System.out.println("SuccessServlet: <" + subjectDN + "> isEarlyAdopter: True");
				}
				else {
					session.setAttribute(USER_IS_EARY_ADOPTER_KEY, false);
					System.out.println("SuccessServlet: <" + subjectDN + "> isEarlyAdopter: False");
				}

				if (userIsDataProvider) {
					session.setAttribute(USER_IS_DATA_PROVIDER_KEY, true);
					System.out.println("SuccessServlet: <" + subjectDN + "> isDataProvider: True");
				}
				else {
					session.setAttribute(USER_IS_DATA_PROVIDER_KEY, false);
					System.out.println("SuccessServlet: <" + subjectDN + "> isDataProvider: False");
				}

				if (userIsMarineOperator) {
					session.setAttribute(USER_IS_MARINE_OPERATOR_KEY, true);
					System.out.println("SuccessServlet: <" + subjectDN + "> isMarineOperator: True");
				}
				else {
					session.setAttribute(USER_IS_MARINE_OPERATOR_KEY, false);
					System.out.println("SuccessServlet: <" + subjectDN + "> isMarineOperator: False");
				}

				if (userIsAlreadyRegistered) {
					session.setAttribute(USER_ALREADY_REGISTERED_KEY, true);
					System.out.println("SuccessServlet: <" + subjectDN + "> isAlreadyRegistered: True");
				}
				else {
					session.setAttribute(USER_ALREADY_REGISTERED_KEY, false);
					System.out.println("SuccessServlet: <" + subjectDN + "> isAlreadyRegistered: False");
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

				URI redirectUri = new URI(httpServletRequest.getContextPath() + mainUrl);
				httpServletResponse.sendRedirect(redirectUri.toString());
			}
			else {
				int status = BootstrapIONService.appIntegrationService.getStatus();
				String errorMessage = BootstrapIONService.appIntegrationService.getErrorMessage();

				System.out.println("SuccessServlet: Error received logging in <" + subjectDN + ">\nRequest message: " + request + "\nStatus: " + status + "\nError message: " + errorMessage);

				URI redirectUri = new URI(httpServletRequest.getContextPath() + "/index.html");
				httpServletResponse.sendRedirect(redirectUri.toString());
			}
		}
		else {
			System.out.println("SuccessServlet: Received certificate for <" + subjectDN + "> has invalid time value");
			System.out.println("SuccessServlet: current time MS       <" + currentDateMS + ">");
			System.out.println("SuccessServlet: certificate expiry MS <" + expirationDateMS + ">");

			URI redirectUri = new URI(httpServletRequest.getContextPath() + "/index.html");
			httpServletResponse.sendRedirect(redirectUri.toString());
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
