package ooici.pres.domain

/**
 * @author Stephen Pasco
 *
 * Published types
 */
public enum Published {

	YES("published"),
	NO("not published")

	private final String value

	Published(String value) {
		this.value = value
	}

	String getValue() {
		value
	}

	static list() {
		[YES, NO]
	}

}