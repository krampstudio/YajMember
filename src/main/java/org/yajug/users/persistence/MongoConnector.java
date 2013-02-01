package org.yajug.users.persistence;

import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mongodb.DB;
import com.mongodb.MongoClient;

@Singleton
public class MongoConnector {
	
	private static MongoClient mongoClient;
	private DB database;
	
	@Inject 
	public MongoConnector(
			@Named("db.host") String host,
			@Named("db.port") int port,
			@Named("db.name") String name
		){
		
		//set default values
		if(StringUtils.isBlank(host)){
			host = "localhost";
		}
		if(port <= 0){
			port = 27017;
		}
		if(StringUtils.isBlank(name)){
			host = "yajmember";
		}
		
		if(mongoClient == null){
			try {
				mongoClient = new MongoClient(host, port);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		database = mongoClient.getDB(name);
	}
	
	public DB getDatabase() {
		return database;
	}
}
