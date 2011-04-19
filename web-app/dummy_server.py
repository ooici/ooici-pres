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

    def render_POST(self, request):
        import time; time.sleep(0.8) #mock out real latency
        return "ok"

class Notifications(Resource):

    DATA = [{"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS", "created":"10-1-2010 23:00Z"},{"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS", "created":"10-1-2010 23:00Z"},{"user_ooi_id": "3f27a744-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS", "created":"10-1-2010 23:00Z"},]
  
    def render_GET(self, request):
        return json.dumps(self.DATA)

    def render_POST(self, request):
        return "ok"


class DownloadData(Resource):

    def render_GET(self, request):
        request.setHeader("Content-Disposition","attachment; filename=test_data.txt");
        return (" - Test Data - " * 1000)


class DataDetails(Resource):

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

    DATA = {"data":DATA_RAW, "dataResourceSummary": [{"user_ooi_id": "45FF9F6C-9D0A-4519-A7CE-AE9797D4AF54", "data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E047F1","title": "NDBC Sensor Observation Service data from http://sdf.ndbc.noaa.gov/sos/","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS","references": "http://sdf.ndbc.noaa.gov/sos/","ion_time_coverage_start": "2008-08-01T00:50:00Z","ion_time_coverage_end": "2008-08-01T23:50:00Z","ion_geospatial_lat_min": -45.431,"ion_geospatial_lat_max": -45.431,"ion_geospatial_lon_min": 25.909,"ion_geospatial_lon_max": 25.909,"ion_geospatial_vertical_min": 0.2,"ion_geospatial_vertical_max": 0.0,"ion_geospatial_vertical_positive": "down"}]}

    def render_GET(self, request):
        import time; time.sleep(0.8) #mock out real latency
        return json.dumps(self.DATA)


class Service(Resource):

    def getChild(self, name, request):
        if name == "dataResource":
            return ServiceList()
        if name == "subscriptions":
            return Notifications()
        if name == "createDownloadUrl":
            return DownloadData()
        if name == "dataResourceDetail":
            return DataDetails()



root = Resource()
root.putChild("", File(ROOTPAGE))
root.putChild("service", Service())
root.putChild("css", File("./css"))
root.putChild("js", File("./js"))
root.putChild("images", File("./images"))
factory = Site(root)
reactor.listenTCP(8080, factory)
reactor.run()
