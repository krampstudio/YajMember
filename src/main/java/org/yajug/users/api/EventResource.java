package org.yajug.users.api;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.yajug.users.domain.Event;
import org.yajug.users.service.DataException;
import org.yajug.users.service.EventService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;

@Path("event")
public class EventResource {

	@Inject
	private EventService eventService;
	
	@GET
	@Path("list")
	@Produces({MediaType.APPLICATION_JSON})
	public String list(){
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		
		List<Event> events = new ArrayList<Event>();
		try {
			events = eventService.getAll();
		} catch (DataException e) {
			e.printStackTrace();
		}
		return gson.toJson(events);
	}
}
