package ooici.pres

import ion.core.messaging.MessagingName
import ion.core.messaging.IonMessage
import ion.core.data.DataObject
import ion.resource.ResourceDO
import ooici.pres.domain.DataResource
import ooici.pres.domain.Spatial
import ooici.pres.domain.Temporal
import ooici.pres.domain.Published
import ion.resource.FindResourceDO
import ooici.pres.domain.DataResourceDetail
import ooici.pres.domain.DetailType
import ooici.pres.domain.Subscription
import ooici.pres.domain.Notification
import ooici.pres.domain.Status

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
	Status deleteSubscription(UUID subscriptionId) {

	}

	/**
	 * Updates notification status
	 *
	 * @param notificationID
	 * @return
	 */
	Status updateNotification(UUID notificationID) {

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
	def DataResourceDetail getDataResourceDetail(UUID dataResourceId, DetailType detailType) {
		
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
	def Status updateDataResource(DataResource dataResource) {
		
	}

	/**
	 * Deletes a data resource
	 *
	 * @param dataResourceId The id of the data resource to delete
	 * @return returns the status of the delete operation
	 */
	def Status deleteDataResource(UUID dataResourceId) {

		def status = 'no status returned when deleting a data resource'

		// target resource registry
	    MessagingName instRegSvc = new MessagingName(SYSNAME, "resource_registry")

        ResourceDO dataResourceToDelete = new ResourceDO()
		// make the service call to delete a data resource
        IonMessage msgin = BootstrapIONService.baseProcess.rpcSend(instRegSvc, "delete_dataResource", UUID)

        if (msgin.hasDataObject()) {

        	DataObject dobj = msgin.extractDataObject()

	        if(dobj != null) {
				status = dobj.getAttribute("status")
            }
        }

        BootstrapIONService.baseProcess.ackMessage(msgin)

	    return status

    }

	/**
	 * Finds data resources given a user's id, publish status, spatial extent properties and temporal extent properties 
	 *
	 * @param userId The user making this request
	 * @param published A user can search for published and non-published dataResources
	 * @param spatial A user can search by spatial extent 
	 * @param temporal A user can search by temporal extent
	 * @return Returns a list of dataResources that match the specified parameters
	 */
	def DataResource[] findDataResources(UUID userId, Published published, Spatial spatial, Temporal temporal) {

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
