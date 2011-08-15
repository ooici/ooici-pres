==================================================
OOICI-PRES-GRAILS : Presentation Framework
==================================================

February 2011

This project represents the OOICI presentation framework. 


SETUP
=====

1. Install Grails 1.3.7.
- Grails can be downloaded from: http://grails.org/Download
> sudo mv ~/Downloads/grails-1.3.7 /usr/share
> cd /usr/share
> sudo chown -R root:wheel grails-1.3.7/
> sudo chmod 0755 grails-1.3.7/bin/*
> ln -s grails-1.3.7 grails

Add the following to your .profile:
export GRAILS_HOME=/usr/share/grails
export PATH=$GRAILS_HOME/bin:$PATH

2. Install fabric (used by deploy script).
> easy_install fabric


Starting the Capability Container
=================================

You must have a running capability container for the UI to connect to.  The deployment
script makes the following assumptions on defaults, but you can override any of these
during deployment.

topic host: localhost
topic port: 5672
sysname: username
username: guest
password: guest
exchange name: magnet.topic

Make the Grails directory
=========================
mkdir ~/.grails

Deploy and run it
=================
ioncore-python> bin/twistd cc -n -a sysname=<sysname> -h <rabbitmq hostname> res/deploy/r1deploy.rel


Deploying
=========

To deploy and run the web application on your local machine, run the following command
from the root of the repository:
ooici-pres> fab deployLocal

To deploy and run the web application on your local machine within a non-embedded Tomcat instance,
run the following command from the root of the repository:
ooici-pres> fab deployLocalTomcat

To deploy and run the web application on a remote machine within a non-embedded Tomcat instance,
run the following command from the root of the repository:
ooici-pres> fab deployRemoteTomcat

To deploy and run the web application on the official ion.oceanobservatories.org machine,
run the following command from the root of the repository:
ooici-pres> fab deployIon

To deploy and run the web application on the official ion-test.oceanobservatories.org machine,
run the following command from the root of the repository:
ooici-pres> fab deployIonTest

To deploy and run the web application on amoeba against the unofficial CC installation,
run the following command from the root of the repository:
ooici-pres> fab deployAmoeba


Running
========

To start the web application without re-deploying:
ooici-pres> grails run-app

In a browser:
Visit: http://localhost:8080/


Configuration
=============

There are two critical configuration files involved in running the web appliction.
The first is the ioncore-config.properties file.  This file can be found at
~/.grails in a local deployment.  In a remote deployment, the file is located at
$CATALINA_HOME/webapps/<this app's name>/WEB-INF/classes. This configuration file
retains the capability container information as well as authentication override
control.  See below for more information on authentication.

The second configuration file of interest is the CILogon config file.  This file
is used to redirect CILogon back to the appropriate servlet url:port.  This file is
located at $CATALINA_HOME/webapps/<this app's name>/WEB-INF/cfg.rdf.

Both these files are automatically configured as part of the deployment process. However,
you can subsequently make manual changes.  If you alter any information in these
configuration files, you must restart the web application for the changes to be applied.

A Note About Authentication
===========================

The web application utilizes CILogon for authentication.  However, configuring your machine
to enable CILogon is a minor pain. As a result, the default authentication mode when running
locally is to inhibit CILogon and fake the granting of a CILogon certificate.


CILogon Server Machine Setup
============================

To run the Grails application on your server machine and offer CILogon authentication to clients,
the following pre-requisites must be satisfied:

- Your server machine must have a static IP address.
- You must contact the CILogon organization to acquire a CILogon server certificate.
- You must stash this server certificate on your server machine in your keystore.
- You must install Tomcat and configure SSL on the server machine.
  Assuming that tomcat's top-level directory is $CATALINA_HOME, you need to edit

  $CATALINA_HOME\conf\server.xml

  and look for the ssl connector (normally running on port 8443, so search for that).
  You might need to uncomment it, but the connector should read something like this:


  <Connector port="8443" protocol="HTTP/1.1" SSLEnabled="true"
              maxThreads="150" scheme="https" secure="true"
              keystoreFile="${user.home}/certs/keystore.p12"
              keystorePass="PASSWORD1"
              keystoreType="PKCS12"
              clientAuth="false" sslProtocol="TLS" />

  Alter the keystoreFile and keystorePass values as appropriate for your server machine.


HELP
========

Grails docs:
http://grails.org/doc/latest/

Groovy docs:
http://groovy.codehaus.org/Documentation

