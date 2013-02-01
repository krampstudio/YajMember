/**
 * Manage event's Form UI and IO
 * @module event/form
 */
define(['modernizr', 'notify', 'store', 'jhtmlarea'], function(Modernizr, notify, store){
	
	/**
	 * @todo creates a Form objects that can be used by both the event and the user forms
	 * @constructor
	 * @alias module:event/form
	 */
	var EventForm = {
		
		/**
		 * List of  sub forms
		 * @private 
		 * @memberOf module:event/form
		 */
		_formNames	: ['infos', 'flyer', 'participant'],
		
		/**
		 * Stores the forms refs
		 * @private
		 * @memberOf module:event/form
		 */
		_forms		: {},
		
		/**
		 * Get an event's editor form from it's name
		 * @memberOf module:event/form
		 * @param {String} name
		 * @return {Object} the jQuery element that match the form
		 */
		getForm : function(name){
			
			if($.inArray(name, this._formNames) < 0){
				return;
			}
			if(this._forms[name] === undefined || ($.isArray(this._forms[name]) && this._forms[name].length === 0)){
				if($('#event-'+name+'-editor').length > 0){
					this._forms[name] = $('#event-'+name+'-editor');
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
		 * Initialize the controls behaviors, including form submit event handlers
		 * @memberOf module:event/form
		 * @param {Function} a callback executed once the controls are initialized
		 */
		initFormControls : function(callback){
			var i, $submiter, 
				forms 		= this.getForms();
			
			//setup forms submit buttons
			for(i in forms){
				$submiter  = $('.submiter', forms[i]);
				$submiter.button({label : $submiter.val(), disabled : false});
			}
			this._initInfosControls(this.getForm('infos'));
			this._initFlyerControls(this.getForm('flyer'));
			this._initParticipantControls(this.getForm('participant'));
			
			if(typeof callback === 'function'){
				callback();
			}
		},
		
		/**
		 * Initialize the controls for the Infos form.
		 * @private
		 * @memberOf module:event/form
		 * @param {Object} the jquery element the reference the form
		 */
		_initInfosControls : function($form){
			var self = this,
				$date = $('#date', $form); // the date picker
			
			if(!Modernizr.inputtypes.date){
				$date.datepicker({
					'dateFormat': 'yy-mm-dd'
				});
			}
			$date.on('change', function(){
				if(self.getEventId() !== undefined 
						&& this.value 
						&& this.value.length === 10){
					self.getForm('flyer').show();
				} else {
					self.getForm('flyer').hide();
				}
			});
			
			$('textarea', $form).htmlarea({
				toolbar: [
                    'bold', 'italic', 'underline', '|',
                    'h1', 'h2', 'h3', 'h4', 'h5', 'h6', '|',
                    'link', 'unlink', '|',
                    'orderedList', 'unorderedList', 'indent', 'outdent'
                ],
                css : 'css/lib/jhtmlarea/jhtmlarea-editor.css'
			});
			
			// on form submit
			$form.submit(function(e){
				e.preventDefault();
				
				var udpate = $('#key', $(this)).val().length > 0;
						
				$.ajax({
					type 		: (udpate) ? 'POST' : 'PUT',
					url 		: (udpate) ? 'api/event/update' : 'api/event/add',
					contentType : 'application/x-www-form-urlencoded',
					dataType 	: 'json',
					data 		: {
						event : JSON.stringify(self.serializeEvent($(this)))
					}
				}).done(function(data) {
					if(!data.saved || data.error){
						$.error("Error : " + data.error ? data.error : "unknown");
					} else {
						notify('success', 'Saved');
					}
				});
				
				return false;
			});
		},
		
		/**
		 * Initialize the controls for the Flyer form.
		 * @private
		 * @memberOf module:event/form
		 * @param {Object} the jquery element the reference the form
		 */
		_initFlyerControls : function($form){
			var self = this;
			
			$('#flyer-remover').button({
				icons : { primary: "icon-delete" },
				text : false
			}).click(function(){
				$('#current-flyer').removeAttr('src');
			});
			
			$('#flyer-viewer').button({
				icons : { primary: "icon-image" },
				text : false
			}).click(function(){
				if($('#current-flyer')){
					window.open($('#current-flyer').attr('src').replace('-small', ''));
				}
			});
			
			$('.submiter', $form).click(function(e){
				e.preventDefault();
				
				//clean up 
				$("#postFrame", $form).remove();
				
				//we create an hidden frame as the action of the upload form (to prevent page reload)
				var $postFrame = $("<iframe id='postFrame' />");
				$postFrame
					.attr('name', 'postFrame')
					.css('display', 'none')
					.load(function(){
						
						//we get the response in the frame
						var result = $.parseJSON($(this).contents().text());
						if(result && result.saved === true){
							$('#current-flyer').attr('src', result.thumb);
							notify('success', 'Flyer uploaded');
						} else {
							notify('error', 'Flyer upload error');
						}
					});
				
				//we update the form attributes according to the frame
				$form.attr({
						'action'	: 'api/event/flyer/'+self.getEventId(),
						'method'	: 'POST',
						'enctype'	: 'multipart/form-data',
						'encoding'	: 'multipart/form-data',
						'target'	: 'postFrame'
					})
					.append($postFrame)
					.submit();
				return false;
			});
		},
		
		/**
		 * Initialize the controls for the Participant form.
		 * @private
		 * @memberOf module:event/form
		 * @param {Object} the jquery element the reference the form
		 */
		_initParticipantControls : function($form){
			var self = this;
			
			/**
			 * Set up autocomplete field
			 */
			var setUpAutocomplete = function(){
				$('input#registrants').autocomplete({
					minLength : 2,
					delay: 600,
					source : 'api/member/acSearch',
					select : function(event, ui){
						event.preventDefault();
						
						var $item = $('<li></li>')
							.addClass('ui-state-default')
							.attr('id', 'registered-' + ui.item.value)
							.text(ui.item.label);
						
						$('#registered').append($item);
						
						return false;
					}
				});
			};
			setUpAutocomplete();
			
			//toggle registrants field between input and textarea
			$('#expand-registrants').button({
				icons: { primary: "icon-expand" },
				text : false
			}).click(function(){
				var $field 			= $('#registrants'),
					singleTitle 	= 'Add a registrant',
					mulitpleTitle	= 'Add multiple registrants', 
					expand			= $field.get(0).tagName.toLowerCase() === 'input',
					$newField, title;
				
				if(expand){
					$newField = $("<textarea></textarea>");
					title = singleTitle;
					$(this).button('option', 'label', mulitpleTitle);
				} else {
					$newField = $("<input type='text' />");
					title = mulitpleTitle;
					$(this).button('option', 'label', singleTitle);
				}
				$newField.attr({
					id		: 'registrants', 
					name	: 'registrants',
					title	: title
				}).val($field.val());
				
				//remove the old & insert the new field
				$field.after($newField).remove();
				if(!expand){
					//we need to set it up again because it is a new instance!
					setUpAutocomplete();
				}
				return false;
			});
			
			//move items from registrants to participants list box
			$('.list-box-ctrl a.ltr').button({
				icons: { primary: "ui-icon-carat-1-e" },
				text : false
			}).click(function(){
				$('#registered li.ui-selected').each(function(index, elt){
					var id = 'participant-' + $(elt).attr('id').replace('registered-', ''),
						$item = $('<li></li>');
					if($('#' + id).length === 0){
						$item.addClass('ui-state-default')
							.attr('id', id)
							.text($(elt).text());
						
						$('#participant').append($item);
					}
					$(elt).removeClass('ui-selected ui-state-highlight');
				});
				return false;
			});
			
			//remove participants from the list box
			$('.list-box-ctrl a.rtl').button({
				icons: { primary: "ui-icon-carat-1-w" },
				text : false
			}).click(function(){
				$('#participant li.ui-selected').remove();
				return false;
			});
			
			//remove registered from the list box
			$('.list-box-ctrl a.rml').button({
				icons: { primary: "ui-icon-trash" },
				text : false
			}).click(function(){
				$('#registered li.ui-selected').remove();
				return false;
			});
			
			//enable list multiple selection
			$('.list-box ul').selectable({
				selected: function(event, ui){
					$(ui.selected).addClass('ui-state-highlight');
				},
				unselected: function(event, ui){
					$(ui.unselected).removeClass('ui-state-highlight');
				}
			});
			
			//submit the registered and participants lists
			$('.submiter', $form).click(function(e){
				e.preventDefault();
				
				var lists = {
					registered : [],
					participant : []
				};
				
				//get the ids of the both lists and push them to the lists arrays
				$('.list-box ul', $form).each(function(){
					var listId = $(this).attr('id');
					$('li', $(this)).each(function(){
						lists[listId].push($(this).attr('id').replace(listId+'-', ''));
					});
				});
				
				$.ajax({
					type 		: 'POST',
					url 		: 'api/event/updateParticipant/'+self.getEventId(),
					contentType : 'application/x-www-form-urlencoded',
					dataType 	: 'json',
					data 		: {
						registered  : JSON.stringify(lists.registered),
						participant : JSON.stringify(lists.participant)
					}
				}).done(function(data) {
					if(!data.saved || data.error){
						$.error("Error : " + (data.error ? data.error : "unknown"));
					} else {
						notify('success', 'Saved');
					}
				});
				
				return false;
			});
		},
		
		/**
		 * Load an event from the server and dispatch the data to the forms
		 * @memberOf module:event/form
		 * @param {Number} the identifier of the event to load
		 * @param {Function} a callback executed once the event is loaded
		 */
		loadEvent : function(eventId, callback){
			var self	= this; 
			
			if(eventId && eventId > 0){
				$.ajax({
					type 		: 'GET',
					url 		: 'api/event/getOne',
					dataType 	: 'json',
					data		: {
						id : eventId
					}
				}).done(function(data) {	
					if(!data || data.error){
						$.error("Error : " + (data.error ? data.error : "unknown"));
					} else {
						
						$(':input', self.getForm('infos')).each(function(){
							if(data[$(this).attr('id')]){
								$(this).val(data[$(this).attr('id')]);
							}
						});
						if(data['date']){
							$('#current-flyer',  self.getForm('flyer')).attr('src', 'img/events/event-'+data.date.replace(/\-/g,'')+'-small.png');
						}
						if(data['description']){
							$('#description', self.getForm('infos')).htmlarea('html', data.description.replace(/\\n/g, '<br />'));
						}
						$('#date', self.getForm('infos')).trigger('change');
						
						if(typeof callback === 'function'){
							callback();
						}
					}
				});
			}
		},
		
		/**
		 * Creates an object from the form data
		 * @memberOf module:event/form
		 * @returns {Object} that represents the inputs data
		 */
		serializeEvent : function($form){
			var event = {};
			if($form){
				if($form.prop('tagName') !== 'FORM'){
					$.error('Invalid jQuery element for $form. It much match a form tag.')
				}
				$.map($form.serializeArray(), function(elt, index){
					if(event[elt.name] === undefined && elt.value){
						if( elt.value.trim().length > 0){
							event[elt.name] = elt.value;
						}
					}
				});
				event['description'] = $('#description', $form).htmlarea('toHtmlString');
			}
			return event;
		},
		
		/**
		 * Get the id of the event currently selected
		 * from either the store or the input field
		 * @memberOf module:event/form
		 * @returns {Number} the id or undefined if not found
		 */
		getEventId : function(){
			var $keyField;
			if(store.isset('event')){
				return store.get('event');
			}
			$keyField = $('#key', this.getForm('infos'));
			if($keyField.length > 0 && $keyField.val().trim().length > 0){
				return $keyField.val();
			}
		},
		
		/**
		 * Clear the event forms
		 * @memberOf module:event/form
		 */
		clear : function(){
			$.each(this.getForms(), function(index, elt){
				$(':input', elt).val('');
			});
			$('#description', this.getForm('infos')).htmlarea('updateHtmlArea');
			$('#current-flyer',  this.getForm('flyer')).removeAttr('src');
		}
	};
	
	return EventForm;
});