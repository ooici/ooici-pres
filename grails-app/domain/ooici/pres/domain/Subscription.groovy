package ooici.pres.domain

/**
 * @author Stephen Pasco
 */
class Subscription {

	UUID subscriptionId
	UUID dataResourceId
	UUID deliveryChannelId // id of actual target, such as the dispatcher process id
	String subscriptionName
	String source // the dataResource name
	Date subscriptionDate // date guest signed up
	DeliveryMode deliveryMode
	Integer deliveryFrequency
	DeliveryChannel deliveryChannel

    static constraints = {
    }
}
