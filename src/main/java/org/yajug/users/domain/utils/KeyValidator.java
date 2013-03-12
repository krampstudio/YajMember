package org.yajug.users.domain.utils;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class KeyValidator {

	private final static Pattern OID_PATTERN = Pattern.compile("^[0-9a-f]24$");
	
	public static boolean validate(String key){
		return StringUtils.isNotBlank(key) && OID_PATTERN.matcher(key).find();
	}
}
