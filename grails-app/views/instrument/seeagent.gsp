
<%@ page import="ooici.pres.domain.Instrument" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'instrument.label', default: 'Instrument')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1>Instrument Agent Status</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
	        <div class="dialog">
		        <h3>Headers</h3>
                ${headers}
            </div>
            <div class="dialog">
	            <h3>Status</h3>
                ${status}
            </div>
        </div>
    </body>
</html>
