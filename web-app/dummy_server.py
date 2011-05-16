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


ROOTPAGE = "../grails-app/views/dashboard/show.gsp"


class Notifications(Resource):

    DATA = {"subscriptionListResults": [{"subscriptionInfo": {"user_ooi_id": "A7B44115-34BC-4553-B51E-1D87617F12E0","data_src_id": "3319A67F-81F3-424F-8E69-4F28C4E04801","subscription_type": "EMAILANDDISPATCHER","email_alerts_filter": "UPDATES","dispatcher_alerts_filter": "UPDATES","dispatcher_script_path": "path","date_registered": 1304724473336},"datasetMetadata": {"user_ooi_id": "A7B44115-34BC-4553-B51E-1D87617F12E0","data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04801","title": "path","institution": "HYCOM","source": "HYCOM archive file","references": "","conventions": "","summary": "","comment": "","ion_time_coverage_start": "2011-04-11T00:00:00Z","ion_time_coverage_end": "2011-04-11T00:00:00Z","ion_geospatial_lat_min": 32.0284996033,"ion_geospatial_lat_max": 44.0166015625,"ion_geospatial_lon_min": -81.0400390625,"ion_geospatial_lon_max": -65.0400390625,"ion_geospatial_vertical_min": 1.0,"ion_geospatial_vertical_max": 32.0,"ion_geospatial_vertical_positive": "down","download_url": "http://localhost:8081/thredds/dodsC/scanData/3319A67F-81F3-424F-8E69-4F28C4E04801.ncml"}}]}


    def render_GET(self, request):
        return json.dumps(self.DATA)

    def render_POST(self, request):
        return json.dumps({"success":True})


class UserProfile(Resource):

    DATA = {"name": "MyOOICI","institution":"OOICI","email_address":"myooici@gmail.com", "authenticating_organization":"Google", "profile":[{"name":"twitter", "value":"twitter.com/ooi"}, {"name":"mobilephone", "value":"555-555-5555"}]}

    def render_GET(self, request):
        import time; time.sleep(0.5) #mock out real latency
        return json.dumps(self.DATA)


    def render_POST(self, request):
        import time; time.sleep(0.5) #mock out real latency
        return json.dumps({"success":True})



class DownloadData(Resource):

    def render_GET(self, request):
        request.setHeader("Content-Disposition","attachment; filename=test_data.txt");
        return (" - Test Data - " * 1000)



class DataResource(Resource):


    dataResourceSummary = [{"datasetMetadata":{"title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E0AAAA", "institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)", "summary": "Real-time surface data from ASIMet system 1 on the tenth Northwest Tropcial Stlantic Station (NTAS) observatory.", "source": "NDBC SOS","references": "http://sdf.ndbc.noaa.gov/sos/","ion_time_coverage_start": "2008-08-01T00:50:00Z","ion_time_coverage_end": "2008-08-01T23:50:00Z","ion_geospatial_lat_min": -45.431,"ion_geospatial_lat_max": -45.431,"ion_geospatial_lon_min": 25.909,"ion_geospatial_lon_max": 25.909,"ion_geospatial_vertical_min": 0.2,"ion_geospatial_vertical_max": 0.0,"ion_geospatial_vertical_positive": "down"}, "date_registered": 1304696743406,"notificationSet": False}, {"datasetMetadata":{"title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","data_resource_id": "3119A67F-81F1-424F-8E19-4F28C4E0BBBB", "institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)","source": "NDBC SOS","references": "http://sdf.ndbc.noaa.gov/sos/","ion_time_coverage_start": "2008-08-01T00:50:00Z","ion_time_coverage_end": "2008-08-01T23:50:00Z","ion_geospatial_lat_min": -45.431,"ion_geospatial_lat_max": -45.431,"ion_geospatial_lon_min": 25.909,"ion_geospatial_lon_max": 25.909,"ion_geospatial_vertical_min": 0.2,"ion_geospatial_vertical_max": 0.0,"ion_geospatial_vertical_positive": "down"}, "date_registered": 1304696743406,"notificationSet": False}]

    dataResourceSummary_one = {"title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04802", "institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)", "summary": "Real-time surface data from ASIMet system 1 on the tenth Northwest Tropcial Stlantic Station (NTAS) observatory.", "source": "NDBC SOS","references": "http://sdf.ndbc.noaa.gov/sos/","ion_time_coverage_start": "2008-08-01T00:50:00Z","ion_time_coverage_end": "2008-08-01T23:50:00Z","ion_geospatial_lat_min": -45.431,"ion_geospatial_lat_max": -45.431,"ion_geospatial_lon_min": 25.909,"ion_geospatial_lon_max": 25.909,"ion_geospatial_vertical_min": 0.2,"ion_geospatial_vertical_max": 0.0,"ion_geospatial_vertical_positive": "down"}

    DATA = {
        "data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E0AAAA",
        "source":{"request_type": "DAP","base_url": "http://geoport.whoi.edu/thredds/dodsC/usgs/data0/rsignell/data/oceansites/OS_NTAS_2010_R_M-1.nc","max_ingest_millis": 6000,"ion_title": "NTAS1 Data Source","ion_description": "Data NTAS1","ion_name": "MyOOICI","ion_email": "myooici@gmail.com","ion_institution": "OOICI"},
        "variable": [{"units": "degree_north","standard_name": "latitude","long_name": "northward positive degrees latitude"},{"units": "degree_east","standard_name": "longitude","long_name": "eastward positive degrees longitude"},{"standard_name": "station_id","long_name": "integer station identifier"},{"units": "psu","standard_name": "sea_water_salinity","long_name": "water salinity at location","other_attributes": ["coordinates=time lon lat z"]},{"units": "seconds since 1970-01-01 00:00::00","standard_name": "time","long_name": "time","other_attributes": ["_CoordinateAxisType=Time"]},{"units": "m","standard_name": "depth","long_name": "depth below mean sea level","other_attributes": ["positive=down","::_CoordinateAxisType=Height","::_CoordinateZisPositive=down"]}]
    }

    findByUser = {"datasetByOwnerMetadata": [{"data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04802","title": "NTAS 10 Real-time Mooring Data, System 1","date_registered": 1304696743406,"ion_title": "NTAS1 Data Source","activation_state": "Public","update_interval_seconds": 60},{"data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04805","title": "WHOTS 7 near-real-time Mooring Data, System 2","date_registered": 1304696743328,"ion_title": "WHOTS2 Data Source","activation_state": "Private","update_interval_seconds": 0},{"data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04806","title": "7723 Moanalua RG No 1 at alt 1000 ft Oahu HI (212359157502601) - Instantaneous Value","date_registered": 1304696743377,"ion_title": "Moana Loa Data Source","activation_state": "Public","update_interval_seconds": 60},{"data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04808","title": "CONNECTICUT RIVER AT THOMPSONVILLE CT (01184000) - Instantaneous Value","date_registered": 1304696743367,"ion_title": "Connecticut River Data Source","activation_state": "Private","update_interval_seconds": 0},{"data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04803","title": "NTAS 10 Real-time Mooring Data, System 2","date_registered": 1304696743426,"ion_title": "NTAS2 Data Source","activation_state": "Private","update_interval_seconds": 0},{"data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04807","title": "CHOPTANK RIVER NEAR GREENSBORO MD (01491000) - Instantaneous Value","date_registered": 1304696743397,"ion_title": "Choptank River Data Source","activation_state": "Private","update_interval_seconds": 0},{"data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04804","title": "WHOTS 7 near-real-time Mooring Data, System 1","date_registered": 1304696743348,"ion_title": "WHOTS1 Data Source","activation_state": "Private","update_interval_seconds": 0},{"data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04801","title": "HYCOM","date_registered": 1304696743358,"ion_title": "HyCom Data Source","activation_state": "Private","update_interval_seconds": 0}]}

    def render_GET(self, request):
        #import time; time.sleep(0.8) #mock out real latency
        action = request.args["action"][0]
        if action == "detail":
            self.DATA.update({"dataResourceSummary":self.dataResourceSummary_one})
            return json.dumps(self.DATA)
        elif action == "findByUser":
            return json.dumps(self.findByUser)
        else:
            self.DATA.update({"dataResourceSummary":self.dataResourceSummary})
            return json.dumps(self.DATA)

    def render_POST(self, request):
        return json.dumps({"success":True})


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
