define(
	['jquery', 'multiform', 'controller/member', 'controller/membership', 'controller/event', 'notify', 'store', 'eventbus', 'modernizr'], 
	function($, MultiForm, MemberController, MembershipController, EventController, notify, store, EventBus){
	
	'use strict';

	/**
	 * Manage user's Form UI and IO
	 * The UserForm is a MultiForm that manages widgets for the user's forms
	 * @see module:multiform
	 * @exports member/form
	 */
	var MemberForm = {
			
		/**
		 * The form id prefix
		 * @see module:multiform._id
		 * @private 
		 */
		_id : 'member',
		
		/**
		 * The sub-forms names
		 * @see module:multiform._formNames
		 * @private 
		 */
		_formNames : ['details', 'membership'],
		
		/**
		 * Initialize the form and the events
		 */
		setUp: function(){
			var self = this;
			
			EventBus.subscribe('memberform.load', function(){
				if(store.isset('member')){
					//load the member
					self.loadMember(store.get('member'), function(){
						self.loadMemberships(store.get('member'));
					});		
				} else {
					self._buildMembershipTabs();
				}
			});
			
			EventBus.subscribe('memberform.cleanup', function(){
				self.clearForms(function(){
					store.rm('member');
				});
			});
			
			//initialize the controls
			self.initFormControls(function(){
				
				//if there is a selected member, we load it
				EventBus.publish('memberform.load');
			});
		},
		
		/**
		 * Initialize the controls for the Details sub form.
		 * @see module:multiform.initFormControls
		 * @private
		 * @param {Object} $form - the jQuery element of the form
		 */
		_initDetailsControls : function($form){
			var self = this;
			
			//on form submit
			$form.submit(function(event){
				event.preventDefault();
				
				MemberController.save(self.serializeMember($(this)));
				
				return false;
			});
		},
		
		/**
		 * Initialize the controls for the Membership sub form.
		 * @see module:multiform.initFormControls
		 * @private
		 * @param {Object} $form - the jquery element the reference the form
		 */
		_initMembershipControls: function($form){
			var self = this;
			
			//build the vertical tabs (even if there's no years yet)
			this._buildMembershipTabs();
			
			//Add year for membership form
			if(!Modernizr.inputtypes.number){
				$('#membership-new-year', $form).spinner({
					min: 2010,
					max: 2050,
					step: 1
				});
			}
			
			//The form to add a new membership starts with the definition of the year
			$('#add-year', $form).button({
				icons: { primary: "icon-add" },
				text : false
			}).click(function(event){
				event.preventDefault();
				
				var newYear = $('#membership-new-year').val();
				if(!/^20[0-9]{2}$/.test(newYear)){
					notify('error', 'Invalid date format!');
					return false;
				}
				//we build up the form for the added year
				self._buildMembershipForm({
					'year': newYear, 
					'type': 'PERSONNAL'
				}, function(){
					self._buildMembershipTabs();
				});
			});
			
			//by submitting the Membership form, 
			//we serialize the memberships (everything) using the form values
			//and send them to the server
			$('.submiter', $form).click(function(event){
				event.preventDefault();
				
				var memberId = self.getMemberId();
				
				if(!memberId){
					notify('error', 'Save the member before associate memberships');
					return;
				}
				
				MembershipController.save(self.serializeMemberships($form, memberId));
				
				return false;
			});
			
			$form.submit(function(event){
				event.preventDefault();
				return false;
			});
		},
		
		/**
		 * Build the form for a membership from templates and initialize the included widgets
		 * @private
		 * @param {Object} membership - the complete membership object or at least with the year for new membership
		 * @param {Function} callback - a callback executed once the memberis loaded
		 */
		_buildMembershipForm: function(membership, callback){
			var self = this,
				$form = self.getForm('membership'),
				formTmpl = $('#membership-form-template'),
				tabTmpl = $('#membership-tab-template'),
				$container,
				
				/**
				 * switch the membership form elements based on the type
				 * @param {Object} $container - a jQuery element that contains the views
				 */
				updateFormView = function($container){
					var val = $(this).val();
					if(val === 'sponsored'){
						$('.sponsored-mb', $container).show();
						$('.perso-mb', $container).hide();
					} else {
						$('.sponsored-mb', $container).hide();
						$('.perso-mb', $container).show();
					}
				};
			
			if(membership !== undefined && membership.year){
				
				//fix issue of jquery.tmpl that get DOM nodes from id instead of the data key (#key element is set instead of m
				membership.mkey = membership.key || '';
				membership.mcompany = membership.company || '';
				
				//creates the html form using the templates
				if($("#memberships > ul li a[href='#membership-form-"+membership.year+"']", $form).length === 0){
					$('#memberships > ul', $form).prepend($.tmpl(tabTmpl, {'year' : membership.year}));
				}
				$('#memberships > ul', $form).after($.tmpl(formTmpl, membership));
				
				$container = $('#membership-form-'+membership.year);
				
				//load the events list
				self.loadEvents(membership.year);
				
				//set up the membership type switch
				$('.membership-type', $container).buttonset();
				updateFormView.apply($('.membership-type input:checked', $container), [$container]);
				$('.membership-type input', $container).change(updateFormView);
				
				//set up the membership removal
				$('.membership-remover', $container).button({
					icons : { primary: "icon-delete" },
					text : false
				}).click(function(event){
					event.preventDefault();
					
					MembershipController.remove($(this).attr('href').replace('#', ''), function(){
						$container.remove();
						$("#memberships > ul li a[href='#membership-form-"+membership.year+"']", $form).remove();
						self._buildMembershipTabs();
					});
					return false;
				});
				
				//set up the date selection
				if(!Modernizr.inputtypes.date){
					$('.membership-date', $container).datepicker({
						'dateFormat': 'yy-mm-dd'
					});
				}
				
				if(!Modernizr.inputtypes.number){
					$('.membership-amount', $container).spinner({
						min: 0,
						max: 40,
						step: 40
					});
				}
				
				//and the company list autocompletion
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
		
		/**
		 * Creates a vertical tab widget from the membership form. 
		 * The tabs are refreshed they already exists.
		 * @private
		 * @param {Function} callback - a callback executed once the memberis loaded
		 */
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
		 * Retrieve a member and populate the details form according to the results
		 * @param {Number} memberId - the requried identifier of the member
		 * @param {Function} callback - a callback executed once the memberis loaded
		 */
		loadMember : function(memberId, callback){
			var self = this;
			if(memberId){
				self.toggleForm();
				
				MemberController.getOne(memberId, function(member){
					self.toggleForm();
					$(':input', self.getForm('details')).each(function(){
						if(member[$(this).attr('id')]){
							$(this).val(member[$(this).attr('id')]);
						}
					});
					if(typeof callback === 'function'){
						callback();
					}
				});
			}
		},
		
		/**
		 * Retrieve the memberships of a member and build the forms according to the results
		 * @param {Number} memberId - the requried identifier of the member
		 * @param {Function} callback - a callback executed once the memberships are loaded
		 */
		loadMemberships: function(memberId, callback){
			var self = this;
			if(memberId){
				self.toggleForm();
				
				MembershipController.getByMember(memberId, function(memberships){
					var i = 0;
					self.toggleForm();
					if(memberships){
						//build the form
						for(i in memberships){
							self._buildMembershipForm(memberships[i], function(){
								self._buildMembershipTabs();
							});
						}
					}
					if(typeof callback === 'function'){
						callback();
					}
				});
			}
		},
		
		/**
		 * Loads the events for a year
		 * @param {String} year - the year of the events 
		 * @param {Function} callback - a callback executed once the events are loaded
		 */
		loadEvents : function (year, callback){
			
			var selector = '#membership-' + year + '-event',
				template = "<option value='${key}'>${date} - ${title}</option>";
			
			EventController.getAll(year, function(events){
				if(events && $.isArray(events)){
					$(selector).empty().append($.tmpl(template, events));
				}
				if(typeof callback === 'function'){
					callback();
				}
			});
		},
		
		/**
		 * Get a member instance from the form data
		 * @param {Object} $form - the jQuery element of the form
		 * @returns {Object} a member
		 */
		serializeMember : function($form){
			
			var member = this.serialize($form);
			
			if(member.roles && !$.isArray(member.roles)){
				member.roles = [member.roles];
			}
			
			return member;
		},
		
		/**
		 * Get the memberships instances from the form data
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
					$.error('Invalid jQuery element for $form. It much match a form tag.');
				}
				$.map($form.serializeArray(), function(elt){
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
					if(data[year].type === 'personnal'){
						membership.type = 'PERSONNAL';
						membership.amount = data[year].amount;
						membership.paiementDate = data[year].date;
						membership.event = {
								'key' : data[year].event
						};
					}
					if(data[year].type === 'sponsored'){
						membership.type = 'SPONSORED';
						membership.company = data[year].company;
					}
					if(data[year].key && data[year].key.trim().length > 0){
						membership.key = data[year].key;
					}
					memberships.push(membership);
				}
			}
			return memberships;
		},
		
		/**
		 * Get the id of the event currently selected
		 * from either the store or the input field
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
		 * @see module:multiform.clearForm
		 * @private
		 * @param {Object} $form - the jQuery element of the form
		 */
		_clearMembershipForm : function($form){
			if($('#memberships', $form).data('uiTabs')){
				$('#memberships', $form).tabs('destroy').removeClass('ui-tabs-vertical  ui-helper-clearfix');
			}
			$('#memberships .membership-year', $form).remove();
			$('#memberships .membership-form', $form).remove();
		}
	};
	
	//make MemberForm extends MultiForm
	return $.extend({}, MultiForm, MemberForm);
});