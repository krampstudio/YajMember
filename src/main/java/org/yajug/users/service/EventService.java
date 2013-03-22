package org.yajug.users.service;

import java.util.Collection;

import org.yajug.users.domain.Event;

/**
 * The service provides common management of {@link Event}s
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public interface EventService {

	/**
	 * Get all the {@link Event}s
	 * @return a list of events
	 * @throws DataException
	 */
	Collection<Event> getAll() throws DataException;
	
	/**
	 * get one {@link Event} by key
	 * @param key
	 * @return the {@link Event} or null
	 * @throws DataException
	 */
	Event getOne(String key) throws DataException;
	
	/**
	 * Save an {@link Event}
	 * @param event the {@link Event} to save
	 * @return true if saved
	 * @throws DataException
	 */
	boolean save(Event event) throws DataException;
	
	/**
	 * Save {@link Event}s
	 * @param events the {@link Event}s to save
	 * @return true if saved
	 * @throws DataException
	 */
	boolean save(Collection<Event> events) throws DataException;
	
	/**
	 * Remove an {@link Event}
	 * @param event the {@link Event} to remove
	 * @return true if the removal is done
	 * @throws DataException
	 */
	boolean remove(Event event) throws DataException;
	
}
