package org.yajug.users.servlets.auth;

import java.util.Arrays;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class GoogleOAuthHelper {

	
	private final static String[] SCOPES = {
		"https://www.googleapis.com/auth/userinfo.profile",
		"https://www.googleapis.com/auth/userinfo.email"
	};
	
	@Inject @Named("auth.clientid")  	private String clientId;
	@Inject @Named("auth.clientsecret") private String clientSecret;
	@Inject @Named("auth.redirecturi")  private String redirectUri;
	@Inject private CredentialStore credentialStore;
	@Inject private HttpTransport httpTransport;
	@Inject private JsonFactory jsonFactory;
	
	
	public AuthorizationCodeFlow getAuthorizationCodeFlow(){
		return new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, 
				jsonFactory,
				clientId, 
				clientSecret,
		        Arrays.asList(SCOPES)
	        )
			.setCredentialStore(credentialStore)
	        .build();
	}
	
	public String getRedirectUri() {
		return redirectUri;
	}
}
