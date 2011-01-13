package ooici.pres

import ion.core.messaging.MessagingName
import ion.core.messaging.IonMessage
import ion.core.data.DataObject
import ion.resource.ResourceDO
import ooici.pres.domain.DataResource
import ooici.pres.domain.Spatial
import ooici.pres.domain.Temporal
import ion.resource.ListAllQueryDO
import ion.resource.InstrumentRDO
import ion.resource.FindResourceDO

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
	 * Creates a new data resource
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
	 * Deletes a new data resource
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
	 * @param published A user can search for published and not published dataResources
	 * @param spatial A user can search by spatial extent 
	 * @param temporal A user can search by temporal extent
	 * @return Returns a list of dataResources that match the specified parameters
	 */
	def List findDataResources(UUID userId, Enum published, Spatial spatial, Temporal temporal) {

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
