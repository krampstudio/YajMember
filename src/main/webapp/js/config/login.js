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
	    'noty' 				: ['jquery'],
	    'notify'			: ['noty']
	}
});