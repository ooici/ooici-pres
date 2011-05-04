package ooici.pres.domain

class DashboardController {

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
	
		String rolesString = "["

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
			rolesString += USER_ROLE
		}
		if (userIsAdmin) {
			if (isSignedIn) {
				rolesString += ", "
			}
			rolesString += ADMIN_ROLE
		}
		if (userIsEarlyAdopter) {
			if (isSignedIn || userIsAdmin) {
				rolesString += ", "
			}
			rolesString += EARY_ADOPTER_ROLE
		}
		if (userIsDataProvider) {
			if (isSignedIn || userIsAdmin || userIsEarlyAdopter) {
				rolesString += ", "
			}
			rolesString = DATA_PROVIDER_ROLE
		}
		if (userIsMarineOperator) {
			if (isSignedIn || userIsAdmin || userIsEarlyAdopter || userIsDataProvider) {
				rolesString += ", "
			}
			rolesString += MARINE_OPERATOR_ROLE
		}
		rolesString += "]"

		render(view:"show",model:[OOI_ROLES: rolesString])
	}
}