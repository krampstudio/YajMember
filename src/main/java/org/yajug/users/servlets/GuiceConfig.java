package org.yajug.users.servlets;

import org.yajug.users.api.EventController;
import org.yajug.users.api.MemberController;
import org.yajug.users.service.EventService;
import org.yajug.users.service.EventServiceImpl;
import org.yajug.users.service.MemberService;
import org.yajug.users.service.MemberServiceImpl;
import org.yajug.users.servlets.auth.GoogleOauthCallbackServlet;
import org.yajug.users.servlets.auth.GoogleOauthServlet;
import org.yajug.users.servlets.auth.LoggedCredentialStore;

import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * Configure Guice injector to work in the servlet context
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class GuiceConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		
		Module restModule = new JerseyServletModule() {
			
			@Override
	         protected void configureServlets() {
	            bind(MemberController.class);
	            bind(EventController.class);
	            
	            bind(MemberService.class).to(MemberServiceImpl.class);
	            bind(EventService.class).to(EventServiceImpl.class);
	            
	            // Route all requests through GuiceContainer
	            serve("/api/*").with(GuiceContainer.class);
	         }
		};
		
		Module authModule = new ServletModule() {

			@Override
			protected void configureServlets() {
				bind(CredentialStore.class).to(LoggedCredentialStore.class);
				serve("/auth").with(GoogleOauthServlet.class);
				serve("/authCallback").with(GoogleOauthCallbackServlet.class);
			}
			
		};
		
		//Enables guice injector in Jersey's controllers
		return Guice.createInjector(restModule, authModule);
	}
	
	

}
