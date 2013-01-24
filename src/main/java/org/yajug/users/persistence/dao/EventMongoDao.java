package org.yajug.users.persistence.dao;

import java.util.List;

import org.yajug.users.domain.Event;
import org.yajug.users.persistence.MongoDao;

import com.google.common.collect.Lists;

public class EventMongoDao extends MongoDao {

	public List<Event> getAll(){
		return Lists.newArrayList(
		//	getCollection("events").find().sort("{date : 1}").as(Event.class)
			);
	}
	
	public Event getOne(long key){
		Event event = null;
//		if(key > 0){
//			event = getCollection("events").findOne("{key: #}", key).as(Event.class);
//		}
		return event;
	}	
	
	public boolean save(Event event){
		boolean saved = false;
//		if(event != null){
//			WriteResult wr = getCollection("events").save(event);
//			saved = wr.getError() != null;
//		}
		return saved;
	}
	
	public boolean remove(Event event){
		boolean removed = false;
//		if(event != null && event.getKey() > 0){
//			WriteResult wr = getCollection("events").remove("{key: #}", event.getKey());
//			removed = wr.getError() != null;
//		}
		return removed;
	}
}
