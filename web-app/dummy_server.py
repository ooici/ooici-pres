from twisted.web.server import Site
from twisted.web.static import File
from twisted.web.resource import Resource
from twisted.internet import reactor

import json

ROOTPAGE = "v5.html"


class Notifications(Resource):

    DATA = {"dataResourceSummary":[{"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS", "created":"10-1-2010 23:00Z"},{"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS", "created":"10-1-2010 23:00Z"},{"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS", "created":"10-1-2010 23:00Z"}]}
  
    def render_GET(self, request):
        return json.dumps(self.DATA)

    def render_POST(self, request):
        return "ok"


class DownloadData(Resource):

    def render_GET(self, request):
        request.setHeader("Content-Disposition","attachment; filename=test_data.txt");
        return (" - Test Data - " * 1000)


class DataResource(Resource):

    DATA_TEST =  """ 
      water_press {
        Float32 _FillValue -99999.0;
        Float64 _Fillvalue -99999.0;
        Float32 actual_range -4.484, 2.571;
        Float64 data_max 0.595;
        Float64 data_min -0.059;
        String grid_mapping "crs";
        String ioos_category "Pressure";
        String long_name "Water Pressure";
        Float32 missing_value -99999.0;
        String standard_name "water_pressure";
        String units "dbar";
      }
    """

    DATA_RAW = "<pre>"+(DATA_TEST*20)+"</pre>"

    DATA = {"data":DATA_RAW, "dataResourceSummary":[{"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS"},{"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS"}, {"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS"},{"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS"},{"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS"},{"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS"}]}



    def render_GET(self, request):
        #import time; time.sleep(0.8) #mock out real latency
        return json.dumps(self.DATA)

    def render_POST(self, request):
        return "ok"


root = Resource()
root.putChild("", File(ROOTPAGE))
root.putChild("css", File("./css"))
root.putChild("js", File("./js"))
root.putChild("images", File("./images"))
root.putChild("dataResource", DataResource())
root.putChild("subscription", Notifications())
root.putChild("createDownloadUrl", DownloadData())
factory = Site(root)
reactor.listenTCP(8080, factory)
reactor.run()
