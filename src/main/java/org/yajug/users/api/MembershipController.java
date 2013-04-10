package org.yajug.users.api;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yajug.users.domain.Member;
import org.yajug.users.domain.Membership;
import org.yajug.users.service.DataException;
import org.yajug.users.service.MemberService;
import org.yajug.users.service.MembershipService;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;

/**
 * This controller expose the Membership API over HTTP
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Path("membership")
public class MembershipController extends RestController {
	
	/**
	 * The logger
	 */
	private final static Logger logger = LoggerFactory.getLogger(MembershipController.class);

	/**
	 * The {@link Member} API
	 */
	@Inject private MemberService memberService;
	
	/**
	 * The {@link Membership} API
	 */
	@Inject private MembershipService membershipService;
	
	@GET
	@Path("all")
	@Produces({MediaType.APPLICATION_JSON})
	public String getAll(){
		String response = "";
		try {
			
			response = serializer.get().toJson(membershipService.getAll());
			
		} catch (DataException e) {
			logger.error(e.getLocalizedMessage(), e);
			response = serializeException(e);
		} 
		return response;
	}
	
	/**
	 * Get the {@link Membership}s of a {@link Member} from it's identifier.
	 * 
	 * 
	 * 
	 * @param id the {@link Member} identifier
	 * @return a JSON representation of the {@link Membership}s 
	 */
	@GET
	@Path("get")
	@Produces({MediaType.APPLICATION_JSON})
	public String getByMember(@QueryParam("member") String id){
		String response = "";
		
		try {
			if(StringUtils.isBlank(id)){
				throw new DataException("Unable to retrieve member from a wrong id");
			}
			Collection<Membership> memberships = new ArrayList<>();
			Member member = memberService.getOne(id);
			if(member != null){
				memberships = membershipService.getAllByMember(member);
			}
			response = serializer.get().toJson(memberships);
			
		} catch (DataException e) {
			logger.error(e.getLocalizedMessage(), e);
			response = serializeException(e);
		} 
		return response;
	}
	
	@POST
	@Path("save")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String save(@FormParam("memberships") String data) {
		
		JsonObject response = new JsonObject();
		boolean saved = false;
		
		try {
			
			Type listType = new TypeToken<ArrayList<Membership>>() {}.getType();
			List<Membership> memberships = serializer.get().fromJson(data, listType);
			
			saved = membershipService.save(memberships);
			
		} catch (DataException e) {
			logger.error(e.getLocalizedMessage(), e);
			response.addProperty("error", e.getLocalizedMessage());
		} 
		response.addProperty("saved", saved);
		
		return serializer.get().toJson(response);
	}
	
	@DELETE
	@Path("remove/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public String removeMembership(@PathParam("id") String id){
		JsonObject response = new JsonObject();
		boolean removed = false;
		
		try {
			
			Membership membership = membershipService.getOne(id);
			if(membership != null){
				removed = membershipService.remove(membership);
			}
			
		} catch (DataException e) {
			logger.error(e.getLocalizedMessage(), e);
			response.addProperty("error", e.getLocalizedMessage());
		} 
		response.addProperty("removed", removed);
			
		return serializer.get().toJson(response);
	}

}
