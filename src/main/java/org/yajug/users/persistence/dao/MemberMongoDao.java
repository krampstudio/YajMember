package org.yajug.users.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import org.yajug.users.domain.Member;
import org.yajug.users.persistence.MongoDao;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

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
	
	public List<Member> search(String expression){
		
		//create a mongo search query
		String query = "{ $or : [" +
				"'firstName' : " + expression + "," +
				"'lastName' : " + expression + "," +
				"'email' : " + expression + "," +
				"'company' : " + expression + 
			"]}";
		
		return Lists.newArrayList(
				getCollection("members").find(query).as(Member.class)
			);
	}
	
	public boolean insert(Member member){
		boolean saved = false;
		if(member != null){
			saved = handleWriteResult(
						getCollection("members").insert(
							"{key: #, firstName: #, lastName: #, email: #, company: #, roles: #roles}", 
							member
						)
					);
			if(member.getMemberships() != null){
				
			}
		}
		
		return saved;
	}
	
	public boolean save(Member member){
		boolean saved = false;
		if(member != null){
			WriteResult wr = getCollection("members").save(member);
			saved = wr.getError() != null;
			if(member.getMemberships() != null){
				
			}
			
		}
		
		return saved;
	}
	
	public boolean remove(Member member){
		boolean removed = false;
		if(member != null && member.getKey() > 0){
			WriteResult wr = getCollection("members").remove("{key:#}", member.getKey());
			removed = wr.getError() != null;
		}
		return removed;
	}
}
