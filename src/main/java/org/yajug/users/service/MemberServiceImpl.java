package org.yajug.users.service;

import javax.persistence.EntityManager;

import org.yajug.users.domain.Member;

public class MemberServiceImpl extends Service implements MemberService {

	@Override
	public boolean save(Member member) throws DataException{
		
		if(member == null){
			throw new DataException("Cannot save a null member");
		}
		
		EntityManager em = getEntityManager();
		try{
			em.getTransaction().begin();
			em.persist(member);
			em.getTransaction().commit();
		}
		finally{
			em.close();
		}
		return true;
	}

}
