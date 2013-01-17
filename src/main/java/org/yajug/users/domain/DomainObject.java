package org.yajug.users.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;

/**
 * Provides shared properties by all the domain objects like the identifier.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Entity
@MappedSuperclass
public abstract class DomainObject {

	protected static final String TEXT_PATTERN = "^[\\w\\t \\-_+:'\\\"]{1,250}$";
	protected static final String BLOCK_PATTERN = "^[\\w\\s\\-_+:'\\\"]*$";
	
	@Id
	@Basic 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Min(0)
	protected long  key;
	
	public DomainObject(){
	}
	
	public DomainObject(long  key){
		this.key = key;
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
