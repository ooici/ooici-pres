package ooici.pres.domain

import ion.integration.ais.AppIntegrationService.RequestType

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

//	def start = {
//		
//		preProcessRequest(false)
//
//		sendReceive(RequestType.START_INSTRUMENT_SAMPLING)
//	}
//
//	def stop = {
//		
//		preProcessRequest(false)
//
//		sendReceive(RequestType.STOP_INSTRUMENT_SAMPLING)
//	}
//
//	def getState = {
//		
//		preProcessRequest(false)
//
//		sendReceive(RequestType.GET_INSTRUMENT_STATE)
//	}
//
//	def setState = {
//		
//		preProcessRequest(false)
//		
//		specialPreProcessProperties()
//
//		sendReceive(RequestType.SET_INSTRUMENT_STATE)
//	}
//	
	def specialPreProcessProperties = {
		
		params.put("properties",JSON.parse(params.get("properties")))

		def propertiesJSON = params.get("properties")

		// Special handle numeric and boolean params
		if (propertiesJSON.get("navg") != null)
			propertiesJSON.put("navg",propertiesJSON.int("navg"))
		if (propertiesJSON.get("interval") != null)
			propertiesJSON.put("interval",propertiesJSON.int("interval"))
		if (propertiesJSON.get("outputsv") != null)
			propertiesJSON.put("outputsv",propertiesJSON.boolean("outputsv"))
		if (propertiesJSON.get("outputsal") != null)
			propertiesJSON.put("outputsal",propertiesJSON.int("outputsal"))
		if (propertiesJSON.get("txrealtime") != null)
			propertiesJSON.put("txrealtime",propertiesJSON.int("txrealtime"))
		if (propertiesJSON.get("storetime") != null)
			propertiesJSON.put("storetime",propertiesJSON.int("storetime"))

		params.put("properties",propertiesJSON)
	}
	
	def command = {
		render(view:"command")
	}
	
	def commandStatus = {
		render(view:"commandStatus")
	}
	
	def edit = {
		render(view:"exit")
	}
	
	def getset = {
		render(view:"getset")
	}
	
	def seeagent = {
		render(view:"seeagent")
	}
	
	def show = {
		render(view="show")
	}

	def status = {
		render(view="status")
	}
}