package org.yajug.users.test.it.dao;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import org.yajug.users.domain.Member;
import org.yajug.users.domain.Role;
import org.yajug.users.persistence.MongoConnector;
import org.yajug.users.persistence.dao.MemberMongoDao;
import org.yajug.users.test.it.TestModule;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * Integration test that check basic CRUD operations on the {@link MemberMongoDao}
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Guice(modules=TestModule.class)
@Test(groups={"integration", "dao"})
public class MemberDaoTest {
	
	private final static Logger logger = LoggerFactory.getLogger(MemberDaoTest.class);
	
	@Inject private MemberMongoDao dao;
	@Inject private MongoConnector connector;
	
	@BeforeClass
	public void loadMembers(){
		//clean up and insert data to the collection
		DBCollection members = connector.getDatabase().getCollection("members");
		
		logger.debug("clean up members collection");
		
		members.remove(new BasicDBObject());
		BasicDBList docs = (BasicDBList) com.mongodb.util.JSON.parse(
			"[{'firstName': 'Jackson', 'lastName': 'Turner', 'email': 'jturner@zib.com', 'company': 'ZIB SA', 'roles': ['OLD_MEMBER']}," +
			"{'firstName': 'Megan', 'lastName': 'Perry', 'email': 'megan.perry@qmail.com', 'company': '', 'roles': ['MEMBER']}," +
			"{'firstName': 'Ryan', 'lastName': 'Harris', 'email': 'harrisr01@zahoo.biz', 'company': 'Zaboo & Co', 'roles': ['OLD_MEMBER', 'SPONSOR']}]"
		);
		
		logger.debug("inserting {} documents", docs.size());
		
		for(Object doc : docs){
			members.insert((DBObject)doc);
		}
	}
	
	@Test(enabled=true)
	public void testGetMembers(){
		assertNotNull(dao);
		
		//get the members list
		List<Member> members = dao.getAll();
		assertNotNull(members);
		assertEquals(3, members.size());
		assertNotNull(members.get(0));
		assertTrue(members.get(0).getKey() != null);
		assertEquals(members.get(0).getEmail(), "jturner@zib.com");
	}
	
	@Test(enabled=true)
	public void testInsertUpdateDelete(){
		Member m = new Member("john", "doe",  "ano", "jdoe@gmail.com", Lists.newArrayList(Role.OLD_MEMBER));
		
		//add it
		assertTrue(dao.insert(m));
		
		//check if the new key is assigned
		assertTrue(m.getKey() != null);
	
		//try to retrieve the inserted member
		Member inserted = dao.getOne(m.getKey());
		assertNotNull(inserted);
		assertEquals("doe", inserted.getLastName());
		assertEquals(m.getKey(), inserted.getKey());
		
		//do some changes
		inserted.setEmail("jdoe2@gmail.com");
		
		//update it
		assertTrue(dao.update(inserted));
		
		//search on the modified field
		List<Member> searched = dao.search("jdoe2@gmail.com");
		assertNotNull(searched);
		assertTrue(searched.size() > 0);
		assertEquals("jdoe2@gmail.com", searched.get(0).getEmail());
		assertEquals(m.getKey(), searched.get(0).getKey());
		
		//remove it
		assertTrue(dao.remove(inserted));
		
		//check we can't get it anymore
		Member deleted = dao.getOne(m.getKey());
		assertNull(deleted);
	}
	
}