
<%@ page import="ooici.pres.domain.Instrument" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'instrument.label', default: 'Instrument')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1>Instruments</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>

                            <g:sortableColumn property="commands" title="Commands" />

                            <g:sortableColumn property="registryId" title="${message(code: 'instrument.registryId.label', default: 'Registry Id')}" />

                            <g:sortableColumn property="name" title="${message(code: 'instrument.instrumentType.label', default: 'Name')}" />

                            <g:sortableColumn property="manufacturer" title="${message(code: 'instrument.manufacturer.label', default: 'Manufacturer')}" />
                        
                            <g:sortableColumn property="model" title="${message(code: 'instrument.modelNumber.label', default: 'Model Number')}" />
                        
                            <g:sortableColumn property="serialNum" title="${message(code: 'instrument.name.label', default: 'Serial Number')}" />
                        
                            <g:sortableColumn property="fwVersion" title="${message(code: 'instrument.versionNumber.label', default: 'Version Number')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${instruments}" status="i" var="instrumentInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>
	                            <g:link action="runcommand" params="[instrId:instrumentInstance.registryId, name:instrumentInstance.name]">[Command]</g:link>
                                <g:link action="seestatus" params="[instrId:instrumentInstance.registryId]">[Status]</g:link>
	                            <g:link action="startagent" params="[instrId:instrumentInstance.registryId, model:instrumentInstance.model]">[Start Agent]</g:link></td>

                            <td>${fieldValue(bean: instrumentInstance, field: "registryId")}</td>

                            <td>${fieldValue(bean: instrumentInstance, field: "name")}</td>

                            <td>${fieldValue(bean: instrumentInstance, field: "manufacturer")}</td>
                        
                            <td>${fieldValue(bean: instrumentInstance, field: "model")}</td>
                        
                            <td>${fieldValue(bean: instrumentInstance, field: "serialNum")}</td>
                        
                            <td>${fieldValue(bean: instrumentInstance, field: "fwVersion")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${instrumentInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
