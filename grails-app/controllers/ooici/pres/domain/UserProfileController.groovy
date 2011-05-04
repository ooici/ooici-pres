package ooici.pres.domain

import grails.converters.JSON
import ion.integration.ais.AppIntegrationService;
import ion.integration.ais.AppIntegrationService.RequestType;

class UserProfileController extends BaseController {
	
	def defaultAction = "get"

	def get = {
		
		preProcessRequest(true)
		
		params.put("user_ooi_id", ooi_id)

		sendReceive(RequestType.GET_USER_PROFILE)
	}
	
	def update = {
		
		preProcessRequest(true)
		
		params.put("user_ooi_id", ooi_id)
//		
//		String profileStr = params.get("profile")
//		
//		def parsedProfileStr = JSON.parse(profileStr)
		
		params.put("profile",JSON.parse(params.get("profile")))

		sendReceive(RequestType.UPDATE_USER_PROFILE)
	}
}
