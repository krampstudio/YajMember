package org.yajug.users.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("user")
public class UserResource {

	@GET
	@Produces({"application/json", "text/plain"})
	public String list(){
		Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson("ok");
	}
}
