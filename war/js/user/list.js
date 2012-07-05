	
	function initList(){
		$("#users").gridy({
			url: '/api/user/list',
			clickFx		: true,
			columns		: [
				{ name: 'Valid Member',value: 'valid', width: 100 },
				{ name: 'First Name', value: 'firstname', width: 100 },
				{ name: 'Last Name', value: 'lastname', width: 100 },
				{ name: 'Email', value: 'email', width: 200 },
				{ name: 'Company', value: 'company', width: 100 },
				{ name: 'Role', value: 'role', width: 100 },
				{ name: 'Actions', value: 'actions', width: 100 },
				{ width: 100 }
			],
			dataType	: 'jsonp',
			evenOdd		: true,
			height: 500,
			width: 800
			
		});
		/*
		$("#users").flexigrid({
			url: '/api/user/list',
			dataType: 'json',
			colModel : [
			    {display: 'Valid Member', name : 'valid', width: 'auto', sortable : true, align: 'center'},
				{display: 'Last Name', name : 'lastname', width: 'auto', sortable : true, align: 'left'},
				{display: 'First Name', name : 'firstname', width: 'auto', sortable : true, align: 'left'},
				{display: 'Email', name : 'email', width: 'auto', sortable : true, align: 'left'},
				{display: 'Company', name : 'company', width: 'auto', sortable : true, align: 'left'},
				{display: 'Role', name : 'role', width: 'auto', sortable : true, align: 'left'}
			],
			buttons : [
		   		{name: 'Add', bclass: 'add', onpress : addUser},
		   		{name: 'Delete', bclass: 'delete', onpress : removeUser},
		   		{separator: true}
	   		],
			searchitems : [
				{display: 'Last Name', name : 'lastname', isdefault: true},
				{display: 'First Name', name : 'firstname'},
				{display: 'Email', name : 'email'},
				{display: 'Company', name : 'company'}
			],
			sortname: "lastname",
			sortorder: "asc",
			usepager: true,
			title: 'Yajug Users',
			useRp: true,
			rp: 15,
			showTableToggleBtn: false,
			width: 'auto',
		//	onSubmit: saveUser,
			height: 'auto',
			nowrap: false
		});
	
	function addUser(source, grid){
		console.log(source);
		console.log(grid);
	}
	
	function saveUser(p1){
		console.log(p1);
	}
	
	function removeUser(source, grid){
		console.log(source);
		console.log(grid);
	}*/
	}
