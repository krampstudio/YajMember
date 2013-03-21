//load the configuration
require(['config/app'], function(){
	
	'use strict';

	require(['jquery', 'jquery-ui', 'jquery-tmpl', 'notify', 'store', 'eventbus'],  
		function($, ui, tmpl, notify, store, EventBus){
	
		$(function(){
			
			//one module by tab. The array index match the tab index!
			var modules = ['member/list', 'member/form', 'event/list', 'event/form'];
			
			//create the tabs
			$('#actions').tabs({
				//each tab is loaded once!
				cache: true,		
				
				create: function() {
					//unload splash and display screen
					$('#splash').fadeOut(1000, function(){
						$('#main').fadeIn(800);
					});
				},
				
				load : function(event, ui) {
					
					//load the module that match the tab
					requirejs([modules[ui.index]], function(module) {
						
						//and call the setUp method
						if(module != undefined && typeof module === 'object'){
							if(module.setUp && typeof module.setUp === 'function'){
								module.setUp();
							}
						}
						
					});
				},
			
				//trigger some events at each opening 
				show : function(event, ui) {
					
					if(ui.index === 0){
						//trigger the member list reload
						EventBus.publish('memberlist.reload');
					} 
					
					if(ui.index === 1){
						//load a member into the form if one is selected
						EventBus.publish('memberform.load');
					} else {
						//clean up the form
						EventBus.publish('memberform.cleanup');
					}
					
					if(ui.index === 3){
						//load an event into the form if one is selected
						EventBus.publish('eventform.load');
					} else {
						//clean up the form
						EventBus.publish('eventform.cleanup');
					}
					
					//rename the tab if we are add or editing
					$('#actions ul:first li:nth-child(2) a').text(
						(ui.index === 1 && store.isset('member')) ? 'Edit member' : 'Add a member'
					);
					
					//rename the tab if we are add or editing
					$('#actions ul:first li:nth-child(4) a').text(
						(ui.index === 3 && store.isset('event')) ? 'Edit event' : 'Add an event'
					);
				}
			});
		});
	});
});
