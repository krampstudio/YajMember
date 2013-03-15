package org.yajug.users.service;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yajug.users.domain.Member;
import org.yajug.users.domain.Membership;
import org.yajug.users.domain.Role;
import org.yajug.users.domain.utils.KeyValidator;
import org.yajug.users.persistence.dao.MemberMongoDao;
import org.yajug.users.persistence.dao.MembershipMongoDao;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Implementation of the {@link MemberService} that use JPA to persist data.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Singleton
public class MemberServiceImpl implements MemberService {

	private final static Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
	
	@Inject private MemberMongoDao memberMongoDao;
	@Inject private MembershipMongoDao membershipMongoDao;
	
	public void checkValidity(Collection<Member> members, boolean updateRole) throws DataException{
		if(members != null){
			Collection<Member> membersToUpdate = new ArrayList<>();
			int currentYear  = Calendar.getInstance().get(Calendar.YEAR);
			
			for(Member member : members){
				boolean saveMember = false;
				Collection<Membership> memberships = getMemberships(member);
				
				for(Membership membership : memberships){
					if(membership.getYear() > 0 && membership.getYear() == currentYear){
						//the member is valid
						member.setValid(true);
						
						//we check if the roles are consistents
						if(updateRole && !member.getRoles().contains(Role.MEMBER)){
							member.setRole(Role.MEMBER);
							saveMember = true;
						}
						break;
					}
				}
				if(updateRole && memberships.size() > 0 && !member.isValid()){
					member.setRole(Role.OLD_MEMBER);
					saveMember = true;
				}
				if(saveMember){
					membersToUpdate.add(member);
				}
			}
			
			if(updateRole && membersToUpdate.size() > 0){
				this.save(membersToUpdate);
			}
		}
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
		List<Member> members = memberMongoDao.getAll();
		if(members != null){
			this.checkValidity(members, false);
		}
		return members;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Member> findAll(String expression) throws DataException {
		List<Member> members = new ArrayList<>();
		
		if(StringUtils.isNotBlank(expression)){
			
			//expression validated against a pattern
			if(!validateSearchExpression(expression)){
				throw new ValidationException("Invalid search pattern");
			}
			members = memberMongoDao.search(expression);
			if(members != null){
				this.checkValidity(members, false);
			}
		}
		return members;
	}
	
	@Override
	public List<String> findCompanies(String expression) throws DataException {
		List<String> companies = new ArrayList<>();
		
		if(StringUtils.isNotBlank(expression)){
			
			//expression validated against a pattern
			if(!validateSearchExpression(expression)){
				throw new ValidationException("Invalid search pattern");
			}
			companies = memberMongoDao.getCompanies(expression);
		}
		return companies;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Membership> getMemberships(Member member) throws DataException {
		if(member == null || !KeyValidator.validate(member.getKey())){
			throw new ValidationException("Unable to retrieve a membership from an empty or invalid member");
		}
		return  membershipMongoDao.getAllByMember(member.getKey());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Membership getMembership(String key) throws DataException {
		if (!KeyValidator.validate(key)) {
			throw new ValidationException("Unable to retrieve a membership from an empty or invalid key");
		}
		return  membershipMongoDao.getOne(key);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean save(Member member) throws DataException{
		
		if(member == null){
			throw new ValidationException("Cannot save a null member");
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
			throw new ValidationException("Cannot save null members");
		}
		
		int expected = members.size();
		int saved = 0;
		for(Member member : members){
			if(member != null){
				if(memberMongoDao.isNew(member)){
					if(memberMongoDao.insert(member)){
						saved++;
					}
				} else if(KeyValidator.validate(member.getKey())) {
					if(memberMongoDao.update(member)){
						saved++;
					}
				}
			}
		}
		return saved == expected;
	}
	
	@Override
	public boolean saveMemberships(Collection<Membership> memberships) throws DataException {
		
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
				if(membershipMongoDao.isNew(membership)){
					if(membershipMongoDao.insert(membership)){
						saved++;
					}
				} else if (KeyValidator.validate(membership.getKey())) {
					if(membershipMongoDao.update(membership)){
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
	public boolean remove(Member member) throws DataException {
		if(member == null || !KeyValidator.validate(member.getKey())){
			throw new ValidationException("Cannot remove an empty or invalid member");
		}
		//remove also member's memberships
		for(Membership membership : this.getMemberships(member)){
			try{
				this.removeMembership(membership);
			} catch(DataException dae){
				logger.error("Error while removing a membership, continue removals", dae);
			}
		}
		return  memberMongoDao.remove(member);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeMembership(Membership membership) throws DataException {
		if(membership == null || !KeyValidator.validate(membership.getKey())){
			throw new DataException("Cannot remove an empty or invalid membership");
		}
		return  membershipMongoDao.remove(membership);
	}

	@Override
	public List<Member> findMember(Member member) {
		List<Member> found = new ArrayList<>();
		if(member != null){
			//search field by field: email, name
			List<Member> foundByEmail = memberMongoDao.search(member.getEmail());
			if(foundByEmail.size() == 0){
				List<Member> foundByName = memberMongoDao.search(member.getLastName());
				if(foundByName.size() > 0){
					found.addAll(foundByName);
				}
			} else {
				found.addAll(foundByEmail);
			}
		}
		return found;
	}
}