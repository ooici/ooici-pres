package ooici.pres.domain

import ooici.pres.BootstrapIONService;
import grails.converters.JSON
import ion.integration.ais.AppIntegrationService;
import ion.integration.ais.AppIntegrationService.RequestType;

/**
 * Helper class for actual request controllers.  Centralizes
 * authentication management and send/receive.
 * 
 * @author tomlennan
 *
 */
abstract class BaseController {

	def BootstrapIONService

	String OOI_ID_KEY = "IONCOREOOIID"
	String EXPIRY_KEY = "IONCOREEXPIRY"

	def ooi_id = session.getAttribute(OOI_ID_KEY) == null ? "ANONYMOUS" : session.getAttribute(OOI_ID_KEY)
	def expiry = session.getAttribute(EXPIRY_KEY) == null ? "ANONYMOUS" : session.getAttribute(EXPIRY_KEY)
	
	def reqController = params.get("controller")
	def reqAction = params.get("action")

	def status = 200

	// Helper method to print params for debugging purposes, optionally redirect
	// on authority failure, and remove unnecessary params so that subsequent
	// conversion to JSON request string will work.
	def preProcessRequest(boolean redirectOnUnauthenticated) {
		
		System.out.println("Request params: " + params)
	
		if (redirectOnUnauthenticated) {
			if (ooi_id == null || ooi_id.equals("ANONYMOUS")) {
				System.out.println("Redirecting: " + reqController + "." + reqAction + " requires user to login")
				response.setStatus(401)
				redirect(uri:"/")
				return
			}
			
			long currentDateSec = System.currentTimeMillis()/1000
			if (currentDateSec > new Long(expiry)) {
				System.out.println("Redirecting: User authentication expired while attempting to process " + reqController + "." + reqAction)
				response.setStatus(401)
				redirect(uri:"/")
				return
			}
		}

		// Remove non-user data specific params
		params.remove("action")
		params.remove("controller")
	}

	// Centralized convert/send/receive/convert/render handling	
	def sendReceive(RequestType requestType) {
		
		System.out.println("Modified request params  for " + reqController + "." + reqAction + "before conversion: " + params)

		def requestString = new JSON(params).toString()
		
		System.out.println("Request string for " + reqController + "." + reqAction + ": " + requestString)

		String responseString = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(requestString, requestType, ooi_id, expiry)

		status = BootstrapIONService.appIntegrationService.getStatus()
		if (status != 200) {
			responseString = BootstrapIONService.appIntegrationService.getErrorMessage()
			System.out.println("Error on request " + reqController + "." + reqAction)
		}

		System.out.println("Response string for " + reqController + "." + reqAction + ": " + responseString)
		
		response.setStatus(status)
		
		def jsonArray = JSON.parse(responseString)
		render jsonArray as JSON
	}
}
