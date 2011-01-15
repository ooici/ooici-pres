package ooici.pres.domain

/**
 * @author Stephen Pasco
 *
 * Status types
 */
public enum Status {

	// not yet sure what these values will become
	SUCCESS("success"),
	FAIL("fail")

	private final String value

	Status(String value) {
		this.value = value
	}

	def setStatus(String value) {
    	this.value = value
	}

	String getValue() {
		value
	}

	static list() {
		[SUCCESS, FAIL]
	}

}