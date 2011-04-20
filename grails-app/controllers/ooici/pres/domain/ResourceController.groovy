package ooici.pres.domain

import grails.converters.JSON
import ion.integration.ais.AppIntegrationService;
import ion.integration.ais.AppIntegrationService.RequestType;

class ResourceController extends BaseController {

	def types = {
		
		preProcessRequest(true);

		sendReceive(RequestType.GET_RESOURCE_TYPES)
	}
	
	def resourceType = {
		
		preProcessRequest(true);

		sendReceive(RequestType.GET_RESOURCES_OF_TYPE)
	}
	
	def detail = {
		
		preProcessRequest(true);

		sendReceive(RequestType.GET_RESOURCE)
	}
}
