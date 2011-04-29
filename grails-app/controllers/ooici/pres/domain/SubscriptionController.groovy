package ooici.pres.domain

import grails.converters.JSON
import ion.integration.ais.AppIntegrationService;
import ion.integration.ais.AppIntegrationService.RequestType;

class SubscriptionController extends BaseController {
	
	def find = {
		
		preProcessRequest(true);
		
		params.put("user_ooi_id", ooi_id);

		sendReceive(RequestType.FIND_DATA_RESOURCE_SUBSCRIPTION)
	}

	def create = {
		
		preProcessRequest(true);
		
		params.put("user_ooi_id", ooi_id);

		sendReceive(RequestType.CREATE_DATA_RESOURCE_SUBSCRIPTION)
	}
	
	def update = {
		
		preProcessRequest(true);

		sendReceive(RequestType.UPDATE_DATA_RESOURCE_SUBSCRIPTION)
	}
	
	def delete = {
		
		preProcessRequest(true);

		sendReceive(RequestType.DELETE_DATA_RESOURCE_SUBSCRIPTION)
	}
}
