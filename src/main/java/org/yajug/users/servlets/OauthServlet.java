package org.yajug.users.servlets;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.MemoryCredentialStore;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class OauthServlet extends AbstractAuthorizationCodeServlet {

	private static final long serialVersionUID = -5695026175093524514L;

	@Override
	protected AuthorizationCodeFlow initializeFlow() throws ServletException,
			IOException {
		
		return new GoogleAuthorizationCodeFlow.Builder(
				new NetHttpTransport(), 
				new JacksonFactory(),
		        "691368454221.apps.googleusercontent.com", 
		        "Vg7sUbBtvSmp7H_3eSp7yf1f",
		        Collections.singleton("https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email")
	        )
			.setCredentialStore(new CustomStore())
	        .build();

	}

	@Override
	protected String getRedirectUri(HttpServletRequest req)
			throws ServletException, IOException {
		return "http://dev.yajmember.fr:8000/YajMember/oauth2callback";
	}

	@Override
	protected String getUserId(HttpServletRequest req) throws ServletException,
			IOException {
		String userId =  req.getSession(true).getId();
		System.out.println("get userId : " + userId);
		return userId;
	}

}
