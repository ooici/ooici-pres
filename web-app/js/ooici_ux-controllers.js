OOI.Controllers.Dashboard = Backbone.Controller.extend({
    routes: {
      "":"main",
      //"wf100":"workflow100",
    },

    initialize: function(options) {
        _.bindAll(this, "main");
        this.layout = new OOI.Views.Layout({"el":"#layoutContainer"}); 
        this.workflow100 = new OOI.Views.Workflow100({"el":"#datatable100", controller:this}); 
        this.workflow104 = new OOI.Views.Workflow104({"el":"#datatable104", controller:this}); 
        this.workflow106 = new OOI.Views.Workflow106({"el":"#datatable106", controller:this}); 
    },

    main: function(){
        console.log("Controllers.main called");
        this.workflow100.render();
    },

    datatable_init: function(id, columns){
        var oTable = $(id).dataTable({
            "aaData":[_.map(_.range(columns), function(x){return null;})],
            "bJQueryUI": true, 
            "sPaginationType": "full_numbers"
        });
        return oTable;
    },

    populate_table: function(url, datatable){
        //datatable.fnClearTable();
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
    }

});
