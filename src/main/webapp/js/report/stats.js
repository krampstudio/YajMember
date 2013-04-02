define(['jquery', 'chart'], function($){
	
	return {
		setUp : function(){
			var chart1 = new Chart($('members').get(0).getContext("2d"));
			var chart2 = new Chart($('events').get(0).getContext("2d"));
			
			chart1.Bar({
				labels : ["January","February","March","April","May","June","July"],
				datasets : [
					{
						fillColor : "rgba(220,220,220,0.5)",
						strokeColor : "rgba(220,220,220,1)",
						data : [65,59,90,81,56,55,40]
					},
					{
						fillColor : "rgba(151,187,205,0.5)",
						strokeColor : "rgba(151,187,205,1)",
						data : [28,48,40,19,96,27,100]
					}
				]
			}, {
				scaleShowLabels : true
			});
		}
	};
	
});