package org.yajug.users.service;

import java.util.ArrayList;
import java.util.Collection;
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
	public Collection<Event> getAll() throws DataException {
		
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
	public Event getOne(long key) throws DataException {
		
		if(key <= 0){
			throw new DataException("Unable to retrieve an event from a wrong id");
		}
		
		Event event = null;
		EntityManager em = getEntityManager();
		try{
			TypedQuery<Event> tq = em.createNamedQuery("Event.getOne", Event.class);
			tq.setParameter("key", key);
			event = tq.getSingleResult();
			
		} catch(PersistenceException pe){
			throw new DataException("An error occured whil retrieving an event", pe);
		} finally {
			em.close();
		}
		return event;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean save(Event event) throws DataException{
		
		if(event == null){
			throw new DataException("Cannot save a null event");
		}
		
		List<Event> events = new ArrayList<Event>();
		events.add(event);
		
		return save(events);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean save(Collection<Event> events) throws DataException {
		
		if(events == null){
			throw new DataException("Cannot save null events");
		}
		
		EntityManager em = getEntityManager();
		try{
			em.getTransaction().begin();
			for(Event event : events){
				
				Event previousEvent = em.find(Event.class, event.getKey());
				if(previousEvent != null){
					
					previousEvent.setTitle(event.getTitle());
					previousEvent.setDate(event.getDate());
					previousEvent.setDescription(event.getDescription());
					
					em.merge(previousEvent);
				} else {
					em.persist(event);
				}
			}
			em.getTransaction().commit();
		} catch(PersistenceException pe){
			pe.printStackTrace();
		} finally{
			em.close();
		}
		return true;
	}
}
