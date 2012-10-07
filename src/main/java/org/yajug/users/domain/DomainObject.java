package org.yajug.users.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Provides shared properties by all the domain objects like the identifier.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Entity
@MappedSuperclass
public abstract class DomainObject {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic protected long  key;
	
	public DomainObject(){
	}

	/**
	 * @return the key
	 */
	public long getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(long key) {
		this.key = key;
	}
}
