package org.yajug.users.persistence.dao;

import java.util.List;

import org.yajug.users.domain.Membership;

/**
 * API of the {@link Membership}'s data management.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public interface MembershipDao {

	/**
	 * Get all the memberships from the store
	 * @return the list of memberships
	 */
	List<Membership> getAll();

	/**
	 * Get all the memberships that belongs to a member
	 * @param memberKey the key of the member to check
	 * @return the list of memberships
	 */
	List<Membership> getAllByMember(String memberKey);

	/**
	 * Get a membership from its identifier
	 * @param key the membership identifier
	 * @return the membership instance if the key match
	 */
	Membership getOne(String key);

	/**
	 * Insert a {@link Membership} to the store
	 * @param membership the instance to insert, if the insertion is successful the key is set 
	 * @return true if inserted
	 */
	boolean insert(Membership membership);

	/**
	 * Update a {@link Membership} 
	 * @param membership the instance to update, the key is used to identify it
	 * @return true if updated
	 */
	boolean update(Membership membership);

	/**
	 * Removes a {@link Membership} from the store
	 * @param membership the instance to remove, the key is used to identify it
	 * @return true if removed
	 */
	boolean remove(Membership membership);

}