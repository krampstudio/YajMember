package org.yajug.users.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.yajug.users.service.DataException;
import org.yajug.users.service.EventService;

import com.google.inject.Inject;

@Path("event")
public class EventResource {

	@Inject
	private EventService eventService;
	
	@GET
	@Path("list")
	@Produces({MediaType.APPLICATION_JSON})
	public String list(){
		try {
			eventService.getAll();
		} catch (DataException e) {
			e.printStackTrace();
		}
		return "{\"ok\":\"ok\"}";
	}
}
