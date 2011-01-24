<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="setupErrorPage.jsp" %>
<%@ page import="org.cilogon.portal.util.PortalParameters" %>
<%@ page import="static org.cilogon.portal.config.rdf.PortalVocabulary.*" %>
<%@ page import="static cilogon.ConfigConstants.*" %>

<%
    if (root.hasA(HAS_PORTAL_PARAMETERS)) {
        ppm = root.getPortalParametersModel();
    } else {
        ppm = portalConfiguration.createPortalParameters();
        root.setPortalParametersModel(ppm);
        int localPort = -1;
        if (request.getLocalPort() != 8080) {
            localPort = request.getLocalPort();
        }
        // fiddle with the context paths to make this look about right. Not perfect, but should save the
        // user some typing.
        String head = "://" + request.getLocalName() + (localPort == -1 ? "" : ":" + localPort) + request.getContextPath() + "/";
        ppm.setCallback("https" + head + "ready");
        ppm.setFailure("https" + head + "failure");
        ppm.setSuccess("https" + head + "success");
    }
    PortalParameters pp = ppm.toPortalParameters();

    // regardless of what we do, we should save our state before continuing.
    saveConfig(application, portalConfiguration);

    System.out.println("Portal parameters: name = " + pp.getName());
%>
<html>
<head><title>Setup</title></head>
<body>
<h2>CILogon Delegation Service Configuration</h2>

<h3>Portal callback parameters and name</h3>

<form action="<%= actionToTake %>" method="GET">
    <table>
        <tr>
            <td ALIGN="right">The callback URL:</td>
            <td><input type="text" size="100" name="<%= PORTAL_CALLBACK_URI %>" value="<%= pp.getCallback() %>"/></td>
        </tr>
        <tr>
            <td ALIGN="right">The success URL:</td>
            <td><input type="text" size="100" name="<%= PORTAL_SUCCESS_URI %>" value="<%= pp.getSuccess() %>"/></td>
        </tr>
        <tr>
            <td ALIGN="right">The failure URL:</td>
            <td><input type="text" size="100" name="<%= PORTAL_FAILURE_URI %>" value="<%= pp.getFailure() %>"/></td>
        </tr>
        <tr>
            <td ALIGN="right">Portal name:</td>
            <td><input type="text" size="100" name="<%= PORTAL_NAME %>" value="<%= pp.getName() %>"/></td>
        </tr>
        <tr>
            <td ALIGN="right">Skin directory:</td>
            <td><input type="text" size="100" name="<%= PORTAL_SKIN %>" value="<%= pp.getSkin() %>"/></td>
        </tr>
    </table>

    <h3>Select the type of the store</h3>

    <input type="radio" name="<%= STORE_TYPE %>" value="<%= MEMORY_STORE_TYPE %>" checked/>Memory<br/>
    <input type="radio" name="<%= STORE_TYPE %>" value="<%= FILE_STORE_TYPE %>"/>File<br/>
    <input type="radio" name="<%= STORE_TYPE %>" value="<%= POSTGRES_STORE_TYPE %>"/>Postgres<br/>
    <input type="submit" value="Submit"/>

    <input type="hidden" id="<%= CONFIG_STATUS %>" name="<%= CONFIG_STATUS %>" value="<%= BASIC %>"/>

</form>

</body>
</html>