package org.yajug.users.service;

import java.util.Collection;

import org.yajug.users.domain.Member;
import org.yajug.users.domain.Membership;
import org.yajug.users.domain.utils.KeyValidator;
import org.yajug.users.persistence.dao.MembershipDao;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Implementation of the {@link MembershipService} API that delegates
 * persistence calls to DAOs.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Singleton
public class MembershipServiceImpl implements MembershipService {

	/**
	 * To manage the {@link Membership} related data against the store.
	 */
	@Inject private MembershipDao membershipDao;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Membership> getAllByMember(Member member) throws DataException {
		if(member == null || !KeyValidator.validate(member.getKey())){
			throw new ValidationException("Unable to retrieve a membership from an empty or invalid member");
		}
		return  membershipDao.getAllByMember(member.getKey());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Membership getOne(String key) throws DataException {
		if (!KeyValidator.validate(key)) {
			throw new ValidationException("Unable to retrieve a membership from an empty or invalid key");
		}
		return  membershipDao.getOne(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean save(Collection<Membership> memberships) throws DataException {
		if(memberships == null){
			throw new ValidationException("Cannot save null memberships");
		}
		
		//do the checks prior the save operations in a non transactionnal context
		for(Membership membership : memberships){
			if(membership == null){
				throw new ValidationException("Can't save a null membership!");
			}
			if(membership.getYear() < 1990 || membership.getYear() > 2050){
				throw new ValidationException("Wrong membership year : " + membership.getYear());
			}
			if(membership.getType() == null){
				throw new ValidationException("Membership type is required.");
			}
		}
		
		int expected = memberships.size();
		int saved = 0;
		for(Membership membership : memberships){
			if(membership != null){
				if (KeyValidator.validate(membership.getKey())) {
					if(membershipDao.update(membership)){
						saved++;
					}
				} else {
					if(membershipDao.insert(membership)){
						saved++;
					}
				}
			}
		}
		return saved == expected;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Membership membership) throws DataException {
		if(membership == null || !KeyValidator.validate(membership.getKey())){
			throw new DataException("Cannot remove an empty or invalid membership");
		}
		return  membershipDao.remove(membership);
	}

}
