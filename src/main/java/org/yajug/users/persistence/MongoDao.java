package org.yajug.users.persistence;

import org.yajug.users.domain.DomainObject;
import org.yajug.users.domain.Member;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

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
			return gson.fromJson(dbObject.toString(), clazz);
		}
		return null;
	}
}
