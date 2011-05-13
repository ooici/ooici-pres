OOI.Controllers.Dashboard = Backbone.Controller.extend({

    routes: {
      "":"all_registered_resources",
      "/:nth_dataset":"all_registered_resources_details",
      "notifications":"my_notification_settings",
      "registered":"my_registered_resources",
      "registered/:nth_dataset":"my_registered_resources_details"
    },

    initialize: function(options) {
        _.bindAll(this, "all_registered_resources", "all_registered_resources_details", "my_notification_settings", "my_registered_resources");

        this.layout = new OOI.Views.Layout({"el":"#layoutContainer"}); 

        this.resource_collection = new OOI.Collections.Resources();
        this.my_resources_collection = new OOI.Collections.MyResources();
        this.my_notifications_collection = new OOI.Collections.MyNotifications();

        this.workflow100 = new OOI.Views.Workflow100({el:"#datatable_100", controller:this}); 
        this.workflow104 = new OOI.Views.Workflow104({el:"#layoutContainer", controller:this}); 
        this.workflow105 = new OOI.Views.Workflow105({el:"#resource_selector_view", controller:this}); 
        this.workflow106 = new OOI.Views.Workflow106({el:"#datatable_106", controller:this}); 

        //this.notifications = new OOI.Views.Notifications({el:"#east_sidebar", controller:this});
        this.account_settings = new OOI.Views.AccountSettings({el:"#account_settings", controller:this});
        this.resource_selector = new OOI.Views.ResourceSelector({el:"#view_existing", controller:this}); 
        this.resource_details_scroll = new OOI.Views.ResourceDetailsScroll({el:"#datatable_details_scroll", controller:this}); 
        this.geospatial_container = new OOI.Views.GeospatialContainer({"el":"#west_south", controller:this}); 

        this.datatable_select_buttons();
        
        if (REGISTERED == "False") {
        	$('#account_settings_link').click();
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

    datatable_select_buttons: function(){
      //TODO move into a View
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
                        //TODO: Refresh Table
                        //document.location = "/"; //XXX
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
    }


});
