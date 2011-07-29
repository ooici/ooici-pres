package cilogon;

import edu.uiuc.ncsa.csd.delegation.client.DelegationService;
import edu.uiuc.ncsa.csd.delegation.storage.TransactionStore;
import edu.uiuc.ncsa.csd.delegation.token.TokenFactory;
import edu.uiuc.ncsa.csd.exceptions.ConfigurationException;
import edu.uiuc.ncsa.csd.oauth_1_0a.OAuthTokenFactory;
import edu.uiuc.ncsa.csd.servlet.AbstractServlet;
import org.cilogon.portal.PortalEnvironment;
import org.cilogon.portal.config.cli.PortalConfigurationDepot;
import org.cilogon.portal.config.rdf.PortalRoot;
import org.cilogon.portal.util.PortalParameters;
import org.cilogon.rdf.CILogonConfiguration;
import org.cilogon.util.CILogon;
import org.tupeloproject.kernel.OperatorException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
     *
     * @param request
     * @param response
     */
    protected String clearCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String identifier = null;
        if (cookies != null) {
            // if there are no cookies (usually because the user surfed into a random page) then
            // exit gracefully rather than just giving some big null pointer stack trace.
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
        }
        return identifier;
    }

    public static PortalEnvironment getPortalEnvironment() throws IOException {
        return (PortalEnvironment) getEnvironment();
    }

    @Override
    public void loadEnvironment() throws IOException {
        setDebugOn(true);
        debug("creating a new portal environment");
        setEnvironment(new PortalEnvironment());
        getEnvironment().setConfiguration(getConfiguration());
    }


    static PortalConfigurationDepot configurationDepot;

    protected CILogonConfiguration getConfiguration() throws IOException {
        return getConfigurationDepot().getCurrentConfiguration();
    }
    // This can be configured either by setting the config file location in the web.xml file OR
    // by letting the system try to get it from the default location of WEB-INF/cfg.rdf

    protected PortalConfigurationDepot getConfigurationDepot() throws IOException {
        if (configurationDepot == null) {
            InputStream is = null;
            setDebugOn(true);
            // Figure out if there has been a configuration file set first
            String configFile = getServletContext().getInitParameter(PortalConfigurationDepot.CONFIG_FILE_PROPERTY);
            if (configFile != null && configFile.length() != 0) {
                File f = new File(configFile);
                debug("file = " + f.getCanonicalPath());
                debug("file exists? " + f.exists());
                if (!f.exists()) {
                    throw new ConfigurationException("Error bootstrapping system: The configuration file \"" + f.getCanonicalPath() + "\" does not exist.");
                }
                //configurationDepot.deserialize(configFile);
                is = new FileInputStream(f);
            } else {
                debug("Getting configuration file = " + CONFIG_FILE_PATH + CONFIG_FILE_NAME);
                is = getServletContext().getResourceAsStream(CONFIG_FILE_PATH + CONFIG_FILE_NAME);
                if (is == null) {
                    throw new ConfigurationException("Error:No configuration found.");
                }
            }
            configurationDepot = new PortalConfigurationDepot(is);
            /* if(configurationDepot.getCurrentConfiguration() == null){
                // so more than one configuration, start snooping.

            }*/
            // here is where we make an integrity check on the configuration.


            try {
                List<PortalRoot> roots = configurationDepot.listPortalRoots();
                String configName = getServletContext().getInitParameter(PortalConfigurationDepot.CONFIG_NAME_PROPERTY);
                if (configName != null && configName.length() != 0) {
                    // got one that is not trivial
                    debug("looking for root  = " + configName);
                    boolean gotRoot = false;
                    for (PortalRoot root : roots) {
                        if (root.getLabel().equals(configName)) {
                            gotRoot = true;
                            configurationDepot.setRoot(root);
                            break;
                        }
                    }
                    if (!gotRoot) {
                        throw new ConfigurationException("Error no configuration found with name \"" + configName + "\"");
                    }
                } else {
                    // note that getRoot throws an exception is there are multiple configuration in this file.
                    debug("reading cfg file, root = " + configurationDepot.findRoot());
                }


            } catch (OperatorException e) {
                throw new ConfigurationException("Error getting roots", e);
            }
            if (configurationDepot.findRoot() == null) {
                throw new ConfigurationException("Error: No root for the configuration found.");
            }


        }
        debug("Got a configuration = " + configurationDepot);
        return configurationDepot;
    }

    @Override
    public void resetState() {
        super.resetState();
        configurationDepot = null;
    }

    protected TransactionStore getStore() throws IOException, ServletException {
        return getPortalEnvironment().getTransactionStore();
    }

    public PortalParameters getPortalParameters() throws IOException, ServletException {
        return getPortalEnvironment().getPortalParameters();
    }

    TokenFactory tokenFactory;

    public TokenFactory getTokenFactory() {
        if (tokenFactory == null) {
            tokenFactory = new OAuthTokenFactory();
        }
        return tokenFactory;
    }

    public DelegationService getDelegationService() throws IOException {
        return getPortalEnvironment().getDelegationService();
    }


}
