package ooici.pres.domain

import ion.core.messaging.IonMessage
import ion.resource.ResourceDO
import com.rabbitmq.client.AMQP

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

        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [services:serviceList, serviceInstanceList: Service.list(params), serviceInstanceTotal: Service.count()]
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
