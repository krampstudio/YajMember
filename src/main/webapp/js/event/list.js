define(['jquery', 'controller/event', 'store', 'eventbus', 'epiceditor'], function($, EventController, store, EventBus){
	
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
			this._buildComponents();
		},
		
		/**
		 * Build the UI components (the accordion)
		 * @private
		 */
		_buildComponents : function(){
			var self = this;
			
			EventBus.subscribe('eventlist.loadevents', function(event, $container, year){
				self._loadEvents($container, year);
			});
			
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
					heightStyle : 'content',
					create: function(event, ui) {
						 $(this).accordion('option', 'active', 0);
					},
					
					//load the events list by activating a pad 
					activate: function(event, ui){
						var year = ui.newHeader.find('a').attr('href').replace('#', '');
						if(ui.newPanel){
							EventBus.publish("eventlist.loadevents", [year]);
						}
						return false;
					}
				});
			});
		},
		
		/**
		 * Load the events list for a year
		 * @private
		 * @param {Number} year - the year
		 */
		_loadEvents : function(year){
			var self = this,
				$container = $('#event-list-'+year),
				$list = $container.find('ul.event-list');
		
			if(year && $container){
				$list.find('li').remove();
			
				//load events
				EventController.getAll(year, function(events){
					
					//parse markdown
					for(var i in events){
						if(events[i].description){
							events[i].description = marked(events[i].description);
						}
					}
					
					$container.find('.loader').fadeOut();
					
					//build the event list
					$list.append($.tmpl($('#event-item-template'), events));
					
					//set up the widgets 
					self._setUpEventsControls($list);
				});
			}
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
					$('#actions').tabs('option', 'active', 4);
					
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