package ooici.pres.domain

/**
 * @author Stephen Pasco
 */
public enum DetailType {

	SUMMARY("summary"),
	METADATA("metadata"),
	VARIABLE_NAME("variable name"),
	VARIABLE_EXTENTS("variable extents"),
	VISUALIZATIONS("visualizations")

	private final String value

	DetailType(String value) {
		this.value = value
	}

	String getValue() {
		value
	}

	static list() {
		[SUMMARY, METADATA, VARIABLE_NAME, VARIABLE_EXTENTS, VISUALIZATIONS]
	}
}