define(['jquery', 'notify', 'store'], function($, notify, store){
	
	'use strict';
	
	/**
	 * Enables you to send and retrieve membership's data from/to the server
	 * @exports controller/membership
	 */
	var MembershipController = {

		/** 
		 * The base path for the server calls
		 * @private
		 */
		_apiBase : 'api/membership/',
		
		/**
		 * Call the API to get all the memberships
		 * @param {MembershipsCallback} callback - called once retrieved, with the memberships in parameter
		 */
		getAll : function(callback){
			$.ajax({
				type		: 'GET',
				url			: this._apiBase + 'all',
				dataType	: 'json'
			}).done(function(data) {	
				if(!data || data.error){
					debug.warn((data.error ? data.error : "unknown"));
					notify('warn', (data.error ? data.error : "unknown"));
				} else {
					if(typeof callback === 'function'){
						callback(data);
					}
				}
			});
		},
		
		/**
		 * Call the API to get the memberships that belongs to a member
		 * @param {String} key - the member key
		 * @param {MembershipsCallback} callback - called once retrieved, with the memberships in parameter
		 */
		getByMember : function(key, callback){
			if(!key){
				debug.error('Invalid member key : ' + key);
			} else {
				$.ajax({
					type		: 'GET',
					url			: this._apiBase + 'get',
					dataType	: 'json',
					data		: { member : key }
				}).done(function(data) {	
					if(!data || data.error){
						debug.warn((data.error ? data.error : "unknown"));
						notify('warn', (data.error ? data.error : "unknown"));
					} else {
						if(typeof callback === 'function'){
							callback(data);
						}
					}
				});
			}
		},
		
		/**
		 * Call the API to save memberships
		 * @param {Array} memberships - the list of memberships to save
		 * @param {Function}�callback - called if there was no errors
		 */
		save : function(memberships, callback){
			if(!memberships || !$.isArray(memberships) ){
				debug.error('Invalid memberships : ' + memberships);
			} else {
			
				$.ajax({
					type		: 'POST',
					url			: this._apiBase + 'save',
					contentType	: 'application/x-www-form-urlencoded',
					dataType	: 'json',
					data		: {
						memberships : JSON.stringify(memberships)
					}
				}).done(function(data) {
					if(!data.saved || data.error){
						debug.warn((data.error ? data.error : "unknown"));
						notify('warn', (data.error ? data.error : "unknown"));
					} else {
						notify('success', 'The membership have been saved');
						if(typeof callback === 'function'){
							callback();
						}
					}
				});
			}
		},
		
		/**
		 * Call the API to removes a member
		 * @param {String} key - the key of the member to remove
		 * @param {Function} callback - called if there was no errors
		 */
		remove : function(key, callback){
			var self = this;
			if(!key){
				debug.error("Invalid membership's key : " + key);
			} else {
				notify('confirm', 'You really want to remove this membership?', function(){
					$.ajax({
						type		: 'DELETE',
						url			: self._apiBase + 'remove/' + key,
						dataType	: 'json'
					}).done(function(data) {	
						if(!data.removed  || data.error){
							debug.warn((data.error ? data.error : "unknown"));
							notify('warn', (data.error ? data.error : "unknown"));
						} else {
							notify('success', 'Membership removed');
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
	 * A callback with memberships in parameter
	 * @callback MembershipsCallback
	 * @param {Array} memberships - the retrieved memberships
	 */
	
	return MembershipController;
});