class BootStrap {

	def BootstrapIONService
	
    def init = { servletContext ->
	    // Initiating ION Messaging process
	    BootstrapIONService.bootstrap()
	    
    }
    def destroy = {
		// Tear down ION Messaging process
		BootstrapIONService.destroy()
    }
}
