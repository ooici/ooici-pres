package ooici.pres

import javax.servlet.http.Cookie;

import grails.converters.JSON
import ion.integration.ais.AppIntegrationService;
import ion.integration.ais.AppIntegrationService.RequestType;

class DataResourceController {

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

	def find = {
		
		setupForSendReceive(false);
		
		if (ooi_id != null && !ooi_id.equals("ANONYMOUS")) {
			params.put("user_ooi_id", ooi_id);
		}

		def requestString = new JSON(params).toString()

		int status = 200;		
		String responseString = BootstrapIONService.sendReceive(RequestType.FIND_DATA_RESOURCES, requestString, ooi_id, expiry, status);

		response.setStatus(status);
		
		def jsonArray = JSON.parse(responseString)
		render jsonArray as JSON
	}
	
	def findByUser = {
		
		setupForSendReceive(true);
				
		params.put("user_ooi_id", ooi_id);

		def requestString = new JSON(params).toString()
		
		int status = 200;		
		String responseString = BootstrapIONService.sendReceive(RequestType.FIND_DATA_RESOURCES_BY_USER, requestString, ooi_id, expiry, status)
		
		response.setStatus(status);

		def jsonArray = JSON.parse(responseString)
		render jsonArray as JSON
	}
	
	def detail = {
		
		setupForSendReceive(false);

		def requestString = new JSON(params).toString()
		
		int status = 200;		
		String responseString = BootstrapIONService.sendReceive(RequestType.GET_DATA_RESOURCE_DETAIL, requestString, ooi_id, expiry, status)
		
		response.setStatus(status);

		def jsonArray = JSON.parse(responseString)
		render jsonArray as JSON
	}
	
	def create = {
		
		setupForSendReceive(true);

		def requestString = new JSON(params).toString()
		
		int status = 200;		
		String responseString = BootstrapIONService.sendReceive(RequestType.CREATE_DATA_RESOURCE, requestString, ooi_id, expiry, status)
		
		response.setStatus(status);

		def jsonArray = JSON.parse(responseString)
		render jsonArray as JSON
	}
	
	def update = {
		
		setupForSendReceive(true);

		def requestString = new JSON(params).toString()
		
		int status = 200;		
		String responseString = BootstrapIONService.sendReceive(RequestType.UPDATE_DATA_RESOURCE, requestString, ooi_id, expiry, status)
		
		response.setStatus(status);

		def jsonArray = JSON.parse(responseString)
		render jsonArray as JSON
	}
	
	def delete = {
		
		setupForSendReceive(true);

		def requestString = new JSON(params).toString()
		
		int status = 200;		
		String responseString = BootstrapIONService.sendReceive(RequestType.DELETE_DATA_RESOURCE, requestString, ooi_id, expiry, status)
		
		response.setStatus(status);

		def jsonArray = JSON.parse(responseString)
		render jsonArray as JSON
	}
}