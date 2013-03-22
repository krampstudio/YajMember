define(['jquery', 'notify', 'store'], function($, notify, store){
	
	'use strict';
	
	/**
	 * Enables you to send and retrieve member's data from/to the server
	 * @exports controller/member
	 */
	var MemberController = {

		/** the base path for the server calls*/
		_apiBase : 'api/member/',
		
		
		/**
		 * Call the API to get a member
		 * @param {String} key - the member identifier
		 * @param {MemberCallback} callback - called once retrieved, with the member in parameter
		 */
		getOne : function(key, callback){
			if(!key){
				$.error('Invalid member key : ' + key);
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
		 * Call the API to save a member
		 * @param {Object} member - the member to save, if it has no key attribute, it will be inserted
		 * @param {Function} callback - called if there was no errors
		 */
		save : function(event, callback){
			if(!event){
				$.error('Invalid member : ' + member);
			} else {
				var update = event.key !== undefined;
				$.ajax({
					type		: update ? 'POST' : 'PUT',
					url			: this._apiBase + (update ? 'update' : 'add'),
					contentType	: 'application/x-www-form-urlencoded',
					dataType	: 'json',
					data		: {
						member : JSON.stringify(member)
					}
				}).done(function(data) {
					if(!data.saved || data.error){
						$.error("Error : " + data.error ? data.error : "unknown");
					} else {
						notify('success', 'The member has been saved');
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
				$.error("Invalid member's key : " + key);
			} else {
				notify('confirm', 'You really want to remove this member?', function(){
					$.ajax({
						type		: 'DELETE',
						url			: self._apiBase + 'remove/' + key,
						dataType	: 'json'
					}).done(function(data) {	
						if(!data.removed  || data.error){
							$.error("Error : " + data.error ? data.error : "unknown");
						} else {
							if(store.get('member') === key){
								store.rm('member');
							}
							notify('success', 'Member removed');
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
	 * A callback with an member in parameter
	 * @callback MemberCallback
	 * @param {Object} member - a retrieved member
	 */
	
	return MemberController;
});