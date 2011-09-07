OOI.Controllers.Dashboard = Backbone.Controller.extend({

    routes: {
      "":"all_registered_resources",
      "/:nth_dataset":"all_registered_resources_details",
      "notifications":"my_notification_settings",
      "registered":"my_registered_resources",
      "registered/:nth_dataset":"my_registered_resources_details",
      "running-epus":"running_epus",
      "registered-users":"registered_users",
	  "datasets": "datasets",
	  "datasources": "datasources",
      "register-dataset/:dataset": "register_dataset",
      "instrument/list": "instrument_list",
      "instrument/new": "instrument_new"
    },

    initialize: function(options) {
        _.bindAll(this, "all_registered_resources", "all_registered_resources_details", "my_notification_settings", "my_registered_resources",
				"running_epus", "registered_users", "datasets", "datasources", "register_dataset", "instrument_list", "instrument_new");

        this.layout = new OOI.Views.Layout({"el":"#layoutContainer", controller:this}); 

        _.templateSettings = {interpolate : /\{\{(.+?)\}\}/g}; //underscore.js templates

        this.resource_collection = new OOI.Collections.Resources();
        this.my_resources_collection = new OOI.Collections.MyResources();
        this.my_notifications_collection = new OOI.Collections.MyNotifications();

        this.workflow100 = new OOI.Views.Workflow100({el:"#datatable_100", controller:this}); 
        this.workflow104 = new OOI.Views.Workflow104({el:"#delegate-me-01", controller:this});
        this.workflow105 = new OOI.Views.Workflow105({el:"#delegate-me-02", controller:this});
        this.workflow106 = new OOI.Views.Workflow106({el:"#datatable_106", controller:this}); 
		this.workflow109epus = new OOI.Views.Workflow109EPUs({el:"#datatable_109_epus", controller:this});
		this.workflow109users = new OOI.Views.Workflow109Users({el:"#datatable_109_users", controller:this});
		this.workflow109datasets = new OOI.Views.Workflow109Datasets({el:"#datatable_109_datasets", controller:this});
		this.workflow109datasources = new OOI.Views.Workflow109Datasources({el:"#datatable_109_datasources", controller:this});

        //this.notifications = new OOI.Views.Notifications({el:"#east_sidebar", controller:this});
        this.account_settings = new OOI.Views.AccountSettings({el:"#account_settings", controller:this});
        this.resource_selector = new OOI.Views.ResourceSelector({el:"#view_existing", controller:this}); 
        this.admin_selector = new OOI.Views.AdminSelector({el:"#view_admin_tools", controller:this}); 
        this.resource_details_scroll = new OOI.Views.ResourceDetailsScroll({el:"#datatable_details_scroll", controller:this}); 
        this.geospatial_container = new OOI.Views.GeospatialContainer({"el":"#geospatialContainer", controller:this}); 
        this.resource_actions = new OOI.Views.ResourceActions({"el":".east-south", controller:this});
		this.instruments = new OOI.Views.InstrumentList({"el":"#datatable_instruments", controller:this});
		this.sessionmgr = new OOI.Views.SessionMgr({controller:this});

        this.datatable_select_buttons();
        this.datetime_selectors();
        this.notifications_dispatcher_check_init();
        this.dispatcher_script_path_check_init();
        this.add_dynamic_tooltips();
        
        //TODO: the below should go in a self contained view:

        if (OOI_ROLES.length === 0) {
            $("#radioMyPubRes, #radioMySub").prop("disabled", true);
            $(".non_guest").css("color", "#AAAAAA");
            $("#setup_notifications").hide();//TODO: put this in
        }

        if (!REGISTERED && OOI_ROLES.length > 0) {
        	$('#account_settings_link').click();
        }

        var is_user = _.any(OOI_ROLES, function(role){return role === "USER"});
        if (is_user) {
            $("#login_link, #registration_link").hide();
        } else {
            $("#logout_link, #account_settings_link").hide();
        }

        var is_data_provider = _.any(OOI_ROLES, function(role){return role === "DATA_PROVIDER"});
        if (!is_data_provider){
            $("#resource_selector_view_spacer, #register_new_tab").hide();
        }

        var is_admin = _.any(OOI_ROLES, function(role){return role === "ADMIN"});
        if (!is_admin){
            $(".admin_role").hide();
        }

		var is_marine_op = _.any(OOI_ROLES, function(role){return role === "MARINE_OPERATOR"});
        if (!is_marine_op){
            $(".marine_op_role").hide();
        }

        //XXX this.error_dialog("/dataResources", 400, "Resource id is invalid");
        //XXX this.error_dialog("/dataResources", 500);
    },

    all_registered_resources: function(){
        //$("#radioAllPubRes").prop("checked", true);
        this.reset_sidebar();
        this.workflow100.render();
    },

    all_registered_resources_details: function(nth_dataset){
        if (this.resource_collection.models.length === 0){
            var self = this;
            $.when(this.all_registered_resources()).then(setTimeout(function(){
                $("#datatable_details_scroll, #datatable_details_container").show();
                $("#datatable_select_buttons, .dataTables_wrapper").hide();
                self._all_registered_resources_details(nth_dataset)
            }, 2000));
        } else {
            this._all_registered_resources_details(nth_dataset);
        }
    },

    _all_registered_resources_details: function(nth_dataset){
        var data_resource_id = $("#datatable_100 tbody tr:nth("+nth_dataset+")").attr("id");
        this.workflow100.show_detail(data_resource_id);
    },

    my_notification_settings: function(){
        //$("#radioMySub").prop("checked", true);
        this.reset_sidebar();
        this.workflow104.render();
    },

    my_registered_resources: function(){
        //$("#radioMyPubRes").prop("checked", true);
        this.reset_sidebar();
        this.workflow106.render();
    },

    my_registered_resources_details: function(nth_dataset){
        if (this.my_resources_collection.models.length === 0){
            var self = this;
            $.when(this.my_registered_resources()).then(setTimeout(function(){
                self._my_registered_resources_details(nth_dataset)
                $("#datatable_details_scroll, #datatable_details_container").show();
                $("#datatable_select_buttons, .dataTables_wrapper").hide();
            }, 2000));
        } else {
            this._my_registered_resources_details(nth_dataset);
        }
    },

    _my_registered_resources_details: function(nth_dataset){
        var data_resource_id = $("#datatable_106 tbody tr:nth("+nth_dataset+")").attr("id");
        this.workflow106.show_detail(data_resource_id);
    },

    running_epus: function(){
        this.workflow109epus.render();
    },
	registered_users: function(){
        this.workflow109users.render();
    },
	datasets: function(){
        this.workflow109datasets.render();
    },
	datasources: function(){
        this.workflow109datasources.render();
    },
	register_dataset: function(dataset) {
		// TODO: support deeplinking for register_dataset
		dataset = decodeURIComponent(dataset);
	},

	instrument_list: function() {
		this.instruments.render();
	},
	instrument_new: function() {
		this.instruments.register();
	},

    datatable_init: function(id, columns){
        switch (id){
            case "#datatable_100":
                var aoColumns = [{sWidth:'15%'}, {sWidth:'20%'}, {sWidth:'15%'}, {sWidth:'10%'}, {sWidth:'20%'}, {sWidth:'15%'}];
                break;
            case "#datatable_104":
                var aoColumns = [{sWidth:'6%'}, {sWidth:'32%'}, {sWidth:'32%'}, {sWidth:'30%'}];
                break;
            case "#datatable_106":
                var aoColumns = [{sWidth:'5%'}, {sWidth:'10%'}, {sWidth:'8%'}, {sWidth:'27%'}, {sWidth:'25%'}, {sWidth:'15%'}, {sWidth:'10%'}];
                break;
            default: break;
        }
        var oTable = $(id).dataTable({
            "iDisplayLength":20,
            "aLengthMenu": [[10, 20, 25, 50, -1], [10, 20, 25, 50, "All"]],
            "aaData":[_.map(_.range(columns), function(x){return null;})],
            "bJQueryUI": true, 
            "bAutoWidth":true,
            "sPaginationType": "full_numbers",
            "aoColumns": aoColumns,
            "oLanguage":{"sSearch":"Filter:"}
        });
        return oTable;
    },

    reset_sidebar: function(){
        $("#eastMultiOpenAccordion h3").addClass("accordion-inactive");
    },

    datatable_resizer: function(dobind){
        var _resizer = function(){
            var datatable_id = "";
            $.each($(".datatable").filter(":visible"), function(i, e){
                if ($(e).attr("id") != "") datatable_id = $(e).attr("id");
            });
            $("#"+datatable_id).dataTable().fnAdjustColumnSizing();;  
        };
        if (dobind){
            $(window).bind('resize',function(){_resizer();});
        } else {
            _resizer();
        }
    },

    loading_dialog: function(msg){
        var elem = $("#loading_message");
        if (typeof msg == "undefined"){
            elem.fadeOut("slow");
        } else {
            elem.show().find(".msg").text(msg);
        }
    },
    
    error_dialog: function(path, error_type, error_msg){
        this.loading_dialog();
        var contact_msg = "Contact helpdesk@oceanobservatories.org";
        if ((error_type+"").slice(0,2) === "40"){ //Trigger on all 40x errors
            var msg = error_msg + " '"+path+"'";
        }
        if (error_type === 500){
			msg = "Your request failed due to an internal error.\nPlease try again.\nIf it continues to fail, please contact support at:\nhelpdesk@oceanobservatories.org";
            msg += "\n\n\nThe error occurred while accessing: '"+path+"'";
        } else {
			msg += "\n\n"+contact_msg;
		}
        setTimeout(function(){alert(msg)}, 100); //setTimeout needed for loading_dialog to correctly be closed.
    },

    add_dynamic_tooltips: function(){
        $(".dataTables_filter input").attr("title", "Display only rows that contain text matching filter content")
    },

    datetime_selectors:function(){
        $("#te_from_input, #te_to_input").datetimepicker({showSecond:true, dateFormat:'yy-mm-dd', timeFormat:'hh-mm-ss', separator:'-'});

        var date_with_utc_offset = function(current_date){
            var utcoffset_mins = (new Date).getTimezoneOffset();
            var val_parts = current_date.split("-");
            var year=val_parts[0], month=val_parts[1], day=val_parts[2], hours=val_parts[3], mins=val_parts[4], secs=val_parts[5];
            var current_date = new Date(year, month, day, hours, mins, secs);
            var new_date = new Date(current_date.setMinutes(current_date.getMinutes() + utcoffset_mins));
            var pretty_month = new_date.getMonth()+"";
            if (pretty_month.length == 1) pretty_month = "0"+pretty_month;
            var pretty_day = new_date.getDate()+"";
            if (pretty_day.length == 1) pretty_day = "0"+pretty_day;
            var pretty_hours = new_date.getHours()+"";
            if (pretty_hours.length == 1) pretty_hours = "0"+pretty_hours;
            var pretty_mins = new_date.getMinutes()+"";
            if (pretty_mins.length == 1) pretty_mins = "0"+pretty_mins;
            var pretty_secs = new_date.getSeconds()+"";
            if (pretty_secs.length == 1) pretty_secs = "0"+pretty_secs;
            var pretty_new_date = new_date.getFullYear()+"-"+pretty_month+"-"+pretty_day+"T"+pretty_hours+":"+pretty_mins+":"+pretty_secs+"Z";
            return pretty_new_date;
        };

        $("#te_from_input, #te_to_input").change(function(){
            var new_date = date_with_utc_offset($(this).val());
            $(this).val(new_date);
        });
    },

    notifications_dispatcher_check_init: function(){
        $("input.notifications_dispatcher").bind("change", function(e){
            if ($(".notifications_dispatcher:checked").length === 0){
                $("#start_notifications, #save_notifications_changes").prop("disabled", true);
                } else {
                    if ($("div.dispatcher_settings .notifications_dispatcher:checked").length > 0){
                        var script_path_elem = $("#dispatcher_script_path");
                        if (script_path_elem.val() === ""){
                            script_path_elem.addClass("input_error"); 
                            $("#start_notifications, #save_notifications_changes").prop("disabled", true);
                        } else {
                            script_path_elem.removeClass("input_error");
                            $("#start_notifications, #save_notifications_changes").prop("disabled", false);
                        }
                    } else {
                        $("#start_notifications, #save_notifications_changes").prop("disabled", false);
                        $("#dispatcher_script_path").removeClass("input_error");
                    }
                }
         });
    },

    dispatcher_script_path_check_init: function(){
        $("#dispatcher_script_path").bind("keyup", function(e){
            if ($("div.dispatcher_settings .notifications_dispatcher:checked").length > 0){
                var script_path_elem = $("#dispatcher_script_path");
                if (script_path_elem.val() === ""){
                    script_path_elem.addClass("input_error"); 
                    $("#start_notifications, #save_notifications_changes").prop("disabled", true);
                } else {
                    script_path_elem.removeClass("input_error");
                    $("#start_notifications, #save_notifications_changes").prop("disabled", false);
                }
            } 
        });
    },

    datatable_select_buttons: function(){
      //TODO move into a View
      var self = this;
      $(".select_button").click(function(e){
        var button_id = $(this).attr("id");
        var datatable_id = "";
        $.each($(".datatable:visible"), function(i, e){
            if ($(e).attr("id") !== "") datatable_id = $(e).attr("id"); 
        });
        if (document.location.hash.indexOf("notifications") > 0){
            var url = "subscription";
            var data_src_id_name = "data_src_id"; 
            var table_id = "#datatable_104";
        } else {
            var url = "dataResource";
            var data_src_id_name = "data_set_resource_id";
            var table_id = "#datatable_106";
        }
        switch (button_id) {
          case "deselect_all":
            $("#"+datatable_id+" input:checkbox").prop("checked", false);
            break;
          case "select_all":
            $("#"+datatable_id+" input:checkbox").prop("checked", true);
            break;
          case "delete_selected":
            var ds_checked = $("#"+datatable_id+" input:checked");
            var ds_delete_list = [];
            if (url === "dataResource"){
                $.each(ds_checked, function(i, e){
                    ds_delete_list.push($(e).parent().parent().attr("id"));
                });
            } else {
                $.each(ds_checked, function(i, e){
                    var delete_item = {};
                    delete_item[data_src_id_name] = $(e).parent().parent().attr("id");
                    ds_delete_list.push(delete_item);
                });
            }
            var num_selected = ds_checked.length;
            if (num_selected == 0) return alert("Select items to delete them");
            var answer = confirm("Delete "+num_selected + " selected items?");
            if (answer){ 
                self.loading_dialog("Deleting "+num_selected+" items...");
                var subscriptions = JSON.stringify(ds_delete_list);
                if (url == "dataResource"){
                    var data = {"action":"delete"};
                    data[data_src_id_name] = subscriptions;
                } else {
                    var data = {"action":"delete", "subscriptions":subscriptions};
                }
                $.ajax({url:url, type:"POST", data:data, 
                    success: function(resp){
                        $("#radioMySub").trigger("click"); //HACK to refresh current Notifications correctly
                        /*var datatable_inst = $(table_id).dataTable();
                        $.each(ds_delete_list, function(i, e){
                          if (url === "dataResource"){
                            var idx = datatable_inst.fnGetPosition($("#"+e)[0]);
                            datatable_inst.fnDeleteRow(idx);
                          } else {
                            var idx = datatable_inst.fnGetPosition($("#"+e[data_src_id_name])[0]);
                            datatable_inst.fnDeleteRow(idx);
                          }
                        });*/
                        self.loading_dialog();
                    }
                });
                return;
            } else {
                return;
            }
          default:
            return;
        }
      });
    }


});
