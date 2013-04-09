define(['jquery', 'controller/event', 'eventbus', 'chart'], function($, EventController, EventBus){
	
	'use strict';
	
	/**
	 * Builds dynamic charts.
	 * 
	 * @exports report/stats
	 */
	var Stats = {
		
		/**
		 * Conventional method used to initialize the module
		 */
		setUp : function setUp(){
			this.buildParticipantChart();
		},
		
		/**
		 * Build a bar like chart to compare registrants and participants 
		 */
		buildParticipantChart : function buildParticipantChart(){
			
			EventBus.subscribe('chart.participant.update', function(event, year){
				
				EventController.getAll(year, function(events){
					var i = 0, width = 0,
						$canvas = $('#stat-participants'),
						chart, 
						data = {
							labels : [],
							participants : [],
							registrants : []
						};
					if(events && $.isArray(events)){
					
						for(i in events){
							data.labels.push(events[i].date + " ");
							if(events[i].participants && $.isArray(events[i].participants)){
								data.participants.push(events[i].participants.length);
							} else {
								data.participants.push(0);
							}
							if(events[i].registrants && $.isArray(events[i].registrants)){
								data.registrants.push(events[i].registrants.length);
							} else {
								data.registrants.push(0);
							}
						}
						width = data.labels.length * 100;
						$canvas.attr('width', (width < 250) ? 250 : width);
						$canvas.attr('height', '300');
						
						debug.debug(data)
						
						chart = new Chart($canvas.get(0).getContext("2d"));
						chart.Bar({
							labels : data.labels,
							datasets : [{
								fillColor : "rgba(151,187,205,0.5)",
								strokeColor : "rgba(151,187,205,1)",
								data : data.participants
							},{
								fillColor : "rgba(231,113,36,0.5)",
								strokeColor : "rgba(231,113,36,1)",
								data : data.registrants
								
							}]
						}, {
							scaleShowLabels : true
						});
					}
				});
			});
			
			EventController.getYears(function(years){
				
				var $container = $('#participants-chart'),
					$selectYear = $('select', $container),
					i = 0;
				
				$container.find('.loader').hide();
				$container.find('.chart').show();
				
				if(years.length > 0){
					for(i in years){
						$selectYear.append($.tmpl("<option value='${item}'>${item}</option>", {item : years[i]}));
					}
					$selectYear.on('change', function(){
						EventBus.publish('chart.participant.update', [$(this).val()]);
					});
					
					EventBus.publish('chart.participant.update', [$selectYear.val()]);
				} else {
					$container.append('Nothing to display');
				}
			});
		}
	};
	
	return Stats;
});