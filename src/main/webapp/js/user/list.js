define( ['gridy'], function(){
	return {
		build: function(){
			$("#users").gridy({
				url		: 'api/member/list',
				dataType	: 'jsonp',
				evenOdd		: true,
				clickFx		: true,
				resize		: true,
				height		: 600,
				width		: 1200,
				before		: function(){
					$('.gridy-search :button').button({disabled : false})
				},
				
				/*
				 * the column list must match the template defined in index.html! 
				 */
				columns	: [
				    { name: 'Id', value: 'key', width: 45 },
					{ name: 'Valid Member',value: 'valid', width: 100 },
					{ name: 'First Name', value: 'firstName', width: 150 },
					{ name: 'Last Name', value: 'lastName', width: 150 },
					{ name: 'Email', value: 'email', width: 280 },
					{ name: 'Company', value: 'company', width: 100 },
					{ name: 'Roles', width: 100 },
					{ name: 'Actions', width: 240 }
				]
			});
		}
	}
});
