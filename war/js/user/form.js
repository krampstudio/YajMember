function initForm(){
	
	$('#member-editor').submit(function(event){
		event.preventDefault();
		
		var formData = $(this).serializeArray();
		console.log(formData)
		var formParam = JSON.stringify(formData)
		console.log(formParam)
		
		$.post('/api/user/add', {member: formParam), function(data) {
			
			if(data && data.saved === true){
				alert('user saved');
			}
		}, 'json');
		
		return false;
	});
	
}