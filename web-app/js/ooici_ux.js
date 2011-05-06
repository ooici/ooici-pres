OOI = {
    Models:{},
    Collections:{},
    Views: {},
    Controllers: {},
    init: function() {
        new OOI.Controllers.Dashboard({});
        Backbone.history.start();
    }
}
