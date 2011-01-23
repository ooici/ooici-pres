package cilogon;

import org.cilogon.portal.PortalEnvironment;
import org.cilogon.portal.config.cli.PortalConfiguration;
import org.cilogon.portal.storage.PortalStoreFactory;
import org.cilogon.portal.storage.sql.PortalTransactionStore;
import org.cilogon.portal.util.PortalParameters;
import org.cilogon.servlet.AbstractServlet;
import org.cilogon.util.CILogon;
import org.cilogon.util.exceptions.ConfigurationException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

import static cilogon.ConfigConstants.CONFIG_FILE_NAME;
import static cilogon.ConfigConstants.CONFIG_FILE_PATH;

/**
 * <p>Created by Jeff Gaynor<br>
 * on Jun 25, 2010 at  11:56:26 AM
 */
abstract public class PortalAbstractServlet extends AbstractServlet {

    /**
     * Clear the CILogon portal cookie. This way if there is an error the user won't get a
     * stale one with a possible exception if a pending transaction has expired.
     * <br><br> clears the cert request id cookie and returns the currently set value for it.
     * This
     * @param request
     * @param response
     */
    protected String clearCookie(HttpServletRequest request, HttpServletResponse response){
           Cookie[] cookies = request.getCookies();
        String identifier = null;
        
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(CILogon.CERT_REQUEST_ID)) {
                identifier = cookie.getValue();
                // This removes the cookie since we are done with it.
                // This way if the user surfs to another portal there won't
                // be a cookie clash.
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        return identifier;
    }
	
    public PortalEnvironment getPortalEnvironment() throws IOException{
        if(portalEnvironment == null){
            debug("creating a new portal environment");
            portalEnvironment = new PortalEnvironment();
            portalEnvironment.setConfiguration(getConfiguration());
        }
        return portalEnvironment;
    }

    /*
    Having the portal environment static is what permits us to bootstrap and configure all the
    servlets in this web app. we can have multiple environments, but Tomcat allows no easy way
    to manage them.
     */
    static PortalEnvironment portalEnvironment;

    static PortalConfiguration portalConfiguration;

    // This can be configured either by setting the config file location in the web.xml file OR
    // by letting the system try to get it from the default location of WEB-INF/cfg.rdf

    protected PortalConfiguration getConfiguration() throws IOException{
        if (portalConfiguration == null) {
            portalConfiguration = new PortalConfiguration();
            boolean cfgFound = false;
            setDebugOn(true);
            debug("Getting configuration file = " + CONFIG_FILE_PATH + CONFIG_FILE_NAME);
            InputStream is = getServletContext().getResourceAsStream(CONFIG_FILE_PATH + CONFIG_FILE_NAME);
            if (is != null) {
                portalConfiguration.deserialize(getServletContext().getResourceAsStream(CONFIG_FILE_PATH + CONFIG_FILE_NAME));
                cfgFound = true;
            }
            debug("Read config ? " + cfgFound);

            if (!cfgFound) {
                throw new ConfigurationException("Error:No configuration found.");
            }
            // here is where we make an integrity check on the configuration.
            if (portalConfiguration.getRoot() == null) {
                throw new ConfigurationException("Error: No root for the configuration found.");
            }

            debug("reading cfg file, root = " + portalConfiguration.getRoot());

        }
        debug("Got a configuration = " + portalConfiguration);
        return portalConfiguration;
    }

    static PortalStoreFactory portalStoreFactory;

    /**
     *  This forces a completely new reload of all the configuration
     */
    public static void resetState(){
        portalEnvironment = null;
        portalConfiguration = null;

    }


    protected PortalTransactionStore getStore() throws IOException, ServletException {
        return getPortalEnvironment().getTransactionStore();
    }

    public PortalParameters getPortalParameters() throws IOException, ServletException {
       return  getPortalEnvironment().getPortalParameters();
    }

}
