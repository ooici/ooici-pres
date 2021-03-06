package ooici.pres.domain

import org.codehaus.groovy.grails.web.json.JSONArray;
import org.codehaus.groovy.grails.web.json.JSONObject;

import grails.converters.JSON
import ion.integration.ais.AppIntegrationService;
import ion.integration.ais.AppIntegrationService.RequestType;

class SubscriptionController extends BaseController {
	
	def find = {
		
		preProcessRequest(true)
		
		specialPreProcessFindRequest()

		params.put("user_ooi_id", ooi_id)
		
		// TODO handle bounds

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
		
		JSONArray subscriptionList = (JSONArray)JSON.parse(params.get("subscriptions"))

		for (int i = 0; i < subscriptionList.length(); i++) {
			HashMap aSubscription = subscriptionList.get(i)
			aSubscription.put("user_ooi_id", ooi_id)
			subscriptionList.put(i, aSubscription)
		}
		
		params.put("subscriptions",subscriptionList)

		sendReceive(RequestType.DELETE_DATA_RESOURCE_SUBSCRIPTION)
	}
	
	def specialPreProcessFindRequest = {

		if (params.get("dataBounds") != null) {
			params.put("dataBounds",JSON.parse(params.get("dataBounds")))
		}
	}

	def specialPreProcessRequest = {
		
		if (params.get("subscriptionInfo") != null) {
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

			params.put("subscriptionInfo",subscriptionInfoJSON)
		}

		if (params.get("datasetMetadata") != null) {
			params.put("datasetMetadata",JSON.parse(params.get("datasetMetadata")))
		}
	}
}
