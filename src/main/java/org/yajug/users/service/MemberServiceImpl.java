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
import org.yajug.users.persistence.dao.MemberDao;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Implementation of the {@link MemberService} API that delegates
 * persistence calls to DAOs.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Singleton
public class MemberServiceImpl implements MemberService {

	/**
	 * The logger
	 */
	private final static Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
	
	/**
	 * To manage the {@link Member} related data against the store.
	 */
	@Inject private MemberDao memberDao;
	
	/**
	 * Use to update the memberships that belongs to a member
	 */
	@Inject private MembershipService membershipService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkValidity(Collection<Member> members, boolean updateRole) throws DataException{
		if(members != null){
			Collection<Member> membersToUpdate = new ArrayList<>();
			int currentYear  = Calendar.getInstance().get(Calendar.YEAR);
			
			for(Member member : members){
				boolean saveMember = false;
				Collection<Membership> memberships = membershipService.getAllByMember(member);
				
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
	 * {@inheritDoc}
	 */
	@Override
	public Member getOne(String key) throws DataException {
		if (!KeyValidator.validate(key)) {
			throw new ValidationException("Unable to retrieve a member from a wrong id");
		}
		return memberDao.getOne(key);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Member> getAll() throws DataException {
		List<Member> members = memberDao.getAll();
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
				throw new ValidationException("Invalid search pattern : " + expression);
			}
			members = memberDao.search(expression);
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
			companies = memberDao.getCompanies(expression);
		}
		return companies;
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
				if(KeyValidator.validate(member.getKey())) {
					if(memberDao.update(member)){
						saved++;
					}
				} else {
					if(memberDao.insert(member)){
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
		for(Membership membership : membershipService.getAllByMember(member)){
			try{
				membershipService.remove(membership);
			} catch(DataException dae){
				logger.error("Error while removing a membership, continue removals", dae);
			}
		}
		return  memberDao.remove(member);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Member> findMember(Member member) {
		List<Member> found = new ArrayList<>();
		if(member != null){
			//search field by field: email, name
			List<Member> foundByEmail = memberDao.search(member.getEmail());
			if(foundByEmail.size() == 0){
				List<Member> foundByName = memberDao.search(member.getLastName());
				if(foundByName.size() > 0){
					found.addAll(foundByName);
				}
			} else {
				found.addAll(foundByEmail);
			}
		}
		return found;
	}
	
	/**
	 * Validate search expression: only alpha num chars, dot and \@ are allowed
	 * @param expression
	 * @return
	 */
	private boolean validateSearchExpression(String expression){
		return Pattern.compile("^[\\p{Alnum}.@+\\-_]*$").matcher(expression).find();
	}
}