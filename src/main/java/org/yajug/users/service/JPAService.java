package org.yajug.users.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Provides an {@link EntityManager} to it's subclasses via 
 * {Service{@link #getEntityManager()}. 
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public abstract class JPAService {

	/** The name of the unit defined in persistence.xml */
	private static final String PERSISTENCE_UNIT = "transactions-optional";
	
	/** The factory */
	private static EntityManagerFactory emfInstance;
	
	/** 
	 * And the manager instance 
	 * TODO check if the {@link EntityManager} is thread-safe an can be shared.
	 */
	private EntityManager em;
	
	static{
		//get the factory from the persistence unit, done before any entity is called.
		emfInstance = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
	}
	
	/**
	 * The {@link EntityManager} is created at instantiation
	 */
	public JPAService(){
		em = emfInstance.createEntityManager();
	}
	
	/**
	 * Used to get the {@link EntityManager}
	 * @return the manager
	 */
	protected EntityManager getEntityManager(){
		return em;
	}
}
