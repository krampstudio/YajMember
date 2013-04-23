define( 
	['jquery', 'controller/member', 'store', 'notify', 'eventbus', 'gridy'], 
	function($, MemberController, store, notify, EventBus){
	
	'use strict';
	
	/**
	 * Creates a grid like list widget for the members
	 * @exports member/list
	 */
	var MemberList = {
			
		_$container : $('#users'),
		
		/**
		 * Initialize the grid
		 */
		setUp: function(){
			
			var self = this;
			
			//listen for reload event to reload the grid
			EventBus.subscribe('memberlist.reload',function(){
				if(self._$container.data('settings')){
					self._$container.gridy('reload');
				}
			});
			
			//build the grid
			self._$container.gridy({
				url		: 'api/member/list',
				dataType	: 'jsonp',
				evenOdd		: true,
				clickFx		: true,
				resize		: true,
				height		: 700,
				width		: 950,
				before		: function(){
					$('.gridy-search :button').button({disabled : false});
				},
				done		: function(){
					
					/**
					 * Extract the member id from the href attr of a node
					 * @param {Object} $elt - the jquery element of the node
					 * @returns {String|Array} the id or the ids if $elt match more than one node
					 */
					var getMemberId = function($elt){
						if($elt.length > 0){
							if($elt.length === 1){
								return $elt.attr('href').replace('#', '');
							} else {
								var ids = [];
								$elt.each(function(index, elt){
									ids.push(getMemberId(elt));
								});
								return ids;
							}
						}
					};
					
					/*
					 * manage multiple removes
					 */
					//creates and inject a button
					var $rmAll = $("<a href='#'>Remove selected</a>")
								.attr('id', 'members-remover')
								.css({	'display': 'none',
										'position':'absolute',
										'right' : '.2em',
										'top' : '.2em'});
					$('.gridy-footer').css('position', 'relative').append($rmAll);
					$('#members-remover').button().click(function(){
						var members = getMemberId($('.gridy-content tr.gridy-row-selected .member-remove'));
						
						//TODO remove members
						return false;
					});				
					
					//show/hide the button regarding the selection state
					$('.gridy-content > tr', self._$container).click(function(){
						setTimeout(function(){
							if($('.gridy-content tr.gridy-row-selected').length > 1){
								$rmAll.show();
							} else {
								$rmAll.hide();
							}
						}, 100);
					});
					
					//edit a member
					$('.member-edit').click(function(event){
						event.preventDefault();
						
						//extract the id and store it 
						store.set('member',  getMemberId($(this)));
						
						//opens the edit tab
						$('#actions').tabs('option', 'active', 2);
						
						return false;
					});
					
					//remove a member
					$('.member-remove').click(function(event){
						event.preventDefault();
						
						MemberController.remove(getMemberId($(this)), function(){
							EventBus.publish('memberlist.reload');
						});
						return false;
					});
				},
				
				/*
				 * the column list must match the template defined in index.html! 
				 */
				columns	: [
					{ name: 'Valid', width: 45 },
					{ name: 'First Name', value: 'firstName', width: 125 },
					{ name: 'Last Name', value: 'lastName', width: 125 },
					{ name: 'Email', value: 'email', width: 255 },
					{ name: 'Company', value: 'company', width: 145 },
					{ name: 'Roles', width: 80 },
					{ name: 'Actions', width: 150 }
				]
			});
		}
	};
	
	
	return MemberList;
});
