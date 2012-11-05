package org.yajug.users.service;


import java.util.Collection;
import java.util.List;

import org.yajug.users.domain.Member;

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
	List<Member> getAll() throws DataException;
	
	/**
	 * Get all the members
	 * @param checkValidy check for each member the valid status
	 * @return a list of members
	 * @throws DataException
	 */
	List<Member> getAll(boolean checkValidy) throws DataException;
	
	/**
	 * Textual search in the members  
	 * @param expression to look for 
	 * @return a list of members
	 * @throws DataException
	 */
	List<Member> findAll(String expression) throws DataException;
	
	/**
	 * Textual search in the members  
	 * @param checkValidy check for each member the valid status
	 * @param expression to look for 
	 * @return a list of members
	 * @throws DataException
	 */
	List<Member> findAll(boolean checkValidy, String expression) throws DataException;
	
	/**
	 * Save a member
	 * @param member the member to save
	 * @return true if save
	 * @throws DataException
	 */
	boolean save(Member member) throws DataException;
	
	/**
	 * Save  members
	 * @param members the members to save
	 * @return true if save
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
}
