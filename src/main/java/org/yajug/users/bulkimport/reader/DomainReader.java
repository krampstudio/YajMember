package org.yajug.users.bulkimport.reader;

import java.io.IOException;
import java.util.Collection;

import org.yajug.users.domain.DomainObject;

/**
 * Reads domain objects
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 *
 * @param <T> the type (subclass) of  {@link DomainObject}
 */
public interface DomainReader<T extends DomainObject> {

	/**
	 * Reads domain objects from a file
	 * @param fileName the name of the file to read from
	 * @return the collection of domainObjects read
	 * @throws IOException
	 */
	Collection<T> read(String fileName) throws IOException;
}
