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

import com.google.inject.Binder;
import com.google.inject.name.Names;

public class ModuleHelper {

	private final static String CONFIG_FILE = "config.properties";
	
	public static void bindProperties(Binder binder){
		Names.bindProperties(binder, getProperties());
	}
	
	public static void bindApis(Binder binder){
		binder.bind(MemberController.class);
		binder.bind(EventController.class);
        
		binder.bind(MemberService.class).to(MemberServiceImpl.class);
		binder.bind(EventService.class).to(EventServiceImpl.class);
        
		binder.bind(MemberDao.class).to(MemberMongoDao.class);
		binder.bind(MembershipDao.class).to(MembershipMongoDao.class);
		binder.bind(EventDao.class).to(EventMongoDao.class);
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
