define( ['gridy'], function(){
	return {
		build: function(){
			$("#users").gridy({
				url		: 'api/member/list',
				dataType	: 'jsonp',
				evenOdd		: true,
				clickFx		: true,
				resize		: true,
				height		: 700,
				width		: 975,
				before		: function(){
					$('.gridy-search :button').button({disabled : false})
				},
				
				/*
				 * the column list must match the template defined in index.html! 
				 */
				columns	: [
				    { name: 'Id', value: 'key', width: 45 },
					{ name: 'Valid',value: 'valid', width: 50 },
					{ name: 'First Name', value: 'firstName', width: 125 },
					{ name: 'Last Name', value: 'lastName', width: 125 },
					{ name: 'Email', value: 'email', width: 225 },
					{ name: 'Company', value: 'company', width: 100 },
					{ name: 'Roles', width: 80 },
					{ name: 'Actions', width: 200 }
				]
			});
		}
	}
});
