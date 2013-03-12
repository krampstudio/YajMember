package org.yajug.users.persistence;

import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mongodb.DB;
import com.mongodb.MongoClient;

/**
 * Enables you to connect to a mongo server
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Singleton
public class MongoConnector {
	
	private final static Logger logger = LoggerFactory.getLogger(MongoConnector.class);
	
	/** A client connected to server (thread-safe) */
	private static MongoClient mongoClient;
	
	/** The current database (thread-safe) */
	private DB database;
	
	/**
	 * Creates a connector to a Mongo base
	 * @param host the hostname defaulted to localhost
	 * @param port the port defaulted to 27017
	 * @param name the base name defaulted to yajmember
	 */
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
			
			logger.debug("Connecting to {}:{}", host, port);
			
			try {
				mongoClient = new MongoClient(host, port);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		logger.debug("Using to {}", name);
		database = mongoClient.getDB(name);
	}
	
	/**
	 * Get the current instance of mongo {@link DB}
	 * @return the connected database
	 */
	public DB getDatabase() {
		return database;
	}
}
