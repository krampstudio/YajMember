package org.yajug.users.service;


import java.util.List;

import org.yajug.users.domain.Member;

public interface MemberService {

	List<Member> getAll() throws DataException;
	
	boolean save(Member member) throws DataException;
}
