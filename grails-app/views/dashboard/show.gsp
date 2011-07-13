<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <title>OOI Integrated Observatory Network</title>

  <link rel="stylesheet" type="text/css" href="css/ciux-default.css"/>
  <link rel="stylesheet" type="text/css" media="screen" href="css/jquery-ui-1.8.6.custom.css"/> 
  <link rel="stylesheet" type="text/css" media="screen" href="css/ciux-datatable-page.css"/>
  <link rel="stylesheet" type="text/css" media="screen" href="css/ciux-datatable-table-jui.css"/>
  <link rel="stylesheet" type="text/css" media="screen" href="css/colorbox.css"/>
  <link rel="stylesheet" type="text/css" media="screen" href="css/ooici_ux-main.css"/>

  <script src="js/jquery-1.4.4.min.js" type="text/javascript"></script>
  <script src="js/jquery-ui-1.8.6.custom.min.js" type="text/javascript"></script>
  <script src="js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
  <script src="js/jquery.layout.min.js" type="text/javascript"></script>
  <script src="js/jquery.dataTables.min.js" type="text/javascript"></script>
  <script src="js/jquery.colorbox.min.js" type="text/javascript"></script>
  <script src="js/jquery.multi-open-accordion.js" type="text/javascript"></script>
  <script src="js/jquery.tmpl.min.js" type="text/javascript"></script>
  <script src="js/json2.js" type="text/javascript"></script>
  <script src="js/underscore-min.js" type="text/javascript"></script>
  <script src="js/backbone-min.js" type="text/javascript"></script>
  <script src="js/ooici_ux.js" type="text/javascript"></script>
  <script src="js/ooici_ux-models.js" type="text/javascript"></script>
  <script src="js/ooici_ux-views.js" type="text/javascript"></script>
  <script src="js/ooici_ux-controllers.js" type="text/javascript"></script>
  <script type="text/javascript">
  $(function() {
    try {
        window.OOI_ROLES = JSON.parse('<%= OOI_ROLES %>');
        window.REGISTERED = JSON.parse('<%= REGISTERED %>');
        window.CERTIFICATE_TIMEOUT_SECS = JSON.parse('<%= CERTIFICATE_TIMEOUT_SECS %>');
        window.INSTRUMENT_MONITOR_URL = '<%= INSTRUMENT_MONITOR_URL %>';
    } catch(err){ //For development:
        if (document.location.hostname === 'localhost'){
            window.OOI_ROLES = ['USER', 'ADMIN', 'DATA_PROVIDER', 'MARINE_OPERATOR', 'EARLY_ADOPTER'];
            window.REGISTERED = true;
            window.CERTIFICATE_TIMEOUT_SECS = 0;
            window.INSTRUMENT_MONITOR_URL = 'http://example.edu';
        } else {
            return alert("One of: OOI_ROLES, REGISTERED, INSTRUMENT_MONITOR_URL failed to load.");
        }
    }
    OOI.init();
  });
  </script>
</head>
<body id="body">
<div id="loading_message"><img src="images/loading_wave.gif"><span class="msg">Loading...</span></div>

<!-- Cheesy hack nodes that exist only for binding events, fix a better way later -->
<div id="delegate-me-01">
<div id="delegate-me-02">
<div id="delegate-me-03">
<div id="delegate-me-04">
<div id="delegate-me-05">

<div id="layoutContainer">

  <div id="top" class="ui-layout-north">
    <div id="branding">
    </div>
    <div id="userbar">
      <a id="login_link" title="<%= HELP.P1097_SP277 %>" href="login">Sign In&nbsp;&nbsp;&nbsp;</a>
      <a id="logout_link" title="<%= HELP.P1084_SP244 %>" href="logout">Sign Out&nbsp;&nbsp;&nbsp;</a>
      <a id="registration_link" title="<%= HELP.P1097_SP278 %>" href="#">Create an Account&nbsp;</a>
      <a id="account_settings_link" title="<%= HELP.P1084_SP242 %>" href="#">Account Settings&nbsp;&nbsp;&nbsp;&nbsp;</a>
      <a id="help_link" title="<%= HELP.P1084_SP243 %>" href="static/IONHelpContent.pdf">Help&nbsp;</a>
    </div>
  </div><!-- end .ui-layout-north -->

  <div class="ui-layout-center hidden">

    <div class="center-center">
      <div id="datatable">
      <h1>Data Resources</h1>
      <table id="datatable_100" class="datatable" cellpadding="0" cellspacing="0" border="0">
       <thead><tr><th>Title</th><th>Notification Set</th><th>Provider</th><th>Type</th><th>Date Registered</th><th>Details</th> </tr></thead>
        <tbody></tbody>
      </table>

       <table id="datatable_104" class="datatable" cellpadding="0" cellspacing="0" border="0">
          <thead><tr><th>&nbsp;</th><th>Resource Title</th><th>Source</th><th>Notification Initiated</th></tr></thead>
          <tbody></tbody>
       </table>

       <table id="datatable_106" class="datatable" cellpadding="0" cellspacing="0" border="0">
            <thead><tr><th>&nbsp;</th><th>Active</th><th>Availability</th><th>My Registration Title</th><th>Original Source Title</th><th>Publication Date</th><th>Details</th></tr></thead>
            <tbody></tbody>
       </table>

       <table id="datatable_109_epus" class="datatable" cellpadding="0" cellspacing="0" border="0">
          <thead><tr><th>EPU Controller ID</th><th>Details</th></tr></thead>
          <tbody></tbody>
       </table>

       <table id="datatable_109_users" class="datatable" cellpadding="0" cellspacing="0" border="0">
          <thead><tr><th>OOI ID</th><th>Name</th><th>Email</th><th>Institution</th><th>Subject</th><th>Role</th><th>Details</th></tr></thead>
          <tbody></tbody>
       </table>

       <table id="datatable_109_datasets" class="datatable" cellpadding="0" cellspacing="0" border="0">
          <thead><tr><th>OOI ID</th><th>Title</th><th>Details</th></tr></thead>
          <tbody></tbody>
       </table>

       <table id="datatable_109_datasources" class="datatable" cellpadding="0" cellspacing="0" border="0">
          <thead><tr><th>OOI ID</th><th>Station ID</th><th>Details</th></tr></thead>
          <tbody></tbody>
       </table>

       <table id="datatable_instruments" class="datatable" cellpadding="0" cellspacing="0" border="0">
          <thead><tr><th>OOI ID</th><th>Name</th><th>Description</th><th>Manufacturer</th><th>Model</th><th>Serial</th><th>Firmware</th></tr></thead>
          <tbody></tbody>
       </table>


            <div style="display:none" id="datatable_details_scroll"><span id="dataset_scroll_left" class="arrow dataset_scroll"><img src="images/Arrow-Left.png"/></span><span id="dataset_return_button"><img style="width:30px;height:20px" src="images/List-View.png"/></span><span id="dataset_scroll_right" class="arrow dataset_scroll"><img src="images/Arrow-Right.png"/></span></div>

       <div id="datatable_details_container"></div>
      </div><!-- end #datatable -->

     </div><!-- end .center-center -->
    <div class="center-south">
     <div id="datatable_select_buttons">
      <span class="select_button" id="delete_selected" title="<%= HELP.P1047_SP156 %>">Delete Selected</span>
      <span class="select_button" id="deselect_all">Deselect All</span>
      <span class="select_button" id="select_all">Select All</span>
     </div>
    </div><!-- end .center-south -->

  </div><!-- end .ui-layout-center -->

  <div class="ui-layout-west hidden">
   <div class="west-center">
    <div id="westMultiOpenAccordion">
      <h3><a href="#">Resource Selector</a></h3>
      <div style="padding-left: 10px; padding-right: 10px;">
        <div id="resource_selector_view">  
          <span id="view_existing_tab" title="<%= HELP.P1000_SP6 %>" class="resource_selector_tab selected">View Existing</span>
          <span id="resource_selector_view_spacer">|</span>  
          <span id="register_new_tab" title="<%= HELP.P1000_SP7 %>" title="<%= HELP.P1000_SP7 %>" class="resource_selector_tab">Register New</span>
        </div>

        <div id="view_existing">
          <form action="">
            <table>
              <tr>
                <td><input id="radioAllPubRes" title="<%= HELP.P1000_SP8 %>" class="resource_selector controlradios" name="group1" type="radio"/></td>
                <td><label for="radioAllPubRes" title="<%= HELP.P1000_SP8 %>">All Registered Resources</label></td>
                <td><input id="radioMySub" title="<%= HELP.P1000_SP10 %>" class="resource_selector controlradios" name="group1" type="radio"/></td>
                <td><label class="non_guest" for="radioMySub" title="<%= HELP.P1000_SP10 %>">My Notification Settings</label></td>
              </tr>
              <tr>
                <td><input id="radioMyPubRes" title="<%= HELP.P1000_SP9 %>" class="resource_selector controlradios" name="group1" type="radio"/></td>
                <td><label class="non_guest" for="radioMyPubRes" title="<%= HELP.P1000_SP9 %>">My Registered Resources</label></td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
            </table>
          </form>
        </div>

        <div id="register_new">
            <table>
              <tr><td><span id="data_resource_url_text">Source URL:</span><textarea id="data_resource_url" title="<%= HELP.P1030_SP128 %>"></textarea></td></tr>
            </table>
        </div>

      </div>

    <h3 class="view_existing admin_role"><a href="#">Administration Selector</a></h3>
      <div id="view_admin_tools" class="view_existing admin_role" style="padding-left: 10px; padding-right: 10px;">
        <form action="">
          <table>
            <tr>
              <td><input id="radioRunEPU" title="<%= HELP.P1028_SP127 %>" class="admin_selector controlradios" name="group1" type="radio"/></td>
              <td><label for="radioRunEPU" title="<%= HELP.P1028_SP127 %>">Running EPUs</label></td>
              <td><input id="radioRegUsers" title="<%= HELP.P1028_SP126 %>" class="admin_selector controlradios" name="group1" type="radio"/></td>
              <td><label for="radioRegUsers" title="<%= HELP.P1028_SP126 %>">Registered Users</label></td>
            </tr>
            <tr>
              <td><input id="radioDatasets" title="<%= HELP.P1028_SP128 %>" class="admin_selector controlradios" name="group1" type="radio"/></td>
              <td><label for="radioDatasets" title="<%= HELP.P1028_SP128 %>">Data Sets</label></td>
              <td><input id="radioDatasources" title="<%= HELP.P1028_SP129 %>" class="admin_selector controlradios" name="group1" type="radio"/></td>
              <td><label for="radioDatasources" title="<%= HELP.P1028_SP129 %>">Data Sources</label></td>
            </tr>
          </table>
        </form>
      </div>

      <h3 class="marine_op_role instrument_view"><a href="#">Instrument Management</a></h3>
      <div class="marine_op_role instrument_view" style="padding-left: 10px; padding-right: 10px;">
        <div id="view_instruments">
          <form action="">
            <table>
              <tr>
                <td><a href="#instrument/list"><input id="radioViewInstruments" title="<%= HELP.P1099_SP1 %>" class="resource_selector controlradios" name="group1" type="radio"/></a></td>
                <td style="padding-right: 30px;"><a href="#instrument/list"><label for="radioViewInstruments" title="<%= HELP.P1099_SP1 %>">View All Instruments</label></a></td>
                <td><a href="#instrument/new"><input id="radioNewInstrument" title="<%= HELP.P1099_SP2 %>" class="resource_selector controlradios" name="group1" type="radio"/></a></td>
                <td style="padding-right: 30px;"><a href="#instrument/new"><label for="radioNewInstrument" title="<%= HELP.P1099_SP2 %>">Register New Instrument</label></a></td>
              </tr>
            </table>
          </form>
        </div>
      </div>

      <h3 class="view_existing"><a href="#">Geospatial Extent</a></h3>
      <div class="view_existing" id="geospatialContainer">
        <div class="boundingBoxRadios">
          <form action="">
            <table>
              <tr>
                <td>&nbsp;</td>
                <td><strong style="position:relative;left:-30px">Bounding Box:</strong></td>
              </tr>
              <tr>
                <td><input id="radioBoundingAll" title="<%= HELP.P1001_SP19 %>" class="all bounding" name="group1" type="radio"/>
                    <label for="radioBoundingAll" title="<%= HELP.P1001_SP19 %>">All</label></td>
                <td><input id="radioBoundingDefined" title="<%= HELP.P1001_SP20 %>" class="defined bounding" name="group1" type="radio"/>
                    <label for="radioBoundingDefined" title="<%= HELP.P1001_SP20 %>">Defined</label></td>
              </tr>
            </table>
          </form>
        </div>
        <div class="altitudeRadios">
          <form action="">
            <table>
              <tr>
                <td>&nbsp;</td>
                <td><strong style="position:relative;left:-30px">Vertical Extent</strong><span id="vertical_extent_units_toggle" title="<%= HELP.P1001_SP27 %>" >m</span></td>
              </tr>
              <tr>
                <td><input id="radioAltitudeAll" title="<%= HELP.P1001_SP21 %>" class="all altitude" name="group1" type="radio"/>
                    <label for="radioAltitudeAll" title="<%= HELP.P1001_SP21 %>">All</label></td>
                <td><input id="radioAltitudeDefined" title="<%= HELP.P1001_SP22 %>" class="defined altitude" name="group1" type="radio"/>
                    <label for="radioAltitudeDefined" title="<%= HELP.P1001_SP22 %>">Defined</label></td>
              </tr>
            </table>
          </form>
        </div>
        <div class="boundingBoxControls">
          <span class="bb_direction Ntext" title="<%= HELP.P1001_SP26 %>">N</span>
          <input id="ge_bb_north" title="<%= HELP.P1001_SP23 %>" class="north textfield" name="north" type="text" size="11" maxlength="11"/>
          <span class="bb_direction Stext" title="<%= HELP.P1001_SP26 %>">S</span>
          <input id="ge_bb_south" title="<%= HELP.P1001_SP23 %>" class="south textfield" name="south" type="text" size="11" maxlength="11"/>
          <span class="bb_direction Etext" title="<%= HELP.P1001_SP26 %>">E</span>
          <input id="ge_bb_east" title="<%= HELP.P1001_SP23 %>" class="east textfield" name="east" type="text" size="11" maxlength="11"/>
          <span class="bb_direction Wtext" title="<%= HELP.P1001_SP26 %>">W</span>
          <input id="ge_bb_west" title="<%= HELP.P1001_SP23 %>" class="west textfield" name="west" type="text" size="11" maxlength="11"/>
          <span class="NSEWBackgroundBorder"></span>
        </div>
        <div class="altitudeControls">
            <span class="altitudeUpper"><div>Upper Bound</div>
            <input  id="ge_altitude_ub" title="<%= HELP.P1001_SP28 %>" class="textfield" name="altUpper" type="text" size="6" maxlength="6"/>
            <img  id="vertical_extent_above" title="<%= HELP.P1001_SP28 %>" class="vertical_extent_button" src="images/Below-Sea-Level-Simple.png">
            </span>
            <span class="altitudeLower"><div>Lower Bound</div>
            <input id="ge_altitude_lb" title="<%= HELP.P1001_SP28 %>" class="textfield" name="altLower" type="text" size="6" maxlength="6"/>
            <img id="vertical_extent_below" title="<%= HELP.P1001_SP28 %>" class="vertical_extent_button" src="images/Below-Sea-Level-Simple.png">
            </span>
        </div>
      </div>
      <h3 class="view_existing"><a href="#">Temporal Extent</a></h3>
      <div id="temporalExtent" class="temporalExtentContainer view_existing">
        <form action="">
          <table>
            <tr>
              <td class="boldText">Time Range:</td>
              <td><input id="TE_timeRange_all" title="<%= HELP.P1003_SP35 %>" class="all" name="group1" type="radio"/></td>
              <td><label for="TE_timeRange_all" title="<%= HELP.P1003_SP35 %>">All</label></td>
              <td><input id="TE_timeRange_defined" title="<%= HELP.P1003_SP36 %>" class="defined" name="group1" type="radio"/></td>
              <td><label for="TE_timeRange_defined" title="<%= HELP.P1003_SP36 %>">Defined</label></td>
            </tr>
          </table>
        </form>
      <div class="temporalExtentControls">
        <span class="te-from boldText">From:</span>
        <input id="te_from_input" title="<%= HELP.P1003_SP17 %>" name="te_from_input" type="text" size="21" maxlength="21"/>
        <br><span class="te-to boldText">To:</span>
        <input id="te_to_input" title="<%= HELP.P1003_SP17 %>" name="te_to_input" type="text" size="21" maxlength="21"/>
        <div style="color:#aaa" class="te-footer-text">ISO Formatted Time in UTC</div>
      </div><!-- end .temporalExtentControls -->
      </div>

    </div>
   </div><!-- end .west-center -->
   <div id="west_south" class="west-south">
      <!--<button id="geospatial_selection_button" disabled="disabled">Geospatial Selection Query</button>-->
      <button id="apply_filter_button" disabled="disabled">Apply Filter</button>
      <button id="register_resource_button" title="<%= HELP.P1062_SP190 %>">Validate Resource</button>
   </div><!-- end .west-south -->

 </div> <!-- end .ui-layout-west -->

  <div id="east_sidebar" class="ui-layout-east hidden">
   <div class="east-center">
    <div id="eastMultiOpenAccordion">
      <h3 class="data_sources "><a id="rp_dsTitle" href="#">Resource Registration Description</a></h3>
      <div class="data_sources registered_resource_editable">
        <div id="ds_title"></div><br>
      </div>

      <h3 class="data_sources"><a href="#">Resource Registration Contact Information</a></h3>
      <div class="data_sources registered_registration_contact_editable">
        <div id="ds_publisher_contact"></div><br>
      </div>

      <h3 class="data_sources my_resources_sidebar"><a href="#">Resource Availability Settings</a></h3>
      <div class="data_sources registered_registration_availability_editable my_resources_sidebar">
        <div><input id="availability_radio_private" title="<%= HELP.P1064_SP255 %>" name="availability_radio" type="radio"/><label for="availability_radio_private" title="<%= HELP.P1064_SP255 %>">Resource is private and available to me only</label></div>
        <div><input id="availability_radio_public" title="<%= HELP.P1064_SP255 %>" name="availability_radio" type="radio"/><label for="availability_radio_public" title="<%= HELP.P1064_SP255 %>">Resource is publically available</label></div>
      </div>

      <h3 class="data_sources my_resources_sidebar"><a href="#">Resource Activation Settings</a></h3>
      <div class="data_sources my_resources_sidebar" id="registered_registration_activation_polling_editable">
        <div><input id="polling_radio_yes" class="polling_radio" title="<%= HELP.P1064_SP196 %>" name="polling_radio" type="radio"/><label for="polling_radio_yes" title="<%= HELP.P1064_SP196 %>">Activate resource <br><span style="margin-left:40px">Poll every: </label><input id="polling_time" title="<%= HELP.P1064_SP197 %>" name="" type="text" size="8" maxlength="8"/> DD:HH:MM</span></div>
        <div><input id="polling_radio_no" class="polling_radio" title="<%= HELP.P1064_SP196 %>" name="polling_radio" type="radio"/><label for="polling_radio_no" title="<%= HELP.P1064_SP196 %>">Deactivate resource</label></div>
      </div>

      <h3 class="data_sources"><a href="#">Original Source Description</a></h3>
      <div class="data_sources" id="ds_source"></div>

      <h3 class="data_sources"><a href="#">Original Source Contact Information</a></h3>
      <div class="data_sources" id="ds_source_contact"></div>

      <h3 class="data_sources"><a href="#">Geospatial Coverage</a></h3>
      <div class="data_sources" id="ds_geospatial_coverage"></div>

      <h3 class="data_sources"><a href="#">Temporal Coverage</a></h3>
      <div class="data_sources" id="ds_temporal_coverage"></div>

      <h3 class="data_sources"><a href="#">Variables</a></h3>
      <div class="data_sources" id="ds_variables">Variables</div>

      <h3 class="data_sources"><a href="#">References</a></h3>
      <div class="data_sources" id="ds_references">References</div>

    <br>
    <h3 id="notification_settings" class="notification_settings"><a href="#">Notification Settings</a></h3>
      <div class="notification_settings">
        <form action="">
          <p>Send notifications when:</p>
          <p id="notification_details"></p>
          <p><input id="updateWhenAvailable" title="<%= HELP.P1023_SP120 %>" class="_controlradios notifications_dispatcher" name="group1" type="checkbox"/><label for="updateWhenAvailable" title="<%= HELP.P1023_SP120 %>">Update when available</label></p>
        <p><input id="datasourceIsOffline" title="<%= HELP.P1067_SP276 %>" class="_controlradios notifications_dispatcher" name="group1" type="checkbox"/><label for="datasourceIsOffline" title="<%= HELP.P1067_SP276 %>">Datasource is offline</label></p>
        </form>
      </div><!-- end #notification_settings -->

    <h3 id="dispatcher_settings" class="dispatcher_settings early_adopter"><a href="#">Dispatcher Settings</a></h3>
      <div class="dispatcher_settings early_adopter">
        <form action="">
          <p class="dispatcher_details">Notification Triggers</p>
          <p>Send notifications when:</p>
          <p><input id="dispatcher_updateWhenAvailable" title="<%= HELP.P1023_SP120 %>" class="_controlradios notifications_dispatcher" name="group1" type="checkbox"/><label for="dispatcher_updateWhenAvailable" title="<%= HELP.P1023_SP120 %>">Update when available</label></p>
          <p><input id="dispatcher_datasourceIsOffline" title="<%= HELP.P1067_SP276 %>" class="_controlradios notifications_dispatcher" name="group1" type="checkbox"/><label for="dispatcher_datasourceIsOffline" title="<%= HELP.P1067_SP276 %>">Datasource is offline</label></p>
          <p class="dispatcher_details">Dispatcher Script</p>
          Dispatcher Script Path: <input id="dispatcher_script_path" title="<%= HELP.P1067_SP125 %>" type="text"/>
        </form>
      </div><!-- end #dispatcher_settings -->

      <h3 class="instrument_agent"><a href="#">Instrument Agent Details</a></h3>
      <div class="instrument_agent" id="instrument_agent_details">&nbsp;</div>

      <h3 class="user_settings"><a href="#">User Role Settings</a></h3>
      <div class="user_settings" id="user_role_panel">
        <label for="user_setting_role">Role: </label>
        <select id="user_setting_role" name="user_setting_role" multiple size="4">
          <option value="DATA_PROVIDER">Data Provider</option>
          <option value="MARINE_OPERATOR">Marine Operator</option>
          <option value="EARLY_ADOPTER">Early Adopter</option>
          <option value="ADMIN">Administrator</option>
        </select>
      </div>

    </div><!-- end #eastMultiOpenAccordion -->
   </div><!-- end .east-center -->

   <div class="east-south">
      <button id="download_dataset_button" title="<%= HELP.P1073_SP63 %>" disabled="disabled">Download</button>
      <button id="setup_notifications" title="<%= HELP.P1073_SP64 %>" disabled="disabled">Setup Notifications</button>
      <button id="start_notifications" title="<%= HELP.P1072_SP211 %>">Start Notifications</button>
      <button id="save_myresources_changes" title="<%= HELP.P1065_SP198 %>" disabled="disabled">Save Changes</button>
      <button id="save_notifications_changes" title="<%= HELP.P1065_SP198 %>" title="<%= HELP.P1065_SP198 %>" disabled="disabled">Save Changes</button>
      <button id="save_register_resource" title="<%= HELP.P1062_SP189 %>" style="display: none;">Register Resource</button>
      <!--
      <button id="agent_start" class="agent_button" style="display: none;">Start Agent</button>
      <button id="agent_stop" class="agent_button" style="display: none;">Stop Agent</button>
      -->
      <button id="agent_sampling_start" class="agent_button" style="display: none;">Start Sampling</button>
      <button id="agent_sampling_stop" class="agent_button" style="display: none;">Stop Sampling</button>
      <button id="agent_sample_monitor" class="agent_button" style="display: none;">Sample Monitor</button>

      <button id="save_user_changes" title="<%= HELP.P1065_SP198 %>" class="user_button" style="display: none;">Save Changes</button>
   </div><!-- end .east-south -->

  </div><!-- end .ui-layout-east -->

</div>

<!-- Cheesy hack nodes that exist only for binding events, fix a better way later -->
</div>
</div>
</div>
</div>
</div>


<div id="modal_dialogs" style="display:none">


<div id="registration_dialog" style="width:640px; height:170px; padding:10px; padding-top:2px; background-image: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#494949), to(#A9A9A9)); border:1px solid #000; box-shadow: 3px 3px 3px #000">
    <!--Header-->
    <div align="left" style="height:20px; width:100%; padding:5px; padding-left:0" class="header">OOI Registration</div>
    <!--Body-->
    <div align="left" style=" background-color:#F4F4F6; width:590px; height:80px; padding:10px; padding-left:30px; padding-right:20px; border:1px solid #494949;border-bottom:none">
      <p class="heading">Registration is a two-step Process</p>
      <p>STEP 1: Get your credentials by clicking on the 'Get Credentials' button</p>
      <p>STEP 2: After you have your credentials, you will be able to personalize your OOI Account.</p>
      </div>
    <div align="left" style=" background-color:#FFF; width:590px; height:30px; padding:5px; padding-left:30px; padding-right:20px; border:1px solid #494949;">
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
        <tbody><tr>
          <td>&nbsp;</td>
          <td width="100px"><a href="#" class="modal_close colorbox_button blue-button-inactive">Cancel</a></td>
          <td width="10px">&nbsp;</td>
          <td width="100px"><a href="login" class="colorbox_button blue-button-inactive-focus">Get Credentials</a></td>
        </tr>
      </tbody></table></div>
 </div><!-- end #registration_dialog -->



<div id="account_settings" style="width:640px; height:606px; padding:10px; padding-top:2px; background-image: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#494949), to(#A9A9A9)); border:1px solid #000; box-shadow: 3px 3px 3px #000;overflow:hidden">
    <!--Header-->
    <div align="left" style="height:20px; width:100%; padding:5px; padding-left:0" class="header">ION Account Settings</div>
    <!--Body-->
    <div id="account_settings_content" align="left" style=" background-color:#F4F4F6; width:590px; height:500px; padding:10px; padding-left:30px; padding-right:20px; border-left:1px solid #494949; border-right:1px solid #494949; border-bottom:1px solid #B9B9B9;">
      <p class="heading">ION Account Information</p>
      <p>You have established an ION Account with the following settings:</p>
      <table width="100%" cellspacing="0" cellpadding="5px" border="0" class="table-text">
        <tbody><tr>
          <td width="120px">Name:</td>
          <td><input id="account_name" class="colorbox_textinput" type="text"/></td>
        </tr>
        <tr>
          <td>Institution:</td>
          <td><input id="account_institution" class="colorbox_textinput" type="text"/></td>
        </tr>
        <tr>
          <td>Email:</td>
          <td><input id="account_email" class="colorbox_textinput" type="text"/></td>
        </tr>
        <tr>
          <td>Identity provider:</td>
          <td id="authenticating_organization"></td>
        </tr>
      </tbody></table>
      <p class="heading">Optional Notification Settings</p>
      <table width="100%" cellspacing="0" cellpadding="5px" border="0" class="table-text">
        <tbody><tr>
          <td width="120px">Mobile Phone:</td>
          <td><input id="account_mobilephone" class="colorbox_textinput" type="text"/></td>
        </tr>
        <tr>
          <td>Twitter:</td>
          <td><input id="account_twitter" class="colorbox_textinput" type="text"/></td>
        </tr>
      </tbody></table>
      <p class="heading">Optional Email Updates</p>
      <p>Send email when there is:</p>
      <table width="100%" cellspacing="0" cellpadding="3px" border="0" style="margin-left:132px; margin-bottom:10px" class="table-text">
        <tbody><tr>
          <td width="10px"><input id="system_change" type="checkbox" class="colorbox_checkbox"/></td>
          <td>OOI System change Information</td>
        </tr>
        <tr>
          <td><input id="project_update" type="checkbox" class="colorbox_checkbox"/></td>
          <td>OOI Project Update</td>
        </tr>
        <tr>
          <td><input id="ocean_leadership_news", type="checkbox" class="colorbox_checkbox"/></td>
          <td>Ocean Leadership News</td>
        </tr>
        <tr>
          <td><input id="ooi_participate" type="checkbox" class="colorbox_checkbox"/></td>
          <td>Opportunity to participate in the OOI User Experience Improvement Program</td>
        </tr>
      </tbody></table>
      <p style="padding:0px;margin-bottom:10px;position:relative;top:-12px">You can change your optional selections at any time by <br>clicking Account Settings in the top right of the ION window.</p>
      </div>
    <div id="account_settings_bottom" align="left" style=" background-color:#FFF; width:590px; height:30px; padding:5px; padding-left:30px; padding-right:20px; border-left:1px solid #494949; border-right:1px solid #494949; border-bottom:1px solid #494949">
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
        <tbody><tr>
          <td>&nbsp;</td>
          <td width="100px"><a href="#" class="modal_close colorbox_button blue-button-inactive">Cancel</a></td>
          <td width="10px">&nbsp;</td>
          <td width="100px"><a id="account_settings_done" href="#" class="colorbox_button blue-button-inactive-focus">Done</a></td>
        </tr>
      </tbody></table>
    </div>
   </div><!-- end #registration_complete_dialog -->

<div id="validate-resource-dialog" class="form-dialog" style="width:640px; height:400px; padding:10px; padding-top:2px; background-image: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#494949), to(#A9A9A9)); border:1px solid #000; box-shadow: 3px 3px 3px #000">
    <!--Header-->
    <div align="left" style="height:20px; width:100%; padding:5px; padding-left:0" class="header">Register Data Resource</div>
    <!--Body-->
    <div align="left" style=" background-color:#F4F4F6; width:590px; height:310px; padding:10px; padding-left:30px; padding-right:20px; border:1px solid #494949;border-bottom:none">

        <div class="loading"><p>Validating the dataset, this can take several minutes...</p></div>

        <div class="field clearfix result"><label>Result:</label><input type="text" class="value" readonly /></div>
        <div class="field clearfix error-msg"><label>Error Message:</label><pre class="value"></pre></div>
        <div class="field clearfix cf-error"><label>CF Error Count:</label><input type="text" class="value" readonly /></div>
        <div class="field clearfix cf-warning"><label>CF Warning Count:</label><input type="text" class="value" readonly /></div>
        <div class="field clearfix cf-info"><label>CF Info Count:</label><input type="text" class="value" readonly /></div>
        <div class="field clearfix cf-output"><label>CF Output:</label><pre class="value"></pre></div>
        <div class="field clearfix cdm-output"><label>CDM Output:</label><pre class="value"></pre></div>
    </div>

    <div align="left" style=" background-color:#FFF; width:590px; height:30px; padding:5px; padding-left:30px; padding-right:20px; border:1px solid #494949;">
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
        <tbody><tr>
          <td>&nbsp;</td>
          <td width="100px"><a href="#" class="modal_close colorbox_button blue-button-inactive">Cancel</a></td>
          <td width="10px">&nbsp;</td>
          <td width="100px"><a href="#" class="validate-ok colorbox_button blue-button-inactive">OK</a></td>
        </tr>
      </tbody></table></div>
</div>

<div id="register-resource-complete" style="width:640px; height:400px; padding:10px; padding-top:2px; background-image: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#494949), to(#A9A9A9)); border:1px solid #000; box-shadow: 3px 3px 3px #000">
    <!--Header-->
    <div align="left" style="height:20px; width:100%; padding:5px; padding-left:0" class="header">Data Resource Registration Successful</div>
    <!--Body-->
    <div align="left" style=" background-color:#F4F4F6; width:590px; height:310px; padding:10px; padding-left:30px; padding-right:20px; border:1px solid #494949;border-bottom:none">

        <p>Registration was successful. Ingestion has begun, but it can take some time depending on the size of the dataset. Do not be alarmed if you do not see the data right away.</p>
    </div>

    <div align="left" style=" background-color:#FFF; width:590px; height:30px; padding:5px; padding-left:30px; padding-right:20px; border:1px solid #494949;">
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
        <tbody><tr>
          <td>&nbsp;</td>
          <td width="100px">&nbsp;</td>
          <td width="10px">&nbsp;</td>
          <td width="100px"><a href="#" class="modal_close colorbox_button blue-button-inactive">OK</a></td>
        </tr>
      </tbody></table></div>
</div>


<div id="register-instrument-dialog" class="form-dialog" style="width:640px; height:340px; padding:10px; padding-top:2px; background-image: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#494949), to(#A9A9A9)); border:1px solid #000; box-shadow: 3px 3px 3px #000">
    <!--Header-->
    <div align="left" style="height:20px; width:100%; padding:5px; padding-left:0" class="header">Register an Instrument</div>
    <!--Body-->
    <div align="left" style=" background-color:#F4F4F6; width:590px; height:250px; padding:10px; padding-left:30px; padding-right:20px; border:1px solid #494949;border-bottom:none">

      <form id="register-instrument-form" name="register-instrument-form" action="#">
        <!--
        <div class="field clearfix"><label>Name:</label><input type="text" class="value" id="instrument_name" value="SeaBird SBE37" /></div>
        <div class="field clearfix"><label>Description:</label><input type="text" class="value" id="instrument_description" value="SeaBird Sensor" /></div>
        <div class="field clearfix"><label>Manufacturer:</label><select id="instrument_manufacturer"><option value="SeaBird Electronics">SeaBird Electronics</option></select></div>
        <div class="field clearfix"><label>Model:</label><select id="instrument_model"><option value="SBE37">SBE37</option></select></div>
        <div class="field clearfix"><label>Serial Number:</label><input type="text" class="value" id="instrument_serial_num" value="123ABC" /></div>
        <div class="field clearfix"><label>Firmware Version:</label><input type="text" class="value" id="instrument_fw_version" value="1.0" /></div>
        -->
        <div class="field clearfix"><label>Name:</label><input type="text" class="value" id="instrument_name" value="" /></div>
        <div class="field clearfix"><label>Description:</label><input type="text" class="value" id="instrument_description" value="" /></div>
        <div class="field clearfix"><label>Manufacturer:</label><select id="instrument_manufacturer"><option value="SeaBird Electronics">SeaBird Electronics</option></select></div>
        <div class="field clearfix"><label>Model:</label><select id="instrument_model"><option value="SBE37">SBE37</option></select></div>
        <div class="field clearfix"><label>Serial Number:</label><input type="text" class="value" id="instrument_serial_num" value="" /></div>
        <div class="field clearfix"><label>Firmware Version:</label><input type="text" class="value" id="instrument_fw_version" value="" /></div>
      </form>
    </div>

    <div align="left" style=" background-color:#FFF; width:590px; height:30px; padding:5px; padding-left:30px; padding-right:20px; border:1px solid #494949;">
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
        <tbody><tr>
          <td>&nbsp;</td>
          <td width="100px"><a href="#" class="modal_close colorbox_button blue-button-inactive">Cancel</a></td>
          <td width="10px">&nbsp;</td>
          <td width="100px"><a href="#" class="register_instrument_ok colorbox_button blue-button-inactive-focus">Register</a></td>
        </tr>
      </tbody></table></div>
 </div><!-- end #register-instrument-dialog -->

<div id="certificate-timeout" style="width:530px; height:190px; padding:10px; padding-top:2px; background-image: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#494949), to(#A9A9A9)); border:1px solid #000; box-shadow: 3px 3px 3px #000">
    <!--Header-->
    <div align="left" style="height:20px; width:100%; padding:5px; padding-left:0" class="header">Session Expired</div>
    <!--Body-->
    <div align="left" style=" background-color:#F4F4F6; width:480px; height:100px; padding:10px; padding-left:30px; padding-right:20px; border:1px solid #494949;border-bottom:none">

        <p>Your session has expired.  You may choose to continue working as a guest or re-login.</p>
    </div>

    <div align="left" style=" background-color:#FFF; width:480px; height:30px; padding:5px; padding-left:30px; padding-right:20px; border:1px solid #494949;">
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
        <tbody><tr>
          <td>&nbsp;</td>
          <td width="100px"><a href="#" class="modal_close guest colorbox_button blue-button-inactive">Guest</a></td>
          <td width="10px">&nbsp;</td>
          <td width="100px"><a href="#" class="modal_close login colorbox_button blue-button-inactive">Login</a></td>
        </tr>
      </tbody></table></div>
</div>


</div><!-- end #modal_dialogs -->

<div id="templates" style="display: none;">
	<div id="template-register-resource">
		Title: <input id='resource_registration_title' value='' name='resource_registration_title' title='<%= HELP.P1064_SP257 %>' type='text' size='30' maxlength='50'/>
     	<br/><br/><span style='position:relative;top:-32px'>Description:</span><textarea style='width:167px' id='resource_registration_description' title='<%= HELP.P1064_SP258 %>'></textarea>
		<br/><br/><span style='position:relative;top:-32px'>Visualization URL:</span><textarea style='width:167px' id='resource_registration_visualization_url' title='<%= HELP.P1064_SP256 %>'></textarea>
	</div>
</div>

</body>

</html>
