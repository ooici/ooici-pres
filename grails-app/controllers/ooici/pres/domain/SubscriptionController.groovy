package ooici.pres.domain

import grails.converters.JSON
import ion.integration.ais.AppIntegrationService;
import ion.integration.ais.AppIntegrationService.RequestType;

class SubscriptionController extends BaseController {

	def create = {
		
		preProcessRequest(true);

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
