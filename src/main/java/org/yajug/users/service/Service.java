package org.yajug.users.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class Service {

	private static final String PERSISTENCE_UNIT = "transactions-optional";
	
	private static EntityManagerFactory emfInstance;
	private EntityManager em;
	
	static{
		emfInstance = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
	}
	
	public Service(){
		em = emfInstance.createEntityManager();
	}
	
	protected EntityManager getEntityManager(){
		return em;
	}
}
