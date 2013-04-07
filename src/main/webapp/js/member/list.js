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
					$('.member-edit').click(function(event){
						event.preventDefault();
						
						//extract the id and store it 
						store.set('member',  $(this).attr('href').replace('#', ''));
						
						//opens the edit tab
						$('#actions').tabs('option', 'active', 2);
						
						return false;
					});
					
					$('.member-remove').click(function(event){
						event.preventDefault();
						
						MemberController.remove($(this).attr('href').replace('#', ''), function(){
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
