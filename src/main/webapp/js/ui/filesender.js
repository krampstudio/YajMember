define( ['jquery'], function($){
	
	'use strict';
	
	/**
	 * The FileSender widget enables you to post a file 
	 * to the server asynchronously.
	 * 
	 * @exports filesender
	 */
	var FileSender = {
	
		_opts : {
			url : '',
			frame : '__postFrame',
			loaded : function(data){}
		},
			
		_init : function(options){
			var self = FileSender,
	            opts = $.extend(true, {}, self._opts, options),
	            $form = this;
			
			if(!$form || !$form.is('form')){
				$.error('The function requires a jQuery element that matches a form');
			}
			
			//clean up if already exists
			$('#' + opts.frame, $form).remove();
			
			//we create the hidden frame as the action of the upload form (to prevent page reload)
			var $postFrame = $("<iframe />");
			$postFrame.attr({
				'name': opts.frame,
				'id' : opts.frame
			})
			.css('display', 'none')
			.load(function(){
				//we get the response in the frame
				var result = $.parseJSON($(this).contents().text());
				if(typeof opts.loaded === 'function'){
					 opts.loaded(result);
				}
			});
			
			//we update the form attributes according to the frame
			$form.attr({
					'action'	: opts.url,
					'method'	: 'POST',
					'enctype'	: 'multipart/form-data',
					'encoding'	: 'multipart/form-data',
					'target'	: opts.frame
				})
				.append($postFrame)
				.submit();
		}
			
	};
	
	$.fn.sendfile = function(options){
		return FileSender._init.call(this, options);
	};
});