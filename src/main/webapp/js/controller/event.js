define(['jquery', 'notify', 'store'], function($, notify, store){
	
	'use strict';
	
	/**
	 * Enables you to send and retrieve event's data from/to the server
	 * @exports controller/event
	 */
	var EventController = {

		/** the base path for the server calls*/
		_apiBase : 'api/event/',
		
		
		/**
		 * Call the API to get all the available events for a  year
		 * @param {Number} year - the year we want the events for
		 * @param {EventsCallback} callback - called if no errors with the retrieved events
		 */
		getAll : function(year, callback){
			$.ajax({
				type		: 'GET',
				url			: this._apiBase + 'list',
				dataType	: 'json',
				data		: { year : year }
			}).done(function(data) {	
				if(!data || data.error){
					$.error("Error : " + (data.error ? data.error : "unknown"));
				} else {
					if(typeof callback === 'function'){
						callback(data);
					}
				}
			});
		},
		
		/**
		 * Call the API to get the years where there was events, it always includes the current year. 
		 * @param {Function} callback - called if no errors with the retrieved years in parameter
		 */
		getYears : function(callback){
			$.ajax({
				type		: 'GET',
				url			: this._apiBase + 'getYears',
				dataType	: 'json'
			}).done(function(data) {	
				if(!data || data.error || data.length === 0){
					$.error("Error : " + (data.error ? data.error : "unknown"));
				} else {
					if(typeof callback === 'function'){
						callback(data);
					}
				}
			});
		},
		
		/**
		 * Call the API to get an event
		 * @param {String} key - the event identifier
		 * @param {EventCallback} callback - called with the retrieved event
		 */
		getOne : function(key, callback){
			if(!key){
				$.error('Invalid event key : ' + key);
			} else {
				$.ajax({
					type		: 'GET',
					url			: this._apiBase + 'getOne',
					dataType	: 'json',
					data		: { id : key }
				}).done(function(data) {	
					if(!data || data.error){
						$.error("Error : " + (data.error ? data.error : "unknown"));
					} else {
						if(typeof callback === 'function'){
							callback(data);
						}
					}
				});
			}
		},
		
		/**
		 * Call the API to save an event
		 * @param {Object} event - the event to save, if it has no key attribute, it will be inserted
		 * @param {Function} callback - called if there was no errors
		 */
		save : function(event, callback){
			if(!event){
				$.error('Invalid event : ' + event);
			} else {
				var update = event.key !== undefined;
				$.ajax({
					type		: update ? 'POST' : 'PUT',
					url			: this._apiBase + (update ? 'update' : 'add'),
					contentType	: 'application/x-www-form-urlencoded',
					dataType	: 'json',
					data		: {
						event : JSON.stringify(event)
					}
				}).done(function(data) {
					if(!data.saved || data.error){
						$.error("Error : " + data.error ? data.error : "unknown");
					} else {
						notify('success', 'The event has been saved');
						if(typeof callback === 'function'){
							callback();
						}
					}
				});
			}
		},
		
		/**
		 * Call the API to removes an event
		 * @param {String} key - the key of the event to remove
		 * @param {Function} callback - called if there was no errors
		 */
		remove : function(key, callback){
			if(!key){
				$.error('Invalid event key : ' + key);
			} else {
				notify('confirm', 'You really want to remove this event?', function(){
					$.ajax({
						type		: 'DELETE',
						url			: this._apiBase + 'remove/' + key,
						dataType	: 'json'
					}).done(function(data) {	
						if(!data.removed  || data.error){
							$.error("Error : " + data.error ? data.error : "unknown");
						} else {
							if(store.get('event') === key){
								store.rm('event');
							}
							notify('success', 'Event removed');
							if(typeof callback === 'function'){
								callback();
							}
						}
					});
				});
			}
		}
	};
	
	//callback's documentation
	
	/**
	 * A callback with a list of events in parameters
	 * @callback EventsCallback
	 * @param {Array} events - a list retrieved events
	 */
	
	/**
	 * A callback with an event in parameter
	 * @callback EventCallback
	 * @param {Object} event - a retrieved event
	 */
	
	return EventController;
});