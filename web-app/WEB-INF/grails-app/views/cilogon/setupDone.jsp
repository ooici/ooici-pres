<%@ page import="cilogon.PortalAbstractServlet" %>
<%--
  Date: Aug 7, 2010
  Time: 9:09:42 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Force a complete reload of the configuration
    PortalAbstractServlet.resetState();
%>
<html>
<head><title>Setup is complete</title></head>
<body>
<h2>Setup is complete!</h2>

<p>You have finished setting up your delegation service and it should be ready for use.<br><br>A restart of the servlet is strongly
suggested before proceeding.</p>

<form name="input" action="<%= setupDoneAction %>" method="get">
    Click to request a credential<br><br>
    <input type="submit" value="Submit"/>

</body>
</html>