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
        this.datatable = this.datatable_init();
        this.wf_100(this.datatable);
        this.wf_101(this.datatable);
        this.wf_104(this.datatable);
        this.geospatial_container();
        $("#radioAllPubRes").trigger("click"); //XXX temporary default
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

    datatable_init: function(){
        var oTable = $('#example').dataTable({
            "aaData":[[null, null, null, null, null]],
            "bJQueryUI": true, 
            "sPaginationType": "full_numbers"
        });
        return oTable;
    // Initialize the center pane datatable
    //oTable = $('#example').dataTable();
    },

    populate_table: function(url, datatable){
        datatable.fnClearTable();
        $.getJSON(url, function(data){
            if (url == "service/list"){
                $.each(data, function(i, elem){
                    datatable.fnAddData([elem.title, elem.institution, elem.source, "Date Registered", "Details"]);
                });
            } 
            if (url == "service/notifications"){
                $.each(data, function(i, elem){
                    datatable.fnAddData(["[]", elem.title, elem.institution, elem.created, "Details"]);
                });
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
        /**
         * WF 100
         *
         * Handles user click events within center pane datatable
        */

        $('#eastMultiOpenAccordion').multiAccordion();
        $('#westMultiOpenAccordion').multiAccordion();

        // auto open Resource Selector panel
        $('#westMultiOpenAccordion h3:eq(0)').trigger('click');
        // auto open Geospatial Extent panel
        $('#westMultiOpenAccordion h3:eq(1)').trigger('click');
        // auto open Temporal Extent panel
        $('#westMultiOpenAccordion h3:eq(2)').trigger('click');

        $('.ui-layout-center').hide();
        $('.ui-layout-east').hide();

        self = this;
        $('#radioAllPubRes').bind('click', function(event) {
            $("#container h1").text("Data Resources");
            $(".notification_settings").hide();
            if ($("#rp_dsMetaInfo").text() == ""){
                $("div.data_sources").hide();
            } else {
                $(".data_sources").show();
            }
            $("table#example thead tr:first").find("th:eq(0)").text("Title").end().find("th:eq(1)").text("Provider").end().find("th:eq(2)").text("Type").end().find("th:eq(3)").text("Date Registered");
            self.populate_table("service/list", datatable);
            $('.ui-layout-center').show();
            $('.ui-layout-east').show();
        });


        $("#example tbody").unbind("click").click(function(event) {
        // Upon deselect of a row, turns that row's highlighting off

            //console.log($(this).target.index());
            var nth_elem = $(event.target).parent().index()+1;

            $(datatable.fnSettings().aoData).each(function () {
               $(this.nTr).removeClass('row_selected');
            });

            // Highlights selected row
            $(event.target.parentNode).addClass('row_selected').text();

            // Expands right pane panels when row is selected. Also closes panels if already expanded.
            if(!$('#eastMultiOpenAccordion h3').hasClass('ui-state-active ui-corner-top')) {
                $('#eastMultiOpenAccordion h3').trigger('click');
            }
            // Get the hidden column data for this row
            var rowData = datatable.fnGetData(event.target.parentNode);

            // Get the data source Id from column 0
            var dsId = rowData[0];

            // Get dataSource title from the selected row
            var dsTitle = rowData[1];
            // Set dataSource title panel content
            $('a#rp_dsTitle').html(dsTitle);

            // Get dataSource MetaInfo from the selected row
            var dsMeta = rowData[5] || "DataSource MetaInfo" + " #"+nth_elem;
            // Set dataSource MetaInfo panel content
            $('div#rp_dsMetaInfo').html(dsMeta);

            // Get dataSource Publisher Info from the selected row
            var dsPubInfo = rowData[6] || "DataSource Publisher Info" + " #"+nth_elem;
            // Set dataSource Publisher info content
            $('div#rp_publisherInfo').html(dsPubInfo);

            // Get dataSource Creator info from the selected row
            var dsCreatorInfo = rowData[7] || "DataSource Creator Info" + " #"+nth_elem;
            // Set dataSource Creator info content
            $('div#rp_creatorInfo').html(dsCreatorInfo);

            // Get dataSource Documentation Info from the selected row
            var dsDocInfo = rowData[8] || "DataSource Documentation" + " #"+nth_elem;
            // Set dataSource Documentation panel content
            $('div#rp_docInfo').html(dsDocInfo);

            // Get dataSource Variables Info from the selected row
            var dsVarInfo = rowData[9] || "DataSource Variables" + " #"+nth_elem;
            // Set dataSource variables panel content
            $('div#rp_variablesInfo').html(dsVarInfo);

            // Get dataSource Access Info from the selected row
            var dsAccessInfo = rowData[10] || "DataSource Access Info" + " #"+nth_elem;
            // Set dataSource access info panel content
            $('div#rp_accessInfo').html(dsAccessInfo);

            // Get dataSource Viewers Info from the selected row
            var dsViewersInfo = rowData[11] || "DataSource Viewers Info" + " #"+nth_elem;
            // Set dataSource viewers info panel content
            $('div#rp_viewersInfo').html(dsViewersInfo);
            $(".notification_settings").hide();

        });


   },

    wf_101: function(datatable){
       /**
        * WF101
        *
        * Handles double click action on a row w/in the center pane's dataResource table.
        *
        * The result of this action should hide the dataResource table and display a comprehensive
        * summary view of the dataResource selected. 
        */
        $("#example tbody").bind('dblclick', function(evt) {
            // Get the hidden column data for this row
            var rowData = datatable.fnGetData(evt.target.parentNode);
            alert("Showing Datatable details...");
        });
    },

    wf_104: function(datatable){
        /* WF104 - User subscriptions */
        self = this;
        $("#radioMySub").bind('click', function(evt) {
            $("#container h1").text("Notification Settings");
            $('#eastMultiOpenAccordion h3:eq(7)').show().trigger('click');
            $(".data_sources").hide();
            $("table#example thead tr:first").find("th:eq(0)").css("width","1px").text("").end().find("th:eq(1)").text("Resource Title").find("th:eq(2)").text("Source").end().end().find("th:eq(3)").text("Notification Initiated").end().find("th:eq(4)").text("Details");
            //var rowData = datatable.fnGetData(event.target.parentNode);
            self.populate_table("service/notifications", datatable);
            //alert("radioMySub");
        });
        $("#example tbody").unbind("click").bind('click', function(evt){
            var nth_elem = $(evt.target).parent().index()+1;
            alert("mysub elem: "+nth_elem);
        });
    },




});
