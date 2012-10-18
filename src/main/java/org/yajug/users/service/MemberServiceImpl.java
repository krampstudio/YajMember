package org.yajug.users.service;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.yajug.users.domain.Member;
import org.yajug.users.domain.Membership;

public class MemberServiceImpl extends Service implements MemberService {

	@Override
	public List<Member> getAll() throws DataException {
		
		List<Member> members = new ArrayList<Member>();
		EntityManager em = getEntityManager();
		try{
			TypedQuery<Member> tq = em.createNamedQuery("Member.findAll", Member.class);
			members = tq.getResultList();
		} finally{
			em.close();
		}
		return members;
	}
	
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
		} finally{
			em.close();
		}
		return true;
	}

	@Override
	public boolean remove(Member member) throws DataException {
		if(member == null){
			throw new DataException("Cannot save a null member");
		}
		
		EntityManager em = getEntityManager();
		try{
			em.getTransaction().begin();
			em.remove(member);
			em.getTransaction().commit();
		} finally{
			em.close();
		}
		return true;
	}
	
//	private boolean isValidMember(Member member, int year) throws DataException {
//		if(member == null){
//			throw new DataException("Cannot check for a null member");
//		}
//		
//		EntityManager em = getEntityManager();
//		try{
//			TypedQuery<Membership> tq = em.createNamedQuery("Membership.getAllByMember", Membership.class);
//			tq.setParameter("member", member);
//			
//		} finally{
//			em.close();
//		}
//		return false;
//	}
}
