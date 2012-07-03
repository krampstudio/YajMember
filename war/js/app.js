requirejs.config({
	baseUrl: 'js/lib',
	paths: {
		user: '../user'
	}
});
requirejs(['jquery', 'jquery-ui'], function($){
	$(function () {
		$('#actions').tabs();
	});
});