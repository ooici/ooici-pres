package ooici.pres.domain

/**
 * @author Stephen Pasco
 */
class Notification {

	UUID notificationId
	UUID datasetId
	String regarding // dataResource name
	Enum status
	String notification // this is the readable type of notification
	Date date
	String from // readable name of the originator
	String body

    static constraints = {
    }
}
