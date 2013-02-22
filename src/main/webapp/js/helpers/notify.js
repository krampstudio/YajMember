/**
 * Notification system based on Noty
 * @module notify
 */
define(['jquery',
        'noty-base/layouts/topCenter', 
        'noty-base/layouts/center',
        'noty-base/themes/default'], 
function($){
	
	'use strict';
	
	$.noty.defaults.timeout = 20000;
	
	/**
	 * @constructor
	 * @alias module:notify
	 * @param {String} type the type of notification in alert, info, confirm, success, error or warning
	 * @param {String} msg the message to display in the notification
	 * @param {Function} [callback] a function executed after the notification closes (or when confirm is 'OK')
	 */
	return  function(type, msg, callback){
		var topLayout = 'topCenter',
			centerLayout = 'center',
			layouts = {		//map types to layout
				'alert'		: centerLayout,
				'info'		: topLayout,
				'confirm'	: centerLayout,
				'success'	: topLayout,
				'error'		: topLayout,
				'warning'	: topLayout
			},
			types = {		//re-map some types
				'info'		: 'information',
				'confirm'	: 'warning'
			};
		
		if(msg && type){
			return noty({
				text	: msg, 
				layout	: layouts[type] || topLayout, 
				type	: types[type] ? types[type] : type,
				timeout	: 10000,
				dismissQueue: true,
				buttons : type != 'confirm' ? false : [{
					addClass : 'btn btn-primary',
					text : 'Yes',
					onClick : function($noty) {
						$noty.close();
						if(typeof callback === 'function'){
							callback();							
						}
					}
				}, {
					addClass : 'btn btn-danger',
					text : 'No',
					onClick : function($noty) {
						$noty.close();
					}
				}] ,
				afterClose : function(){
					if(type != 'confirm' && typeof callback === 'function'){
						callback();							
					}
				}
			});
		}
	};
});