from twisted.web.server import Site
from twisted.web.static import File
from twisted.web.resource import Resource
from twisted.internet import reactor

import json

ROOTPAGE = "v5.html"


class ServiceList(Resource):

    DATA = [{"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS"},{"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS"}, {"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS"},{"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS"},{"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS"},{"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS"},]
  
    def render_GET(self, request):
        return json.dumps(self.DATA)

class Notifications(Resource):

    DATA = [{"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS", "created":"10-1-2010 23:00Z"},{"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS", "created":"10-1-2010 23:00Z"},{"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS", "created":"10-1-2010 23:00Z"},]
  
    def render_GET(self, request):
        return json.dumps(self.DATA)


class Service(Resource):

    def getChild(self, name, request):
        if name == "list":
            return ServiceList()
        if name == "my_registered_resources":
            return ServiceList()
        if name == "notifications":
            return Notifications()



root = Resource()
root.putChild("", File(ROOTPAGE))
root.putChild("service", Service())
root.putChild("css", File("./css"))
root.putChild("js", File("./js"))
root.putChild("images", File("./images"))
factory = Site(root)
reactor.listenTCP(8080, factory)
reactor.run()
