requirejs.config({
	baseUrl: 'js/lib',
	urlArgs: 'bust=' + (new Date()).getTime(),	//only for dev  : no-cache
	paths: {
		user: '../user'
	},
	shim: {
	    'jquery-ui'			: ['jquery'],
	    'jquery-tmpl'		: ['jquery'],
	    'gridy'				: ['jquery'],
	    'noty/jquery.noty' 	: ['jquery'],
	    'notify'			: ['noty/jquery.noty']
	}
});
requirejs(['jquery', 'jquery-ui', 'jquery-tmpl', 'notify'],  function($, ui, tmpl, notify){

	$(function() {
		$('#actions').tabs({
			create: function(event, ui) {
				//unload splash and display screen
				$('#splash').fadeOut();
				$('#main').show('slow');
			},
			load : function(event, ui) {
				if (ui.index === 0) {
					requirejs([ 'user/list' ], function(list) {
						list.build();
						notify('success', 'done');
					});
				}
				if (ui.index === 1) {
					requirejs([ 'user/form' ], function(form) {
						form.initControls();
						form.loadEvents();
					});
				}
			}
		});
	});
	
});
