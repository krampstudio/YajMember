define( [], function(){
	return {
		load: function(callback){
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
		},
		setUpControls: function(){
			var getEventId = function($elt){
				return $elt.parents('li.event').attr('id').replace('event-', '');
			}
			$('.event-editor')
				.button({icons: { primary: "icon-evt-edit" }})
				.click(function(){
					console.log(getEventId($(this)));
				});
			$('.event-deletor')
				.button({icons: { primary: "icon-evt-delete" }})
				.click(function(){
					console.log(getEventId($(this)));
				});
		}
	};
});