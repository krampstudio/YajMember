package org.yajug.users.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.yajug.users.domain.Member;
import org.yajug.users.domain.Member_;

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
		return getAll(false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Member> getAll(boolean checkValidy) throws DataException {
		
		List<Member> members = new ArrayList<Member>();
		EntityManager em = getEntityManager();
		try{
			TypedQuery<Member> tq = em.createNamedQuery("Member.findAll", Member.class);
			members = tq.getResultList();
			
			if(checkValidy && members != null){
				for(Member member : members){
					member.checkValidity();
				}
			}
		} finally{
			em.close();
		}
		return members;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Member> findAll(String expression) throws DataException {
		return findAll(false, expression);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Member> findAll(boolean checkValidy, String expression) throws DataException {
		List<Member> members = new ArrayList<Member>();
		
		if(!validateSearchExpression(expression)){
			throw new DataException("invalid search pattern");
		}
		
		EntityManager em = getEntityManager();
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Member> query = cb.createQuery(Member.class);
			Root<Member> member = query.from(Member.class);

			String exp = "%" + expression.toLowerCase() + "%";
			
			query
				.select(member)
				.where(
					cb.or(
						cb.like(cb.lower(member.get(Member_.firstName)), exp),
						cb.like(cb.lower(member.get(Member_.lastName)), exp),
						cb.like(cb.lower(member.get(Member_.email)), exp),
						cb.like(cb.lower(member.get(Member_.company)), exp)
					)
				);
			
			TypedQuery<Member> tq = em.createQuery(query);
			members = tq.getResultList();
			
			if(checkValidy && members != null){
				for(Member m : members){
					m.checkValidity();
				}
			}
			
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
	
	private boolean validateSearchExpression(String expression){
		return Pattern.compile("^[\\p{Alnum}.@]*$").matcher(expression).find();
	}
	
}
