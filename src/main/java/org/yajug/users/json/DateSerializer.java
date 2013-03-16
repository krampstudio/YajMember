package org.yajug.users.json;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yajug.users.api.MemberController;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.inject.Singleton;

/**
 * (De)Serialize dates to/from JSON.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Singleton
public class DateSerializer implements JsonSerializer<Date>, JsonDeserializer<Date> {
	
	private final static Logger logger = LoggerFactory.getLogger(MemberController.class);
	private final static String DATE_PATTERN = "yyyy-MM-dd";
	private final static String TIME_ZONE = "GMT-00:00";
	
	private SimpleDateFormat formatter;
	
	/**
	 * Initialize the serializer with our pattern
	 */
	public DateSerializer() {
		formatter = new SimpleDateFormat(DATE_PATTERN);
		formatter.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
	}
	
	/**
	 * we need to synchronize because the date formatter is not thread safe 
	 */
	@Override
	public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
		synchronized (formatter) {
			String date = formatter.format(src);
		    return new JsonPrimitive(date);
		}
	}
	
	/**
	 * we need to synchronize because the date formatter is not thread safe 
	 */
	@Override
	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		synchronized (formatter) {
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
				logger.error("Error while deserializing json date :" + json.toString(), e);
			}
			return date;
		}
	}
	
	public SimpleDateFormat getFormatter() {
		return formatter;
	}
}