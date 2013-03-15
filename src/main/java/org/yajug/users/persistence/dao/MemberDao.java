package org.yajug.users.persistence.dao;

import java.util.List;
import java.util.Set;

import org.yajug.users.domain.Member;

/**
 * API of the {@link Member}'s data management.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public interface MemberDao {

	/**
	 * Get all the members from the store
	 * @return the list of members
	 */
	List<Member> getAll();

	/**
	 * Get the members from their keys
	 * @param keys the keys' set
	 * @return the matching members
	 */
	List<Member> getAllIn(Set<String> keys);

	/**
	 * Get a member from its identifier
	 * @param key the member identifier
	 * @return the member instance if the key matches
	 */
	Member getOne(String key);

	/**
	 * Keyword search of members, by looking into the firstName, lastName, email and company fields
	 * @param expression the search pattern
	 * @return the list of matching members
	 */
	List<Member> search(String expression);

	/**
	 * Search company names 
	 * @param expression the search pattern
	 * @return the list of matching company names
	 */
	List<String> getCompanies(String expression);

	/**
	 * Insert an {@link Member} to the store
	 * @param member the instance to insert, if the insertion is successful the key is set 
	 * @return true if inserted
	 */
	boolean insert(Member member);

	/**
	 * Update a {@link Member} 
	 * @param member the instance to update, the key is used to identify it
	 * @return true if updated
	 */
	boolean update(Member member);

	/**
	 * Removes a {@link Member} from the store
	 * @param member the instance to remove, the key is used to identify it
	 * @return true if removed
	 */
	boolean remove(Member member);

}