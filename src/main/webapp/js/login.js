//RequireJs configuration: 
requirejs.config({
	urlArgs: 'bust=' + (new Date()).getTime(),	//only for dev  : no-cache for the laoded scripts 
	paths: {
		'jquery' 		: 'lib/jquery/jquery',
		'noty-base'		: 'lib/noty/js/noty',
		'noty'			: 'lib/noty/js/noty/jquery.noty',
		'notify'		: 'helpers/notify',
		'uri'			: 'helpers/uri'
	},
	//dependencies
	shim: {
		'uri'				: ['jquery'],
	    'noty' 				: ['jquery'],
	    'notify'			: ['noty']
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