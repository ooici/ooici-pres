package ooici.pres

import ion.core.BaseProcess
import ion.resource.ServiceRDO
import ion.core.data.DataObjectManager
import ion.resource.DataProductRDO
import ion.resource.InstrumentRDO
import com.rabbitmq.client.AMQP
import ion.core.messaging.MsgBrokerClient

class BootstrapIONService  {

    static transactional = false
	static BaseProcess baseProcess
	static MsgBrokerClient ionClient

	def bootstrap() {

	String hostName = "amoeba.ucsd.edu"
        int portNumber = AMQP.PROTOCOL.PORT
        String exchange = "magnet.topic"

    	println "\nSTEP: Process and Message Broker Client Setup"

    	// DataObject handling
        DataObjectManager.registerDOType(InstrumentRDO.class)
        DataObjectManager.registerDOType(DataProductRDO.class)
        DataObjectManager.registerDOType(ServiceRDO.class)

		// Messaging environment
        ionClient = new MsgBrokerClient(hostName, portNumber, exchange)
        ionClient.attach()

		baseProcess = new BaseProcess(ionClient)
		baseProcess.spawn()
		
	}

}
