package org.yajug.users.servlets;

import org.yajug.users.api.EventResource;
import org.yajug.users.api.UserResource;
import org.yajug.users.service.EventService;
import org.yajug.users.service.EventServiceImpl;
import org.yajug.users.service.MemberService;
import org.yajug.users.service.MemberServiceImpl;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class GuiceConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new JerseyServletModule() {
			
			@Override
	         protected void configureServlets() {
	            bind(UserResource.class);
	            bind(EventResource.class);
	            
	            bind(MemberService.class).to(MemberServiceImpl.class);
	            bind(EventService.class).to(EventServiceImpl.class);
	            
	            // Route all requests through GuiceContainer
	            serve("/api/*").with(GuiceContainer.class);
	         }
		});
	}

}
