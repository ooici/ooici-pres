package ooici.pres.domain

/**
 * @author Stephen Pasco
 */
class DataResource {

	UUID dataResourceId
	Published published
	String provider
	String format
	String protocol
	String type
	String title
	String dataFormat
	String dataType
	String namingAuthority
	String summary
	String publisherInstitution
	String publisherName
	String publisherEmail
	String publisherWebsite
	String creatorInstitution
	String creatorName
	String creatorEmail
	String openDAP
	String wcs
	String ncml
	String uddc
	String iso
	Date lastUpdated
	Date dateCreated

    static constraints = {
    }
}
