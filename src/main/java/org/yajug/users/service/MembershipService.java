package org.yajug.users.service;

import java.util.Collection;

import org.yajug.users.domain.Member;
import org.yajug.users.domain.Membership;

public interface MembershipService {

	/**
	 * Get all the {@link Membership}s
	 * @return  all the available {@link Membership}s
	 * @throws DataException
	 */
	Collection<Membership> getAll() throws DataException;
	
	/**
	 * Get the {@link Member}'s {@link Membership}s
	 * @param member the member to get the membership from
	 * @return the {@link Member}'s {@link Membership}s
	 * @throws DataException
	 */
	Collection<Membership> getAllByMember(Member member) throws DataException;
	
	/**
	 * Get a {@link Membership} from it's identifier
	 * @param key the membership identifier
	 * @return the membership instance
	 * @throws DataException
	 */
	Membership getOne(String key) throws DataException;
	
	/**
	 * Save a collection of {@link Membership}
	 * @param memberships
	 * @return true if saved
	 * @throws DataException
	 */
	boolean save(Collection<Membership> memberships) throws DataException;

	/**
	 * Remove a membership
	 * @param membership the memebership to remove
	 * @return true if removed
	 * @throws DataException
	 */
	boolean remove(Membership membership) throws DataException;
}
