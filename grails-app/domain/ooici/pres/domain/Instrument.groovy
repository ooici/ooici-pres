package ooici.pres.domain

/**
 * @author Stephen Pasco
 *
 * An OOI resource
 */
class Instrument {

	String registryId
	String name
	String model
	String manufacturer
	String serialNum
	String fwVersion

	static transients = ['registryId']

    static constraints = {
	    registryId blank:true
	    name blank:false
	    model blank:false
	    manufacturer blank:false
	    serialNum blank:false
	    fwVersion blank:false
    }
}
