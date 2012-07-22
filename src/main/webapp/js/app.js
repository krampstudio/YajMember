requirejs.config({
	baseUrl: 'js/lib',
	paths: {
		user: '../user'
	},
	shim: {
	    'jquery-ui': ['jquery'],
	    'jquery-tmpl': ['jquery'],
	    'gridy': ['jquery']
	}
});
requirejs(['jquery', 'jquery-ui', 'jquery-tmpl'],  function($){

	$(function() {
		$('#actions').tabs({
			load : function(event, ui) {
				if (ui.index === 0) {
					requirejs([ 'user/list' ], function(list) {
						list.build();
					});
				}
				if (ui.index === 1) {
					requirejs([ 'user/form' ], function(form) {
						form.build();
					});
				}
			}
		});
	});
	
});
