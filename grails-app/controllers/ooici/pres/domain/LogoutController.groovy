package ooici.pres.domain

class LogoutController {

	/**
	 * Index action. Zap the user id and expiry from the session
	 */
	def index = {
		session.removeAttribute("IONCOREOOID")
		session.removeAttribute("IONCOREEXPIRY")
		session.removeAttribute("user_is_admin")
		session.removeAttribute("user_is_early_adopter")
		session.removeAttribute("user_is_data_provider")
		session.removeAttribute("user_is_marine_operator")
		session.removeAttribute("user_already_registered")

		redirect(uri:"/")
	}
	
}
