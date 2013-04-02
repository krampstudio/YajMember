
//RequireJs configuration: 
requirejs.config({
	urlArgs: 'bust=' + (new Date()).getTime(),	//only for dev  : no-cache for the laoded scripts 
	paths: {
		'jquery' 		: 'lib/jquery/jquery',
		'jquery-ui' 	: 'lib/jquery-ui/jquery-ui',
		'jquery-tmpl' 	: 'lib/jquery-tmpl/jquery.tmpl',
		'gridy'			: 'lib/gridy/js/jquery.gridy',
		'epiceditor'	: 'lib/epiceditor/epiceditor/js/epiceditor',
		'modernizr'		: 'lib/modernizr/modernizr',
		'noty-base'		: 'lib/noty/js/noty',
		'noty'			: 'lib/noty/js/noty/jquery.noty',
		'debug'			: 'lib/javascript-debug/ba-debug',
		'chart'			: 'lib/Chart.js/Chart.min'
		'multiform'		: 'helpers/multiform',
		'notify'		: 'helpers/notify',
		'store'			: 'helpers/store',
		'uri'			: 'helpers/uri',
		'eventbus'		: 'helpers/eventbus',
		'filesender'	: 'ui/filesender'
	},
	//dependencies
	shim: {
	    'jquery-ui'			: ['jquery'],
	    'jquery-tmpl'		: ['jquery'],
	    'gridy'				: ['jquery', 'jquery-tmpl'],
	    'jhtmlarea'			: ['jquery', 'jquery-ui'],
	    'noty'			 	: ['jquery'],
	    'notify'			: ['noty'],
	    'filesender'		: ['jquery']
	}
});
