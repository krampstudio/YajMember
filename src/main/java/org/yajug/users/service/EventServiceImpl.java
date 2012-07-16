package org.yajug.users.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.yajug.users.domain.Event;

public class EventServiceImpl extends Service implements EventService {

	@Override
	public List<Event> getAll() throws DataException {
		List<Event> results = new ArrayList<Event>();
		
		Event e = new Event();
		e.setDate(new Date());
		e.setDescription("test");
		results.add(e);
		return results;
	}

}
