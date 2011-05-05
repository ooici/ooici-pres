
OOI.Views.ResourceSelector = Backbone.View.extend({

    events: {
        "click .resource_selector":"switch_resource"
    },

    initialize: function() {
        _.bindAll(this, "switch_resource"); 
        this.controller = this.options.controller;
    },

    switch_resource: function(e){
        var resource = $(e.target).attr("id");
        switch (resource) {
            case "radioAllPubRes":
                return this.controller.workflow100.render();
            case "radioMySub":
                return this.controller.workflow104.render();
            case "radioMyPubRes":
                return this.controller.workflow106.render();
            default:
                return;
        }
    }
});


OOI.Views.Workflow100 = Backbone.View.extend({
    /*
        All Resources.
    */
    events: {
        "click tbody tr":"show_detail"
    },

    initialize: function() {
        _.bindAll(this, "render", "show_detail", "show_all_detail"); 
        this.controller = this.options.controller;
        this.datatable = this.controller.datatable_init("#datatable_100", 5);
        $("#radioAllPubRes").trigger("click");
    },

    render: function() {
        this.populate_table();
        this.presentation();
        $('.ui-layout-center, .ui-layout-east').show();
        return this;
    },

    show_detail: function(e) {
        var tr = $(e.target);
        var data_resource_id = tr.parent().attr("id"); 
        if (tr.text() == "Details"){
            this.show_all_detail(data_resource_id);
        }
        self = this;
        $.ajax({url:"dataResource", type:"GET", dataType:"json", data:{"action":"detail", "data_resource_id":data_resource_id}, 
            success: function(resp){self.dataset_sidebar(resp, self)}
        });
    },

    show_all_detail: function(data_resource_id) {
        $("#datatable_details_scroll").show();
        $("#datatable_100_wrapper, #datatable_104_wrapper, #datatable_106_wrapper").hide();
        self = this;
        $.ajax({url:"dataResource", type:"GET", dataType:"json", 
            data:{"action":"detail", "data_resource_id":data_resource_id}, 
            success: function(resp){
                var html = "<pre style='font-size:18px'>"+JSON.stringify(resp.dataResourceSummary);
                html += "<br><br>"+JSON.stringify(resp.source);
                html += "<br><br>"+JSON.stringify(resp.variable)+"</pre>";
                html = html.replace(/,/g, "<br>").replace(/}/g, "").replace(/{/g, "").replace(/\[/g, "").replace(/\]/g, "");
                $("#datatable_details_container").html(html).show();
                self.controller.loading_dialog();
            }
        });
    },

    dataset_sidebar: function(resp, self){
        var data = resp.dataResourceSummary;
        $(self.datatable.fnSettings().aoData).each(function () {
           $(this.nTr).removeClass('row_selected');
        });
        // Expands right pane panels when row is selected. Also closes panels if already expanded.
        if(!$('#eastMultiOpenAccordion h3').hasClass('ui-state-active ui-corner-top')) $('#eastMultiOpenAccordion h3').trigger('click');
        $('a#rp_dsTitle').html(data.institution);
        $("#ds_title").html(data.title);
        $("#ds_publisher_contact").html(data.institution);
        var ds_source = "<b>Title:</b> "+data.title+"<br><br><b>Description:</b><br>"+data.summary;
        $("#ds_source").html(ds_source);
        $("#ds_source").html(data.source);
        $("#ds_source_contact").html(data.source);
        $("#ds_variables").html(JSON.stringify(resp.variable));
        $("#ds_geospatial_coverage").html("lat_min:"+data.ion_geospatial_lat_min + ", lat_max:"+data.ion_geospatial_lat_max+", lon_min"+data.ion_geospatial_lon_min+", lon_max:"+data.ion_geospatial_lon_max + ", vertical_min:" + data.ion_geospatial_vertical_min + ", vertical_max:" + data.ion_geospatial_vertical_max + " vertical_positive: " + data.ion_geospatial_vertical_positive);
        $("#ds_temporal_coverage").html(data.ion_time_coverage_start + " - "+data.ion_time_coverage_end);
        $("#ds_references").html(data.references);
        $(".data_sources").show();
        $(".notification_settings, .dispatcher_settings").hide();
        $("#download_dataset_button, #setup_notifications").removeAttr("disabled");
        self.controller.loading_dialog();
    },

    populate_table: function(){
        this.controller.loading_dialog("Loading datasets...");
        this.datatable.fnClearTable();
        var datatable_id = this.datatable.attr("id");
        var self = this;
        $.ajax({url:"dataResource", type:"GET", data:{action:"find"}, dataType:"json",
            success: function(data){
                $("#datatable_select_buttons").hide();
                self.controller.resource_collection.remove_all();
                $.each(data.dataResourceSummary, function(i, elem){
                    self.controller.resource_collection.add(elem);
                    self.datatable.fnAddData([elem.title, elem.institution, elem.source, "Date Registered", "Details"]);
                    $($("#datatable_100").dataTable().fnGetNodes(i)).attr("id", elem.data_resource_id);
                });
                c = self.controller.resource_collection;
                $("table#datatable_100 tbody tr td").css("width", "30%");
                self.controller.loading_dialog();
            }
        });
    },

    presentation: function(){
        $("#datatable_100_wrapper").show();
        $("#datatable_104_wrapper, #datatable_106_wrapper").hide();
        $("#datatable_details_container").hide();
        $("#datatable h1").text("All Registered Resources");
        $(".notification_settings").hide();
        $("#save_notification_settings").hide(); //button
        $("#geospatial_selection_button").show();
        $("#download_dataset_button, #setup_notifications").show().attr("disabled", "disabled");
        $("h3.data_sources").show();
        $("table#datatable_100 thead tr:first").find("th:eq(0)").text("Title").end().find("th:eq(1)").text("Provider").end().find("th:eq(2)").text("Type").end().find("th:eq(3)").text("Date Registered"); //TODO: put logic into template
    }
});

OOI.Views.Workflow104 = Backbone.View.extend({
    /*
        My Notification Settings.
    */
    events: {
        //"click #radioMyPubRes":"render",
    },

    initialize: function() {
        _.bindAll(this, "render"); 
        this.controller = this.options.controller;
        this.datatable = this.controller.datatable_init("#datatable_104", 5);
    },

    render: function() {
        this.presentation();
        this.populate_table();
        return this;
    },

    populate_table: function(){
        this.controller.loading_dialog("Loading datasets...");
        this.datatable.fnClearTable();
        var datatable_id = this.datatable.attr("id");
        var self = this;
        $.ajax({url:"subscription", type:"GET", data:{action:"find"}, dataType:"json",
            success: function(data){
                var cb = "<input type='checkbox'/>";
                $.each(data.dataResourceSummary, function(i, elem){
                    self.datatable.fnAddData([cb, elem.title, elem.institution, elem.created, "Details"]);
                    $($("#datatable_104").dataTable().fnGetNodes(i)).attr("id", elem.data_resource_id); //XXX use Backbone for this
                });
                $("#datatable_select_buttons").show();
                $.each($("table#datatable_104 tbody tr"), function(i, e){$(e).find("td:first").css("width", "4% !important")}); //XXX 
                $("table#datatable_104 tbody tr").not(":first").find("td:not(:first)").css("width", "25%"); //XXX
                self.controller.loading_dialog();
            }
        });
    },

    presentation: function(){
        $("#datatable_104_wrapper").show();
        $("#datatable_100_wrapper").hide();
        $("#datatable_106_wrapper").hide();
        $(".notification_settings").hide();
        $("#datatable_details_container").hide();
        $("#datatable h1").text("Notification Settings");
        $('#eastMultiOpenAccordion h3:eq(7)').show().trigger('click');
        $(".data_sources").hide();
        $("#geospatial_selection_button").hide();
        $("#download_dataset_button, #setup_notifications").hide();
    },


});


OOI.Views.Workflow105 = Backbone.View.extend({
    /*
        Register New Resource.
    */
    events: {
        "click .resouce_selector_tab":"resource_selector"
    },

    initialize: function() { 
        _.bindAll(this, "render"); 
        this.controller = this.options.controller;
    },

    render: function() {
        return this;
    },

    resource_selector: function(e){
        var id = $(e.target).attr("id");
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
    }

});




OOI.Views.Workflow106 = Backbone.View.extend({
    /*
        My Registered Resources.
    */

    events: {
        //"click #radioMyPubRes":"datatable"
    },

    initialize: function() {
        _.bindAll(this, "render"); 
        this.controller = this.options.controller;
        this.datatable = this.controller.datatable_init("#datatable_106", 6);
    },

    render: function() {
        this.populate_table();
        this.presentation();
        return this;
    }, 
    
    populate_table: function(){
        this.controller.loading_dialog("Loading datasets...");
        this.datatable.fnClearTable();
        var datatable_id = this.datatable.attr("id");
        var self = this;
        $.ajax({url:"dataResource", type:"GET", data:{action:"findByUser"}, dataType:"json",
            success: function(data){
                $.each(data.dataResourceSummary, function(i, elem){
                    var cb = "<input type='checkbox'/>";
                    self.datatable.fnAddData([cb, elem.title, elem.institution, elem.source, "Date Registered", "Details"]);
                    $($("#datatable_106").dataTable().fnGetNodes(i)).attr("id", elem.data_resource_id);
                });
                $("#datatable_select_buttons").show();
                $.each($("table#datatable_106 tbody tr"), function(i, e){$(e).find("td:first").css("width", "4% !important")});
                $("table#datatable_106 tbody tr").not(":first").find("td:not(:first)").css("width", "25%");
                self.controller.loading_dialog();
            }
        });
    },

    presentation: function(){
        $("#datatable_106_wrapper").show();
        $("#datatable_100_wrapper").hide();
        $("#datatable_104_wrapper").hide();
        $(".notification_settings").hide();
        $("#datatable_details_container").hide();
        $("#datatable h1").text("My Registered Resources");
        $("#save_notification_settings").hide(); //button
        $("#geospatial_selection_button").hide();
        $(".notification_settings").hide();
        $("#download_dataset_button, #setup_notifications").hide().attr("disabled", "disabled");
    }

});


OOI.Views.GeospatialContainer = Backbone.View.extend({
    //TODO: incomplete functionality

    events: {
        "click #geospatial_selection_button":"render_geo"
    },

    initialize: function() {
        _.bindAll(this, "render_geo"); 
        this.controller = this.options.controller;
    },

    render_geo:function(){
        var action = "find";
        var minTime = $("#te_from_input").val(), maxTime = $("#te_to_input").val();
        var minLatitude = $("#ge_bb_south").val(), maxLatitude = $("#ge_bb_north").val(); 
        var minLongitude = $("#ge_bb_east").val(), maxLongitude = $("#ge_bb_west").val();
        var minVertical = $("#ge_altitude_lb").val(), maxVertical = $("#ge_altitude_ub").val();
        var posVertical="down"; //XXX
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
        self = this;
        self.controller.loading_dialog("Loading datatable...");
        $.ajax({url:"dataResource", type:"GET", dataType:"json", data:data, 
            success: function(resp){
                self.datatable_100.fnClearTable(); //XXX
                $.each(resp.dataResourceSummary, function(i, elem){
                    self.datatable_100.fnAddData([elem.title, elem.institution, elem.source, "Date Registered", "Details"]); //XXX
                });
                $("table#datatable_100 tbody tr td").css("width", "30%");
                self.controller.loading_dialog();
            }
        });
        return this;
    }

});
 

OOI.Views.Layout = Backbone.View.extend({
    events: {},

    initialize: function() {
        this.layout = this.layout_main_init();
        this.layout_center_inner = this.layout_center_inner_init();
        this.layout_west_inner = this.layout_west_inner_init();
        this.layout_east_inner = this.layout_east_inner_init();
        $('.ui-layout-center').hide();
        $('.ui-layout-east').hide();
        $('#eastMultiOpenAccordion, #westMultiOpenAccordion').multiAccordion();
        $('#westMultiOpenAccordion h3').slice(0, 3).trigger('click');

    },

    layout_main_init: function(){
        //  set a 'fixed height' on the container so it does not collapse...
        $(this.el).height($(window).height() - $(this.el).offset().top);
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
    }
});



