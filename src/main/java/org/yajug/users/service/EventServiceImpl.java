package org.yajug.users.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.yajug.users.domain.Event;

/**
 * Implementation of the {@link MemberService} that use JPA to persist data.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class EventServiceImpl extends JPAService implements EventService {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Event> getAll() throws DataException {
		
		List<Event> events = new ArrayList<Event>();
		EntityManager em = getEntityManager();
		try{
			TypedQuery<Event> tq = em.createNamedQuery("Event.findAll", Event.class);
			events = tq.getResultList();
		} catch(PersistenceException pe){
			throw new DataException("", pe);
		} finally {
			em.close();
		}
		return events;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean save(Event event) throws DataException{
		
		if(event == null){
			throw new DataException("Cannot save a null event");
		}
		
		EntityManager em = getEntityManager();
		try{
			em.getTransaction().begin();
			em.persist(event);
			em.getTransaction().commit();
		} catch(PersistenceException pe){
			throw new DataException("", pe);
		} finally{
			em.close();
		}
		return true;
	}
}
