/**
 * Browser storage, persistent if localStorage is supported 
 * or fallback to DOM data is not.
 */
define( ['modernizr'], function(Modernizr){
	var ls = (Modernizr.localstorage === true),
	
	/**
	 * @class Store
	 */
	Store = {
		
		/**
		 * @param {String} key
		 */
		get : function(key){
			if(ls){
				return localStorage.getItem(key);
			}
			return $('body').data(key)
		},
				
		/**
		 * @param {String} key
		 * @param {String} value
		 */
		set : function(key, value){
			if(ls){
				localStorage.setItem(key, value);
			} else {
				$('body').data(key, value);
			}
		},
				
		/**
		 * @param {String} key
		 */
		rm 	: function(key){
			if(ls){
				localStorage.removeItem(key);
			} else {
				$('body').removeData(key)
			}
		},
		
		/**
		 * @param {String} key
		 */
		isset : function(key){
			if(ls){
				return localStorage[key] !== undefined;
			}
			return $('body').data(key) !== undefined;
		}
	};
	
	return Store;
});