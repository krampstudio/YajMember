package org.yajug.users.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.testng.annotations.Test;
import org.yajug.users.domain.Member;
import org.yajug.users.domain.Membership;
import org.yajug.users.domain.Role;
import org.yajug.users.json.DateSerializer;
import org.yajug.users.json.Serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@Test(groups={"unit"})
public class JsonSerializerTest {

	/**
	 * Test simple deserialisation on a member
	 */
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

	/**
	 * Test serialization of a jsonObject
	 */
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
	
	/**
	 * Test the {@link Serializer} 
	 */
	@Test
	public void testBSON(){
		
		Serializer serializer = new Serializer();
		serializer.setDateSerializer(new DateSerializer());
		Gson gson = serializer.get();
		assertNotNull(gson);
		
		final String datePattern = "yyyy-MM-dd";
		String bson = "{\"key\" : \"1\" ,\"amount\": 40 ,\"paiementDate\": { \"$date\":\"2012-03-20T00:00:00.000Z\"} , \"year\" : 2012}";
		
		
		Membership ms = gson.fromJson(bson, Membership.class);
		assertNotNull(ms);
		assertEquals(ms.getKey(), "1");
		
		SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT-00:00"));
		assertEquals(formatter.format(ms.getPaiementDate()), "2012-03-20");
	}
}
