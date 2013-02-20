//load the configuration
require(['config/login'], function(){
	
	'use strict';
	
	//login page entry point
	requirejs(['jquery','notify', 'uri'], function($, notify, uri){
		
		//on page load
		$(function(){
			
			//if the window contains an error param in the query we display an error message
			var params = uri.getQueryParams(window.location.href);
			if(params.error){
				notify('error', params.error);
			}
		});
	});
});