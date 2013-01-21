package org.yajug.users.persistence;

import java.net.UnknownHostException;

import com.google.inject.Singleton;
import com.mongodb.DB;
import com.mongodb.MongoClient;

@Singleton
public class MongoConnector {

	private static MongoClient mongoClient;
	private DB database;
	
	public MongoConnector(){
		if(mongoClient == null){
			try {
				mongoClient = new MongoClient("localhost", 27017);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		database = mongoClient.getDB("yajmember");
	}
	
	public DB getDatabase() {
		return database;
	}
}
