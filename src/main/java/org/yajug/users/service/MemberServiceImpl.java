package org.yajug.users.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.yajug.users.domain.Member;

/**
 * Implementation of the {@link MemberService} that use JPA to persist data.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class MemberServiceImpl extends JPAService implements MemberService {

	/**
	 * {@inheritDoc}
	 */
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean save(Member member) throws DataException{
		
		if(member == null){
			throw new DataException("Cannot save a null member");
		}
		
		Collection<Member> members = new ArrayList<Member>();
		members.add(member);
		
		return save(members);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean save(Collection<Member> members) throws DataException{
		
		if(members == null){
			throw new DataException("Cannot save null members");
		}
		
		EntityManager em = getEntityManager();
		try{
			em.getTransaction().begin();
			for(Member member : members){
				em.persist(member);
			}
			em.getTransaction().commit();
		} catch(PersistenceException pe){
			pe.printStackTrace();
		} finally{
			em.close();
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
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
	
}
