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

	$(function(){
		
		var initialized = {
			user : false,
			event : false
		};
		
		//create the tabs
		$('#actions').tabs({
			cache: true,
			create: function(event, ui) {
				//unload splash and display screen
				$('#splash').fadeOut();
				$('#main').show('slow');
			},
			load : function(event, ui) {
				
				//Do the initializations by tab, once loaded
				switch(ui.index) {
					case 0 : 
						requirejs(['user/list'], function(list) {
							//build the member list
							list.build();
						});
						break;
					case 1:
						requirejs(['user/form'], function(userForm) {
							userForm.initFormControls(function(){
								if(!initialized['member']){
									if(store.isset('member')){
										userForm.loadMember(store.get('member'));	
									}
									initialized['member'] = true;
								}
							});
						});
						break;
					case 2:
						requirejs(['event/list'], function(eventList) {
							eventList.setUp();
						});
						break;
					case 3:
						requirejs(['event/form'], function(eventForm) {
							eventForm.initFormControls(function(){
								if(!initialized['event']){
									if( store.isset('event')){
										eventForm.loadEvent(store.get('event'));
									}
									initialized['event'] = true;
								}
							});
						});
						break;
				}
			},
			
			//do some clean and load at each tab opening
			show : function(event, ui) {

				//User
				if(initialized['member']){
					requirejs(['user/form'], function(userForm) {
						if(ui.index === 1 && store.isset('member')){
							//load the member
							userForm.loadMember(store.get('member'));	
						} else {
							//clean up
							userForm.clear();
							store.rm('member');
						}
					});
					if(ui.index === 0){
						requirejs(['user/list'], function(userList) {
							userList.reload();
						});
					}
				}
				
				//rename the tab if we are add or editing
				$('#actions ul:first li:nth-child(2) a').text(
					(ui.index === 1 && store.isset('member')) ? 'Edit member' : 'Add a member'
				);
				
				//Event
				if(initialized['event']){
					requirejs(['event/form'], function(eventForm) {
						if(ui.index === 3 && store.isset('event')){
							eventForm.loadEvent(store.get('event'));
						} else {
							//clean up
							eventForm.clear();
							store.rm('event');
						}
					});
				}
				//rename the tab if we are add or editing
				$('#actions ul:first li:nth-child(4) a').text(
					(ui.index === 3 && store.isset('event')) ? 'Edit event' : 'Add an event'
				);
			}
		});
		
	});
});
