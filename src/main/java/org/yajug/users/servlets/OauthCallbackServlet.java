package org.yajug.users.servlets;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.MemoryCredentialStore;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class OauthCallbackServlet extends AbstractAuthorizationCodeCallbackServlet {

	private static final long serialVersionUID = 4089922082374477164L;

	@Override
	  protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential)
	      throws ServletException, IOException {
		
		System.out.println("Access token : " +credential.getAccessToken());
		System.out.println("Refresh token : " +credential.getRefreshToken());
		
		resp.sendRedirect("index.html");
	  }
	
	@Override
	protected void onError(
		      HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse)
		      throws ServletException, IOException {
		
		 resp.sendRedirect("index.html?autherror="+errorResponse.getErrorDescription());
		  }

	
	@Override
	protected AuthorizationCodeFlow initializeFlow() throws ServletException,
			IOException {
		return new GoogleAuthorizationCodeFlow.Builder(
					new NetHttpTransport(), 
					new JacksonFactory(),
			        "691368454221.apps.googleusercontent.com", 
			        "Vg7sUbBtvSmp7H_3eSp7yf1f",
			        Collections.singleton("https://www.googleapis.com/auth/userinfo.profile  https://www.googleapis.com/auth/userinfo.email")
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
		System.out.println("get userId from callback : " + userId);
		return userId;
	}

}
