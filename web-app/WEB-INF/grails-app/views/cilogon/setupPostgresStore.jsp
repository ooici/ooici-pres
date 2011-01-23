<%--
  Setup whatever storage is required by the user.
  Date: Aug 5, 2010
  Time: 10:09:53 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.cilogon.portal.config.models.PortalTransactionTableModel" %>
<%@ page import="org.cilogon.portal.config.rdf.PortalVocabulary" %>
<%@ page import="cilogon.ConfigConstants" %>
<%@ page import="org.cilogon.rdf.models.*" %>
<%@ page import="org.cilogon.storage.impl.postgres.PostgresAdminConnectionParameters" %>
<%@ page import="static org.cilogon.config.cli.CLITools.prompt" %>
<%@ page import="org.cilogon.storage.impl.postgres.PostgresConnectionParameters" %>
<%@ page import="org.cilogon.util.AbstractIdentifierFactory" %>
<%@ page import="java.util.Iterator" %>


<%
    boolean createPGStore = true;
    StoreModel sm = root.getStore();
    if (sm != null) {
        if (!sm.isA(POSTGRES_STORE_TYPE)) {
            root.removeStore(sm);
            createPGStore = true;
        } else {
            postgresStoreModel = (PostgresStoreModel) portalConfiguration.getMyThingSession().fetchThing(root.getStore().getSubject(), PostgresStoreModel.class);
            createPGStore = false;
        }
    }
        if (createPGStore) {
            postgresStoreModel = portalConfiguration.createPostgresStore();
            root.setStore(postgresStoreModel);
        }


    Iterator<DatabaseModel> it = postgresStoreModel.getDatabases().iterator();
    currentDatabase = postgresStoreModel.getFirstDatabase();
    if (currentDatabase == null) {
        say("NO DATABASE!! CREATING NEW");
        currentDatabase = portalConfiguration.createDatabase();
        postgresStoreModel.addDatabase(currentDatabase);
        tt = (PortalTransactionTableModel) portalConfiguration.getMyThingSession().fetchThing(AbstractIdentifierFactory.uriRef(), PortalTransactionTableModel.class);
        tt.setPrefix("portal");
        tt.setName("transactions");

        currentDatabase.setTransactionTable(tt);
    } else {
        say("GOT DATABASE, subj = " + currentDatabase.getSubject());
        try {
            Resource pttSubject = null;
            if (currentDatabase.getTransactionTable() != null) {
                pttSubject = currentDatabase.getTransactionTable().getSubject();
                say("HAS PTT = " + pttSubject);
            } else {
                say("DOES **NOT** HAVE PTT");
                pttSubject = AbstractIdentifierFactory.uriRef();
            }
            say("TRYING TO GET PTT, SUBJ =" + pttSubject);            
            tt = (PortalTransactionTableModel) currentDatabase.fetchThingByPredicate(HAS_TRANSACTION_TABLE, PortalTransactionTableModel.class);
            say("GOT PTT");
        } catch (OperatorException e) {
            e.printStackTrace();
        }
    }

    String dbName = currentDatabase.getName();
    String dbSchema = currentDatabase.getSchema();
    String ttName = tt.getName();
    String ttPrefix = tt.getPrefix();

%>

<html>
<head><title>Setup</title></head>
<body>
<h2>Postgres Configuration</h2>

<form action="<%= actionToTake %>" method="GET">

    <h3>Database setup.</h3>

    <table>
        <tr>
            <td ALIGN="right">Database name:</td>
            <td><input type="text" size="100" name="<%= PortalVocabulary.SQL_DATABASE_NAME %>" value=<%= dbName %> />
            </td>
        </tr>
        <tr>
            <td ALIGN="right">Database schema:</td>
            <td><input type="text" size="100" name="<%= PortalVocabulary.SQL_DATABASE_SCHEMA %>" value=<%= dbSchema %> />
            </td>
        </tr>
        <tr>
            <td ALIGN="right">Table name:</td>
            <td><input type="text" size="100" name="<%= PortalVocabulary.SQL_TABLE_NAME %>" value=<%= ttName %> /></td>
        </tr>
        <tr>
            <td ALIGN="right">Table prefix (opt.):</td>
            <td><input type="text" size="100" name="<%= PortalVocabulary.SQL_TABLE_PREFIX %>" value=<%= ttPrefix %> />
            </td>
        </tr>
    </table>

    <%
        PostgresConnectionParameters pcp = new PostgresConnectionParameters();
        if (postgresStoreModel.hasA(HAS_CONNECTION)) {
            postgresStoreModel.getConnectionParametersModel().toConnectionParameters(pcp);
        } else {
            ConnectionParametersModel cpm = portalConfiguration.createConnectionParameters();
            postgresStoreModel.setConnectionParameters(cpm);
            cpm.toConnectionParameters(pcp);
        }
    %>
    <h3>User account setup</h3>

    <table>
        <tr>
            <td ALIGN="right">Username:</td>
            <td><input type="text" size="100" name="<%= PortalVocabulary.CONNECTION_USERNAME%>"
                       value=<%= pcp.getUsername() %> /></td>
        </tr>
        <tr>
            <td ALIGN="right">Password:</td>
            <td><input type="text" size="100" name="<%= PortalVocabulary.CONNECTION_PASSWORD %>"
                       value=<%= pcp.getPassword() %> /></td>
        </tr>
        <tr>
            <td ALIGN="right">Hostname</td>
            <td><input type="text" size="100" name="<%= PortalVocabulary.CONNECTION_HOST %>"
                       value=<%= pcp.getHost() %> /></td>
        </tr>
        <tr>
            <td ALIGN="right">Port:</td>
            <td><input type="text" size="100" name="<%= PortalVocabulary.CONNECTION_PORT%>" value=<%= pcp.getPort() %> />
            </td>
        </tr>
        <tr>
            <td ALIGN="right">JDBC driver:</td>
            <td><input type="text" size="100" name="<%= PortalVocabulary.CONNECTION_DRIVER %>"
                       value=<%= pcp.getJdbcDriver() %> /></td>
        </tr>
    </table>

    <%
        PostgresAdminConnectionParameters pacp = new PostgresAdminConnectionParameters();

        if (postgresStoreModel.hasA(HAS_ADMIN_CONNECTION)) {
            postgresStoreModel.getAdminConnectionParametersModel().toAdminConnectionParameters(pacp);
        } else {
            AdminConnectionParametersModel acpm = portalConfiguration.createAdminConnectionParameters();
            postgresStoreModel.setAdminConnectionParameters(acpm);
            acpm.toAdminConnectionParameters(pacp);
        }
        saveConfig(application, portalConfiguration);

    %>
    <H3>Administrator account setup</h3>
    <table>
        <tr>
            <td ALIGN="right">Admin username:</td>
            <td><input type="text" size="100" name="<%= PortalVocabulary.CONNECTION_ADMIN_USERNAME %>"
                       value=<%= pacp.getUsername() %> /></td>
        </tr>
        <tr>
            <td ALIGN="right">Admin password:</td>
            <td><input type="text" size="100" name="<%= PortalVocabulary.CONNECTION_ADMIN_PASSWORD %>"
                       value=<%= pacp.getPassword() %> /></td>
        </tr>
        <tr>
            <td ALIGN="right">Admin hostname:</td>
            <td><input type="text" size="100" name="<%= PortalVocabulary.CONNECTION_ADMIN_HOST %>"
                       value=<%= pacp.getHost() %> /></td>
        </tr>
        <tr>
            <td ALIGN="right">Admin port:</td>
            <td><input type="text" size="100" name="<%= PortalVocabulary.CONNECTION_ADMIN_PORT %>"
                       value=<%= pacp.getPort() %> /></td>
        </tr>
        <tr>
            <td ALIGN="right">Admin JDBC driver:</td>
            <td><input type="text" size="100" name="<%= PortalVocabulary.CONNECTION_ADMIN_DRIVER %>"
                       value=<%= pacp.getJdbcDriver() %> /></td>
        </tr>
    </table>

    <input type="submit" value="Submit"/>
    <input type="hidden" id="<%= ConfigConstants.CONFIG_STATUS %>"
           name=<%= ConfigConstants.CONFIG_STATUS %> value="<%= ConfigConstants.POSTGRES_STORE_SETUP %>" />

</form>

</body>
</html>