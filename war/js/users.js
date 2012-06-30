$(function(){
	
	$(document).ready(function(){
		$("#user-grid").flexigrid({
			url: '/yajug/users',
			dataType: 'json',
			colModel : [
			    {display: 'Valid Member', name : 'valid', sortable : true, align: 'center', hide: true},
				{display: 'Last Name', name : 'lastname', sortable : true, align: 'left', hide: true},
				{display: 'First Name', name : 'firstname', sortable : true, align: 'left', hide: true},
				{display: 'Email', name : 'email', sortable : true, align: 'left', hide: true},
				{display: 'Company', name : 'company', sortable : true, align: 'left', hide: true},
				{display: 'Role', name : 'role', sortable : true, align: 'left', hide: true}
			],
			buttons : [
		   		{name: 'Add', bclass: 'add', onpress : addUser},
		   		{name: 'Delete', bclass: 'delete', onpress : removeUser},
		   		{separator: true}
	   		],
			searchitems : [
				{display: 'Last Name', name : 'lastname', isdefault: true},
				{display: 'First Name', name : 'firstname'}
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
			width: "100%",
			onSubmit: saveUser,
			height: "100%"
		});
	});
	
	function addUser(source, grid){
		console.log(source);
		console.log(grid);
	}
	
	function saveUser(p1){
		console.log(p1);
		console.log(p2);
	}
	
	function removeUser(source, grid){
		console.log(source);
		console.log(grid);
	}
	
})(jQuery);