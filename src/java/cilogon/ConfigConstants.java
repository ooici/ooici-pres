package cilogon;

/**
 * <p>Created by Jeff Gaynor<br>
 * on Aug 5, 2010 at  9:13:02 AM
 */
public interface ConfigConstants {
	
    public static final String CONFIG_STATUS = "configStatus";
    // The following operational codes tell which part of the process we are in.
    public static final int NO_OP = -1;
    public static final int BASIC = 1;
    public static final int STORE_SETUP = 2;
    public static final int POSTGRES_STORE_SETUP = 1000;

    public static final int FILE_STORE_SETUP = 1001;
    public static final int ADMINISTER = 2000;
    String CONFIG_FILE_PATH = "/WEB-INF/";
    String CONFIG_FILE_NAME = "cfg.rdf";
    String TEMP_DIRECTORY_NAME = "temp";
    public static final String STORE_TYPE = "storeType";
    public static final String ADMIN_ACTION = "adminAction";
    public static final String ADMIN_DESTROY = "adminDestroy";
    public static final String ADMIN_CREATE = "adminCreate";
    public static final String ADMIN_DO_NOTHING = "adminNoAction";
    public static final String ADMIN_INITIALIZE = "adminInitialize";
}
