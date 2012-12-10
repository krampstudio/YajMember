package org.yajug.users.test;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.yajug.users.domain.Member;
import org.yajug.users.service.DataException;
import org.yajug.users.service.MemberService;
import org.yajug.users.service.MemberServiceImpl;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MemberServiceTest {

	@Mock private EntityManager em;
	
	@InjectMocks private MemberService memberService = new MemberServiceImpl();
	
	@Test
	public void testUpdate() throws DataException {
		doAnswer(new Answer<Member>() {

			@Override
			public Member answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return (Member)args[0];
			}
		}).when(em).merge(any(Member.class));
		
		memberService.save(new Member());
	}
}
