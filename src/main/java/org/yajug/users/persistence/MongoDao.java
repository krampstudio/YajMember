package org.yajug.users.persistence;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.google.inject.Inject;

public abstract class MongoDao {

	@Inject private MongoConnector connector;
	
	protected MongoCollection getCollection(String name){
		return new Jongo(connector.getDatabase()).getCollection(name);
	}
}
