/**
 * URI helper module
 * @module uri
 */
define(function(){
	
	'use strict';
	
	/**
	 * @constructor
	 * @alias module:uri
	 */
	var URI = {
			
		/**
		 * Extract query parameters from an URI
		 * @memberOf uri
		 * @params {String} uri the URL to extract the query parameters from
		 * @returns an object that represents the parameters
		 */
		getQueryParams : function(uri){
			var params = {},
				queryParams = [],
				queryParam  = [],
				i = 0;
			if(uri && uri.indexOf('?') > -1){
				queryParams = uri.substring(uri.indexOf('?') + 1, uri.length).split('&');
				for(i in queryParams){
					if(queryParams[i].indexOf('=') > -1){
						queryParam = queryParams[i].split('=');
						if(queryParam.length === 2){
							params[queryParam[0]] = decodeURIComponent(queryParam[1]);
						}
					}
				}
			}
			return params;
		}
	};
	return URI;
});