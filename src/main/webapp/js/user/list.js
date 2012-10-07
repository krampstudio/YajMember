define( ['gridy'], function(){
	return {
		build: function(){
			$("#users").gridy({
				url		: 'api/user/list',
				dataType	: 'jsonp',
				evenOdd		: true,
				clickFx		: true,
				resize		: true,
				height		: 600,
				width		: 1200,
				columns	: [
					{ name: 'Valid Member',value: 'valid', width: 100 },
					{ name: 'First Name', value: 'firstname', width: 150 },
					{ name: 'Last Name', value: 'lastname', width: 150 },
					{ name: 'Email', value: 'email', width: 300 },
					{ name: 'Company', value: 'company', width: 100 },
					{ name: 'Role', value: 'role', width: 100 },
					{ name: 'Actions', value: 'actions', width: 250 },
					{ width: 100 }
				]
			});
		}
	}
});
