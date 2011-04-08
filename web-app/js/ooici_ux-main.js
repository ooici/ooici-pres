/*
Workflow implementations for OOI UX.
(this file may be split up as it grows larger)
*/

var OOIUX = Backbone.View.extend({

    initialize: function() {
        this.layout = this.layout_main_init();
        this.layout_center_inner = this.layout_center_inner_init();
        this.layout_west_inner = this.layout_west_inner_init();
        this.layout_east_inner = this.layout_east_inner_init();
        this.datatable_100 = this.datatable_init("#datatable_100", 5);
        this.datatable_104 = this.datatable_init("#datatable_104", 5);
        this.datatable_106 = this.datatable_init("#datatable_106", 5);
        this.wf_100(this.datatable_100);
        this.wf_101(this.datatable_100);
        this.wf_104(this.datatable_104);
        this.wf_105();
        this.wf_106(this.datatable_106);
        this.geospatial_container();
        $("#radioAllPubRes").trigger("click"); //XXX temporary default
        $("#datatable_104_wrapper").hide();  //XXX temporary default
        $("#datatable_106_wrapper").hide();  //XXX temporary default
    },

    layout_main_init: function(){
        // first set a 'fixed height' on the container so it does not collapse...
        $(this.el).height($(window).height() - $(this.el).offset().top);
        // NOW create the layout
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
        datatable.fnClearTable();
        $.getJSON(url, function(data){
            if (url == "service/list" || url == "service/my_registered_resources"){
                $.each(data, function(i, elem){
                    datatable.fnAddData([elem.title, elem.institution, elem.source, "Date Registered", "Details"]);
                });
                $("table#datatable_106 tbody tr td").css("width","20%"); //XXX
            } 
            if (url == "service/notifications"){
                $.each(data, function(i, elem){
                    datatable.fnAddData(["[]", elem.title, elem.institution, elem.created, "Details"]);
                });
                $("table#datatable_104 tbody tr td").css("width","25%"); //XXX
                $.each($("table#datatable_104 tbody tr"), function(i, e){$(e).find("td:first").css("width", "5%")});
            } 

        });
    },


    geospatial_container: function(){
        /* MOCK OUT of geospatial_container widget */
        var geospatial_container_data = function(){
            var data = JSON.stringify({"user_ooi_id":"3f27a744-2c3e-4d2a-a98c-050b246334a3","minLatitude":32.87521,"maxLatitude":32.97521,"minLongitude":-117.274609,"maxLongitude":-117.174609,"minVertical":5.5,"maxVertical":6.6,"posVertical":7.7,"minTime":8.8,"maxTime": 9.9,"identity":""});
            $.ajax({url:"service/geospatial", type:"POST", data:data, 
                success: function(resp){
                    alert("geospatial_container resp: "+resp);
                }
            });
        };
        $("#geospatialContainer").click(geospatial_container_data);
    },


    wf_100: function(datatable){
        /* WF 100 - Handles user click events within center pane datatable */
        $('#eastMultiOpenAccordion, #westMultiOpenAccordion').multiAccordion();

        // auto open Resource Selector panel
        $('#westMultiOpenAccordion h3:eq(0)').trigger('click');
        // auto open Geospatial Extent panel
        $('#westMultiOpenAccordion h3:eq(1)').trigger('click');
        // auto open Temporal Extent panel
        $('#westMultiOpenAccordion h3:eq(2)').trigger('click');

        $('.ui-layout-center').hide();
        $('.ui-layout-east').hide();

        $("#geospatialContainer .all").attr("checked", "checked");
        $(".boundingBoxControls input").attr("disabled", "disabled");
        $(".altitudeControls input").attr("disabled", "disabled");

        self = this;

        $("#geospatialContainer .defined").click(function(){
          var is_bounding = $(this).hasClass("bounding");
          if (is_bounding){
            $(".boundingBoxControls input").removeAttr("disabled");
          } else {
            $(".altitudeControls input").removeAttr("disabled");
          }
          $("#geospatial_selection_button").removeAttr("disabled");
        });
        $("#geospatialContainer .all").click(function(){
          var is_bounding = $(this).hasClass("bounding");
          if (is_bounding){
            $(".boundingBoxControls input").attr("disabled", "disabled");
          } else {
            $(".altitudeControls input").attr("disabled", "disabled");
          }
          $("#geospatial_selection_button").removeAttr("disabled");
        });

        $('#radioAllPubRes').bind('click', function(event) {
            self.wf_100_presentation();
            $("h3.data_sources").show();

            $("table#datatable_100 thead tr:first").find("th:eq(0)").text("Title").end().find("th:eq(1)").text("Provider").end().find("th:eq(2)").text("Type").end().find("th:eq(3)").text("Date Registered");
            self.populate_table("service/list", datatable);
            $('.ui-layout-center').show();
            $('.ui-layout-east').show();
        });


        $("#datatable_100 tbody").unbind("click").click(function(event) {
            var nth_elem = $(event.target).parent().index()+1;

            $(datatable.fnSettings().aoData).each(function () {
               $(this.nTr).removeClass('row_selected');
            });

            $(event.target.parentNode).addClass('row_selected').text(); // Highlights selected row

            // Expands right pane panels when row is selected. Also closes panels if already expanded.
            if(!$('#eastMultiOpenAccordion h3').hasClass('ui-state-active ui-corner-top')) {
                $('#eastMultiOpenAccordion h3').trigger('click');
            }
            // Get the hidden column data for this row
            var rowData = datatable.fnGetData(event.target.parentNode);

            // Get the data source Id from column 0 var dsId = rowData[0];
            $('a#rp_dsTitle').html(rowData[1]); // Get and set dataSource title
            $('div#rp_dsMetaInfo').html(rowData[5] || "DataSource MetaInfo" + " #"+nth_elem);
            $('div#rp_publisherInfo').html(rowData[6] || "DataSource Publisher Info" + " #"+nth_elem);
            $('div#rp_creatorInfo').html(rowData[7] || "DataSource Creator Info" + " #"+nth_elem);
            $('div#rp_docInfo').html(rowData[8] || "DataSource Documentation" + " #"+nth_elem);
            $('div#rp_variablesInfo').html(rowData[9] || "DataSource Variables" + " #"+nth_elem);
            $('div#rp_accessInfo').html(rowData[10] || "DataSource Access Info" + " #"+nth_elem);
            $('div#rp_viewersInfo').html(rowData[11] || "DataSource Viewers Info" + " #"+nth_elem);

            $(".data_sources").show();
            $(".notification_settings").hide();
            $("#download_dataset_button, #setup_notifications").removeAttr("disabled");
        });
   },

    wf_100_presentation: function(){
        $("#datatable_100_wrapper").show();
        $("#datatable_104_wrapper").hide();
        $("#container h1").text("All Registered Resources");
        $(".notification_settings").hide();
        $("#save_notification_settings").hide(); //button
        $("#geospatial_selection_button").show();
        $("#download_dataset_button, #setup_notifications").show().attr("disabled", "disabled");
    },

    wf_101: function(datatable){
       /* WF101 - Handles double click action on a row w/in the center pane's dataResource table.  */
        $("#download_dataset_button").click(function(){
            document.location = "service/createDownloadUrl";
        });
        $("#datatable_100 tbody").bind('dblclick', function(evt) {
            // Get the hidden column data for this row
            var rowData = datatable.fnGetData(evt.target.parentNode);
            alert("Showing Datatable details...");
        });
    },

    wf_104: function(datatable){
        /* WF104 - User subscriptions */
        self = this;
        $(".notification_settings input:checkbox").change(function(){
            $("#save_notification_settings").removeAttr("disabled");
        });
        $("#save_notification_settings").click(function(){
            if ($("#save_notification_settings").attr("disabled") != "") return;
            var settings_checked = "";
            $.each($(".notification_settings input:checked"), function(i, e){
                settings_checked += $(e).attr("id") + ", ";
            });
            alert("Saving Notification Setting w/ ids: '"+settings_checked+"'");
        });

        $("#radioMySub").bind('click', function(evt) {
            self.wf_104_presentation();
            $("#notification_settings").show();

            $("table#datatable_104 thead tr:first").find("th:eq(0)").text("").end().find("th:eq(1)").text("Resource Title").find("th:eq(2)").text("Source").end().end().find("th:eq(3)").text("Notification Initiated").end().find("th:eq(4)").text("Details");
            self.populate_table("service/notifications", datatable);
        });
        $("#datatable_104 tbody").unbind("click").bind('click', function(evt){ //TODO: use 'delegate'.
            var nth_elem = $(evt.target).parent().index()+1;
            $("#save_notification_settings").show(); //button
            $("#notification_details").html("Notification Triggers for: <b>#"+nth_elem+"</b> element.");
            if (!$(".notification_settings:nth(1)").is(":visible") ) $(".notification_settings").click();
        });
    },
    wf_104_presentation: function(){
        $("#datatable_104_wrapper").show();
        $("#datatable_100_wrapper").hide();
        $("#datatable_106_wrapper").hide();
        $("#container h1").text("Notification Settings");
        $('#eastMultiOpenAccordion h3:eq(7)').show().trigger('click');
        $(".data_sources").hide();
        $("#geospatial_selection_button").hide();
        $("#download_dataset_button, #setup_notifications").hide();
    },

    wf_105: function(){
        this.resource_selector();
    },

    resource_selector: function(){
        $(".resouce_selector_tab").bind('click', function(evt) {
            var id = $(this).attr("id");
            $("#"+id).addClass("selected");
            if (id == "view_existing_tab"){
                $("#view_existing").show();
                $("#register_new").hide();
                $("#register_new_tab").removeClass("selected");
            } else {
                $("#register_new").show();
                $("#view_existing").hide();
                $("#view_existing_tab").removeClass("selected");
            }
        });

    },


    wf_106: function(datatable){
        $("#radioMyPubRes").bind('click', function(evt) {
            self.wf_106_presentation();
            self.populate_table("service/my_registered_resources", datatable);
        });
    },

    wf_106_presentation: function(){
        $("#datatable_106_wrapper").show();
        $("#datatable_100_wrapper").hide();
        $("#datatable_104_wrapper").hide();
        $("#container h1").text("My Registered Resources");
        $("#save_notification_settings").hide(); //button
        $("#geospatial_selection_button").hide();
        $("#download_dataset_button, #setup_notifications").hide().attr("disabled", "disabled");
    }

});
