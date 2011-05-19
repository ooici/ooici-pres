package ooici.pres.domain

import ion.integration.ais.AppIntegrationService.RequestType;

class DataResourceController extends BaseController {

	def find = {
		
		preProcessRequest(false);
		
		specialPreProcessFindRequest()
		
		params.put("user_ooi_id", ooi_id)

		sendReceive(RequestType.FIND_DATA_RESOURCES);
	}
	
	def findByUser = {
		
		preProcessRequest(true)
		
		specialPreProcessFindRequest()

		params.put("user_ooi_id", ooi_id)
		
		sendReceive(RequestType.FIND_DATA_RESOURCES_BY_USER)
	}
	
	def detail = {
		
		preProcessRequest(false)

		sendReceive(RequestType.GET_DATA_RESOURCE_DETAIL)
	}
	
	def create = {
		
		preProcessRequest(true)
		
		specialPreProcessBooleanAndTimeValues()

		specialPreProcessBounds()
		
		params.put("user_id", ooi_id)

		sendReceive(RequestType.CREATE_DATA_RESOURCE)
	}
	
	def update = {
		
		preProcessRequest(true)
		
		specialPreProcessBooleanAndTimeValues()

		sendReceive(RequestType.UPDATE_DATA_RESOURCE)
	}
	
	def delete = {
		
		preProcessRequest(true)
		
		sendReceive(RequestType.DELETE_DATA_RESOURCE)
	}
	
	def validate = {
		
		preProcessRequest(true)
		
		sendReceive(RequestType.VALIDATE_DATA_RESOURCE)
	}
	
	def specialPreProcessFindRequest = {
		
		// Special handle numeric params
		if (params.get("minLongitude") != null)
			params.put("minLongitude",params.float("minLongitude"));
		if (params.get("maxLongitude") != null)
			params.put("maxLongitude",params.float("maxLongitude"));
		if (params.get("minLatitude") != null)
			params.put("minLatitude",params.float("minLatitude"));
		if (params.get("maxLatitude") != null)
			params.put("maxLatitude",params.float("maxLatitude"));
		if (params.get("minVertical") != null)
			params.put("minVertical",params.float("minVertical"));
		if (params.get("maxVertical") != null)
			params.put("maxVertical",params.float("maxVertical"));
	}
	
	def specialPreProcessBooleanAndTimeValues = {
		
		// Special handle numeric params
		if (params.get("update_interval_seconds") != null)
			params.put("update_interval_seconds",params.long("update_interval_seconds"));
		if (params.get("max_ingest_millis") != null)
			params.put("max_ingest_millis",params.long("max_ingest_millis"));
		if (params.get("update_start_datetime_millis") != null)
			params.put("update_start_datetime_millis",params.long("update_start_datetime_millis"));
		if (params.get("is_public") != null)
			params.put("is_public",params.boolean("is_public"));
	}
	
	def specialPreProcessBounds = {
		
		// Special handle numeric params
		if (params.get("request_bounds_north") != null)
			params.put("request_bounds_north",params.float("request_bounds_north"));
		if (params.get("request_bounds_south") != null)
			params.put("request_bounds_south",params.float("request_bounds_south"));
		if (params.get("request_bounds_west") != null)
			params.put("request_bounds_west",params.float("request_bounds_west"));
		if (params.get("request_bounds_east") != null)
			params.put("request_bounds_east",params.float("request_bounds_east"));
	}

}