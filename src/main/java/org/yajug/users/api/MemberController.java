package org.yajug.users.api;

import java.text.SimpleDateFormat;
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
import org.yajug.users.service.DataException;
import org.yajug.users.service.MemberService;
import org.yajug.users.vo.GridVo;

import com.google.gson.JsonObject;
import com.google.inject.Inject;

@Path("user")
public class MemberController extends RestController {

	@Inject
	private MemberService memberService;
	
	@GET
	@Path("list")
	@Produces({MediaType.APPLICATION_JSON})
	public String list(@QueryParam("callback") String callback) {
		
		String response = "";
		
		try {
			List<Member> members = memberService.getAll();
			
			response = serializeJsonp(new GridVo(members), callback);
			
		} catch (DataException e) {
			e.printStackTrace();
			response = serializeException(e);
		} 
		return response;
	}
	
	@PUT
	@Path("add")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String add(@FormParam("member") String memberData, @FormParam("validMembership") boolean validMembership){
		
		JsonObject response = new JsonObject();

		boolean saved = false;
		
		
		Member member = getSerializer().fromJson(memberData, Member.class);
		
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
		
		return getSerializer().toJson(response);
	}
	
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}
}
