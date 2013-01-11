/**
 * Manage event's list UI and IO
 * @module event/list
 */
define(['store', 'notify'], function(store, notify){
	
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
			$.ajax({
				type 		: 'GET',
				url 		: 'api/event/getYears',
				dataType 	: 'json'
			}).done(function(data) {	
				if(!data || data.error || data.length === 0){
					$.error("Error : " + (data.error ? data.error : "unknown"));
				} else {
					
					//create the HTML structure for the years accordion
					var template = $('#events-acc-template');
					$.tmpl(template, {years: data}).appendTo('#events');
					
					//set up the accordion
					$('#events').accordion({
						active: false,
						collapsible: true,
						clearStyle : true,
						//load the event list by activating a 
					    changestart: function(event, ui ){
					    	 var $eventList = ui.newContent.find('ul');
					    	 if($eventList.find('li').length === 0){
					    		 self.loadEvents(
					    			 ui.newContent.find('ul'), 
					    			 ui.newHeader.find('a').attr('href').replace('#', ''),
					    			 function(){
					    				 self._setUpEventsControls($eventList);
					    			 }
					    		);
					    	 }
						}
					});
				}
			});
		},
		
		/**
		 * Load a list of events of a year onto a container 
		 * @memberOf module:event/list
		 * @param {Object} $container jQuery element of the events container (an ul element)
		 * @param {Number} year the year to retrieve the events for
		 * @param {Function} [callback] a callback executed once the events are loaded
		 */
		loadEvents: function($container, year, callback){
			//load events
			$.ajax({
				type 		: 'GET',
				url 		: 'api/event/list',
				dataType 	: 'json',
				data		: {year : year}
			}).done(function(data) {	
				if(!data || data.error){
					$.error("Error : " + (data.error ? data.error : "unknown"));
				} else if(!data.length){
					//data is empty no event
				} else {
					var template = $('#event-item-template');
					$container.append($.tmpl(template, data));
				}
				if(typeof callback === 'function'){
					callback();
				}
			});
		},
		
		/**
		 * set up the controls of an event list (the buttons associated to each event)
		 * @private
		 * @memberOf module:event/list
		 * @param {Object} $container jQuery element of the events container (an ul element)
		 */
		_setUpEventsControls: function($container){
			var self = this;
			var getEventId = function($elt){
				return $elt.parents('li.event').attr('id').replace('event-', '');
			}
			
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
					var eventId = getEventId($(this));
					notify('confirm', 'You really want to remove this event?', function(){
						self._rmEvent(eventId);
					});
				});
		},
		
		/**
		 * Removes an event
		 * @private
		 * @memberOf module:event/list
		 * @param {Number} eventId the id of the event to remove
		 */
		_rmEvent: function(eventId){
			$.ajax({
				type 		: 'DELETE',
				url 		: 'api/event/remove/'+eventId,
				dataType 	: 'json'
			}).done(function(data) {	
				if(data && data.removed === true){
					notify('success', 'Event removed');
				}
			});
		}
	};
	
	return EventList;
});