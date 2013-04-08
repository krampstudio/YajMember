package org.yajug.users.api;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yajug.users.api.helper.MemberImportHelper;
import org.yajug.users.domain.Event;
import org.yajug.users.domain.Member;
import org.yajug.users.service.DataException;
import org.yajug.users.service.EventService;
import org.yajug.users.service.MemberService;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;

/**
 * This controller expose the Event API over HTTP
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Path("event")
public class EventController extends RestController {

	private final static Logger logger = LoggerFactory.getLogger(EventController.class);
	
	/** The service instance that manages events */
	@Inject private EventService eventService;
	
	/** The service instance that manages members */
	@Inject private MemberService memberService;
	
	/** Helps you to parse and import event's registrants */
	@Inject private MemberImportHelper memberImportHelper;
	
	/**
	 * Get the list of events either for the current or a particular year <br>
	 * 
	 * Example:<br>
	 * {@code curl -i -H "Accept: application/json" http://localhost:8000/YajMember/api/event/list?current=true}
	 * 
	 * @param current if true we load the events of the current year
	 * @param year if current is false, it defines which year we load the events for
	 * @return a JSON representation of the events
	 */
	@GET
	@Path("list")
	@Produces({MediaType.APPLICATION_JSON})
	public String list(@QueryParam("current") Boolean current, @QueryParam("year") Integer year){
		
		String response = "";
		Collection<Event> events = new ArrayList<>();
		
		try {
			if(BooleanUtils.isTrue(current)){
				year = Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			}
			if(year != null && year.intValue() > 1990 && year.intValue() < 2030) {	
				//yes ! a strong validation with hard coded values, I assume that
				
				Collection<Event> allEvents = eventService.getAll();
				
				//we filter events based on the event year
				final int yearFilter = year.intValue();
				events = ImmutableList.copyOf(
							Iterables.filter(
									allEvents, 
									new Predicate<Event> (){

										private final SimpleDateFormat formater = new SimpleDateFormat("yyyy");
										
										@Override
										public boolean apply(Event event) {
											return (event != null 
													&& event.getDate() != null 
													&& Integer.valueOf(formater.format(event.getDate())) == yearFilter);
										}
									}
								)
							);
			}
			response = serializer.get().toJson(events);
			
		} catch (DataException e) {
			logger.error(e.getLocalizedMessage(), e);
			response = serializeException(e);
		}
		return response;
	}
	
	/**
	 * Get the list of years where there was events. 
	 * The current year is always present.<br>
	 * 
	 * Example:<br>
	 * {@code curl -i -H "Accept: application/json" http://localhost:8000/YajMember/api/event/getYears}
	 * 
	 * @return a JSON representation of the list.
	 */
	@GET
	@Path("getYears")
	@Produces({MediaType.APPLICATION_JSON})
	public String getEventYears(){
		String response = "";
		
		try {
			//we use a TreeSet to avoid duplicate and get the years ordered
			TreeSet<Integer> years = Sets.newTreeSet();
			int currentYear = Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			years.add(currentYear);
			
			Collection<Event> allEvents = eventService.getAll();
			
			final SimpleDateFormat formater = new SimpleDateFormat("yyyy");
			for (Event event : allEvents) {
				int year = Integer.valueOf(formater.format(event.getDate()));
				if(!years.contains(year)){
					years.add(year);
				}
			}
			
			response = serializer.get().toJson(years.descendingSet());
		} catch (DataException e) {
			logger.error(e.getLocalizedMessage(), e);
			response = serializeException(e);
		} 
		return response;
	}
	
	/**
	 * Get an event from it's identifier<br>
	 * 
	 * Example:<br>
	 * {@code curl -i -H "Accept: application/json"  http://localhost:8000/YajMember/api/event/getOne?id=1}
	 * 
	 * @param id
	 * @return  a JSON representation of the event
	 */
	@GET
	@Path("getOne")
	@Produces({MediaType.APPLICATION_JSON})
	public String getOne(@QueryParam("id") String id){
		String response = "";
		
		try {
			Event event = eventService.getOne(id);
			response = serializer.get().toJson(event);
			
		} catch (DataException e) {
			logger.error(e.getLocalizedMessage(), e);
			response = serializeException(e);
		} 
		return response;
	}
	
	/**
	 * Add a new event<br>
	 * 
	 * Example:<br>
	 * {@code curl -i -H "Accept: application/json" -X PUT -d "event={key:...}"  http://localhost:8000/YajMember/api/event/update}
	 * 
	 * @param event the event to add in JSON format (a parsing/mapping will be done)
	 * @return a JSON object that contains the 'saved' property
	 */
	@PUT
	@Path("add")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String add(@FormParam("event") String event){
		return saveEvent(event);
	}
	
	/**
	 * Update an event<br>
	 * 
	 * Example:<br>
	 * {@code curl -i -H "Accept: application/json" -X POST -d "event={key:...}"  http://localhost:8000/YajMember/api/event/update}
	 * 
	 * @param event the event to update in JSON format (a parsing/mapping will be done)
	 * @return a JSON object that contains the 'saved' property
	 */
	@POST
	@Path("update")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String update(@FormParam("event") String event){
		return saveEvent(event);
	}
	
	/**
	 * Save an event
	 * @param eventData
	 * @return the json response 
	 */
	private String saveEvent(String eventData){
		
		JsonObject response = new JsonObject();
		boolean saved = false;
		
		Event event = serializer.get().fromJson(eventData, Event.class);
		try {
			saved = this.eventService.save(event);
		} catch (DataException e) {
			logger.error(e.getLocalizedMessage(), e);
			response.addProperty("error", e.getLocalizedMessage());
		} 
		response.addProperty("saved", saved);
		
		return serializer.get().toJson(response);
	}
	
	/**
	 * Remove an event<br>
	 * 
	 * Example:<br>
	 * {@code curl -i -H "Accept: application/json" -X DELETE  http://localhost:8000/YajMember/api/event/remove/1}
	 * 
	 * @param id the eventidentifier
	 * @return a JSON object that contains the 'removed' property
	 */
	@DELETE
	@Path("remove/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public String remove(@PathParam("id") String id){
		
		JsonObject response = new JsonObject();
		boolean removed = false;
		
		try {
			removed = eventService.remove(new Event(id));
		} catch (DataException e) {
			logger.error(e.getLocalizedMessage(), e);
			response.addProperty("error", e.getLocalizedMessage());
		} 
		response.addProperty("removed", removed);
		
		return  serializer.get().toJson(response);
	}
	
	
	/**
	 * Update an the participants of an event.<br>
	 * 
	 * Example:<br>
	 * {@code curl -i -H "Accept: application/json" -X POST -d "registered=[1,...]&participant=[2,...]"  http://localhost:8000/YajMember/api/event/updateParticipant/12}
	 * 
	 * @param id the key of the event to update
	 * @param registeredData JSON array of the registered member's ids
	 * @param participantData JSON array of the participants member's ids
	 * @return a JSON object that contains the 'saved' property
	 */
	@POST
	@Path("updateParticipant/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String updateParticipant(
			@PathParam("id") String id, 
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
			Event event = eventService.getOne(id);
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
	 * @param id the event id
	 * @return a JSON object that contains the status of the import
	 */
	@POST
	@Path("importRegistrants/{id}")
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
			@PathParam("id") String id){
		
		JsonObject response = new JsonObject();
		
		try {
			//check MIME TYPE
			if(bodyPart == null || !bodyPart.getMediaType().isCompatible(new MediaType("text", "csv"))){
				throw new DataException("Unknow or unsupported file type");
			} 

			//get the CSV columns in the right order
			Type listType = new TypeToken<ArrayList<String>>() {}.getType();
			List<String> fields = serializer.get().fromJson(order, listType);
			
			if(fields == null || fields.isEmpty() || !fields.contains("email")){
				throw new DataException("CSV fields must contains at least the email.");
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
