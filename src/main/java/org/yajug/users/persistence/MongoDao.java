package org.yajug.users.persistence;

import java.util.List;

import org.bson.types.ObjectId;
import org.yajug.users.domain.DomainObject;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

public abstract class MongoDao {

	@Inject private MongoConnector connector;
	
	protected DBCollection getCollection(String name){
		return connector.getDatabase().getCollection(name);
	}
	
	protected boolean handleWriteResult(WriteResult wr) throws MongoException{
		boolean written = false;
		if(wr != null){
			if(wr.getError() != null){
				throw new MongoException(wr.getError());
			} else {
				written = true;
			}
		}
		return written;
	}
	
	protected <T extends DomainObject> T map(Class<T> clazz, BasicDBObject dbObject){
		if(dbObject != null){
			Gson gson = new GsonBuilder()
							.serializeNulls()
							.setDateFormat("yyyy-MM-dd")
							.create();
			ObjectId id = null;
			if(dbObject.containsField("_id")){
				id = dbObject.getObjectId("_id");
				dbObject.remove("_id");
			}
			T domain = gson.fromJson(JSON.serialize(dbObject), clazz);
			if(id != null){
				domain._setId(id.toString());
			}
			return domain;
		}
		return null;
	}
	
	protected long getNextKey(String collectionName){
		
		long next = 0l;
		
		DBCursor cursor = getCollection("members").find().sort(new BasicDBObject("key", "1")).limit(1);
		try{
			while(cursor.hasNext()){
				next = ((BasicDBObject)cursor.next()).getLong("key") + 1;
			}
		} finally {
            cursor.close();
        }
		return next;
	}
	
	protected List<Long> domainToIds(List<? extends DomainObject> domains){
		return Lists.transform(domains, new Function<DomainObject, Long>() {
			@Override public Long apply(DomainObject input) {
				return (input != null) ? input.getKey() : null;
			}
		});
	}
	
	protected <E extends Enum<E>> List<String> enumsToStrings(List<E> enums){
		return Lists.transform(enums, new Function<E, String>() {
			@Override public String apply(E input) {
				return (input != null) ? input.name() : null;
			}
		});
	}
}
