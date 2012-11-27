define(function(){
	return {
		clear : function(){
			$('#event-editor').each(function(){
				this.reset();
			});
		}
	};
});