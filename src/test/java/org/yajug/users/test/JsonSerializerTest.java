package org.yajug.users.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.yajug.users.domain.Member;
import org.yajug.users.domain.Role;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class JsonSerializerTest {

	@Test
	public void deserializeMember() {
		final String json = "{\"firstName\":\"bertrand\",\"lastName\":\"chevrier\",\"email\":\"chevrier.bertrand@gmail.com\",\"company\":\"logica\",\"roles\":[\"BOARD\",\"MEMBER\"]}";
		final String json2 = "{\"firstName\":\"bertrand\",\"lastName\":\"chevrier\",\"email\":\"chevrier.bertrand@gmail.com\",\"company\":\"logica\"}";
		Gson gson = new GsonBuilder().create();
		
		Member member = (Member)gson.fromJson(json, Member.class);
		assertNotNull(member);
		assertEquals("bertrand", member.getFirstName());
		assertNotNull(member.getRoles());
		assertEquals(2, member.getRoles().size());
		assertTrue(member.getRoles().contains(Role.BOARD));
		
		Member member2 = (Member)gson.fromJson(json2, Member.class);
		assertNotNull(member2);
		assertEquals("bertrand", member2.getFirstName());
	}

	@Test
	public void serializeResponse(){
		final String json = "{\"saved\":true}";
		Gson gson = new GsonBuilder().create();
		
		JsonObject jsonElement = new JsonObject();
		jsonElement.addProperty("saved", true);
		
		String serialized = gson.toJson(jsonElement);
		assertNotNull(serialized);
		assertEquals(json, serialized);
	}
}
