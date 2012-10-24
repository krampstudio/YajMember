package org.yajug.users.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.BooleanUtils;
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
				
				//we filter events based the event year
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
}
