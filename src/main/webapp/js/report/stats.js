define(['jquery', 'controller/event', 'eventbus', 'chart'], function($, EventController, EventBus){
	
	return {
		setUp : function(){
			EventBus.subscribe('chart.participant.update', function(event, year){
				
				EventController.getAll(year, function(events){
					
					var chart1; 
					var $canvas = $('#stat-participants');
					var labels = [];
					var participants = [];
					var registrants = [];
					
					for(var i in events){
						labels.push(events[i].date + " ");
						if(events[i].participants && $.isArray(events[i].participants)){
							participants.push(events[i].participants.length);
						} else {
							participants.push(0);
						}
						if(events[i].registrants && $.isArray(events[i].registrants)){
							registrants.push(events[i].registrants.length);
						} else {
							registrants.push(0);
						}
					}
					var width = labels.length * 125;
					$canvas.attr('width', (width < 250) ? 250 : width);
					$canvas.attr('height', '300');
					
					chart1 = new Chart($canvas.get(0).getContext("2d"));
					chart1.Bar({
						labels : labels,
						datasets : [
							{
								fillColor : "rgba(151,187,205,0.5)",
								strokeColor : "rgba(151,187,205,1)",
								data : registrants
							},
							{
								fillColor : "rgba(231,113,36,0.5)",
								strokeColor : "rgba(231,113,36,1)",
								data : participants
							}
						]
					}, {
						scaleShowLabels : true
					});
				});
			});
			
			EventController.getYears(function(years){
				if(years.length > 0){
					for(var i in years){
						$('#stat-participants-year')
							.append($.tmpl("<option value='${item}'>${item}</option>", {item : years[i]}));
					}
					$('#stat-participants-year').on('change', function(){
						EventBus.publish('chart.participant.update', [$(this).val()]);
					});
					EventBus.publish('chart.participant.update', [$('#stat-participants-year').val()]);
				}
				//TODO throw error else
			});
		}
	};
	
});