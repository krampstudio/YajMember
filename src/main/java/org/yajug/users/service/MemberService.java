package org.yajug.users.service;

import org.yajug.users.domain.Member;

public interface MemberService {

	boolean save(Member member) throws DataException;
}
