package org.yajug.users.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.yajug.users.domain.Member;
import org.yajug.users.domain.utils.MemberComparator;
import org.yajug.users.service.DataException;
import org.yajug.users.service.MemberService;
import org.yajug.users.vo.GridVo;

import com.google.gson.JsonObject;
import com.google.inject.Inject;

@Path("member")
public class MemberController extends RestController {

	@Inject
	private MemberService memberService;
	
	/** cache of member list */
	private Map<Long, Member> members;
	
	@GET
	@Path("list")
	@Produces({MediaType.APPLICATION_JSON})
	public String list( @QueryParam("callback") String callback,
						@QueryParam("sortName") String sortName,
						@QueryParam("sortOrder") String sortOrder,
						@QueryParam("search") String search,
						@QueryParam("page") int page,
						@QueryParam("rows") int rows ) {
		
		String response = "";
		
		try {
			List<Member> membersList = null;
			if(StringUtils.isNotBlank(search)){
				membersList = memberService.findAll(true, search);
			} else {
				membersList = getMembersList(getMembers());
			}
			
			//ordering
			if(StringUtils.isNotBlank(sortName)){
				if("desc".equalsIgnoreCase(sortOrder)){
					Collections.sort(membersList, Collections.reverseOrder(new MemberComparator(sortName)));
				} else {
					Collections.sort(membersList, new MemberComparator(sortName));
				}
			}
			
			//pagination
			if(rows <= 0){
				rows = 25;
			}
			if(page <= 0){
				page = 1;
			}
			int start = Math.min(0, Math.abs(page * rows));
			int end = Math.min(membersList.size(), Math.abs(page * rows) + rows);
			
			if(start > 0 && end != membersList.size()){
				membersList.subList(0, start).clear();
				membersList.subList(end, membersList.size()).clear();
			}
			
			//jsonize
			response = serializeJsonp(new GridVo(membersList), callback);
			
		} catch (DataException e) {
			e.printStackTrace();
			response = serializeException(e);
		} 
		return response;
	}
	
	@GET
	@Path("getOne")
	@Produces({MediaType.APPLICATION_JSON})
	public String getOne(@QueryParam("id") Long id){
		String response = "";
		
		try {
			
			if(id == null || id.longValue() <= 0){
				throw new DataException("Unable to retrieve member from a wrong id");
			}
			
			//check the cache
			if(getMembers().containsKey(id)){
				response = getSerializer().toJson(getMembers().get(id));
			}
			
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
	public String add( 	@FormParam("member") String memberData, 
						@FormParam("validMembership") boolean validMembership ){
		
		JsonObject response = new JsonObject();

		boolean saved = false;
		
		
		Member member = getSerializer().fromJson(memberData, Member.class);
		
//		if(validMembership){
//			Membership membership = new Membership();
//			membership.setYear(Integer.valueOf(new SimpleDateFormat().format(new Date())));
//			//TODO save membership
//		}
		
		try {
			saved = this.memberService.save(member);
		} catch (DataException e) {
			response.addProperty("error", e.getLocalizedMessage());
		} finally {
			this.clearMembers();
		}
		
		response.addProperty("saved", saved);
		
		return getSerializer().toJson(response);
	}
	
	private Map<Long, Member> getMembers() throws DataException{
		if(this.members == null){
			List<Member> membersList = memberService.getAll(true);
			
			//needs of thread safety
			this.members = new ConcurrentHashMap<Long, Member>(membersList.size());
			for(Member member : membersList){
				this.members.put(member.getKey(), member);
			}
		}
		return this.members;
	}
	
	private void clearMembers(){
		this.members = null;
	}
	
	private static List<Member> getMembersList(Map<Long, Member> membersMap){
		List<Member> membersList = new ArrayList<Member>();
		if(membersMap != null){
			for(Long key : membersMap.keySet()){
				membersList.add(membersMap.get(key));
			}
		}
		return membersList;
	}
	
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}
}
