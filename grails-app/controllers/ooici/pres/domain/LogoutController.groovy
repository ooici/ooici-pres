package ooici.pres.domain

class LogoutController {

	String OOI_ID_KEY = "IONCOREOOIID"
	String EXPIRY_KEY = "IONCOREEXPIRY"
	String USER_IS_ADMIN_KEY = "user_is_admin"
	String USER_IS_EARY_ADOPTER_KEY = "user_is_early_adopter"
	String USER_IS_DATA_PROVIDER_KEY = "user_is_data_provider"
	String USER_IS_MARINE_OPERATOR_KEY = "user_is_marine_operator"
	String USER_ALREADY_REGISTERED_KEY = "user_already_registered"

	/**
	 * Index action. Zap the user id and expiry from the session
	 */
	def index = {
		session.removeAttribute(OOI_ID_KEY)
		session.removeAttribute(EXPIRY_KEY)
		session.removeAttribute(USER_IS_ADMIN_KEY)
		session.removeAttribute(USER_IS_EARY_ADOPTER_KEY)
		session.removeAttribute(USER_IS_DATA_PROVIDER_KEY)
		session.removeAttribute(USER_IS_MARINE_OPERATOR_KEY)
		session.removeAttribute(USER_ALREADY_REGISTERED_KEY)

		redirect(uri:"/index.html")
	}
	
}
