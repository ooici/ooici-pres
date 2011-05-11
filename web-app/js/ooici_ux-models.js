OOI.Models.Resource = Backbone.Model.extend({

});

OOI.Models.MyResource = Backbone.Model.extend({


});


OOI.Models.MyNotification = Backbone.Model.extend({


});


OOI.Collections.Resources = Backbone.Collection.extend({

    model:OOI.Models.Resource,

    get_by_dataset_id:function(ds_id){
        return this.find(function(model){
            return model.get("datasetMetadata").data_resource_id == ds_id;
        });
    },

    remove_all:function(){
        this.refresh([]);
    }

});


OOI.Collections.MyResources = Backbone.Collection.extend({

    model:OOI.Models.MyResource,

    get_by_dataset_id:function(ds_id){
        return this.find(function(model){
            return model.get("data_resource_id") == ds_id;
        });
    },

    remove_all:function(){
        this.refresh([]);
    }

});

OOI.Collections.MyNotifications = Backbone.Collection.extend({

    model:OOI.Models.MyNotification,

    get_by_dataset_id:function(ds_id){
        return this.find(function(model){
            return model.get("data_resource_id") == ds_id;
        });
    },

    remove_all:function(){
        this.refresh([]);
    }

});

