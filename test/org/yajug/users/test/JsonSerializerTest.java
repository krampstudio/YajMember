package org.yajug.users.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.yajug.users.domain.Member;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSerializerTest {

	@Test
	public void deserializeMember() {
		final String json = "{\"firstName\":\"bertrand\",\"lastName\":\"chevrier\",\"email\":\"chevrier.bertrand@gmail.com\",\"company\":\"logica\",\"roles\":[\"BOARD\",\"MEMBER\"]}";
		final String json2 = "{\"firstName\":\"bertrand\",\"lastName\":\"chevrier\",\"email\":\"chevrier.bertrand@gmail.com\",\"company\":\"logica\"}";
		Gson gson = new GsonBuilder().create();
		
		Member member = (Member)gson.fromJson(json, Member.class);
		assertNotNull(member);
		
		Member member2 = (Member)gson.fromJson(json2, Member.class);
		assertNotNull(member2);
	}

}
