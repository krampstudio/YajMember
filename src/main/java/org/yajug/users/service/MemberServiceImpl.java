package org.yajug.users.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
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
	
	private List<Member> setUp(List<Member> members, boolean checkValidy){
		if(members != null){
			if(checkValidy){
				//TODO implement this.checkValidity(members);
			}
		}
		return members;
	}
	
	/**
	 * Validate search expression: only alpha num chars, dot and \@ are allowed
	 * @param expression
	 * @return
	 */
	private boolean validateSearchExpression(String expression){
		return Pattern.compile("^[\\p{Alnum}.@]*$").matcher(expression).find();
	}
	
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
		return setUp(memberMongoDao.getAll(), checkValidy);
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
		members = setUp(memberMongoDao.search(expression), checkValidy);
		
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
		
		int expected = members.size();
		int saved = 0;
		for(Member member : members){
			if(StringUtils.isNotBlank(member._getId())){
				if(memberMongoDao.update(member)){
					saved++;
				}
			} else {
				if(memberMongoDao.insert(member)){
					saved++;
				}
			}
		}
		return saved == expected;
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
}
