package org.yajug.users.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.TreeSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import org.yajug.users.domain.Event;
import org.yajug.users.service.DataException;
import org.yajug.users.service.EventService;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.google.inject.Inject;

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
}
