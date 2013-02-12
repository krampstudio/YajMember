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
		
		_initMembershipControls: function($form){
			var self = this;
			
		//	this._buildMembershipTabs();
			
			//Add year for membership form
			//TODO upgrade jquery-ui
			//			if(!Modernizr.inputtypes.number){
			//				$('.membership-amount', $container).spinner({
			//					min: 0,
			//					max: 40,
			//					step: 40
			//				});
			//			}
			
			$('#add-year').button({
				icons: { primary: "icon-add" },
				text : false
			}).click(function(){
				var newYear = $('#membership-new-year').val();
				if(!/^20[0-9]{2}$/.test(newYear)){
					notify('error', 'Invalid date format!');
					return;
				}
				self._buildMembershipForm({'year' : newYear}, function(){
					self._buildMembershipTabs();
				});
			});
		},
		
		_buildMembershipForm: function(membership, callback){
			var self = this,
				membership = membership || {}, 
				$form = self.getForm('membership'),
				formTmpl = $('#membership-form-template'),
				tabTmpl = $('#membership-tab-template'),
				$container;
			
			if(membership.year){
				if($("#memberships > ul li a[href='#membership-form-"+membership.year+"']", $form).length === 0){
					$('#memberships > ul', $form).prepend($.tmpl(tabTmpl, {'year' : membership.year}));
				}
				$('#memberships > ul', $form).after($.tmpl(formTmpl, membership));
				
				$container = $('#membership-form-'+membership.year);
				
				self.loadEvents(membership.year);
				
				$('.membership-type', $container).buttonset();
				$('.membership-type input', $container).change(function(){
					var val = $(this).val(),
						id = $(this).attr('id');
					
					if(val === 'on'){
						if(/sponsored$/.test(id)){
							$('.sponsored-mb', $container).show();
							$('.perso-mb', $container).hide();
						}
						if(/perso$/.test(id)){
							$('.sponsored-mb', $container).hide();
							$('.perso-mb', $container).show();
						}
					}
				});
				
				if(!Modernizr.inputtypes.date){
					$('.membership-date', $container).datepicker({
						'dateFormat': 'yy-mm-dd'
					});
				}
				//TODO upgrade jquery-ui
	//			if(!Modernizr.inputtypes.number){
	//				$('.membership-amount', $container).spinner({
	//					min: 0,
	//					max: 40,
	//					step: 40
	//				});
	//			}
				$('.membership-company', $container).autocomplete({
					minLength : 2,
					delay: 600,
					source : 'api/member/acCompaniesSearch'
				});
				
				if(typeof callback === 'function'){
					callback();
				}
			}
		},
		
		_buildMembershipTabs : function(callback){
			if($('#memberships').hasClass('ui-tabs-vertical')){
				$('#memberships').tabs('destroy');
			}
			$('#memberships').tabs().addClass('ui-tabs-vertical ui-helper-clearfix');
			$('#memberships li').removeClass('ui-corner-top ui-state-default');
			
			if(typeof callback === 'function'){
				callback();
			}
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
		
		loadMemberships: function(memberId, callback){
			var self = this;
			if(memberId && memberId > 0){
				self.toggleForm();
				
				$.ajax({
					type 		: 'GET',
					url 		: 'api/member/getMemberships',
					dataType 	: 'json',
					data		: {
						id : memberId
					}
				}).done(function(data) {	
					var i = 0;
					
					self.toggleForm();
					if(!data || data.error){
						$.error("Error : " + (data.error ? data.error : "unknown"));
					} else {
						
						if(data.length > 0 ){
							//build the form
							for(i in data){
								self._buildMembershipForm(data[i], function(){
									self._buildMembershipTabs();
								});
							}
						}
						
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
		loadEvents : function (year, callback){
			
			var selector = '#membership-' + year + '-event',
				params = {year: year};
			
			//load events
			$.ajax({
				type 		: 'GET',
				url 		: 'api/event/list',
				dataType 	: 'json',
				data		: params
			}).done(function(data) {	
				if(!data || data.error){
					$.error("Error : " + (data.error ? data.error : "unknown"));
				} else if(data.length){
					var template = "<option value='${key}'>${date} - ${title}</option>";
					$(selector).empty().append($.tmpl(template, data));
				}
				if(typeof callback === 'function'){
					callback();
				}
			});
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
		},
	
		_clearMembershipForm : function($form){
			$('#memberships').tabs('destroy').removeClass('ui-tabs-vertical  ui-helper-clearfix');
			$('#memberships .membership-year').remove();
			$('#memberships .membership-form').remove();
		}
	});
	
	return UserForm;
});