package org.yajug.users.domain.utils;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * Validate the format of the domains' keys.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class KeyValidator {

	/**
	 * The pattern used to validate a key
	 */
	private final static Pattern OID_PATTERN = Pattern.compile("^[0-9a-f]{24}$");
	
	/**
	 * Validate the key is not null, not empty and is a 24 hexadecimal digit string.
	 * @param key the key to validate
	 * @return true if valid
	 */
	public static boolean validate(String key){
		return StringUtils.isNotBlank(key) && OID_PATTERN.matcher(key).find();
	}
}
