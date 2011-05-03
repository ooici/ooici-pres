
OOI.Views.Workflow100 = Backbone.View.extend({
    events: {
        "click #radioAllPubRes":"render",
    },

    initialize: function() {
        _.bindAll(this, "render"); 
        this.controller = this.options.controller;
    },

    render: function() {
        //console.log("Workflow100 - render");
        $.fn.dataTableExt.sErrMode = "throw"; //XXX /*throw a*/ in 'js/jquery.dataTables.min.js'.
        this.datatable = this.controller.datatable_init("#datatable_100", 5);
        this.controller.populate_table("dataResource", this.datatable);
        this.presentation();
        $('.ui-layout-center, .ui-layout-east').show();
        return this;
    },

    presentation: function(){
        $("#datatable_100_wrapper").show();
        $("#datatable_104_wrapper").hide();
        $("#datatable_106_wrapper").hide();
        $("#datatable_details_container").hide();
        $("#container h1").text("All Registered Resources");
        $(".notification_settings").hide();
        $("#save_notification_settings").hide(); //button
        $("#geospatial_selection_button").show();
        $("#download_dataset_button, #setup_notifications").show().attr("disabled", "disabled");
        $("h3.data_sources").show();
        $("table#datatable_100 thead tr:first").find("th:eq(0)").text("Title").end().find("th:eq(1)").text("Provider").end().find("th:eq(2)").text("Type").end().find("th:eq(3)").text("Date Registered"); //TODO: put logic into template
    }
});

OOI.Views.Workflow104 = Backbone.View.extend({
    events: {
        //"click #radioAllPubRes":"render",
    },

    initialize: function() {
        _.bindAll(this, "render"); 
        this.controller = this.options.controller;
    },

    render: function() {
        console.log("Workflow104 - render");
        $("#datatable").append($.tmpl("datatable104-tmpl", {}));
        this.controller.datatable_init("datatable_104", 5);
        return this;
    }
});


OOI.Views.Workflow106 = Backbone.View.extend({
    events: {
        //"click #radioAllPubRes":"render",
    },

    initialize: function() {
        _.bindAll(this, "render"); 
        this.controller = this.options.controller;
    },

    render: function() {
        console.log("Workflow106 - render");
        $("#datatable").append($.tmpl("datatable106-tmpl", {}));
        this.controller.datatable_init("datatable_106", 6);
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



