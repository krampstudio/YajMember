package org.yajug.users.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.yajug.users.domain.Member;
import org.yajug.users.domain.MemberShip;
import org.yajug.users.persistence.dao.MemberMongoDao;
import org.yajug.users.persistence.dao.MemberShipMongoDao;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * Implementation of the {@link MemberService} that use JPA to persist data.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class MemberServiceImpl implements MemberService {

	
	@Inject private MemberMongoDao memberMongoDao;
	@Inject private MemberShipMongoDao memberShipMongoDao;
	
	private List<Member> setUp(List<Member> members, boolean checkValidy){
		if(members != null){
			for(Member member : members){
				if(checkValidy){
					member.checkValidity();
				}
				if(member.getMemberships() != null && member.getMemberships().size() > 0){
					List<Long> memberShipIds = 
							Lists.transform(member.getMemberships(), new Function<MemberShip, Long>(){
								@Override public Long apply(MemberShip input) {
									return (input != null) ? input.getKey() : null;
								}
							});
						
					member.setMemberships(memberShipMongoDao.getAllIn(memberShipIds));
				}
			}
		}
		return members;
	}
	
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
			if(member.getMemberships() != null){
				for(MemberShip memberShip : member.getMemberships()){
					if(StringUtils.isNotBlank(memberShip._getId())){
						memberShipMongoDao.update(memberShip);
					} else {
						memberShipMongoDao.insert(memberShip);
					}
				}
			}
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
