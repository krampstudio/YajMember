package org.yajug.users.domain;

import org.bson.types.ObjectId;


/**
 * Provides shared properties by all the domain objects like the identifier.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public abstract class DomainObject {

	protected static final String TEXT_PATTERN = "^[\\w\\t \\-_+:'\\\"]{1,250}$";
	protected static final String BLOCK_PATTERN = "^[\\w\\s\\-_+:'\\\"]*$";
	
	protected transient ObjectId _id;
	
	protected String key;
	
	public DomainObject(){
	}
	
	public DomainObject(String  key){
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public ObjectId _getId() {
		return _id;
	}

	public void _setId(ObjectId _id) {
		this._id = _id;
		if(this._id != null){
			this.key = _id.toString();
		}
	}
}
