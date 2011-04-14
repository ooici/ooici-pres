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


class DownloadData(Resource):

    def render_GET(self, request):
        request.setHeader("Content-Disposition","attachment; filename=test_data.txt");
        return (" - Test Data - " * 1000)

class DataDetails(Resource):

    example_data = """
    <pre>
    Attributes {
     s {
      time {
        String _CoordinateAxisType "Time";
        Float64 actual_range 1.23586572e+9, 1.3028022e+9;
        String axis "T";
        String ioos_category "Time";
        String long_name "Time";
        String standard_name "time";
        String time_origin "01-JAN-1970 00:00:00";
        String units "seconds since 1970-01-01T00:00:00Z";
      }
      wtemp {
        Float32 _FillValue -99999.0;
        Float64 _Fillvalue -99999.0;
        Float32 actual_range 0.0, 26.1;
        Float64 data_max 13.54;
        Float64 data_min 8.54;
        String grid_mapping "crs";
        String ioos_category "Temperature";
        String long_name "Water temperature, IPTS-90";
        Float32 missing_value -99999.0;
        String standard_name "water_temperature";
        String units "degree_Celsius";
      }
      cond {
        Float32 _FillValue -99999.0;
        Float64 _Fillvalue -99999.0;
        Float32 actual_range 0.0, 4.5247;
        Float64 data_max 3.997;
        Float64 data_min 0.2597;
        String grid_mapping "crs";
        String ioos_category "Salinity";
        String long_name "Conductivity";
        Float32 missing_value -99999.0;
        String standard_name "conductivity";
        String units "S";
      }
      sal {
        Float32 _FillValue -99999.0;
        Float64 _Fillvalue -99999.0;
        Float32 actual_range 0.0, 35.432;
        String comment 
    "Salinity is based on the Practical Salinity Scale of 1978 (PSS78) and is 
    without dimensions. The CF-1.4 convention recognizes that PSS78 is 
    dimensionless yet recommends a unit of 0.001 to reflect parts per thousand";
        Float64 data_max 33.885;
        Float64 data_min 1.839;
        String grid_mapping "crs";
        String ioos_category "Salinity";
        String long_name "Salinity, IPSS-78";
        Float32 missing_value -99999.0;
        String standard_name "sea_water_salinity";
        String units "0.001";
      }
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
      chl_conc {
        Float32 _FillValue -99999.0;
        Float64 _Fillvalue -99999.0;
        Float32 actual_range 0.0, 48.39;
        Float64 data_max 3.3;
        Float64 data_min 0.08;
        String grid_mapping "crs";
        String ioos_category "Ocean Color";
        String long_name "In vivo fluorometric chlorophyll concentration";
        Float32 missing_value -99999.0;
        String standard_name "concentration_of_chlorophyll_in_sea_water";
        String units "microg L-1";
      }
      density {
        Float32 _FillValue -99999.0;
        Int32 _Fillvalue -99999;
        Float32 actual_range -1.4, 26.4;
        Int32 data_max 26;
        Int32 data_min 1;
        String grid_mapping "crs";
        String ioos_category "Salinity";
        String long_name "Density";
        Float32 missing_value -99999.0;
        String standard_name "sea_water_density";
        String units "kg m-3";
      }
      latitude {
        String _CoordinateAxisType "Lat";
        Float32 actual_range 38.31652, 38.31652;
        String axis "Y";
        String ioos_category "Location";
        String long_name "Latitude";
        String standard_name "latitude";
        String units "degrees_north";
        Int32 valid_max 90;
        Int32 valid_min -90;
      }
      longitude {
        String _CoordinateAxisType "Lon";
        Float32 actual_range -123.0709, -123.0709;
        String axis "X";
        String ioos_category "Location";
        String long_name "Longitude";
        String standard_name "longitude";
        String units "degrees_east";
        Int32 valid_max 180;
        Int32 valid_min -180;
      }
      altitude {
        String _CoordinateAxisType "Height";
        String _CoordinateZisPositive "up";
        Float32 actual_range -2.93, -2.93;
        String axis "Z";
        Float64 data_max -2.93;
        Float64 data_min -2.93;
        String ioos_category "Location";
        String long_name "Altitude";
        String positive "up";
        String standard_name "altitude";
        String units "m";
      }
     }
      NC_GLOBAL {
        String area "North California Coast";
        String cdm_data_type "Station";
        String contact "Data Manager (bmldata@ucdavis.edu)";
        String Conventions "COARDS, CF-1.0, Unidata Dataset Discovery v1.0";
        String creation_date "2011-04-01 00:06:08 UTC";
        Float64 Easternmost_Easting -123.0709;
        Float64 geospatial_lat_max 38.31652;
        Float64 geospatial_lat_min 38.31652;
        String geospatial_lat_units "degrees_north";
        Float64 geospatial_lon_max -123.0709;
        Float64 geospatial_lon_min -123.0709;
        String geospatial_lon_units "degrees_east";
        Float64 geospatial_vertical_max -2.93;
        Float64 geospatial_vertical_min -2.93;
        String geospatial_vertical_positive "up";
        String geospatial_vertical_reference "mean_sea_level";
        String geospatial_vertical_units "m";
        String history 
    "2011-04-01 00:06:08 UTC: File created at 2011-04-01 00:06:08 UTC
    2011-04-14 http://bmlsc.ucdavis.edu:8080/opendap/data/nc/CEN_BML_WTS.nc
    2011-04-14 http://bmlsc.ucdavis.edu:8080/erddap/tabledap/BML_WTS.html";
        String id "CEN_BML_WTS";
        String infoURL "http://bml.ucdavis.edu";
        String infoUrl "http://www.bml.ucdavis.edu/";
        String institution "University of California Davis, Bodega Marine Laboratory";
        String institution_dods_url "http://bmlsc.ucdavis.edu:8080/opendap/data";
        String institution_url "http://bml.ucdavis.edu";
        String keywords "EARTH SCIENCE > Oceans";
        String keywords_vocabulary "GCMD Earth Science Keywords. Version 5.3.3";
        String license 
    "The data may be used and redistributed for free but is not intended 
    for legal use, since it may contain inaccuracies. Neither the data 
    Contributor, ERD, NOAA, nor the United States Government, nor any 
    of their employees or contractors, makes any warranty, express or 
    implied, including warranties of merchantability and fitness for a 
    particular purpose, or assumes any legal liability for the accuracy, 
    completeness, or usefulness, of this information.";
        String naming_authority "org.cencoos.www";
        Float64 Northernmost_Northing 38.31652;
        String project "CenCOOS";
        String source "moored platform observation - fixed altitude";
        String sourceUrl "http://bmlsc.ucdavis.edu:8080/opendap/data/nc/CEN_BML_WTS.nc";
        Float64 Southernmost_Northing 38.31652;
        String standard_name_vocabulary "CF-11, CeNCOOS_WTS_V 1.2";
        String summary 
    "Water monitoring data collected in situ in Bodega Bay at a single location 
    in Horseshoe Cove from March 2009 to the present.  The data include 
    temperature, conductivity, pressure, salinity and density measurements from 
    a CTD.  Additional sensors collected chlorophyll fluorescence. The data were 
    collected by The Univeristy of California Davis' Bodega Marine Laboratory";
        String time_coverage_end "2011-04-14T17:30:00Z";
        String time_coverage_start "2009-03-01T00:02:00Z";
        String title "Bodega Bay Water Time Series";
        Float64 Westernmost_Easting -123.0709;
      }
    }
    </pre>
    """

    def render_GET(self, request):
        return self.example_data




class Service(Resource):

    def getChild(self, name, request):
        if name == "dataResources":
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
