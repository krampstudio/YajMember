define(function(){
	
	//we know if we are in add or edit if there is a member data on the body DOM's element
	var currentMemberId = $('body').data('member'),
		self = {
			/**
			 * Enable/Disable the form fields
			 */
			toggleForm : function(){
				var $submiter = $('#submiter'),
					isDisabled = $submiter.button('option', 'disabled');
				$('input, select',$('#member-editor')).attr('disabled', !isDisabled);
				$submiter.button(isDisabled ? 'enable' : 'disable');
			},
			
			/**
			 * Initialize the controls behavior
			 */
			initControls : function(){
				
				//submit button
				$('#submiter').button().click(function(event){
					event.preventDefault();
					$('#member-editor').submit();
				});
				
				//valid membership display/hide sections
				$('#membership').change(function() {
					if ($(this).val() === 'true') {
						$('#membership-event').closest('div').show();
						$('#membership-paiementDate').closest('div').show();
					} else {
						$('#membership-event').closest('div').hide();
						$('#membership-paiementDate').closest('div').hide();
					}
				});
				
				//the date picker
				$('#membership-paiementDate').datepicker({
					'dateFormat': 'yy-mm-dd'
				});
				
				//on form submit
				$('#member-editor').submit(function(event){
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
							alert("Saved");
						}
					});
					
					return false;
				});
			},
			
			/**
			 * Load the member data
			 * @param {Number} the identifier of the member to load
			 */
			loadMember : function(memberId){
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
							$('#member-editor :input').each(function(){
								if(data[$(this).attr('id')]){
									$(this).val(data[$(this).attr('id')]);
								}
							});
							if(data['valid'] && data['valid'] === true){
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
							}
						}
					});
				}
			},
			
			/**
			 * Load the list of events
			 */
			loadEvents : function (){
				//load events
				$.ajax({
					type 		: 'GET',
					url 		: 'api/event/list',
					dataType 	: 'json',
					data		: {
						current : true
					}
				}).done(function(data) {	
					if(!data || data.error){
						$.error("Error : " + (data.error ? data.error : "unknown"));
					} else if(!data.length){
						//data is empty no event
					} else {
						var template = "<option value='${key}'>${date} - ${title}</option>";
						$.tmpl(template, data).appendTo('#membership-event');
					}
					
					//load the member data only once the events are loaded
					self.loadMember(currentMemberId);
				});
			},
			
			/**
			 * Serialize the form to a JSON format that match the REST objects
			 * @param {jQueryElement} $form
			 * @return {String} json 
			 */
			serializeMember : function($form){
				var member = {},
					memberShip  = {};
				if($form){
					if($form.prop('tagName') !== 'FORM'){
						$.error('Invalid jQuery element for $form. It much match a form tag.')
					}
					$.map($form.serializeArray(), function(elt, index){
						if(!/^membership/.test(elt.name)){
							if(member[elt.name] === undefined){
								member[elt.name] = elt.value;
							} else {
								if(!$.isArray(member[elt.name])){
									member[elt.name] = [member[elt.name]];
								}
								member[elt.name].push(elt.value);
							}
							
						} else {
							memberShip[elt.name.replace(/^membership-/, '')] = elt.value;
						}
					});
					if(member.roles && !$.isArray(member.roles)){
						member.roles = [member.roles];
					}
					
					if($('#membership', $form).val() === true || memberShip['key']){
						member['memberships'] = [{}];
						if(memberShip['key']){
							member['memberships'][0]['key'] = memberShip['key'];
						}
						if(memberShip['paiementDate']){
							member['memberships'][0]['paiementDate'] = memberShip['paiementDate'];
						}
						if(memberShip['event']){
							member['memberships'][0]['event'] = {'key' : memberShip['event'] };
						}
					}
				}
				return member;
			}
		};
	return self;
});