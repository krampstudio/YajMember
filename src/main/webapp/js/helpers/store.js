/**
 * Browser storage, persistent if localStorage is supported 
 * or fallback to DOM data is not.
 */
define( ['jquery', 'modernizr'], function($){
	
	'use strict';
	
	var ls = (Modernizr.localstorage === true),
		ns = 'yajmember',
	
	/**
	 * @class Store
	 */
	Store = {
		
		/**
		 * @param {String} key
		 */
		get : function(key){
			if(ls){
				return localStorage.getItem(ns + '.' + key);
			}
			return $('body').data(ns + '.' + key);
		},
				
		/**
		 * @param {String} key
		 * @param {String} value
		 */
		set : function(key, value){
			if(ls){
				localStorage.setItem(ns + '.' + key, value);
			} else {
				$('body').data(ns + '.' + key, value);
			}
		},
				
		/**
		 * @param {String} key
		 */
		rm : function(key){
			if(ls){
				localStorage.removeItem(ns + '.' + key);
			} else {
				$('body').removeData(ns + '.' + key);
			}
		},
		
		/**
		 * @param {String} key
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