package org.yajug.users.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EmfKeeper {
	
	private static final EntityManagerFactory emfInstance = 
			Persistence.createEntityManagerFactory("transactions-optional");

	private EmfKeeper() {
	}

	public static EntityManager getManager() {
		return emfInstance.createEntityManager();
	}
}
