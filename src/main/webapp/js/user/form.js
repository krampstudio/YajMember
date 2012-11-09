define(function(){
	return {
		build: function(){
			
			$('#submiter').button().click(function(event){
				event.preventDefault();
				$('#member-editor').submit();
			});
			
			//load member data
			var currentMemberId = $('body').data('member');
			if(currentMemberId){
				
				/**
				 * Enable/disable the form fields
				 */
				var toggleForm = function(){
					var $submiter = $('#submiter'),
						isDisabled = $submiter.button('option', 'disabled');
					$('input, select',$('#member-editor')).attr('disabled', !isDisabled);
					$submiter.button(isDisabled ? 'enable' : 'disable');
				};
				toggleForm();
				
				$.ajax({
					type 		: 'GET',
					url 		: 'api/member/getOne',
					dataType 	: 'json',
					data		: {
						id : currentMemberId
					}
				}).done(function(data) {	
					toggleForm();
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
			});
			
			
			//form controls
			
			$('#membership').change(function() {
				if ($(this).val() === 'true') {
					$('#membership-event').closest('div').show();
					$('#membership-paiementDate').closest('div').show();
				} else {
					$('#membership-event').closest('div').hide();
					$('#membership-paiementDate').closest('div').hide();
				}
			});
			
			$('#membership-paiementDate').datepicker({
				'dateFormat': 'yy-mm-dd'
			});
			
			$('#membership-paiementDate').change(function(){
				if($(this).val() !== ''){
					$('#membership-event').val('');
				}
			});
			$('#membership-event').change(function(){
				if($(this).val() !== ''){
					$('#membership-paiementDate').val('');
				}
			});
			
			/**
			 * Serialize the form to a JSON format that match the REST objects
			 * @param {jQueryElement} $form
			 * @return {String} json 
			 */
			var serializeMember = function($form){
				var member = {},
					memberShip  = {};
				if($form){
					if($form.prop('tagName') !== 'FORM'){
						$.error('Invalid jQuery element for $form. It much match a form tag.')
					}
					$.map($form.serializeArray(), function(elt, index){
						if(!/^membership/.test(elt.name)){
							member[elt.name] = elt.value;
						} else {
							memberShip[elt.name.replace(/^membership-/, '')] = elt.value;
						}
					});
					if(member.roles && !$.isArray(member.roles)){
						member.roles = [member.roles];
					}
					
					member['memberships'] = [{
						'key'	: memberShip['key'],
						'paiementDate' : memberShip['paiementDate'],
						'event' : {
							'key': memberShip['event'] 
						}
					}];
				}
				
				return member;
			}
			
			//send data
			$('#member-editor').submit(function(event){
				event.preventDefault();
				
				var member = serializeMember($(this)),
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
		}
	};
});