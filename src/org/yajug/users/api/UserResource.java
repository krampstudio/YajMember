package org.yajug.users.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("user")
public class UserResource {

	@GET
	@Path("list")
	@Produces({MediaType.APPLICATION_JSON})
	public String list(@QueryParam("callback") String callback){
		
		final String expected = callback+"({" +
				"\"list\":[{" +
					"\"id\":\"1\"," +
					"\"valid\":true," +
					"\"email\":\"chevrier.bertrand@gmail.com\"," +
					"\"company\":\"\"," +
					"\"role\":\"\"," +
					"\"lastname\":\"chevrier\"," +
					"\"firstname\":\"bertrand\"" +
				"},{" +
					"\"id\":\"2\"," +
					"\"valid\":true," +
					"\"email\":\"emmylou.boquet@gmail.com\"," +
					"\"company\":\"\"," +
					"\"role\":\"\"," +
					"\"lastname\":\"boquet\"," +
					"\"firstname\":\"emmylou\"" +
				"}]," +
				"\"total\":2"  +
			"})";
		return expected;
	}
	
	@PUT
	@Path("add")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String add(@FormParam("member") String member){
		
		System.out.print(member);
		
		return "{\"saved\" : true}";
	}
	
	
}
