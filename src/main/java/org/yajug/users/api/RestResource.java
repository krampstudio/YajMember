package org.yajug.users.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public abstract class RestResource {

	private Gson gson;
	
	protected Gson getSerializer(){
		if(gson == null){
			gson = new GsonBuilder()
						.serializeNulls()
						.create();
		}
		return gson;
	}
	
	protected String serializeException(Throwable cause){
		
		JsonObject response = new JsonObject();
		
		response.addProperty("exception", cause.getClass().getName());
		response.addProperty("message", cause.getMessage());
		
		return getSerializer().toJson(response);
	}
}
