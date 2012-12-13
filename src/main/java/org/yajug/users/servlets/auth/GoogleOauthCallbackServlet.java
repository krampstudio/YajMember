package org.yajug.users.servlets.auth;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GoogleOauthCallbackServlet extends AbstractAuthorizationCodeCallbackServlet {

	private static final long serialVersionUID = 4089922082374477164L;
	
	private final HttpTransport httpTransport = new NetHttpTransport();

	@Override
	  protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential)
	      throws ServletException, IOException {
		
		System.out.println("Access token : " +credential.getAccessToken());
		
		HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
		GenericUrl url = new GenericUrl("https://www.googleapis.com/oauth2/v1/userinfo");
		url.set("access_token", credential.getAccessToken());
		
		HttpRequest request = requestFactory.buildGetRequest(url);
		HttpResponse response = request.execute();
		if(response.getStatusCode() == 200){
			String json = response.parseAsString();
			if(StringUtils.isNotBlank(json)){
				System.out.println(json);
				Map<String, String> userInos =  new Gson().fromJson(json, new TypeToken<Map<String,String>>() {}.getType());
			
				System.out.println(userInos.toString());
			}
		}
		
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
					httpTransport, 
					new GsonFactory(),
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
