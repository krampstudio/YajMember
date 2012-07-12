function initForm(){
	
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