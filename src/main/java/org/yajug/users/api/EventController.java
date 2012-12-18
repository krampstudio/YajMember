package org.yajug.users.api;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.BooleanUtils;
import org.yajug.users.domain.Event;
import org.yajug.users.service.DataException;
import org.yajug.users.service.EventService;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

//import com.sun.jersey.multipart.FormDataParam;

@Path("event")
public class EventController extends RestController {

	@Inject
	private EventService eventService;
	
	@GET
	@Path("list")
	@Produces({MediaType.APPLICATION_JSON})
	public String list(@QueryParam("current") Boolean current, @QueryParam("year") Integer year){
		
		String response = "";
		Collection<Event> events = new ArrayList<Event>();
		
		try {
			events = eventService.getAll();
			if(BooleanUtils.isTrue(current)){
				year = Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			}
			if(year != null && year.intValue() > 1990 && year.intValue() < 2030) {	
				//yes ! a strong validation with hard coded values, I assume that
				
				//we filter events based on the event year
				SimpleDateFormat formater = new SimpleDateFormat("yyyy");
				for(Event event : events){
					if(event.getDate() != null){
						Integer eventYear = Integer.valueOf(formater.format(event.getDate()));
						if(eventYear.intValue() != year.intValue()){
							events.remove(event);
						}
					}
				}
			}
			response = getSerializer().toJson(events);
			
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
				throw new DataException("Unable to retrieve an event from a wrong id");
			}
			
			Event event = eventService.getOne(id);
			
			response = getSerializer().toJson(event);
			
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
	public String add(@FormParam("event") String event){
		return saveEvent(event);
	}
	
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
		
		Event event = getSerializer().fromJson(eventData, Event.class);
			
		try {
			
			saved = this.eventService.save(event);
			
		} catch (DataException e) {
			response.addProperty("error", e.getLocalizedMessage());
		} 
		response.addProperty("saved", saved);
		
		return getSerializer().toJson(response);
	}
	
	@PUT
	@Path("/flyer")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateFlyer(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetails) {
		
		String response = "";
		
		fileDetails.getType();
		
		return response;
	}
}
