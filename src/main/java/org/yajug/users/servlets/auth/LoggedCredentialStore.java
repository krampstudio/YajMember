package org.yajug.users.servlets.auth;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.auth.oauth2.MemoryCredentialStore;
import com.google.inject.Singleton;

@Singleton
public class LoggedCredentialStore implements CredentialStore{
	
	private final static Logger logger = LoggerFactory.getLogger(LoggedCredentialStore.class.getName());
	
	private static MemoryCredentialStore delegate = new MemoryCredentialStore();

	@Override
	public boolean load(String userId, Credential credential) throws IOException {
		logger.info("Load : " + userId + " - " + credential.toString());
		return delegate.load(userId, credential);
	}

	@Override
	public void store(String userId, Credential credential) throws IOException {
		logger.info("Store : " + userId + " - " + credential.toString());
		delegate.store(userId, credential);
	}

	@Override
	public void delete(String userId, Credential credential) throws IOException {
		logger.info("Delete : " + userId + " - " + credential);
		delegate.delete(userId, credential);
	}
}
