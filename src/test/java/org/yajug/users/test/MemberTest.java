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
			service.save(new Member() {
				@Override
				public String getFirstName() {
					return "Bertrand";
				}
				@Override
				public String getLastName() {
					return "Chevrier";
				}
			});
		} catch (DataException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

}
