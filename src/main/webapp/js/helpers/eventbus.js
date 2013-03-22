define( ['jquery', 'debug'], function($){
	
	'use strict';
	
	/**
	 * The EventBus provides an element agnostic
	 * event subscription/publishing model.
	 * 
	 * @exports eventbus
	 */
	var EventBus = {
		
		/** 
		 * We use the document has a binding element,
		 * this element will centralize all the events.
		 * @private
		 */
		_$elt : $(document),
			
		/**
		 * Subscribe to an event
		 * @param {String} type - the event type, it can be any key, but namespaced key are encouraged
		 * @param {EventSubscriptionCallback} callback - to be executed once triggered
		 */
		subscribe : function(type, callback){
			debug.debug("subscribe " + type);
			this._$elt.on(type, callback);
		},
		
		/**
		 * Unsubscribe an event
		 * @param t{String} type - the event type
		 */
		unsubscribe : function(type){
			debug.debug("unsuscribe " + type);
			this._$elt.off(type);
		},
		
		/**
		 * Publish an event, it trigger the susbcription's callbacks 
		 * @param {String} type - the event type
		 * @param [Array] data - the additionnal parameter values for the callback
		 */
		publish : function(type, data){
			debug.debug("publish " + type);
			this._$elt.trigger(type, data);
		}
	};

	/**
	 * 
	 * @callback EventSubscriptionCallback
	 * @param {Object} event - the jQuery event object
	 * @param [anything] - extra parameters  
	 */
	
	return EventBus;
});