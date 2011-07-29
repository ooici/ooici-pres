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

ALL_RESOURCES_DATA = [{"datasetMetadata":{"summary":"Near-real-time surface data from ASIMet system 1 on the seventh deployment of the WHOI HOT Station (WHOTS) observatory.","ion_geospatial_lon_min":-158,"ion_geospatial_lon_max":-158,"ion_time_coverage_start":"2011-06-20T20:00:00Z","ion_geospatial_vertical_max":0,"ion_geospatial_vertical_min":0,"title":"WHOTS 7 near-real-time Mooring Data, System 1","references":"http:// www.oceansites.org, http://uop.whoi.edu/projects/whots","user_ooi_id":"Is this used?","source":"Mooring observation","ion_time_coverage_end":"2011-06-21T15:59:59Z","ion_geospatial_lat_max":22.75,"download_url":"http://thredds.oceanobservatories.org/thredds/dodsC/ooiciData/7EF91B33-6784-4BE6-B014-ACEEA2BFFF3D.ncml.html","data_resource_id":"7EF91B33-6784-4BE6-B014-ACEEA2BFFF3D","ion_geospatial_lat_min":22.75,"ion_geospatial_vertical_positive":"down","comment":"Argos, hourly averaged ASIMet data","institution":"WHOI"},"notificationSet":False,"date_registered":1308686653471},{"datasetMetadata":{"ion_geospatial_lon_min":-74.8399963379,"ion_geospatial_lon_max":-74.8399963379,"ion_time_coverage_start":"2011-06-16T00:50:00Z","ion_geospatial_vertical_max":-5,"ion_geospatial_vertical_min":-5,"title":"SOS (urn:ioos:station:wmo:44014) Winds","references":"http://sdf.ndbc.noaa.gov/sos/","user_ooi_id":"Is this used?","source":"Sensor Observation Service (http://sdf.ndbc.noaa.gov/sos/server.php?)","ion_time_coverage_end":"2011-06-16T23:50:00Z","ion_geospatial_lat_max":36.6100006104,"download_url":"http://thredds.oceanobservatories.org/thredds/dodsC/ooiciData/871CE078-9676-4D55-931B-1C3A4022E859.ncml.html","data_resource_id":"871CE078-9676-4D55-931B-1C3A4022E859","ion_geospatial_lat_min":36.6100006104,"ion_geospatial_vertical_positive":"down","institution":"NOAA NDBC"},"notificationSet":False,"date_registered":1308269073544},{"datasetMetadata":{"ion_geospatial_lon_min":-74.8399963379,"ion_geospatial_lon_max":-74.8399963379,"ion_time_coverage_start":"2011-06-20T20:50:00Z","ion_geospatial_vertical_max":3.79999995232,"ion_geospatial_vertical_min":3.79999995232,"title":"SOS (urn:ioos:station:wmo:44014) Currents","references":"http://sdf.ndbc.noaa.gov/sos/","user_ooi_id":"Is this used?","source":"Sensor Observation Service (http://sdf.ndbc.noaa.gov/sos/server.php?)","ion_time_coverage_end":"2011-06-21T20:50:00Z","ion_geospatial_lat_max":36.6100006104,"download_url":"http://thredds.oceanobservatories.org/thredds/dodsC/ooiciData/65851927-E263-4809-B022-94AF0562F77D.ncml.html","data_resource_id":"65851927-E263-4809-B022-94AF0562F77D","ion_geospatial_lat_min":36.6100006104,"ion_geospatial_vertical_positive":"down","institution":"NOAA NDBC"},"notificationSet":True,"date_registered":1308686672556},{"datasetMetadata":{"ion_geospatial_lon_min":-72.6053695679,"ion_geospatial_lon_max":-72.6053695679,"ion_time_coverage_start":"2011-06-19T20:15:00Z","ion_geospatial_vertical_max":0,"ion_geospatial_vertical_min":0,"title":"CONNECTICUT RIVER AT THOMPSONVILLE CT (01184000) - Instantaneous Value","references":"http://waterservices.usgs.gov/rest/WOF-IV-Service.html","user_ooi_id":"Is this used?","source":"Instantaneous Values Webservice (http://waterservices.usgs.gov/mwis/iv?)","ion_time_coverage_end":"2011-06-21T19:45:00Z","ion_geospatial_lat_max":41.9873199463,"download_url":"http://thredds.oceanobservatories.org/thredds/dodsC/ooiciData/50B9D322-F80A-4271-925C-B91EBC66C19E.ncml.html","data_resource_id":"50B9D322-F80A-4271-925C-B91EBC66C19E","ion_geospatial_lat_min":41.9873199463,"ion_geospatial_vertical_positive":"down","institution":"USGS NWIS"},"notificationSet":True,"date_registered":1308686643570},{"datasetMetadata":{"summary":"Real-time surface data from ASIMet system 2 on the tenth Northwest Tropcial Stlantic Station (NTAS) observatory.","ion_geospatial_lon_min":-51,"ion_geospatial_lon_max":-51,"ion_time_coverage_start":"2011-06-20T20:00:00Z","ion_geospatial_vertical_max":0,"ion_geospatial_vertical_min":0,"title":"NTAS 10 Real-time Mooring Data, System 2","references":"http:// www.oceansites.org, http://uop.whoi.edu/projects/ntas","user_ooi_id":"Is this used?","source":"Mooring observation","ion_time_coverage_end":"2011-06-21T15:59:59Z","ion_geospatial_lat_max":15,"download_url":"http://thredds.oceanobservatories.org/thredds/dodsC/ooiciData/FFC86EFE-4918-49D2-9E30-8689581A7715.ncml.html","data_resource_id":"FFC86EFE-4918-49D2-9E30-8689581A7715","ion_geospatial_lat_min":15,"ion_geospatial_vertical_positive":"down","comment":"Argos, hourly averaged ASIMet data","institution":"WHOI"},"notificationSet":False,"date_registered":1308686624444},{"datasetMetadata":{"summary":"Near-real-time surface data from ASIMet system 1 on the seventh deployment of the WHOI HOT Station (WHOTS) observatory.","ion_geospatial_lon_min":-158,"ion_geospatial_lon_max":-158,"ion_time_coverage_start":"2011-06-20T20:00:00Z","ion_geospatial_vertical_max":0,"ion_geospatial_vertical_min":0,"title":"WHOTS 7 near-real-time Mooring Data, System 1","references":"http:// www.oceansites.org, http://uop.whoi.edu/projects/whots","user_ooi_id":"Is this used?","source":"Mooring observation","ion_time_coverage_end":"2011-06-21T15:59:59Z","ion_geospatial_lat_max":22.75,"download_url":"http://thredds.oceanobservatories.org/thredds/dodsC/ooiciData/57CDE283-2066-4107-9CD7-DD11BD1B83BB.ncml.html","data_resource_id":"57CDE283-2066-4107-9CD7-DD11BD1B83BB","ion_geospatial_lat_min":22.75,"ion_geospatial_vertical_positive":"down","comment":"Argos, hourly averaged ASIMet data","institution":"WHOI"},"notificationSet":False,"date_registered":1308686648713},{"datasetMetadata":{"summary":"Real-time surface data from ASIMet system 1 on the tenth Northwest Tropcial Stlantic Station (NTAS) observatory.","ion_geospatial_lon_min":-51,"ion_geospatial_lon_max":-51,"ion_time_coverage_start":"2011-06-15T23:00:00Z","ion_geospatial_vertical_max":0,"ion_geospatial_vertical_min":0,"title":"NTAS 10 Real-time Mooring Data, System 1","references":"http:// www.oceansites.org, http://uop.whoi.edu/projects/ntas","user_ooi_id":"Is this used?","source":"Mooring observation","ion_time_coverage_end":"2011-06-16T21:59:59Z","ion_geospatial_lat_max":15,"download_url":"http://thredds.oceanobservatories.org/thredds/dodsC/ooiciData/8F443AC7-B999-4F49-B4F1-B737BF07E167.ncml.html","data_resource_id":"8F443AC7-B999-4F49-B4F1-B737BF07E167","ion_geospatial_lat_min":15,"ion_geospatial_vertical_positive":"down","comment":"Argos, hourly averaged ASIMet data","institution":"WHOI"},"notificationSet":False,"date_registered":1308265819914},{"datasetMetadata":{"summary":"Near-real-time surface data from ASIMet system 2 on the seventh deployment of the WHOI HOT Station (WHOTS) observatory.","ion_geospatial_lon_min":-158,"ion_geospatial_lon_max":-158,"ion_time_coverage_start":"2011-06-20T20:00:00Z","ion_geospatial_vertical_max":0,"ion_geospatial_vertical_min":0,"title":"WHOTS 7 near-real-time Mooring Data, System 2","references":"http:// www.oceansites.org, http://uop.whoi.edu/projects/whots","user_ooi_id":"Is this used?","source":"Mooring observation","ion_time_coverage_end":"2011-06-21T14:00:00Z","ion_geospatial_lat_max":22.75,"download_url":"http://thredds.oceanobservatories.org/thredds/dodsC/ooiciData/D56B7445-BED7-4F23-9F9F-E05BC07A8618.ncml.html","data_resource_id":"D56B7445-BED7-4F23-9F9F-E05BC07A8618","ion_geospatial_lat_min":22.75,"ion_geospatial_vertical_positive":"down","comment":"Argos, hourly averaged ASIMet data","institution":"WHOI"},"notificationSet":False,"date_registered":1308686663068},{"datasetMetadata":{"ion_geospatial_lon_min":-74.8399963379,"ion_geospatial_lon_max":-74.8399963379,"ion_time_coverage_start":"2011-06-20T20:50:00Z","ion_geospatial_vertical_max":-5,"ion_geospatial_vertical_min":-5,"title":"SOS (urn:ioos:station:wmo:44014) Winds","references":"http://sdf.ndbc.noaa.gov/sos/","user_ooi_id":"Is this used?","source":"Sensor Observation Service (http://sdf.ndbc.noaa.gov/sos/server.php?)","ion_time_coverage_end":"2011-06-21T19:50:00Z","ion_geospatial_lat_max":36.6100006104,"download_url":"http://thredds.oceanobservatories.org/thredds/dodsC/ooiciData/78776F15-0193-45E3-921C-44597D32F8F9.ncml.html","data_resource_id":"78776F15-0193-45E3-921C-44597D32F8F9","ion_geospatial_lat_min":36.6100006104,"ion_geospatial_vertical_positive":"down","institution":"NOAA NDBC"},"notificationSet":False,"date_registered":1308686658126},{"datasetMetadata":{"summary":"Real-time surface data from ASIMet system 1 on the tenth Northwest Tropcial Stlantic Station (NTAS) observatory.","ion_geospatial_lon_min":-51,"ion_geospatial_lon_max":-51,"ion_time_coverage_start":"2011-06-20T20:00:00Z","ion_geospatial_vertical_max":0,"ion_geospatial_vertical_min":0,"title":"NTAS 10 Real-time Mooring Data, System 1","references":"http:// www.oceansites.org, http://uop.whoi.edu/projects/ntas","user_ooi_id":"Is this used?","source":"Mooring observation","ion_time_coverage_end":"2011-06-21T15:59:59Z","ion_geospatial_lat_max":15,"download_url":"http://thredds.oceanobservatories.org/thredds/dodsC/ooiciData/51D20C92-88D1-4D06-BD75-9C7654FE5EC1.ncml.html","data_resource_id":"51D20C92-88D1-4D06-BD75-9C7654FE5EC1","ion_geospatial_lat_min":15,"ion_geospatial_vertical_positive":"down","comment":"Argos, hourly averaged ASIMet data","institution":"WHOI"},"notificationSet":True,"date_registered":1308686607700},{"datasetMetadata":{"ion_geospatial_lon_min":-72.6053695679,"ion_geospatial_lon_max":-72.6053695679,"ion_time_coverage_start":"2011-06-15T23:45:00Z","ion_geospatial_vertical_max":0,"ion_geospatial_vertical_min":0,"title":"CONNECTICUT RIVER AT THOMPSONVILLE CT (01184000) - Instantaneous Value","references":"http://waterservices.usgs.gov/rest/WOF-IV-Service.html","user_ooi_id":"Is this used?","source":"Instantaneous Values Webservice (http://waterservices.usgs.gov/mwis/iv?)","ion_time_coverage_end":"2011-06-17T22:30:00Z","ion_geospatial_lat_max":41.9873199463,"download_url":"http://thredds.oceanobservatories.org/thredds/dodsC/ooiciData/D777193D-A6D9-49D5-91FA-58D980446164.ncml.html","data_resource_id":"D777193D-A6D9-49D5-91FA-58D980446164","ion_geospatial_lat_min":41.9873199463,"ion_geospatial_vertical_positive":"down","institution":"USGS NWIS"},"notificationSet":False,"date_registered":1308353849228},{"datasetMetadata":{"summary":"Real-time surface data from ASIMet system 2 on the tenth Northwest Tropcial Stlantic Station (NTAS) observatory.","ion_geospatial_lon_min":-51,"ion_geospatial_lon_max":-51,"ion_time_coverage_start":"2011-06-20T20:00:00Z","ion_geospatial_vertical_max":0,"ion_geospatial_vertical_min":0,"title":"NTAS 10 Real-time Mooring Data, System 2","references":"http:// www.oceansites.org, http://uop.whoi.edu/projects/ntas","user_ooi_id":"Is this used?","source":"Mooring observation","ion_time_coverage_end":"2011-06-21T15:59:59Z","ion_geospatial_lat_max":15,"download_url":"http://thredds.oceanobservatories.org/thredds/dodsC/ooiciData/679222D7-C661-407C-9483-951DF66D2038.ncml.html","data_resource_id":"679222D7-C661-407C-9483-951DF66D2038","ion_geospatial_lat_min":15,"ion_geospatial_vertical_positive":"down","comment":"Argos, hourly averaged ASIMet data","institution":"WHOI"},"notificationSet":False,"date_registered":1308686619252},{"datasetMetadata":{"ion_geospatial_lon_min":-74.8399963379,"ion_geospatial_lon_max":-74.8399963379,"ion_time_coverage_start":"2011-06-20T20:50:00Z","ion_geospatial_vertical_max":-4,"ion_geospatial_vertical_min":-4,"title":"SOS (urn:ioos:station:wmo:44014) air_temperature","references":"http://sdf.ndbc.noaa.gov/sos/","user_ooi_id":"Is this used?","source":"Sensor Observation Service (http://sdf.ndbc.noaa.gov/sos/server.php?)","ion_time_coverage_end":"2011-06-21T19:50:00Z","ion_geospatial_lat_max":36.6100006104,"download_url":"http://thredds.oceanobservatories.org/thredds/dodsC/ooiciData/417AD47D-98F9-4EE9-AAA8-8C4B19915DDE.ncml.html","data_resource_id":"417AD47D-98F9-4EE9-AAA8-8C4B19915DDE","ion_geospatial_lat_min":36.6100006104,"ion_geospatial_vertical_positive":"down","institution":"NOAA NDBC"},"notificationSet":True,"date_registered":1308686667919},{"datasetMetadata":{"summary":"Real-time surface data from ASIMet system 1 on the tenth Northwest Tropcial Stlantic Station (NTAS) observatory.","ion_geospatial_lon_min":-51,"ion_geospatial_lon_max":-51,"ion_time_coverage_start":"2011-06-20T20:00:00Z","ion_geospatial_vertical_max":0,"ion_geospatial_vertical_min":0,"title":"NTAS 10 Real-time Mooring Data, System 1","references":"http:// www.oceansites.org, http://uop.whoi.edu/projects/ntas","user_ooi_id":"Is this used?","source":"Mooring observation","ion_time_coverage_end":"2011-06-21T15:59:59Z","ion_geospatial_lat_max":15,"download_url":"http://thredds.oceanobservatories.org/thredds/dodsC/ooiciData/C5126491-7C23-4F62-A49B-BEB75B63EDEE.ncml.html","data_resource_id":"C5126491-7C23-4F62-A49B-BEB75B63EDEE","ion_geospatial_lat_min":15,"ion_geospatial_vertical_positive":"down","comment":"Argos, hourly averaged ASIMet data","institution":"WHOI"},"notificationSet":False,"date_registered":1308686613599},{"datasetMetadata":{"ion_geospatial_lon_min":-74.8399963379,"ion_geospatial_lon_max":-74.8399963379,"ion_time_coverage_start":"2011-06-20T20:50:00Z","ion_geospatial_vertical_max":-5,"ion_geospatial_vertical_min":-5,"title":"SOS (urn:ioos:station:wmo:44014) Winds","references":"http://sdf.ndbc.noaa.gov/sos/","user_ooi_id":"Is this used?","source":"Sensor Observation Service (http://sdf.ndbc.noaa.gov/sos/server.php?)","ion_time_coverage_end":"2011-06-21T18:50:00Z","ion_geospatial_lat_max":36.6100006104,"download_url":"http://thredds.oceanobservatories.org/thredds/dodsC/ooiciData/2F73BD07-AE90-42C2-A30A-5FD83AF06A8B.ncml.html","data_resource_id":"2F73BD07-AE90-42C2-A30A-5FD83AF06A8B","ion_geospatial_lat_min":36.6100006104,"ion_geospatial_vertical_positive":"down","institution":"NOAA NDBC"},"notificationSet":False,"date_registered":1308686634113}]


class Notifications(Resource):

    DATA = {"subscriptionListResults": [ {"subscriptionInfo": {"user_ooi_id": "A7B44115-34BC-4553-B51E-1D87617F12E0","data_src_id": "3319A67F-81F3-424F-8E69-4F28C4E04801","subscription_type": "EMAILANDDISPATCHER","email_alerts_filter": "UPDATES","dispatcher_alerts_filter": "UPDATES","dispatcher_script_path": "path","date_registered": 1304724473336},"datasetMetadata": {"user_ooi_id": "A7B44115-34BC-4553-B51E-1D87617F12E0","data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04801","title": "path","institution": "HYCOM","source": "HYCOM archive file","references": "","conventions": "","summary": "","comment": "","ion_time_coverage_start": "2011-04-11T00:00:00Z","ion_time_coverage_end": "2011-04-11T00:00:00Z","ion_geospatial_lat_min": 32.0284996033,"ion_geospatial_lat_max": 44.0166015625,"ion_geospatial_lon_min": -81.0400390625,"ion_geospatial_lon_max": -65.0400390625,"ion_geospatial_vertical_min": 1.0,"ion_geospatial_vertical_max": 32.0,"ion_geospatial_vertical_positive": "down","download_url": "http://localhost:8081/thredds/dodsC/scanData/3319A67F-81F3-424F-8E69-4F28C4E04801.ncml"}}, 
    {"subscriptionInfo": {"user_ooi_id": "A7B44115-34BC-4553-B51E-1D87617F1ZZZ","data_src_id": "3319A67F-81F3-424F-8E69-4F28C4E04ZZZ","subscription_type": "EMAILANDDISPATCHER","email_alerts_filter": "UPDATES","dispatcher_alerts_filter": "UPDATES","dispatcher_script_path": "path","date_registered": 1304724473336},"datasetMetadata": {"user_ooi_id": "A7B44115-34BC-4553-B51E-1D87617F12E0","data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04ZZZ","title": "path","institution": "FOOBAR","source": "FOOBAR archive file","references": "","conventions": "","summary": "","comment": "","ion_time_coverage_start": "2011-04-11T00:00:00Z","ion_time_coverage_end": "2011-04-11T00:00:00Z","ion_geospatial_lat_min": 32.0284996033,"ion_geospatial_lat_max": 44.0166015625,"ion_geospatial_lon_min": -81.0400390625,"ion_geospatial_lon_max": -65.0400390625,"ion_geospatial_vertical_min": 1.0,"ion_geospatial_vertical_max": 32.0,"ion_geospatial_vertical_positive": "down","download_url": "http://localhost:8081/thredds/dodsC/scanData/3319A67F-81F3-424F-8E69-4F28C4E04801.ncml"}},
    {"subscriptionInfo": {"user_ooi_id": "A7B44115-34BC-4553-B51E-1D87617F1ZZZ","data_src_id": "3319A67F-81F3-424F-8E69-4F28C4E04QQQ","subscription_type": "EMAILANDDISPATCHER","email_alerts_filter": "UPDATESANDDATASOURCEOFFLINE","dispatcher_alerts_filter": "UPDATES","dispatcher_script_path": "path","date_registered": 1304724473336},"datasetMetadata": {"user_ooi_id": "A7B44115-34BC-4553-B51E-1D87617F12E0","data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04QQQ","title": "path","institution": "BAZ","source": "BAZ archive file","references": "","conventions": "","summary": "","comment": "","ion_time_coverage_start": "2011-04-11T00:00:00Z","ion_time_coverage_end": "2011-04-11T00:00:00Z","ion_geospatial_lat_min": 32.0284996033,"ion_geospatial_lat_max": 44.0166015625,"ion_geospatial_lon_min": -81.0400390625,"ion_geospatial_lon_max": -65.0400390625,"ion_geospatial_vertical_min": 1.0,"ion_geospatial_vertical_max": 32.0,"ion_geospatial_vertical_positive": "down","download_url": "http://localhost:8081/thredds/dodsC/scanData/3319A67F-81F3-424F-8E69-4F28C4E04801.ncml"}}
]
    }

    def render_GET(self, request):
        return json.dumps(self.DATA)

    def render_POST(self, request):
        return json.dumps({"success":True})


class UserProfile(Resource):

    DATA = {"name": "MyOOICI","institution":"UCSD","email_address":"myooici@gmail.com", "authenticating_organization":"Google", "profile":[{"name":"twitter", "value":"twitter.com/ooi"}, {"name":"mobilephone", "value":"555-555-5555"}, {"name":"project_update","value":"true"}, {"name":"ocean_leadership_news","value":"true"}]}

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

    dataResourceSummary_one = {"title": "NDBC Sensor Observation Service data from \"http://sdf.ndbc.noaa.gov/sos/\"","data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04802", "institution": "NOAA\'s National Data Buoy Center (http://www.ndbc.noaa.gov/)", "summary": "Real-time surface data from ASIMet system 1 on the tenth Northwest Tropcial Stlantic Station (NTAS) observatory.", "source": "NDBC SOS","references": "http://sdf.ndbc.noaa.gov/sos/","ion_time_coverage_start": "2008-08-01T00:50:00Z","ion_time_coverage_end": "2008-08-01T23:50:00Z","ion_geospatial_lat_min": -45.431,"ion_geospatial_lat_max": -45.431,"ion_geospatial_lon_min": 25.909,"ion_geospatial_lon_max": 25.909,"ion_geospatial_vertical_min": 0.2,"ion_geospatial_vertical_max": 0.0,"ion_geospatial_vertical_positive": "down", "station_id":"Station 123abc", "base_url":"http://base.url.edu/testing123"
}

    DATA = {
        "data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E0AAAA",
        "source":{"request_type": "DAP","base_url": "http://geoport.whoi.edu/thredds/dodsC/usgs/data0/rsignell/data/oceansites/OS_NTAS_2010_R_M-1.nc","max_ingest_millis": 6000,"ion_title": "NTAS1 Data Source","ion_description": "Data NTAS1","ion_name": "MyOOICI","ion_email": "myooici@gmail.com","ion_institution": "OOICI"},
        "variable": [{"units": "degree_north","standard_name": "latitude","long_name": "northward positive degrees latitude"},{"units": "degree_east","standard_name": "longitude","long_name": "eastward positive degrees longitude"},{"standard_name": "station_id","long_name": "integer station identifier"},{"units": "psu","standard_name": "sea_water_salinity","long_name": "water salinity at location","other_attributes": ["coordinates=time lon lat z"]},{"units": "seconds since 1970-01-01 00:00::00","standard_name": "time","long_name": "time","other_attributes": ["_CoordinateAxisType=Time"]},{"units": "m","standard_name": "depth","long_name": "depth below mean sea level","other_attributes": ["positive=down","::_CoordinateAxisType=Height","::_CoordinateZisPositive=down"]}],

"variable": [{"standard_name": "station_id","name": "stnId"}, {"units": "degree_east","standard_name": "longitude","long_name": "longitude","name": "lon","other_attributes": [{"name": "_CoordinateAxisType","value": "Lon"}]}, {"units": "seconds since 1970-01-01 00:00:00","standard_name": "time","long_name": "time","name": "time","other_attributes": [{"name": "_CoordinateAxisType","value": "Time"}],"dimensions": [{"name": "time","length": 947.0}]}, {"units": "m","standard_name": "depth","long_name": "depth below mean sea level","name": "z","other_attributes": [{"name": "positive","value": "down"},{"name": "missing_value","value": "-9999.0"},{"name": "_CoordinateAxisType","value": "Height"},{"name": "_CoordinateZisPositive","value": "down"}]}, {"units": "ft","standard_name": "precipitation_total","long_name": "total precipitation at gauge location in feet","name": "precipitation_total","other_attributes": [{"name": "coordinates","value": "time lon lat"}],"dimensions": [{"name": "time","length": 947.0}, {"name": "height","length": 111.0}]}],

"dimensions": [{"name": "time","length": 947.0}, {"name": "height","length": 111.0}],

"other_attributes": [{"name": "CF:featureType","value": "station"},{"name": "Conventions","value": "CF-1.5"},{"name": "history","value": "Converted from WaterML1.1 to OOI CDM by net.ooici.eoi.datasetagent.impl.UsgsAgent"}]


    }

    findByUser = {"datasetByOwnerMetadata": [{"data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04802","title": "NTAS 10 Real-time Mooring Data, System 1","date_registered": 1304696743406,"ion_title": "NTAS1 Data Source","activation_state": "Public","update_interval_seconds": 60},{"data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04805","title": "WHOTS 7 near-real-time Mooring Data, System 2","date_registered": 1304696743328,"ion_title": "WHOTS2 Data Source","activation_state": "Private","update_interval_seconds": 0},{"data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04806","title": "7723 Moanalua RG No 1 at alt 1000 ft Oahu HI (212359157502601) - Instantaneous Value","date_registered": 1304696743377,"ion_title": "Moana Loa Data Source","activation_state": "Public","update_interval_seconds": 60},{"data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04808","title": "CONNECTICUT RIVER AT THOMPSONVILLE CT (01184000) - Instantaneous Value","date_registered": 1304696743367,"ion_title": "Connecticut River Data Source","activation_state": "Private","update_interval_seconds": 0},{"data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04803","title": "NTAS 10 Real-time Mooring Data, System 2","date_registered": 1304696743426,"ion_title": "NTAS2 Data Source","activation_state": "Private","update_interval_seconds": 0},{"data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04807","title": "CHOPTANK RIVER NEAR GREENSBORO MD (01491000) - Instantaneous Value","date_registered": 1304696743397,"ion_title": "Choptank River Data Source","activation_state": "Private","update_interval_seconds": 0},{"data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04804","title": "WHOTS 7 near-real-time Mooring Data, System 1","date_registered": 1304696743348,"ion_title": "WHOTS1 Data Source","activation_state": "Private","update_interval_seconds": 0},{"data_resource_id": "3319A67F-81F3-424F-8E69-4F28C4E04801","title": "HYCOM","date_registered": 1304696743358,"ion_title": "HyCom Data Source","activation_state": "Private","update_interval_seconds": 0}]}

    def render_GET(self, request):
        import time; time.sleep(0.5) #mock out real latency
        action = request.args["action"][0]
        #request.setResponseCode(400)
        #return "Bad stuff happened"
        #error_test = this_will_cause_a_500_error
        if action == "detail":
            #self.DATA["source"]["ion_description"] = request.args["data_resource_id"][0]
            self.DATA["source"]["visualization_url"] = "http://visualize.whirledpeas.edu/crazy-visuals"
            self.DATA.update({"dataResourceSummary":self.dataResourceSummary_one})
            return json.dumps(self.DATA)
        elif action == "findByUser":
            return json.dumps(self.findByUser)
        else:
            self.DATA.update({"dataResourceSummary":ALL_RESOURCES_DATA}) #self.dataResourceSummary*10})
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
