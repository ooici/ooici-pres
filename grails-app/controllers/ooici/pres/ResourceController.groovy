package ooici.pres

import javax.servlet.http.Cookie;

import grails.converters.JSON
import ion.integration.ais.AppIntegrationService;
import ion.integration.ais.AppIntegrationService.RequestType;

class ResourceController {

	def BootstrapIONService

	def ooi_id = null
	def expiry = null
	
	def setupForSendReceive(boolean redirectOnUnauthenticated) {
		
		System.out.println("Request params: " + params);
		
		BootstrapIONService.getOoiIdAndExpiry(request, ooi_id, expiry);

		if (redirectOnUnauthenticated) {
			if (ooi_id == null || ooi_id.equals("ANONYMOUS")) {
				response.setStatus(401);
				redirect(uri:"/");
				return
			}
		}

		// Remove non-user data specific params
		params.remove("action");
		params.remove("controller");
	}
	
	def types = {
		
		setupForSendReceive(true);

		def requestString = new JSON(params).toString()
		
		int status = 200;		
		String responseString = BootstrapIONService.sendReceive(RequestType.GET_RESOURCE_TYPES, requestString, ooi_id, expiry, status)
		
		response.setStatus(status);

		def jsonArray = JSON.parse(responseString)
		render jsonArray as JSON
	}
	
	def resourceType = {
		
		setupForSendReceive(true);

		def requestString = new JSON(params).toString()
		
		int status = 200;		
		String responseString = BootstrapIONService.sendReceive(RequestType.GET_RESOURCES_OF_TYPE, requestString, ooi_id, expiry, status)
		
		response.setStatus(status);

		def jsonArray = JSON.parse(responseString)
		render jsonArray as JSON
	}
	
	def detail = {
		
		setupForSendReceive(true);

		def requestString = new JSON(params).toString()
		
		int status = 200;		
		String responseString = BootstrapIONService.sendReceive(RequestType.GET_RESOURCE, requestString, ooi_id, expiry, status)
		
		response.setStatus(status);

		def jsonArray = JSON.parse(responseString)
		render jsonArray as JSON
	}
}
