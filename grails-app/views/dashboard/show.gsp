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
        window.INSTRUMENT_MONITOR_URL = '<%= INSTRUMENT_MONITOR_URL %>';
    } catch(err){ //For development:
        if (document.location.hostname === 'localhost'){
            window.OOI_ROLES = ['USER', 'ADMIN', 'DATA_PROVIDER', 'MARINE_OPERATOR'];
            window.REGISTERED = true;
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
<div id="loading_message"><img src="images/I1131-OOI-Wave-Animated.png"><span class="msg">Loading...</span></div>

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
      <a id="login_link" href="login">Sign In&nbsp;&nbsp;&nbsp;</a>
      <a id="logout_link" href="logout">Sign Out&nbsp;&nbsp;&nbsp;</a>
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
       <thead><tr><th width="50%">Title</th><th>notificationSet</th><th>Provider</th><th>Type</th><th>Date Registered</th><th>Details</th> </tr></thead>
        <tbody></tbody>
      </table>

       <table id="datatable_104" class="datatable" cellpadding="0" cellspacing="0" border="0">
          <thead><tr><th>&nbsp;</th><th>Resource Title</th><th>Source</th><th>Notification Initiated</th><th>Details</th> </tr></thead>
          <tbody></tbody>
       </table>

       <table id="datatable_106" class="datatable" cellpadding="0" cellspacing="0" border="0">
            <thead><tr><th>&nbsp;</th><th>Active</th><th>Avail.</th><th>My Registration Title</th><th>Original Source Title</th><th>Publication Date</th><th>Details</th></tr></thead>
            <tbody></tbody>
       </table>

       <table id="datatable_109_epus" class="datatable" cellpadding="0" cellspacing="0" border="0">
          <thead><tr><th>EPU Controller ID</th><th>Details</th></tr></thead>
          <tbody></tbody>
       </table>

       <table id="datatable_109_users" class="datatable" cellpadding="0" cellspacing="0" border="0">
          <thead><tr><th>OOI ID</th><th>Name</th><th>Email</th><th>Institution</th><th>Subject</th><th>Details</th></tr></thead>
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
      <span class="select_button" id="delete_selected">Delete Selected</span>
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
                <td style="padding-right: 30px;"><label for="radioAllPubRes">All Registered Resources</label></td>
                <td><input id="radioMyPubRes" title="<%= HELP.P1000_SP9 %>" class="resource_selector controlradios" name="group1" type="radio"/></td>
                <td><label for="radioMyPubRes">My Registered Resources</label></td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td><input id="radioMySub" title="<%= HELP.P1000_SP10 %>" class="resource_selector controlradios" name="group1" type="radio"/></td>
                <td><label for="radioMySub">My Notification Settings</label></td>
              </tr>
            </table>
          </form>
        </div>

        <div id="register_new">
            <table>
              <tr><td>Source URL:&nbsp;<input id="data_resource_url" size='27' type="text"/></td></tr>
              <!-- <tr><td>THREDDS Catalog:&nbsp;<input size='12' type="text"/><button disabled="disabled" style="float:right;margin-left:2px">Look Up</button></td></tr> -->
              <tr><td>Visualization URL:&nbsp;<input size='22' type="text"/></td></tr>
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
              <td><input id="radioDatasets" class="admin_selector controlradios" name="group1" type="radio"/></td>
              <td><label for="radioDatasets">Data Sets</label></td>
              <td><input id="radioDatasources" class="admin_selector controlradios" name="group1" type="radio"/></td>
              <td><label for="radioDatasources">Data Sources</label></td>
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
                <td><a href="#instrument/list"><input id="radioViewInstruments" class="resource_selector controlradios" name="group1" type="radio"/></a></td>
                <td style="padding-right: 30px;"><a href="#instrument/list"><label for="radioViewInstruments">View All Instruments</label></a></td>
                <td><a href="#instrument/new"><input id="radioNewInstrument" class="resource_selector controlradios" name="group1" type="radio"/></a></td>
                <td style="padding-right: 30px;"><a href="#instrument/new"><label for="radioNewInstrument">Register New Instrument</label></a></td>
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
                <td><input id="radioBoundingAll" title="<%= HELP.P1001_SP19 %>" class="all bounding" name="group1" type="radio"/>All</td>
                <td><input id="radioBoundingDefined" title="<%= HELP.P1001_SP20 %>" class="defined bounding" name="group1" type="radio"/>Defined</td>
              </tr>
            </table>
          </form>
        </div>
        <div class="altitudeRadios">
          <form action="">
            <table>
              <tr>
                <td>&nbsp;</td>
                <td><strong style="position:relative;left:-30px">Vertical Extent</strong><span id="vertical_extent_units_toggle">m</span></td>
              </tr>
              <tr>
                <td><input id="radioAltitudeAll" title="<%= HELP.P1001_SP21 %>" class="all altitude" name="group1" type="radio"/>All</td>
                <td><input id="radioAltitudeDefined" title="<%= HELP.P1001_SP22 %>" class="defined altitude" name="group1" type="radio"/>Defined</td>
              </tr>
            </table>
          </form>
        </div>
        <div class="boundingBoxControls">
          <span class="bb_direction Ntext">N</span>
          <input id="ge_bb_north" title="<%= HELP.P1001_SP23 %>" class="north textfield" name="north" type="text" size="5" maxlength="5"/>
          <span class="bb_direction Stext">S</span>
          <input id="ge_bb_south" title="<%= HELP.P1001_SP23 %>" class="south textfield" name="south" type="text" size="5" maxlength="5"/>
          <span class="bb_direction Etext">E</span>
          <input id="ge_bb_east" title="<%= HELP.P1001_SP23 %>" class="east textfield" name="east" type="text" size="5" maxlength="5"/>
          <span class="bb_direction Wtext">W</span>
          <input id="ge_bb_west" title="<%= HELP.P1001_SP23 %>" class="west textfield" name="west" type="text" size="5" maxlength="5"/>
          <span class="NSEWBackgroundBorder"></span>
        </div>
        <div class="altitudeControls">
            <span class="altitudeUpper"><div>Upper Bound</div>
            <input  id="ge_altitude_ub" title="<%= HELP.P1001_SP25 %>" class="textfield" name="altUpper" type="text" size="10" maxlength="10"/>
            <img  id="vertical_extent_above" class="vertical_extent_button" src="images/Above-Sea-Level-Simple.png">
            </span>
            <span class="altitudeLower"><div>Lower Bound</div>
            <input id="ge_altitude_lb" title="<%= HELP.P1001_SP24 %>" class="textfield" name="altLower" type="text" size="10" maxlength="10"/>
            <img id="vertical_extent_below" class="vertical_extent_button" src="images/Below-Sea-Level-Simple.png">
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
              <td><label for="TE_timeRange_all">All</label></td>
              <td><input id="TE_timeRange_defined" title="<%= HELP.P1003_SP36 %>" class="defined" name="group1" type="radio"/></td>
              <td><label for="TE_timeRange_defined">Defined</label></td>
            </tr>
          </table>
        </form>
      <div class="temporalExtentControls">
        <span class="te-from boldText">From:</span>
        <input id="te_from_input" title="<%= HELP.P1003_SP37 %>" name="te_from_input" type="text" size="20" maxlength="20"/>
        <br><span class="te-to boldText">To:</span>
        <input id="te_to_input" name="te_to_input" type="text" size="20" maxlength="20"/>
        <div style="color:#aaa" class="te-footer-text">ISO Formatted Time in UTC</div>
      </div><!-- end .temporalExtentControls -->
      </div>

    </div>
   </div><!-- end .west-center -->
   <div id="west_south" class="west-south">
      <!--<button id="geospatial_selection_button" disabled="disabled">Geospatial Selection Query</button>-->
      <button id="register_resource_button">Validate Resource</button>
   </div><!-- end .west-south -->

 </div> <!-- end .ui-layout-west -->

  <div id="east_sidebar" class="ui-layout-east hidden">
   <div class="east-center">
    <div id="eastMultiOpenAccordion">
      <h3 class="data_sources "><a id="rp_dsTitle" href="#">Resource Registration Description</a></h3>
      <div class="data_sources registered_resource_editable">
        <div id="ds_title"></div><br>
      </div>

      <h3 class="data_sources"><a href="#">Resources Registration Contact Information</a></h3>
      <div class="data_sources registered_registration_contact_editable">
        <div id="ds_publisher_contact"></div><br>
      </div>

      <h3 class="data_sources my_resources_sidebar"><a href="#">Resources Availability Settings</a></h3>
      <div class="data_sources registered_registration_availability_editable my_resources_sidebar">
        <div><input id="availability_radio_private" name="availability_radio" type="radio"/><label for="availability_radio_private">Resource is private and available to me only</label></div>
        <div><input id="availability_radio_public" name="availability_radio" type="radio"/><label for="availability_radio_public">Resource is publically available</label></div>
      </div>

      <h3 class="data_sources my_resources_sidebar"><a href="#">Resources Polling Settings</a></h3>
      <div class="data_sources my_resources_sidebar" id="registered_registration_activation_polling_editable">
        <div><strong>Polling Interval</strong></div>
        <div><input id="polling_radio_yes" name="polling_radio" type="radio"/><label for="polling_radio_yes">Poll resource every: </label><input id="polling_time" name="" type="text" size="8" maxlength="8"/> DD:HH:MM</div>
        <div><input id="polling_radio_no" name="polling_radio" type="radio"/><label for="polling_radio_no">Do not poll the resource</label></div>
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
      <div class="notification_settings" style="padding-left: 10px; padding-right: 10px;">
        <form action="">
          <p id="notification_details"></p>
          <table>
            <thead>
            <tr><th width="50%">Send notifications when:</th></tr>
            </thead>
            <tr>
              <td><input id="updateWhenAvailable" class="_controlradios notifications_dispatcher" name="group1" type="checkbox"/></td>
              <td style="padding-right: 30px;"><label for="updateWhenAvailable">Update When Available</label></td>
            </tr>
            <tr>
              <td><input id="datasourceIsOffline" class="_controlradios notifications_dispatcher" name="group1" type="checkbox"/></td>
              <td><label for="datasourceIsOffline">Datasource is offline</label></td>
            </tr>
          </table>
        </form>
      </div><!-- end #notification_settings -->

    <h3 id="dispatcher_settings" class="dispatcher_settings early_adopter"><a href="#">Dispatcher Settings</a></h3>
      <div class="dispatcher_settings early_adopter" style="padding-left: 10px; padding-right: 10px;">
        <form action="">
          <p  style="font-weight:bold;border-bottom:1px solid #555" class="dispatcher_details">Notification Triggers</p>
          <table>
            <thead>
            <tr>
              <th width="50%">Send notifications when:</th>
            </tr>
            </thead>
            <tr>
              <td><input id="dispatcher_updateWhenAvailable" class="_controlradios notifications_dispatcher" name="group1" type="checkbox"/></td>
              <td style="padding-right: 30px;"><label for="dispatcher_updateWhenAvailable">Update When Available</label></td>
            </tr>
            <tr>
              <td><input id="dispatcher_datasourceIsOffline" class="_controlradios notifications_dispatcher" name="group1" type="checkbox"/></td>
              <td><label for="dispatcher_datasourceIsOffline">Datasource is offline</label></td>
            </tr>
          </table>
          <p style="font-weight:bold;border-bottom:1px solid #555" class="dispatcher_details">Dispatcher Script</p>
          Dispatcher Script Path: <input id="dispatcher_script_path" type="text"/>
        </form>
      </div><!-- end #dispatcher_settings -->

      <h3 class="instrument_agent"><a href="#">Instrument Agent Details</a></h3>
      <div class="instrument_agent" id="instrument_agent_details">&nbsp;</div>

    </div><!-- end #eastMultiOpenAccordion -->
   </div><!-- end .east-center -->

   <div class="east-south">
      <button id="download_dataset_button" disabled="disabled">Download</button>
      <button id="setup_notifications" disabled="disabled">Setup Notifications</button>
      <button id="start_notifications">Start Notifications</button>
      <button id="save_myresources_changes" disabled="disabled">Save Changes</button>
      <button id="save_notifications_changes" disabled="disabled">Save Changes</button>
      <button id="save_register_resource" style="display: none;">Register Resource</button>
      <!--
      <button id="agent_start" class="agent_button" style="display: none;">Start Agent</button>
      <button id="agent_stop" class="agent_button" style="display: none;">Stop Agent</button>
      -->
      <button id="agent_sampling_start" class="agent_button" style="display: none;">Start Sampling</button>
      <button id="agent_sampling_stop" class="agent_button" style="display: none;">Stop Sampling</button>
      <button id="agent_sample_monitor" class="agent_button" style="display: none;">Sample Monitor</button>
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


</div><!-- end #modal_dialogs -->


</body>

</html>
