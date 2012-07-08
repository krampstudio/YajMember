package org.yajug.users.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.yajug.users.domain.Member;
import org.yajug.users.domain.Membership;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
	public String add(@FormParam("member") String memberData, @FormParam("validMembership") boolean validMembership){
		
		System.out.print(memberData);
		
		Gson gson = new GsonBuilder()
						.serializeNulls()
						.create();
		
		Member member = gson.fromJson(memberData, Member.class);
		
		if(validMembership){
			Membership membership = new Membership();
			membership.setYear(Integer.valueOf(new SimpleDateFormat().format(new Date())));
			member.addMembership(membership);
		}
		
		return "{\"saved\" : true}";
	}
	
	
}
