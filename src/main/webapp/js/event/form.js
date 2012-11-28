define(function(){
	/**
	 * TODO creates a Form objects that can be used by both the event and the user forms
	 */
	var EventForm = {
		
		form : $('#event-editor'),
		
		/**
		 * Enable/Disable the form fields
		 */
		toggleForm : function(){
			
			var $submiter = $('#submiter'),
				isDisabled = $submiter.button('option', 'disabled');
			$(':input', this.form).attr('disabled', !isDisabled);
			$submiter.button(isDisabled ? 'enable' : 'disable');
		},
		
		/**
		 * Initialize the controls behavior
		 */
		initControls : function(){
			
			var $form = this.form;
			
			// submit button
			$('#submiter', $form).button().click(function(event){
				event.preventDefault();
				$('#event-editor').submit();
			});
			
			// the date picker
			$('#date', $form).datepicker({
				'dateFormat': 'yy-mm-dd'
			});
			
			// on form submit
			$form.submit(function(event){
				event.preventDefault();
				
				var udpate = (member.key && member.key > 0);
						
				$.ajax({
					type 		: (udpate) ? 'POST' : 'PUT',
					url 		: (udpate) ? 'api/event/update' : 'api/event/add',
					contentType : 'application/x-www-form-urlencoded',
					dataType 	: 'json',
					data 		: {
						event : JSON.stringify($form.serializeArray())
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
		loadMember : function(eventId, callback){
			var $form = this.form;
			
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
						$(':input', $form).each(function(){
							if(data[$(this).attr('id')]){
								$(this).val(data[$(this).attr('id')]);
							}
						});
						if(typeof callback === 'function'){
							callback();
						}
					}
				});
			}
		},
		
		clear : function(){
			this.form.each(function(){
				this.reset();
			});
		}
	};
	
	return EventForm;
});