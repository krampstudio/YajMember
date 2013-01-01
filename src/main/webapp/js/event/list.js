define( ['store', 'notify'], function(store, notify){
	
	/**
	 * @class
	 */
	var EventList = {
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
			var self = this;
			var getEventId = function($elt){
				return $elt.parents('li.event').attr('id').replace('event-', '');
			}
			
			//edit event button
			$('.event-editor')
				.button({icons: { primary: "icon-evt-edit" }})
				.click(function(event){
					event.preventDefault();
					
					store.set('event', getEventId($(this)));
					
					//opens the edit tab
					$('#actions').tabs('select', 3);
					
					return false;
				});
			
			//remove event button
			$('.event-deletor')
				.button({icons: { primary: "icon-evt-delete" }})
				.click(function(){
					if(notify('confirm', 'You really want to remove this event?')){
						self._rmEvent(getEventId($(this)));
					}
				});
		},
		_rmEvent: function(eventId){
			$.ajax({
				type 		: 'DELETE',
				url 		: 'api/event/remove',
				dataType 	: 'json',
				data 		: {id : eventId}
			}).done(function(data) {	
				if(data && data.removed === true){
					notify('success', 'Event removed');
				}
			});
		}
	};
	
	return EventList;
});