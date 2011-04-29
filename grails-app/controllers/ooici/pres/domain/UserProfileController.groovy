package ooici.pres.domain

import grails.converters.JSON
import ion.integration.ais.AppIntegrationService;
import ion.integration.ais.AppIntegrationService.RequestType;

class UserProfileController extends BaseController {

	def find = {
		
		preProcessRequest(true);
		
		params.put("user_ooi_id", ooi_id);

		sendReceive(RequestType.GET_USER_PROFILE)
	}
	
	def update = {
		
		preProcessRequest(true);
		
		params.put("user_ooi_id", ooi_id);

		sendReceive(RequestType.UPDATE_USER_PROFILE)
	}
}
