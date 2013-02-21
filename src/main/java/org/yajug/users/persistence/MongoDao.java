package org.yajug.users.persistence;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.yajug.users.domain.DomainObject;
import org.yajug.users.json.Serializer;

import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

/**
 * Common DAO that provides utilities to handle Mongo data
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public abstract class MongoDao<T extends DomainObject> {

	@Inject private MongoConnector connector;
	@Inject private Serializer serializer; 
	
	/**
	 * Get an domain object from it's key
	 * @param key it's key identifier (different from _id)
	 * @return the Domain object
	 */
	public abstract T getOne(long key);
	
	/**
	 * Check if an instance don't exists in the store
	 * @param model the instance
	 * @return true is the instance don't exist
	 */
	public boolean isNew(T model){
		boolean neew = true;
		if(StringUtils.isNotBlank(model._getId())){
			neew = false;
		} else {
			if(this.getOne(model.getKey()) != null){
				neew = false;
			}
		}
		return neew;
	}
	
	/**
	 * Get a mongo collection
	 * @param name the collection name
	 * @return the collection
	 */
	protected DBCollection getCollection(String name){
		return connector.getDatabase().getCollection(name);
	}
	
	/**
	 * Convenient and central {@link WriteResult} handler that checks for errors  
	 * @param wr the {@link WriteResult}  returned by a write operation
	 * @return true if the operation is written
	 * @throws MongoException wrapped error
 	 */
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
	
	/**
	 * Convenient {@link BasicDBObject} to POJO mapper
	 * @param clazz the pojo class
	 * @param dbObject the {@link BasicDBObject} 
	 * @return a new instance of {@link DomainObject} 
	 */
	protected T map(Class<T> clazz, BasicDBObject dbObject){
		if(dbObject != null){
			
			ObjectId id = null;
			//getting the ObjectId to assign it manually after to simplify JSON parsing
			if(dbObject.containsField("_id")){
				id = dbObject.getObjectId("_id");
				dbObject.remove("_id");
			}
			
			T domain = serializer.get().fromJson(JSON.serialize(dbObject), clazz);
			
			if(id != null){
				domain._setId(id.toString());
			}
			return domain;
		}
		return null;
	}
	
	/**
	 * Get the next key of a collection since we use a minimified id to expose data instead of mongo _id field
	 * @param collectionName the name of the collection to get the key for
	 * @return the next key to insert
	 */
	protected long getNextKey(String collectionName){
		
		long next = 1l;
		
		DBCursor cursor = getCollection(collectionName).find().sort(new BasicDBObject("key", "1")).limit(1);
		try{
			while(cursor.hasNext()){
				BasicDBObject dbObject = ((BasicDBObject)cursor.next());
				next = dbObject.getLong("key", 0) + 1;
			}
		} finally {
            cursor.close();
        }
		return next;
	}
}
