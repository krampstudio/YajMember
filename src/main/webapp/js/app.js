//RequireJs configuration: 
requirejs.config({
	baseUrl: 'js/lib',
	urlArgs: 'bust=' + (new Date()).getTime(),	//only for dev  : no-cache for the laoded scripts 
	paths: {
		user: '../user',
		event: '../event'
	},
	//dependencies
	shim: {
	    'jquery-ui'			: ['jquery'],
	    'jquery-tmpl'		: ['jquery'],
	    'gridy'				: ['jquery'],
	    'noty/jquery.noty' 	: ['jquery'],
	    'notify'			: ['noty/jquery.noty']
	}
});

//The main entry point
requirejs(['jquery', 'jquery-ui', 'jquery-tmpl', 'notify'],  function($, ui, tmpl, notify){

	$(function() {
		
		var firstFormLoad = true,
			isEditingMember = function(){
				return $('body').data('member') !== undefined;
			};
		
		//initialize the tabs
		$('#actions').tabs({
			create: function(event, ui) {
				//unload splash and display screen
				$('#splash').fadeOut();
				$('#main').show('slow');
			},
			load : function(event, ui) {
				if (ui.index === 0) {
					requirejs(['user/list'], function(list) {
						//build the member list
						list.build();
					});
				} else if (ui.index === 1) {
					requirejs(['user/form'], function(form) {
						
						/**
						 * Load member's data in the form.
						 * The member's id is given by a data attribute bound to the body tag
						 * @param {Function} callback executed without parameter when the member is loaded
						 */
						var loadMember = function(callback){
							if(isEditingMember()){
								form.loadMember($('body').data('member'), callback);
							}
						};
						
						if(firstFormLoad === true){
							//we initialise the form the first time
							form.initControls();
							form.loadEvents(function(){
								//we load the member only once the events are loaded
								loadMember(function(){
									firstFormLoad = false;
								});
							});
						} else {
							loadMember();
						}
					});
				} else if (ui.index === 2){
					requirejs(['event/list'], function(list) {
						list.load(function(){
							list.setUpControls();
						});
					});
					
				} else if (ui.index === 3){
					
					
					
				} 
			},
			show : function(event, ui) {
				
				if (ui.index !== 1){
					//clean up teh form
					requirejs(['user/form'], function(form) {
						form.clear();
					});
				}
				//rename the tab if we are add or editing a member
				$('#actions ul:first li:nth-child(2) a').text(
					(ui.index === 1 && isEditingMember()) ? 'Edit member' : 'Add a member'
				);
			}
		});
		
	});
});
