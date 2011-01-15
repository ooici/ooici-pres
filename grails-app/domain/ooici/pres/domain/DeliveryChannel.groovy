package ooici.pres.domain

/**
 * @author Stephen Pasco
 *
 * A delivery channel
 */
public enum DeliveryChannel {

	WEBUI("webui"),
	DISPATCHER("dispatcher"),
	EMAIL("email"),
	RSS("rss")

	private final String value

	DeliveryChannel(String value) {
		this.value = value
	}

	String getValue() {
		value
	}

	static list() {
		[WEBUI, DISPATCHER, EMAIL, RSS]
	}
}