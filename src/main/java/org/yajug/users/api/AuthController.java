package org.yajug.users.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.MemoryCredentialStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.services.CommonGoogleClientRequestInitializer;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.JsonObject;

@Path("auth")
public class AuthController extends RestController {

	private static final String API_KEY = "";
	
	@Context
	private  HttpServletRequest request;
	
	/** Global instance of the HTTP transport. */
	  private  HttpTransport transport = new NetHttpTransport();

	  /** Global instance of the JSON factory. */
	  private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	
	@GET
	@Path("auth")
	@Produces({MediaType.APPLICATION_JSON})
	public String auth() throws IOException{
		
		JsonObject response = new JsonObject();
		boolean authenticate = false;
		
		MemoryCredentialStore store = new MemoryCredentialStore();
		
		Credential credential =
		        new Credential(BearerToken.authorizationHeaderAccessMethod()).setAccessToken("");
		    HttpRequestFactory requestFactory = transport.createRequestFactory(credential);
		  requestFactory.buildGetRequest(new GenericUrl("")).

		
		response.addProperty("authenticated", authenticate);
		return getSerializer().toJson(response);
	}
}
