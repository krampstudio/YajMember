/**
 * Manage user's Form UI and IO
 * @module event/form
 */
define(['multiform', 'modernizr', 'notify'], function(MultiForm, Modernizr, notify){

	/**
	 * The UserForm is a MultiForm that manages widgets for the user's forms
	 * @constructor
	 * @see module:multiform 
	 * @alias module:user/form
	 */
	var UserForm = $.extend({}, MultiForm, {
			
		/**
		 * @private 
		 * @memberOf module:event/form
		 * @see module:form#_id
		 */
		_id : 'member',
		
		/**
		 * @private 
		 * @memberOf module:event/form
		 * @see module:form#_id
		 */
		_formNames	: ['details', 'membership'],
		
		
			/**
			 * Initialize the controls behavior
			 */
		_initDetailsControls : function($form){
			
			//on form submit
			$form.submit(function(event){
				event.preventDefault();
				
				var member = self.serializeMember($(this)),
					udpate = (member.key && member.key > 0);
						
				$.ajax({
					type 		: (udpate) ? 'POST' : 'PUT',
					url 		: (udpate) ? 'api/member/update' : 'api/member/add',
					contentType : 'application/x-www-form-urlencoded',
					dataType 	: 'json',
					data 		: {
						member : JSON.stringify(member)
					}
				}).done(function(data) {
					if(!data.saved || data.error){
						$.error("Error : " + data.error ? data.error : "unknown");
					} else {
						notify('success', 'Saved');
					}
				});
				return false;
			});
		},
		
		/**
		 * Load the member data
		 * @param {Number} the identifier of the member to load
		 */
		loadMember : function(memberId, callback){
			var self = this;
			if(memberId && memberId > 0){
				self.toggleForm();
				
				$.ajax({
					type 		: 'GET',
					url 		: 'api/member/getOne',
					dataType 	: 'json',
					data		: {
						id : memberId
					}
				}).done(function(data) {	
					self.toggleForm();
					if(!data || data.error){
						$.error("Error : " + (data.error ? data.error : "unknown"));
					} else {
						$(':input', self.getForm('details')).each(function(){
							if(data[$(this).attr('id')]){
								$(this).val(data[$(this).attr('id')]);
							}
						});
						if(typeof callback === 'function'){
							callback();
						}
					}
				});
			}
		},
		
		/**
		 * Serialize the form to a JSON format that match the REST objects
		 * @param {jQueryElement} $form
		 * @return {String} json 
		 */
		serializeMember : function($form){
			var member = {},
				membership  = {};
			if($form){
				if($form.prop('tagName') !== 'FORM'){
					$.error('Invalid jQuery element for $form. It much match a form tag.')
				}
				$.map($form.serializeArray(), function(elt, index){
					if(elt.value && elt.value.trim().length > 0){
						if(member[elt.name] === undefined){
							member[elt.name] = elt.value;
						} else {
							if(!$.isArray(member[elt.name])){
								member[elt.name] = [member[elt.name]];
							}
							member[elt.name].push(elt.value);
						}
					}
				});
				if(member.roles && !$.isArray(member.roles)){
					member.roles = [member.roles];
				}
			}
			return member;
		}
	});
	
	return UserForm;
});