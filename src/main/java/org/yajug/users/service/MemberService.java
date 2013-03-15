package org.yajug.users.service;


import java.util.Collection;
import java.util.List;

import org.yajug.users.domain.Member;
import org.yajug.users.domain.Membership;

/**
 * The service provides common management of {@link Member}s
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public interface MemberService {

	/**
	 * Get a member from its identifier
	 * @param key the identifier
	 * @return the member or null
	 * @throws DataException
	 */
	Member getOne(String key)  throws DataException;
	
	/**
	 * Get all the members
	 * @return a list of members
	 * @throws DataException
	 */
	Collection<Member> getAll() throws DataException;
	
	/**
	 * Textual search in the members  
	 * @param expression to look for 
	 * @return a list of members
	 * @throws DataException
	 */
	Collection<Member> findAll(String expression) throws DataException;
	
	/**
	 * Find members' companies based on an expression
	 * @param expression to look for 
	 * @return a list of company name
	 * @throws DataException
	 */
	Collection<String> findCompanies(String expression) throws DataException;
	
	/**
	 * Save a member
	 * @param member the member to save
	 * @return true if save
	 * @throws DataException
	 */
	boolean save(Member member) throws DataException;
	
	/**
	 * Get the member's {@link Membership}
	 * @param member the member to get the membership from
	 * @return
	 * @throws DataException
	 */
	Collection<Membership> getMemberships(Member member) throws DataException;
	
	/**
	 * Get a {@link Membership} from it's identifier
	 * @param key the membership identifier
	 * @return the membership instance
	 * @throws DataException
	 */
	Membership getMembership(String key) throws DataException;
	
	/**
	 * Save  a collection of {@link Members}
	 * @param members the members to save
	 * @return true if saved
	 * @throws DataException
	 */
	boolean save(Collection<Member> members) throws DataException;
	
	/**
	 * Save a collection of {@link Membership}
	 * @param memberships
	 * @return true if saved
	 * @throws DataException
	 */
	boolean saveMemberships(Collection<Membership> memberships) throws DataException;
	
	/**
	 * Remove a member
	 * @param member  the member to remove
	 * @return  true if removed
	 * @throws DataException
	 */
	boolean remove(Member member) throws DataException;
	
	/**
	 * Remove a membership
	 * @param membership the memebership to remove
	 * @return true if removed
	 * @throws DataException
	 */
	boolean removeMembership(Membership membership) throws DataException;
	
	List<Member> findMember(Member member);
}
