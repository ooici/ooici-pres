package ooici.pres.domain

/**
 * @author Stephen Pasco
 *
 * A user's role
 */
class Role {

	String authority

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
