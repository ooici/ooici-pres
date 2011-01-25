==================================================
OOICI-PRES-GRAILS : Presentation Framework
==================================================

August 2010

This project represents the OOICI presentation framework. 


SETUP
========

1. Install Grails 1.3.3.
- Grails can be downloaded from: http://grails.org/Download

2. You must have a running RabbitMQ broker.
- The OOICI-PRES bootstrap process makes a connection to a local Rabbitmq broker.

3. Within your local lcaarch project, run:

twistd -n magnet -a sysname=spasco res/scripts/javalca.py

- This step starts up the needed lcaarch services used by this project.
- 'sysname' could be any name.

4. Grails requires that you set the ion.username. Modify the grails-app/conf/Config.groovy file's
ion.username value with your username. This username gets set within the LcademoService and CiService
classes.


Running
========

To start the app:
ooici-pres> grails run-app

In a browser:
Visit: http://localhost:8080/

- You'll be prompted to login. The un/pwd is admin/admin

CILOGON
========
All CILogon Servlets:
src > java > cilogon > ConfigConstants
src > java > cilogon > FailureServlet
src > java > cilogon > PortalAbstractServlet
src > java > cilogon > ReadyServlet
src > java > cilogon > SuccessServlet
src > java > cilogon > WelcomeServlet

All CILogon JSPs:
grails-app > views > index.jsp
grails-app > views > setup.jsp
grails-app > views > setupBasic.jsp
grails-app > views > setupDone.jsp
grails-app > views > setupErrorPage.jsp
grails-app > views > setupFileStore.jsp
grails-app > views > setupPostresStore.jsp
grails-app > views > setupStorageAdmin.jsp

CILogon config:
web-app > WEB-INF > WEB-INF > cfg.rdf
- The cfg.rdf file contains URL paths that should be changed according to the URL path scheme
of the deployed runtime environment. The URL paths are currently set for use w/in a local runtime
environment and need not change. If deploying to Tomcat (in a production environment), you
may need to adjust the URL path if the project name is part of the path.

- For example:

Production: http://localhost:8443/ooici-pres/WelcomeServlet
Development: http://localhost:8443/WelcomeServlet

- Note above, the addition of "ooici-pres" to the production URL.

- Running Grails with CILogon:
grails run-app --https

- CILogon Start URL:
http://localhost:8080/WelcomeServlet

HELP
========

Grails docs:
http://grails.org/doc/latest/

Groovy docs:
http://groovy.codehaus.org/Documentation

