import ooici.pres.domain.Role
import ooici.pres.domain.User
import ooici.pres.domain.UserRole

class BootStrap {

	def springSecurityService
	def BootstrapIONService
	
    def init = { servletContext ->

	    // Setting up Users and their roles
//	    def adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)
//		def userRole = new Role(authority: 'ROLE_USER').save(flush: true)
//
//		String adminPassword = springSecurityService.encodePassword('admin')
//	    String testPassword = springSecurityService.encodePassword('test')
//
//		def adminUser = new User(username: 'admin', enabled: true, password: adminPassword)
//		def testUser = new User(username: 'test', enabled: true, password: testPassword)
//
//	    adminUser.save(flush: true)
//		testUser.save(flush: true)
//
//	    UserRole.create adminUser, adminRole, true
//		UserRole.create testUser, userRole, true
//
//		assert User.count() == 2
//		assert Role.count() == 2
//
//		assert UserRole.count() == 2

	    // Initiating ION Messaging Base Process
	    BootstrapIONService.bootstrap()
	    
    }
    def destroy = {
    }
}
