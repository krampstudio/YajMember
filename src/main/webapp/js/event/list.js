define( [], function(){
	return {
		load: function(){
			//load events
			$.ajax({
				type 		: 'GET',
				url 		: 'api/event/list',
				dataType 	: 'json'
			}).done(function(data) {	
				if(!data || data.error){
					$.error("Error : " + (data.error ? data.error : "unknown"));
				} else if(!data.length){
					//data is empty no event
				} else {
					var template = $('#event-item-template');
					$.tmpl(template, data).appendTo('#events');
				}
				if(typeof callback === 'function'){
					callback();
				}
			});
		}
	};
});