package org.yajug.users.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import org.yajug.users.domain.Member;
import org.yajug.users.persistence.MongoDao;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

@Singleton
public class MemberMongoDao extends MongoDao{

	public List<Member> getAll(){
		List<Member> members = new ArrayList<>();
		
		DBCursor cursor = getCollection("members").find();
		try {
            while(cursor.hasNext()) {
            	Member member = map(Member.class, (BasicDBObject)cursor.next());
                if(member != null){
                	members.add(member);
                }
            }
        } finally {
            cursor.close();
        }
		return members;
	}
	
	public Member getOne(long key){
		Member member = null;
		if(key > 0){
			DBCursor cursor = getCollection("members").find(new BasicDBObject("key", key)).limit(1);
			try {
	            while(cursor.hasNext()) {
	            	member = map(Member.class, (BasicDBObject)cursor.next());
	            }
	        } finally {
	            cursor.close();
	        }
		}
		return member;
	}
	
	public List<Member> search(String expression){
		
		//create a mongo search query
		String query = "{ $or : [" +
				"'firstName' : " + expression + "," +
				"'lastName' : " + expression + "," +
				"'email' : " + expression + "," +
				"'company' : " + expression + 
			"]}";
		
		return Lists.newArrayList(
			//	getCollection("members").find(query)
			);
	}
	
	public boolean insert(Member member){
		boolean saved = false;
		if(member != null){
			//get next key
			if(member.getKey() <= 0){
				member.setKey(getNextKey("members"));
			}
			BasicDBObject doc =  new BasicDBObject("key", member.getKey())
                    .append("firstName", member.getFirstName())
                    .append("lastName",  member.getLastName())
                    .append("company",  member.getCompany())
                    .append("email", member.getEmail());
			if(member.getRoles() != null){
				doc.append("roles", enumsToStrings(member.getRoles()));
			}
			if(member.getMemberships() != null){
				doc.append("memberShips", domainToIds(member.getMemberships()));
			}
			saved = handleWriteResult(getCollection("members").insert(doc));
		}
		
		return saved;
	}
	
	public boolean save(Member member){
		boolean saved = false;
//		if(member != null){
//			WriteResult wr = getCollection("members").save(member);
//			saved = wr.getError() != null;
//			if(member.getMemberships() != null){
//				
//			}
//			
//		}
		
		return saved;
	}
	
	public boolean remove(Member member){
		boolean removed = false;
		if(member != null && member.getKey() > 0){
			removed = handleWriteResult(getCollection("members").remove(new BasicDBObject("key", member.getKey())));
		}
		return removed;
	}
}
