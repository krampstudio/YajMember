package org.yajug.users.servlets.auth;

import java.util.Arrays;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GoogleOAuthHelper {

	private final static String CLIENT_ID = "***";
	private final static String CLIENT_SECRET = "***";
	private final static String[] SCOPES = {
		"https://www.googleapis.com/auth/userinfo.profile",
		"https://www.googleapis.com/auth/userinfo.email"
	};
	private final String  redirectUri = "http://dev.yajmember.fr:8000/YajMember/authCallback";
	
	private HttpTransport httpTransport = new NetHttpTransport();
	private JsonFactory jsonFactory = new GsonFactory();
	
	@Inject
	private CredentialStore credentialStore;
	
	public AuthorizationCodeFlow getAuthorizationCodeFlow(){
		return new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, 
				jsonFactory,
				CLIENT_ID, 
				CLIENT_SECRET,
		        Arrays.asList(SCOPES)
	        )
			.setCredentialStore(credentialStore)
	        .build();
	}
	
	public String getRedirectUri() {
		return redirectUri;
	}
}
