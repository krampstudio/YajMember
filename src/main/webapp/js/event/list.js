/**
 * Manage event's list UI and IO
 * @module event/list
 */
define(['jquery', 'controller/event', 'store'], function($, EventController, store){
	
	'use strict';
	
	/**
	 * @constructor
	 * @alias module:event/list
	 */
	var EventList = {
			
		/**
		 * Set up the list
		 * @memberOf module:event/list
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
					
					//load the events list by activating a pad 
					changestart: function(event, ui){
						
						var $container = ui.newContent.find('ul'),
							year = ui.newHeader.find('a').attr('href').replace('#', '');
						
						$container.find('li').remove('li');
						
						//load events
						EventController.getAll(year, function(events){
							
							//build the event list
							var template = $('#event-item-template');
							$container.append($.tmpl(template, events));
							
							//set up the widgets 
							self._setUpEventsControls($container);
						});
						
						return false;
					}
				});
			});
		},
		
		/**
		 * set up the controls of an event list (the buttons associated to each event)
		 * @private
		 * @memberOf module:event/list
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