package ooici.pres.domain

import grails.converters.JSON
import ion.integration.ais.AppIntegrationService;
import ion.integration.ais.AppIntegrationService.RequestType;

class UserProfileController extends BaseController {
	final String USER_ALREADY_REGISTERED_KEY = "user_already_registered"
	
	def defaultAction = "get"

	def get = {
		
		preProcessRequest(true)
		
		params.put("user_ooi_id", ooi_id)

		sendReceive(RequestType.GET_USER_PROFILE)
	}
	
	def update = {
		
		preProcessRequest(true)
		
		params.put("user_ooi_id", ooi_id)
		
		params.put("profile",JSON.parse(params.get("profile")))

		session.setAttribute(USER_ALREADY_REGISTERED_KEY,true)
		
		sendReceive(RequestType.UPDATE_USER_PROFILE)
	}
}
