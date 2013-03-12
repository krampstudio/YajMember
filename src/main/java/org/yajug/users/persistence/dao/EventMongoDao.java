package org.yajug.users.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.yajug.users.domain.Event;
import org.yajug.users.domain.utils.MappingHelper;
import org.yajug.users.persistence.MongoDao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

/**
 * Enables you to access {@link Event} data stored onto a Mongo database.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Singleton
public class EventMongoDao extends MongoDao<Event> {

	/**
	 * The name of the collection in mongo
	 */
	private final static String COLLECTION_NAME = "events";
	
	/**
	 * This instance provides some mapping facilities
	 */
	@Inject private MappingHelper mappingHelper;
	
	/**
	 * Get the events collection (ie. db.members in mongo)
	 * @return the collection
	 */
	private DBCollection events(){
		return getCollection(COLLECTION_NAME);
	}
	
	/**
	 * Get all the events from the store
	 * @return a list of the events
	 */
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
	
	/**
	 * Get an event from its identifier
	 * @param key the event identifier
	 * @return the event instance or null if not found
	 */
	@Override
	public Event getOne(String key){
		
		assert StringUtils.isNotBlank(key);
		
		Event event = null;
		DBCursor cursor = events().find(new BasicDBObject("key", key)).limit(1);
		try {
            while(cursor.hasNext()) {
            	event = map(Event.class, (BasicDBObject)cursor.next());
            }
        } finally {
            cursor.close();
        }
		return event;
	}	
	
	//TODO retrieve OID
	public boolean insert(Event event){
		
		assert event != null;
		
		BasicDBObject doc =  new BasicDBObject("title", event.getTitle())
							                    .append("description",  event.getDescription())
							                    .append("date",  event.getDate());
		if(event.getParticipants() != null){
			doc.append("participants", mappingHelper.extractKeys(event.getParticipants()));
		}
		if(event.getRegistrants() != null){
			doc.append("registrants", mappingHelper.extractKeys(event.getRegistrants()));
		}
		return handleWriteResult(events().insert(doc));
	}
	
	public boolean update(Event event){
		
		assert event != null;
		assert StringUtils.isNotBlank(event.getKey());
		
		boolean saved = false;
		BasicDBObject query =  new BasicDBObject("_id", new ObjectId(event.getKey()));
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
				events().update(query, new BasicDBObject("$set", doc))
			);
		
		return saved;
	}
	
	public boolean remove(Event event){
		
		assert event != null;
		assert StringUtils.isNotBlank(event.getKey());
		
		return handleWriteResult(
					events().remove(new BasicDBObject("_id", new ObjectId(event.getKey())))
				);
	}
}
