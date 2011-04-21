package ooici.pres.domain

import java.util.Map;

import org.codehaus.groovy.grails.commons.ConfigurationHolder;

class LoginController {

	/**
	 * Default action; redirects to CILogon process.
	 */
	def index = {
		def config = ConfigurationHolder.config;
		Map map = ConfigurationHolder.getConfig().flatten();

		def cilogonstarturl = (String)map.get("ioncore.cilogonstarturl");
		
		redirect(uri:cilogonstarturl);
	}
}
