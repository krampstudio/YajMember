define(['jquery', 'notify', 'store'], function($, notify, store){
	
	'use strict';
	
	/**
	 * Enables you to send and retrieve flyer's data from/to the server
	 * @exports controller/flyer
	 */
	var FlyerController = {

		/** 
		 * the base path for the server calls
		 * @private
		 */
		_apiBase : 'api/flyer/',
		
		/**
		 * Call the API to removes a flyer
		 * @param {String} key - the key of the event to remove
		 * @param {Function} callback - called if there was no errors
		 */
		remove : function(key, callback){
			var self = this;
			if(!key){
				$.error('Invalid event key : ' + key);
			} else {
				notify('confirm', 'You really want to remove this flyer ?', function(){
					$.ajax({
						type		: 'DELETE',
						url			: self._apiBase + 'remove/' + key,
						dataType	: 'json'
					}).done(function(data) {	
						if(!data.removed  || data.error){
							debug.warn((data.error ? data.error : "unknown"));
							notify('warn', (data.error ? data.error : "unknown"));
						} else {
							notify('success', 'Flyer removed');
							if(typeof callback === 'function'){
								callback();
							}
						}
					});
				});
			}
		}
	};
	
	return FlyerController;
});