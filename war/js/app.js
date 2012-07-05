requirejs.config({
	baseUrl: 'js/lib',
	paths: {
		user: '../user'
	}
});
requirejs(['jquery', 'jquery-ui', 'jquery-tmpl'], function($){
	$(function () {
		$('#actions').tabs({
			load: function (event, ui) {
                if (ui.index === 0) {
                	requirejs(['gridy', 'user/list'], function () {
                           initList();
                    });
                }
			}
		});
	});
});