package ooici.pres.domain

/**
 * @author Stephen Pasco
 *
 * Meta data of a data resource
 */
class DataResourceDetail {

	UUID dataResourceId
	// DetailTypes include: summary, metadata, variableName, variableExtents, visualizations
	String detailType
	String body // structured body content

    static constraints = {
    }
}
