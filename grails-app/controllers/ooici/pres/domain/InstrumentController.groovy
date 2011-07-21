package ooici.pres.domain

import org.codehaus.groovy.grails.web.json.JSONException;

import grails.converters.JSON
import ion.integration.ais.AppIntegrationService;
import ion.integration.ais.AppIntegrationService.RequestType;

class InstrumentController extends BaseController {

	def index = {
		redirect(action: "list", params: params)
	}
  
  	def list = {
  		
  		preProcessRequest(false)
  
  		sendReceive(RequestType.GET_INSTRUMENT_LIST)
  	}
  
  	def create = {
  		
  		preProcessRequest(false)
  
  		sendReceive(RequestType.CREATE_INSTRUMENT)
  	}

	def startAgent = {
		
		preProcessRequest(false)

		sendReceive(RequestType.START_INSTRUMENT_AGENT)
	}
  
  	def start = {
  		
  		preProcessRequest(false)
  
  		sendReceive(RequestType.START_INSTRUMENT_SAMPLING)
  	}
  
  	def stop = {
  		
  		preProcessRequest(false)
  
  		sendReceive(RequestType.STOP_INSTRUMENT_SAMPLING)
  	}
  
  	def getState = {
  		
  		preProcessRequest(false)
  
  		sendReceive(RequestType.GET_INSTRUMENT_STATE)
  	}
  
  	def setState = {
  		
  		preProcessRequest(false)
  		
  		specialPreProcessProperties()
  
  		sendReceive(RequestType.SET_INSTRUMENT_STATE)
  	}
  	
	def specialPreProcessProperties = {
		
		params.put("properties",JSON.parse(params.get("properties")))

		def propertiesJSON = params.get("properties")

		// Special handle numeric and boolean params
		try {
			if (propertiesJSON.get("navg") != null)
				propertiesJSON.put("navg",propertiesJSON.int("navg"))
		}
		catch(JSONException e) {
			// Ignore
		}
		try {
			if (propertiesJSON.get("interval") != null)
				propertiesJSON.put("interval",propertiesJSON.int("interval"))
		}
		catch(JSONException e) {
			// Ignore
		}
		try {
			if (propertiesJSON.get("outputsv") != null)
				propertiesJSON.put("outputsv",propertiesJSON.boolean("outputsv"))
		}
		catch(JSONException e) {
			// Ignore
		}
		try {
			if (propertiesJSON.get("outputsal") != null)
				propertiesJSON.put("outputsal",propertiesJSON.int("outputsal"))
		}
		catch(JSONException e) {
			// Ignore
		}
		try {
			if (propertiesJSON.get("txrealtime") != null)
				propertiesJSON.put("txrealtime",propertiesJSON.int("txrealtime"))
		}
		catch(JSONException e) {
			// Ignore
		}
		try {
			if (propertiesJSON.get("storetime") != null)
				propertiesJSON.put("storetime",propertiesJSON.int("storetime"))
		}
		catch(JSONException e) {
			// Ignore
		}

		params.put("properties",propertiesJSON)
	}
	
	def lcacommand = {
		render(view:"command")
	}
	
	def lcacommandStatus = {
		render(view:"commandStatus")
	}
	
	def lcacreate = {
		render(view:"create")
	}
	
	def lcaedit = {
		render(view:"exit")
	}
	
	def lcagetset = {
		render(view:"getset")
	}
	
	def lcalist = {
		render(view:"list")
	}
	
	def lcaseeagent = {
		render(view:"seeagent")
	}
	
	def lcashow = {
		render(view="show")
	}

	def lcastatus = {
		render(view="status")
	}
}