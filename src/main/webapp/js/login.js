//RequireJs configuration: 
requirejs.config({
	baseUrl: 'js/lib',
	urlArgs: 'bust=' + (new Date()).getTime(),	//only for dev  : no-cache for the laoded scripts 
	//dependencies
	shim: {
		'uri'				: ['jquery'],
	    'noty/jquery.noty' 	: ['jquery'],
	    'notify'			: ['noty/jquery.noty']
	}
});

//The main entry point
requirejs(['jquery','notify', 'uri'], function($, notify, uri){

	$(function(){
		
		//if the window contains an error param in the query we display an error message
		var params = uri.getQueryParams(window.location.href);
		if(params.error){
			notify('error', params.error);
		}
	});
});