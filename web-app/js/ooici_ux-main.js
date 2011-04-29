/*
Workflow implementations for OOI UX.
(this file may be split up as it grows larger)
*/

var OOIUX = Backbone.View.extend({

    initialize: function() {
        this.layout = this.layout_main_init();
        this.layout_center_inner = this.layout_center_inner_init();
        this.layout_west_inner = this.layout_west_inner_init();
        this.layout_east_inner = this.layout_east_inner_init();
        this.datatable_100 = this.datatable_init("#datatable_100", 5);
        this.datatable_104 = this.datatable_init("#datatable_104", 5);
        this.datatable_106 = this.datatable_init("#datatable_106", 6);
        this.wf_100(this.datatable_100);
        this.wf_101(this.datatable_100);
        this.wf_104(this.datatable_104);
        this.wf_105();
        this.wf_106(this.datatable_106);
        this.dataset_return();
        this.dataset_scroll();
        this.geospatial_container();
        this.datatable_select_buttons();
        this.setup_notifications();
        this.register_resource();
        this.modal_dialogs();
        $("#radioAllPubRes").trigger("click"); //XXX temporary default
        $("#temporalExtent").siblings().last().trigger("click");  //XXX temporary default
        $("#datatable_104_wrapper").hide();  //XXX temporary default
        $("#datatable_106_wrapper").hide();  //XXX temporary default
    },

    layout_main_init: function(){
        // first set a 'fixed height' on the container so it does not collapse...
        $(this.el).height($(window).height() - $(this.el).offset().top);
        // NOW create the layout
        var layout_main = $(this.el).layout({
            resizerClass: 'ui-state-default',
            north__size: 60,
            west__size: 350,
            east__size: 350,
        });
        return layout_main;
    },
    layout_west_inner_init: function(){ 
        var layout_west_inner = $("div.ui-layout-west").layout({
            minSize: 50,
            center__paneSelector:".west-center",
            south__paneSelector: ".west-south",
        });
        return layout_west_inner;
    },

    layout_center_inner_init: function(){
        var layout_center_inner = $("div.ui-layout-center").layout({
            minSize: 50,
            center__paneSelector:".center-center",
            south__paneSelector: ".center-south",
        });
        return layout_center_inner;
    },

    layout_east_inner_init: function(){ 
        var layout_east_inner = $("div.ui-layout-east").layout({
            minSize: 50,
            center__paneSelector:".east-center",
            south__paneSelector: ".east-south",
        });
        return layout_east_inner;
    },

    datatable_init: function(id, columns){
        var oTable = $(id).dataTable({
            "aaData":[_.map(_.range(columns), function(x){return null;})],
            "bJQueryUI": true, 
            "sPaginationType": "full_numbers"
        });
        return oTable;
    },

    loading_dialog: function(msg){
        var elem = $("#loading_message");
        if (typeof msg == "undefined"){
            elem.fadeOut("slow");
        } else {
            elem.show().find(".msg").text(msg);
        }
    },

    modal_dialogs: function(){
        $("#registration_link").colorbox({inline:true, href:"#registration_dialog", transition:"none", opacity:0.7});
        $("#account_settings_link").colorbox({inline:true, onOpen:this.account_settings, href:"#account_settings", transition:"none", opacity:0.7});
        $(".modal_close").live("click", function(e){$.fn.colorbox.close();e.preventDefault();});
        if (document.location.search.search("action=register") != -1){
          $.fn.colorbox({inline:true, href:"#account_settings", transition:"none", opacity:0.7});
        }

        $("#account_settings_done").click(function(){
            var name = $("#account_name").val(), institution = $("#account_institution").val(), email = $("#account_email").val(), mobilephone = $("#account_mobilephone").val(), twitter = $("#account_twitter").val(), system_change = $("#system_change").is(":checked"), project_update = $("#project_update").is(":checked"), ocean_leadership_news = $("#ocean_leadership_news").is(":checked"), ooi_participate = $("#ooi_participate").is(":checked");
            var data = {"action":"update", "name":name, "institution":institution, "email":email, "mobilephone":mobilephone, "twitter":twitter, "system_change":system_change, "project_update":project_update, "ocean_leadership_news":ocean_leadership_news, "ooi_participate":ooi_participate};
            $("#account_settings_done").text("Saving...");    
            $.ajax({url:"userProfile", type:"POST", data:data,
                success: function(resp){
                    $("#account_settings_done").text("Done");    
                    $(".modal_close").trigger("click");
                }
            });
        });
    },

    account_settings: function(){
        //TODO clear out modal form data
        $("#account_settings_content, #account_settings_bottom").css("opacity", "0");
        $("#account_settings").prepend($("<div>").attr("id", "loading_account_settings").text("Loading Acccount Settings..."));
        $.ajax({url:"userProfile", type:"GET",
            success: function(resp){
                $("#loading_account_settings").remove();
                $("#account_settings_content, #account_settings_bottom").css("opacity", "1");
            }
        });
    },


    datatable_select_buttons: function(){
      var self = this;
      $(".select_button").click(function(){
        var button_id = $(this).attr("id");
        var datatable_id = $(".datatable:visible").attr("id"); 
        switch (button_id) {
          case "delete_selected":
            var num_selected = $("#"+datatable_id+" input:checked").length;
            if (num_selected == 0) return alert("Select items to delete them");
            var answer = confirm("Delete "+num_selected + " selected items?");
            if (answer){ 
                self.loading_dialog("Deleting "+num_selected+" items...");
                var dataset_ids = [111, 222, 333]; //TODO
                return $.ajax({url:"dataResource", type:"POST", data:{"action":"delete", "dataset_ids":dataset_ids}, 
                    success: function(resp){
                        self.loading_dialog();
                        document.location = "/"; //XXX
                    }
                });
            } else {
                return;
            }
          case "deselect_all":
            return $("#"+datatable_id+" input:checkbox").attr("checked", "");
          case "select_all":
            return $("#"+datatable_id+" input:checkbox").attr("checked", "checked");
          default:
            return;
        }
      });
    },

    setup_notifications: function(){
      $("#setup_notifications").bind("click", function(){
        $(".notification_settings, .dispatcher_settings").trigger("click").trigger("click");  //XXX 
        $("#start_notifications, #notification_settings, #dispatcher_settings").show();
        $("#save_notification_settings, #download_dataset_button, #setup_notifications").hide();
        $(".data_sources").hide();
      });

     $("#start_notifications").bind("click", function(){
        //TODO: btn is diabled if by default, and if nothing changed. IMPORTANT: handle no-op (nothing checked at all)
        var data_src_id = "abc123"; //XXX $("#da"); TODO save data
        var subscription_type = -1, email_alerts_filter = -1, dispatcher_alerts_filter = -1;
        if ($("#updateWhenAvailable").is(":checked") && !$("#datasourceIsOffline").is(":checked")) email_alerts_filter = 0;
        if (!$("#updateWhenAvailable").is(":checked") && $("#datasourceIsOffline").is(":checked")) email_alerts_filter = 1;
        if ($("#updateWhenAvailable").is(":checked") && $("#datasourceIsOffline").is(":checked")) email_alerts_filter = 2;

        if ($("#dispatcher_updateWhenAvailable").is(":checked") && !$("#dispatcher_datasourceIsOffline").is(":checked")) dispatcher_alerts_filter = 0;
        if (!$("#dispatcher_updateWhenAvailable").is(":checked") && $("#dispatcher_datasourceIsOffline").is(":checked")) dispatcher_alerts_filter = 1;
        if ($("#dispatcher_updateWhenAvailable").is(":checked") && $("#dispatcher_datasourceIsOffline").is(":checked")) dispatcher_alerts_filter = 2;

        if (email_alerts_filter > -1 && dispatcher_alerts_filter == -1) subscription_type = 0;
        if (email_alerts_filter == -1 && dispatcher_alerts_filter > -1) subscription_type = 1;
        if (email_alerts_filter > -1 && dispatcher_alerts_filter > -1) subscription_type = 2;
        
        //TODO: dont send at all if any val is -1
        var dispatcher_script_path = $("#dispatcher_script_path").val();
        $.ajax({url:"subscription", type:"POST", data:{"action":"create", "data_src_id":data_src_id, "subscription_type":subscription_type,
            "dispatcher_alerts_filter":dispatcher_alerts_filter, "dispatcher_script_path":dispatcher_script_path}, 
            success: function(resp){
                alert("subscription saved");
                setTimeout(function(){document.location="/";}, 100);
            },
            error: function(jqXHR, textStatus, error){
                alert("subscription error");
            }
        });

     });
    },

    populate_table: function(url, datatable){
        datatable.fnClearTable();
        var datatable_id = datatable.attr("id");
        var action = "find";
        if (datatable_id == "datatable_106") action = "findByUser";
        $.ajax({url:url, type:"GET", data:{action:action}, dataType:"json", 
            success: function(data){
                if (url == "dataResource"){
                    if (datatable_id == "datatable_106"){
                        $.each(data.dataResourceSummary, function(i, elem){
                            var cb = "<input type='checkbox'/>";
                            datatable.fnAddData([cb, elem.title, elem.institution, elem.source, "Date Registered", "Details"]);
                            $($("#datatable_106").dataTable().fnGetNodes(i)).attr("id", elem.data_resource_id); //XXX use Backbone for this
                        });
                        $("#datatable_select_buttons").show();
                        $.each($("table#datatable_106 tbody tr"), function(i, e){$(e).find("td:first").css("width", "4% !important")}); //XXX
                        $("table#datatable_106 tbody tr").not(":first").find("td:not(:first)").css("width", "25%"); //XXX
                    } else {
                        $("#datatable_select_buttons").hide();
                        $.each(data.dataResourceSummary, function(i, elem){
                            datatable.fnAddData([elem.title, elem.institution, elem.source, "Date Registered", "Details"]);
                            $($("#datatable_100").dataTable().fnGetNodes(i)).attr("id", elem.data_resource_id); //XXX use Backbone for this
                        });
                        $("table#datatable_100 tbody tr td").css("width", "30%"); //XXX
                    }
                } 
                if (url == "subscription"){
                    var cb = "<input type='checkbox'/>";
                    $.each(data.dataResourceSummary, function(i, elem){
                        datatable.fnAddData([cb, elem.title, elem.institution, elem.created, "Details"]);
                        $($("#datatable_104").dataTable().fnGetNodes(i)).attr("id", elem.data_resource_id); //XXX use Backbone for this
                    });
                    $("#datatable_select_buttons").show();
                    $.each($("table#datatable_104 tbody tr"), function(i, e){$(e).find("td:first").css("width", "4% !important")}); //XXX 
                    $("table#datatable_104 tbody tr").not(":first").find("td:not(:first)").css("width", "25%"); //XXX
                } 
            } //end 'success: function...'.
        });
    },

    geospatial_container: function(){
        /* MOCK OUT of geospatial_container widget */
        self = this;
        var geospatial_container_data = function(){
            var action = "find";
            var minTime = $("#te_from_input").val(), maxTime = $("#te_to_input").val();
            var minLatitude = $("#ge_bb_south").val(), maxLatitude = $("#ge_bb_north").val(), minLongitude = $("#ge_bb_east").val(), maxLongitude = $("#ge_bb_west").val();
            var minVertical = $("#ge_altitude_lb").val(), maxVertical = $("#ge_altitude_ub").val(), posVertical="down"; //XXX
            var data = {};
            data.action = action;
            if (minLatitude) data.minLatitude = minLatitude;
            if (maxLatitude) data.maxLatitude = maxLatitude;
            if (minLongitude) data.minLongitude = minLongitude;
            if (maxLongitude) data.maxLongitude = maxLongitude;
            if (minVertical) data.minVertical= minVertical;
            if (maxVertical) data.maxVertical = maxVertical;
            if (posVertical) data.posVertical = posVertical;
            if (minTime) data.minTime = minTime;
            if (maxTime) data.maxTime = maxTime
            self.loading_dialog("Loading datatable...");
            $.ajax({url:"dataResource", type:"GET", dataType:"json", data:data, 
                success: function(resp){
                    self.datatable_100.fnClearTable();
                    $.each(resp.dataResourceSummary, function(i, elem){
                        self.datatable_100.fnAddData([elem.title, elem.institution, elem.source, "Date Registered", "Details"]);
                    });
                    $("table#datatable_100 tbody tr td").css("width", "30%"); //XXX
                    self.loading_dialog();
                }
            });
        };
        $("#geospatial_selection_button").click(geospatial_container_data);
    },

    register_resource: function(){
      var self = this;
      $("#register_resource_button").click(function(){
          self.loading_dialog("Registering resource...");
          var data = {"action":"create", "source_url":"http://example.edu"};
          $.ajax({url:"dataResource", type:"POST", data:data, 
              success: function(resp){
                  self.loading_dialog();
                  window.location.reload(); //XXX
              }
          });
      });
    },

    datatable_details: function(){
        $(".datatable tbody").bind('dblclick', function(evt) {
            // Get the hidden column data for this row
            var rowData = datatable.fnGetData(evt.target.parentNode);
            alert("Showing Datatable details...");
        });
    },

    wf_100: function(datatable){
        /* WF 100 - Handles user click events within center pane datatable */
        $('#eastMultiOpenAccordion, #westMultiOpenAccordion').multiAccordion();

        // auto open Resource Selector panel
        $('#westMultiOpenAccordion h3:eq(0)').trigger('click');
        // auto open Geospatial Extent panel
        $('#westMultiOpenAccordion h3:eq(1)').trigger('click');
        // auto open Temporal Extent panel
        $('#westMultiOpenAccordion h3:eq(2)').trigger('click');

        $('.ui-layout-center').hide();
        $('.ui-layout-east').hide();

        $("#geospatialContainer .all").attr("checked", "checked");
        $("#temporalExtent .all").attr("checked", "checked");
        $(".temporalExtentControls input").attr("disabled", "disabled");
        $(".boundingBoxControls input").attr("disabled", "disabled");
        $(".altitudeControls input").attr("disabled", "disabled");

        self = this;

        $("#geospatialContainer .defined").click(function(){
          var is_bounding = $(this).hasClass("bounding");
          if (is_bounding){
            $(".boundingBoxControls input").removeAttr("disabled");
          } else {
            $(".altitudeControls input").removeAttr("disabled");
          }
          $("#geospatial_selection_button").removeAttr("disabled");
        });
        $("#geospatialContainer .all").click(function(){
          var is_bounding = $(this).hasClass("bounding");
          if (is_bounding){
            $(".boundingBoxControls input").attr("disabled", "disabled");
          } else {
            $(".altitudeControls input").attr("disabled", "disabled");
          }
          $("#geospatial_selection_button").removeAttr("disabled");
        });

        $(".temporalExtentContainer input[type='radio']").click(function(){
          var is_all = $(this).hasClass("all");
          if (is_all){
            $(".temporalExtentControls input").attr("disabled", "disabled");
          } else {
            $(".temporalExtentControls input").removeAttr("disabled");
          }
        });


        $('#radioAllPubRes').bind('click', function(evt) {
            self.wf_100_presentation();
            $("h3.data_sources").show();

            $("table#datatable_100 thead tr:first").find("th:eq(0)").text("Title").end().find("th:eq(1)").text("Provider").end().find("th:eq(2)").text("Type").end().find("th:eq(3)").text("Date Registered");
            self.populate_table("dataResource", datatable);
            $('.ui-layout-center').show();
            $('.ui-layout-east').show();
        });

        $("#datatable_100 tbody").click(function(evt) {
            var td_target = $(evt.target);
            $("#dataset_return_button").addClass("dataset_100");
            self.dataset_details(datatable, td_target, self);

        });
    },

    dataset_details: function(datatable, td_target, self){
        self.loading_dialog("Loading dataset details...");
        var data_resource_id = td_target.parent().attr("id");
        if (td_target.text() == "Details"){
            $("#datatable_details_scroll").show();
            $("#datatable_100_wrapper, #datatable_104_wrapper, #datatable_106_wrapper").hide();
            $.ajax({url:"dataResource", type:"GET", dataType:"json", 
                data:{"action":"detail", "data_resource_id":data_resource_id}, 
                success: function(resp){self.dataset_focus_details(resp, self)}
            });
            //return; //XXX
        }
    
        $.ajax({url:"dataResource", type:"GET", dataType:"json", data:{"action":"detail", "data_resource_id":data_resource_id}, 
            success: function(resp){self.dataset_sidebar(resp, datatable, self)}
        });
    },

    dataset_sidebar: function(resp, datatable, self){
        var data = resp.dataResourceSummary;
        $(datatable.fnSettings().aoData).each(function () {
           $(this.nTr).removeClass('row_selected');
        });
        // Expands right pane panels when row is selected. Also closes panels if already expanded.
        if(!$('#eastMultiOpenAccordion h3').hasClass('ui-state-active ui-corner-top')) $('#eastMultiOpenAccordion h3').trigger('click');
        $("#ds_title").html(data.title);
        $("#ds_publisher_contact").html(data.institution);
        $("#ds_source").html(data.source);
        $("#ds_source_contact").html(data.source);
        $("#ds_variables").html(JSON.stringify(resp.variable));
        $("#ds_geospatial_coverage").html("lat_min:"+data.ion_geospatial_lat_min + ", lat_max:"+data.ion_geospatial_lat_max+", lon_min"+data.ion_geospatial_lon_min+", lon_max:"+data.ion_geospatial_lon_max);
        $("#ds_temporal_coverage").html(data.ion_time_coverage_start + " - "+data.ion_time_coverage_end);
        $("#ds_references").html(data.references);
        $(".data_sources").show();
        $(".notification_settings, .dispatcher_settings").hide();
        $("#download_dataset_button, #setup_notifications").removeAttr("disabled");
        self.loading_dialog();
    },

    dataset_focus_details: function(resp, self){
        var html = "<pre style='font-size:18px'>"+JSON.stringify(resp.dataResourceSummary);
        html += "<br><br>"+JSON.stringify(resp.source);
        html += "<br><br>"+JSON.stringify(resp.variable)+"</pre>";
        $("#datatable_details_container").html(html).show();
        self.loading_dialog();
    },


    wf_100_presentation: function(){
        $("#datatable_100_wrapper").show();
        $("#datatable_104_wrapper").hide();
        $("#datatable_106_wrapper").hide();
        $("#datatable_details_container").hide();
        $("#container h1").text("All Registered Resources");
        $(".notification_settings").hide();
        $("#save_notification_settings").hide(); //button
        $("#geospatial_selection_button").show();
        $("#download_dataset_button, #setup_notifications").show().attr("disabled", "disabled");
    },

    wf_101: function(datatable){
       /* WF101 - Handles double click action on a row w/in the center pane's dataResource table.  */
        $("#download_dataset_button").click(function(){
            document.location = "service/createDownloadUrl";
        });
    },

    wf_104: function(datatable){
        /* WF104 - User subscriptions */
        self = this;
        $(".notification_settings input:checkbox").change(function(){
            $("#save_notification_settings").removeAttr("disabled");
        });
        $("#save_notification_settings").click(function(){
            if ($("#save_notification_settings").attr("disabled") != "") return;
            var settings_checked = [];
            $.each($(".notification_settings input:checked"), function(i, e){
                settings_checked.push($(e).attr("id"));
            });
            self.loading_dialog("Saving notification setting...");
            var data = {"action":"update", "settings":settings_checked};
              $.ajax({url:"subscription", type:"POST", data:data, 
                  success: function(resp){
                      self.loading_dialog();
                      return setTimeout(function(){window.location.reload();}, 800); //XXX
                  }
              });
        });

        $("#radioMySub").bind('click', function(evt) {
            self.wf_104_presentation();
            $("#notification_settings").show();

            $("table#datatable_104 thead tr:first").find("th:eq(0)").text("").end().find("th:eq(1)").text("Resource Title").find("th:eq(2)").text("Source").end().end().find("th:eq(3)").text("Notification Initiated").end().find("th:eq(4)").text("Details");
            self.populate_table("subscription", datatable);
        });
        $("#datatable_104 tbody").unbind("click").bind('click', function(evt){ //TODO: use 'delegate'.
            var nth_elem = $(evt.target).parent().index()+1;
            $("#save_notification_settings").show(); //button
            $("#notification_details").html("Notification Triggers for: <b>#"+nth_elem+"</b> element.");
            if (!$(".notification_settings:nth(1)").is(":visible") ) $(".notification_settings").click();
        });
    },
    wf_104_presentation: function(){
        $("#datatable_104_wrapper").show();
        $("#datatable_100_wrapper").hide();
        $("#datatable_106_wrapper").hide();
        $(".notification_settings").hide();
        $("#datatable_details_container").hide();
        $("#container h1").text("Notification Settings");
        $('#eastMultiOpenAccordion h3:eq(7)').show().trigger('click');
        $(".data_sources").hide();
        $("#geospatial_selection_button").hide();
        $("#download_dataset_button, #setup_notifications").hide();
    },

    wf_105: function(){
        this.resource_selector();
    },

    resource_selector: function(){
        $(".resouce_selector_tab").bind('click', function(evt) {
            var id = $(this).attr("id");
            $("#"+id).addClass("selected");
            if (id == "view_existing_tab"){
                $("#geospatial_selection_button, #view_existing, .view_existing").show();
                $("#register_new, #register_resource_button").hide();
                $("#register_new_tab").removeClass("selected");
            } else {
                $("#register_new, #register_resource_button").show();
                $("#geospatial_selection_button, #view_existing, .view_existing").hide();
                $("#view_existing_tab").removeClass("selected");
            }
        });
    },


    wf_106: function(datatable){
        var self = this;

        $("#radioMyPubRes").bind('click', function(evt) {
            self.wf_106_presentation();
            self.populate_table("dataResource", datatable);
        });

       $("#datatable_106 tbody").click(function(evt) {
            var td_target = $(evt.target);
            self.dataset_details(datatable, td_target, self);
       });

    },

    wf_106_presentation: function(){
        $("#datatable_106_wrapper").show();
        $("#datatable_100_wrapper").hide();
        $("#datatable_104_wrapper").hide();
        $(".notification_settings").hide();
        $("#datatable_details_container").hide();
        $("#container h1").text("My Registered Resources");
        $("#save_notification_settings").hide(); //button
        $("#geospatial_selection_button").hide();
        $(".notification_settings").hide();
        $("#download_dataset_button, #setup_notifications").hide().attr("disabled", "disabled");
    },

    dataset_return: function(){
        /* go back to dataset listing, coming from a specific dataset details view.*/
        $("#dataset_return_button").click(function(){
            $("#datatable_details_container, #datatable_details_scroll").hide();
            if ($(this).hasClass("dataset_100")){ //XXX remove state stored in the DOM 
                $("#datatable_100_wrapper").show();
            } else {
                $("#datatable_106_wrapper").show();
            }
        });
    },

    dataset_scroll: function(){
        /* scroll through dataset details */
        $(".dataset_scroll").click(function(){
            var scroll_id = $(this).attr("id");
            if (scroll_id == "dataset_scroll_left"){ //XXX remove state stored in the DOM 
                var count = 1;
                alert("scroll left");
            } else {
                alert("scroll right");
                var count = 2;
            }
        });
    }

});
