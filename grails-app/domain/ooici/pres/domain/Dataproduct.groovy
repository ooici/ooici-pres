package ooici.pres.domain

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
