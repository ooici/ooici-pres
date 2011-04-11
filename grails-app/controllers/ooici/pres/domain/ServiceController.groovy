package ooici.pres.domain

import javax.servlet.http.Cookie;

import grails.converters.JSON
import ion.integration.ais.AppIntegrationService;

class ServiceController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "findDataResources", params: params)
    }

	def BootstrapIONService
	def ooi_id = null
	def expiry = null

	def getOoiidAndExpiry = {
		
		Cookie[] cookies = request.getCookies();
		boolean ooiidFound = false;
		boolean expiryFound = false;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("IONCOREOOIID")) {
					ooi_id = cookies[i].getValue();
					ooiidFound = true;
				}
				if (cookies[i].getName().equals("IONCOREEXPIRY")) {
					expiry = cookies[i].getValue();
					expiryFound = true;
				}
			}
		}
		if (!ooiidFound || !expiryFound) {
			redirect(uri:"/");
		}
    }
	
	def findDataResources = {
		
		System.out.println("params: " + params);

		getOoiidAndExpiry();			
		
		params.remove("action");
		params.remove("controller");
		
		// Process numeric values
//		String minVertical = params.remove("minVertical");
//		params.put("minVertical", new Double(minVertical));
//		String maxVertical = params.remove("maxVertical");
//		params.put("maxVertical", new Double(maxVertical));
//		String minLatitude = params.remove("minLatitude");
//		params.put("minLatitude", new Double(minLatitude));
//		String maxLatitude = params.remove("maxLatitude");
//		params.put("maxLatitude", new Double(maxLatitude));
//		String minLongitude = params.remove("minLongitude");
//		params.put("minLongitude", new Double(minLongitude));
//		String maxLongitude = params.remove("maxLongitude");
//		params.put("maxLongitude", new Double(maxLongitude));
//		String minTime = params.remove("minTime");
//		params.put("minTime", new Double(minTime));
//		String maxTime = params.remove("maxTime");
//		params.put("maxTime", new Double(maxTime));

		params.put("user_ooi_id", ooi_id);
		def requestJsonString = new JSON(params).toString()

//		def requestJsonString = "{\"user_ooi_id\": \"" + ooid + "\",\"minLatitude\": 32.87521,\"maxLatitude\": 32.97521,\"minLongitude\": -117.274609,\"maxLongitude\": -117.174609,\"minVertical\": 5.5,\"maxVertical\": 6.6,\"posVertical\": \"7.7\",\"minTime\": 8.8,\"maxTime\": 9.9,\"identity\": \"\"}";
		def dataResourceList = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(requestJsonString, AppIntegrationService.RequestType.FIND_DATA_RESOURCES, ooi_id, expiry);

		int status = BootstrapIONService.appIntegrationService.getStatus();
		if (status != 200) {
			def errorMessage = BootstrapIONService.appIntegrationService.getErrorMessage();
			System.out.println("Error message: " + errorMessage);
			// TODO report error message
		}
		else {
			def jsonArray = JSON.parse(dataResourceList)

			render jsonArray as JSON
		}
	}
	
	def getDataResourceDetail = {

		getOoiidAndExpiry();			
		
		params.remove("action");
		params.remove("controller");
		
		def requestJsonString = new JSON(params).toString()

		def dataResourceDetails = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(requestJsonString, AppIntegrationService.RequestType.GET_DATA_RESOURCE_DETAIL, ooi_id, expiry);

		int status = BootstrapIONService.appIntegrationService.getStatus();
		if (status != 200) {
			def errorMessage = BootstrapIONService.appIntegrationService.getErrorMessage();
			System.out.println("Error message: " + errorMessage);
			// TODO report error message
		}
		else {
			def jsonArray = JSON.parse(dataResourceDetails)

			render jsonArray as JSON
		}
	}
	
	def createDownloadURL = {

		getOoiidAndExpiry();			
		
		params.remove("action");
		params.remove("controller");
		
		params.put("user_ooi_id", ooi_id);
		def requestJsonString = new JSON(params).toString()

		def downloadURL = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(requestJsonString, AppIntegrationService.RequestType.CREATE_DOWNLOAD_URL, ooi_id, expiry);

		int status = BootstrapIONService.appIntegrationService.getStatus();
		if (status != 200) {
			def errorMessage = BootstrapIONService.appIntegrationService.getErrorMessage();
			System.out.println("Error message: " + errorMessage);
			// TODO report error message
		}
		else {
			def jsonArray = JSON.parse(downloadURL)

			render jsonArray as JSON
		}
	}
	
// TODO
//	
//	def createSubscription = {
//
//		getOoiidAndExpiry();			
//		
//		params.remove("action");
//		params.remove("controller");
//		
//		def requestJsonString = new JSON(params).toString()
//
//		def subscriptionId = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(requestJsonString, AppIntegrationService.RequestType.CREATE_SUBSCRIPTION, ooi_id, expiry);
//
//		int status = BootstrapIONService.appIntegrationService.getStatus();
//		if (status != 200) {
//			def errorMessage = BootstrapIONService.appIntegrationService.getErrorMessage();
//			System.out.println("Error message: " + errorMessage);
//			// TODO report error message
//		}
//		else {
//			def jsonArray = JSON.parse(subscriptionId)
//
//			render jsonArray as JSON
//		}
//	}

//TODO
//	
//	def findSubscription = {
//
//		getOoiidAndExpiry();			
//		
//		params.remove("action");
//		params.remove("controller");
//		
//		def requestJsonString = new JSON(params).toString()
//
//		def subscriptionId = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(requestJsonString, AppIntegrationService.RequestType.FIND_SUBSCRIPTION, ooi_id, expiry);
//
//		int status = BootstrapIONService.appIntegrationService.getStatus();
//		if (status != 200) {
//			def errorMessage = BootstrapIONService.appIntegrationService.getErrorMessage();
//			System.out.println("Error message: " + errorMessage);
//			// TODO report error message
//		}
//		else {
//			def jsonArray = JSON.parse(subscriptionId)
//
//			render jsonArray as JSON
//		}
//	}
	
//TODO
//
//	def deleteSubscription = {
//
//		getOoiidAndExpiry();			
//
//		params.remove("action");
//		params.remove("controller");
//
//		def requestJsonString = new JSON(params).toString()
//
//		def subscriptionId = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(requestJsonString, AppIntegrationService.RequestType.DELETE_SUBSCRIPTION, ooi_id, expiry);
//
//		int status = BootstrapIONService.appIntegrationService.getStatus();
//		if (status != 200) {
//			def errorMessage = BootstrapIONService.appIntegrationService.getErrorMessage();
//			System.out.println("Error message: " + errorMessage);
//			// TODO report error message
//		}
//		else {
//			def jsonArray = JSON.parse(subscriptionId)
//
//			render jsonArray as JSON
//		}
//	}

// TODO	
//	def createDataResource = {
//
//		getOoiidAndExpiry();			
//		
//		params.remove("action");
//		params.remove("controller");
//		
//		params.put("user_ooi_id", ooi_id);
//		def requestJsonString = new JSON(params).toString()
//
//		def downloadURL = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(requestJsonString, AppIntegrationService.RequestType.CREATE_DATA_RESOURCE, ooi_id, expiry);
//
//		int status = BootstrapIONService.appIntegrationService.getStatus();
//		if (status != 200) {
//			def errorMessage = BootstrapIONService.appIntegrationService.getErrorMessage();
//			System.out.println("Error message: " + errorMessage);
//			// TODO report error message
//		}
//		else {
//			def jsonArray = JSON.parse(downloadURL)
//
//			render jsonArray as JSON
//		}
//	}
	
// TODO
//	def updateDataResource = {
//
//		getOoiidAndExpiry();			
//		
//		params.remove("action");
//		params.remove("controller");
//		
//		params.put("user_ooi_id", ooi_id);
//		def requestJsonString = new JSON(params).toString()
//
//		def downloadURL = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(requestJsonString, AppIntegrationService.RequestType.UPDATE_DATA_RESOURCE, ooi_id, expiry);
//
//		int status = BootstrapIONService.appIntegrationService.getStatus();
//		if (status != 200) {
//			def errorMessage = BootstrapIONService.appIntegrationService.getErrorMessage();
//			System.out.println("Error message: " + errorMessage);
//			// TODO report error message
//		}
//	}
	
	def updateUserEmail = {

		getOoiidAndExpiry();			
		
		params.remove("action");
		params.remove("controller");
		
		params.put("user_ooi_id", ooi_id);
		def requestJsonString = new JSON(params).toString()

		def downloadURL = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(requestJsonString, AppIntegrationService.RequestType.UPDATE_USER_EMAIL, ooi_id, expiry);

		int status = BootstrapIONService.appIntegrationService.getStatus();
		if (status != 200) {
			def errorMessage = BootstrapIONService.appIntegrationService.getErrorMessage();
			System.out.println("Error message: " + errorMessage);
			// TODO report error message
		}
	}
	
	def updateUserDispatcherQueue = {

		getOoiidAndExpiry();			
		
		params.remove("action");
		params.remove("controller");
		
		params.put("user_ooi_id", ooi_id);
		def requestJsonString = new JSON(params).toString()

		def downloadURL = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(requestJsonString, AppIntegrationService.RequestType.UPDATE_USER_DISPATCHER_QUEUE, ooi_id, expiry);

		int status = BootstrapIONService.appIntegrationService.getStatus();
		if (status != 200) {
			def errorMessage = BootstrapIONService.appIntegrationService.getErrorMessage();
			System.out.println("Error message: " + errorMessage);
			// TODO report error message
		}
	}
	
	def findResources = {

		getOoiidAndExpiry();			
		
		params.remove("action");
		params.remove("controller");
		
		params.put("user_ooi_id", ooi_id);
		def requestJsonString = new JSON(params).toString()

		def downloadURL = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(requestJsonString, AppIntegrationService.RequestType.FIND_RESOURCES, ooi_id, expiry);

		int status = BootstrapIONService.appIntegrationService.getStatus();
		if (status != 200) {
			def errorMessage = BootstrapIONService.appIntegrationService.getErrorMessage();
			System.out.println("Error message: " + errorMessage);
			// TODO report error message
		}
		else {
			def jsonArray = JSON.parse(downloadURL)

			render jsonArray as JSON
		}
	}
	
	def getResourceDetail = {

		getOoiidAndExpiry();			
		
		params.remove("action");
		params.remove("controller");
		
		params.put("user_ooi_id", ooi_id);
		def requestJsonString = new JSON(params).toString()

		def downloadURL = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(requestJsonString, AppIntegrationService.RequestType.GET_RESOURCE_DETAIL, ooi_id, expiry);

		int status = BootstrapIONService.appIntegrationService.getStatus();
		if (status != 200) {
			def errorMessage = BootstrapIONService.appIntegrationService.getErrorMessage();
			System.out.println("Error message: " + errorMessage);
			// TODO report error message
		}
		else {
			def jsonArray = JSON.parse(downloadURL)

			render jsonArray as JSON
		}
	}

}
