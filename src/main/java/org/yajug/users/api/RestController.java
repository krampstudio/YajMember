package org.yajug.users.api;

import java.security.InvalidParameterException;

import org.apache.commons.lang3.StringUtils;
import org.yajug.users.json.Serializer;

import com.google.gson.JsonObject;
import com.google.inject.Inject;

/**
 * Base Controller that provides high level service
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public abstract class RestController {

	@Inject protected Serializer serializer; 
	
	/**
	 * Creates a JSON representation for the errors/exeptions
	 * @param cause the cause exception
	 * @return the JSON
	 */
	protected String serializeException(Throwable cause){
		
		JsonObject response = new JsonObject();
		
		response.addProperty("exception", cause.getClass().getName());
		response.addProperty("message", cause.getMessage());
		
		return serializer.get().toJson(response);
	}
	
	/**
	 * Serialize for a JSONp response
	 * @param data the data to serialize 
	 * @param callback the JSONp callback
	 * @return the JSONp respresentation
	 */
	protected String serializeJsonp(Object data, String callback){
		if(StringUtils.isBlank(callback)){
			return serializeException(new InvalidParameterException("Empty JSON-P callback"));
		}
		return callback + "(" + serializer.get().toJson(data) + ")";
	}
}
