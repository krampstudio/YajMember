define( ['jquery', 'store', 'notify', 'eventbus', 'gridy'], function($, store, notify, EventBus){
	
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
				self._$container.gridy('reload');
			});
			
			//build the grid
			this._$container.gridy({
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
						$('#actions').tabs('select', 1);
						
						return false;
					});
					
					$('.member-remove').click(function(event){
						event.preventDefault();
						
						//extract the id and store it 
						var id = $(this).attr('href').replace('#', '');
						if(id){
							notify('confirm', 'You really want to remove this member ?', function(){
								self._rmMember(id);
							});
						}
						return false;
					});
				},
				
				/*
				 * the column list must match the template defined in index.html! 
				 */
				columns	: [
					{ name: 'Valid',value: 'valid', width: 50 },
					{ name: 'First Name', value: 'firstName', width: 125 },
					{ name: 'Last Name', value: 'lastName', width: 125 },
					{ name: 'Email', value: 'email', width: 225 },
					{ name: 'Company', value: 'company', width: 100 },
					{ name: 'Roles', width: 80 },
					{ name: 'Actions', width: 200 }
				]
			});
		},
		
		/**
		 * Removes a member
		 * @private
		 * @memberOf user/list
		 * @params {Number} memberId - the identifier of the member to remove
		 */
		_rmMember: function(memberId){
			var self = this;
			
			if(memberId){
				$.ajax({
					type		: 'DELETE',
					url			: 'api/member/remove/'+memberId,
					dataType	: 'json'
				}).done(function(data) {
					if(!data.removed || data.error){
						$.error("Error : " + data.error ? data.error : "unknown");
					} else {
						store.rm(memberId);
						self.reload();
						notify('success', 'Removed');
					}
				});
			}
		}
	};
	
	return MemberList;
});
