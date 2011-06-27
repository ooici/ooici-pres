package ooici.pres.domain

import ooici.pres.BootstrapIONService
import grails.converters.JSON
import ion.integration.ais.AppIntegrationService
import ion.integration.ais.AppIntegrationService.RequestType

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
	
	def ooiidFound = session.getAttribute(OOI_ID_KEY) != null;
	def expiryFound = session.getAttribute(EXPIRY_KEY) != null;

	def ooi_id = session.getAttribute(OOI_ID_KEY) == null ? "ANONYMOUS" : session.getAttribute(OOI_ID_KEY)
	def expiry = session.getAttribute(EXPIRY_KEY) == null ? "0" : session.getAttribute(EXPIRY_KEY)
	
	def reqController = params.get("controller")
	def reqAction = params.get("action")

	def status = 200

	// Helper method to print params for debugging purposes, optionally redirect
	// on authority failure, and remove unnecessary params so that subsequent
	// conversion to JSON request string will work.
	def preProcessRequest(boolean redirectOnUnauthenticated) {
		
		System.out.println("Request params: " + params)
		
		// Reload page if session expired
		if (!ooiidFound || !expiryFound) {
			System.out.println("Session timeout: redirecting to landing page")
			redirect(uri:"/index.html")
			return
		}
	
		if (redirectOnUnauthenticated) {
			if (ooi_id.equals("ANONYMOUS")) {
				String errorMsg = "{\"ErrorCode\": 401, \"ErrorMessage\": \"Unauthorized: " + reqController + "." + reqAction + " requires user to login\"}"
				System.out.println(errorMsg)
				response.setStatus(401)
				def jsonArray = JSON.parse(errorMsg)
				render jsonArray as JSON
			}
			
			long currentDateSec = System.currentTimeMillis()/1000
			if (currentDateSec > new Long(expiry)) {
				String errorMsg = "{\"ErrorCode\": 401, \"ErrorMessage\": \"Unauthorized: User authentication expired while attempting to process " + reqController + "." + reqAction + "\"}"
				System.out.println(errorMsg)
				response.setStatus(401)
				def jsonArray = JSON.parse(errorMsg)
				render jsonArray as JSON
			}
		}

		// Remove non-user data specific params
		params.remove("action")
		params.remove("controller")
	}

	// Centralized convert/send/receive/convert/render handling	
	def sendReceive(RequestType requestType) {
		
		System.out.println("Modified request params for " + reqController + "." + reqAction + " before conversion: " + params)

		def requestString = new JSON(params).toString()
		// This is a hack to handle undefined numeric fields
		requestString = requestString.replaceAll("\"NaN\"","NaN")
		
		System.out.println("Request string for " + reqController + "." + reqAction + " for user <" + ooi_id + ">: " + requestString)

		String responseString = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(requestString, requestType, ooi_id, expiry)

		status = BootstrapIONService.appIntegrationService.getStatus()
		if (status != 200) {
			responseString = BootstrapIONService.appIntegrationService.getErrorMessage()
			System.out.println("Error on request " + reqController + "." + reqAction)
		}
		
		if (responseString == null) {
			responseString = "{}"
		}

		System.out.println("Response string for " + reqController + "." + reqAction + ": " + responseString)
		
		response.setStatus(status)
		
		def jsonArray = JSON.parse(responseString)
		render jsonArray as JSON
	}
}
