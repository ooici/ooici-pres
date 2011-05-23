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
        this.geospatial_container = new OOI.Views.GeospatialContainer({"el":"#west_south", controller:this}); 
        this.resource_actions = new OOI.Views.ResourceActions({"el":".east-south", controller:this});
		this.instruments = new OOI.Views.InstrumentList({"el":"#datatable_instruments", controller:this});

        this.datatable_select_buttons();
        
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
    },

    all_registered_resources: function(){
        this.workflow100.render();
    },

    all_registered_resources_details: function(nth_dataset){
        var model = this.resource_collection.models[nth_dataset];
        if (typeof model === "undefined"){
            window.location.hash = "#/0";
        } else {
            var data_resource_id = model.get("datasetMetadata")["data_resource_id"];
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
        var oTable = $(id).dataTable({
            "aLengthMenu": [[20, 25, 50, -1], [20, 25, 50, "All"]],
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

    datatable_select_buttons: function(){
      //TODO move into a View
      var self = this;
      $(".select_button").click(function(){
        var button_id = $(this).attr("id");
        var datatable_id = $(".datatable:visible").attr("id"); 
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
            if (ds_checked.first().parent().attr("id") !== ""){
                $.each(ds_checked, function(i, e){
                    var delete_item = {"data_src_id":$(e).parent().parent().attr("id")};
                    ds_delete_list.push(delete_item);
                });
            } else {
                $.each(ds_checked, function(i, e){
                    var delete_item = {"data_src_id":$(e).parent().parent().attr("id")};
                    ds_delete_list.push(delete_item);
                });
            }
            var num_selected = ds_checked.length;
            if (num_selected == 0) return alert("Select items to delete them");
            var answer = confirm("Delete "+num_selected + " selected items?");
            if (answer){ 
                self.loading_dialog("Deleting "+num_selected+" items...");
                if (document.location.hash.indexOf("notifications") > 0){
                    var url = "subscription";
                } else {
                    var url = "dataResource";
                }
                var subscriptions = JSON.stringify(ds_delete_list);
                var data = {"action":"delete", "subscriptions":subscriptions};
                $.ajax({url:url, type:"POST", data:data, 
                    success: function(resp){
                        //TODO: Refresh Table
                        //document.location = "/";
                    }
                });
                setTimeout(function(){self.loading_dialog()}, 500); //XXX this is imperfect if the response time is long.
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
