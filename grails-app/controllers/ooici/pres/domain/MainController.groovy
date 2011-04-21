package ooici.pres.domain



class MainController {
	
	def BootstrapIONService

    def index = {
		String rolesString = "[";
		

		boolean isRole = session.getAttribute(BootstrapIONService.USER_IS_ADMIN_KEY);
		if (isRole) {
			rolesString += BootstrapIONService.ADMIN_ROLE;
		}
		isRole = session.getAttribute(BootstrapIONService.USER_IS_EARY_ADOPTER_KEY);
		if (isRole) {
			rolesString = BootstrapIONService.EARY_ADOPTER_ROLE;
		}
		isRole = session.getAttribute(BootstrapIONService.DATA_PROVIDER_ROLE);
		if (isRole) {
			rolesString = BootstrapIONService.DATA_PROVIDER_ROLE;
		}
		isRole = session.getAttribute(BootstrapIONService.MARINE_OPERATOR_ROLE);
		if (isRole) {
			rolesString = BootstrapIONService.MARINE_OPERATOR_ROLE;
		}

		boolean isRegistered = session.getAttribute(BootstrapIONService.USER_ALREADY_REGISTERED_KEY);
		if (isRegistered) {
			redirect(uri:"dashboard", params:[OOI_ROLES : rolesString, registered: registeredString]);
		}
		else {
			redirect(uri:"dashboard", action:"register", params:[OOI_ROLES : rolesString]);
		}
	}
}
