package org.yajug.users.persistence.dao;

import java.util.List;

import org.yajug.users.domain.Event;

/**
 * API of the {@link Event}'s data management.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public interface EventDao {

	/**
	 * Get all the events from the store
	 * @return a list of the events
	 */
	List<Event> getAll();

	/**
	 * Get an event from its identifier
	 * @param key the event identifier
	 * @return the event instance or null if not found
	 */
	Event getOne(String key);

	/**
	 * Insert an {@link Event} to the store
	 * @param event the instance to insert, if the insertion is successful the key is set 
	 * @return true if inserted
	 */
	boolean insert(Event event);

	/**
	 * Update a {@link Event} 
	 * @param event the instance to update, the key is used to identify it
	 * @return true if updated
	 */
	boolean update(Event event);

	/**
	 * Removes a {@link Event} from the store
	 * @param event the instance to remove, the key is used to identify it
	 * @return true if removed
	 */
	boolean remove(Event event);

}