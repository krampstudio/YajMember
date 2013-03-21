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
		 */
		_ls : (Modernizr.localstorage === true),
		
		/**
		 * namespace the entries to prevent collisions from other sites
		 */
		_ns : 'yajmember',
		
		/**
		 * Get an entry from the store
		 * @param {String} key - the entry key
		 * @returns the value or undefined
		 */
		get : function(key){
			if(ls){
				return localStorage.getItem(ns + '.' + key);
			}
			return $('body').data(ns + '.' + key);
		},
				
		/**
		 * Set an entry to the store
		 * @param {String} key - the entry key
		 * @param {String} value - the value bound to the key
		 */
		set : function(key, value){
		//	console.log("set " + key  + " with " + value);
			if(ls){
				localStorage.setItem(ns + '.' + key, value);
			} else {
				$('body').data(ns + '.' + key, value);
			}
		},
				
		/**
		 * Remove an entry from the store 
		 * @param {String} key - the entry key
		 */
		rm : function(key){
		//	console.log("removes " + key );
			if(ls){
				localStorage.removeItem(ns + '.' + key);
			} else {
				$('body').removeData(ns + '.' + key);
			}
		},
		
		/**
		 * Check if an entry exists in the store
		 * @param {String} key - the entry key
		 */
		isset : function(key){
			if(ls){
				return localStorage[ns + '.' + key] !== undefined;
			}
			return $('body').data(ns + '.' + key) !== undefined;
		}
	};

	return Store;
});