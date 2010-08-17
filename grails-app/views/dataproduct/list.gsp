
<%@ page import="ooici.pres.domain.Dataproduct" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'dataproduct.label', default: 'Dataproduct')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1>Data Products</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>

                            <g:sortableColumn property="name" title="Name" />

                            <g:sortableColumn property="dataFormat" title="Data Format" />
                        
                            <g:sortableColumn property="instrumentId" title="Instrument Id" />

                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${dataProducts}" status="i" var="dps">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

	                        <td>${dps.name}</td>

	                        <td>${dps.dataFormat}</td>
                        
                            <td>${dps.instrumentId}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${dataproductInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
