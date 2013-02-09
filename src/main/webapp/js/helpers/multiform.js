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
			},
			
			/**
			 * Calls dynamically the sub forms initializer based on naming convention:
			 * _initFormnameControls(Object) 
			 * where Formname is the name of the subform as defined by the _formNames array
			 * 
			 * @memberOf module:multiform
			 * @param {Function} a callback executed once the controls are initialized
			 */
			initFormControls : function(callback){
				var i, $submiter, name, init;
				
				for(i in this._formNames){
					name = this._formNames[i];
					
					//setup forms submit buttons
					$submiter  = $('.submiter', this.getForm(name));
					$submiter.button({label : $submiter.val(), disabled : false});
					
					//setup form controls based on method name
					init = '_init' + name[0].toUpperCase() + name.slice(1) + 'Controls';
					if(this[init] !== undefined && typeof this[init] === 'function'){
						this[init].apply(this, [this.getForm(name)]);
					}
				}
				
				if(typeof callback === 'function'){
					callback();
				}
			},
			
			/**
			 * Clear the sub forms and call specific clear method based on naming convention:
			 * _clearFormnameForm
			 * where Formname is the name of the subform as defined by the _formNames array
			 * 
			 * @memberOf module:multiform
			 * @param {Function} a callback executed once the forms are cleared
			 */
			clearForms : function(callback){
				var i, $form, name, clearForm;
				
				for(i in this._formNames){
					name = this._formNames[i];
					$form = this.getForm(name);
					if($form.length){
						$form.each(function(){
							this.reset();
						});
						clearForm = '_clear' + name[0].toUpperCase() + name.slice(1) + 'Form';
						if(this[clearForm] !== undefined && typeof this[clearForm] === 'function'){
							this[clearForm].call(this, $form);
						}
					}
				}
				if(typeof callback === 'function'){
					callback();
				}
			}
	}
	return MultiForm;
});