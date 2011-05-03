OOI = {
    Views: {},
    Controllers: {},
    init: function() {
        $("#datatable100-tmpl").template("datatable100-tmpl");
        $("#datatable104-tmpl").template("datatable104-tmpl");
        $("#datatable106-tmpl").template("datatable106-tmpl");
        new OOI.Controllers.Dashboard({});
        Backbone.history.start();
    }
}
