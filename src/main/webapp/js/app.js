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
requirejs(	
		['jquery', 'jquery-ui', 'jquery-tmpl', 'notify', 'store'],  
		function($, ui, tmpl, notify, store){

	$(function() {
		var initialized = {};
		
		//create the tabs
		$('#actions').tabs({
			cache: true,
			create: function(event, ui) {
				//unload splash and display screen
				$('#splash').fadeOut();
				$('#main').show('slow');
			},
			load : function(event, ui) {
				switch(ui.index) {
					case 0 : 
						requirejs(['user/list'], function(list) {
							//build the member list
							list.build();
						});
						break;
					case 1:
						requirejs(['user/form'], function(form) {
							form.initControls();
						});
						break;
					case 2:
						requirejs(['event/list'], function(list) {
							list.load(function(){
								list.setUpControls();
							});
						});
						break;
					case 3:
						requirejs(['event/form'], function(form) {
							form.initControls();
						});
						break;
				}
			},
			show : function(event, ui) {
				
				requirejs(['user/form'], function(userForm) {
					/**
					 * Load member's data in the form.
					 * The member's id is given by a data attribute bound to the body tag
					 * @param {Function} callback executed without parameter when the member is loaded
					 */
					var loadMember = function(callback){
						if(store.isset('member')){
							userForm.loadMember(store.get('member'), callback);
						}
					};
					
					if(ui.index === 1 ){
						//the 1st time, we load the member only once the events are loaded
						if(!initialized['member']){
							userForm.loadEvents(function(){
								loadMember(function(){
									initialized['member'] = true;
								});
							});
						} else {
							loadMember();
						}
					} else {
						//clean up the form
						userForm.clear();
						store.rm('member');
					}
				});
			
				
				requirejs(['event/form'], function(eventForm) {
					if(ui.index === 3){
						//load the event
						if(store.isset('event')){
							eventForm.loadEvent(store.get('event'));
						}
					} else {
						//clean up the form
						eventForm.clear();
						store.rm('event');
					}
					
				});
				//rename the tab if we are add or editing
				$('#actions ul:first li:nth-child(2) a').text(
					(ui.index === 1 && store.isset('member')) ? 'Edit member' : 'Add a member'
				);
				$('#actions ul:first li:nth-child(4) a').text(
					(ui.index === 3 && store.isset('event')) ? 'Edit event' : 'Add an event'
				);
			}
		});
		
	});
});
