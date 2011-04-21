package ooici.pres

import ion.core.BaseProcess
import com.rabbitmq.client.AMQP
import ion.core.messaging.MsgBrokerClient
import ion.integration.ais.AppIntegrationService
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class BootstrapIONService  {
	// Constant defs for use various places in app
	public static final String OOI_ID_KEY = "ooi_id";
	public static final String USER_IS_ADMIN_KEY = "user_is_admin";
	public static final String USER_IS_EARY_ADOPTER_KEY = "user_is_early_adopter";
	public static final String USER_IS_DATA_PROVIDER_KEY = "user_is_data_provider";
	public static final String USER_IS_MARINE_OPERATOR_KEY = "user_is_marine_operator";
	public static final String USER_ALREADY_REGISTERED_KEY = "user_already_registered";

	public static final String ADMIN_ROLE = "user_is_admin";
	public static final String EARY_ADOPTER_ROLE = "user_is_early_adopter";
	public static final String DATA_PROVIDER_ROLE = "user_is_data_provider";
	public static final String MARINE_OPERATOR_ROLE = "user_is_marine_operator";


	static transactional = false
	public static BaseProcess baseProcess
	public static MsgBrokerClient ionClient
	public static AppIntegrationService appIntegrationService

	def bootstrap() {

		def config = ConfigurationHolder.config
		String hostName = config.ioncore.host
		int portNumber = Integer.parseInt(config.ioncore.amqpport)
		String exchange = config.ioncore.exchange
		String sysName = config.ioncore.sysname

		// Messaging environment
		ionClient = new MsgBrokerClient(hostName, portNumber, exchange)
		ionClient.attach()

		baseProcess = new BaseProcess(ionClient)
		baseProcess.spawn()

		appIntegrationService = new AppIntegrationService(sysName, baseProcess)
	}
	
	def destroy() {
		appIntegrationService.dispose()
	}

}
