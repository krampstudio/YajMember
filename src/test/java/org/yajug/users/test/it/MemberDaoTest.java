package org.yajug.users.test.it;

import static org.junit.Assert.*;

import java.util.List;

import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.yajug.users.domain.Member;
import org.yajug.users.domain.Membership;
import org.yajug.users.domain.Role;
import org.yajug.users.persistence.dao.MemberMongoDao;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

@RunWith(JukitoRunner.class)
public class MemberDaoTest {

	@Inject private MemberMongoDao dao;
	
	@Test
	public void testGetMembers(){
		assertNotNull(dao);
		List<Member> members = dao.getAll();
		assertNotNull(members);
		assertTrue(members.size() > 0);
		assertNotNull(members.get(0));
		assertTrue(members.get(0).getKey() > 0);
	}
	
	@Test
	public void testInsertUpdateDelete(){
		Member m = new Member("john", "doe",  "ano", "jdoe@gmail.com", Lists.newArrayList(Role.OLD_MEMBER));
		m.setMembership(new Membership(24));
		
		assertTrue(dao.insert(m));
		assertTrue(m.getKey() > 0);
		
		Member inserted = dao.getOne(m.getKey());
		assertNotNull(inserted);
		assertEquals("doe", inserted.getLastName());
		
		assertTrue(dao.remove(m));
		Member deleted = dao.getOne(m.getKey());
		assertNull(deleted);
	}
	
}