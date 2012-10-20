package org.yajug.users.api;

import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.yajug.users.domain.Event;
import org.yajug.users.service.DataException;
import org.yajug.users.service.EventService;

import com.google.inject.Inject;

@Path("event")
public class EventController extends RestController {

	@Inject
	private EventService eventService;
	
	@GET
	@Path("list")
	@Produces({MediaType.APPLICATION_JSON})
	public String list(){
		
		String response = "";
		Collection<Event> events = new ArrayList<Event>();
		
		try {
			events = eventService.getAll();
			response = getSerializer().toJson(events);
			
		} catch (DataException e) {
			e.printStackTrace();
			response = serializeException(e);
		}
		return response;
	}
}
