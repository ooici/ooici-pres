package ooici.pres.domain

class LogoutController {

	/**
	 * Index action. Zap the user id and expiry from the session
	 */
	def index = {
		session.removeAttribute("IONCOREOOID")
		session.removeAttribute("IONCOREEXPIRY")

		redirect(uri:"/")
	}
	
}
