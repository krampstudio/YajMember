package org.yajug.users.servlets;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yajug.users.config.ModuleHelper;
import org.yajug.users.servlets.auth.AuthenticationFilter;
import org.yajug.users.servlets.auth.GoogleOauthCallbackServlet;
import org.yajug.users.servlets.auth.GoogleOauthServlet;
import org.yajug.users.servlets.auth.LoggedCredentialStore;
import org.yajug.users.servlets.auth.LogoutServlet;

import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * Configure Guice injector to work in the servlet context
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class GuiceConfig extends GuiceServletContextListener {
	
	private final static Logger logger = LoggerFactory.getLogger(GuiceConfig.class);
	
	@Override
	protected Injector getInjector() {
		
		final Properties properties = ModuleHelper.getProperties();
		final boolean authEnabled = Boolean.parseBoolean(properties.getProperty("auth.enabled", "true"));
		
		/*
		 * Guice module for the REST API 
		 */
		return Guice.createInjector(new JerseyServletModule() {
			
			@Override
	         protected void configureServlets() {
				
				ModuleHelper.bindProperties(binder());
				ModuleHelper.bindApis(binder());
	            
	            if(authEnabled){
	            	
	            	logger.debug("authentication enabled");
	            
		            bind(CredentialStore.class).to(LoggedCredentialStore.class);
					bind(HttpTransport.class).to(NetHttpTransport.class);
					bind(JsonFactory.class).to(GsonFactory.class);
		            
		            filter("/", "*.html", "/api/*").through(AuthenticationFilter.class);
		            
					serve("/auth").with(GoogleOauthServlet.class);
					serve("/authCallback").with(GoogleOauthCallbackServlet.class);
					serve("/logout").with(LogoutServlet.class);
	            } else {
	            	 logger.info("authentication disabled (update the config file to enable)");
	            }
				serve("/api/*").with(GuiceContainer.class);
	         }
		});
	}
}
