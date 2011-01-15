package ooici.pres.domain

/**
 * @author Stephen Pasco
 *
 * Delivery modes
 */
public enum DeliveryMode {

	INSTANT("instant"),
	DIGEST("digest")

	private final String value

	DeliveryMode(String value) {
		this.value = value
	}

	String getValue() {
		value
	}

	static list() {
		[INSTANT, DIGEST]
	}

}