package org.yajug.users.service;


import java.util.Collection;

import org.yajug.users.domain.Member;

/**
 * The service provides common management of {@link Member}s
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public interface MemberService {

	/**
	 * Check the current validity of {@link Member}s regarding their roles and memberships
	 * @param members the members to check, the {@link Member}'s valid field is updated.
	 * @param updateRole if the method should update the roles that don't reflect the current validity
	 * @throws DataException
	 */
	void checkValidity(Collection<Member> members, boolean updateRole) throws DataException;
	
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
	 * Save  a collection of {@link Members}
	 * @param members the members to save
	 * @return true if saved
	 * @throws DataException
	 */
	boolean save(Collection<Member> members) throws DataException;
	
	
	/**
	 * Remove a member
	 * @param member  the member to remove
	 * @return  true if removed
	 * @throws DataException
	 */
	boolean remove(Member member) throws DataException;
	
	/**
	 * Find an existing member regarding the data of the member in parameter
	 * @param member the search member
	 * @return the matching members
	 */
	Collection<Member> findMember(Member member);
}
