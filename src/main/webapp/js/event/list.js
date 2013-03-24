define(['jquery', 'controller/event', 'store'], function($, EventController, store){
	
	'use strict';
	
	/**
	 * Manage event's list UI and IO
	 * @exports event/list
	 */
	var EventList = {
			
		/**
		 * Set up the list
		 */
		setUp: function(){
			var self = this;
			
			//load the years list
			EventController.getYears(function(years){
				$('#events').empty();
				
				//create the HTML structure for the years accordion
				var template = $('#events-acc-template');
				$.tmpl(template, {years: years}).appendTo('#events');
				
				//set up the accordion
				$('#events').accordion({
					active: false,
					collapsible: true,
					clearStyle : true,
					create: function( event, ui ) {
						$(this).accordion('activate', 0);
					},
					//load the events list by activating a pad 
					change: function(event, ui){
						
						if(ui.newHeader){
							var $container = ui.newContent,
								$list = ui.newContent.find('ul'),
								year = ui.newHeader.find('a').attr('href').replace('#', '');
							
							$list.find('li').remove();
							
							//load events
							EventController.getAll(year, function(events){
								
								$container.find('.loader').fadeOut();
								
								//build the event list
								var template = $('#event-item-template');
								$list.append($.tmpl(template, events));
								
								//set up the widgets 
								self._setUpEventsControls($list);
							});
							return false;
						}
					}
				});
			});
		},
		
		/**
		 * set up the controls of an event list (the buttons associated to each event)
		 * @private
		 * @param {Object} $container jQuery element of the events container (an ul element)
		 */
		_setUpEventsControls: function($container){
	
			var getEventId = function($elt){
				return $elt.parents('li.event').attr('id').replace('event-', '');
			};
			
			//edit event button
			$('.event-editor', $container)
				.button({icons: { primary: "icon-evt-edit" }})
				.click(function(event){
					event.preventDefault();
					
					store.set('event', getEventId($(this)));
					
					//opens the edit tab
					$('#actions').tabs('select', 3);
					
					return false;
				});
			
			//remove event button
			$('.event-deletor', $container)
				.button({icons: { primary: "icon-evt-delete" }})
				.click(function(){
					EventController.remove(getEventId($(this)));
				});
		}
	};
	
	return EventList;
});