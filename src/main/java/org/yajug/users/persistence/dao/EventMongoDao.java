package org.yajug.users.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import org.yajug.users.domain.Event;
import org.yajug.users.domain.utils.MappingHelper;
import org.yajug.users.persistence.MongoDao;

import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class EventMongoDao extends MongoDao<Event> {

	private final static String COLLECTION_NAME = "events";
	
	@Inject private MappingHelper mappingHelper;
	
	/**
	 * Get the events collection (ie. db.members in mongo)
	 * @return the collection
	 */
	private DBCollection events(){
		return getCollection(COLLECTION_NAME);
	}
	
	public List<Event> getAll(){
		List<Event> events = new ArrayList<>();
		
		DBCursor cursor = events().find().sort(new BasicDBObject("date", -1));
		try {
            while(cursor.hasNext()) {
            	Event event = map(Event.class, (BasicDBObject)cursor.next());
                if(event != null){
                	events.add(event);
                }
            }
        } finally {
            cursor.close();
        }
		return events;
	}
	
	@Override
	public Event getOne(long key){
		Event event = null;
		if(key > 0){
			DBCursor cursor = events().find(new BasicDBObject("key", key)).limit(1);
			try {
	            while(cursor.hasNext()) {
	            	event = map(Event.class, (BasicDBObject)cursor.next());
	            }
	        } finally {
	            cursor.close();
	        }
		}
		return event;
	}	
	
	public boolean insert(Event event){
		boolean saved = false;
		if(event != null){
			//get next key
			if(event.getKey() <= 0){
				event.setKey(getNextKey(COLLECTION_NAME));
			}
			BasicDBObject doc =  new BasicDBObject("key", event.getKey())
                    .append("title", event.getTitle())
                    .append("description",  event.getDescription())
                    .append("date",  event.getDate());
			if(event.getParticipants() != null){
				doc.append("participants", mappingHelper.extractKeys(event.getParticipants()));
			}
			if(event.getRegistrants() != null){
				doc.append("registrants", mappingHelper.extractKeys(event.getRegistrants()));
			}
			saved = handleWriteResult(
						events().insert(doc)
					);
		}
		return saved;
	}
	
	public boolean update(Event event){
		boolean saved = false;
		if(event != null){
			//get next key
			if(event.getKey() > 0){
				BasicDBObject query =  new BasicDBObject("key", event.getKey());
				BasicDBObject doc =  
					new BasicDBObject("title", event.getTitle())
                    .append("description",  event.getDescription())
                    .append("date",  event.getDate());
				if(event.getParticipants() != null){
					doc.append("participants", mappingHelper.extractKeys(event.getParticipants()));
				}
				if(event.getRegistrants() != null){
					doc.append("registrants", mappingHelper.extractKeys(event.getRegistrants()));
				}
				saved = handleWriteResult(
						events().update(query, doc)
					);
			}
		}
		return saved;
	}
	
	public boolean remove(Event event){
		boolean removed = false;
		if(event != null && event.getKey() > 0){
			removed = handleWriteResult(
						events().remove(new BasicDBObject("key", event.getKey()))
					);
		}
		return removed;
	}
}
