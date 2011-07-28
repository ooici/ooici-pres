
OOI.Views.ResourceSelector = Backbone.View.extend({

    events: {
        "click .resource_selector":"switch_resource"
    },

    initialize: function() {
        _.bindAll(this, "switch_resource"); 
        this.controller = this.options.controller;
    },

    switch_resource: function(e){
        /* Manually change hash to trigger corresponding
        callback function. If hash is currently the same,
        call corresponding callback function directly
        */
        var resource = $(e.target).attr("id");
        switch (resource) {
            case "radioAllPubRes":
                if (window.location.hash === ""){
                    this.controller.all_registered_resources();
                }
                return window.location.hash="";
            case "radioMySub":
                if (window.location.hash === "#notifications"){
                    this.controller.my_notification_settings();
                }
                return window.location.hash="notifications";
            case "radioMyPubRes":
                if (window.location.hash === "#registered"){
                    this.controller.my_registered_resources();
                }
                return window.location.hash="registered";
            default:
                return window.location.hash="";
        }
    }
});

OOI.Views.AdminSelector = Backbone.View.extend({

    events: {
        "click .admin_selector":"switch_admin"
    },

    initialize: function() {
        _.bindAll(this, "switch_admin"); 
        this.controller = this.options.controller;
    },

    switch_admin: function(e){
        var tool = $(e.target).attr("id");
        switch (tool) {
            case "radioRunEPU":
                return window.location.hash="running-epus";
            case "radioRegUsers":
                return window.location.hash="registered-users";
			case "radioDatasets":
			    return window.location.hash="datasets";
			case "radioDatasources":
			    return window.location.hash="datasources";
            default:
                return window.location.hash="";
        }
    }
});


OOI.Views.ResourceDetailsScroll = Backbone.View.extend({

    events: {
        "click #dataset_scroll_left":"scroll_left",
        "click #dataset_scroll_right":"scroll_right",
        "click #dataset_return_button":"return_to_dataset_listing",
    },

    initialize: function() {
        _.bindAll(this, "scroll_left", "scroll_right", "return_to_dataset_listing"); 
        this.controller = this.options.controller;
    },

    scroll_left: function(e){
        var hash_args = document.location.hash.split("/");
        var nth_elem = parseInt(hash_args[1]); 
        if (nth_elem <= 0){
            var current = hash_args[0];
            if (current == "" || current == "#" ){
                var end_num = this.controller.resource_collection.models.length-1;
            }
            if (current == "#registered"){
                var end_num = this.controller.my_resources_collection.models.length-1;
            }
            document.location.hash = hash_args[0]+"/"+end_num;
        } else {
            var next_n = nth_elem - 1;
            document.location.hash = hash_args[0]+"/"+next_n;
        }
    },

    scroll_right: function(e){
        var hash_args = document.location.hash.split("/");
        var nth_elem = parseInt(hash_args[1]); 
        var next_n = nth_elem + 1;
        var current = hash_args[0];
        if (current == "" || current == "#" ){
            if (next_n > this.controller.resource_collection.models.length - 1) next_n = 0;
        }
        if (current == "#registered"){
            if (next_n > this.controller.my_resources_collection.models.length - 1) next_n = 0;
        }
        document.location.hash = hash_args[0]+"/"+next_n;
    },

    return_to_dataset_listing: function(e){
        var return_hash = document.location.hash.split("/")[0];
        document.location.hash = return_hash;
    }
});




OOI.Views.Workflow100 = Backbone.View.extend({
    /*
        All Resources.
    */

    events: {
        "click tbody tr":"show_detail_clicked"
    },

    initialize: function() {
        _.bindAll(this, "render", "show_detail", "show_detail_clicked", "show_detail_all"); 
        this.controller = this.options.controller;
        this.datatable = this.controller.datatable_init("#datatable_100", 6);
        $("#radioAllPubRes").trigger("click");
    },

    render: function() {
        this.populate_table();
        this.presentation();
        $('.ui-layout-center, .ui-layout-east').show();
        return this;
    },

    show_detail_clicked: function(e) {
        if ( $(e.target).hasClass("dataTables_empty") ) return;
        var tr_target = $(e.target).parents("tr");
        var data_resource_id = tr_target.attr("id"); 
        $("#datatable_100 tr").removeClass("selected");
        $("#eastMultiOpenAccordion .accordion-inactive").removeClass("accordion-inactive");
        tr_target.addClass("selected");
        if ($(e.target).hasClass("dataset_details")){
            $("#datatable_details_scroll, #datatable_details_container").show();
            $("#datatable_select_buttons, .dataTables_wrapper").hide();
            var nth_elem = tr_target.index();
            if (window.location.hash === ""){
                window.location.hash += "#/"+nth_elem;
            } else {
                window.location.hash += "/"+nth_elem;
            }
        } else {
            this.show_detail(data_resource_id);
        }
    },

    show_detail: function(data_resource_id){
        $("#download_dataset_button, #setup_notifications").show();
        $("#start_notifications").hide();
        $("#datatable_details_container").html("<p>Loading dataset details...</p>");
        this.controller.loading_dialog("Loading dataset details...");
        var self = this;
        $.ajax({url:"dataResource", type:"GET", dataType:"json", data:{"action":"detail", "data_resource_id":data_resource_id}, 
            success: function(resp){
                self.show_detail_all(resp, data_resource_id);
                self.dataset_sidebar(resp, self);
            },
            error:function(xhr, ajaxOptions, thrownError){
                self.controller.error_dialog("dataResource", xhr.status, xhr.responseText);
            }
        });
    },

    show_detail_all: function(resp, data_resource_id) {
        var dataResourceSummary = resp.dataResourceSummary, source = resp.source || {};

        if ( $("#datatable_details_container").is(":visible") ) { //only if view dataset details:
            $("#datatable h1").text("Metadata for " + dataResourceSummary.title);
        }
        
        var tmpl_str = $("#template-dataset-details").html();

        if (resp.dimensions) {
            var dims = "<h3>Dimensions:</h3>";
        
            $.each(resp.dimensions, function(i, e){ 
                dims += "<div>" + e.name + " = " +  e.length;
            });
        }

        if (resp.variable) {
            var vars = "<h3>Variables:</h3>";
            $.each(resp.variable, function(i, obj){ 
                if (obj.dimensions) {
                    var dim_length = obj.dimensions.length;
                    vars += "<div class='vars0'>" + obj.name + "("; 
                    $.each(obj.dimensions, function(i, e){ 
                        if (i == (dim_length - 1)){
                            vars += e.name + "=" + e.length;
                        } else {
                            vars += e.name + "=" + e.length + ", ";
                        }
                    });
                    vars += ")</div>";
                } else {
                    vars += "<div class='vars0'>" + obj.name + "</div>";
                }
                if (obj.units) vars += "<div class='vars1'>Units = " + obj.units + "</div>";
                if (obj.standard_name) vars += "<div class='vars1'>Standard name = " + obj.standard_name + "</div>";
                if (obj.other_attributes){
                    $.each(obj.other_attributes, function(i, e){ 
                        vars += "<div class='vars1'>" + e.name + " = " + e.value + "</div>";
                    });
                }
            });
        }

        if (resp.other_attributes) {
            var other_attrs = "<h3>Other Attributes:</h3>"
            $.each(resp.other_attributes, function(i, obj){ 
                other_attrs += "<div class='other-attributes'>" + obj.name + " = " + obj.value + "</div>";
            });
        }
        var tmpl_vals = {
            ion_title:source.ion_title, ion_description:source.ion_description, visualization_url:source.visualization_url,
            ion_name:source.ion_name, ion_email:source.ion_email, ion_institution:source.ion_institution,
            title:dataResourceSummary.title, institution:dataResourceSummary.institution,
            ion_geospatial_lat_min:dataResourceSummary.ion_geospatial_lat_min, ion_geospatial_lat_max:dataResourceSummary.ion_geospatial_lat_max,
            ion_geospatial_lon_min:dataResourceSummary.ion_geospatial_lon_min, ion_geospatial_lon_max:dataResourceSummary.ion_geospatial_lon_max,
            ion_geospatial_vertical_min:dataResourceSummary.ion_geospatial_vertical_min, 
            ion_geospatial_vertical_max:dataResourceSummary.ion_geospatial_vertical_max, 
            ion_geospatial_vertical_positive:dataResourceSummary.ion_geospatial_vertical_positive, 
            ion_time_coverage_start:dataResourceSummary.ion_time_coverage_start, 
            ion_time_coverage_end:dataResourceSummary.ion_time_coverage_end,base_url:dataResourceSummary.base_url,
            source:dataResourceSummary.source, references:dataResourceSummary.references, station_id:dataResourceSummary.station_id,
            dimensions:dims, variables:vars, other_attributes:other_attrs
        }
        var html = _.template(tmpl_str, tmpl_vals);
        html += "<h3>Dataset Id:</h3><div>"+data_resource_id+"</div><br>";
        $("#datatable_details_container").html(html).removeClass().addClass(data_resource_id);
    },

    format_variables:function(data){
        html = "<div class='detail'><strong>Variables</strong>";
        $.each(data, function(v){
            var vari = data[v];
            var var_string = (vari.name?vari.name:"N/A") + " (" + (vari.units?vari.units:"N/A") +") [" + (vari.standard_name?vari.standard_name:"N/A") +"]";
            html += "<div>"+var_string+"</div>";
        });
        html += "</div>";
        return html;
    },

    dataset_sidebar: function(resp, self){
        var data = resp.dataResourceSummary;
        $(self.datatable.fnSettings().aoData).each(function () {
           $(this.nTr).removeClass('row_selected');
        });
        // Expands right pane panels when row is selected. Also closes panels if already expanded.
        if(!$("h3.data_sources").hasClass("ui-state-active")){
             $('h3.data_sources').trigger('click');
        }
        $('h3.data_sources span').removeClass("ui-icon-triangle-1-e").addClass("ui-icon-triangle-1-s");
		if (resp.source) {
			var ds_title = "<b>Title:</b> "+resp.source.ion_title+"<br><br><b>Description:</b><br>"+resp.source.ion_description+"<br><br><b>Visualization URL:</b><br><a style='text-decoration:underline' target='_blank' href='"+resp.source.visualization_url+"'>"+resp.source.visualization_url+"</a>";
			$("#ds_title").html(ds_title);
			var ds_publisher_contact = "<b>Contact Name:</b> "+resp.source.ion_name+"<br><b>Contact Email:</b>"+resp.source.ion_email+"<br><b>Contact Institution:</b>"+resp.source.ion_institution;
			$("#ds_publisher_contact").html(ds_publisher_contact);
		}

        var ds_source = "<b>Title:</b> "+data.title+"<br><br><b>Description:</b><br>"+data.summary;
        $("#ds_source").html(ds_source);
        var ds_source_contact = "<br><b>Contact Institution:</b>"+data.institution;
        $("#ds_source_contact").html(ds_source_contact);
        $("#ds_variables").html(self.format_variables(resp.variable || {}));
        var geo_html = this.format_geospatial(data);
        $("#ds_geospatial_coverage").html(geo_html); 
        $("#ds_temporal_coverage").html(data.ion_time_coverage_start + " - "+data.ion_time_coverage_end);
        $("#ds_references").html("<a style='text-decoration:underline' target='_blank' href='"+data.references+"'>"+data.references+"</a>");
        $(".data_sources").show();
        $(".notification_settings, .dispatcher_settings").hide();
        $("#download_dataset_button, #setup_notifications").removeAttr("disabled");
        if (OOI_ROLES.length === 0) { //Guest User
            $("#setup_notifications").attr("disabled", "disabled");
        }
        $("#download_dataset_button").unbind('click').click(function(e) {
            var model = self.controller.resource_collection.get_by_dataset_id(resp.data_resource_id);
            var url = model.get("datasetMetadata").download_url;
            window.open(url);
	    });
        self.controller.loading_dialog();
        $(".my_resources_sidebar").hide();
    },

    format_geospatial:function(data){
        var tmpl_str = $("#template-bounding-box").html();
        var tmpl_vals = {
            "north":data.ion_geospatial_lat_max, "south":data.ion_geospatial_lat_min, 
            "east":data.ion_geospatial_lon_min, "west":data.ion_geospatial_lon_max,
            "upper":data.ion_geospatial_vertical_max, "lower":data.ion_geospatial_vertical_min,
            "vertical_positive":data.ion_geospatial_vertical_positive
        };
        var html = _.template(tmpl_str, tmpl_vals);
        return html;
    },

    populate_table: function(){
        this.controller.loading_dialog("Loading datasets...");
        this.datatable.fnClearTable();
        var datatable_id = this.datatable.attr("id");
        var self = this;
        var geo_data = this.controller.geospatial_container.get_form_data();
        var data = $.extend(geo_data, {"action":"find"});
        $.ajax({url:"dataResource", type:"GET", data:data, dataType:"json",
            success: function(data){
                self.controller.resource_collection.remove_all();
                if (typeof data.dataResourceSummary === "undefined"){
                    data["dataResourceSummary"] = [];
                }
                $.each(data.dataResourceSummary, function(i, elem){
                    self.controller.resource_collection.add(elem);
                    var new_date = new Date(elem.date_registered);
                    var pretty_date = new_date.getFullYear()+"-"+(new_date.getMonth()+1)+"-"+new_date.getDate();
                    var details_image = "<img class='dataset_details' title='Show details' src='images/I1136-Details-List.png'>";
                    var notification_check = elem.notificationSet ? "<img class='dataset_details' src='images/G1110-Checkbox-Tick-White.png'>" : "";
                    self.datatable.fnAddData([elem.datasetMetadata.title, notification_check, elem.datasetMetadata.institution, elem.datasetMetadata.source, pretty_date, details_image]);
                    $($("#datatable_100").dataTable().fnGetNodes(i)).attr("id", elem.datasetMetadata.data_resource_id);
                });
                self.controller.loading_dialog();
            },
            error:function(xhr, ajaxOptions, thrownError){
                self.controller.error_dialog("dataResource", xhr.status, xhr.responseText);
            }
        });
    },

    presentation: function(){
        $("h3.data_sources").filter(".ui-state-active").trigger("click");
		$(".dataTables_wrapper").hide();
        $("#datatable_100_wrapper").show();
        $("#datatable_select_buttons").hide();
        $(".east-south button").hide();
        $("#datatable_details_container").hide();
        $("#datatable h1").text("All Registered Resources");
        $(".notification_settings, .dispatcher_settings, .user_settings").hide();
        $("#datatable_details_scroll").hide();
        $("#geospatial_selection_button").show();
        $("#download_dataset_button, #setup_notifications").show().attr("disabled", "disabled");
		$('.instrument_agent').hide();
        $("h3.data_sources").show();
        $("#save_notifications_changes, #notification_settings, #dispatcher_settings").hide()
        $(".my_resources_sidebar").hide();
    }
});

OOI.Views.Workflow104 = Backbone.View.extend({
    /*
        My Notification Settings.
    */
    events: {
        "click #datatable_104 tbody tr":"show_detail_clicked",
        "click #setup_notifications":"setup_notifications", //XXX part of Workflow100 really...
        "click #start_notifications":"start_notifications",
        "click #save_notifications_changes":"save_notifications_changes",
    },

    initialize: function() {
        _.bindAll(this, "render", "show_detail_clicked", "setup_notifications", "start_notifications", "save_notifications_changes"); 
        this.controller = this.options.controller;
        this.datatable = this.controller.datatable_init("#datatable_104", 4);
        var self = this;
    },

    render: function() {
        this.presentation();
        this.populate_table();
        return this;
    },
 

    populate_table: function(){
        this.controller.loading_dialog("Loading notifications...");
        this.datatable.fnClearTable();
        var datatable_id = this.datatable.attr("id");
        var self = this;
        var geo_data = this.controller.geospatial_container.get_form_data();
        var data = $.extend(geo_data, {"action":"find"});
        $.ajax({url:"subscription", type:"GET", data:data, dataType:"json",
            success: function(data){
                var cb = "<input type='checkbox'/>";
                self.controller.my_notifications_collection.remove_all();
                if (typeof data.subscriptionListResults === "undefined"){
                    data["subscriptionListResults"] = [];
                }
                $.each(data.subscriptionListResults, function(i, elem){
                    var model_info = $.extend({}, elem.datasetMetadata, elem.subscriptionInfo);
                    self.controller.my_notifications_collection.add(model_info);
                    var new_date = new Date(elem.subscriptionInfo.date_registered);
                    var pretty_date = new_date.getFullYear()+"-"+(new_date.getMonth()+1)+"-"+new_date.getDate();
                    self.datatable.fnAddData([cb, elem.datasetMetadata.title, elem.datasetMetadata.source, pretty_date]);
                    $($("#datatable_104").dataTable().fnGetNodes(i)).attr("id", elem.subscriptionInfo.data_src_id);
                });
                $("#datatable_select_buttons").show();
                self.controller.loading_dialog();
            },
            error:function(xhr, ajaxOptions, thrownError){
                self.controller.error_dialog("dataResource", xhr.status, xhr.responseText);
            }
        });
    },

    setup_notifications: function(){
        $(".notification_settings input[type='checkbox']").removeAttr("checked");//clear out old vals
        $(".dispatcher_settings input[type='checkbox']").removeAttr("checked");//clear out old vals
        $("#dispatcher_script_path").val("");//clear out old vals
        $("#start_notifications, .notification_settings, .dispatcher_settings").show();
        $("#start_notifications").attr("disabled", "disabled");
        $("#download_dataset_button, #setup_notifications").hide();
        $(".data_sources").hide();
        var is_early_adopter = _.any(OOI_ROLES, function(role){return role === "EARLY_ADOPTER"});
        if (!is_early_adopter){
            $(".dispatcher_settings").hide();
        }
    },

    show_detail_clicked: function(e){
        if ( $(e.target).hasClass("dataTables_empty") ) return;
        $(".notification_settings, .dispatcher_settings").show();
        $("#eastMultiOpenAccordion .accordion-inactive").removeClass("accordion-inactive");
        var tr = $(e.target);
        if ( tr.hasClass("dataTables_empty") ) return;
        $(".notification_settings, .dispatcher_settings").show();
        var data_resource_id = tr.parent().attr("id"); 
        if (data_resource_id == ""){
            data_resource_id = tr.parent().parent().attr("id");  //click on the checkbox
        }
        $("#datatable_104 tr").removeClass("selected");
        tr.parent().addClass("selected");
        var model = this.controller.my_notifications_collection.get_by_dataset_id(data_resource_id);
        var subscription_type = model.get("subscription_type");
        var email_alerts_filter = model.get("email_alerts_filter");
        var dispatcher_alerts_filter = model.get("dispatcher_alerts_filter");
        var dispatcher_script_path = model.get("dispatcher_script_path");
        if (subscription_type === "EMAIL" || subscription_type === "EMAILANDDISPATCHER"){
            $("#updateWhenAvailable, #datasourceIsOffline").removeAttr("checked");
            switch (email_alerts_filter){
                case "UPDATES":
                    $("#updateWhenAvailable").attr("checked", "checked");
                    break;
                case "DATASOURCEOFFLINE":
                    $("#datasourceIsOffline").attr("checked", "checked");
                    break;
                case "UPDATESANDDATASOURCEOFFLINE":
                    $("#updateWhenAvailable").attr("checked", "checked");
                    $("#datasourceIsOffline").attr("checked", "checked");
                    break;
                default:
                    break;
            }
        }
        if (subscription_type === "DISPATCHER" || subscription_type === "EMAILANDDISPATCHER"){
            $("#dispatcher_script_path").val(dispatcher_script_path).attr("style", "");
            $("#dispatcher_updateWhenAvailable, #dispatcher_datasourceIsOffline").removeAttr("checked");
            switch (dispatcher_alerts_filter){
                case "UPDATES":
                    $("#dispatcher_updateWhenAvailable").attr("checked", "checked");
                    break;
                case "DATASOURCEOFFLINE":
                    $("#dispatcher_datasourceIsOffline").attr("checked", "checked");
                    break;
                case "UPDATESANDDATASOURCEOFFLINE":
                    $("#dispatcher_updateWhenAvailable").attr("checked", "checked");
                    $("#dispatcher_datasourceIsOffline").attr("checked", "checked");
                    break;
                default:
                    break;
            }
        }
        if ($("div.notification_settings").is(":hidden")){
            $("#notification_settings, #dispatcher_settings").trigger("click");
        }
        var is_early_adopter = _.any(OOI_ROLES, function(role){return role === "EARLY_ADOPTER"});
        if (!is_early_adopter){
            $(".dispatcher_settings").hide();
        }
    },

    start_notifications: function(){
        this.controller.loading_dialog("Saving Notification...");
        if (window.location.hash === "#notifications"){
            var data_resource_id = $("#datatable_104 tr.selected").attr("id");
            var model = this.controller.my_notifications_collection.get_by_dataset_id(data_resource_id);
        } else { //XXX mixing Workflow100 with Workflow104 right here:
            var data_resource_id = $("#datatable_100 tr.selected").attr("id");
            var model = this.controller.resource_collection.get_by_dataset_id(data_resource_id);
        }
        var subscription_type = "", email_alerts_filter = "", dispatcher_alerts_filter = "";
        if ($("#updateWhenAvailable").is(":checked") && !$("#datasourceIsOffline").is(":checked")) email_alerts_filter = "UPDATES";
        if (!$("#updateWhenAvailable").is(":checked") && $("#datasourceIsOffline").is(":checked")) email_alerts_filter = "DATASOURCEOFFLINE";
        if ($("#updateWhenAvailable").is(":checked") && $("#datasourceIsOffline").is(":checked")) email_alerts_filter = "UPDATESANDDATASOURCEOFFLINE";

        if ($("#dispatcher_updateWhenAvailable").is(":checked") && !$("#dispatcher_datasourceIsOffline").is(":checked")) dispatcher_alerts_filter = "UPDATES";
        if (!$("#dispatcher_updateWhenAvailable").is(":checked") && $("#dispatcher_datasourceIsOffline").is(":checked")) dispatcher_alerts_filter = "DATASOURCEOFFLINE";
        if ($("#dispatcher_updateWhenAvailable").is(":checked") && $("#dispatcher_datasourceIsOffline").is(":checked")) dispatcher_alerts_filter = "UPDATESANDDATASOURCEOFFLINE";

        if (email_alerts_filter !== "" && dispatcher_alerts_filter === "") subscription_type = "EMAIL";
        if (email_alerts_filter == "" && dispatcher_alerts_filter !== "") subscription_type = "DISPATCHER";
        if (email_alerts_filter !== "" && dispatcher_alerts_filter !== "") subscription_type = "EMAILANDDISPATCHER";
        var dispatcher_script_path = $("#dispatcher_script_path").val();

        var modelJSON = model.toJSON();
        var datasetMetadata = modelJSON.datasetMetadata;
        var datasetMetadataJson = JSON.stringify(datasetMetadata);
        var subscriptionInfo = {"data_src_id":data_resource_id, "subscription_type":subscription_type, "email_alerts_filter":email_alerts_filter, "dispatcher_alerts_filter":dispatcher_alerts_filter, "dispatcher_script_path":dispatcher_script_path};
        var subscriptionInfoJson = JSON.stringify(subscriptionInfo);
        var data = {"action":"create", "subscriptionInfo":subscriptionInfoJson, "datasetMetadata": datasetMetadataJson};
        var self = this;
        $.ajax({url:"subscription", type:"POST", data:data, 
            success: function(resp){
                self.controller.loading_dialog();
                alert("Notification saved");
                //setTimeout(function(){document.location="/";}, 100);
            },
            error:function(xhr, ajaxOptions, thrownError){
                self.controller.error_dialog("dataResource", xhr.status, xhr.responseText);
            }
        });
    },

    save_notifications_changes: function(){
        this.controller.loading_dialog("Saving Notification...");
        var data_resource_id = $("#datatable_104 tr.selected").attr("id");
        var model = this.controller.my_notifications_collection.get_by_dataset_id(data_resource_id);
        var subscription_type = "", email_alerts_filter = "", dispatcher_alerts_filter = "";
        if ($("#updateWhenAvailable").is(":checked") && !$("#datasourceIsOffline").is(":checked")) email_alerts_filter = "UPDATES";
        if (!$("#updateWhenAvailable").is(":checked") && $("#datasourceIsOffline").is(":checked")) email_alerts_filter = "DATASOURCEOFFLINE";
        if ($("#updateWhenAvailable").is(":checked") && $("#datasourceIsOffline").is(":checked")) email_alerts_filter = "UPDATESANDDATASOURCEOFFLINE";

        if ($("#dispatcher_updateWhenAvailable").is(":checked") && !$("#dispatcher_datasourceIsOffline").is(":checked")) dispatcher_alerts_filter = "UPDATES";
        if (!$("#dispatcher_updateWhenAvailable").is(":checked") && $("#dispatcher_datasourceIsOffline").is(":checked")) dispatcher_alerts_filter = "DATASOURCEOFFLINE";
        if ($("#dispatcher_updateWhenAvailable").is(":checked") && $("#dispatcher_datasourceIsOffline").is(":checked")) dispatcher_alerts_filter = "UPDATESANDDATASOURCEOFFLINE";

        if (email_alerts_filter !== "" && dispatcher_alerts_filter === "") subscription_type = "EMAIL";
        if (email_alerts_filter == "" && dispatcher_alerts_filter !== "") subscription_type = "DISPATCHER";
        if (email_alerts_filter !== "" && dispatcher_alerts_filter !== "") subscription_type = "EMAILANDDISPATCHER";
        var dispatcher_script_path = $("#dispatcher_script_path").val();

        var modelJSON = model.toJSON();
        var datasetMetadataJson = JSON.stringify(modelJSON);
        var subscriptionInfo = {"data_src_id":data_resource_id, "subscription_type":subscription_type, "email_alerts_filter":email_alerts_filter, "dispatcher_alerts_filter":dispatcher_alerts_filter, "dispatcher_script_path":dispatcher_script_path};
        var subscriptionInfoJson = JSON.stringify(subscriptionInfo);
        var data = {"action":"update", "subscriptionInfo":subscriptionInfoJson, "datasetMetadata": datasetMetadataJson};
        var self = this;
        $.ajax({url:"subscription", type:"POST", data:data, 
            success: function(resp){
                self.controller.loading_dialog();
                alert("Notification saved");
                //setTimeout(function(){document.location="/";}, 100);
            },
            error:function(xhr, ajaxOptions, thrownError){
                self.controller.error_dialog("dataResource", xhr.status, xhr.responseText);
            }
        });
    },

	presentation: function(){
		$(".dataTables_wrapper").hide();
        $("#datatable_104_wrapper").show();
        $(".notification_settings, .dispatcher_settings, .user_settings").hide();
        $(".notification_settings").hide();
        $("#datatable_details_container, #datatable_details_scroll").hide();
        $("#datatable h1").text("My Notification Settings");
        $(".data_sources").hide();
		$('.instrument_agent').hide();
        $("#geospatial_selection_button").hide();
        $(".east-south button").hide();
        $("#save_notifications_changes, #notification_settings, #dispatcher_settings").show()
        var is_early_adopter = _.any(OOI_ROLES, function(role){return role === "EARLY_ADOPTER"});
        if (!is_early_adopter){
            $(".early_adopter").hide();
        }
    }

});


OOI.Views.Workflow105 = Backbone.View.extend({
    /*
        Register New Resource.
    */
    events: {
        "click .resource_selector_tab":"resource_selector",
        "click #register_resource_button":"register_resource"
    },

    initialize: function() { 
        _.bindAll(this, "render", "resource_selector", "register_resource"); 
        this.controller = this.options.controller;
    },

    render: function() {
        return this;
    },

	show_validate_response: function($el, data_resource_url, data) {
		var self = this;

		$el.find('.loading:first').hide();
		var $result = $el.find('.result:first').show().find('.value:first');

		if (_.size(data) == 0 || data.error_str) {
			$result.val('Failure');
			$el.find('.error-msg:first').show().find('.value:first').text(data.error_str);

			$el.find('.validate-ok:first').hide();
		} else {
			$result.val('Success');

			var cdm = data.cdmResponse;
			if (cdm) {
				if (cdm.cf_info_count !== undefined)		$el.find('.cf-info').show().find('.value:first').val(cdm.cf_info_count);
				if (cdm.cf_warning_count !== undefined)		$el.find('.cf-warning').show().find('.value:first').val(cdm.cf_warning_count);
				if (cdm.cf_error_count !== undefined)		$el.find('.cf-error').show().find('.value:first').val(cdm.cf_error_count);
				if (cdm.cf_output !== undefined)			$el.find('.cf-output').show().find('.value:first').text(cdm.cf_output);
				if (cdm.cdm_output !== undefined)			$el.find('.cdm-output').show().find('.value:first').text(cdm.cdm_output);
			}

			$el.find('.validate-ok:first').show().one('click', function(e) {
				e.preventDefault();
				$.fn.colorbox.close();

				//window.location.href = '#register-dataset/' + encodeURIComponent(data_resource_url);

				$("#datatable_details_container").show();
				$(".dataTables_wrapper").hide();
				$('.east-south button').hide();
				$('#save_register_resource').show();
                $("#eastMultiOpenAccordion .accordion-inactive").removeClass("accordion-inactive");

				 $('#save_register_resource').data('dataset_url', data_resource_url);
				var data_resource_id = 'register-dataset';
				self.show_detail_all(data, data_resource_id);
				var workflow = self.controller.workflow106;
                workflow.dataset_sidebar(data, data_resource_id, workflow);
			});
		}
	},

    show_detail_all:function(data, data_resource_id){
        var dataResourceSummary = data.dataResourceSummary;

        if ( $("#datatable_details_container").is(":visible") ) { //only if view dataset details:
            $("#datatable h1").text("Metadata for " + dataResourceSummary.title);
        }
        
        var tmpl_str = $("#template-dataset-details-resourcesummary").html();

        var tmpl_vals = {
            title:dataResourceSummary.title, institution:dataResourceSummary.institution,
            ion_geospatial_lat_min:dataResourceSummary.ion_geospatial_lat_min, ion_geospatial_lat_max:dataResourceSummary.ion_geospatial_lat_max,
            ion_geospatial_lon_min:dataResourceSummary.ion_geospatial_lon_min, ion_geospatial_lon_max:dataResourceSummary.ion_geospatial_lon_max,
            ion_geospatial_vertical_min:dataResourceSummary.ion_geospatial_vertical_min, 
            ion_geospatial_vertical_max:dataResourceSummary.ion_geospatial_vertical_max, 
            ion_geospatial_vertical_positive:dataResourceSummary.ion_geospatial_vertical_positive, 
            ion_time_coverage_start:dataResourceSummary.ion_time_coverage_start, 
            ion_time_coverage_end:dataResourceSummary.ion_time_coverage_end,base_url:dataResourceSummary.base_url,
            summary:dataResourceSummary.summary, references:dataResourceSummary.references, station_id:dataResourceSummary.station_id,
        }
        var html = _.template(tmpl_str, tmpl_vals);
        $("#datatable_details_container").html(html).removeClass().addClass(data_resource_id);
    },

    register_resource:function(){
		var selector = '#validate-resource-dialog', $el = $(selector);
		$el.find('.field').hide();
		$el.find('.loading:first').show();
		$.colorbox({
            inline: true,
            href: selector,
            transition: 'none',
            opacity: 0.7
        });

        var data_resource_url = $.trim($("#data_resource_url").val());
        // var visualization_url = $("#visualization_url").val();
        // $.ajax({url:"dataResource", type:"POST", data:{action:"validate", "data_resource_url":data_resource_url, "visualization_url":visualization_url}, dataType:"json",
        $.ajax({url:"dataResource", type:"POST", data:{action:"validate", "data_resource_url":data_resource_url}, dataType:"json",
            success: _.bind(function(data){
				this.show_validate_response($el, data_resource_url, data);
            }, this),
            error: _.bind(function(xhr){
				var data = JSON.parse(xhr.responseText);
				this.show_validate_response($el, data_resource_url, data);
            }, this)
        });


		// Dummy data to test with when the backend is borked
		//var goodResponse = {"dataResourceSummary": {"title": "WHOTS 7 near-real-time Mooring Data, System 1","institution": "WHOI","references": "http:// www.oceansites.org, http://uop.whoi.edu/projects/whots","summary": "Near-real-time surface data from ASIMet system 1 on the seventh deployment of the WHOI HOT Station (WHOTS) observatory.","comment": "Argos, hourly averaged ASIMet data","ion_time_coverage_start": "2010-07-29T06:00:00Z","ion_time_coverage_end": "2011-05-16T13:00:00Z","ion_geospatial_lat_min": 22.75,"ion_geospatial_lat_max": 22.75,"ion_geospatial_lon_min": -158.0,"ion_geospatial_lon_max": -158.0,"ion_geospatial_vertical_min": -1.0,"ion_geospatial_vertical_max": 2.0,"ion_geospatial_vertical_positive": "True"},"cdmResponse": {"response_type": "PASS","cf_output": "\nCHECKING NetCDF FILE: http://geoport.whoi.edu/thredds/dodsC/usgs/data0/rsignell/data/oceansites/OS_WHOTS_2010_R_M-1.nc\n=====================\nUsing CF Checker Version 2.0.2\nUsing Standard Name Table Version 16 (2010-10-11T12:16:51Z)\nUsing Area Type Table Version 1 (5 December 2008)\n\n\n------------------\nChecking variable: UWND\n------------------\n\n------------------\nChecking variable: TEMP\n------------------\n\n------------------\nChecking variable: VWND\n------------------\n\n------------------\nChecking variable: SW\n------------------\n\n------------------\nChecking variable: longitude\n------------------\nINFO: attribute \'comment\' is being used in a non-standard way\nINFO: attribute \'_FillValue\' is being used in a non-standard way\n\n------------------\nChecking variable: ATMS\n------------------\n\n------------------\nChecking variable: PSAL\n------------------\n\n------------------\nChecking variable: LW\n------------------\n\n------------------\nChecking variable: depth\n------------------\nINFO: attribute \'comment\' is being used in a non-standard way\n\n------------------\nChecking variable: RELH\n------------------\n\n------------------\nChecking variable: AIRT\n------------------\n\n------------------\nChecking variable: time\n------------------\nWARNING (4.4.1): Use of the calendar and/or month_lengths attributes is recommended for time coordinate variables\n\n------------------\nChecking variable: latitude\n------------------\nINFO: attribute \'comment\' is being used in a non-standard way\nINFO: attribute \'_FillValue\' is being used in a non-standard way\n\n------------------\nChecking variable: RAIT\n------------------\n\nERRORS detected: 0\nWARNINGS given: 1\nINFORMATION messages: 5\n","cdm_output": "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<netcdfDatasetInfo location=\"dods://geoport.whoi.edu/thredds/dodsC/usgs/data0/rsignell/data/oceansites/OS_WHOTS_2010_R_M-1.nc\">\r\n  <convention name=\"CF-1.4\" />\r\n  <axis name=\"time\" decl=\"double time(time)\" type=\"Time\" units=\"days since 1950-01-01T00:00:00Z\" udunits=\"date\" regular=\".041666\" />\r\n  <axis name=\"depth\" decl=\"float depth(depth)\" type=\"Height\" units=\"meters\" udunits=\"m\" regular=\".0\" />\r\n  <axis name=\"latitude\" decl=\"float latitude(latitude)\" type=\"Lat\" units=\"degrees_north\" udunits=\"0.017453292519943295 rad\" regular=\".0\" />\r\n  <axis name=\"longitude\" decl=\"float longitude(longitude)\" type=\"Lon\" units=\"degrees_east\" udunits=\"0.017453292519943295 rad\" regular=\".0\" />\r\n  <coordSystem name=\"time depth\" />\r\n  <variable name=\"time\" decl=\"double time(time)\" units=\"days since 1950-01-01T00:00:00Z\" udunits=\"date\" coordSys=\" \" />\r\n  <variable name=\"depth\" decl=\"float depth(depth)\" units=\"meters\" udunits=\"m\" coordSys=\" \" />\r\n  <variable name=\"latitude\" decl=\"float latitude(latitude)\" units=\"degrees_north\" udunits=\"0.017453292519943295 rad\" coordSys=\" \" />\r\n  <variable name=\"longitude\" decl=\"float longitude(longitude)\" units=\"degrees_east\" udunits=\"0.017453292519943295 rad\" coordSys=\" \" />\r\n  <variable name=\"AIRT\" decl=\"float AIRT(time, depth)\" units=\"degree_C\" udunits=\"(K) @ 273.15\" coordSys=\"time depth\" />\r\n  <variable name=\"ATMS\" decl=\"float ATMS(time, depth)\" units=\"millibars\" udunits=\"100.0 Pa\" coordSys=\"time depth\" />\r\n  <variable name=\"RELH\" decl=\"float RELH(time, depth)\" units=\"percent\" udunits=\"0.01 \" coordSys=\"time depth\" />\r\n  <variable name=\"LW\" decl=\"float LW(time, depth)\" units=\"W m-2\" udunits=\"kg.s-3\" coordSys=\"time depth\" />\r\n  <variable name=\"RAIT\" decl=\"float RAIT(time, depth)\" units=\"mm\" udunits=\"0.0010 m\" coordSys=\"time depth\" />\r\n  <variable name=\"TEMP\" decl=\"float TEMP(time, depth)\" units=\"degree_C\" udunits=\"(K) @ 273.15\" coordSys=\"time depth\" />\r\n  <variable name=\"SW\" decl=\"float SW(time, depth)\" units=\"W m-2\" udunits=\"kg.s-3\" coordSys=\"time depth\" />\r\n  <variable name=\"UWND\" decl=\"float UWND(time, depth)\" units=\"meters/second\" udunits=\"m.s-1\" coordSys=\"time depth\" />\r\n  <variable name=\"VWND\" decl=\"float VWND(time, depth)\" units=\"meters/second\" udunits=\"m.s-1\" coordSys=\"time depth\" />\r\n  <variable name=\"PSAL\" decl=\"float PSAL(time, depth)\" units=\"1\" udunits=\"1.0 \" coordSys=\"time depth\" />\r\n  <userAdvice>No gridded data variables were found.</userAdvice>\r\n</netcdfDatasetInfo>\r\n\r\n","cf_error_count": 0,"cf_warning_count": 1,"cf_info_count": 5,"err_msg": ""}};
		//var badResponse = {"error_num": 500,"error_str": "ValidateDataResource.validate(): INVALID: CF compliance failed x5  :: cf_output: \nCHECKING NetCDF FILE: http://hfrnet.ucsd.edu:8080/thredds/dodsC/HFRNet/USEGC/6km/hourly/RTV\n=====================\nUsing CF Checker Version 2.0.2\nUsing Standard Name Table Version 16 (2010-10-11T12:16:51Z)\nUsing Area Type Table Version 1 (5 December 2008)\n\nWARNING: Inconsistency - The conventions attribute is set to CF-1.1, but you\'ve requested a validity check against CF 1.4\n\n------------------\nChecking variable: v\n------------------\n\n------------------\nChecking variable: DOPx\n------------------\nERROR (3.1): No units attribute set\n\n------------------\nChecking variable: site_code\n------------------\nERROR (3.1): No units attribute set\n\n------------------\nChecking variable: site_lon\n------------------\n\n------------------\nChecking variable: lon\n------------------\n\n------------------\nChecking variable: procParams\n------------------\nERROR (3.1): No units attribute set\n\n------------------\nChecking variable: u\n------------------\n\n------------------\nChecking variable: DOPy\n------------------\nERROR (3.1): No units attribute set\n\n------------------\nChecking variable: time\n------------------\n\n------------------\nChecking variable: lat\n------------------\n\n------------------\nChecking variable: site_netCode\n------------------\nERROR (3.1): No units attribute set\n\n------------------\nChecking variable: site_lat\n------------------\n\nERRORS detected: 5\nWARNINGS given: 1\nINFORMATION messages: 0\n :: cdm_output: <?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<netcdfDatasetInfo location=\"dods://hfrnet.ucsd.edu:8080/thredds/dodsC/HFRNet/USEGC/6km/hourly/RTV\">\r\n  <convention name=\"CF-1.1\" />\r\n  <axis name=\"lat\" decl=\"float lat(lat)\" type=\"Lat\" units=\"degrees_north\" udunits=\"0.017453292519943295 rad\" regular=\".053939\" />\r\n  <axis name=\"lon\" decl=\"float lon(lon)\" type=\"Lon\" units=\"degrees_east\" udunits=\"0.017453292519943295 rad\" regular=\".058075\" />\r\n  <axis name=\"time\" decl=\"int time(time)\" type=\"Time\" units=\"seconds since 1970-01-01\" udunits=\"date\" />\r\n  <gridCoordSystem name=\"time lat lon\" horizX=\"lon\" horizY=\"lat\" time=\"time\" />\r\n  <grid name=\"u\" decl=\"short u(time, lat, lon)\" units=\"m s-1\" udunits=\"m.s-1\" coordSys=\"time lat lon\" />\r\n  <grid name=\"v\" decl=\"short v(time, lat, lon)\" units=\"m s-1\" udunits=\"m.s-1\" coordSys=\"time lat lon\" />\r\n  <grid name=\"DOPx\" decl=\"short DOPx(time, lat, lon)\" coordSys=\"time lat lon\" />\r\n  <grid name=\"DOPy\" decl=\"short DOPy(time, lat, lon)\" coordSys=\"time lat lon\" />\r\n  <variable name=\"lat\" decl=\"float lat(lat)\" units=\"degrees_north\" udunits=\"0.017453292519943295 rad\" coordSys=\" \" />\r\n  <variable name=\"lon\" decl=\"float lon(lon)\" units=\"degrees_east\" udunits=\"0.017453292519943295 rad\" coordSys=\" \" />\r\n  <variable name=\"site_lat\" decl=\"float site_lat(nSites)\" units=\"degrees_north\" udunits=\"0.017453292519943295 rad\" coordSys=\" \" />\r\n  <variable name=\"site_lon\" decl=\"float site_lon(nSites)\" units=\"degrees_east\" udunits=\"0.017453292519943295 rad\" coordSys=\" \" />\r\n  <variable name=\"site_code\" decl=\"char site_code(nSites, &quot;nSites_maxStrlen&quot;)\" coordSys=\" \" />\r\n  <variable name=\"site_netCode\" decl=\"char site_netCode(nSites, &quot;nSites_maxStrlen&quot;)\" coordSys=\" \" />\r\n  <variable name=\"procParams\" decl=\"float procParams(nProcParam)\" coordSys=\" \" />\r\n  <variable name=\"time\" decl=\"int time(time)\" units=\"seconds since 1970-01-01\" udunits=\"date\" coordSys=\" \" />\r\n  <userAdvice>Dataset contains useable gridded data.</userAdvice>\r\n  <userAdvice>Some variables are not gridded fields; check that is what you expect.</userAdvice>\r\n</netcdfDatasetInfo>\r\n\r\n :: err_msg:  "};
		//this.show_validate_response(goodResponse);
    },

    resource_selector: function(e){
        var id = $(e.target).attr("id");
        $("#"+id).addClass("selected");
        if (id == "view_existing_tab"){
            $("#geospatial_selection_button, #view_existing, .view_existing, .instrument_view").show();
            $("#register_new, #register_resource_button").hide();
            $("#register_new_tab").removeClass("selected");
        } else {
            $("#register_new, #register_resource_button").show();
            $("#geospatial_selection_button, #view_existing, .view_existing, .instrument_view").hide();
            $("#view_existing_tab").removeClass("selected");
        }
        //TODO: this 'role' type logic needs to be changed to a class based switch strategy:
        var is_admin = _.any(OOI_ROLES, function(role){return role === "ADMIN"});
        if (!is_admin){
            $(".admin_role").hide();
        }
		var is_marine_op = _.any(OOI_ROLES, function(role){return role === "MARINE_OPERATOR"});
        if (!is_marine_op){
            $(".marine_op_role").hide();
        }
    }

});

OOI.Views.ResourceActions = Backbone.View.extend({
    /*
        Handles resource actions like saving,
        initiated by click on certain buttons.
    */

    events: {
        "click #save_myresources_changes":"save_myresources_changes",
		"click #save_register_resource":"save_register_resource",
    },

    initialize: function() {
        _.bindAll(this, "save_myresources_changes"); 
        this.validate_polling_interval();
        this.controller = this.options.controller;
    },

	save_resource: function(action, data_set_resource_id, dataset_url, callback) {
		if ($("#polling_radio_yes").is(":checked")){
            var polling_vals = $("#polling_time").val().split(":");
            var days = parseInt(polling_vals[0]), hours = parseInt(polling_vals[1]), mins = parseInt(polling_vals[2]);
            var update_interval_seconds = (days * 24 * 60 * 60) + (hours * 60 * 60) + (mins * 60); 
        } else {
            var update_interval_seconds = 0;
        }
        var ion_title = $("#resource_registration_title").val();
        var ion_description = $("#resource_registration_description").val();
        var ion_visualization_url = $("#resource_registration_visualization_url").val();
        if ($("#availability_radio_private").is(":checked")){
            var is_public = false;
        } else {
            var is_public = true;
        }
        var max_ingest_millis = update_interval_seconds * 1000;
        var update_start_datetime_millis = (new Date()).getTime();
        var data = {"action":action, "update_interval_seconds":update_interval_seconds,
            "ion_title":ion_title, "ion_description":ion_description, "visualization_url":ion_visualization_url,
            "is_public":is_public, "max_ingest_millis":max_ingest_millis,
            "update_start_datetime_millis":update_start_datetime_millis};
		if (action === 'create') {
			data['source_type'] = 'NETCDF_S';
			data['request_type'] = 'DAP';
			data['ion_institution_id'] = 'default';
		}
		if (data_set_resource_id !== undefined)  data["data_set_resource_id"] = data_set_resource_id;
		if (dataset_url !== undefined)  data["dataset_url"] = dataset_url;

        this.controller.loading_dialog("Saving Resource Changes...");
        var self = this;
        $.ajax({url:"dataResource", type:"POST", data:data,
            success: function(resp){
                self.controller.loading_dialog();
				if (callback) callback(resp);
            },
            error:function(xhr, ajaxOptions, thrownError){
                self.controller.error_dialog("dataResource", xhr.status, xhr.responseText);
            }
        });
	},

    validate_polling_interval:function(){
        $("#polling_time").bind("keyup", function(e){
            var val = $(e.target).val();
            var nums = val.split(":");
            if (nums.length != 3){
                $(e.target).css("border", "1px solid #ff0000").css("padding", "3px");
                $("#save_myresources_changes").attr("disabled", "disabled");
            } else {
                var n0 = nums[0], n1 = nums[1], n2 = nums[2];
                if ( n0.length != 2 || n0 == "" || isNaN(n0) || n1.length != 2 || n1 == "" || isNaN(n1) || n2.length != 2 || n2 == "" || isNaN(n2)  ){
                    $(e.target).css("border", "1px solid #ff0000").css("padding", "3px");
                    $("#save_myresources_changes").attr("disabled", "disabled");
                } else {
                    $(e.target).attr("style", "");
                    $("#save_myresources_changes").removeAttr("disabled");
                }
            }
        });
        $(".polling_radio").bind("click", function(e){
            var elem_id = $(e.target).attr("id");
            if (elem_id === "polling_radio_yes"){
                $("#polling_time").removeAttr("disabled");
            } else {
                $("#polling_time").attr("disabled", "disabled");
            }
        });
    },

    save_myresources_changes:function(){
        var data_set_resource_id = $("#datatable_106 tr.selected").attr("id");
		this.save_resource('update', data_set_resource_id);
    },

	save_register_resource: function() {
		// TODO: remove this atrocious hack
		var $btn = $('#save_register_resource');
		var url = $btn.data('dataset_url');
		if (url) $btn.removeData('dataset_url');
		this.save_resource('create', undefined, url, function() {
			var selector = '#validate-resource-dialog', $el = $(selector);
		$el.find('.field').hide();
		$el.find('.loading:first').show();
			$.colorbox({ inline: true, href: '#register-resource-complete', transition: 'none', opacity: 0.7 });
			$('#register-resource-complete').find('.modal_close').one('click', function(e) {
				 window.location.reload();
			});
		});
	}
});
    


OOI.Views.Workflow106 = Backbone.View.extend({
    /*
        My Registered Resources.
    */

    events: {
        "click tbody tr":"show_detail_clicked"
    },

    initialize: function() {
        _.bindAll(this, "render", "show_detail", "show_detail_clicked", "show_detail_all");
        this.controller = this.options.controller;
        this.datatable = this.controller.datatable_init("#datatable_106", 7);
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
        var geo_data = this.controller.geospatial_container.get_form_data();
        var data = $.extend(geo_data, {"action":"findByUser"});
        $.ajax({url:"dataResource", type:"GET", data:data, dataType:"json",
            success: function(data){
                self.controller.my_resources_collection.remove_all();
                if (typeof data.datasetByOwnerMetadata === "undefined"){
                    data["datasetByOwnerMetadata"] = [];
                }
                $.each(data.datasetByOwnerMetadata, function(i, elem){
                    self.controller.my_resources_collection.add(elem);
                    var cb = "<input type='checkbox'/>";
                    var new_date = new Date(elem.date_registered);
                    var pretty_date = new_date.getFullYear()+"-"+(new_date.getMonth()+1)+"-"+new_date.getDate();
                    var active = "";
                    var details_image = "<img class='dataset_details' title='Show details' src='images/I1136-Details-List.png'>";
                    if (elem.update_interval_seconds !== 0) active = '<img src="images/G1110-Checkbox-Tick-White.png">';
                    self.datatable.fnAddData([cb, active, elem.activation_state, elem.ion_title, elem.title, pretty_date, details_image]);
                    $($("#datatable_106").dataTable().fnGetNodes(i)).attr("id", elem.data_resource_id);
                });
                $("#datatable_select_buttons").show();
                $("table#datatable_106 tbody tr").not(":first").find("td:not(:first)").css("width", "15%");
                $.each($("table#datatable_106 tbody tr"), function(i, e){$(e).find("td:eq(4)").css("width", "30%")});
                self.controller.loading_dialog();
            },
            error:function(xhr, ajaxOptions, thrownError){
                self.controller.error_dialog("dataResource", xhr.status, xhr.responseText);
            }
        });
    },

    show_detail_clicked: function(e) {
        if ( $(e.target).hasClass("dataTables_empty") ) return;
        var tr_target = $(e.target).parents("tr");
        var data_resource_id = tr_target.attr("id"); 
        $("#datatable_106 tr").removeClass("selected");
        $("#eastMultiOpenAccordion .accordion-inactive").removeClass("accordion-inactive");
        tr_target.addClass("selected");
        if ($(e.target).hasClass("dataset_details")){
            $("#datatable_details_scroll, #datatable_details_container").show();
			$("#datatable_select_buttons, .dataTables_wrapper").hide();
            var nth_elem = tr_target.index();
            window.location.hash += "/"+nth_elem;
        } else {
            this.show_detail(data_resource_id);
        }
    },

    show_detail: function(data_resource_id){
        this.controller.loading_dialog("Loading dataset details...");
        $("#datatable_details_container").html("<p>Loading dataset details...</p>");
        self = this;
        $.ajax({url:"dataResource", type:"GET", dataType:"json", data:{"action":"detail", "data_resource_id":data_resource_id}, 
            success: function(resp){
                self.show_detail_all(resp, data_resource_id);
                self.dataset_sidebar(resp, data_resource_id, self);
            },
            error:function(xhr, ajaxOptions, thrownError){
                self.controller.error_dialog("dataResource", xhr.status, xhr.responseText);
            }
        });
    },

    show_detail_all: function(resp, data_resource_id) {
        var dataResourceSummary = resp.dataResourceSummary, source = resp.source || {};

        if ( $("#datatable_details_container").is(":visible") ) { //only if view dataset details:
            $("#datatable h1").text("Metadata for " + dataResourceSummary.title);
        }
        
        var tmpl_str = $("#template-dataset-details").html();

        if (resp.dimensions) {
            var dims = "<h3>Dimensions:</h3>";
        
            $.each(resp.dimensions, function(i, e){ 
                dims += "<div>" + e.name + " = " +  e.length;
            });
        }

        if (resp.variable) {
            var vars = "<h3>Variables:</h3>";
            $.each(resp.variable, function(i, obj){ 
                if (obj.dimensions) {
                    var dim_length = obj.dimensions.length;
                    vars += "<div class='vars0'>" + obj.name + "("; 
                    $.each(obj.dimensions, function(i, e){ 
                        if (i == (dim_length - 1)){
                            vars += e.name + "=" + e.length;
                        } else {
                            vars += e.name + "=" + e.length + ", ";
                        }
                    });
                    vars += ")</div>";
                } else {
                    vars += "<div class='vars0'>" + obj.name + "</div>";
                }
                if (obj.units) vars += "<div class='vars1'>Units = " + obj.units + "</div>";
                if (obj.standard_name) vars += "<div class='vars1'>Standard name = " + obj.standard_name + "</div>";
                if (obj.other_attributes){
                    $.each(obj.other_attributes, function(i, e){ 
                        vars += "<div class='vars1'>" + e.name + " = " + e.value + "</div>";
                    });
                }
            });
        }

        if (resp.other_attributes) {
            var other_attrs = "<h3>Other Attributes:</h3>"
            $.each(resp.other_attributes, function(i, obj){ 
                other_attrs += "<div class='other-attributes'>" + obj.name + " = " + obj.value + "</div>";
            });
        }
        var tmpl_vals = {
            ion_title:source.ion_title, ion_description:source.ion_description, visualization_url:source.visualization_url,
            ion_name:source.ion_name, ion_email:source.ion_email, ion_institution:source.ion_institution,
            title:dataResourceSummary.title, institution:dataResourceSummary.institution,
            ion_geospatial_lat_min:dataResourceSummary.ion_geospatial_lat_min, ion_geospatial_lat_max:dataResourceSummary.ion_geospatial_lat_max,
            ion_geospatial_lon_min:dataResourceSummary.ion_geospatial_lon_min, ion_geospatial_lon_max:dataResourceSummary.ion_geospatial_lon_max,
            ion_geospatial_vertical_min:dataResourceSummary.ion_geospatial_vertical_min, 
            ion_geospatial_vertical_max:dataResourceSummary.ion_geospatial_vertical_max, 
            ion_geospatial_vertical_positive:dataResourceSummary.ion_geospatial_vertical_positive, 
            ion_time_coverage_start:dataResourceSummary.ion_time_coverage_start, 
            ion_time_coverage_end:dataResourceSummary.ion_time_coverage_end,base_url:dataResourceSummary.base_url,
            source:dataResourceSummary.source, references:dataResourceSummary.references, station_id:dataResourceSummary.station_id,
            dimensions:dims, variables:vars, other_attributes:other_attrs
        }
        var html = _.template(tmpl_str, tmpl_vals);
        html += "<h3>Dataset Id:</h3><div>"+data_resource_id+"</div><br>";
        $("#datatable_details_container").html(html).removeClass().addClass(data_resource_id);
    },

    format_variables:function(data){
        html = "<div class='detail'><strong>Variables</strong>";
        $.each(data, function(v){
            var vari = data[v];
            var var_string = (vari.name?vari.name:"N/A") + " (" + (vari.units?vari.units:"N/A") +") [" + (vari.standard_name?vari.standard_name:"N/A") +"]";
            html += "<div>"+var_string+"</div>";
        });
        html += "</div>";
        return html;
    },

    interval_seconds_pretty: function(seconds){
        // put 'seconds' into form: 'DD:HH:MM'.

        var days_in_seconds = 24 * 60 * 60;
        if (seconds > days_in_seconds){
            var days = Math.floor(seconds / days_in_seconds);
            var days_string = days+"";
            if (days_string.length == 1){
                days_string = "0"+days_string;
            }
            seconds = seconds % days_in_seconds; //remaining
        } else {
            days_string = "00";
        }

        var hours_in_seconds = 60 * 60;
        
        if (seconds > hours_in_seconds){
            var hours = Math.floor(seconds / hours_in_seconds);
            var hours_string = hours + "";
            if (hours_string.length == 1){
                hours_string = "0" + hours_string;
            }
            seconds = seconds % hours_in_seconds; //remaining
        } else {
            hours_string = "00";
        }

        var mins_in_seconds = 60;
        if (seconds > mins_in_seconds){
            var mins = Math.floor(seconds / mins_in_seconds)
            var mins_string = mins+"";
            if (mins_string.length == 1){
                mins_string = "0" + mins_string;
            }
        } else {
            mins_string = "00";
        }

        return days_string + ":" + hours_string + ":" + mins_string;
    },

    dataset_sidebar: function(resp, data_resource_id, self){
        var data = resp.dataResourceSummary;
        $(self.datatable.fnSettings().aoData).each(function () {
           $(this.nTr).removeClass('row_selected');
        });
        if(!$("h3.data_sources").hasClass("ui-state-active")){
             $('h3.data_sources').trigger('click');
        }
        $('h3.data_sources span').removeClass("ui-icon-triangle-1-e").addClass("ui-icon-triangle-1-s");
        var my_resource_model = self.controller.my_resources_collection.get_by_dataset_id(data_resource_id);
        var activation_state = (my_resource_model) ? my_resource_model.get("activation_state") : 'Private';
        var update_interval_seconds = (my_resource_model) ? my_resource_model.get("update_interval_seconds") : 0;
        var active_check_elem_num = (activation_state == "Private") ? 0 : 1;
        $("input[name='availability_radio']").eq(active_check_elem_num).attr("checked", "checked");
        var update_interval_seconds_num = (update_interval_seconds > 0) ? 0 : 1;
        $("input[name='polling_radio']").eq(update_interval_seconds_num).attr("checked", "checked");
        var update_interval_seconds_pretty = this.interval_seconds_pretty(parseInt(update_interval_seconds));
        if (update_interval_seconds > 0 ) {
            $("#polling_time").val(update_interval_seconds_pretty);
            $("#polling_time").removeAttr("disabled");
        } else {
            $("#polling_time").val("");
            $("#polling_time").attr("disabled", "disabled");
        }
        
        function renderFields() {
        	var $rrContents = $('#templates #template-register-resource').clone();
        	$rrContents.find('#resource_registration_title').val(ion_title);
        	$rrContents.find('#resource_registration_description').text(ion_description);
        	$rrContents.find('#resource_registration_visualization_url').text(ion_visualization_url);
        	$("#ds_title").empty().append($rrContents);
    		var ds_publisher_contact = "<b>Contact Name:</b> "+ion_name+"<br><b>Contact Email:</b> "+ion_email+"<br><b>Contact Institution:</b> "+ion_institution;
    		$("#ds_publisher_contact").html(ds_publisher_contact);
    		var ds_source = "<b>Title:</b> "+data.title;
    		if (data.summary) ds_source += "<br><br><b>Description:</b><br>"+data.summary;
            $("#ds_source").html(ds_source);
    		var ds_source_contact = "<br><b>Contact Institution:</b>"+data.institution;
            $("#ds_source_contact").html(ds_source_contact);
            $("#ds_variables").html(self.format_variables(resp.variable || {}));
            var geo_html = self.format_geospatial(data);
            $("#ds_geospatial_coverage").html(geo_html);
            $("#ds_temporal_coverage").html(data.ion_time_coverage_start + " - "+data.ion_time_coverage_end);
            $("#ds_references").html("<a style='text-decoration:underline' target='_blank' href='"+data.references+"'>"+data.references+"</a>");
            $(".notification_settings, .dispatcher_settings").hide();
            $("#download_dataset_button, #setup_notifications, #save_myresources_changes").removeAttr("disabled"); self.controller.loading_dialog();
        }

		var ion_title = '', ion_description = '', ion_name = '', ion_email = '', ion_institution = '', ion_visualization_url = '';
		if (resp.source) {
			ion_title = resp.source.ion_title;
			ion_description = resp.source.ion_description;
			ion_visualization_url = resp.source.visualization_url;
			ion_name = resp.source.ion_name;
			ion_email = resp.source.ion_email;
			ion_institution = resp.source.ion_institution;
			
			renderFields();
		}
		else {
			$.ajax({url:"userProfile", type:"GET", data:{action:"get"}, dataType:"json",
				success: function(profile_resp){
					ion_name = profile_resp.name;
					ion_institution = profile_resp.institution;
					ion_email = profile_resp.email_address;

					renderFields();
				}
			});
		}
		
    },

    format_geospatial:function(data){
        var tmpl_str = $("#template-bounding-box").html();
        //data.ion_geospatial_vertical_positive
        var tmpl_vals = {
            "north":data.ion_geospatial_lat_max, "south":data.ion_geospatial_lat_min, 
            "east":data.ion_geospatial_lon_min, "west":data.ion_geospatial_lon_max,
            "upper":data.ion_geospatial_vertical_max, "lower":data.ion_geospatial_vertical_min,
            "vertical_positive":data.ion_geospatial_vertical_positive
        };
        var html = _.template(tmpl_str, tmpl_vals);
        return html;
    },

    presentation: function(){
        /*if ($("h3.data_sources:first").hasClass("ui-state-active")){
            $(".data_sources").trigger("click");
        }*/
        $("h3.data_sources").filter(".ui-state-active").trigger("click");
		$(".dataTables_wrapper").hide();
        $("#datatable_106_wrapper").show();
		$(".east-south button").hide();
        $("#save_myresources_changes").show();
        $(".notification_settings, .dispatcher_settings").hide();
        $("#datatable_details_scroll").hide();
        $("#datatable_details_container").hide();
        $("#datatable h1").text("My Registered Resources");
        $("#save_notification_settings, #start_notifications").hide(); //button
        $(".notification_settings, .user_settings").hide();
		$('.instrument_agent').hide();
        $("#download_dataset_button, #setup_notifications").hide().attr("disabled", "disabled");
        $("#save_notifications_changes, #notification_settings, #dispatcher_settings").hide()
        $("h3.my_resources_sidebar").show();
        $("h3.data_sources").show();
    }

});

OOI.Views.Workflow109 = Backbone.View.extend({
    /*
        Base class for the 109 workflows.
    */

	resourceType: '',	// To be filled by derived classes
	tableTitle: '',		// To be filled by derived classes

    events: {
        "click tbody tr":"show_detail_clicked"
    },

    initialize: function() {
        _.bindAll(this, "render", "show_detail", "show_detail_clicked", "show_detail_all");
        this.controller = this.options.controller;
		this.$table = $(this.options.el);
		this.columnCount = this.$table.find('thead th').length;
        this.datatable = this.controller.datatable_init(this.options.el, this.columnCount);
		this.$tableWrapper = $(this.options.el + '_wrapper');
    },

    render: function() {
        this.populate_table();
        this.presentation();
        return this;
    },

    populate_table: function(){
        this.controller.loading_dialog("Loading " + this.tableTitle + "...");
        this.datatable.fnClearTable();
        var datatable_id = this.datatable.attr("id");
        var self = this;
        $.ajax({url:"resource", type:"GET", data:{action: "ofType", resource_type: self.resourceType}, dataType:"json",
            success: function(data){
				$.each(data.resources, function(i, elem){
                	// Automatically add all of the columns in the middle
					var details_image = "<img class='dataset_details' title='Show details' src='images/I1136-Details-List.png'>";
					var columns = [details_image];
					var resourceCols = elem.attribute.slice();
					while (resourceCols.length < (self.columnCount - columns.length)) resourceCols.push('');
					Array.prototype.splice.apply(columns, [0, 0].concat(resourceCols));
                    self.datatable.fnAddData(columns);
                    $(self.$table.dataTable().fnGetNodes(i)).attr("id", resourceCols[0]);
                });
                //$.each(self.$table.find("tr"), function(i, e){ $(e).find("td:first").css("width", "4%")});
				var colWidth = (100.0/self.columnCount) + '%';
                self.$table.find("tr").find("td, th").css("width", colWidth);
                self.controller.loading_dialog();
            },
            error:function(xhr, ajaxOptions, thrownError){
                self.controller.error_dialog("resource", xhr.status, xhr.responseText);
            }
        });
    },

    show_detail_clicked: function(e) {
		var self = this;
        var tr = $(e.target);
        var ooi_id = tr.parent().attr('id') || tr.parent().parent().attr('id');
        this.$table.find("tr").removeClass("selected");
        tr.parent().addClass("selected");
        if (tr.hasClass("dataset_details")){
            $("#datatable_details_scroll, #datatable_details_container").show();
			$('#dataset_scroll_left, #dataset_scroll_right').hide();
			$('#dataset_return_button').unbind('click').one('click', function(e) {
				$('#datatable_details_container, #datatable_details_scroll').hide();
				$('#dataset_scroll_left, #dataset_scroll_right').show();
				self.$tableWrapper.show();
			});
            $(".dataTables_wrapper").hide();
            if (!$("#datatable_details_container").hasClass(ooi_id)){
                //var nth_elem = $(e.target).parent().index();
                //window.location.hash += "/"+nth_elem;

				this.show_detail(ooi_id);
            }
        } else {
            this.show_detail(ooi_id);
        }
    },

    show_detail: function(ooi_id){
        var self = this;
		$("#datatable_details_container").empty();
        $.ajax({url:"resource", type:"GET", dataType:"json", data:{"action":"detail", "ooi_id":ooi_id},
            success: function(resp){
                self.show_detail_all(resp, ooi_id);
                //self.dataset_sidebar(resp, ooi_id, self);
            },
            error:function(xhr, ajaxOptions, thrownError){
                self.controller.error_dialog("resource", xhr.status, xhr.responseText);
            }
        });
    },

    show_detail_all: function(resp, ooi_id) {
		var $container = $("#datatable_details_container").empty();
		
		// The CSS here should be moved into one of the stylesheets; doing inline here to avoid interfering with Alex's work
		var labelCss = {display: 'block', float: 'left', width: '24em', margin: 0};
		var fieldCss = {display: 'block', float: 'left', width: '20em', margin: '0 1.5em 0 0'};
		var fieldsetCss = {fontSize: '1.25em', margin: 0, padding: '1.5em', border: 0};

		var $fieldset = $('<fieldset/>').css(fieldsetCss);
		$(resp.resource).each(function(i,v) {
			var $name = $('<label/>').text(v.name + ':').css(labelCss);
			var $value = $('<input type="text" readonly />').val(v.value).css(fieldCss);
			//$fieldset.append($name).append($value);
			var $nobr = $('<div/>').addClass('clearfix').append($name).append($value).appendTo($fieldset);
			// if (i & 1) $fieldset.append('<br/>');
		});
        $container.removeClass().addClass(ooi_id).append($fieldset);
    },

    dataset_sidebar: function(resp, ooi_id, self){
    },

    presentation: function(){
        if ($("h3.data_sources:first").hasClass("ui-state-active")){
            $(".data_sources").trigger("click");
        }
		$('#east_sidebar').hide();
		$(".dataTables_wrapper").hide();
        this.$tableWrapper.show();
		$(".east-south button").hide();
        $("#datatable_details_scroll").hide();
        $("#datatable_details_container").hide();
        $("#datatable h1").text(this.tableTitle);
        $("#save_notification_settings").hide(); //button
		$(".notification_settings, .dispatcher_settings, .user_settings").hide();
		$('.instrument_agent').hide();
        $("#download_dataset_button, #setup_notifications").hide().attr("disabled", "disabled");
        $("#save_notifications_changes, #notification_settings, #dispatcher_settings").hide();
        $("#east_sidebar h3").hide();
		$("#datatable_select_buttons").hide();
    }

});

/**
 * Super-efficient string trim, taken from:
 * http://flesler.blogspot.com/2008/11/fast-trim-function-for-javascript.html
 *
 * @param {Object} str - The string to trim
 */
function trim(str) {
        var start = -1, end = str.length;
        while (str.charCodeAt(--end) < 33) {;};
        while (str.charCodeAt(++start) < 33) {;};
        return str.slice(start, end + 1);
};

OOI.Views.Workflow109EPUs = OOI.Views.Workflow109.extend({
	  resourceType: 'epucontrollers'
	, tableTitle: 'Running EPUs'
});
OOI.Views.Workflow109Users = OOI.Views.Workflow109.extend({
	  resourceType: 'identities'
	, tableTitle: 'Registered Users'

	, initialize: function() {
        OOI.Views.Workflow109.prototype.initialize.call(this);

        var roleNameToVal = this.roleNameToVal = {};
		var roleValToName = this.roleValToName = {};
		$('#user_setting_role option').each(function(i,v) {
			roleValToName[$(v).val()] = $(v).text();
			roleNameToVal[$(v).text()] = $(v).val();
		});

		$('#save_user_changes').click(_.bind(this.save_user, this));
	}

	, show_detail_clicked: function(e) {
		OOI.Views.Workflow109.prototype.show_detail_clicked.call(this, e);

		var $td = this.$roleTd = $(e.target);
		// TODO: This should probably be a lookup into a data Model once we get there
		var roleName = $td.closest('tr').find('td').eq(-2).text();
		var roleNameToVal = this.roleNameToVal;
		var roleVals = _.map(roleName.split(','), function(roleName) {
			return roleNameToVal[trim(roleName)];
		});

		this.ooi_id = $td.closest('tr').find('td').eq(0).text();
		$('#user_setting_role').val(roleVals);
		$('.user_settings').show();

		$('#save_user_changes').attr('disabled', false);
	}

	, save_user: function(e) {
		var roles = $('#user_setting_role').val();
		if (roles === null || roles.length == 0) roles = ['AUTHENTICATED'];
		var self = this;

		self.controller.loading_dialog('Saving user role...');
		var data = {action: 'setRole', user_ooi_id: this.ooi_id, role: roles.join(',')};
        $.ajax({url: 'userProfile', type: 'POST', data: data,
            success: function(resp){
                self.controller.loading_dialog();
				self.$roleTd.text(self.roleValToName[role]);
            },
            error: function(jqXHR, textStatus, error){
                self.controller.loading_dialog();
                alert('Error saving user role');
            }
        });
	}

	, presentation: function() {
		OOI.Views.Workflow109.prototype.presentation.call(this);

		$('#east_sidebar').show();
		$('.user_button').show();
		$('.user_settings').hide();
		$('#save_user_changes').attr('disabled', true);
	}
});
OOI.Views.Workflow109Datasets = OOI.Views.Workflow109.extend({
	  resourceType: 'datasets'
	, tableTitle: 'Data Sets'
});
OOI.Views.Workflow109Datasources = OOI.Views.Workflow109.extend({
	  resourceType: 'datasources'
	, tableTitle: 'Data Sources'
});

OOI.Views.GeospatialContainer = Backbone.View.extend({

    events: {
        //TODO: "click #geospatial_selection_button":"render_geo"
        "click #vertical_extent_units_toggle":"vertical_extent_units_toggle",
        "click .vertical_extent_button":"vertical_extent_direction_toggle",
        "click .bb_direction":"bounding_box_direction_toggle",
        "keyup input[type='text']":"validate_input_values",
    },

    initialize: function() {
        _.bindAll(this, "render_geo", "init_bounding", "vertical_extent_units_toggle", "vertical_extent_direction_toggle"); 
        this.controller = this.options.controller;
        this.init_geo(); //'el' property doesn't encapsulate properly ... fixme
        this.init_bounding(); //'el' property doesn't encapsulate properly.. fixme
        this.apply_filter(); //'el' property doesn't encapsulate properly.. fixme
        $("#radioBoundingAll, #radioAltitudeAll, #TE_timeRange_all").attr("checked", "checked");
    },

    apply_filter:function(){
        $("#geospatialContainer input").change(function(){
            $("#apply_filter_button").removeAttr("disabled");
        });
        $("#temporalExtent input").change(function(){
            $("#apply_filter_button").removeAttr("disabled");
        });
        $("#apply_filter_button").click(function(){
            var current_view = $("#view_existing input[type='radio']:checked").attr("id");
            switch(current_view){
                case "radioAllPubRes":
                    $("#"+current_view).trigger("click");
                    break;

                case "radioMySub":
                    $("#"+current_view).trigger("click");
                    break;

                case "radioMyPubRes":
                    $("#"+current_view).trigger("click");
                    break;

                default:break;
            }
        });
    },

    validate_input_values: function(e){
        var val = $(e.target).val();
        if (isNaN(val) && val !== ""){
            $(e.target).css("border", "1px solid #ff0000").css("padding", "3px");
            $("#apply_filter_button").attr("disabled", "disabled");
        } else {
            $(e.target).attr("style", "");
            $("#apply_filter_button").removeAttr("disabled");
        }
    },

    vertical_extent_units_toggle: function(e){
        var current_units = $(e.target).text();
        if (current_units == "ft"){
            $(e.target).text("m");
        } else {
            $(e.target).text("ft");
        }
    },

    bounding_box_direction_toggle: function(e){
        var current_direction = $(e.target).text();
        switch (current_direction) {
            case "N":
                $(e.target).text("S");
                break;
            case "S":
                $(e.target).text("N");
                break;
            case "E":
                $(e.target).text("W");
                break;
            case "W":
                $(e.target).text("E");
                break;
            default:
                return;
        }
    },

    vertical_extent_direction_toggle: function(e){
        var current_direction = $(e.target).attr("src").indexOf("Above");
        if (current_direction > 0){
            $(e.target).attr("src", "images/Below-Sea-Level-Simple.png")
        } else {
            $(e.target).attr("src", "images/Above-Sea-Level-Simple.png")
        }
    },

    render_geo:function(){
        var action = "find";
        var minTime = $("#te_from_input").val(), maxTime = $("#te_to_input").val();
        var minLatitude = $("#ge_bb_south").val(), maxLatitude = $("#ge_bb_north").val(); 
        var minLongitude = $("#ge_bb_west").val(), maxLongitude = $("#ge_bb_east").val();
        var minVertical = $("#ge_altitude_ub").val(), maxVertical = $("#ge_altitude_lb").val();
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
            },
            error:function(xhr, ajaxOptions, thrownError){
                self.controller.error_dialog("dataResource", xhr.status, xhr.responseText);
            }
        });
        return this;
    },

    get_form_data: function(){
        var data = {}
        var minLatitude = $("#ge_bb_south").val(), maxLatitude = $("#ge_bb_north").val(); 
        var minLongitude = $("#ge_bb_west").val(), maxLongitude = $("#ge_bb_east").val();
        var minVertical = $("#ge_altitude_ub").val(), maxVertical = $("#ge_altitude_lb").val();
        var minTime = $("#te_from_input").val(), maxTime = $("#te_to_input").val();
        var posVertical = "down";
        if ($("#radioBoundingDefined").is(":checked")){
            data["minLatitude"] = minLatitude;
            data["maxLatitude"] = maxLatitude;
            data["minLongitude"] = minLongitude;
            data["maxLongitude"] = maxLongitude;
        }
        var default_minVertical = false, default_maxVertical = false;
        if ($("#radioAltitudeDefined").is(":checked")){
            if (minVertical === "" && maxVertical === ""){
                minVertical = -99999; //default 'highest possible atmosphere height'
                default_minVertical = true;
            }
            if (minVertical !== "" && maxVertical === ""){
                maxVertical = 99999; //default 'lowest possible ocean depth'
                default_maxVertical = true;
            }
            minVertical = parseInt(minVertical), maxVertical = parseInt(maxVertical);
            if ($("#vertical_extent_units_toggle").text() === "ft"){
                minVertical = (minVertical * 0.3048), maxVertical = (maxVertical * 0.3048); //convert to meters
            }
            var upper_sign = ($("#vertical_extent_above").attr("src").indexOf("Above") > 0) ? -1 : 1;
            var lower_sign = ($("#vertical_extent_below").attr("src").indexOf("Above") > 0) ? -1 : 1;
            if (default_minVertical) {
                data["minVertical"] = minVertical;
            } else {
                data["minVertical"] = minVertical * upper_sign;
            }
            if (default_maxVertical) {
                data["maxVertical"] = maxVertical;
            } else {
                data["maxVertical"] = maxVertical * lower_sign;
            }
            data["posVertical"] = posVertical;
        }
        if ($("#TE_timeRange_defined").is(":checked")){
            if (minTime) {
                data["minTime"] = minTime;
            } else {
                data["minTime"] = "1970-01-01T00:00:00Z"; // Epoch
            }
            if (maxTime) {
                data["maxTime"] = maxTime;
            } else {
                data["maxTime"] = "2111-01-01T00:00:00Z"; // "Forever" in the future
            }
        }
        return data;
    },

    init_geo:function(){
        $("#geospatial_selection_button").click(this.render_geo);
    },

    init_bounding:function(){
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

        $(".temporalExtentContainer input[type='radio']").click(function(){
          var is_all = $(this).hasClass("all");
          if (is_all){
            $(".temporalExtentControls input").attr("disabled", "disabled");
          } else {
            $(".temporalExtentControls input").removeAttr("disabled");
          }
        });
        $("#geospatialContainer .defined").trigger("click");
        $("#geospatialContainer .all").trigger("click");
        $(".temporalExtentControls input").attr("disabled", "disabled");
    }


});



OOI.Views.AccountSettings = Backbone.View.extend({

    events: {
        "click #account_settings_done":"account_settings_done",
        "keyup .required_settings":"required_settings"
    },

    initialize: function() {
        _.bindAll(this, "account_settings", "account_settings_done", "required_settings"); 
        this.controller = this.options.controller;
        this.modal_dialogs();
    },

    modal_dialogs: function(){
        $("#registration_link").colorbox({
            inline:true, 
            href:"#registration_dialog", 
            transition:"none", 
            opacity:0.7
        });
        $("#account_settings_link").colorbox({
            inline:true, 
            onOpen:this.account_settings, 
            href:"#account_settings", 
            transition:"none", 
            opacity:0.7
        });
        $(".modal_close").live("click", function(e){$.fn.colorbox.close();e.preventDefault();});
        if (document.location.search.search("action=register") != -1){
          $.fn.colorbox({inline:true, href:"#account_settings", transition:"none", opacity:0.7});
        }
    },

    required_settings:function(e){
        if (typeof e === "string"){
            var elem = $(e);
        } else {
            var elem = $(e.target);
        }
        var text = elem.val();
        if (text === ""){
            $("#account_settings_done").addClass("account_settings_invalid");
            elem.css("border", "1px solid #ff0000").parent().parent().find(".required_text").show();
        } else {
            $("#account_settings_done").removeClass("account_settings_invalid");
            elem.attr("style", "").parent().parent().find(".required_text").hide();
        }
        if (elem.attr("id") == "account_email"){
            var email_regex = /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
            if (email_regex.test(text)){
                $("#account_settings_done").removeClass("account_settings_invalid");
                elem.attr("style", "").parent().parent().find(".required_text").hide();
            } else {
                $("#account_settings_done").addClass("account_settings_invalid");
                elem.css("border", "1px solid #ff0000")
                elem.css("border", "1px solid #ff0000").parent().parent().find(".required_text").show();
            }
        }
    },

    account_settings_done: function(e){
        e.preventDefault();
        if ($("#account_settings_done").hasClass("account_settings_invalid"))return false;
        var name = $("#account_name").val();
        var institution = $("#account_institution").val();
        var email = $("#account_email").val();
        var mobilephone = $("#account_mobilephone").val();
        var twitter = $("#account_twitter").val();
        var system_change_str = $("#system_change").is(":checked") ? "true" : "false";
        var project_update_str = $("#project_update").is(":checked") ? "true" : "false";
        var ocean_leadership_news_str = $("#ocean_leadership_news").is(":checked") ? "true" : "false";
        var ooi_participate_str = $("#ooi_participate").is(":checked") ? "true" : "false";
        var profileData = [{"name": "mobilephone","value": mobilephone}, {"name": "twitter","value": twitter}, {"name": "system_change","value": system_change_str}, {"name": "project_update","value": project_update_str}, {"name": "ocean_leadership_news","value": ocean_leadership_news_str}, {"name": "ooi_participate","value": ooi_participate_str}];
        var profileJson = JSON.stringify(profileData);
        var data = {"action":"update", "name":name, "institution":institution, "email_address":email, "profile": profileJson};
        $("#account_settings_done").text("Saving...");
        $.ajax({url:"userProfile", type:"POST", data:data,
            success: function(resp){
                $("#account_settings_done").text("Done");
                $(".modal_close").trigger("click");
            },
            error:function(xhr, ajaxOptions, thrownError){
                self.controller.error_dialog("userProfile", xhr.status, xhr.responseText);
            }
        });
    },

    account_settings: function(){
        //TODO clear out modal form data
        $("#account_settings_content, #account_settings_bottom").css("opacity", "0");
        $("#account_settings").prepend($("<div>").attr("id", "loading_account_settings").text("Loading Acccount Settings..."));
        var self = this;
        $.ajax({url:"userProfile", type:"GET", data:{action:"get"}, dataType:"json",
            success: function(resp){
                $("#account_name").val(resp.name);
                $("#account_institution").val(resp.institution);
                $("#account_email").val(resp.email_address);
                $("#authenticating_organization").text(resp.authenticating_organization);
                $("#loading_account_settings").remove();
                $("#account_settings_content, #account_settings_bottom").css("opacity", "1");
                if (resp.profile){
                    $.each(resp.profile, function(i, v){
                        $("#account_"+v.name).val(v.value);
                    });
                }
                if (resp.name === "" || resp.institution === "" || resp.email_address === ""){
                    $("#account_settings_done").addClass("account_settings_invalid");
                    if (resp.name === "") $("#account_name").css("border", "1px solid #ff0000").parent().parent().find(".required_text").show();
                    if (resp.institution === "") $("#account_institution").css("border", "1px solid #ff0000").parent().parent().find(".required_text").show();
                    if (resp.email_address === "") $("#account_email").css("border", "1px solid #ff0000").parent().parent().find(".required_text").show();
                }
            }
        });
    }

});

OOI.Views.InstrumentList = Backbone.View.extend({
    /*
        All Resources.
    */

    events: {
        "click tbody tr":"show_detail_clicked"
    },

    initialize: function() {
        _.bindAll(this, "render", "show_detail", "show_detail_clicked", "populate_table", "register",
				  "agent_sampling_start", "agent_sampling_stop");
        this.controller = this.options.controller;
		var $table = $("#datatable_instruments");
        this.datatable = this.controller.datatable_init($table.selector, $table.find('thead th').length);

		//$('#agent_sampling_start').click(this.agent_sampling_start);
		//$('#agent_sampling_stop').click(this.agent_sampling_stop);
    },

    render: function() {
        this.populate_table();
        this.presentation();
        $('.ui-layout-center, .ui-layout-east').show();
		$('.agent_button').attr('disabled', 'disabled');
        return this;
    },

    show_detail_clicked: function(e) {
        var tr = $(e.target);
		var $table = $("#datatable_instruments");
        var instrument_resource_id = tr.parent().attr("id");
        $table.find("tr").removeClass("selected");
        tr.parent().addClass("selected");
        this.show_detail(instrument_resource_id);
    },

    show_detail: function(instrument_resource_id){
        self = this;
		$('.agent_button').attr('disabled', 'disabled');
		$("#instrument_agent_details").text('Loading instrument details...');
        this.controller.loading_dialog("Loading instrument details...");
        $.ajax({url:"instrument", type:"POST", dataType:"json", data:{action: "getState", instrument_resource_id:instrument_resource_id},
            success: function(resp){
                self.instrument_sidebar(instrument_resource_id, resp, self);
            }, error: function(xhr) {
				$("#instrument_agent_details").text('Failed to load properties for this agent. Are you sure it is running?');
			}
        });
    },

    instrument_sidebar: function(instrument_resource_id, data, self){
        $(self.datatable.fnSettings().aoData).each(function () {
           $(this.nTr).removeClass('row_selected');
        });
        // Expands right pane panels when row is selected. Also closes panels if already expanded.
        if(!$("h3.instrument_agent").hasClass("ui-state-active")){
             $('h3.instrument_agent').trigger('click');
        }

		if (!data.properties) data.properties = {};
		var $details = $("#instrument_agent_details").empty();
		var $form = $('<form action="#"></form>').appendTo($details);
		var css = {display: 'inline-block', width: '130px'};
        _.each(data.properties, function(val, key) {
			var $row = $('<div/>').appendTo($details);
			var $label = $('<label/>').text(key + ': ').css(css).appendTo($form);
			var $input = $('<input type="text" />').attr('name', key).val(val).css(css).appendTo($form);
		});

		var $row = $('<div/>').appendTo($form).append($('<label>&nbsp;</label>').css(css));
		var $saveBtn = $('<input type="submit" value="Save Properties" />').css(css).appendTo($row);
		$form.bind('submit', function(e) {
			e.preventDefault(); e.stopPropagation();
			
			var props = {};
			_.each($form.serializeArray(), function(item) { props[item.name] = item.value; });

			self.controller.loading_dialog('Saving instrument properties...');
			var postData = {action: "setState", instrument_resource_id: instrument_resource_id, properties: props};
			$.ajax({url: 'instrument', type: 'POST', dataType: 'json', data: postData, success: function(data) {
				self.controller.loading_dialog();
			}, error: function(xhr) {
				self.controller.loading_dialog('Failed to set the instrument properties.');
				setTimeout(function() { self.controller.loading_dialog(); }, 5000);
			}});
		});

		function toggleSampleState(state) {
			self.controller.loading_dialog('Trying to ' + state + ' sampling...');
			var postData = {action: state, instrument_resource_id: instrument_resource_id};
			$.ajax({url: 'instrument', type: 'POST', dataType: 'json', data: postData, success: function(data) {
				self.controller.loading_dialog();
			}, error: function(xhr) {
				self.controller.loading_dialog('Failed to ' + state + ' sampling.');
				setTimeout(function() { self.controller.loading_dialog(); }, 5000);
			}});
		}

		$('.agent_button').attr('disabled', false);
		$('#agent_sampling_start').unbind('click').click(function(e) {
			toggleSampleState('start');
		});
		$('#agent_sampling_stop').unbind('click').click(function(e) {
			toggleSampleState('stop');
		});
		$('#agent_sample_monitor').unbind('click').click(function(e) {
			var url = INSTRUMENT_MONITOR_URL;
			if (data.properties.navg) {
				// Sea Bird Instrument
				url += "?SeaBird"
			}
			else if (data.properties.diffmode) {
				// NMEA Instrument
				url += "?NMEA"
			}
			window.open(url, '_blank');
		});

        self.controller.loading_dialog();
        $(".my_resources_sidebar").hide();
    },

    populate_table: function(){
        this.controller.loading_dialog("Loading instruments...");
        this.datatable.fnClearTable();
		var $table = $("#datatable_instruments");
        var datatable_id = this.datatable.attr("id");
        var self = this;
        $.ajax({url:"instrument", type:"GET", data:{action: "list"}, dataType:"json",
            success: function(data){
                $("#datatable_select_buttons").hide();
                self.controller.resource_collection.remove_all();
                if (typeof data.instrument_metadata === "undefined"){
                    data["instrument_metadata"] = [];
                }
                $.each(data.instrument_metadata, function(i, elem){
                    self.datatable.fnAddData([elem.instrument_resource_id, elem.name, elem.description, elem.manufacturer,
						elem.model, elem.serial_num, elem.fw_version]);
                    $(self.datatable.dataTable().fnGetNodes(i)).attr("id", elem.instrument_resource_id);
                });
                c = self.controller.resource_collection;
                self.datatable.find("thead th:eq(0), tbody tr td:eq(0)").css("width", "260px");
				//self.datatable.find("thead th:eq(1), tbody tr td:eq(1)").css("width", "20%");
                self.controller.loading_dialog();
            },
            error:function(xhr, ajaxOptions, thrownError){
                self.controller.error_dialog("instrument", xhr.status, xhr.responseText);
            }
        });
    },

    presentation: function(){
        //TODO need to "broadcast presentation events" instead of couple style with workflows so directly.
		$(".instrument_agent").show();
		if ($("h3.instrument_agent:first").hasClass("ui-state-active")){
            $(".instrument_agent").trigger("click");
        }
		$(".dataTables_wrapper").hide();
        $("#datatable_instruments_wrapper").show();
        $(".east-south button").hide();
        $("#datatable_details_container").hide();
        $("#datatable h1").text("All Instruments");
        $(".data_sources, .notification_settings, .dispatcher_settings").hide();
        $("#datatable_details_scroll").hide();
        $("#geospatial_selection_button").show();
        $(".agent_button").show();
        $("#save_notifications_changes, #notification_settings, #dispatcher_settings").hide();
        $(".my_resources_sidebar").hide();
    },

	register: function() {
		var self = this;

		var selector = '#register-instrument-dialog', $el = $(selector);
		var $dlg = $.colorbox({inline: true, href: selector, transition: 'none', opacity: 0.7});
		$el.find('.modal_close').one('click', function(e) {
			history.back();
		});
		
		function submit(e) {
			e.preventDefault(); e.stopPropagation();

			function close() {
				self.controller.loading_dialog();
				$dlg.colorbox.close();
				window.location.href = '#instrument/list';
			}

			self.controller.loading_dialog('Registering instrument...');
	        var name = $("#instrument_name").val();
	        var description = $("#instrument_description").val();
	        var manufacturer = $("#instrument_manufacturer").val();
	        var model = $("#instrument_model").val();
	        var serial_number = $("#instrument_serial_num").val();
	        var fw_version = $("#instrument_fw_version").val();
			var regData = {action: "create", "name": name, "description": description, "manufacturer": manufacturer, "model": model, "serial_number": serial_number, "fw_version": fw_version};
			
                        $.ajax({url:"instrument", type:"POST", data:regData, dataType:"json",
                                success: function(data) {
                                    var instrument_id = data.instrument_resource_id
                                    self.controller.loading_dialog('Starting instrument agent...');
                                    var startData = {action: "startAgent", name: manufacturer, model: model, instrument_resource_id: instrument_id};
                                    $.ajax({url:"instrument", type:"POST", data:startData, dataType:"json",
                                        success: function(data) {
                                            if (model == "NMEA0183") {
                                                var props = {"gpgga":"ON", "gpgll":"OFF", "gprmc":"OFF", "pgrmf":"OFF", "pgrmc":"OFF", "fix_mode":"A", "alt_msl":0.0, "earth_datum":100, "diffmode":"A"}
                                                var propsJson = JSON.stringify(props);

                                                var setStateData = {action: "setState", instrument_resource_id: instrument_id, properties: propsJson};
                                                $.ajax({url:"instrument", type:"POST", data:setStateData, dataType:"json",
                                                    success: function(data) {
                                                    }, error: function() {
                                                         close();
                                                         self.controller.loading_dialog('Failed to configure instrument.');
                                                         setTimeout(function() { self.controller.loading_dialog(); }, 5000);
                                                    }
                                                });
                                            }
                                            close();
                                        }, error: function() {
                                            close();
                                            self.controller.loading_dialog('Failed to start instrument agent.');
                                            setTimeout(function() { self.controller.loading_dialog(); }, 5000);
                                        }
                                    });
                                }, error: function() {
                                        close();
                                        self.controller.loading_dialog('Failed to register instrument.');
                                        setTimeout(function() { self.controller.loading_dialog(); }, 5000);
                                }
                        });
		}
		$el.find('#register-instrument-form:first').one('submit', submit);
		$el.find('.register_instrument_ok:first').one('click', submit);
	},

	agent_sampling_start: function(e) {
	},
	agent_sampling_stop: function(e) {
	}
});
 

OOI.Views.Layout = Backbone.View.extend({
    events: {},

    initialize: function() {
        this.controller = this.options.controller;
        this.layout = this.layout_main_init();
        this.layout_center_inner = this.layout_center_inner_init();
        this.layout_west_inner = this.layout_west_inner_init();
        this.layout_east_inner = this.layout_east_inner_init();
        $('.ui-layout-center').hide();
        $('.ui-layout-east').hide();
        $('#eastMultiOpenAccordion, #westMultiOpenAccordion').multiAccordion();
        $('#westMultiOpenAccordion h3').slice(0, 5).trigger('click');
        $("#top").css("padding-bottom", "17px");

		$('.ui-layout-center, .ui-layout-east').show();
		$('.ui-layout-center').css('zIndex', 10);		// Workaround so download frame shows over footer
    },

    layout_main_init: function(){
        //  set a 'fixed height' on the container so it does not collapse...
        //$(this.el).height($(window).height() - $(this.el).offset().top);
        var self = this;
        var layout_main = $(this.el).layout({
            resizerClass: 'ui-state-default',
            north__resizable: false,
            north__closable: false,
            north__size: 60,
            west__size: 350,
            east__size: 350,
            onresize:function(){self.controller.datatable_resizer();},
        });
        return layout_main;
    },
    layout_west_inner_init: function(){
        var self = this;
        var layout_west_inner = $("div.ui-layout-west").layout({
            minSize: 50,
            center__paneSelector:".west-center",
            south__paneSelector: ".west-south",
            onresize:function(){self.controller.datatable_resizer();},
            onopen:function(){self.controller.datatable_resizer();},
            onclose:function(){self.controller.datatable_resizer();}
        });
        return layout_west_inner;
    },

    layout_center_inner_init: function(){
        var self = this;
        var layout_center_inner = $("div.ui-layout-center").layout({
            minSize: 50,
            center__paneSelector:".center-center",
            south__paneSelector: ".center-south",
        });
        return layout_center_inner;
    },

    layout_east_inner_init: function(){
        var self = this;
        var layout_east_inner = $("div.ui-layout-east").layout({
            minSize: 50,
            center__paneSelector:".east-center",
            south__paneSelector: ".east-south",
            onresize:function(){self.controller.datatable_resizer();},
            onopen:function(){self.controller.datatable_resizer();},
            onclose:function(){self.controller.datatable_resizer();}
        });
        return layout_east_inner;
    }
});


OOI.Views.SessionMgr = Backbone.View.extend({
    events: {},

    initialize: function() {
    	if (CERTIFICATE_TIMEOUT_SECS > 0) {
    		setTimeout(function() {
    			var selector = '#certificate-timeout', $el = $(selector);
    			var $dlg = $.colorbox({inline: true, href: selector, transition: 'none', opacity: 0.7});
    			$el.find('.guest').one('click', function(e) {
    				window.location.reload();
    			});
    			$el.find('.login').one('click', function(e) {
    				window.location.href = "login";
    			});
    		}, CERTIFICATE_TIMEOUT_SECS * 1000);
    	}
    }
});



