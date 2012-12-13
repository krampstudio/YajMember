package org.yajug.users.servlets.auth;

import java.io.IOException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.auth.oauth2.MemoryCredentialStore;

public class CustomStore implements CredentialStore{
	
	private static MemoryCredentialStore delegate = new MemoryCredentialStore();

	@Override
	public boolean load(String userId, Credential credential)
			throws IOException {
		System.out.println("Load : " + userId + " - " + credential.toString());
		return delegate.load(userId, credential);
	}

	@Override
	public void store(String userId, Credential credential) throws IOException {
		System.out.println("Store : " + userId + " - " + credential.toString());
		 delegate.store(userId, credential);
		
	}

	@Override
	public void delete(String userId, Credential credential) throws IOException {
		System.out.println("Delete : " + userId + " - " + credential.toString());
		 delegate.delete(userId, credential);
	}

}
