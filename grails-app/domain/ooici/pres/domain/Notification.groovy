package ooici.pres.domain

/**
 * @author Stephen Pasco
 *
 * An OOI notification
 */
class Notification {

	int notificationId
	int datasetId
	String regarding // dataResource name
	String status
	String notification // this is the readable type of notification
	Date date
	String source // readable name of the originator
	String body

    static constraints = {
    }
}
