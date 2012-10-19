package org.yajug.users.service;

import java.util.List;

import org.yajug.users.domain.Event;

/**
 * The service provides common management of {@link Event}s
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public interface EventService {

	/**
	 * Get all the events
	 * @return a list of events
	 * @throws DataException
	 */
	List<Event> getAll() throws DataException;
	
	/**
	 * Save an event
	 * @param event the event to save
	 * @return true if saved
	 * @throws DataException
	 */
	boolean save(Event event) throws DataException;
}
