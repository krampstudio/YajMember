package org.yajug.users.persistence;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.yajug.users.domain.DomainObject;
import org.yajug.users.domain.Member;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
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

	private final static String DATE_PATTERN = "yyyy-MM-dd";
	
	@Inject private MongoConnector connector;
	
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
	 * Create a JSON deserializer adapted for the DomainObjects
	 * @return the deserializer
	 */
	protected Gson getDeSerializer(){
		return new GsonBuilder()
					.serializeNulls()
					//manages dates
					.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

						private SimpleDateFormat formatter;
						
						@Override
						public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
								throws JsonParseException {
							
							if(formatter == null){
								formatter = new SimpleDateFormat(DATE_PATTERN);
								formatter.setTimeZone(TimeZone.getTimeZone("GMT-00:00"));
							}
							Date date = null;
								try {
								if(json.isJsonPrimitive()){	//{date : "2012-06-01"} we use common parsing 
									date = formatter.parse(json.getAsString());
								} else if(json.isJsonObject() //{date : {$date : "2012-06-01"}} we retrieve the string
									&& json.getAsJsonObject().getAsJsonPrimitive("$date") != null){
									
									String jsonDate = json.getAsJsonObject().getAsJsonPrimitive("$date").getAsString();
									date = formatter.parse(jsonDate);
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
							return date;
						}
					})
					//manage domains lists
					.registerTypeAdapter(
							List.class, 
							new JsonDeserializer<List<? extends DomainObject>>() {
						
						//create an other parser to avoid stack overflow with context parser
						private Gson gson =  new GsonBuilder().create();
						
						@Override
						public List<? extends DomainObject> deserialize(JsonElement json, final Type typeOfT, JsonDeserializationContext context)
								throws JsonParseException {
							
							//TODO handle the case where a generic is not defined 
							boolean domainList = false;
							Class<?> generic = (Class<?>)((ParameterizedType)typeOfT).getActualTypeArguments()[0];
							
							//check if the list is a list of DomainObjects
							if(DomainObject.class.isAssignableFrom(generic)){
								 domainList = true;
							}
							
							//if this is Domain Objects
							if(domainList){
								//we get the JSON array of ids
								List<Long> ids = gson.fromJson(json, new TypeToken<ArrayList<Long>>(){}.getType());
								//and transform it into a list of instance with only the key set
								return Lists.transform(ids, new Function<Long, DomainObject>() {

									@Override public DomainObject apply(Long input) {
										DomainObject domainObject = null;
										try {
											 domainObject = (DomainObject) Class.forName(typeOfT.getClass().getName()).newInstance() ;
											 domainObject.setKey(input);
										} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
											e.printStackTrace();
										}
										return domainObject;
									}
									
								});
							} 
							//or do a simple parsing
							return gson.fromJson(json, typeOfT);
						}
					})
					.create();
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
			
			T domain = getDeSerializer().fromJson(JSON.serialize(dbObject), clazz);
			
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
