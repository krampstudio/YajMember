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
				
				
//				//valid membership display/hide sections
//				$('#membership').change(function() {
//					if ($(this).attr('checked') === 'checked') {
//						$('#membership-event').closest('div').show();
//						$('#membership-paiementDate').closest('div').show();
//					} else {
//						$('#membership-event').closest('div').hide();
//						$('#membership-paiementDate').closest('div').hide();
//					}
//				});
				
//				//the date picker
//				$('#membership-paiementDate').datepicker({
//					'dateFormat': 'yy-mm-dd'
//				});
				
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
							/*if(data['valid'] && data['valid'] === true){
								$('#membership').prop('checked', true);
								$('#membership-event').closest('div').show();
								$('#membership-paiementDate').closest('div').show();
								if(data['memberships'] && $.isArray(data['memberships'])){
									var lastMembership = null, i;
									for(i in data.memberships){
										if(lastMembership === null 
												|| data.memberships[i].year > lastMembership.year){
											lastMembership = data.memberships[i];
										}
									}
									if(typeof lastMembership === 'object'){
										if(lastMembership['key']){
											$('#membership-key').val(lastMembership.key);
										}
										if(lastMembership['event']){
											$('#membership-event').val(lastMembership.event.key);
										}
										if(lastMembership['paiementDate']){
											$('#membership-paiementDate').val(lastMembership.paiementDate);
										}
									}
								}
							}*/
							if(typeof callback === 'function'){
								callback();
							}
						}
					});
				}
			},
			
			/**
			 * Load the list of events
			 */
//			loadEvents : function (callback){
//				//load events
//				$.ajax({
//					type 		: 'GET',
//					url 		: 'api/event/list',
//					dataType 	: 'json',
//					data		: {
//						current : true
//					}
//				}).done(function(data) {	
//					if(!data || data.error){
//						$.error("Error : " + (data.error ? data.error : "unknown"));
//					} else if(!data.length){
//						//data is empty no event
//					} else {
//						var template = "<option value='${key}'>${date} - ${title}</option>";
//						$.tmpl(template, data).appendTo('#membership-event');
//					}
//					if(typeof callback === 'function'){
//						callback();
//					}
//				});
//			},
			
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
						//if(!/^membership/.test(elt.name)){
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
//						} else {
//							membership[elt.name.replace(/^membership-/, '')] = elt.value;
//						}
					});
					if(member.roles && !$.isArray(member.roles)){
						member.roles = [member.roles];
					}
					
//					if($('#membership', $form).val() === true || membership['key']){
//						member['memberships'] = [{}];
//						if(membership['key']){
//							member['memberships'][0]['key'] = membership['key'];
//						}
//						if(membership['paiementDate']){
//							member['memberships'][0]['paiementDate'] = membership['paiementDate'];
//						}
//						if(membership['event']){
//							member['memberships'][0]['event'] = {'key' : membership['event'] };
//						}
//					}
				}
				return member;
			},
			
			clear : function(){
//				this.getForms().each(function(){
//					this.reset();
//				});
			}
		});
	
	return UserForm;
});