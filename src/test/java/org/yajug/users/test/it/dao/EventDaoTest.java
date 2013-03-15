package org.yajug.users.test.it.dao;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import org.yajug.users.domain.Event;
import org.yajug.users.persistence.MongoConnector;
import org.yajug.users.persistence.dao.EventDao;
import org.yajug.users.persistence.dao.EventMongoDao;
import org.yajug.users.test.it.TestModule;

import com.google.inject.Inject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * Integration test that check basic CRUD operations on the {@link EventMongoDao}
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Guice(modules=TestModule.class)
@Test(groups={"integration", "dao"})
public class EventDaoTest {
	
	private final static Logger logger = LoggerFactory.getLogger(EventDaoTest.class);
	
	@Inject private EventDao dao;
	@Inject private MongoConnector connector;
	
	@BeforeClass
	public void loadMembers(){
		//clean up and insert data to the collection
		DBCollection members = connector.getDatabase().getCollection("events");
		
		logger.debug("clean up events collection");
		
		members.remove(new BasicDBObject());
		BasicDBList docs = (BasicDBList) com.mongodb.util.JSON.parse("[" +
			"{ 'title' : 'YaJUG++ Hackerfest at Hackerspace' , 'description' : \"An informal hackers gathering. No strings attached, no presentation, bring your laptops and favorite beverages and hack away with your fellow Juggers\" , 'date' : { '$date' : '2012-10-10T00:00:00.000Z'}}," +
			"{ 'title' : 'De la synchronisation aux transactions en m�moire et aux acteurs' , 'description' : \"Depuis la g�n�ralisation des processeurs multicoeurs, il n'est plus possible � la JVM de mentir : le multithread ex�cute bien du code en parall�le.\" , 'date' : { '$date' : '2012-06-26T00:00:00.000Z'}}," +
			"{ 'title' : 'Les 5 mercenaires de Devops' , 'description' : \"5 cowboys mercenaires viennent des 4 coins de l'Europe francophone pour venir vous pr�senter en avant-premi�re leur session de Devoxx France.\" , 'date' : { '$date' : '2012-04-16T00:00:00.000Z'}},"+
			"{ 'title' : 'XWiki' , 'description' : \"When developing a web application the traditional way is to develop the application from scratch using a general purpose language such as PHP, Grails, Java/JSP, etc.\" , 'date' : { '$date' : '2012-03-20T00:00:00.000Z'}}"+
		"]");
		
		logger.debug("inserting {} documents", docs.size());
		
		for(Object doc : docs){
			members.insert((DBObject)doc);
		}
	}
	
	@Test(enabled=true)
	public void testGetEvents(){
		assertNotNull(dao);
		List<Event> events = dao.getAll();
		assertNotNull(events);
		assertEquals(events.size(), 4);
		assertNotNull(events.get(3));
		assertNotNull(events.get(3).getKey());
		assertEquals(events.get(3).getTitle(), "XWiki");
	}
	
}