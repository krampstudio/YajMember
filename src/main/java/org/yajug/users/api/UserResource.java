package org.yajug.users.api;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import org.yajug.users.service.DataException;
import org.yajug.users.service.MemberService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.inject.Inject;

@Path("user")
public class UserResource {

	@Inject
	private MemberService memberService;
	
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
		
		JsonObject response = new JsonObject();

		boolean saved = false;
		
		Gson gson = new GsonBuilder()
						.serializeNulls()
						.create();
		
		Member member = gson.fromJson(memberData, Member.class);
		
		if(validMembership){
			Membership membership = new Membership();
			membership.setYear(Integer.valueOf(new SimpleDateFormat().format(new Date())));
			//TODO save membership
		}
		
		try {
			saved = this.memberService.save(member);
		} catch (DataException e) {
			response.addProperty("error", e.getLocalizedMessage());
		}
		
		response.addProperty("saved", saved);
		
		return gson.toJson(response);
	}
	
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}
}
