package ooici.pres.domain

import ion.core.messaging.IonMessage
import ion.resource.ResourceDO
import com.rabbitmq.client.AMQP
import grails.converters.JSON

class ServiceController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {

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
//	    ResourceDO res = new ResourceDO();
//
//		// Create and send request message
//		IonMessage msgout = ionAttach.createMessage(fromName, toName, "list_all_services", res);
//		ionAttach.sendMessage(msgout);
//
//	    // Receive response message
//		IonMessage msgin = ionAttach.consumeMessage(queue);
//
//	    // create dataproduct list
//		def serviceList = []
//
//	    // get the value content from the message
//	    def value = msgin.getContent().get("value")
//
//	    // place value content into dataproduct domain model object
//	    value.each{
//
//		    def service = new Service()
//
//		    service.name = "$it.name"
//		    service.status = "$it.status"
//
//		    serviceList << service
//	    }
//
//		ionAttach.ackMessage(msgin);

	    // Converting the data to JSON

	    DataResource dataResource = new DataResource()
	    
	    dataResource.id = 1
	    dataResource.title = "title 123"
	    dataResource.provider = "The provider 123"
	    dataResource.format = "format 123"
	    dataResource.type = "type 123"
	    dataResource.summary = "summary 123"
	    dataResource.publisherName = "publisher name 123"
	    dataResource.creatorName = "creator name 123"
	    
	    def dataResourceList = []
	    dataResourceList.add(dataResource)

	    render(contentType:"text/json") {
			aaData = [
				['test id','Test Title 1 - This is a longer title','Test provider','Test format','Test type','test summary','test publisher Name','tset creator Name'],
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

//	    render dataResource as JSON

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
