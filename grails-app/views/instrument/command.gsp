

<%@ page import="ooici.pres.domain.Instrument" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'instrument.label', default: 'Instrument')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1>Command Instrument ${params.name}</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${instrumentInstance}">
            <div class="errors">
                <g:renderErrors bean="${instrumentInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="command" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="instrumentId">Instrument ID</label>
                                </td>
                                <td valign="top" class="value">
                                    <g:textField size="40" name="instrumentId" value="${params.instrId}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="command"><g:message default="Command" />Command</label>
                                </td>
                                <td valign="top" class="value">
                                    <g:textField name="command" value="command" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label><g:message default="Argument 1" />Argument 1</label>
                                </td>
                                <td valign="top" class="value">
                                    <g:textField name="arg0" value="" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label><g:message default="Argument 2" />Argument 2</label>
                                </td>
                                <td valign="top" class="value">
                                    <g:textField name="arg1" value="" />
                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
