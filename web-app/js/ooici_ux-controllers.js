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

        this.layout = new OOI.Views.Layout({"el":"#layoutContainer"}); 

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

        this.datatable_select_buttons();
        this.datetime_selectors();
        
        //TODO: the below should go in a self contained view:

        if (OOI_ROLES.length === 0) {
            $("#radioMyPubRes, #radioMySub").attr("disabled", "disabled");
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
        this.workflow100.render();
    },

    all_registered_resources_details: function(nth_dataset){
        //var model = this.resource_collection.models[nth_dataset];
        if (isNaN(parseInt(nth_dataset))){
            window.location.hash = "#/0";
        } else {
            //var data_resource_id = model.get("datasetMetadata")["data_resource_id"];
            var data_resource_id = $("#datatable_100 tbody tr:nth("+nth_dataset+")").attr("id");
            this.workflow100.show_detail(data_resource_id);
        }
    },

    my_notification_settings: function(){
        this.workflow104.render();
    },

    my_registered_resources: function(){
        this.workflow106.render();
    },

    my_registered_resources_details: function(nth_dataset){
        var model = this.my_resources_collection.models[nth_dataset];
        if (typeof model === "undefined"){
            window.location.hash = "#registered/0";
        } else {
            var data_resource_id = model.get("data_resource_id");
            this.workflow106.show_detail(data_resource_id);
        }
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
                var aoColumns = [{sWidth:'30%'}, {sWidth:'12%'}, {sWidth:'25%'}, {sWidth:'10%'}, {sWidth:'13%'}, {sWidth:'10%'}];
                break;
            case "#datatable_104":
                var aoColumns = [{sWidth: '50px'}, {sWidth: '100px'}, {sWidth: '120px'}, {sWidth: '30px'}, {sWidth: '50px'}];
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
            "sScrollX":"100%",
            "bJQueryUI": true, 
            "sPaginationType": "full_numbers",
            "aoColumns": aoColumns
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
    
    error_dialog: function(path, error_type, error_msg){
        var contact_msg = "Contact helpdesk@oceanobservatories.org";
        if (error_type === 400){
            var msg = error_msg + " '"+path+"'";
        }

        if (error_type === 500){
            msg = "Error accessing: '"+path+"'";
        }
        msg += "\n\n"+contact_msg;
        return alert(msg);
    },

    datetime_selectors:function(){
        $("#te_from_input, #te_to_input").datetimepicker({
            showSecond:true, showTimezone:true, timezone: "+0700", dateFormat:'yy-mm-dd', timeFormat:'hh:mm:ssZ', separator: 'T'});
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
            $("#"+datatable_id+" input:checkbox").attr("checked", "");
            break;
          case "select_all":
            $("#"+datatable_id+" input:checkbox").attr("checked", "checked");
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
                        $.each(ds_delete_list, function(i, e){
                          if (url === "dataResource"){
                            var elem = $("#"+e)[0];
                            $(table_id).dataTable().fnDeleteRow(elem);
                          } else {
                            var elem = $("#"+e[data_src_id_name])[0];
                            $(table_id).dataTable().fnDeleteRow(elem);
                          }
                        });
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
