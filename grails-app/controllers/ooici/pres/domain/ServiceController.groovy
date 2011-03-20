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

//        def ooid = session.getAttribute("IONCOREOOID");
//
//        def requestJsonString = "{\"user_ooi_id\": \"3f27a744-2c3e-4d2a-a98c-050b246334a3\",\"minLatitude\": 32.87521,\"maxLatitude\": 32.97521,\"minLongitude\": -117.274609,\"maxLongitude\": -117.174609,\"minDepth\": 5.5,\"maxDepth\": 6.6,\"minTime\": 7.7,\"maxTime\": 8.8,\"identity\": \"fd204aa3-2faa-4d49-84ee-457094666b23\"}";
//
//        def dataResourceList = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(requestJsonString, AppIntegrationService.RequestType.FIND_DATA_RESOURCES, ooid, "0");

		// We will receive a list of objects that define name:value pairs
//		def sampleJsonResultString = "[{\"user_ooi_id\": \"3f27a744-2c3e-4d2a-a98c-050b246334a3\",\"data_resource_id\": \"fd204aa3-2faa-4d49-84ee-457094666b23\",\"title\": \"NDBC Sensor Observation Service data\",\"institution\": \"NOAA National Data Buoy Center (http://www.ndbc.noaa.gov/)\",\"source\": \"NDBC SOS\"}]"

//	    DataResource dataResource = new DataResource()
//	    
//	    dataResource.id = 1
//	    dataResource.title = "title 123"
//	    dataResource.provider = "The provider 123"
//	    dataResource.format = "format 123"
//	    dataResource.type = "type 123"
//	    dataResource.summary = "summary 123"
//	    dataResource.publisherName = "publisher name 123"
//	    dataResource.creatorName = "creator name 123"
//	    
//	    def dataResourceList = []
//	    dataResourceList.add(dataResource)

	    render(contentType:"text/json") {
			aaData = [
				['test id','Test Title XXX - This is a longer title','Test provider','Test format','Test type','test summary','test publisher Name','tset creator Name'],
				['test id','Test Title 2' ,'Test provider','Test format','Test type','test summary','test publisher Name','tset creator Name'],
				['test id','Test Title 3','Test provider','Test format','Test type','test summary','test publisher Name','tset creator Name'],
				['test id','Test Title 4','Test provider','Test format','Test type','test summary','test publisher Name','tset creator Name'],
				['test id','Test Title 5','Test provider','Test format','Test type','test summary','test publisher Name','tset creator Name'],
				['test id','Test Title 6','Test provider','Test format','Test type','test summary','test publisher Name','tset creator Name'],
				['test id','Test Title 7','Test provider','Test format','Test type','test summary','test publisher Name','tset creator Name'],
				['test id','Test Title 8','Test provider','Test format','Test type','test summary','test publisher Name','tset creator Name'],
				['test id','Test Title 9','Test provider','Test format','Test type','test summary','test publisher Name','tset creator Name'],
				['test id','Test Title 10','Test provider','Test format','Test type','test summary','test publisher Name','tset creator Name'],
				['test id','Test Title 11','Test provider','Test format','Test type','test summary','test publisher Name','tset creator Name'],
				['test id','Test Title 12','Test provider','Test format','Test type','test summary','test publisher Name','tset creator Name'],
				['test id','Test Title 13','Test provider','Test format','Test type','test summary','test publisher Name','tset creator Name'],
				['test id','Test Title 14','Test provider','Test format','Test type','test summary','test publisher Name','tset creator Name'],
				['test id','Test Title 15','Test provider','Test format','Test type','test summary','test publisher Name','tset creator Name'],
				['test id','Test Title 16','Test provider','Test format','Test type','test summary','test publisher Name','tset creator Name'],
				['test id','Test Title 17','Test provider','Test format','Test type','test summary','test publisher Name','tset creator Name'],
				['test id','Test Title 18','Test provider','Test format','Test type','test summary','test publisher Name','tset creator Name']
			]
		}

//	    render dataResourceList as JSON

//        params.max = Math.min(params.max ? params.int('max') : 10, 100)
//        [services:serviceList, serviceInstanceList: Service.list(params), serviceInstanceTotal: Service.count()]
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
