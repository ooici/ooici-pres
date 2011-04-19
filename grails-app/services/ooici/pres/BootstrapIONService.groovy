package ooici.pres

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ion.core.BaseProcess
import ion.resource.ServiceRDO
import ion.core.data.DataObjectManager
import ion.resource.DataProductRDO
import ion.resource.InstrumentRDO
import com.rabbitmq.client.AMQP
import ion.core.messaging.MsgBrokerClient
import ion.integration.ais.AppIntegrationService
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class BootstrapIONService  {

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

	
	def String sendReceive(AppIntegrationService.RequestType requestType, String requestString, String ooi_id, String expiry, int status) {

		String responseString = BootstrapIONService.appIntegrationService.sendReceiveUIRequest(requestString, requestType, ooi_id, expiry);

		status = BootstrapIONService.appIntegrationService.getStatus();
		if (status != 200) {
			def errorMessage = BootstrapIONService.appIntegrationService.getErrorMessage();
			System.out.println("Error:\nrequest type: " + requestType + "\nrequest message: " + requestString + "\nooi_id: " + ooi_id + "\nexpiry: " + expiry + "\nStatus: " + status + "\nError message: " + errorMessage);
			responseString = "{\"Error\": \"" + errorMessage  + "\"}";
		}

		return responseString;
	}

	
	def getOoiIdAndExpiry(HttpServletRequest servletRequest, String ooi_id, String expiry) {
		HttpSession httpSession = servletRequest.getSession(true);
		
		ooi_id = httpSession.getAttribute("IONCOREOOIID") == null ? "ANONYMOUS" : httpSession.getAttribute("IONCOREOOIID");
		expiry = httpSession.getAttribute("IONCOREEXPIRY") == null ? "ANONYMOUS" : httpSession.getAttribute("IONCOREEXPIRY");

//		ooi_id = "ANONYMOUS";
//		expiry = "0";
//
//		Cookie[] cookies = servletRequest.getCookies();
//		if (cookies != null) {
//			for (int i = 0; i < cookies.length; i++) {
//				if (cookies[i].getName().equals("IONCOREOOIID")) {
//					ooi_id = cookies[i].getValue();
//				}
//				if (cookies[i].getName().equals("IONCOREEXPIRY")) {
//					expiry = cookies[i].getValue();
//				}
//			}
//		}
	}

}
