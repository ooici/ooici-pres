package ooici.pres.domain

/**
 * @author Stephen Pasco
 *
 * An OOI subscription to a data resource
 */
class Subscription {

	int subscriptionId
	int dataResourceId
	// id of actual target, such as the dispatcher process id
	int deliveryChannelId
	String subscriptionName
	// the dataResource name
	String source
	// date guest signed up
	Date subscriptionDate
	// deliveryModes are: instant, digest
	String deliveryMode
	int deliveryFrequency
	// delivery channel type = WEBUI, DISPATCHER, EMAIL, RSS
	String deliveryChannel

    static constraints = {
    }
}
