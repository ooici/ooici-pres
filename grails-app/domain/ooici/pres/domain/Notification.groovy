package ooici.pres.domain

class Notification {

	UUID notificationId
	UUID dataResourceId
	String subscriptionName
	String source
	Date subscriptionDate
	Date subscriptionStart
	Frequency subscriptionFrequency
	Transform transformations
	SubscriptionAggregation aggregations	

    static constraints = {
    }
}
