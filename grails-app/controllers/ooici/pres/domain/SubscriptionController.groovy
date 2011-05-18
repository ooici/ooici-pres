package ooici.pres.domain

import grails.converters.JSON
import ion.integration.ais.AppIntegrationService;
import ion.integration.ais.AppIntegrationService.RequestType;

class SubscriptionController extends BaseController {
	
	def find = {
		
		preProcessRequest(true)
		
		params.put("user_ooi_id", ooi_id)

		sendReceive(RequestType.FIND_DATA_RESOURCE_SUBSCRIPTION)
	}

	def create = {
		
		preProcessRequest(true)
		
		specialPreProcessRequest()

		sendReceive(RequestType.CREATE_DATA_RESOURCE_SUBSCRIPTION)
	}
	
	def update = {
		
		preProcessRequest(true)
		
		specialPreProcessRequest()

		sendReceive(RequestType.UPDATE_DATA_RESOURCE_SUBSCRIPTION)
	}
	
	def delete = {
		
		preProcessRequest(true)
		
		params.put("subscriptionInfo",JSON.parse(params.get("subscriptionInfo")))
		
		def subscriptionInfoJSON = params.get("subscriptionInfo")
		
		subscriptionInfoJSON.put("user_ooi_id", ooi_id)

		params.put("subscriptionInfo",subscriptionInfoJSON)

		sendReceive(RequestType.DELETE_DATA_RESOURCE_SUBSCRIPTION)
	}
	
	def specialPreProcessRequest = {
		
		params.put("subscriptionInfo",JSON.parse(params.get("subscriptionInfo")))
		
		def subscriptionInfoJSON = params.get("subscriptionInfo")
		
		subscriptionInfoJSON.put("user_ooi_id", ooi_id)

		def dispatcher_alerts_filter = subscriptionInfoJSON.get("dispatcher_alerts_filter")
		if (dispatcher_alerts_filter == "") {
			subscriptionInfoJSON.remove("dispatcher_alerts_filter")
		}

		def email_alerts_filter = subscriptionInfoJSON.get("email_alerts_filter")
		if (email_alerts_filter == "") {
			subscriptionInfoJSON.remove("email_alerts_filter")
		}
		
		params.put("datasetMetadata",JSON.parse(params.get("datasetMetadata")))

		params.put("subscriptionInfo",subscriptionInfoJSON)
	}
}
