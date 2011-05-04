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
        this.workflow106 = new OOI.Views.Workflow106({"el":"#datatable", controller:this}); 
        this.resource_selector = new OOI.Views.ResourceSelector({"el":"#view_existing", controller:this}); 
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

});
