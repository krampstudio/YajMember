package org.yajug.users.test.it.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.yajug.users.config.ModuleHelper;
import org.yajug.users.domain.Member;
import org.yajug.users.domain.Role;
import org.yajug.users.persistence.dao.MemberMongoDao;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * Integration test that check basic CRUD operations on the {@link MemberMongoDao}
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@RunWith(JukitoRunner.class)
public class MemberDaoTest {
	
	@Inject private MemberMongoDao dao;
	
	@Test
	public void testGetMembers(){
		assertNotNull(dao);
		
		//get the members list
		List<Member> members = dao.getAll();
		assertNotNull(members);
		assertTrue(members.size() > 0);
		assertNotNull(members.get(0));
		assertTrue(members.get(0).getKey() != null);
	}
	
	@Test
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
	
	public static class  TestModule extends JukitoModule{
		@Override
		protected void configureTest() {
			ModuleHelper.bindProperties(binder());
		}
	}
}