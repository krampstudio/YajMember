define(['jquery',
        'noty/layouts/topCenter', 
        'noty/layouts/center',
        'noty/themes/default'], 
function($){
	
	$.noty.defaults.timeout = 20000;
	
	return  function(type, msg){
		var topLayout = 'topCenter',
			centerLayout = 'bottomRight',
			layout = {
				'alert'		: centerLayout,
				'info'		: topLayout,
				'confirm'	: centerLayout,
				'success'	: topLayout,
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