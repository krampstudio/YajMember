define(['jquery',
        'noty/layouts/topCenter', 
        'noty/layouts/bottomRight',
        'noty/themes/default'], 
function($){
	
	$.noty.defaults.timeout = 20000;
	
	return  function(type, msg){
		var topLayout = 'topCenter',
			bottomLayout = 'bottomRight',
			layout = {
				'alert'		: topLayout,
				'info'		: bottomLayout,
				'confirm'	: topLayout,
				'success'	: bottomLayout,
				'error'		: topLayout,
				'warning'	: topLayout
			};
		
		if(msg && type){
			return noty({
				text : msg, 
				layout: layout[type] || topLayout, 
				type : type
			});
		}
	}
});