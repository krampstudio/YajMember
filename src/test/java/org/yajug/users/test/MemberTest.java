package org.yajug.users.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.yajug.users.domain.Member;
import org.yajug.users.service.DataException;
import org.yajug.users.service.MemberService;
import org.yajug.users.service.MemberServiceImpl;

public class MemberTest {

	@Test
	public void test() {
		MemberService service = new MemberServiceImpl();
		
		assertNotNull(service);
		
		try {
			Member m = new Member();
			m.setFirstName("Bertrand");
			service.save(m);
		} catch (DataException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

}
