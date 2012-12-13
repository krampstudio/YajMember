define(['modernizr', 'notify', 'jhtmlarea'], function(Modernizr, notify){
	
	/**
	 * TODO creates a Form objects that can be used by both the event and the user forms
	 */
	var EventForm = {
		
		_formNames	: ['infos', 'flyer'],
		_forms		: {},
		
		getForm : function(name){
			if($.inArray(name, this._formNames) < 0){
				return;
			}
			if(this._forms[name] === undefined || ($.isArray(this._forms[name]) && this._forms[name].length === 0)){
				this._forms[name] = $('#event-'+name+'-editor');
			}
			return this._forms[name];
		},
		
		getForms : function(){
			var name, forms = [];
			for(name in this._formNames){
				forms.push(this.getForm(name));
			}
			return forms;
		},
		
		/**
		 * Enable/Disable the form fields
		 */
		toggleForm : function(){
			var $submiter, isDisabled,
				i, forms = this.getForms();
			
			for(i in forms){
				$submiter = $('.submiter', forms[i]);
				isDisabled = $submiter.button('option', 'disabled');
				
				$(':input', forms[i]).attr('disabled', !isDisabled);
				$submiter.button(isDisabled ? 'enable' : 'disable');
			}
		},
		
		/**
		 * Initialize the controls behavior
		 */
		initControls : function(){
			console.log('initControls');
			var self = this,
				i, forms = this.getForms(),
				$submiter, $date;
			
			for(i in forms){
				$submiter  = $('.submiter', forms[i]);
				// submit button
				$submiter.button({label : $submiter.val()});
			}
			
			// the date picker
			$date = $('#date', this.getForm('infos'));
			if(!Modernizr.inputtypes.date){
				$date.datepicker({
					'dateFormat': 'yy-mm-dd'
				});
			}
			
			$date.on('change', function(){
				if(this.value && this.value.length === 10){
					self.getForm('flyer').show();
				} else {
					self.getForm('flyer').hide();
				}
			});
			
			$('textarea', this.getForm('infos'))
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
			this.getForm('infos').submit(function(event){
				event.preventDefault();
				
				var udpate = (member.key && member.key > 0);
						
				$.ajax({
					type 		: (udpate) ? 'POST' : 'PUT',
					url 		: (udpate) ? 'api/event/update' : 'api/event/add',
					contentType : 'application/x-www-form-urlencoded',
					dataType 	: 'json',
					data 		: {
						event : JSON.stringify($(this).serializeArray())
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
		 * Load an event
		 * @param {Number} the identifier of the event to load
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
						console.log($(':input', self.getForm('infos')));
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
		
		clear : function(){
			$.each(this.getForms(), function(index, elt){
				$(':input', elt).val('');
			});
			console.log('cleared');
			/*this.getForm('infos').each(function(index, elt){
				elt.reset();
			});
			$('#current-flyer',  this.getForm('flyer')).removeAttr('src');*/
		}
	};
	
	return EventForm;
});