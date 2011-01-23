<%--
  Created by IntelliJ IDEA.
  User: ncsa
  Date: Aug 7, 2010
  Time: 9:14:32 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="static org.cilogon.portal.config.rdf.PortalVocabulary.*" %>
<%@ page import="static cilogon.ConfigConstants.FILE_STORE_SETUP" %>
<%@ page import="static cilogon.ConfigConstants.CONFIG_STATUS" %>


<%
    FileStoreModel fileStoreModel = null;
    String dataPath = null;
    String indexPath = null;
    boolean createFileStore = true;
    say("starting file store setup");
    StoreModel storeModel1 = root.getStore();
    if (storeModel1 != null) {
        // FIXME. Better logic in case the user changes his or her mind about the store type. So there might be some other store here to replace.
        if (!storeModel1.isA(FILE_STORE_TYPE)) {
            root.removeStore(storeModel1);
            createFileStore = true;
        } else {
            fileStoreModel =
                    (FileStoreModel) portalConfiguration.getMyThingSession().fetchThing(root.getStore().getSubject(), FileStoreModel.class);
             createFileStore = false;
        }
    }
    if (createFileStore) {
        say("creating new file store");
        fileStoreModel = portalConfiguration.createFileStore(application.getRealPath(CONFIG_FILE_PATH + TEMP_DIRECTORY_NAME));
        root.setStore(fileStoreModel);
        // we will set the defaults here
    }
    saveConfig(application, portalConfiguration);

    dataPath = fileStoreModel.getDataPath();
    indexPath = fileStoreModel.getIndexPath();
%>
<html>
<head><title>File store setup</title></head>
<body>
<h3>Configuring the file store.</h3>

<p>This requires two paths. The first is a directory where transactions will be stored and the second is for
    indices. The defaults will place these in the current WEB-INF directory, which should be sufficient for
    most cases.<br>
    <b><i>Note</i></b>: If the paths do not exist, they will be created as needed. Be sure you have proper permissions for any
    directories you list

</p>

<form action="<%= actionToTake %>" method="GET">
    <table>
        <tr>
            <td ALIGN="right">Data directory:</td>
            <td><input type="text" size="100" name="<%= FILE_DATA_PATH %>" value=<%= dataPath %> /></td>
        </tr>
        <tr>
            <td ALIGN="right">Index directory:</td>
            <td><input type="text" size="100" name="<%= FILE_INDEX_PATH %>" value=<%= indexPath %> />
            </td>
        </tr>
    </table>
    <input type="submit" value="Submit"/>
    <input type="hidden" id="<%= CONFIG_STATUS %>" name="<%= CONFIG_STATUS %>" value=<%= FILE_STORE_SETUP %> />
</form>
</body>
</html>