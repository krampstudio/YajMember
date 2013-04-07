//load the configuration
require(['config/app'], function(){
	
	'use strict';

	require(['jquery', 'jquery-ui', 'jquery-tmpl', 'notify', 'store', 'eventbus', 'debug'],  
		function($, ui, tmpl, notify, store, EventBus){
	
		$(function(){
			
			var level = {
				'none' : 0, 
				'error' : 1,
				'warn' : 2,
				'info' : 3,
				'debug' : 4,
				'log' : 5,
				'all' : 9
			};
			
			//initialize logging: 0 disabled, 1 error, 2 warn
			debug.setLevel(level.all);
			
			//create the tabs
			$('#actions').tabs({
				
				//each tab is loaded once!
				beforeLoad: function( event, ui ) {
					if (ui.tab.data( "loaded" )) {
						event.preventDefault();
						return;
					}
					ui.jqXHR.success(function() {
						ui.tab.data("loaded", true );
					});
				},		

				create: function() {
					//unload splash and display screen
					$('#splash').fadeOut(1000, function(){
						$('#main').fadeIn(800);
					});
				},
				
				load : function(event, ui) {
					var module = ui.tab.find('a').attr('href').replace(/\.html$/, '');
				
					//load the module that match the loaded content : member/list.html load module member/list
					requirejs([module], function(component) {
						
						//and call the setUp method
						if(component !== undefined && typeof component === 'object'){
							if(component.setUp && typeof component.setUp === 'function'){
								debug.debug('Set up module ', module, component);
								component.setUp();
							}
						}
					});
				},
			
				//trigger some events at each opening 
				activate : function(event, ui) {
					var module = ui.newTab.find('a').attr('href').replace(/\.html$/, '');
					
					if(module === 'member/list'){
						//trigger the member list reload
						EventBus.publish('memberlist.reload');
					} 
					
					if(module === 'member/form'){
						//load a member into the form if one is selected
						EventBus.publish('memberform.load');
					} else {
						//clean up the form
						EventBus.publish('memberform.cleanup');
					}
					
					if(module === 'event/form'){
						//load an event into the form if one is selected
						EventBus.publish('eventform.load');
					} else {
						//clean up the form
						EventBus.publish('eventform.cleanup');
					}
					
					//rename the tabs regarding if we are in add or editing mode
					$('#actions ul:first li:nth-child(3) a').text(
						(module === 'member/form' && store.isset('member')) ? 'Edit member' : 'Add a member'
					);
					$('#actions ul:first li:nth-child(5) a').text(
						(module === 'event/form' && store.isset('event')) ? 'Edit event' : 'Add an event'
					);
				}
			});
		});
	});
});
