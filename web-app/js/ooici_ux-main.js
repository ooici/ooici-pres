/*
Workflow implementations for OOI UX.
(this file may be split up as it grows larger)
*/

var OOIUX = Backbone.View.extend({

    initialize: function() {
        this.layout = this.layout_init();
        this.datatable = this.datatable_init();
        this.populate_table(this.datatable);
        this.wf_100(this.datatable);
        this.wf_101(this.datatable);
        $("#radioAllPubRes").trigger("click"); //XXX temporary default
    },

    layout_init: function(){
        // first set a 'fixed height' on the container so it does not collapse...
        $(this.el).height($(window).height() - $(this.el).offset().top);
        // NOW create the layout
        var pgLayout = $(this.el).layout({
            resizerClass: 'ui-state-default',
            north__size: 60,
            west__size: 350,
            east__size: 350
        });
        return pgLayout;
    },

    datatable_init: function(){
        var oTable = $('#example').dataTable({
            "aaData":[[null, null, null, null]],
            "bJQueryUI": true, 
            "sPaginationType": "full_numbers"
        });
        return oTable;
    // Initialize the center pane datatable
    //oTable = $('#example').dataTable();
    },

    populate_table: function(datatable){
        //XXX this will be generalized a lot.
        datatable.fnClearTable();//XXX
        $.getJSON("service/list", function(data){
            $.each(data, function(i, elem){
                datatable.fnAddData([elem.title, elem.institution, elem.source, "-"]);
                //console.log(elem);
            });
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
        $("#example tbody").bind('dblclick', function(event) {
            // Get the hidden column data for this row
            var rowData = datatable.fnGetData(event.target.parentNode);
            alert("Showing Datatable details...");
            // Hides the data resource table
            //$('.ui-layout-center').hide();
            //$('#container').html(
            //'<a href="http://www.google.com">back to data table</a>',
            //'hi'
            //);
            /*    $('#container').load('/html/fetchDataResourceDetails', {title:rowData[1]}
                    function(response) { $('container')html(response); }
                )
            */
        });
    },

    wf_100: function(datatable){
        /**
         * WF 100
         *
         * Handles user click events within center pane datatable
        */
        $("#example tbody").click(function(event) {
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

        });

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

        $('input.controlradios').bind('click', function(event) {
            $('.ui-layout-center').show();
            $('.ui-layout-east').show();
        });

   }

});
