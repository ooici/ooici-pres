==================================================
OOICI-PRES-GRAILS : Presentation Framework
==================================================

February 2011

This project represents the OOICI presentation framework. 


SETUP
=====

1. Install Grails 1.3.7.
- Grails can be downloaded from: http://grails.org/Download

2. You must have a running capability container listening to a topic on a RabbitMQ broker somewhere.
- Take note of the topic host name, sysname, exchange name and AMQ port number.  You'll need this info during deployment.

3. You must have fabric installed to deploy.
easy-install fabric


Deploying
=========

To deploy to your local machine, run the following command from the root of the repository:
ooici-pres> fab deployLocal


Running
========

To start the app:
ooici-pres> grails run-app

In a browser:
Visit: http://localhost:8080/


CILOGON
========
CILogon authentication currently takes advantage of the sample CILogon portal project servlets
and JSPs implemented by the CILogon team.  These objects have been integrated into the Grails project
as part of the move towards a more seamless logon process.

All CILogon Servlets:
src > java > cilogon > ConfigConstants
src > java > cilogon > FailureServlet
src > java > cilogon > PortalAbstractServlet
src > java > cilogon > ReadyServlet
src > java > cilogon > SuccessServlet

All CILogon JSPs:
web-app > index.jsp

web-app > WEB-INF > WEB-INF > cfg.rdf
- The cfg.rdf file contains URL paths that should be changed according to the URL path scheme
of the deployed runtime environment. The URL paths are currently set for use on machine 'spasco'.


CILogon Pre-requisites
======================
To run the Grails application on your server machine and offer CILogon authentication, the following
pre-requisites must be satisfied:

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

Running CILogon
===============
1. Deploy the Grails war file to Tomcat on the server machine.
   For example, copy ooici-pres-0.1.war to $CATALINA_HOME/webapps
2. Alter the $CATALINA_HOME/webapps/ooici-pres-0.1/WEB-INF/cfg.rdf file to reference
   your server machine's name (ie. search/replace occurrences of 'spasco')
3. Start Tomcat on the server machine.
   $CATALINA_HOME/bin/startup.sh
4. Start the identity service and the services it depends on:

twistd --pidfile=ps1 -n cc -a sysname=spasco ion/services/coi/datastore/DataStoreService
twistd --pidfile=ps2 -n cc -a sysname=spasco ion/services/coi/resource_registry_beta/resource_registry/ResourceRegistryService
twistd --pidfile=ps3 -n cc -a sysname=spasco ion/services/coi/identity_registry/IdentityRegistryService

5. Point a web browser at
   https://<yourservermachinename>:8443/ooici-pres-o.1/WelcomeServlet
   and follow the web page instructions to run through the CILogon certificate authentication
   process. You should end up with https://cilogon.org/delegate/ page reporting success.  Follow
   the "Return to Sample Java delegation portal" link at the bottom of the success page.  This
   will run the SuccessServlet on the server machine.  This servlet will access the ion identity
   service to register the user credential and public key.  The resulting OOID returned from the
   ion identity service as well as the X.509 certificate and public key content with be displayed
   in the browser.


DEVELOPMENT MADE EASY WITHOUT CILOGON:
======================================
CILOGON requires much setup. If you're working on the UX templates and would rather bypass
CILOGON setup altogether, there's an easy way to deactivate it so you can streamline development w/out CILOGON.

Here's how:
1. Change ooici-pres/src/java/templates/web.xml

To:

ooici-pres/src/java/templates/web.xml.orig

Don't commit this change. ;)

2. Now, you can start Grails (grails run-app) and work away seeing changes made to grails files reflected immediately w/in the browser.

Start Grails as usual: grails run-app

View it w/in the browser without the CILOGON required dependencies.

For example:
http://localhost:8080/test.html


HELP
========

Grails docs:
http://grails.org/doc/latest/

Groovy docs:
http://groovy.codehaus.org/Documentation

