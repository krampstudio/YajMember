package org.yajug.users.domain;


/**
 * Provides shared properties by all the domain objects like the identifier.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public abstract class DomainObject {

	protected static final String TEXT_PATTERN = "^[\\w\\t \\-_+:'\\\"]{1,250}$";
	protected static final String BLOCK_PATTERN = "^[\\w\\s\\-_+:'\\\"]*$";
	
	protected String _id;
	
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
