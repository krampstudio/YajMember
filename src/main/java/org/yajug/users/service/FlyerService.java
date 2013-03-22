package org.yajug.users.service;

import java.io.InputStream;

import org.yajug.users.domain.Flyer;

/**
 * The service provides common management of {@link Flyer}s
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public interface FlyerService {

	/**
	 * Persist data of an event's flyer
	 * @param input the flyer's data
	 * @param format the format of the input data (jpg, png, etc.)
	 * @param flyer the flyer meta data object
	 * @return true if the flyer is saved
	 * @throws DataException
	 */
	boolean save(InputStream input, String format, Flyer flyer)  throws DataException;

	/**
	 * Remove an existing flyer
	 * @param flyer the flyer to remove
	 * @return
	 * @throws DataException
	 */
	boolean remove(Flyer flyer)  throws DataException;
	
}
