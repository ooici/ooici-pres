package ooici.pres.domain

import grails.converters.JSON
import ion.integration.ais.AppIntegrationService;
import ion.integration.ais.AppIntegrationService.RequestType;

class UserInfoController extends BaseController {

	def find = {
		
		preProcessRequest(true);
		
		params.put("user_ooi_id", ooi_id);

		sendReceive(RequestType.FIND_USER_CONFIG)
	}
	
	def update = {
		
		preProcessRequest(true);
		
		params.put("user_ooi_id", ooi_id);

		sendReceive(RequestType.UPDATE_USER_CONFIG)
	}
}
