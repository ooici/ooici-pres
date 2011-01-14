package ooici.pres.domain

/**
 * @author Stephen Pasco
 */
class DataResourceDetail {

	UUID dataResourceId
	DetailType detailType
	String body // structured body content

    static constraints = {
    }
}
