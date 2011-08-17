$(document).ready(function() {
  $(".access_button").hover(function(){$(this).css("background-position","0px -24px");}, function(){$(this).css("background-position","0px 0px");});
  $(".access_button").mousedown(function(){$(this).css("background-position","0px -50px");});
  $("#enterion-content").hover(function(){$(this).css("background-position","0px -103px");}, function(){$(this).css("background-position","0px 0px");});
  $("#enterion-content").mousedown(function(){$(this).css("background-position","0px -204px");});
	$("#registration_link").colorbox({
        inline:true, 
        href:"#registration_dialog", 
        transition:"none", 
        opacity:0.7
    });
    $(".modal_close").live("click", function(e){$.fn.colorbox.close();e.preventDefault();});
});
