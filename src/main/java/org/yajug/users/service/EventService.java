package org.yajug.users.service;

import java.io.InputStream;
import java.util.Collection;

import org.yajug.users.domain.Event;
import org.yajug.users.domain.Flyer;

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
	Collection<Event> getAll() throws DataException;
	
	/**
	 * get one event by key
	 * @param key
	 * @return teh event
	 * @throws DataException
	 */
	Event getOne(long key) throws DataException;
	
	/**
	 * Save an event
	 * @param event the event to save
	 * @return true if saved
	 * @throws DataException
	 */
	boolean save(Event event) throws DataException;
	
	/**
	 * Save events
	 * @param events
	 * @return true if saved
	 * @throws DataException
	 */
	boolean save(Collection<Event> events) throws DataException;
	
	/**
	 * Persist data of an event's flyer
	 * @param input the flyer's data
	 * @param format the format of the input data (jpg, png, etc.)
	 * @param flyer the flyer meta data object
	 * @return true if the flyer is saved
	 * @throws DataException
	 */
	boolean saveFlyer(InputStream input, String format, Flyer flyer)  throws DataException;
}
