

<%@ page import="ooici.pres.domain.Instrument" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'instrument.label', default: 'Instrument')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${instrumentInstance}">
            <div class="errors">
                <g:renderErrors bean="${instrumentInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${instrumentInstance?.id}" />
                <g:hiddenField name="version" value="${instrumentInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="instrumentType"><g:message code="instrument.instrumentType.label" default="Instrument Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: instrumentInstance, field: 'instrumentType', 'errors')}">
                                    <g:textField name="instrumentType" value="${instrumentInstance?.instrumentType}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="manufacturer"><g:message code="instrument.manufacturer.label" default="Manufacturer" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: instrumentInstance, field: 'manufacturer', 'errors')}">
                                    <g:textField name="manufacturer" value="${instrumentInstance?.manufacturer}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="modelNumber"><g:message code="instrument.modelNumber.label" default="Model Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: instrumentInstance, field: 'modelNumber', 'errors')}">
                                    <g:textField name="modelNumber" value="${instrumentInstance?.modelNumber}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="instrument.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: instrumentInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${instrumentInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="versionNumber"><g:message code="instrument.versionNumber.label" default="Version Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: instrumentInstance, field: 'versionNumber', 'errors')}">
                                    <g:textField name="versionNumber" value="${instrumentInstance?.versionNumber}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
