define(function(){
	return {
		build: function(){
			
			$('#submiter').button().click(function(event){
				event.preventDefault();
				$('#member-editor').submit();
			});
			
			//load member data
			var currentMemberId = $('body').data('member');
			if(currentMemberId){
				
				var toggleForm = function(){
					var $submiter = $('#submiter'),
						isDisabled = $submiter.button('option', 'disabled');
					$('input, select',$('#member-editor')).attr('disabled', !isDisabled);
					$submiter.button(isDisabled ? 'enable' : 'disable');
				}();
				
				$.ajax({
					type 		: 'GET',
					url 		: 'api/member/getOne',
					dataType 	: 'json',
					data		: {
						id : currentMemberId
					}
				}).done(function(data) {	
					toggleForm();
					if(!data || data.error){
						$.error("Error : " + (data.error ? data.error : "unknown"));
					} else {
						console.log(data)
					}
				});
			}
			
			//load events
			$.ajax({
				type 		: 'GET',
				url 		: 'api/event/list',
				dataType 	: 'json',
				data		: {
					current : true
				}
			}).done(function(data) {	
				if(!data || data.error){
					$.error("Error : " + (data.error ? data.error : "unknown"));
				} else if(!data.length){
					//data is empty no event
				} else {
					var template = "<option value='${key}'>${date} - ${title}</option>";
					$.tmpl(template, data).appendTo('#events-subscribed');
				}
			});
			
			
			//form controls
			
			$('#membership').change(function() {
				if ($(this).val() === 'true') {
					$('#events-subscribed').closest('div').show();
					$('#date-subscribed').closest('div').show();
				} else {
					$('#events-subscribed').closest('div').hide();
					$('#date-subscribed').closest('div').hide();
				}
			});
			
			$('#date-subscribed').datepicker({
				'dateFormat': 'yy-mm-dd'
			});
			
			$('#date-subscribed').change(function(){
				if($(this).val() !== ''){
					$('#events-subscribed').val('');
				}
			});
			$('#events-subscribed').change(function(){
				if($(this).val() !== ''){
					$('#date-subscribed').val('');
				}
			});
			
			//send data
			$('#member-editor').submit(function(event){
				event.preventDefault();
				
				var member = {
					'firstName' : $('#firstname').val(),
					'lastName'  : $('#lastname').val(),
					'email' 	: $('#email').val(),
					'company'  	: $('#company').val(),
					'roles' 	: $('#roles').val(),
					'membership': {
						'event' : {
							//TODO event
						}
					}
				};
				var validMemberShip = $('#membership').val() === 'true';
						
				$.ajax({
					type 		: 'PUT',
					url 		: 'api/member/add',
					contentType : 'application/x-www-form-urlencoded',
					dataType 	: 'json',
					data 		: {
						member : JSON.stringify(member),
						validMembership : validMemberShip
					}
				}).done(function(data) {
					if(!data.saved || data.error){
						$.error("Error : " + data.error ? data.error : "unknown");
					} else {
						alert("Saved");
					}
				});
				
				return false;
			});
		}
	};
});