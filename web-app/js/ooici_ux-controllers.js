OOI.Controllers.Dashboard = Backbone.Controller.extend({
    routes: {
      "":"main",
      //"wf100":"workflow100",
    },

    initialize: function(options) {
        _.bindAll(this, "main");
        this.layout = new OOI.Views.Layout({"el":"#layoutContainer"}); 

        this.resource_collection = new OOI.Collections.Resources();

        this.workflow100 = new OOI.Views.Workflow100({el:"#datatable_100", controller:this}); 
        this.workflow104 = new OOI.Views.Workflow104({el:"#datatable", controller:this}); 
        this.workflow105 = new OOI.Views.Workflow105({el:"#resource_selector_view", controller:this}); 
        this.workflow106 = new OOI.Views.Workflow106({el:"#datatable", controller:this}); 

        this.resource_selector = new OOI.Views.ResourceSelector({el:"#view_existing", controller:this}); 
        this.geospatial_container = new OOI.Views.GeospatialContainer({"el":"#west_south", controller:this}); 

        this.modal_dialogs();
        this.datatable_select_buttons();
        this.setup_notifications();
        $("#temporalExtent").siblings().last().trigger("click");  //XXX temporary default
    },

    main: function(){
        this.workflow100.render();
        //this.workflow104.render();
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
        $("#account_settings_link").colorbox({inline:true, href:"#account_settings", transition:"none", opacity:0.7});
        $(".modal_close").live("click", function(e){$.fn.colorbox.close();e.preventDefault();});
        if (document.location.search.search("action=register") != -1){
          $.fn.colorbox({inline:true, href:"#account_settings", transition:"none", opacity:0.7});
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
    }


});
