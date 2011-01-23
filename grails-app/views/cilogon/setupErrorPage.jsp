<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.StringWriter" %>
<%--
  Really basic error page.
  Date: Aug 5, 2010
  Time: 2:17:49 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<html>
<body>
<h1>
    Error Page
</h1>
<hr>
<h2>
    Received the exception:<br>
    <font color=red>
        <%= exception.toString()   %>
    </font>
</h2>

<%
    out.println("<!--");
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    exception.printStackTrace(pw);
    out.println(sw);

    sw.close();
    pw.close();
    out.println("-->");
%>
<%= sw.toString() %>
</body>
</html>
