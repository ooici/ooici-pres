package ooici.pres

import ion.core.messaging.MessagingName
import ion.core.messaging.IonMessage
import ion.core.data.DataObject
import ion.resource.ResourceDO
import ooici.pres.domain.DataResource
import ooici.pres.domain.Spatial
import ooici.pres.domain.Temporal
import ion.resource.FindResourceDO
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
	 * @param subscriptionId
	 * @return
	 */
	String deleteSubscription(UUID subscriptionId) {

		def status
		
	    // target resource_registry service
	    MessagingName instRegSvc = new MessagingName(SYSNAME, "subscription_registry")

		ResourceDO subscriptionToDelete = new ResourceDO()
	    // set props from passed dataResource
	    newDataResource.mRegIdentity = subscriptionId
		// make the service call to create a new data resource
        IonMessage msgin = BootstrapIONService.baseProcess.rpcSend(instRegSvc, "delete_subscription", subscriptionToDelete)

	    // get UUID of newly created dataResource
        if (msgin.hasDataObject()) {

        	DataObject dobj = msgin.extractDataObject()
	        if(dobj != null) {
        	    status = (String)dobj.getAttribute("status").toString()
	        }
        }

        BootstrapIONService.baseProcess.ackMessage(msgin)

	    return status
	}

	/**
	 * Updates notification status
	 *
	 * @param notificationID
	 * @return
	 */
	String updateNotification(UUID notificationID) {

		return "status"

	}

	/**
	 * Returns current user's notifications
	 *
	 * @return
	 */
	Notification[] getUserNotifications() {

	}

	/**
	 * Creates a new subscription for a user
	 *
	 * @return
	 */
	UUID createSubscription(Subscription subscription) {

	}

	/**
	 * Returns current user's subscriptions
	 *
	 * @return
	 */
	Subscription[] findUserSubscriptions() {

	}

	/**
	 * Creates a URL for the download of the complete data resource in its current state
	 * from the OOI DAP server
	 *
	 * @return
	 */
	def String createDownloadUrl(UUID dataResourceId) {

	}

	/**
	 * Returns current user's notifications
	 *
	 * @return
	 */
	def DataResourceDetail getDataResourceDetail(UUID dataResourceId, String detailType) {
		
	}

	/**
	 * Creates a new data resource and publishes it into the OOI
	 *
	 * @param dataResource
	 * @return unique id of new data resource
	 */
    def UUID createDataResource(DataResource dataResource) {

	    def dataResources = []
		def uuid = null

	    // target resource_registry service
	    MessagingName instRegSvc = new MessagingName(SYSNAME, "resource_registry")

		ResourceDO newDataResource = new ResourceDO()
	    // set props from passed dataResource
	    newDataResource.mRegIdentity = dataResource.UUID
		// make the service call to create a new data resource
        IonMessage msgin = BootstrapIONService.baseProcess.rpcSend(instRegSvc, "create_dataResource", newDataResource)

	    // get UUID of newly created dataResource
        if (msgin.hasDataObject()) {

        	DataObject dobj = msgin.extractDataObject()
	        if(dobj != null) {
        	    uuid = (UUID)dobj.getAttribute("uuid")
	        }

        }

        BootstrapIONService.baseProcess.ackMessage(msgin)

	    return uuid

    }

	/**
	 * Updates metadata related to a data resource
	 * 
	 * @param dataResource
	 * @return
	 */
	def String updateDataResource(DataResource dataResource) {

		def status = null

		MessagingName instRegSvc = new MessagingName(SYSNAME, "resource_registry")

		// Create and package the resourceDO with the query payload
		ResourceDO updatedResourceDO = new ResourceDO()

		def updatedDataResourceMap = [
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
		
		def contentMap = ['dataResourceInput': updatedDataResourceMap]		

        IonMessage msgin = BootstrapIONService.baseProcess.rpcSend(instRegSvc, "update_dataResource", contentMap)

        if (msgin.hasDataObject()) {

        	DataObject dobj = msgin.extractDataObject()
	        // not sure if we'll be getting back a List or Array
        	status = (String) dobj.getAttribute("status")

	        if(status == null) {
				status = 'status returned was null from updateDataResource'
            }
        }

        BootstrapIONService.baseProcess.ackMessage(msgin)

		return status
		
	}

	/**
	 * Deletes a data resource
	 *
	 * @param dataResourceId The id of the data resource to delete
	 * @return returns the status of the delete operation
	 */
	def String deleteDataResource(UUID dataResourceId) {

		def status = 'no status returned when deleting a data resource'

		// target resource registry
	    MessagingName instRegSvc = new MessagingName(SYSNAME, "resource_registry")

        ResourceDO dataResourceToDelete = new ResourceDO()
		// make the service call to delete a data resource
        IonMessage msgin = BootstrapIONService.baseProcess.rpcSend(instRegSvc, "delete_dataResource", UUID)

        if (msgin.hasDataObject()) {

        	DataObject dobj = msgin.extractDataObject()

	        if(dobj != null) {
				status = dobj.getAttribute("status").toString()
            }
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

		MessagingName instRegSvc = new MessagingName(SYSNAME, "instrument_registry")

		// Create and package the resourceDO with the query payload
		ResourceDO resourceDO = new ResourceDO()

		resourceDO.addAttribute('userId', userId)
		resourceDO.addAttribute('published', published)
		resourceDO.addAttribute('spatial', spatial)
		resourceDO.addAttribute('temporal', temporal)

		// send the resourceDO, set ignoreDefaults to false, set regex to false, set List of additional params (null for now)
		FindResourceDO findBy = new FindResourceDO(resourceDO, false, false, null)

        IonMessage msgin = BootstrapIONService.baseProcess.rpcSend(instRegSvc, "find_dataResources", findBy)

        if (msgin.hasDataObject()) {

        	DataObject dobj = msgin.extractDataObject()
	        // not sure if we'll be getting back a List or Array
        	List resList = (List) dobj.getAttribute("resources")

	        if(resList != null) {
				for (Iterator it = resList.iterator(); it.hasNext();) {
					ResourceDO resobj = (ResourceDO) it.next()
					dataResources << resobj
				}
            }
	        else {
		        println '\n***** RESOURCE LIST IS NULL'
	        }
        }

        BootstrapIONService.baseProcess.ackMessage(msgin)

	    return dataResources

    }

}
