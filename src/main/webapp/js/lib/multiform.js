/**
 * This module provides a MultiForm helper instance
 * @module multiform
 */
define(['jquery'], function($){
	'use strict';
	
	/**
	 * @constructor
	 * @alias module:multiform
	 */
	var MultiForm = {
			
			/**
			 * The forms id prefix
			 * Must be defined/overridden
			 * @memberOf module:form
			 */
			_id : '',
			
			/**
			 * The forms id suffix
			 * May be defined/overridden
			 * @memberOf module:form
			 */
			_suffix : 'editor',
			
			/**
			 * List of  sub forms
			 * Must be defined/overridden
			 * @memberOf module:form
			 */
			_formNames	: [],
			
			/**
			 * Stores the forms refs
			 * @private
			 * @memberOf module:form
			 */
			_forms		: {},
			
			/**
			 * Get the identifier of the form
			 * @param {String} the form name as defined in _formNames
			 * @returns {String} the id that usable by jQuery
			 */
			getFormId :  function(name){
				return '#' + this._id + '-' + name + '-' + this._suffix;
			},
			
			/**
			 * Get an editor form from it's name
			 * @memberOf module:form
			 * @param {String} name
			 * @returns {Object} the jQuery element that match the form
			 */
			getForm : function(name){
				var formId = this.getFormId(name);
				
				if($.inArray(name, this._formNames) < 0){
					return;
				}
				if(this._forms[name] === undefined || ($.isArray(this._forms[name]) && this._forms[name].length === 0)){
					if($(formId).length > 0){
						this._forms[name] = $(formId);
					}
				}
				
				return this._forms[name];
			},
			
			/**
			 * Get all the event's editor forms
			 * @memberOf module:event/form
			 * @returns {Array} of jQuery elements that match each form
			 */
			getForms : function(){
				var i, forms = [];
				for(i in this._formNames){
					forms.push(this.getForm(this._formNames[i]));
				}
				return forms;
			},
			
			/**
			 * Enable/Disable the form fields
			 * @deprecated
			 * @memberOf module:event/form
			 */
			toggleForm : function(){
				var $submiter, isDisabled, i, 
					forms = this.getForms();
				
				for(i in forms){
					$submiter = $('.submiter', forms[i]);
					isDisabled = $submiter.button('option', 'disabled');
					
					$(':input', forms[i]).attr('disabled', !isDisabled);
					$submiter.button(isDisabled ? 'enable' : 'disable');
				}
			}
	}
	return MultiForm;
});