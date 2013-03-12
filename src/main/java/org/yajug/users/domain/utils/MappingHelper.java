package org.yajug.users.domain.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yajug.users.domain.DomainObject;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;

@Singleton
public class MappingHelper {

	/**
	 * Convenient transform method from a list of {@link DomainObject}  to a set it's item's keys
	 * @param domains
	 * @return the set of the domains keys
	 */
	public Set<String> extractKeys(List<? extends DomainObject> domains){
		Set<String> keys = new HashSet<>();
		keys.addAll(Lists.transform(domains, new Function<DomainObject, String>() {
			@Override public String apply(DomainObject input) {
				return (input != null) ? input.getKey() : null;
			}
		}));
		return keys;
	}
	
	/**
	 * Map a list of enums to it's string equivalent
	 * @param enums the enums list
	 * @return the list of string
	 */
	public <E extends Enum<E>> List<String> enumsToStrings(List<E> enums){
		return Lists.transform(enums, new Function<E, String>() {
			@Override public String apply(E input) {
				return (input != null) ? input.name() : null;
			}
		});
	}
}
