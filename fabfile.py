#!/usr/bin/env python

from __future__ import with_statement
from fabric.api import *
from fabric.contrib.console import confirm
from fabric.contrib.files import exists
import os
import re
import sys
import time

# global variables
gitUrl = 'git://github.com/ooici/ooici-pres.git'
project = 'ooici-pres'
webAppHost = None
webAppName = None
webAppPort = None
context = None

# Monkey-patch "open" to honor fabric's current directory
_old_open = open
def open(path, *args, **kwargs):
    if os.path.isabs(path):
        return _old_open(path, *args, **kwargs)
    return _old_open(os.path.join(env.lcwd, path), *args, **kwargs)

# Decorator class
class cloneDir(object):
    def __init__(self, f):
        self.f = f

    def __call__(self, *args, **kwargs):
        global webAppName
        local('rm -rf ../tmpfab')
        local('mkdir ../tmpfab')
        local('git clone %s ../tmpfab/%s' % (gitUrl, project))

        with lcd(os.path.join('..', 'tmpfab', project)):
            print 'Below is a list of release tags available for deploying:'
            local('git tag | sort -r')
            defaultTagVersion = local('git tag|sort -n|tail -1', capture=True)
            tagVersion = prompt('Please enter release tag you want to deploy based on list above:',
                default=defaultTagVersion)
            webAppName = 'ooici-pres-%s' % tagVersion[1:]
            self.f(*args, **kwargs)
        local('rm -rf ../tmpfab')

def ciLogonConfig(localDeployment):
    global webAppHost
    if webAppHost is None:
        if localDeployment:
            localhostName = local('uname -n', capture=True)
            webAppHost = prompt('Please enter fully qualified web application host name:', default=localhostName)
        else:
            webAppHost = prompt('Please enter fully qualified web application host name:')

    global webAppPort
    if webAppPort is None:
        webAppPort = prompt('Please enter web application SSL port number:')

    # Perform value substitution in CILogon config file
    o = open('web-app/WEB-INF/cfg.rdf', 'w')
    cilogonCfg = open('config/cfg.rdf.template').read()

    global context
    if context is None:
        readyURL = 'https://' + webAppHost + ':' + webAppPort + '/' + webAppName + '/ready'
        failureURL = 'https://' + webAppHost + ':' + webAppPort + '/' + webAppName + '/failure'
        successURL = 'https://' + webAppHost + ':' + webAppPort + '/' + webAppName + '/SuccessServlet'
    else:
        readyURL = 'https://' + webAppHost + ':' + webAppPort + '/ready'
        failureURL = 'https://' + webAppHost + ':' + webAppPort + '/failure'
        successURL = 'https://' + webAppHost + ':' + webAppPort + '/SuccessServlet'

    cilogonCfg = re.sub('READY', readyURL, cilogonCfg)
    cilogonCfg = re.sub('FAILURE', failureURL, cilogonCfg)
    cilogonCfg = re.sub('SUCCESS', successURL, cilogonCfg)

    o.write( re.sub('PORT', webAppPort, cilogonCfg) )
    o.close()

topicHost = None
topicSysname = None
topicPort = None
topicUsername = None
topicPassword = None
topicExchange = None
instrumentMonitorURL = None
debugMode = None
def appConfig(localDeployment):
    global topicHost
    if topicHost is None:
        if localDeployment:
            topicHost = prompt('Please enter fully qualified topic host name:', default='localhost')
        else:
            topicHost = prompt('Please enter fully qualified topic host name:', default='amoeba.ucsd.edu')

    global topicSysname
    if topicSysname is None:
        if localDeployment:
            topicSysname = prompt('Please enter topic sysname:', default=os.getlogin())
        else:
            topicSysname = prompt('Please enter topic sysname:', default='R1_UI_DEMO')

    global topicPort
    if topicPort is None:
        topicPort = prompt('Please enter topic AMQ port number:', default='5672')

    global topicUsername
    if topicUsername is None:
        topicUsername = prompt('Please enter username for connecting to RabbitMQ:', default='guest')

    global topicPassword
    if topicPassword is None:
        topicPassword = prompt('Please enter password for connecting to RabbitMQ:', default='guest')

    global topicExchange
    if topicExchange is None:
        topicExchange = prompt('Please enter topic exchange:', default='magnet.topic')

    global instrumentMonitorURL
    if instrumentMonitorURL is None:
        instrumentMonitorURL = prompt('Please enter instrument monitor URL:', default='http://localhost:9998')

    global debugMode
    if debugMode is None:
        if localDeployment:
            debugMode = prompt('Please enter CILogon bypass authentication mode (force, url, disabled):', default='force')
        else:
            debugMode = prompt('Please enter CILogon bypass authentication mode (force, url, disabled):', default='disabled')

    # Perform value substitution in app config file(s)
    o = open('src/java/ioncore-config.properties', 'w')
    appCfg = open('config/ioncore-config.properties.template').read()
    appCfg = re.sub('HOSTNAME', topicHost, appCfg)
    appCfg = re.sub('SYSNAME', topicSysname, appCfg)
    appCfg = re.sub('PORT', topicPort, appCfg)
    appCfg = re.sub('USERNAME', topicUsername, appCfg)
    appCfg = re.sub('PASSWORD', topicPassword, appCfg)
    appCfg = re.sub('INSTRUMENT_MONITOR_URL', instrumentMonitorURL, appCfg)
    appCfg = re.sub('BYPASSAUTHENTICATIONMODE', debugMode, appCfg)
    o.write( re.sub('EXCHANGE', topicExchange, appCfg) )
    o.close()

    if localDeployment:
        homeDir = os.environ['HOME']
        o = open('%s/.grails/ioncore-config.properties'% (homeDir), 'w')
        appCfg = open('config/ioncore-config.properties.template').read()
        appCfg = re.sub('HOSTNAME', topicHost, appCfg)
        appCfg = re.sub('SYSNAME', topicSysname, appCfg)
        appCfg = re.sub('PORT', topicPort, appCfg)
        appCfg = re.sub('USERNAME', topicUsername, appCfg)
        appCfg = re.sub('PASSWORD', topicPassword, appCfg)
        appCfg = re.sub('INSTRUMENT_MONITOR_URL', instrumentMonitorURL, appCfg)
        appCfg = re.sub('BYPASSAUTHENTICATIONMODE', debugMode, appCfg)
        o.write( re.sub('EXCHANGE', topicExchange, appCfg) )
        o.close()

def buildWebApp(localDeployment, useTomcat):
    global webAppName

    if useTomcat:
        ciLogonConfig(localDeployment)
    appConfig(localDeployment)

    # Build and package app
    local('grails war')

sshUser = None
tomcatDir = None
def startWebApp(localDeployment, useTomcat, restartTomcat):
    if localDeployment:
        if useTomcat:
            global tomcatDir
            if tomcatDir is None:
                tomcatDir = prompt('Please enter fully qualified Tomcat install directory:', default='/opt/tomcat')
            if restartTomcat:
                local('%s/bin/shutdown.sh' % (tomcatDir))
                print 'Waiting for application to fully stop'
                time.sleep(10);
            local('rm -rf %s/webapps/%s*' % (tomcatDir, webAppName))
            local('cp target/%s.war %s/webapps' % (webAppName, tomcatDir))
            if restartTomcat:
               local('%s/bin/startup.sh' % (tomcatDir))
        else:
            local('grails run-app')
    else:
        global webAppHost
        if webAppHost is None:
            webAppHost = prompt('Please enter fully qualified web application host name:', default='ion-test.oceanobservatories.org')
        if useTomcat:
            if tomcatDir is None:
                tomcatDir = prompt('Please enter fully qualified Tomcat install directory:', default='/opt/tomcat')
        global sshUser
        if sshUser is None:
            sshUser = os.getlogin()
            sshUser = prompt('Please enter your ssh login name:', default=sshUser)

        local('ssh %s@%s -t %s/bin/shutdown.sh' % (sshUser, webAppHost, tomcatDir))
        print 'Waiting for application to fully stop'
        time.sleep(10);
        local('ssh %s@%s -t rm -rf %s/webapps/%s*' % (sshUser, webAppHost, tomcatDir, webAppName))
        local('scp target/%s.war %s@%s:%s/webapps' % (webAppName, sshUser, webAppHost, tomcatDir))
        local('ssh %s@%s -t %s/bin/startup.sh' % (sshUser, webAppHost, tomcatDir))

def startWebAppOfficial(localDeployment):
    if localDeployment:
        local('grails run-app')
    else:
        global sshUser
        if sshUser is None:
            sshUser = os.getlogin()
            sshUser = prompt('Please enter your ssh login name:', default=sshUser)

        local('ssh %s@%s -t sudo /etc/init.d/grails stop' % (sshUser, webAppHost))
        print 'Waiting for application to fully stop'
        time.sleep(10);
        local('ssh %s@%s -t sudo rm -rf /opt/tomcat/webapps/%s*' % (sshUser, webAppHost, context))
        local('scp target/%s.war %s@%s:/opt/tomcat/webapps/%s.war' % (webAppName, sshUser, webAppHost, context))
        local('ssh %s@%s -t chmod 666 /opt/tomcat/webapps/%s.war' % (sshUser, webAppHost, context))
        local('ssh %s@%s -t sudo /etc/init.d/grails start' % (sshUser, webAppHost))

@cloneDir
def buildAndStartWebApp(isWebAppOfficial, localDeployment, useTomcat,
        restartTomcat):
    buildWebApp(localDeployment, useTomcat)
    if isWebAppOfficial:
        startWebAppOfficial(localDeployment)
    else:
        startWebApp(localDeployment, useTomcat, restartTomcat)

def deployIonBeta():
    global webAppHost
    global webAppName
    global context
    global webAppPort
    global topicHost
    global topicSysname
    global topicPort
    global topicUsername
    global topicPassword
    global topicExchange
    global instrumentMonitorURL
    global debugMode
    webAppHost = 'ion-beta.oceanobservatories.org'
    context = 'ROOT'
    webAppPort = '443'
    topicHost = 'rabbitmq-prod.oceanobservatories.org'
    topicSysname = 'R1_PROD_SYSTEM'
    topicPort = '5672'
    topicUsername = 'rabbit_user'
    topicPassword = 'rabbit_pass'
    topicExchange = 'magnet.topic'
    instrumentMonitorURL = 'http://pubdebug01.oceanobservatories.org:9998'
    debugMode = 'disabled'
    buildAndStartWebApp(isWebAppOfficial=True, localDeployment=False,
            useTomcat=True, restartTomcat=True)
    # buildWebApp(False,True)
    # startWebAppOfficial(False)

def deployIonAlpha():
    global webAppHost
    global webAppName
    global context
    global webAppPort
    global topicHost
    global topicSysname
    global topicPort
    global topicUsername
    global topicPassword
    global topicExchange
    global instrumentMonitorURL
    global debugMode
    webAppHost = 'ion-alpha.oceanobservatories.org'
    context = 'ROOT'
    webAppPort = '443'
    topicHost = 'rabbitmq.oceanobservatories.org'
    topicSysname = 'R1_TEST_SYSTEM2'
    topicPort = '5672'
    topicUsername = 'rabbit_user'
    topicPassword = 'rabbit_pass'
    topicExchange = 'magnet.topic'
    instrumentMonitorURL = 'http://pubdebug01.oceanobservatories.org:9998'
    debugMode = 'disabled'
    buildAndStartWebApp(isWebAppOfficial=True, localDeployment=False,
            useTomcat=True, restartTomcat=True)
    # buildWebApp(False,True)
    # startWebAppOfficial(False)

def deployIonTest():
    global webAppHost
    global webAppName
    global context
    global webAppPort
    global topicHost
    global topicSysname
    global topicPort
    global topicUsername
    global topicPassword
    global topicExchange
    global instrumentMonitorURL
    global debugMode
    webAppHost = 'ion-test.oceanobservatories.org'
    context = 'ROOT'
    webAppPort = '443'
    topicHost = 'rabbitmq-test.oceanobservatories.org'
    topicSysname = 'R1_TEST_SYSTEM1'
    topicPort = '5672'
    topicUsername = 'rabbit_user'
    topicPassword = 'rabbit_pass'
    topicExchange = 'magnet.topic'
    instrumentMonitorURL = 'http://pubdebug01.oceanobservatories.org:9998'
    debugMode = 'disabled'
    buildAndStartWebApp(isWebAppOfficial=True, localDeployment=False,
            useTomcat=True, restartTomcat=True)
    # buildWebApp(False,True)
    # startWebAppOfficial(False)

def deployIonStage():
    global webAppHost
    global webAppName
    global context
    global webAppPort
    global topicHost
    global topicSysname
    global topicPort
    global topicUsername
    global topicPassword
    global topicExchange
    global instrumentMonitorURL
    global debugMode
    webAppHost = 'ion-stage.oceanobservatories.org'
    context = 'ROOT'
    webAppPort = '443'
    topicHost = 'rabbitmq-stage.oceanobservatories.org'
    topicSysname = 'R1_STAGE_SYSTEM'
    topicPort = '5672'
    topicUsername = 'rabbit_user'
    topicPassword = 'rabbit_pass'
    topicExchange = 'magnet.topic'
    instrumentMonitorURL = 'http://pubdebug01.oceanobservatories.org:9998'
    debugMode = 'disabled'
    buildAndStartWebApp(isWebAppOfficial=True, localDeployment=False,
            useTomcat=True, restartTomcat=True)
    # buildWebApp(False,True)
    # startWebAppOfficial(False)

def deployAmoeba():
    global webAppHost
    global webAppName
    global webAppPort
    global topicHost
    global topicSysname
    global topicPort
    global topicUsername
    global topicPassword
    global topicExchange
    global instrumentMonitorURL
    global debugMode
    global tomcatDir
    webAppHost = 'amoeba.ucsd.edu'
    webAppPort = '9443'
    topicHost = 'amoeba.ucsd.edu'
    topicSysname = 'R1_UI_DEMO'
    topicPort = '5672'
    topicUsername = 'guest'
    topicPassword = 'guest'
    topicExchange = 'magnet.topic'
    instrumentMonitorURL = 'http://amoeba.ucsd.edu:9998'
    debugMode = 'disabled'
    tomcatDir = '/opt/apache-tomcat-6.0.32'
    buildAndStartWebApp(isWebAppOfficial=False, localDeployment=False,
            useTomcat=True, restartTomcat=True)
    # buildWebApp(False,True)
    # startWebApp(False,True,True)

def deployTest():
    global webAppHost
    global webAppName
    global webAppPort
    global topicHost
    global topicSysname
    global topicPort
    global topicUsername
    global topicPassword
    global topicExchange
    global instrumentMonitorURL
    global debugMode
    global tomcatDir
    webAppHost = 'buildbot.oceanobservatories.org'
    webAppPort = '9443'
    webAppName = 'ooici-pres-0.1'
    topicHost = 'amoeba.ucsd.edu'
    topicSysname = 'buildbot'
    topicPort = '5672'
    topicUsername = 'guest'
    topicPassword = 'guest'
    topicExchange = 'magnet.topic'
    instrumentMonitorURL = 'http://buildbot.oceanobservatories.org:9998'
    debugMode = 'disabled'
    tomcatDir = '/var/lib/jenkins/apache-tomcat-6.0.32'

    local('sed -e "s/app.version=.*/app.version=0.1/" application.properties > tmp.properties')
    local("mv tmp.properties application.properties")
    buildWebApp(True,True)
    startWebApp(True,True,False)

def deployRemoteTomcat():
    buildAndStartWebApp(isWebAppOfficial=False, localDeployment=False,
            useTomcat=True, restartTomcat=True)
    # buildWebApp(False,True)
    # startWebApp(False,True,True)

def deployLocalTomcat():
    buildAndStartWebApp(isWebAppOfficial=False, localDeployment=True,
            useTomcat=True, restartTomcat=True)
    # buildWebApp(True,True)
    # startWebApp(True,True,True)

def deployLocal():
    buildAndStartWebApp(isWebAppOfficial=False, localDeployment=True,
            useTomcat=False, restartTomcat=False)
    # buildWebApp(True,False)
    # startWebApp(True,False,True)
