package ooici.pres.domain

/**
 * @author Stephen Pasco
 */
class Dataproduct {

	String name
	String dataFormat
	String instrumentId

	static transients = ["instrumentId"]

    static constraints = {
	    name blank:false
	    dataFormat blank:false
	    instrumentId blank:false
    }
}
