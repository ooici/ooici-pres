package ooici.pres.domain

import org.codehaus.groovy.grails.commons.ConfigurationHolder;

import ooici.pres.BootstrapIONService
import grails.converters.JSON

class DashboardController {
	
	def BootstrapIONService

	def index = {

		// Constants
		String OOI_ID_KEY = "IONCOREOOIID"
		String EXPIRY_KEY = "IONCOREEXPIRY"
		String USER_IS_ADMIN_KEY = "user_is_admin"
		String USER_IS_EARY_ADOPTER_KEY = "user_is_early_adopter"
		String USER_IS_DATA_PROVIDER_KEY = "user_is_data_provider"
		String USER_IS_MARINE_OPERATOR_KEY = "user_is_marine_operator"
		String USER_ALREADY_REGISTERED_KEY = "user_already_registered"

		String USER_ROLE = "USER"
		String ADMIN_ROLE = "ADMIN"
		String EARY_ADOPTER_ROLE = "EARLY_ADOPTER"
		String DATA_PROVIDER_ROLE = "DATA_PROVIDER"
		String MARINE_OPERATOR_ROLE = "MARINE_OPERATOR"
	
		ArrayList<String> roles = new ArrayList<String>()
		String rolesString;

		String ooi_id = session.getAttribute(OOI_ID_KEY)
		boolean isSignedIn = false
		if (ooi_id == null || ooi_id.equals("")) {
			// Force OOI ID to ANONYMOUS
			session.setAttribute(OOI_ID_KEY, "ANONYMOUS")
			session.setAttribute(EXPIRY_KEY, "0")
		}
		else {
			if (!ooi_id.equals("ANONYMOUS")) {
				isSignedIn = true;
			}
		}

		boolean userIsAdmin = session.getAttribute(USER_IS_ADMIN_KEY)
		boolean userIsEarlyAdopter = session.getAttribute(USER_IS_EARY_ADOPTER_KEY)
		boolean userIsDataProvider = session.getAttribute(DATA_PROVIDER_ROLE)
		boolean userIsMarineOperator = session.getAttribute(MARINE_OPERATOR_ROLE)
		boolean isRegistered = session.getAttribute(USER_ALREADY_REGISTERED_KEY)

		if (isSignedIn) {
			roles.add(USER_ROLE)
		}
		if (userIsAdmin) {
			roles.add(ADMIN_ROLE)
		}
		if (userIsEarlyAdopter) {
			roles.add(EARY_ADOPTER_ROLE)
		}
		if (userIsDataProvider) {
			roles.add(DATA_PROVIDER_ROLE)
		}
		if (userIsMarineOperator) {
			roles.add(MARINE_OPERATOR_ROLE)
		}
		rolesString = roles.encodeAsJSON()

		String isRegisteredString = isRegistered ? "true" : "false"

		Map map = ConfigurationHolder.getConfig().flatten()
		
		def instrument_monitor_url = (String)map.get("ioncore.instrument_monitor_url")
		
		render(view:"show",model:[OOI_ROLES: rolesString, REGISTERED: isRegisteredString, INSTRUMENT_MONITOR_URL: instrument_monitor_url, HELP: BootstrapIONService.helpStrings])
	}
}