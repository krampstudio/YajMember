package org.yajug.users.servlets.auth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LogoutHelper {

	@Inject private CredentialStore credentialStore;
	
	public void logout(HttpServletRequest req){
		HttpSession session = req.getSession(false);
		if(session != null){
			try {
				credentialStore.delete(session.getId(), null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			session.invalidate();
		}
	}
}
