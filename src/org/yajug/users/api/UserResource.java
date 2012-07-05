package org.yajug.users.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("user")
public class UserResource {

	@GET
	@Path("list")
	@Produces({"application/json"})
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
}
