package org.yajug.users.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.yajug.users.domain.Member;
import org.yajug.users.domain.Role;
import org.yajug.users.service.DataException;
import org.yajug.users.service.MemberService;
import org.yajug.users.service.MemberServiceImpl;

@RunWith(Parameterized.class)
public class MemberTest {

	private MemberService service;
	
	private Member member;
	
	public MemberTest(Member member) {
		this.member = member;
	}
	
	@Parameters
 	public static Collection<Object[]> data() {
 		return Arrays.asList(new Object[][] {
 			{ new Member("Bertrand", "Chevrier", null, null, null) },
 			{ new Member("Bertrand", "Chevrier", "bertrand.chevrier@yajug.org", "yajug", null) },
 			{ new Member("Bertrand", "Chevrier", "bertrand.chevrier@yajug.org", "yajug", Arrays.asList(new Role[]{Role.MEMBER, Role.BOARD})) },
 		});
 	}
	
	
	@Before
	public void setUpService(){
		if(service == null){
			service = new MemberServiceImpl();
		}
		assertNotNull(service);
	}
	
	@Test
	public void testSave() {
		
		try {
			assertNotNull(this.member);
			service.save(this.member);
			
		} catch (DataException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

}
