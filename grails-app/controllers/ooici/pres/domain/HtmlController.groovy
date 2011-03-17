package ooici.pres.domain

/**
 * This class serves HTML to jQuery Ajax load() calls
 *
 * @author Stephen Pasco
 */
class HtmlController {

    def index = { }

	/**
	 * Fetches the data resource and returns HTML using a template
	 */
	def fetchDataResourceDetails =  {

		/**
		 * TODO: Implement this piece once we have round-trip Grails to App Service layer calls working
		 */

//		def dr = DataResource.get(params.id)
		// Gets the _dataresourcedetail template and passes it the dataresource object
//		render(template:"dataresourcedetail", model:[dataresource:dr])

		
		render(template:"dataresourcedetail", model:[dataResourceTitle:params.title])

	}
}