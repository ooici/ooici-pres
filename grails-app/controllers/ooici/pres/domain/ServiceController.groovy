package ooici.pres.domain

import grails.converters.JSON
import ion.integration.ais.AppIntegrationService;

class ServiceController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

	def BootstrapIONService
//	
//    def list = {
//		
//		def ooi_id = session.getAttribute("IONCOREOOIID");
//		def expiry = session.getAttribute("IONCOREEXPIRY");
//		
//		params.remove("action");
//		params.remove("controller");
//
////		params.put("user_ooi_id", "3f27a744-2c3e-4d2a-a98c-050b246334a3");
//		params.put("user_ooi_id", ooi_id);
//		def requestJsonString = new JSON(params).toString()
//
////		def requestJsonString = "{\"user_ooi_id\": \"" + ooid + "\",\"minLatitude\": 32.87521,\"maxLatitude\": 32.97521,\"minLongitude\": -117.274609,\"maxLongitude\": -117.174609,\"minVertical\": 5.5,\"maxVertical\": 6.6,\"posVertical\": \"7.7\",\"minTime\": 8.8,\"maxTime\": 9.9,\"identity\": \"\"}";
//        def dataResourceList = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(requestJsonString, AppIntegrationService.RequestType.FIND_DATA_RESOURCES, ooi_id, expiry);
//
//		def status = BootstrapIONService.appIntegrationService.getStatus();
//		if (status != 200) {
//			System.out.println("status: " + status);
//			def errorMessage = BootstrapIONService.appIntegrationService.getErrorMessage();
//			System.out.println("errorMessage: " + errorMessage);
//			// TODO handle error message
//		}
//
//		def jsonArray = JSON.parse(dataResourceList)
//
//		render jsonArray as JSON
//    }
	
	def findDataResources = {
		
		def ooi_id = session.getAttribute("IONCOREOOIID");
		def expiry = session.getAttribute("IONCOREEXPIRY");
		
		System.out.println("params: " + params)
		
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
		
		def ooi_id = session.getAttribute("IONCOREOOIID");
		def expiry = session.getAttribute("IONCOREEXPIRY");
		
		params.remove("action");
		params.remove("controller");
		
		params.put("user_ooi_id", ooi_id);
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
		
		def ooi_id = session.getAttribute("IONCOREOOIID");
		def expiry = session.getAttribute("IONCOREEXPIRY");
		
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
//		def ooi_id = session.getAttribute("IONCOREOOIID");
//		def expiry = session.getAttribute("IONCOREEXPIRY");
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
//		def ooi_id = session.getAttribute("IONCOREOOIID");
//		def expiry = session.getAttribute("IONCOREEXPIRY");
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
//		def ooi_id = session.getAttribute("IONCOREOOIID");
//		def expiry = session.getAttribute("IONCOREEXPIRY");
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
//		def ooi_id = session.getAttribute("IONCOREOOIID");
//		def expiry = session.getAttribute("IONCOREEXPIRY");
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
//		def ooi_id = session.getAttribute("IONCOREOOIID");
//		def expiry = session.getAttribute("IONCOREEXPIRY");
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
		
		def ooi_id = session.getAttribute("IONCOREOOIID");
		def expiry = session.getAttribute("IONCOREEXPIRY");
		
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
		
		def ooi_id = session.getAttribute("IONCOREOOIID");
		def expiry = session.getAttribute("IONCOREEXPIRY");
		
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
		
		def ooi_id = session.getAttribute("IONCOREOOIID");
		def expiry = session.getAttribute("IONCOREEXPIRY");
		
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
		
		def ooi_id = session.getAttribute("IONCOREOOIID");
		def expiry = session.getAttribute("IONCOREEXPIRY");
		
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

//    def create = {
//        def serviceInstance = new Service()
//        serviceInstance.properties = params
//        return [serviceInstance: serviceInstance]
//    }
//
//    def save = {
//
//	    String hostName = "localhost";
//        int portNumber = AMQP.PROTOCOL.PORT;
//        String exchange = "magnet.topic";
//        String toName = "spasco.javaint";
//        String fromName = "mysys.return";
//
//        // Messaging environment
//        MsgBrokerAttachment ionAttach = new MsgBrokerAttachment(hostName, portNumber, exchange);
//        ionAttach.attach();
//
//        // Create return queue
//        String queue = ionAttach.declareQueue(null);
//        ionAttach.bindQueue(queue, fromName, null);
//        ionAttach.attachConsumer(queue);
//
//		String msg = "{\"name\":\"" + params.name + "\", \"status\":\"" + params.status + "\"}";
//
//		IonMessage msgout = ionAttach.createMessage(fromName, toName, "register_service", msg);
//		ionAttach.sendMessage(msgout);
//
//		// Receive response message
//		IonMessage msgin = ionAttach.consumeMessage(queue);
//		ionAttach.ackMessage(msgin);
//
//        def serviceInstance = new Service(params)
//        if (serviceInstance.save(flush: true)) {
//            flash.message = "${message(code: 'default.created.message', args: [message(code: 'service.label', default: 'Service'), serviceInstance.id])}"
//            redirect(action: "show")
//        }
//        else {
//            render(view: "create", model: [serviceInstance: serviceInstance])
//        }
//    }
//
//    def show = {
//
//		// We will receive an object that define name:value pairs
//		def sampleJsonResultString = "{\"data_resource_id\": \"fd204aa3-2faa-4d49-84ee-457094666b23\",\"data_resource_details\": \"Some detail data...\"}"
//
//		        def serviceInstance = Service.get(params.id)
//        if (!serviceInstance) {
//            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), params.id])}"
//            redirect(action: "list")
//        }
//        else {
//            [serviceInstance: serviceInstance]
//        }
//    }
//
//    def edit = {
//        def serviceInstance = Service.get(params.id)
//        if (!serviceInstance) {
//            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), params.id])}"
//            redirect(action: "list")
//        }
//        else {
//            return [serviceInstance: serviceInstance]
//        }
//    }
//
//    def update = {
//        def serviceInstance = Service.get(params.id)
//        if (serviceInstance) {
//            if (params.version) {
//                def version = params.version.toLong()
//                if (serviceInstance.version > version) {
//                    
//                    serviceInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'service.label', default: 'Service')] as Object[], "Another user has updated this Service while you were editing")
//                    render(view: "edit", model: [serviceInstance: serviceInstance])
//                    return
//                }
//            }
//            serviceInstance.properties = params
//            if (!serviceInstance.hasErrors() && serviceInstance.save(flush: true)) {
//                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'service.label', default: 'Service'), serviceInstance.id])}"
//                redirect(action: "show", id: serviceInstance.id)
//            }
//            else {
//                render(view: "edit", model: [serviceInstance: serviceInstance])
//            }
//        }
//        else {
//            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), params.id])}"
//            redirect(action: "list")
//        }
//    }
//
//    def delete = {
//        def serviceInstance = Service.get(params.id)
//        if (serviceInstance) {
//            try {
//                serviceInstance.delete(flush: true)
//                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'service.label', default: 'Service'), params.id])}"
//                redirect(action: "list")
//            }
//            catch (org.springframework.dao.DataIntegrityViolationException e) {
//                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'service.label', default: 'Service'), params.id])}"
//                redirect(action: "show", id: params.id)
//            }
//        }
//        else {
//            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), params.id])}"
//            redirect(action: "list")
//        }
//    }
}
