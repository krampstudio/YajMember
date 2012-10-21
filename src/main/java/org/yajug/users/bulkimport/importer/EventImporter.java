package org.yajug.users.bulkimport.importer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.yajug.users.bulkimport.reader.DomainReader;
import org.yajug.users.domain.Event;
import org.yajug.users.service.DataException;
import org.yajug.users.service.EventService;

import com.google.inject.Inject;

public class EventImporter implements DomainImporter {

	@Inject
	private DomainReader<Event> reader;
	
	@Inject
	private EventService eventService;
	
	@Override
	public int doImport(String fileName) {
		
		int imported = 0;
		
		Collection<Event> events = new ArrayList<Event>();
		try {
			events = reader.read(fileName);
			
			if(events.size() > 0){
				//be sure the key is null
				for(Event event : events){
					event.setKey(0);
				}
				if(eventService.save(events)){
					imported = events.size();
				}
			}
			
		} catch (IOException | DataException e) {
			e.printStackTrace();
		}

		return imported;
	}

}
