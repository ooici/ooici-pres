package ooici.pres.domain

import grails.converters.JSON
import ion.integration.ais.AppIntegrationService;
import ion.integration.ais.AppIntegrationService.RequestType;

abstract class BaseController {

	def BootstrapIONService

	def ooi_id = session.getAttribute("IONCOREOOIID") == null ? "ANONYMOUS" : session.getAttribute("IONCOREOOIID");
	def expiry = session.getAttribute("IONCOREEXPIRY") == null ? "ANONYMOUS" : session.getAttribute("IONCOREEXPIRY");
	def status = 200;
	
	def reqAction = params.get("action");
	def reqController = params.get("controller");

	// Helper method to print params for debugging purposes, optionally redirect
	// on authority failure, and remove unnecessary params so that subsequent
	// conversion to JSON request string will work.
	def preProcessRequest(boolean redirectOnUnauthenticated) {
		
		System.out.println("Request params: " + params);
		
		if (redirectOnUnauthenticated) {
			if (ooi_id == null || ooi_id.equals("ANONYMOUS")) {
				System.out.println("Redirecting: " + params.get("controller") + "." + params.get("action") + " requires user to login")
				response.setStatus(401);
				redirect(uri:"/");
				return
			}
			
			long currentDateSec = System.currentTimeMillis()/1000;
			if (currentDateSec > new Long(expiry)) {
				System.out.println("Redirecting: User authentication expired while attempting to process " + params.get("controller") + "." + params.get("action"))
				response.setStatus(401);
				redirect(uri:"/");
				return
			}
		}

		// Remove non-user data specific params
		params.remove("action");
		params.remove("controller");
	}

	// Centralized convert/send/receive/convert/render handling	
	def sendReceive(RequestType requestType) {
		
		System.out.println("Modified request params  for " + reqAction + "." + reqController + "before conversion: " + params);

		def requestString = new JSON(params).toString()
		
		System.out.println("Request string for " + reqAction + "." + reqController + ": " + requestString);

		String responseString = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(requestString, requestType, ooi_id, expiry);

		status = BootstrapIONService.appIntegrationService.getStatus();
		if (status != 200) {
			responseString = BootstrapIONService.appIntegrationService.getErrorMessage();
			System.out.println("Error on request " + reqAction + "." + reqController);
		}

		System.out.println("Response string for " + reqAction + "." + reqController + ": " + responseString);
		
		response.setStatus(status);
		
		def jsonArray = JSON.parse(responseString)
		render jsonArray as JSON
	}
}
