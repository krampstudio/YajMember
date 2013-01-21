package org.yajug.users.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.yajug.users.domain.Member;
import org.yajug.users.domain.Membership;
import org.yajug.users.domain.Event;
import org.yajug.users.persistence.dao.MemberMongoDao;

import com.google.inject.Inject;

/**
 * Implementation of the {@link MemberService} that use JPA to persist data.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class MemberServiceImpl implements MemberService {

	
	@Inject private MemberMongoDao memberMongoDao;
	
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
		
		List<Member> members = memberMongoDao.getAll();
		if(checkValidy && members != null){
			for(Member member : members){
				member.checkValidity();
			}
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
		
		//TODO validate expression against javascript injections
		members = memberMongoDao.search(expression);
			
		if(checkValidy && members != null){
			for(Member m : members){
				m.checkValidity();
			}
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
				
				Member previousMember = em.find(Member.class, member.getKey());
				if(previousMember != null){
					
					previousMember.setFirstName(member.getFirstName());
					previousMember.setLastName(member.getLastName());
					previousMember.setCompany(member.getCompany());
					previousMember.setRoles(member.getRoles());
					previousMember.setEmail(member.getEmail());
					
					for(Membership membership : member.getMemberships()){
						if(membership.getKey() > 0){
							for(Membership entityMembership : previousMember.getMemberships()){
								if(entityMembership.getKey() == membership.getKey()){
									if(membership.getPaiementDate() != null){
										entityMembership.setPaiementDate(membership.getPaiementDate());
									}
									if(membership.getEvent() != null && membership.getEvent().getKey() > 0){
										entityMembership.setEvent(em.find(Event.class, membership.getEvent().getKey()));
									}
									break; //there is only one membership
								}
							}
						}
					}
					em.merge(previousMember);
					
				} else {
					em.persist(member);
				}
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
