package org.yajug.users.servlets.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GoogleOauthServlet extends AbstractAuthorizationCodeServlet {

	private static final long serialVersionUID = -5695026175093524514L;
	
	@Inject private GoogleOAuthHelper oAuthHelper;
	
	@Override
	protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
		
		return oAuthHelper.getAuthorizationCodeFlow();
	}

	@Override
	protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
		return oAuthHelper.getRedirectUri();
	}

	@Override
	protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
		return req.getSession(true).getId();
	}
}
