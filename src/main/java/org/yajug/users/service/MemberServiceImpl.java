package org.yajug.users.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.yajug.users.domain.Member;
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
		
		for(Member member : members){
			memberMongoDao.save(member);
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
		
		return  memberMongoDao.remove(member);
	}
	
	private boolean validateSearchExpression(String expression){
		return Pattern.compile("^[\\p{Alnum}.@]*$").matcher(expression).find();
	}
	
}
