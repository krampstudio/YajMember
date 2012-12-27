package org.yajug.users.servlets.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yajug.users.domain.User;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * 
 * @author bertrand
 *
 */
@Singleton
public class GoogleOauthCallbackServlet extends AbstractAuthorizationCodeCallbackServlet {

	private static final long serialVersionUID = 4089922082374477164L;
	
	/**
	 * all the actions are delegated to the {@link GoogleOAuthHelper}
	 */
	@Inject private GoogleOAuthHelper oAuthHelper;

	@Override
	protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential)
			  throws ServletException, IOException {
		User user = oAuthHelper.getUser(credential);
		if(oAuthHelper.isAllowed(user)){
			req.getSession().setAttribute("activeUser", user);
			resp.sendRedirect("index.html?auth=true");
			return;
		} 
		resp.sendRedirect("index.html?autherror=user not allowed");
	}
	
	@Override
	protected void onError(HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse)
			throws ServletException, IOException {
		 resp.sendRedirect("index.html?autherror="+errorResponse.getErrorDescription());
	}
	
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
		String userId = req.getSession(true).getId();
		System.out.println("user id : " + userId);
		return userId;
	}

}
