package org.yajug.users.persistence.dao;

import java.util.List;

import org.yajug.users.domain.Member;
import org.yajug.users.persistence.MongoDao;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;

@Singleton
public class MemberMongoDao extends MongoDao{

	public List<Member> getAll(){
		return Lists.newArrayList(
				getCollection("members").find().as(Member.class)
			);
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
}
