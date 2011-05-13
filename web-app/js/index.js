$(document).ready(function() {
	$("#registration_link").colorbox({
        inline:true, 
        href:"#registration_dialog", 
        transition:"none", 
        opacity:0.7
    });
    $(".modal_close").live("click", function(e){$.fn.colorbox.close();e.preventDefault();});
});