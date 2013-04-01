package org.yajug.users.persistence.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.yajug.users.domain.Member;
import org.yajug.users.domain.utils.MappingHelper;
import org.yajug.users.persistence.MongoDao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * Enables you to access {@link Member} data stored onto a Mongo database.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Singleton
public class MemberMongoDao extends MongoDao<Member> implements MemberDao{
	
	/**
	 * The name of the collection in mongo
	 */
	private final static String COLLECTION_NAME = "members";
	
	/**
	 * This instance provides some mapping facilities
	 */
	@Inject private MappingHelper mappingHelper;
	
	/**
	 * Get the members collection (ie. db.members in mongo)
	 * @return the collection
	 */
	private DBCollection members(){
		return getCollection(COLLECTION_NAME);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Member> getAll(){
		List<Member> members = new ArrayList<>();
		
		DBCursor cursor = members().find();
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Member> getAllIn(Set<String> keys){
		List<Member> members = new ArrayList<>();
		if(keys != null && keys.size() > 0){
			
			BasicDBList inKeys = new BasicDBList();
			for(String key : keys){
				inKeys.add(new ObjectId(key));
			}
			DBObject query = new BasicDBObject("_id", new BasicDBObject("$in", inKeys));
			
			DBCursor cursor = members().find(query);
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
		}
		return members;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Member getOne(String key){
		
		assert StringUtils.isNotBlank(key);
		
		Member member = null;
		DBCursor cursor = members().find(new BasicDBObject("_id", new ObjectId(key))).limit(1);
		try {
            while(cursor.hasNext()) {
            	member = map(Member.class, (BasicDBObject)cursor.next());
            }
        } finally {
            cursor.close();
        }
		return member;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Member> search(String expression){
		List<Member> members = new ArrayList<>();
		
		BasicDBObject searchExpression = new BasicDBObject("$regex", Pattern.quote(expression)).append("$options", "-i");
		BasicDBList search = new BasicDBList();
		search.add(new BasicDBObject("firstName", searchExpression));
		search.add(new BasicDBObject("lastName", searchExpression));
		search.add(new BasicDBObject("email", searchExpression));
		search.add(new BasicDBObject("company", searchExpression));
		
		//create a mongo search query document
		BasicDBObject query = new BasicDBObject("$or", search);
		DBCursor cursor = members().find(query);
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getCompanies(String expression){
		List<String> companies = new ArrayList<>();
		
		BasicDBObject searchExpression = new BasicDBObject("$regex", expression).append("$options", "-i");
		DBCursor cursor = members().find(new BasicDBObject("company", searchExpression));
		try {
            while(cursor.hasNext()) {
            	String company = ((BasicDBObject)cursor.next()).getString("company");
                if(StringUtils.isNotBlank(company)){
                	companies.add(company);
                }
            }
        } finally {
            cursor.close();
        }
		return companies;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean insert(Member member){
		
		assert member != null;
		
		boolean inserted = false;
		
		BasicDBObject doc =  new BasicDBObject("firstName", member.getFirstName())
                .append("lastName",  member.getLastName())
                .append("company",  member.getCompany())
                .append("email", member.getEmail());
		if(member.getRoles() != null){
			doc.append("roles", mappingHelper.enumsToStrings(member.getRoles()));
		}
		inserted = handleWriteResult(members().insert(doc));
		if(doc.get( "_id") != null){
			member._setId((ObjectId)doc.get( "_id"));
		}
		return inserted;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean update(Member member){
		
		assert member != null;
		assert StringUtils.isNotBlank(member.getKey());
		
		BasicDBObject query =  new BasicDBObject("_id", new ObjectId(member.getKey()));
		BasicDBObject doc = 
				new BasicDBObject("firstName", member.getFirstName())
	                    	.append("lastName", member.getLastName())
	                    	.append("company", member.getCompany())
	                    	.append("email", member.getEmail());
		if(member.getRoles() != null){
			doc.append("roles", mappingHelper.enumsToStrings(member.getRoles()));
		}
		return handleWriteResult(
				members().update(query, new BasicDBObject("$set", doc))
			);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Member member){
		
		assert member != null;
		assert StringUtils.isNotBlank(member.getKey());
		
		return handleWriteResult(
					members().remove(new BasicDBObject("_id", new ObjectId(member.getKey())))
				);
	}
}
