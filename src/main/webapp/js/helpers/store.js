define( ['jquery', 'modernizr'], function($){
	
	'use strict';
	
	/**
	 * Browser persistent key/value storage,   
	 * it uses the localStorage is supported 
	 * or fallback to DOM data attr but not persistent anymore
	 * 
	 * @exports store
	 */
	var Store = {
			
		/**
		 * Is localStorage supported
		 * @private
		 */
		_ls : (Modernizr.localstorage === true),
		
		/**
		 * namespace the entries to prevent collisions from other sites
		 * @private
		 */
		_ns : 'yajmember',
		
		/**
		 * Get an entry from the store
		 * @param {String} key - the entry key
		 * @returns {String} the value or undefined
		 */
		get : function(key){
			if(this._ls){
				return localStorage.getItem(this._ns + '.' + key);
			}
			return $('body').data(this._ns + '.' + key);
		},
				
		/**
		 * Set an entry to the store
		 * @param {String} key - the entry key
		 * @param {String} value - the value bound to the key
		 */
		set : function(key, value){
			debug.debug("set " + key  + " with " + value);
			if(this._ls){
				localStorage.setItem(this._ns + '.' + key, value);
			} else {
				$('body').data(this._ns + '.' + key, value);
			}
		},
				
		/**
		 * Remove an entry from the store 
		 * @param {String} key - the entry key
		 */
		rm : function(key){
			debug.debug("removes " + key );
			if(this._ls){
				localStorage.removeItem(this._ns + '.' + key);
			} else {
				$('body').removeData(this._ns + '.' + key);
			}
		},
		
		/**
		 * Check if an entry exists in the store
		 * @param {String} key - the entry key
		 * @returns {Boolean} true if the entry exists
		 */
		isset : function(key){
			if(this._ls){
				return localStorage[this._ns + '.' + key] !== undefined;
			}
			return $('body').data(this._ns + '.' + key) !== undefined;
		}
	};

	return Store;
});