package org.yajug.users.api;

import java.security.InvalidParameterException;

import org.apache.commons.lang.StringUtils;
import org.yajug.users.vo.GridVo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public abstract class RestController {

	private Gson gson;
	
	protected Gson getSerializer(){
		if(gson == null){
			gson = new GsonBuilder()
						.serializeNulls()
						.setDateFormat("yyyy-MM-dd")
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
	
	protected String serializeJsonp(Object data, String callback){
		if(StringUtils.isBlank(callback)){
			return serializeException(new InvalidParameterException("Empty JSON-P callback"));
		}
		return callback + "(" + getSerializer().toJson(data) + ")";
	}
}
