package org.yajug.users.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.yajug.users.domain.Event;
import org.yajug.users.domain.Member;
import org.yajug.users.domain.Membership;
import org.yajug.users.domain.utils.KeyValidator;
import org.yajug.users.persistence.MongoDao;

import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

/**
 * Enables you to access {@link Membership} data stored onto a Mongo database.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Singleton
public class MembershipMongoDao extends MongoDao<Membership> implements MembershipDao{
	
	/**
	 * The name of the collection in mongo
	 */
	private final static String COLLECTION_NAME = "memberships";
	
	/**
	 * Get the members collection (ie. db.members in mongo)
	 * @return the collection
	 */
	private DBCollection memberships(){
		return getCollection(COLLECTION_NAME);
	}
	
	/**
	 * Custom mapping to handle Member and event fields
	 * @param dbObject the {@link BasicDBObject} that contains the doc from Mongo
	 * @return the mapped {@link Membership} 
	 */
	private Membership map(BasicDBObject dbObject) {
		Membership membership = null;
		String memberKey = null;
		String eventKey = null;
		
		if(dbObject.containsField("member")){
			memberKey = dbObject.getString("member");
			dbObject.remove("member");
		}
		if(dbObject.containsField("event")){
			eventKey = dbObject.getString("event");
			dbObject.remove("event");
		}
		
		membership = super.map(Membership.class, dbObject);
		
		if(memberKey != null && KeyValidator.validate(memberKey)){
			membership.setMember(new Member(memberKey));
		}
		if(eventKey != null && KeyValidator.validate(eventKey)){
			membership.setEvent(new Event(eventKey));
		}
		
		return membership;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Membership> getAll(){
		List<Membership> memberships = new ArrayList<>();
		
		DBCursor cursor = memberships().find();
		try {
            while(cursor.hasNext()) {
            	Membership membership = map((BasicDBObject)cursor.next());
                if(membership != null){
                	memberships.add(membership);
                }
            }
        } finally {
            cursor.close();
        }
		return memberships;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Membership> getAllByMember(String memberKey){
		
		assert StringUtils.isNotBlank(memberKey);
		
		List<Membership> memberships = new ArrayList<>();
		DBCursor cursor = memberships().find(new BasicDBObject("member", memberKey));
		try {
            while(cursor.hasNext()) {
            	Membership membership = map((BasicDBObject)cursor.next());
                if(membership != null){
                	memberships.add(membership);
                }
            }
        } finally {
            cursor.close();
        }
		
		return memberships;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Membership getOne(String key){
		
		assert StringUtils.isNotBlank(key);
		
		Membership membership = null;
		DBCursor cursor = memberships().find(new BasicDBObject("_id", new ObjectId(key))).limit(1);
		try {
            while(cursor.hasNext()) {
            	membership = map((BasicDBObject)cursor.next());
            }
        } finally {
            cursor.close();
        }
		return membership;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean insert(Membership membership){
		
		assert membership != null;
		assert StringUtils.isNotBlank(membership.getKey());
		
		boolean inserted = false;
		
		BasicDBObject doc = new BasicDBObject("year",  membership.getYear())
				            .append("type", membership.getType().name());
		switch(membership.getType()){
			case PERSONNAL:
				doc.append("amount", membership.getAmount());
				doc.append("paiementDate",  membership.getPaiementDate());
				if(membership.getEvent() != null){
					doc.append("event", membership.getEvent().getKey());
				}
				break;
			case SPONSORED:
				doc.append("company",  membership.getCompany());
				break;
		}
		if(membership.getMember() != null){
			doc.append("member", membership.getMember().getKey());
		}
		
		inserted = handleWriteResult(memberships().insert(doc));
		if(doc.get( "_id") != null){
			membership._setId((ObjectId)doc.get( "_id"));
		}
		return inserted; 
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean update(Membership membership){
		
		assert membership != null;
		assert StringUtils.isNotBlank(membership.getKey());
		
		BasicDBObject query = new BasicDBObject("_id", new ObjectId(membership.getKey()));
		
		BasicDBObject doc = new BasicDBObject("type", membership.getType().name());
		switch(membership.getType()){
			case PERSONNAL:
				doc.append("amount", membership.getAmount());
				doc.append("paiementDate",  membership.getPaiementDate());
				doc.append("event", (membership.getEvent() != null) ? membership.getEvent().getKey() : null);
				break;
			case SPONSORED:
				doc.append("company",  membership.getCompany());
				break;
		}
		doc.append("member", ( membership.getMember() != null) ? membership.getMember().getKey() : null);
		return handleWriteResult(
				memberships().update(query, new BasicDBObject("$set", doc))
			);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Membership membership){
		
		assert membership != null;
		assert StringUtils.isNotBlank(membership.getKey());
		
		return handleWriteResult(
					memberships().remove(new BasicDBObject("_id", new ObjectId(membership.getKey())))
				);
	}
}