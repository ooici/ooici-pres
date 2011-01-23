<%--
  User: Jeff Gaynor
  Date: Aug 10, 2010
  Time: 9:38:35 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="setupErrorPage.jsp" %>
<%@ page import="static org.cilogon.portal.config.rdf.PortalVocabulary.*" %>
<%@ page import="static cilogon.ConfigConstants.*" %>

<%

%>
<html>
  <head><title>Simple jsp page</title></head>
  <body>
  <form action="<%= actionToTake %>" method="GET">
        <h3>Administrative actions</h3>
   <p>The options allow you to clear the store, drop it or (re)-initialize the store.</p>

      <input type="radio" name="<%= ADMIN_ACTION %>" value="<%= ADMIN_DO_NOTHING %>"/>Do nothing.<br/>
      <input type="radio" name="<%= ADMIN_ACTION %>" value="<%= ADMIN_CREATE %>" />Clear the store: Remove all entries from an existing store<br/>
      <input type="radio" name = "<%= ADMIN_ACTION %>" value="<%= ADMIN_INITIALIZE %>"/>Initialize the store: (Re)create the store.<br/>
      <input type="radio" name="<%= ADMIN_ACTION %>" value="<%= ADMIN_DESTROY %>"/>Remove the store completely.<br/>
  <input type="submit" value="Submit" />

  <input type="hidden" id="<%= CONFIG_STATUS %>" name="<%= CONFIG_STATUS %>" value="<%= ADMINISTER %>" />
      </form>

  </body>
</html>