define(['modernizr', 'notify', 'store', 'jhtmlarea'], function(Modernizr, notify, store){
	
	/**
	 * @todo creates a Form objects that can be used by both the event and the user forms
	 * @class
	 */
	var EventForm = {
		
		/**
		 * list of forms
		 * @private 
		 */
		_formNames	: ['infos', 'flyer', 'participant'],
		
		/**
		 * Store the forms 
		 * @private
		 */
		_forms		: {},
		
		/**
		 * Get an event's editor form from it's name
		 * @param {String} name
		 * @return {Object} the jQuery element  that match the form
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
		 * @param {Function} a callback executed once the controls are initialized
		 */
		initControls : function(callback){
			var i, $submiter, $date,
				self 		= this,
				forms 		= this.getForms(),
				$infosForm 	= this.getForm('infos'),
				$flyerForm 	= this.getForm('flyer'),
				$partForm	= this.getForm('participant');
			
			for(i in forms){
				$submiter  = $('.submiter', forms[i]);
				
				// submit button
				$submiter.button({label : $submiter.val(), disabled : false});
			}
			
			//init infos controls
			
			// the date picker
			$date = $('#date', $infosForm);
			if(!Modernizr.inputtypes.date){
				$date.datepicker({
					'dateFormat': 'yy-mm-dd'
				});
			}
			
			$date.on('change', function(){
				if(self.getEventId() !== undefined && 
					this.value && this.value.length === 10){
					
					$flyerForm.show();
					
				} else {
					$flyerForm.hide();
				}
			});
			
			$('textarea', $infosForm)
				.htmlarea({
						toolbar: [
	                        'bold', 'italic', 'underline', '|',
	                        'h1', 'h2', 'h3', 'h4', 'h5', 'h6', '|',
	                        'link', 'unlink', '|',
	                        'orderedList', 'unorderedList', 'indent', 'outdent'
	                    ],
	                    css : 'css/lib/jhtmlarea/jhtmlarea-editor.css'
					});
			
			// on form submit
			$infosForm.submit(function(e){
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
			
			//init flyer controls
			
			$('#flyer-remover', $flyerForm).click(function(){
				$('#current-flyer',  $flyerForm).removeAttr('src');
			});
			
			$('#flyer-viewer', $flyerForm).click(function(){
				if($('#current-flyer')){
					window.open($('#current-flyer').attr('src').replace('-small', ''));
				}
			});
			
			$('.submiter', $flyerForm).click(function(e){
				e.preventDefault();
				
				//clean up 
				$("#postFrame", $flyerForm).remove();
				
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
				$flyerForm.attr({
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
			
			
			//init participant controls
			
			//toggle registrants field between input and textarea
			$('#expand-registrants', $partForm)
				.button({
					icons: { primary: "icon-expand" },
					text : false
				})
				.click(function(){
					var $field		 = $('#registrants'),
						$newField, title;
					if($field.get(0).tagName.toLowerCase() === 'textarea'){
						$newField = $("<input type='text' />");
						title = 'Add single registrant';
					} else {
						$newField = $("<textarea></textarea>");
						title = 'Add mulitple registrants';
					}
					$newField.attr({
							id		: 'registrants', 
							name	: 'registrants',
							title	: title
						})
						.css('width', '40%')
						.val($field.val());
					$field.after($newField).remove();
					
					return false;
				});
				
			
			
			if(typeof callback === 'function'){
				callback();
			}
		},
		
		/**
		 * Load an event from the server and dispatch the data to the forms
		 * @param {Number} the identifier of the event to load
		 * @param {Function} a callback executed once the event is loaded
		 */
		loadEvent : function(eventId, callback){
			var self	= this; 
			
			if(eventId && eventId > 0){
				self.toggleForm();
				
				$.ajax({
					type 		: 'GET',
					url 		: 'api/event/getOne',
					dataType 	: 'json',
					data		: {
						id : eventId
					}
				}).done(function(data) {	
					self.toggleForm();
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