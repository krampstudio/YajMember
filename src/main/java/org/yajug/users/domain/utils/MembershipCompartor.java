package org.yajug.users.domain.utils;

import java.util.Comparator;

import org.yajug.users.domain.Membership;

/**
 * Compare {@link Membership}s based on their years.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class MembershipCompartor implements Comparator<Membership> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(Membership o1, Membership o2) {
		int comparison = 0;
		if(o1 != null && o2 != null){
			return o1.getYear() - o2.getYear();
		}
		return comparison;
	}
}
