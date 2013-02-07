package org.yajug.users.service;


import java.util.Collection;
import org.yajug.users.domain.Member;
import org.yajug.users.domain.Membership;

/**
 * The service provides common management of {@link Member}s
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public interface MemberService {

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
}
