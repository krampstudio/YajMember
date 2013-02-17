package org.yajug.users.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import org.yajug.users.domain.Event;
import org.yajug.users.domain.Member;
import org.yajug.users.domain.Membership;
import org.yajug.users.persistence.MongoDao;

import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

/**
 * Manage memberships data from/to the Mongo collection
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Singleton
public class MembershipMongoDao extends MongoDao<Membership>{
	

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
		Long memberId = null;
		Long eventId = null;
		
		if(dbObject.containsField("member")){
			memberId = dbObject.getLong("member");
			dbObject.remove("member");
		}
		if(dbObject.containsField("event")){
			eventId = dbObject.getLong("event");
			dbObject.remove("event");
		}
		
		membership = super.map(Membership.class, dbObject);
		
		if(memberId != null && memberId > 0){
			membership.setMember(new Member(memberId));
		}
		if(eventId != null && eventId > 0){
			membership.setEvent(new Event(eventId));
		}
		
		return membership;
	}

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
	
	public List<Membership> getAllByMember(long memberKey){
		List<Membership> memberships = new ArrayList<>();
		
		if(memberKey > 0){
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
		}
		return memberships;
	}
	
	@Override
	public Membership getOne(long key){
		Membership membership = null;
		if(key > 0){
			DBCursor cursor = memberships().find(new BasicDBObject("key", key)).limit(1);
			try {
	            while(cursor.hasNext()) {
	            	membership = map((BasicDBObject)cursor.next());
	            }
	        } finally {
	            cursor.close();
	        }
		}
		return membership;
	}
	
	public boolean insert(Membership membership){
		boolean saved = false;
		if(membership != null){
			//get next key
			if(membership.getKey() <= 0){
				membership.setKey(getNextKey(COLLECTION_NAME));
			}
			BasicDBObject doc = 
					new BasicDBObject("key", membership.getKey())
		                    .append("amount", membership.getAmount())
		                    .append("paiementDate",  membership.getPaiementDate())
		                    .append("year",  membership.getYear());
			if(membership.getEvent() != null){
				doc.append("event", membership.getEvent().getKey());
			}
			if(membership.getMember() != null){
				doc.append("member", membership.getMember().getKey());
			}
			saved = handleWriteResult(
						memberships().insert(doc)
					);
		}
		
		return saved;
	}
	
	public boolean update(Membership membership){
		boolean saved = false;
		if(membership != null){
			//get next key
			if(membership.getKey() > 0){
				BasicDBObject query = new BasicDBObject("key", membership.getKey());
				BasicDBObject doc = 
						new BasicDBObject("amount", membership.getAmount())
			                    .append("paiementDate",  membership.getPaiementDate())
			                    .append("year",  membership.getYear())
								.append("event", (membership.getEvent() != null) ? membership.getEvent().getKey() : null)
								.append("member", ( membership.getMember() != null) ? membership.getMember().getKey() : null);
				saved = handleWriteResult(
						memberships().update(query, new BasicDBObject("$set", doc))
					);
			}
		}
		return saved;
	}
	
	public boolean remove(Membership member){
		boolean removed = false;
		if(member != null && member.getKey() > 0){
			removed = handleWriteResult(
						memberships().remove(new BasicDBObject("key", member.getKey()))
					);
		}
		return removed;
	}
}
