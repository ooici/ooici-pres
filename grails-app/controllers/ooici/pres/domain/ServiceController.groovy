package ooici.pres.domain

import ion.core.messaging.IonMessage
import ion.resource.ResourceDO
import com.rabbitmq.client.AMQP
import grails.converters.JSON

import ion.integration.ais.AppIntegrationService

class ServiceController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

	def BootstrapIONService
	
    def list = {

        def ooid = session.getAttribute("IONCOREOOID");

		def requestJsonString = "{\"user_ooi_id\": \"" + ooid + "\",\"minLatitude\": 32.87521,\"maxLatitude\": 32.97521,\"minLongitude\": -117.274609,\"maxLongitude\": -117.174609,\"minVertical\": 5.5,\"maxVertical\": 6.6,\"posVertical\": \"7.7\",\"minTime\": 8.8,\"maxTime\": 9.9,\"identity\": \"\"}";
        def dataResourceList = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(requestJsonString, AppIntegrationService.RequestType.FIND_DATA_RESOURCES, ooid, "0");

		if (BootstrapIONService.appIntegrationService.getStatus() != 200) {
			def errorMessage = BootstrapIONService.appIntegrationService.getErrorMessage();
			// TODO handle error message
		}

		def jsonArray = JSON.parse(dataResourceList)

		render jsonArray as JSON
    }

    def create = {
        def serviceInstance = new Service()
        serviceInstance.properties = params
        return [serviceInstance: serviceInstance]
    }

    def save = {

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

        def serviceInstance = new Service(params)
        if (serviceInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'service.label', default: 'Service'), serviceInstance.id])}"
            redirect(action: "show")
        }
        else {
            render(view: "create", model: [serviceInstance: serviceInstance])
        }
    }

    def show = {

		// We will receive an object that define name:value pairs
		def sampleJsonResultString = "{\"data_resource_id\": \"fd204aa3-2faa-4d49-84ee-457094666b23\",\"data_resource_details\": \"Some detail data...\"}"

		        def serviceInstance = Service.get(params.id)
        if (!serviceInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), params.id])}"
            redirect(action: "list")
        }
        else {
            [serviceInstance: serviceInstance]
        }
    }

    def edit = {
        def serviceInstance = Service.get(params.id)
        if (!serviceInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [serviceInstance: serviceInstance]
        }
    }

    def update = {
        def serviceInstance = Service.get(params.id)
        if (serviceInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (serviceInstance.version > version) {
                    
                    serviceInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'service.label', default: 'Service')] as Object[], "Another user has updated this Service while you were editing")
                    render(view: "edit", model: [serviceInstance: serviceInstance])
                    return
                }
            }
            serviceInstance.properties = params
            if (!serviceInstance.hasErrors() && serviceInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'service.label', default: 'Service'), serviceInstance.id])}"
                redirect(action: "show", id: serviceInstance.id)
            }
            else {
                render(view: "edit", model: [serviceInstance: serviceInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def serviceInstance = Service.get(params.id)
        if (serviceInstance) {
            try {
                serviceInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'service.label', default: 'Service'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'service.label', default: 'Service'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), params.id])}"
            redirect(action: "list")
        }
    }
}
