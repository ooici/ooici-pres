from twisted.web.server import Site
from twisted.web.static import File
from twisted.web.resource import Resource
from twisted.internet import reactor

from twisted.application import service, internet
from twisted.web import static, server
from twisted.web.resource import Resource


try:
    import simplejson as json
except ImportError:
    import json


ROOTPAGE = "dashboard.gsp"


class Notifications(Resource):

    DATA = {"dataResourceSummary":[{"user_ooi_id": "3f27a544-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd241aa5-2faa-4d39-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS", "created":"10-1-2010 23:00Z"},{"user_ooi_id": "3f23a444-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "fd204aa3-2faa-1d41-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS", "created":"10-1-2010 23:00Z"},{"user_ooi_id": "3f27a742-2c3e-4d2a-a98c-050b246334a3","data_resource_id": "1d104aa3-2faa-4d49-84ee-457094666b23","title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS", "created":"10-1-2010 23:00Z"}]}


  
    def render_GET(self, request):
        return json.dumps(self.DATA)

    def render_POST(self, request):
        return "ok"


class UserProfile(Resource):

    DATA = {"name": "MyOOICI","institution":"OOICI","email_address":"myooici@gmail.com","profile":[{"name":"twitter", "value":"twitter.com/ooi"}, {"name":"mobilephone", "value":"555-555-5555"}]}

    def render_GET(self, request):
        import time; time.sleep(0.8) #mock out real latency
        return json.dumps(self.DATA)


    def render_POST(self, request):
        import time; time.sleep(0.8) #mock out real latency
        return "ok"



class DownloadData(Resource):

    def render_GET(self, request):
        request.setHeader("Content-Disposition","attachment; filename=test_data.txt");
        return (" - Test Data - " * 1000)


class UserProfile(Resource):

    DATA = {"name": "MyOOICI","institution":"OOICI","email_address":"myooici@gmail.com","profile":[{"name":"twitter", "value":"twitter.com/ooi"}, {"name":"mobilephone", "value":"555-555-5555"}]}

    def render_GET(self, request):
        import time; time.sleep(0.8) #mock out real latency
        return json.dumps(self.DATA)


    def render_POST(self, request):
        import time; time.sleep(0.8) #mock out real latency
        return "ok"




class DataResource(Resource):


    dataResourceSummary = [{"datasetMetadata":{"title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","data_resource_id": "1119A67F-81F3-424F-8E69-4F28C4E047F1", "institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)", "summary": "Real-time surface data from ASIMet system 1 on the tenth Northwest Tropcial Stlantic Station (NTAS) observatory.", "source": "NDBC SOS","references": "http://sdf.ndbc.noaa.gov/sos/","ion_time_coverage_start": "2008-08-01T00:50:00Z","ion_time_coverage_end": "2008-08-01T23:50:00Z","ion_geospatial_lat_min": -45.431,"ion_geospatial_lat_max": -45.431,"ion_geospatial_lon_min": 25.909,"ion_geospatial_lon_max": 25.909,"ion_geospatial_vertical_min": 0.2,"ion_geospatial_vertical_max": 0.0,"ion_geospatial_vertical_positive": "down"}, "date_registered": 1304696743406,"notificationSet": False}, {"datasetMetadata":{"title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","data_resource_id": "3119A67F-81F1-424F-8E19-4F28C4E047F1", "institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS","references": "http://sdf.ndbc.noaa.gov/sos/","ion_time_coverage_start": "2008-08-01T00:50:00Z","ion_time_coverage_end": "2008-08-01T23:50:00Z","ion_geospatial_lat_min": -45.431,"ion_geospatial_lat_max": -45.431,"ion_geospatial_lon_min": 25.909,"ion_geospatial_lon_max": 25.909,"ion_geospatial_vertical_min": 0.2,"ion_geospatial_vertical_max": 0.0,"ion_geospatial_vertical_positive": "down"}, "date_registered": 1304696743406,"notificationSet": False}]

    dataResourceSummary_one = {"title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","data_resource_id": "1119A67F-81F3-424F-8E69-4F28C4E047F1", "institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)", "summary": "Real-time surface data from ASIMet system 1 on the tenth Northwest Tropcial Stlantic Station (NTAS) observatory.", "source": "NDBC SOS","references": "http://sdf.ndbc.noaa.gov/sos/","ion_time_coverage_start": "2008-08-01T00:50:00Z","ion_time_coverage_end": "2008-08-01T23:50:00Z","ion_geospatial_lat_min": -45.431,"ion_geospatial_lat_max": -45.431,"ion_geospatial_lon_min": 25.909,"ion_geospatial_lon_max": 25.909,"ion_geospatial_vertical_min": 0.2,"ion_geospatial_vertical_max": 0.0,"ion_geospatial_vertical_positive": "down"}

    DATA = {
        "data_resource_id": "3219A67F-81F3-421F-8E69-4F28C4E047F1",
        "source":{"request_type": "DAP","base_url": "http://geoport.whoi.edu/thredds/dodsC/usgs/data0/rsignell/data/oceansites/OS_NTAS_2010_R_M-1.nc","max_ingest_millis": 6000,"ion_title": "NTAS1 Data Source","ion_description": "Data NTAS1","ion_name": "MyOOICI","ion_email": "myooici@gmail.com","ion_institution": "OOICI"},
        "variable": [{"units": "degree_north","standard_name": "latitude","long_name": "northward positive degrees latitude"},{"units": "degree_east","standard_name": "longitude","long_name": "eastward positive degrees longitude"},{"standard_name": "station_id","long_name": "integer station identifier"},{"units": "psu","standard_name": "sea_water_salinity","long_name": "water salinity at location","other_attributes": ["coordinates=time lon lat z"]},{"units": "seconds since 1970-01-01 00:00::00","standard_name": "time","long_name": "time","other_attributes": ["_CoordinateAxisType=Time"]},{"units": "m","standard_name": "depth","long_name": "depth below mean sea level","other_attributes": ["positive=down","::_CoordinateAxisType=Height","::_CoordinateZisPositive=down"]}]
    }

    def render_GET(self, request):
        #import time; time.sleep(0.8) #mock out real latency
        action = request.args["action"][0]
        if action == "detail":
            self.DATA.update({"dataResourceSummary":self.dataResourceSummary_one})
        else:
            self.DATA.update({"dataResourceSummary":self.dataResourceSummary})
        return json.dumps(self.DATA)

    def render_POST(self, request):
        return "ok"


root = Resource()
root.putChild("", File(ROOTPAGE))
root.putChild("css", File("./css"))
root.putChild("js", File("./js"))
root.putChild("images", File("./images"))
root.putChild("static", File("./static"))
root.putChild("dataResource", DataResource())
root.putChild("subscription", Notifications())
root.putChild("createDownloadUrl", DownloadData())
root.putChild("userProfile", UserProfile())

application = service.Application("ux_dummmy_server")
service = internet.TCPServer(8080, Site(root))
service.setServiceParent(application)
