package ooici.pres

import ion.core.messaging.MessagingName
import ion.core.messaging.IonMessage
import ion.core.data.DataObject
import ion.resource.ResourceDO
import ooici.pres.domain.DataResource
import ooici.pres.domain.Spatial
import ooici.pres.domain.Temporal
import ooici.pres.domain.DataResourceDetail
import ooici.pres.domain.Subscription
import ooici.pres.domain.Notification

/**
 * CiService
 *
 * This service class allows the UI to dispatch service calls to the application service layer. Each function targets
 * specific functionality targeted for Release 1.
 *
 * @author Stephen Pasco
 *
 */
class CiService {

    static transactional = false

	def BootstrapIONService

	def SYSNAME = System.getProperty("ioncore.sysname","spasco");

	/**
	 * Deletes a user's subscription
	 * 
	 * @param subscriptionId The subscription id to delete
	 * @return Returns a status message
	 */
	def String deleteSubscription(UUID subscriptionId) {

		def status
		
	    // Target subscription registry service
	    MessagingName instRegSvc = new MessagingName(SYSNAME, "subscription_registry")

		// Establish command map
		def commandMap = [
				'subscriptionId': subscriptionId
		]

		// Establish content map
		def contentMap = ['commandMap': commandMap]

		// Make the service call
        IonMessage msgin = BootstrapIONService.baseProcess.rpcSend(instRegSvc, "delete_subscription", contentMap)

	    // Extract status from message
        if (msgin.hasDataObject()) {

        	DataObject dobj = msgin.extractDataObject()

	        // TODO

	        /*

	        Transform message payload to status message

	        */
        }

        BootstrapIONService.baseProcess.ackMessage(msgin)

	    return status
	}

	/**
	 * Updates a user's notification
	 *
	 * @param notification The notification to update
	 * @return Returns a status message
	 */
	def String updateNotification(Notification notification) {

		// set default status message
		def status = null

		// Target subscription registry service
		MessagingName instRegSvc = new MessagingName(SYSNAME, "notification_registry")

		// Establish command map
		def commandMap = [
				'notificationId': notification.notificationId,
				'datasetId': notification.dataResourceId,
				'regarding': notification.deliveryChannelId,
				'status': notification.subscriptionName,
				'notification': notification.source,
				'date': notification.subscriptionDate,
				'from': notification.deliveryMode,
				'body': notification.deliveryFrequency
		]

		// Establish content map
		def contentMap = ['commandMap': commandMap]

		// Make the service call
        IonMessage msgin = BootstrapIONService.baseProcess.rpcSend(instRegSvc, "create_subscription", contentMap)

		// Extract UUID from message
        if (msgin.hasDataObject()) {

        	DataObject dobj = msgin.extractDataObject()

	        // TODO

	        /*

	        Transform message payload to UUID

	        */

        }

        BootstrapIONService.baseProcess.ackMessage(msgin)

		return status

	}

	/**
	 * Returns current user's notifications
	 *
	 * @param userId The user's id
	 * @return Returns an Array of notifications
	 */
	def Notification[] getUserNotifications(UUID userId) {

		def notifications = []

		// Target notification registry service
		MessagingName instRegSvc = new MessagingName(SYSNAME, "notification_registry")

		// Establish command map
		def commandMap = [
				'userId': userId
		]

		// Establish content map
		def contentMap = ['commandInput': commandMap]

		// Make the service call
        IonMessage msgin = BootstrapIONService.baseProcess.rpcSend(instRegSvc, "get_userNotifications", contentMap)

		// Extract DataResourceDetail from message
        if (msgin.hasDataObject()) {

        	DataObject dobj = msgin.extractDataObject()

	        // not sure if we'll be getting back a List or Array
        	List resList = (List) dobj.getAttribute("notifications")

	        // TODO

	        /*

	        Transform message payload to Notification[]

	        */
        }

        BootstrapIONService.baseProcess.ackMessage(msgin)

		return notifications
		
	}

	/**
	 * Creates a new subscription for a user
	 *
	 * @param subscription The subscription to create
	 * @return Returns the UUID of the newly created subscription
	 */
	def UUID createSubscription(Subscription subscription) {

		// set default status message
		UUID uuid = null

		// Target subscription registry service
		MessagingName instRegSvc = new MessagingName(SYSNAME, "subscription_registry")

		// Establish command map
		def commandMap = [
				'subscriptionId': subscription.subscriptionId,
				'dataResourceId': subscription.dataResourceId,
				'deliveryChannelId': subscription.deliveryChannelId,
				'subscriptionName': subscription.subscriptionName,
				'source': subscription.source,
				'subscriptionDate': subscription.subscriptionDate,
				'deliveryMode': subscription.deliveryMode,
				'deliveryFrequency': subscription.deliveryFrequency,
				'deliveryChannel': subscription.deliveryChannel
		]

		// Establish content map
		def contentMap = ['commandMap': commandMap]

		// Make the service call
        IonMessage msgin = BootstrapIONService.baseProcess.rpcSend(instRegSvc, "create_subscription", contentMap)

		// Extract UUID from message
        if (msgin.hasDataObject()) {

        	DataObject dobj = msgin.extractDataObject()

	        // TODO

	        /*

	        Transform message payload to UUID

	        */

        }

        BootstrapIONService.baseProcess.ackMessage(msgin)

		return uuid

	}

	/**
	 * Returns current user's subscriptions
	 *
	 * @return Returns an Array of subscriptions
	 */
	def Subscription[] findUserSubscriptions() {

		def subscriptions = []

		// Target resource registry service
		MessagingName instRegSvc = new MessagingName(SYSNAME, "resource_registry")

		// Establish map
		def commandMap = [
				'userId': userId
		]

		// Establish content map
		def contentMap = ['commandInput':commandMap]

		// Make the service call
        IonMessage msgin = BootstrapIONService.baseProcess.rpcSend(instRegSvc, "find_dataResources", contentMap)

		// Extract Subscription[] from message
        if (msgin.hasDataObject()) {

        	DataObject dobj = msgin.extractDataObject()

	        // not sure if we'll be getting back a List or Array
        	List resList = (List) dobj.getAttribute("subscriptions")

	        // TODO

	        /*

	        Transform resList to subscriptions Array

	        */
        }

        BootstrapIONService.baseProcess.ackMessage(msgin)

	    return dataResources
	}

	/**
	 * Creates a download URL
	 *
	 * @param dataResourceId The data resource id
	 * @return Returns a status message
	 */
	def String createDownloadUrl(UUID dataResourceId) {

		// Set default status message
		def status = "error creating download URL"

		// Target resource registry service
		MessagingName instRegSvc = new MessagingName(SYSNAME, "resourc_registry")

		// Establish command map
		def commandMap = [
				'dataResourceId': dataResourceId
		]

		// Establish content map
		def contentMap = ['commandMap': commandMap]

		// Make the service call
        IonMessage msgin = BootstrapIONService.baseProcess.rpcSend(instRegSvc, "create_downloadUrl", contentMap)

		// Extract status from message
        if (msgin.hasDataObject()) {

        	DataObject dobj = msgin.extractDataObject()

	        // TODO
	        
	        /*
	        
	        Transform message payload to status

	        */
        }

        BootstrapIONService.baseProcess.ackMessage(msgin)

		return status
	}

	/**
	 * Returns current the Data resource detail of a data resource
	 *
	 * @param dataResourceId The data resource id
	 * @param detailType The detailType (SUMMARY, METADATA, VARIABLE_NAME, VARIABLE_EXTENTS, VISUALIZATIONS
	 * @return Returns data detail of a data resource
	 */
	def DataResourceDetail getDataResourceDetail(UUID dataResourceId, String detailType) {

		DataResourceDetail dataResourceDetail = null

		// Target resource registry service
		MessagingName instRegSvc = new MessagingName(SYSNAME, "resource_registry")

		// Establish command map
		def commandMap = [
				'dataResourceId': dataResourceId,
				'detailType': detailType
		]

		// Establish content map
		def contentMap = ['commandInput': commandMap]

		// Make the service call
        IonMessage msgin = BootstrapIONService.baseProcess.rpcSend(instRegSvc, "get_dataResourceDetail", contentMap)

		// Extract DataResourceDetail from message
        if (msgin.hasDataObject()) {
	        
        	DataObject dobj = msgin.extractDataObject()

	        // TODO

	        /*
	        
	        Transform message payload to DataResourceDetail

	        */
        }

        BootstrapIONService.baseProcess.ackMessage(msgin)

		return dataResourceDetail
	}

	/**
	 * Creates a new data resource and publishes it into the OOI
	 *
	 * @param dataResource The data resource to be created
	 * @return Returns the unique id of new data resource
	 */
    def UUID createDataResource(DataResource dataResource) {

	    UUID uuid = null

	    // Target resource registry service
		MessagingName instRegSvc = new MessagingName(SYSNAME, "resource_registry")

	    // Establish command map
		def commandMap = [
				'dataResourceId':dataResource.dataResourceId,
				'published':dataResource.published,
				'provider':dataResource.provider,
				'format':dataResource.format,
				'protocol':dataResource.protocol,
				'type':dataResource.type,
				'title':dataResource.title,
				'dataFormat':dataResource.dataFormat,
				'dataType':dataResource.dataType,
				'namingAuhority':dataResource.namingAuthority,
				'summary':dataResource.summary,
				'publisherInstitution':dataResource.publisherInstitution,
				'publisherName':dataResource.publisherName,
				'publisherEmail':dataResource.publisherEmail,
				'publisherWebsite':dataResource.publisherWebsite,
				'creatorInstitution':dataResource.creatorInstitution,
				'creatorName':dataResource.creatorName,
				'creatorEmail':dataResource.creatorEmail,
				'openDAP':dataResource.openDAP,
				'wcs':dataResource.wcs,
				'ncml':dataResource.ncml,
				'uddc':dataResource.uddc,
				'iso':dataResource.iso
		]

	    // Establish content map
		def contentMap = ['commandInput': commandMap]

	    // Make the service call
        IonMessage msgin = BootstrapIONService.baseProcess.rpcSend(instRegSvc, "create_dataResource", contentMap)

	    // Extract UUID from message
        if (msgin.hasDataObject()) {

        	DataObject dobj = msgin.extractDataObject()

	        // TODO

	        /*

	        Transform message payload to UUID

	        */
        }

        BootstrapIONService.baseProcess.ackMessage(msgin)

		return uuid

    }

	/**
	 * Updates metadata related to a data resource
	 * 
	 * @param dataResource The data resource to update
	 * @return Returns a status message
	 */
	def String updateDataResource(DataResource dataResource) {

		def status = null

		// Target resource registry service
		MessagingName instRegSvc = new MessagingName(SYSNAME, "resource_registry")

		// Establish command map
		def commandMap = [
				'dataResourceId':dataResource.dataResourceId,
				'published':dataResource.published,
				'provider':dataResource.provider,
				'format':dataResource.format,
				'protocol':dataResource.protocol,
				'type':dataResource.type,
				'title':dataResource.title,
				'dataFormat':dataResource.dataFormat,
				'dataType':dataResource.dataType,
				'namingAuhority':dataResource.namingAuthority,
				'summary':dataResource.summary,
				'publisherInstitution':dataResource.publisherInstitution,
				'publisherName':dataResource.publisherName,
				'publisherEmail':dataResource.publisherEmail,
				'publisherWebsite':dataResource.publisherWebsite,
				'creatorInstitution':dataResource.creatorInstitution,
				'creatorName':dataResource.creatorName,
				'creatorEmail':dataResource.creatorEmail,
				'openDAP':dataResource.openDAP,
				'wcs':dataResource.wcs,
				'ncml':dataResource.ncml,
				'uddc':dataResource.uddc,
				'iso':dataResource.iso
		]

		// Establish content map
		def contentMap = ['commandInput': commandMap]

		// Make the service call
        IonMessage msgin = BootstrapIONService.baseProcess.rpcSend(instRegSvc, "update_dataResource", contentMap)

		// Extract status from message
        if (msgin.hasDataObject()) {

        	DataObject dobj = msgin.extractDataObject()

	        // TODO

	        /*

	        Transform message payload to status message

	        */

        }

        BootstrapIONService.baseProcess.ackMessage(msgin)

		return status
		
	}

	/**
	 * Deletes a data resource
	 *
	 * @param dataResourceId The id of the data resource to delete
	 * @return Returns the status of the delete operation
	 */
	def String deleteDataResource(UUID dataResourceId) {

		// Set default status message
		def status = 'no status returned when deleting a data resource'

		// Target resource registry
	    MessagingName instRegSvc = new MessagingName(SYSNAME, "resource_registry")

		// Establish command map
		def commandMap = ['dataResourceId': dataResourceId]
		// Establish content map
		def contentMap = ['commandInput': commandMap]

		// Make the service call
        IonMessage msgin = BootstrapIONService.baseProcess.rpcSend(instRegSvc, "delete_dataResource", contentMap)

		// Extract status from message
        if (msgin.hasDataObject()) {

        	DataObject dobj = msgin.extractDataObject()

	        // TODO

	        /*

	        Transform message payload to status message
	        
	        */
        }

        BootstrapIONService.baseProcess.ackMessage(msgin)

	    return status

    }

	/**
	 * Finds data resources given a user's id, publish status, spatial extent properties and temporal extent properties 
	 *
	 * @param userId The user making this request
	 * @param published A user can search for published and non-published dataResources: possible values are: "yes" and "no"
	 * @param spatial A user can search by spatial extent 
	 * @param temporal A user can search by temporal extent
	 * @return Returns a list of dataResources that match the specified parameters
	 */
	def DataResource[] findDataResources(UUID userId, String published, Spatial spatial, Temporal temporal) {

	    def dataResources = []

		// Target resource registry service
		MessagingName instRegSvc = new MessagingName(SYSNAME, "resource_registry")

		// Establish command map
		def commandMap = [
				'userId': userId,
				'published': published,
				'spatial': spatial,
				'temporal', temporal
		]

		// Establish content map
		def contentMap = ['commandInput':commandMap]

		// Make the service call
        IonMessage msgin = BootstrapIONService.baseProcess.rpcSend(instRegSvc, "find_dataResources", contentMap)

		// Extract DataResource[] from message
        if (msgin.hasDataObject()) {

        	DataObject dobj = msgin.extractDataObject()
	        
	        // TODO

	        /*

	        Transform message payload to DataResource[]
	        
	        */

        }

        BootstrapIONService.baseProcess.ackMessage(msgin)

	    return dataResources

    }

}
