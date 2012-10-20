package org.yajug.users.bulkimport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.yajug.users.bulkimport.reader.DomainReader;
import org.yajug.users.domain.Event;
import org.yajug.users.service.EventService;

import com.google.inject.Inject;

public class EventImporter implements DomainImporter {

	@Inject
	private DomainReader<Event> reader;
	
	@Inject
	private EventService eventService;
	
	@Override
	public int doImport(String fileName) {
		Collection<Event> events = new ArrayList<Event>();
		try {
			events = reader.read(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return events.size();
	}

}
