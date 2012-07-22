define(function(){
	return {
		build: function(){

			$.ajax({
				type 		: 'GET',
				url 		: '/api/event/list',
				dataType 	: 'json',
			}).done(function(data) {
				if(!data.length || data.error){
					alert("Error : " + data.error ? data.error : "unknown");
				} else {
					var template = "<option value='${date}'>${date} - ${description}</option>";
					$.tmpl(template, data).appendTo('#events-subscribed');
				}
			});
			
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
			})
			
			$('#member-editor').submit(function(event){
				event.preventDefault();
				
				var member = {
					'firstName' : $('#firstname').val(),
					'lastName'  : $('#lastname').val(),
					'email' 	: $('#email').val(),
					'company'  	: $('#company').val(),
					'roles' 	: $('#roles').val()
				};
				var validMemberShip = $('#membership').val() === 'true';
						
				$.ajax({
					type 		: 'PUT',
					url 		: '/api/user/add',
					contentType : 'application/x-www-form-urlencoded',
					dataType 	: 'json',
					data 		: {
						member : JSON.stringify(member),
						validMembership : validMemberShip
					}
				}).done(function(data) {
					if(!data.saved || data.error){
						alert("Error : " + data.error ? data.error : "unknown");
					} else {
						alert("Saved");
					}
				});
				
				return false;
			});
		}
	}
});