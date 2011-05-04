OOI.Controllers.Dashboard = Backbone.Controller.extend({
    routes: {
      "":"main",
      //"wf100":"workflow100",
    },

    initialize: function(options) {
        _.bindAll(this, "main");
        this.layout = new OOI.Views.Layout({"el":"#layoutContainer"}); 
        this.workflow100 = new OOI.Views.Workflow100({"el":"#datatable", controller:this}); 
        this.workflow104 = new OOI.Views.Workflow104({"el":"#datatable", controller:this}); 
        this.workflow105 = new OOI.Views.Workflow105({"el":"#resource_selector_view", controller:this}); 
        this.workflow106 = new OOI.Views.Workflow106({"el":"#datatable", controller:this}); 
        this.resource_selector = new OOI.Views.ResourceSelector({"el":"#view_existing", controller:this}); 
        this.modal_dialogs();
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
    }


});
