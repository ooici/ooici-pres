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

- This last step starts up the needed lcaarch services used by this project.
- 'sysname' could be any name.


Running
========

To start the app:
ooici-pres> grails run-app

In a browser:
Visit: http://localhost:8080/ 

