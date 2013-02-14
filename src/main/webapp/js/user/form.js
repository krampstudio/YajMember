/**
 * Manage user's Form UI and IO
 * @module event/form
 */
define(['multiform', 'notify', 'store', 'modernizr'], function(MultiForm, notify, store){

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
		 * Initialize the controls for the Details sub form.
		 * @see module:multiform#initFormControls
		 * @private
		 * @memberOf module:user/form
		 * @param {Object} $form - the jQuery element of the form
		 */
		_initDetailsControls : function($form){
			var self = this;
			
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
		 * Initialize the controls for the Membership sub form.
		 * @see module:multiform#initFormControls
		 * @private
		 * @memberOf module:user/form
		 * @param {Object} $form - the jquery element the reference the form
		 */
		_initMembershipControls: function($form){
			var self = this;
			
			//build the vertical tabs (even if there's no years yet)
			this._buildMembershipTabs();
			
			//Add year for membership form
			//TODO upgrade jquery-ui
			//			if(!Modernizr.inputtypes.number){
			//				$('.membership-amount', $container).spinner({
			//					min: 0,
			//					max: 40,
			//					step: 40
			//				});
			//			}
			
			//The form to add a new membership starts with the definition of the year
			$('#add-year', $form).button({
				icons: { primary: "icon-add" },
				text : false
			}).click(function(){
				var newYear = $('#membership-new-year').val();
				if(!/^20[0-9]{2}$/.test(newYear)){
					notify('error', 'Invalid date format!');
					return;
				}
				//we build up the form for the added year
				self._buildMembershipForm({'year' : newYear}, function(){
					self._buildMembershipTabs();
				});
			});
			
			//by submitting the Membership form, 
			//we serialize the memberships (everything) using the form values
			//and send them to the server
			$form.submit(function(event){
				event.preventDefault();
				
				var memberId = self.getMemberId();
				
				if(!memberId){
					notify('error', 'Save the member before associate memberships');
					return;
				}
				
				$.ajax({
					type 		: 'POST',
					url 		: 'api/member/updateMemberships/'+memberId,
					contentType : 'application/x-www-form-urlencoded',
					dataType 	: 'json',
					data 		: {
						memberships : self.serializeMemberships($form, memberId)
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
		 * Retrieve the memberships of a member and build the forms according to the results
		 * @memberOf module:user/form
		 * @param {Number} memberId - the requried identifier of the member
		 * @param {formCallback} callback - a callback executed once the memberships are loaded
		 */
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
		 * Loads the events for a year
		 * @memberOf module:user/form
		 * @param {String} year - the year of the events 
		 * @param {formCallback} callback - a callback executed once the events are loaded
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
		 * Get a member instance from the form data
		 * @memberOf module:user/form
		 * @param {Object} $form - the jQuery element of the form
		 * @returns {Object} a member
		 */
		serializeMember : function($form){
			var member = {};
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
		
		/**
		 *  Get the memberships instances from the form data
		 * @memberOf module:user/form
		 * @param {Object} $form - the jQuery element of the form
		 * @returns {Array} of memberships
		 */
		serializeMemberships : function($form, memberId){
			var memberships = [],
				data = {},
				membership = {},
				year;
			if($form){
				if($form.prop('tagName') !== 'FORM'){
					$.error('Invalid jQuery element for $form. It much match a form tag.')
				}
				$.map($form.serializeArray(), function(elt, index){
					var year = elt.name.replace(/^membership-/, '').replace(/-[a-z]+$/, '');
					if(/^20[0-9]{2}$/.test(year)){
						if(data[year] === undefined){
							data[year] = {};
						}
						data[year][elt.name.replace('membership-'+year+'-', '')] = elt.value;
					}
				});
				for(year in data){
					membership = { 
							'year': year,
							'member' : {
								'key' : memberId
							}
						};
					if(data[year]['type'] === 'personnal'){
						membership['amount'] = 'PERSONNAL';
						membership['amount'] = data[year]['amount'];
						membership['paiementDate'] = data[year]['date'];
						membership['event'] = {
								'key' : data[year]['event']
						};
					}
					if(data[year]['type'] === 'sponsored'){
						membership['amount'] = 'SPONSORED';
						membership['company'] = data[year]['company'];
					}
					memberships.push(membership);
				}
			}
			return memberships;
		},
		
		/**
		 * Get the id of the event currently selected
		 * from either the store or the input field
		 * @memberOf module:event/form
		 * @returns {Number} the id or undefined if not found
		 */
		getMemberId : function(){
			var $keyField;
			if(store.isset('member')){
				return store.get('member');
			}
			$keyField = $('#key', this.getForm('details'));
			if($keyField.length > 0 && $keyField.val().trim().length > 0){
				return $keyField.val();
			}
		},
	
		/**
		 * Clear the membership form mannually
		 * @see module:multiform#clear
		 * @private
		 * @memberOf module:event/form
		 * @param {Object} $form - the jQuery element of the form
		 */
		_clearMembershipForm : function($form){
			$('#memberships').tabs('destroy').removeClass('ui-tabs-vertical  ui-helper-clearfix');
			$('#memberships .membership-year').remove();
			$('#memberships .membership-form').remove();
		}
	});
	
	/**
	 * Global callback, do what you f** you want
	 * @callback formCallback
	 */
	
	return UserForm;
});