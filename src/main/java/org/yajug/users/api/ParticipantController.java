package org.yajug.users.api;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yajug.users.api.helper.MemberImportHelper;
import org.yajug.users.domain.Event;
import org.yajug.users.domain.Member;
import org.yajug.users.service.DataException;
import org.yajug.users.service.EventService;
import org.yajug.users.service.MemberService;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;

/**
 * This controller expose the Event's Participant API over HTTP
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Path("participant")
public class ParticipantController extends RestController {

	private final static Logger logger = LoggerFactory.getLogger(ParticipantController.class);
	
	/** The service instance that manages events */
	@Inject private EventService eventService;
	
	/** The service instance that manages members */
	@Inject private MemberService memberService;
	
	/** Helps you to parse and import event's registrants */
	@Inject private MemberImportHelper memberImportHelper;
	
	/**
	 * Update an the participants of an event.<br>
	 * 
	 * Example:<br>
	 * {@code curl -i -H "Accept: application/json" -X POST -d "registered=[1,...]&participant=[2,...]"  http://localhost:8000/YajMember/api/event/updateParticipant/12}
	 * 
	 * @param eventId the key of the event to update
	 * @param registeredData JSON array of the registered member's ids
	 * @param participantData JSON array of the participants member's ids
	 * @return a JSON object that contains the 'saved' property
	 */
	@POST
	@Path("save/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String save(
			@PathParam("id") String eventId, 
			@FormParam("registrant")  @DefaultValue("[]") String registeredData, 
			@FormParam("participant") @DefaultValue("[]") String participantData){
		
		JsonObject response = new JsonObject();
		boolean saved = false;
		
		
		//unserialize the JSON ids to lists
		Type listType = new TypeToken<ArrayList<String>>() {}.getType();
		List<String> registeredIds = serializer.get().fromJson(registeredData, listType);
		List<String> participantIds = serializer.get().fromJson(participantData, listType);
	
		//function used to map an id to a member
		Function <String, Member> idToMember = new Function<String, Member>() {
			@Override public Member apply(String input) {
				return new Member(input);
			}
		};
		
		//do the mapping
		List<Member> registereds = Lists.transform(registeredIds, idToMember);
		List<Member> participants = Lists.transform(participantIds, idToMember);
		
		try {
			//create a bag instance
			Event event = eventService.getOne(eventId);
			if(event != null){
				event.setParticipants(participants);
				event.setRegistrants(registereds);
				saved = eventService.save(event);
			}
		} catch (DataException e) {
			logger.error(e.getLocalizedMessage(), e);
			response.addProperty("error", serializeException(e));
		} 
		response.addProperty("saved", saved);
		return serializer.get().toJson(response);
	}
	
	/**
	 * Import registrant from a CSV file.
	 * 
	 * @param stream  uploaded file data.
	 * @param contentDisposition  uploaded file meta
	 * @param bodyPart uploaded file meta
	 * @param ignoreHeader option to ignore the 1st line of the file, true by default
	 * @param delimiter the CSV delimiter, <i>,</i> by default
	 * @param wrapper the CSV field wrapper, <i>"</i>  by default
	 * @param order a JSON encoded array of the fields to import, in the same order than the file columns
	 * @param eventId the event id
	 * @return a JSON object that contains the status of the import
	 */
	@POST
	@Path("import/{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/plain; charset=UTF-8")
	public String importRegistrants(
			@FormDataParam("reg-import-file") InputStream stream,
			@FormDataParam("reg-import-file") FormDataContentDisposition contentDisposition,
			@FormDataParam("reg-import-file") FormDataBodyPart bodyPart, 
			@FormDataParam("reg-import-opts-no-head") @DefaultValue("true") boolean ignoreHeader,
			@FormDataParam("reg-import-opts-del") @DefaultValue(",") String delimiter,
			@FormDataParam("reg-import-opts-wrap") @DefaultValue("\"") String wrapper,
			@FormDataParam("reg-import-opts-order") @DefaultValue("") String order,
			@PathParam("id") String eventId){
		
		JsonObject response = new JsonObject();
		
		try {
			//check MIME TYPE
			if(bodyPart == null || !bodyPart.getMediaType().isCompatible(new MediaType("text", "csv"))){
				throw new DataException("Unknow or unsupported file type");
			} 

			//get the CSV columns in the right order
			Type listType = new TypeToken<ArrayList<String>>() {}.getType();
			List<String> fields = serializer.get().fromJson(order, listType);
			
			//fields check
			if(fields == null || fields.isEmpty() || !fields.contains("email")){
				throw new DataException("CSV fields must contains at least the email.");
			}
			if(fields.contains("name") && (fields.contains("firstName") || fields.contains("lastName") )){
				throw new DataException("Ambiguous fields selection: name should not be select with first or last name");
			}
			
			Collection<Member> members =  memberImportHelper.readFromCsv(
					stream, 
					fields, 
					ignoreHeader, 
					delimiter.charAt(0), 
					wrapper.charAt(0)
				);
		
			JsonArray membersStates = new JsonArray();
			
			//TODO check members against the db 
			for(Member member : members) {

				JsonObject memberState = new JsonObject();
				List<Member> found = Lists.newArrayList(memberService.findMember(member));
				if(found.size() == 0){
					if(memberService.save(member)){
						memberState.addProperty("state", "added");
						memberState.add("member", serializeRegistrant(member));
					}
				} else if (found.size() == 1){
					memberState.addProperty("state", "exists");
					memberState.add("member", serializeRegistrant(found.get(0)));
					
				} else if (found.size() > 1){
					
					memberState.addProperty("state", "ambiguous");
					memberState.add("given", serializeRegistrant(member));
					JsonArray ambiguous = new JsonArray();
					for(Member foundMember : found){
						ambiguous.add(serializeRegistrant(foundMember));
					}
					memberState.add("ambiguous", ambiguous);
				}
				membersStates.add(memberState);
			}
			
			response.add("members", membersStates);
			
		} catch (DataException e) {
			logger.error(e.getLocalizedMessage(), e);
			response.addProperty("error", serializeException(e));
		}
		return serializer.get().toJson(response);
	}
	
	/**
	 * Serialize partially a member to a JsonObject
	 * @param member the member to serialize
	 * @return the JsonObject that represents the member
	 */
	private JsonObject serializeRegistrant(Member member){
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("key", member.getKey());
		jsonObject.addProperty("name", member.getName());
		jsonObject.addProperty("company", member.getCompany());
		jsonObject.addProperty("email", member.getEmail());
		return jsonObject;
	}
}
