package org.yajug.users.test.it.dao;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import org.yajug.users.domain.Event;
import org.yajug.users.domain.Member;
import org.yajug.users.domain.utils.KeyValidator;
import org.yajug.users.json.DateSerializer;
import org.yajug.users.persistence.MongoConnector;
import org.yajug.users.persistence.dao.EventDao;
import org.yajug.users.persistence.dao.EventMongoDao;
import org.yajug.users.test.it.TestModule;

import com.google.common.collect.Lists;
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
	@Inject private DateSerializer dateSerializer;
	
	/**
	 * Drop the events collection and insert some test documents
	 */
	@BeforeClass
	public void loadEvents(){
		//clean up and insert data to the collection
		DBCollection events = connector.getDatabase().getCollection("events");
		
		logger.debug("clean up events collection");
		events.remove(new BasicDBObject());
		
		BasicDBList docs = (BasicDBList) com.mongodb.util.JSON.parse("[" +
			"{ '_id':{'$oid' :'51439178e4b03426a2a062aa'}, 'title' : 'YaJUG++ Hackerfest at Hackerspace' , 'description' : \"An informal hackers gathering. No strings attached, no presentation, bring your laptops and favorite beverages and hack away with your fellow Juggers\" , 'date' : { '$date' : '2012-10-10T00:00:00.000Z'}, 'registrants': ['51439178e4b03426a2a062bb', '51439178e4b03426a2a062bc'], 'participants': ['51439178e4b03426a2a062bb', '51439178e4b03426a2a062bc']}," +
			"{ '_id':{'$oid' :'51439178e4b03426a2a062ab'}, 'title' : 'De la synchronisation aux transactions en mémoire et aux acteurs' , 'description' : \"Depuis la généralisation des processeurs multicoeurs, il n'est plus possible à la JVM de mentir : le multithread exécute bien du code en parallèle.\" , 'date' : { '$date' : '2012-06-26T00:00:00.000Z'}}," +
			"{ '_id':{'$oid' :'51439178e4b03426a2a062ac'}, 'title' : 'Les 5 mercenaires de Devops' , 'description' : \"5 cowboys mercenaires viennent des 4 coins de l'Europe francophone pour venir vous présenter en avant-première leur session de Devoxx France.\" , 'date' : { '$date' : '2012-04-16T00:00:00.000Z'}},"+
			"{ '_id':{'$oid' :'51439178e4b03426a2a062ad'}, 'title' : 'XWiki' , 'description' : \"When developing a web application the traditional way is to develop the application from scratch using a general purpose language such as PHP, Grails, Java/JSP, etc.\" , 'date' : { '$date' : '2012-03-20T00:00:00.000Z'}}"+
		"]");
		
		logger.debug("inserting {} documents", docs.size());
		
		for(Object doc : docs){
			events.insert((DBObject)doc);
		}
	}
	
	/**
	 * Test the method {@link EventDao#getAll()}
	 */
	@Test(enabled=true)
	public void testGetEvents(){
		
		List<Event> events = dao.getAll();
		assertNotNull(events);
		assertEquals(events.size(), 4);
		assertNotNull(events.get(3));
		assertTrue(KeyValidator.validate(events.get(3).getKey()));
	}
	
	/**
	 * Provides event's key/title pairs
	 * @return the data
	 */
	@DataProvider(name="getOneProvider")
	public static Object[][] getOneProvider(){
		return new Object[][]{
			{"51439178e4b03426a2a062aa", "YaJUG++ Hackerfest at Hackerspace", 2, 2},
			{"51439178e4b03426a2a062ad", "XWiki", 0, 0}
		};
	}
	
	/**
	 * Test the method {@link EventDao#getOne(String)}
	 * @param key the key used to retrieved the event
	 * @param title the expected title
	 */
	@Test(dataProvider="getOneProvider", enabled=true)
	public void testGetOne(String key, String title, int expectedRegistrants, int expectedParticipants){
		Event event = dao.getOne(key);
		assertNotNull(event);
		assertEquals(event.getKey(), key);
		assertEquals(event.getTitle(), title);
		if(expectedRegistrants > 0){
			assertEquals(event.getRegistrants().size(), expectedRegistrants);
			assertTrue(KeyValidator.validate(event.getRegistrants().get(0).getKey()));
		} else {
			assertNull(event.getRegistrants());
		}
		if(expectedParticipants > 0){
			assertEquals(event.getParticipants().size(), expectedParticipants);
			assertTrue(KeyValidator.validate(event.getParticipants().get(0).getKey()));
		} else {
			assertNull(event.getParticipants());
		}
	}
	
	/**
	 * Test the method {@link EventDao#insert(Event)}
	 */
	@Test(enabled=true)
	public void testInsert(){
		Event event = new Event("Java show", "A show about the java flavours", new Date());
		
		//add it
		assertTrue(dao.insert(event));
		
		//check if the new key is assigned
		assertTrue(KeyValidator.validate(event.getKey()));
	
		//try to retrieve the inserted event
		Event inserted = dao.getOne(event.getKey());
		assertNotNull(inserted);
		assertEquals("Java show", inserted.getTitle());
		assertEquals(event.getKey(), inserted.getKey());
	}
	
	/**
	 * Provides data to update an event
	 * @return the data
	 */
	@DataProvider(name="updateEventProvider")
	public static Object[][] updateEventProvider(){
		return new  Object[][]{{
				"51439178e4b03426a2a062aa",
				"An hacking session",
				new Date(),
				Lists.newArrayList(new Member("51439178e4b03426a2a062bb"), new Member("51439178e4b03426a2a062bc"), new Member("51439178e4b03426a2a062bd")),
				new ArrayList<Member>(0),
			}
		};
	}
	
	/**
	 * Test the method {@link EventDao#update(Event)}
	 * @param key the key of the event to update 
	 * @param updatedDesc the new value of the description field
	 * @param updatedDate the new value of the date field
	 * @param updatedRegistrants the new value of the registrants field
	 * @param updatedParticipants the new value of the participants field
	 */
	@Test(dataProvider="updateEventProvider", enabled=true)
	public void testUpdate(String key, 
			String updatedDesc, 
			Date updatedDate, 
			List<Member> updatedRegistrants, 
			List<Member> updatedParticipants){
		
		Event event = dao.getOne(key);
		assertNotNull(event);
		
		event.setDescription(updatedDesc);
		event.setDate(updatedDate);
		event.setRegistrants(updatedRegistrants);
		event.setParticipants(updatedParticipants);
		
		assertTrue(dao.update(event));
		
		Event updated = dao.getOne(key);
		assertNotNull(event);
		assertEquals(updated.getDescription(), updatedDesc);
		assertEquals(dateSerializer.getFormatter().format(updated.getDate()), dateSerializer.getFormatter().format(updatedDate));
		assertEquals(updated.getRegistrants(), updatedRegistrants);
		assertEquals(updated.getParticipants(), updatedParticipants);
	}
	
	/**
	 * Test the method {@link EventDao#remove(Event)}
	 */
	@Test(enabled=true)
	public void testInsertUpdateDelete(){
		
		String key = "51439178e4b03426a2a062ad";
		
		Event event = dao.getOne(key);
		assertNotNull(event);
		
		//remove it
		assertTrue(dao.remove(event));
		
		//check we can't get it anymore
		Event deleted = dao.getOne(key);
		assertNull(deleted);
	}
	
	/**
	 * Clean up again the collection
	 */
	@AfterClass
	public void cleanUp(){
		logger.debug("clean up events collection");
		DBCollection events = connector.getDatabase().getCollection("events");
		events.remove(new BasicDBObject());
	}
}