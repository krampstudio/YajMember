package org.yajug.users.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.inject.Binder;
import com.google.inject.name.Names;

public class ModuleHelper {

	private final static String CONFIG_FILE = "config.properties";
	
	public static void bindProperties(Binder binder){
		Names.bindProperties(binder, getProperties());
	}
	
	/**
	 * Load the configuration properties
	 * @return the properties
	 */
	public static Properties getProperties(){
		Properties properties = new Properties();
		
		try(InputStream input = ModuleHelper.class.getResourceAsStream("/" + CONFIG_FILE)){
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return properties;
	}
}
