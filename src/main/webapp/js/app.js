//load the configuration
require(['config/app'], function(){
	
	'use strict';

	require(['jquery', 'jquery-ui', 'jquery-tmpl', 'notify', 'store', 'eventbus'],  
		function($, ui, tmpl, notify, store, EventBus){
	
		$(function(){
			
			var initialized = {
				member : false,
				event : false
			};
			
			//create the tabs
			$('#actions').tabs({
				cache: true,
				create: function() {
					//unload splash and display screen
					$('#splash').fadeOut();
					$('#main').show('slow');
				},
				load : function(event, ui) {
					
					//Do the initializations by tab, once loaded
					switch(ui.index) {
						case 0 : 
							requirejs(['member/list'], function(memberList) {
								memberList.setUp();
							});
							break;
						case 1:
							requirejs(['member/form'], function(memberForm) {
								memberForm.setUp();
								memberForm.initFormControls(function(){
									if(!initialized.member){
										if(store.isset('member')){
											memberForm.loadMember(store.get('member'), function(){
												memberForm.loadMemberships(store.get('member'));
											});	
										}
										initialized.member = true;
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
								eventForm.setUp();
							});
							break;
					}
				},
			
				//do some clean and load at each tab opening
				show : function(event, ui) {
					
					
					//User
					if(ui.index === 0){
						EventBus.publish('memberlist.reload', [ 't2' ]);
					} 
					if(initialized.member === true){
						
						if(ui.index === 1){
							requirejs(['member/form'], function(memberForm) {
								if(store.isset('member')){
									//load the member
									memberForm.loadMember(store.get('member'), function(){
										memberForm.loadMemberships(store.get('member'));
									});		
								} else {
									memberForm._buildMembershipTabs();
								}
							});
						} else {
							EventBus.publish('memberform.cleanup');
						}
					}
					
					//Event
					if(ui.index === 3){
						EventBus.publish('eventform.load');
					} else {
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
