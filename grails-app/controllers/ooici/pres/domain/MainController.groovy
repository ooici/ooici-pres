package ooici.pres.domain

class MainController {

	def index = {

		// Constants
		String OOI_ID_KEY = "IONCOREOOIID"
		String EXPIRY_KEY = "IONCOREEXPIRY"
		String USER_IS_ADMIN_KEY = "user_is_admin";
		String USER_IS_EARY_ADOPTER_KEY = "user_is_early_adopter"
		String USER_IS_DATA_PROVIDER_KEY = "user_is_data_provider"
		String USER_IS_MARINE_OPERATOR_KEY = "user_is_marine_operator"
		String USER_ALREADY_REGISTERED_KEY = "user_already_registered"

		String ADMIN_ROLE = "ADMIN"
		String EARY_ADOPTER_ROLE = "EARLY_ADOPTER"
		String DATA_PROVIDER_ROLE = "DATA_PROVIDER"
		String MARINE_OPERATOR_ROLE = "MARINE_OPERATOR"
	
		String rolesString = "[";

		boolean userIsAdmin = session.getAttribute(USER_IS_ADMIN_KEY)
		boolean userIsEarlyAdopter = session.getAttribute(USER_IS_EARY_ADOPTER_KEY)
		boolean userIsDataProvider = session.getAttribute(DATA_PROVIDER_ROLE)
		boolean userIsMarineOperator = session.getAttribute(MARINE_OPERATOR_ROLE)
		boolean isRegistered = session.getAttribute(USER_ALREADY_REGISTERED_KEY)

		if (userIsAdmin) {
			rolesString += ADMIN_ROLE
		}
		if (userIsEarlyAdopter) {
			if (userIsAdmin) {
				rolesString += ", "
			}
			rolesString += EARY_ADOPTER_ROLE
		}
		if (userIsDataProvider) {
			if (userIsAdmin || userIsEarlyAdopter) {
				rolesString += ", "
			}
			rolesString = DATA_PROVIDER_ROLE
		}
		if (userIsMarineOperator) {
			if (userIsAdmin || userIsEarlyAdopter || userIsDataProvider) {
				rolesString += ", "
			}
			rolesString += MARINE_OPERATOR_ROLE
		}
		rolesString += "]"

		if (isRegistered) {
			redirect(uri:"/dashboard.gsp", params:[OOI_ROLES : rolesString])
		}
		else {
			redirect(uri:"/dashboard.gsp?register", params:[OOI_ROLES : rolesString])
		}
	}
}