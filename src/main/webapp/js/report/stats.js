define(
	['jquery', 'controller/event', 'controller/membership', 'eventbus', 'chart'], 
	function($, EventController, MembershipController, EventBus){
	
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
			this.buildMembershipChart();
		},
		
		/**
		 * Build a bar like chart to compare registrants and participants 
		 */
		buildParticipantChart : function buildParticipantChart(){
			
			//(re)load the chart when this event is triggered 
			EventBus.subscribe('chart.participant.update', function(event, year){
				
				//get the event for the given year
				EventController.getAll(year, function(events){
					var i = 0, 
						$canvas = $('#stat-participants'),
						chart, 
						size = {
							width : 0,
							minW : 250,
							maxW : $('#stats').width(),
							height: 275
						},
						data = {
							labels : [],
							participants : [],
							registrants : []
						};
					if(events && $.isArray(events)){
					
						//extract the data to display
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

						//resize the canvas regarding the data
						size.width = data.labels.length * 100;
						if(size.width < size.minW){
							size.width = size.minW;
						} else if (size.width > size.maxW){
							size.width = size.maxW;
						} 
						$canvas.attr('width', size.width);
						$canvas.attr('height', size.height);
						
						//build the Bar Chart
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
			
			//get all the years with eve ts
			EventController.getYears(function(years){
				
				var $container = $('#participants-chart'),
					$selectYear = $('select', $container),
					i = 0;
				
				//hide the loader and show the chart
				$container.find('.loader').hide();
				$container.find('.chart').show();
				
				
				if(years.length > 0){
					//populate the select box
					for(i in years){
						$selectYear.append($.tmpl("<option value='${item}'>${item}</option>", {item : years[i]}));
					}

					//triger the event at startup and when the select box changes
					$selectYear.on('change', function(){
						EventBus.publish('chart.participant.update', [$(this).val()]);
					});
					
					EventBus.publish('chart.participant.update', [$selectYear.val()]);
				} else {
					$container.append('Nothing to display');
				}
			});
		},
		
		buildMembershipChart : function(){
			MembershipController.getAll(function(memberships){
				var i = 0, index = 0, width = 0, year = '',
				$container = $('#memberships-chart'),
				$canvas = $('#stat-memberships'),
				chart, 
				data = { labels : [], members: [] };
				
				for(i in memberships){
					year = memberships[i].year+'';
					index = $.inArray(year, data.labels);
					if(index === -1){
						index = data.labels.length;
						data.labels.push(year);
					}
					if(data.members[index] === undefined){
						data.members[index] = 0;
					}
					data.members[index]++;
				}
				
				//hide the loader and show the chart
				$container.find('.loader').hide();
				$container.find('.chart').show();
				
				//resize the canvas regarding the data
				width = data.labels.length * 150;
				$canvas.attr('width', (width < 250) ? 250 : width);
				$canvas.attr('height', '300');
				
				debug.info(data)
				
				//build the Bar Chart
				chart = new Chart($canvas.get(0).getContext("2d"));
				chart.Line({
						labels : data.labels,
						datasets : [{
							fillColor : "rgba(151,187,205,0.5)",
							strokeColor : "rgba(151,187,205,1)",
							pointColor : "rgba(151,187,205,1)",
							pointStrokeColor : "#fff",
							data : data.members
						}]
					}, { 
						scaleShowLabels : true,
						scaleOverride : true,
						scaleStartValue : 0,
						scaleStepWidth : 10,
						scaleSteps : 10,
				});
			});
		}
	};
	
	return Stats;
});