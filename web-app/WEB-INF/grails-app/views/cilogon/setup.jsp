<%--
  Date: Aug 4, 2010
  Time: 9:14:58 AM
--%>
<%@ page errorPage="setupErrorPage.jsp" %>

<%@ page import="org.cilogon.portal.config.cli.PortalConfiguration" %>
<%@ page import="org.cilogon.portal.config.models.PortalParametersModel" %>
<%@ page import="org.cilogon.portal.config.rdf.PortalRoot" %>
<%@ page import="org.cilogon.portal.storage.PortalStoreFactory" %>
<%@ page import="org.cilogon.util.Initializable" %>
<%@ page import="static org.cilogon.util.AbstractIdentifierFactory.uriRef" %>
<%@ page import="static org.cilogon.portal.config.rdf.PortalVocabulary.*" %>
<%@ page import="static org.cilogon.rdf.Vocabulary.*" %>
<%@ page import="static cilogon.ConfigConstants.*" %>
<%@ page import="org.cilogon.util.MyLogger" %>
<%@ page import="org.tupeloproject.kernel.OperatorException" %>
<%@ page import="org.tupeloproject.rdf.Resource" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.IOException" %>

<%!
    protected String getParam(HttpServletRequest request, Resource key) {
        return request.getParameter(key.toString());
    }

    protected PortalConfiguration getConfig(ServletContext context) throws IOException, OperatorException {
        File f = new File(context.getRealPath(CONFIG_FILE_PATH + CONFIG_FILE_NAME));
        //InputStream inputStream = context.getResourceAsStream(CONFIG_FILE_PATH + CONFIG_FILE_NAME);
        PortalConfiguration portalConfiguration = null;
        if (f.exists()) {
            portalConfiguration = new PortalConfiguration(f);
        } else {
            portalConfiguration = new PortalConfiguration();
        }
        if (portalConfiguration.getRoot() == null) {
            portalConfiguration.createRoot(uriRef());
        }
        return portalConfiguration;
    }

    protected void saveConfig(ServletContext context, PortalConfiguration portalConfiguration) throws Exception {
        portalConfiguration.save(); // save it to the context before serializing!!!
        File f = new File(context.getRealPath(CONFIG_FILE_PATH + CONFIG_FILE_NAME));
        portalConfiguration.serialize(f);
    }

    MyLogger logger;

    public MyLogger getLogger() {
        if (logger == null) {
            logger = new MyLogger("setup.jsp");
        }
        return logger;
    }

    public void setLogger(MyLogger logger) {
        this.logger = logger;
    }

    public void say(String x) {
        getLogger().info(x);
    }
%>
<%
    PortalConfiguration portalConfiguration = getConfig(application);
    // Hack to get Tomcat5 to work with java 1.6.
    System.setProperty("javax.xml.transform.TransformerFactory", "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
    PortalParametersModel ppm = null;
    PostgresStoreModel postgresStoreModel = null;
    DatabaseModel currentDatabase = null;

    PortalTransactionTableModel tt = null;

    PortalRoot root = (PortalRoot) portalConfiguration.getRoot();
    String x = request.getParameter(CONFIG_STATUS);
    int operation = -1;
    if (x != null) {
        operation = Integer.parseInt(x);
    }
    // read off the portal's path and point all form actions back to this jsp page.
    String actionToTake = request.getContextPath() + "/setup.jsp";
    String setupDoneAction = request.getContextPath() + "/StartRequest";


    switch (operation) {
        default:
        case NO_OP:
            // Nothing has been done yet, so start with the base page
%>
<%@ include file="setupBasic.jsp" %>
<%
        break;
    case BASIC:
        if (request.getParameter(STORE_TYPE) == null) {
            // FIXME no store selected -- do something here??
        }

        if(ppm == null){
            // possible if the user surf in from someplace strange this hasn't been set.
            ppm = root.getPortalParametersModel();
        }
        // Basic setup;
        ppm.setCallback(getParam(request, PORTAL_CALLBACK_URI));
        ppm.setSuccess(getParam(request, PORTAL_SUCCESS_URI));
        ppm.setFailure(getParam(request, PORTAL_FAILURE_URI));
        ppm.setName(getParam(request, PORTAL_NAME));
        say(PORTAL_SKIN + " = \"" + getParam(request, PORTAL_SKIN) + "\"");
        ppm.setSkin(getParam(request, PORTAL_SKIN));
        saveConfig(application, portalConfiguration);
        // Saved basic state. Now see if we have to configure a store. Check the store type the user selected.
        if (request.getParameter(STORE_TYPE).equals(PortalVocabulary.POSTGRES_STORE_TYPE.toString())) {
%>
<%@ include file="setupPostgresStore.jsp" %>
<%
    }

    if (request.getParameter(STORE_TYPE).equals(PortalVocabulary.FILE_STORE_TYPE.toString())) {
        say("Starting file store config");
%>
<%@ include file="setupFileStore.jsp" %>
<%
    }

    if (request.getParameter(STORE_TYPE).equals(PortalVocabulary.MEMORY_STORE_TYPE.toString())) {
        // FIXME if it has some other type of store, replace it with a memory store.
        StoreModel sm0 = root.getStore();
        boolean createMemoryStore = true;
        if (sm0 != null) {
            if (!sm0.isA(MEMORY_STORE_TYPE)) {
                root.removeStore(sm0);
                createMemoryStore = true;
            } else {
                createMemoryStore = false;
            }
        }
        if (createMemoryStore) {
            MemoryStoreModel msm = portalConfiguration.createMemoryStore();
            root.setStore(msm);
        }

        saveConfig(application, portalConfiguration);
%>
<%@ include file="setupDone.jsp" %>
<%
        }


        break;
    case POSTGRES_STORE_SETUP:
        // Case is that the user has configured a postgres store. Save the information.

        postgresStoreModel = new PostgresStoreModel(root.getStore());
        if (postgresStoreModel.hasDatabase()) {
            currentDatabase = postgresStoreModel.getFirstDatabase();
        } else {
            currentDatabase = portalConfiguration.createDatabase();
            postgresStoreModel.addDatabase(currentDatabase);
        }
        currentDatabase.setName(getParam(request, SQL_DATABASE_NAME));
        currentDatabase.setSchema(getParam(request, SQL_DATABASE_SCHEMA));
        if(currentDatabase.getTransactionTable() == null){
            tt = (PortalTransactionTableModel) portalConfiguration.getMyThingSession().fetchThing(AbstractIdentifierFactory.uriRef(), PortalTransactionTableModel.class);
            currentDatabase.setTransactionTable(tt);
        }else{
            say("IN SETUP.JSP, TT != NULL, SUBJECT = " + currentDatabase.getTransactionTable().getSubject());
            tt = (PortalTransactionTableModel) portalConfiguration.getMyThingSession().fetchThing(currentDatabase.getTransactionTable().getSubject(),PortalTransactionTableModel.class);
        }
        tt.setPrefix(getParam(request, SQL_TABLE_PREFIX));
        tt.setName(getParam(request, SQL_TABLE_NAME));


        ConnectionParametersModel cpm = postgresStoreModel.getConnectionParametersModel();
        cpm.setUserName(getParam(request, CONNECTION_USERNAME));
        cpm.setPassword(getParam(request, CONNECTION_PASSWORD));
        cpm.setHost(getParam(request, CONNECTION_HOST));
        String tempInt = getParam(request, CONNECTION_PORT);
        if (tempInt != null) {
            try {
                cpm.setPort(Integer.parseInt(tempInt));
            } catch (NumberFormatException nfe) {
                // can't do anything...
            }
        }
        cpm.setDriver(getParam(request, CONNECTION_DRIVER));

        AdminConnectionParametersModel acpm = postgresStoreModel.getAdminConnectionParametersModel();
        acpm.setAdminUserName(getParam(request, CONNECTION_ADMIN_USERNAME));
        acpm.setAdminPassword(getParam(request, CONNECTION_ADMIN_PASSWORD));
        acpm.setAdminHost(getParam(request, CONNECTION_ADMIN_HOST));
        tempInt = getParam(request, CONNECTION_ADMIN_PORT);
        if (tempInt != null) {
            try {
                acpm.setAdminPort(Integer.parseInt(tempInt));
            } catch (NumberFormatException nfe) {
                // can't do anything...
            }
        }

        acpm.setAdminUserName(getParam(request, CONNECTION_ADMIN_USERNAME));
        acpm.setDriver(getParam(request, CONNECTION_ADMIN_DRIVER));
        saveConfig(application, portalConfiguration);
%>
<%@ include file="setupStorageAdmin.jsp" %>
<%

        break;
    case ADMINISTER:
        PortalStoreFactory portalStoreFactory = new PortalStoreFactory();
        if (!portalStoreFactory.isConfigured()) {
            portalStoreFactory.setConfiguration(portalConfiguration);
        }
        Initializable ppa = portalStoreFactory.getAdminClient();
        String pgAdminAction = request.getParameter(cilogon.ConfigConstants.ADMIN_ACTION);
        if (pgAdminAction == null || pgAdminAction.length() == 0) {
            // Nothing selected.
            // do nothing
        } else {
            if (pgAdminAction.equals(ADMIN_CREATE)) {
                ppa.create();
            }
            if (pgAdminAction.equals(ADMIN_DESTROY)) {
                ppa.destroy();
            }

            if (pgAdminAction.equals(ADMIN_INITIALIZE)) {
                ppa.destroy(); // make sure its gone first.                
                ppa.init();
            }
        }

%>
<%@ include file="setupDone.jsp" %>
<%

        break;
    case FILE_STORE_SETUP:
        FileStoreModel fsm = new FileStoreModel(root.getStore());
        fsm.setDataPath(getParam(request, FILE_DATA_PATH));
        fsm.setLookup(getParam(request, FILE_INDEX_PATH));
        saveConfig(application, portalConfiguration);
%>
<%@ include file="setupStorageAdmin.jsp" %>
<%


            break;

    }
%>

