package org.yajug.users.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class Service {

	private static final String PERSISTENCE_UNIT = "transactions-optional";
	
	private static EntityManagerFactory emfInstance;
	
	private static EntityManagerFactory getEntityManagerFactory() {
		if (emfInstance == null) {
			emfInstance = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		}
		return emfInstance;
	}
	
	protected static EntityManager getEntityManager(){
		return getEntityManagerFactory().createEntityManager();
	}
}
