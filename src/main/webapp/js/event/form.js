/**
 * Manage event's Form UI and IO
 * @module event/form
 */
define(
	['jquery', 'controller/event', 'multiform', 'notify', 'store', 'eventbus', 'jhtmlarea', 'modernizr', 'filesender'], 
	function($, EventController, MultiForm, notify, store, EventBus){
	
	'use strict';
	
	/**
	 * The EventForm extends from the Form object
	 * @constructor
	 * @see module:multiform 
	 * @alias module:event/form
	 */
	var EventForm = $.extend({}, MultiForm, {
		
		/**
		 * @private 
		 * @memberOf module:event/form
		 * @see module:form#_id
		 */
		_id : 'event',
		
		/**
		 * @private 
		 * @memberOf module:event/form
		 * @see module:form#_id
		 */
		_formNames	: ['infos', 'flyer', 'participant'],
		
		setUp: function(){
			var self = this;
			EventBus.subscribe('eventform.cleanup', function(){
				self.clearForms(function(){
					store.rm('event');
				});
			});
		},
		
		/**
		 * Initialize the controls for the Infos form.
		 * @see module:multiform#initFormControls
		 * @private
		 * @memberOf module:event/form
		 * @param {Object} $form - the jQuery element of the form
		 */
		_initInfosControls : function($form){
			var self = this,
				$date = $('#date', $form); // the date picker
			
			if(!Modernizr.inputtypes.date){
				$date.datepicker({
					'dateFormat': 'yy-mm-dd'
				});
			}
			
			//TODO show on freshly added event.
			$date.on('change', function(){
				if(self.getEventId() !== undefined  && this.value && this.value.length === 10){
					self.getForm('flyer').show();
				} else {
					self.getForm('flyer').hide();
				}
			});
			
			$('textarea', $form).htmlarea({
				toolbar: [
                    'bold', 'italic', 'underline', '|',
                    'h1', 'h2', 'h3', 'h4', 'h5', 'h6', '|',
                    'link', 'unlink', '|',
                    'orderedList', 'unorderedList', 'indent', 'outdent'
                ],
                css : 'css/lib/jhtmlarea/jhtmlarea-editor.css'
			});
			
			// on form submit
			$form.bind('submit', function(e){
				e.preventDefault();
				
				EventController.save(self.serializeEvent($(this)));
						
				return false;
			});
		},
		
		/**
		 * Initialize the controls for the Flyer form.
		 * @see module:multiform#initFormControls
		 * @private
		 * @memberOf module:event/form
		 * @param {Object} $form - the jQuery element of the form
		 */
		_initFlyerControls : function($form){
			var self = this;
			
			$('#flyer-remover').button({
				icons : { primary: "icon-delete" },
				text : false
			}).click(function(){
				notify('confirm', 'You really want to remove this image ?', function(){
					var eventId = self.getEventId();
					if(eventId){
						$.ajax({
							type		: 'DELETE',
							url			: 'api/event/removeFlyer/'+eventId,
							dataType	: 'json'
						}).done(function(data) {
							if(!data.removed || data.error){
								$.error("Error : " + data.error ? data.error : "unknown");
							} else {
								$('#current-flyer').removeAttr('src');
								notify('success', 'Removed');
							} 
						});
					} else {
						$('#current-flyer').removeAttr('src');
					}
				});
			});
			
			$('#flyer-viewer').button({
				icons : { primary: "icon-image" },
				text : false
			}).click(function(){
				if($('#current-flyer')){
					window.open($('#current-flyer').attr('src').replace('-small', ''));
				}
			});
			
			//TODO show the preview before the upload
			
			$('.submiter', $form).click(function(e){
				e.preventDefault();
				
				//post file form 
				$form.sendfile({
					url : 'api/event/flyer/'+self.getEventId(),
					loaded : function(result){
						if(result && result.saved === true){
							
							//TODO force cache reload
							$('#current-flyer').attr('src', result.thumb);
							notify('success', 'Flyer uploaded');
						} else {
							notify('error', 'Flyer upload error');
						}
					}
				});
				return false;
			});
		},
		
		/**
		 * Initialize the controls for the Participant form.
		 * @see module:multiform#initFormControls
		 * @private
		 * @memberOf module:event/form
		 * @param {Object} $form - the jQuery element of the form
		 */
		_initParticipantControls : function($form){
			var self					= this,
				$importFields			= $('.reg-import-field', $form),
				$importCtrl				= $('#reg-import-ctrl', $form),
				$importOptsOrder		= $('#reg-import-opts-order', $form),
				$importOptsOrderItems	= $('#reg-import-opts-order li', $form),
				$importOptsOrderTrash	= $('#reg-import-opts-order-ctrl-trash', $form),
				$importOptsOrderReset	= $('#reg-import-opts-order-ctrl-reset', $form),
				$addRegistrants			= $('#reg-add', $form),
				$registrantList			= $('#registrant', $form),
				$participantList		= $('#participant', $form),
				$ltr					= $('.list-box-ctrl a.ltr', $form),
				$rtl					= $('.list-box-ctrl a.rtl', $form),
				$trash					= $('.list-box-ctrl a.rml', $form),
				$listBoxes				= $('.list-box ul', $form),
				
				/**
				 * Add registrant to his list
				 * @param {String} key - the required member's identifier
				 * @param {String} name - the required member's name
				 */
				addRegistrant			= function(key, name){
					if(key && name){
						var $item = $('<li></li>')
							.addClass('ui-state-default')
							.attr('id', 'registrant-' + key)
							.text(name);
						$registrantList.append($item);
					}
				};
			
			$importFields.css('display', 'none');
			
			//Set up auto complete field for adding registrants
			$addRegistrants.autocomplete({
				minLength : 2,
				delay: 600,
				source : 'api/member/acSearch',
				focus: function(event){
					//prevent the item value to be displayed in the field
					event.preventDefault();
				},
				select : function(event, ui){
					event.preventDefault();
					addRegistrant(ui.item.value, ui.item.label);
					$(this).val('');
				}
			});
			
			//toggle import regsitrant sub form
			$importCtrl.button({
				icons: { primary: "icon-import" },
				text : false
			}).click(function(){
				$importFields.toggle();
				return false;
			});
			
			//enables to sort the fields
			$importOptsOrder.sortable({
				cursor : 'move',
				opacity: 0.6,
				placeholder: 'sortable-placeholder',
				forcePlaceholderSizeType: true,
				revert: true
			});
			
			//a trash bin to remove import fields
			$importOptsOrderTrash.droppable({
				accept: $importOptsOrderItems,
				activeClass:  'accpet-drop',
				hoverClass: 'dropping',
				tolerance: 'pointer',
				drop: function(event, ui){
					ui.draggable.remove();
					return false;
				}
			});
			
			//reset the import fields (order and removed)
			$importOptsOrderReset.button({
				icons: { primary: "icon-reset" },
				text : false
			}).click(function(event){
				event.preventDefault();
				$importOptsOrder.empty().append($importOptsOrderItems);
				return false;
			});
			
			//Do the import
			$('#reg-importer', $form).click(function(e){
				e.preventDefault();
				
				//set the order values to the hidden fields
				$("input[name='reg-import-opts-order']", $form).val(
						JSON.stringify($importOptsOrder.sortable("toArray"))
					);
				
				//send the file
				$form.sendfile({
					url : 'api/event/importRegistrants/'+self.getEventId(),
					loaded : function(result){
						if(result && result.members){
							var stats = {}, infos = 'Event registration :';
							notify('success', 'File uploaded');
							
							//TODO improve an test this
							$.each(result.members, function(index, elt){
								
								if(!stats[elt.state]){
									stats[elt.state] = 0;
								}
								stats[elt.state]++;
								
								switch(elt.state){
									case 'exists' :
										addRegistrant(elt.member.key, elt.member.name);
										break;
									case 'added':
										addRegistrant(elt.member.key, elt.member.name);
										break;
									case 'ambiguous':	
								}
							});
							if(stats.exists){
								info += stats.exists + ' existing members.\n'
							}
							if(stats.added){
								info += stats.added + ' new members.\n'
							}
							notify('info', stats.exists);
							
							if(stats.ambiguous){
								notify('warning', "Ambiguous state : Some members of the file cannot " +
										"be imported because we found more than one member for their data.");
							}
						} else {
							//TODO catch error
							notify('error', 'File upload error');
						}
					}
				});
				return false;
			});
			
			//move items from registrants to participants list box
			$ltr.button({
				icons: { primary: "ui-icon-carat-1-e" },
				text : false
			}).click(function(){
				$('#registrant li.ui-selected', $form).each(function(index, elt){
					var id = 'participant-' + $(elt).attr('id').replace('registrant-', ''),
						$item = $('<li></li>');
					if($('#' + id).length === 0){
						$item.addClass('ui-state-default')
							.attr('id', id)
							.text($(elt).text());
						
						$participantList.append($item);
					}
					$(elt).removeClass('ui-selected ui-state-highlight');
				});
				return false;
			});
			
			//remove participants from the list box
			$rtl.button({
				icons: { primary: "ui-icon-carat-1-w" },
				text : false
			}).click(function(){
				$('#participant li.ui-selected', $form).remove();
				return false;
			});
			
			//remove registrant from the list box
			$trash.button({
				icons: { primary: "ui-icon-trash" },
				text : false
			}).click(function(){
				$('#registrant li.ui-selected', $form).each(function(){
					$('#' + $(this).attr('id').replace('registrant-', 'participant-')).remove();
					$(this).remove();
				});
				$('#participant li.ui-selected', $form).remove();
				return false;
			});
			
			//enable list multiple selection
			$listBoxes.selectable({
				selected: function(event, ui){
					$(ui.selected).addClass('ui-state-highlight');
				},
				unselected: function(event, ui){
					$(ui.unselected).removeClass('ui-state-highlight');
				}
			});
			
			//submit the registrant and participants lists
			$('#reg-submiter', $form).click(function(e){
				e.preventDefault();
				
				var lists = {
					registrant : [],
					participant : []
				};
				
				//get the ids of the both lists and push them to the lists arrays
				$listBoxes.each(function(){
					var listId = $(this).attr('id');
					$('li', $(this)).each(function(){
						lists[listId].push($(this).attr('id').replace(listId+'-', ''));
					});
				});
				
				$.ajax({
					type		: 'POST',
					url			: 'api/event/updateParticipant/'+self.getEventId(),
					contentType	: 'application/x-www-form-urlencoded',
					dataType	: 'json',
					data		: {
						registrant  : JSON.stringify(lists.registrant),
						participant : JSON.stringify(lists.participant)
					}
				}).done(function(data) {
					if(!data.saved || data.error){
						$.error("Error : " + (data.error ? data.error : "unknown"));
					} else {
						notify('success', 'Saved');
					}
				});
				
				return false;
			});
		},
		
		/**
		 * Load an event from the server and dispatch the data to the forms
		 * @memberOf module:event/form
		 * @param {Number} eventId -  the identifier of the event to load
		 * @param {formCallback} callback - a callback executed once the event is loaded
		 */
		loadEvent : function(eventId, callback){
			var self	= this; 
			
			EventController.getOne(eventId, function(event){
				$(':input', self.getForm('infos')).each(function(){
					if(event[$(this).attr('id')]){
						$(this).val(event[$(this).attr('id')]);
					}
				});
				if(event.date){
					$('#current-flyer',  self.getForm('flyer')).attr('src', 'img/events/event-'+event.date.replace(/\-/g,'')+'-small.png');
				}
				if(event.description){
					$('#description', self.getForm('infos')).htmlarea('html', event.description.replace(/\\n/g, '<br />'));
				}
				$('#date', self.getForm('infos')).trigger('change');
				if(event.registrants){
					self.addParticipant(event.registrants, 'registrant');
				}
				if(event.participants){
					self.addParticipant(event.participants, 'participant');
				}
				
				if(typeof callback === 'function'){
					callback();
				}
			});
		},
		
		/**
		 * Add a participant/registrant to the list box
		 * @param {Array} data - of participant, each item must contains a key, a firstname and a lastname
		 * @param {String} type - either registrant or participant
		 */
		addParticipant : function(data, type){
			if(!type || type !== 'registrant' && type !== 'participant'){
				$.error('Invalid participant type :' + type);
			}
			if(!data || !$.isArray(data)){
				$.error('Invalid participant list, array excepted');
			}
			var tmpl = $('#participant-item-template'),
				$container = $('#' + type),
				participants = [];
			
			//remap data to avoid issue with duplicate ids in jquery-tmpl
			$.each(data, function(index, elt){
				participants.push({
					'pkey' : type + '-' + elt.key,
					'pname' : elt.firstName + elt.lastName
				});
			});
			
			if(participants.length > 0){
				$container.append($.tmpl(tmpl, participants));
			}
		},
		
		/**
		 * Creates an object from the form data
		 * @memberOf module:event/form
		 * @param {Object} $form - the jQuery element of the form
		 * @returns {Object} that represents the inputs data
		 */
		serializeEvent : function($form){
			var event = {};
			if($form){
				if($form.prop('tagName') !== 'FORM'){
					$.error('Invalid jQuery element for $form. It much match a form tag.');
				}
				$.map($form.serializeArray(), function(elt){
					if(event[elt.name] === undefined && elt.value){
						if( elt.value.trim().length > 0){
							event[elt.name] = elt.value;
						}
					}
				});
				event.description = $('#description', $form).htmlarea('toHtmlString');
			}
			return event;
		},
		
		/**
		 * Get the id of the event currently selected
		 * from either the store or the input field
		 * @memberOf module:event/form
		 * @returns {Number} the id or undefined if not found
		 */
		getEventId : function(){
			var $keyField;
			if(store.isset('event')){
				return store.get('event');
			}
			$keyField = $('#key', this.getForm('infos'));
			if($keyField.length > 0 && $keyField.val().trim().length > 0){
				return $keyField.val();
			}
		},
		
		/**
		 * Clear description field manually
		 * @see module:multiform#clear
		 * @private
		 * @memberOf module:event/form
		 * @param {Object} $form - the jQuery element of the form
		 */
		_clearInfosForm : function($form){
			$('#description', $form).htmlarea('updateHtmlArea');
		},
		
		/**
		 * Clear the flyer field manually
		 * @see module:multiform#clear
		 * @private
		 * @memberOf module:event/form
		 * @param {Object} $form - the jQuery element of the form
		 */
		_clearFlyerForm: function($form){
			$('#current-flyer', $form).removeAttr('src');
		}
	});
	
	/**
	 * Global callback, do what you f** you want
	 * @callback formCallback
	 */
	
	return EventForm;
});