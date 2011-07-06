package ooici.pres

import ion.core.BaseProcess
import au.com.bytecode.opencsv.CSVReader

import com.rabbitmq.client.AMQP
import ion.core.messaging.MsgBrokerClient
import ion.integration.ais.AppIntegrationService

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class BootstrapIONService  {

	static transactional = false
	public static BaseProcess baseProcess
	public static MsgBrokerClient ionClient
	public static AppIntegrationService appIntegrationService
	public static HashMap helpStrings

	def bootstrap() {

		def config = ConfigurationHolder.config
		String hostName = config.ioncore.host
		int portNumber = Integer.parseInt(config.ioncore.amqpport)
		String username = config.ioncore.username
		if (username.equals("{}")) {
			username = null;
		}
		String password = config.ioncore.password
		if (password.equals("{}")) {
			password = null;
		}
		String exchange = config.ioncore.exchange
		String sysName = config.ioncore.sysname

		String str = "Starting msg broker client.  Connecting to " + hostName + ":" + portNumber
		if (username != null) {
			str += ":" + username
		}
		str += ":" + exchange + ":" + sysName
		System.out.println(str)

		// Messaging environment
		ionClient = new MsgBrokerClient(hostName, portNumber, username, password, exchange)
		ionClient.attach()

		baseProcess = new BaseProcess(ionClient)
		baseProcess.spawn()

		appIntegrationService = new AppIntegrationService(sysName, baseProcess)
		
		// Load help file into list
		String helpFile = config.ioncore.helpfile
		def appHolder = ApplicationHolder.application.parentContext.getResource(helpFile)
		CSVReader reader = new CSVReader(new FileReader(appHolder.getFile()))
		List fileEntries = reader.readAll()
		
		// Stash help strings into map
		helpStrings = new HashMap()
		for (String[] entry : fileEntries) {
			helpStrings.put(entry[0], entry[1])
		}
	}
	
	def destroy() {
		appIntegrationService.dispose()
	}

}
