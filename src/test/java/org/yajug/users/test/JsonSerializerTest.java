package org.yajug.users.test;

import static org.testng.Assert.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.testng.annotations.Test;
import org.yajug.users.domain.DomainObject;
import org.yajug.users.domain.Member;
import org.yajug.users.domain.Membership;
import org.yajug.users.domain.Role;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

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
	
	@Test
	public void testBSON(){
		final String datePattern = "yyyy-MM-dd";
		String bson = "{\"key\" : 1 ,\"amount\": 40 ,\"paiementDate\": { \"$date\":\"2012-03-20T00:00:00.000Z\"} , \"year\" : 2012}";
		Gson gson = 
			new GsonBuilder()
				.serializeNulls()
				//manages dates
				.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

					private SimpleDateFormat formatter;
					
					@Override
					public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
							throws JsonParseException {
						
						if(formatter == null){
							formatter = new SimpleDateFormat(datePattern);
							formatter.setTimeZone(TimeZone.getTimeZone("GMT-00:00"));
						}
						Date date = null;
						try {
							if(json.isJsonPrimitive()){	//{date : "2012-06-01"} we use common parsing 
								date = formatter.parse(json.getAsString());
							} else if(json.isJsonObject() //{date : {$date : "2012-06-01"}} we retrieve the string
								&& json.getAsJsonObject().getAsJsonPrimitive("$date") != null){
								
								String jsonDate = json.getAsJsonObject().getAsJsonPrimitive("$date").getAsString();
								date = formatter.parse(jsonDate);
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
						return date;
					}
				})
				
				//manage domains lists
				.registerTypeAdapter(
						List.class, 
						new JsonDeserializer<List<? extends DomainObject>>() {
					
					//create an other parser to avoid stack overflow with context parser
					private Gson gson =  new GsonBuilder().create();
					
					@Override
					public List<? extends DomainObject> deserialize(JsonElement json, final Type typeOfT, JsonDeserializationContext context)
							throws JsonParseException {
						
						//TODO handle the case where a generic is not defined 
						boolean domainList = false;
						Class<?> generic = (Class<?>)((ParameterizedType)typeOfT).getActualTypeArguments()[0];
						
						//check if the list is a list of DomainObjects
						if(DomainObject.class.isAssignableFrom(generic)){
							 domainList = true;
						}
						
						//if this is Domain Objects
						if(domainList){
							//we get the JSON array of ids
							List<String> ids = gson.fromJson(json, new TypeToken<ArrayList<String>>(){}.getType());
							//and transform it into a list of instance with only the key set
							return Lists.transform(ids, new Function<String, DomainObject>() {

								@Override public DomainObject apply(String input) {
									DomainObject domainObject = null;
									try {
										 domainObject = (DomainObject) Class.forName(typeOfT.getClass().getName()).newInstance() ;
										 domainObject.setKey(input);
									} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
										e.printStackTrace();
									}
									return domainObject;
								}
								
							});
						} 
						//or do a simple parsing
						return gson.fromJson(json, typeOfT);
					}
				})
				.create();
		
		Membership ms = gson.fromJson(bson, Membership.class);
		assertNotNull(ms);
		assertEquals(1, ms.getKey());
		
		SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT-00:00"));
		assertEquals("2012-03-20", formatter.format(ms.getPaiementDate()));
	}
}
