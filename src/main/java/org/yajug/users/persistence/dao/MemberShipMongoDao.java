package org.yajug.users.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import org.yajug.users.domain.MemberShip;
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
public class MemberShipMongoDao extends MongoDao{
	
	private final static String COLLECTION_NAME = "memberShips";
	
	/**
	 * Get the members collection (ie. db.members in mongo)
	 * @return the collection
	 */
	private DBCollection memberShips(){
		return getCollection(COLLECTION_NAME);
	}

	public List<MemberShip> getAll(){
		List<MemberShip> memberships = new ArrayList<>();
		
		DBCursor cursor = memberShips().find();
		try {
            while(cursor.hasNext()) {
            	MemberShip membership = map(MemberShip.class, (BasicDBObject)cursor.next());
                if(membership != null){
                	memberships.add(membership);
                }
            }
        } finally {
            cursor.close();
        }
		return memberships;
	}
	
	public List<MemberShip> getAllByMember(long memberKey){
		List<MemberShip> memberships = new ArrayList<>();
		
		if(memberKey > 0){
			DBCursor cursor = memberShips().find(new BasicDBObject("member", memberKey));
			try {
	            while(cursor.hasNext()) {
	            	MemberShip membership = map(MemberShip.class, (BasicDBObject)cursor.next());
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
	
	public MemberShip getOne(long key){
		MemberShip membership = null;
		if(key > 0){
			DBCursor cursor = memberShips().find(new BasicDBObject("key", key)).limit(1);
			try {
	            while(cursor.hasNext()) {
	            	membership = map(MemberShip.class, (BasicDBObject)cursor.next());
	            }
	        } finally {
	            cursor.close();
	        }
		}
		return membership;
	}
	
	public boolean insert(MemberShip membership){
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
						memberShips().insert(doc)
					);
		}
		
		return saved;
	}
	
	public boolean update(MemberShip membership){
		boolean saved = false;
		if(membership != null){
			//get next key
			if(membership.getKey() > 0){
				BasicDBObject query = new BasicDBObject("key", membership.getKey());
				BasicDBObject doc = new BasicDBObject("amount", membership.getAmount())
		                    .append("paiementDate",  membership.getPaiementDate())
		                    .append("year",  membership.getYear())
							.append("event", (membership.getEvent() != null) ? membership.getEvent().getKey() : null)
							.append("member", ( membership.getMember() != null) ? membership.getMember().getKey() : null);
				
				saved = handleWriteResult(
						memberShips().update(query, doc)
					);
			}
		}
		return saved;
	}
	
	public boolean remove(MemberShip member){
		boolean removed = false;
		if(member != null && member.getKey() > 0){
			removed = handleWriteResult(
						memberShips().remove(new BasicDBObject("key", member.getKey()))
					);
		}
		return removed;
	}
}
