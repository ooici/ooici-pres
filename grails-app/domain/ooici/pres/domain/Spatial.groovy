package ooici.pres.domain

/**
 * @author Stephen Pasco
 *
 * Represents spatial extents used when querying for data resources 
 */
class Spatial {

	String north
	String south
	String east
	String west
	String altitudeMin
	String altitudeMax

    static constraints = {
    }
}
