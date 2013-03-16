package org.yajug.users.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.yajug.users.api.EventController;
import org.yajug.users.api.MemberController;
import org.yajug.users.persistence.dao.EventDao;
import org.yajug.users.persistence.dao.EventMongoDao;
import org.yajug.users.persistence.dao.MemberDao;
import org.yajug.users.persistence.dao.MemberMongoDao;
import org.yajug.users.persistence.dao.MembershipDao;
import org.yajug.users.persistence.dao.MembershipMongoDao;
import org.yajug.users.service.EventService;
import org.yajug.users.service.EventServiceImpl;
import org.yajug.users.service.MemberService;
import org.yajug.users.service.MemberServiceImpl;
import org.yajug.users.service.MembershipService;
import org.yajug.users.service.MembershipServiceImpl;

import com.google.inject.Binder;
import com.google.inject.name.Names;

/**
 * Helps you to do the common injection binding jobs.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class ModuleHelper {

	/**
	 * the name of the config file to be loaded into the classpath.
	 */
	private final static String CONFIG_FILE = "config.properties";
	
	/**
	 * Bind the app properties to the module, 
	 * they'll be available to injection by key.
	 * 
	 * @param binder the module binder
	 */
	public static void bindProperties(Binder binder){
		Names.bindProperties(binder, getProperties());
	}
	
	/**
	 * Bind APIs implementation to there interfaces
	 * 
	 * @param binder the module binder
	 */
	public static void bindApis(Binder binder){
		
		//bind controllers
		binder.bind(MemberController.class);
		binder.bind(EventController.class);
        
		//biond services
		binder.bind(MembershipService.class).to(MembershipServiceImpl.class);
		binder.bind(MemberService.class).to(MemberServiceImpl.class);
		binder.bind(EventService.class).to(EventServiceImpl.class);
        
		//bind daos
		binder.bind(MemberDao.class).to(MemberMongoDao.class);
		binder.bind(MembershipDao.class).to(MembershipMongoDao.class);
		binder.bind(EventDao.class).to(EventMongoDao.class);
	}
	
	/**
	 * Load the configuration properties
	 * @return the properties instance
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
