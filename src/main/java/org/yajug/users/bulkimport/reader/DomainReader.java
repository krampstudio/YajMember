package org.yajug.users.bulkimport.reader;

import java.io.IOException;
import java.util.Collection;

import org.yajug.users.domain.DomainObject;


public interface DomainReader<T extends DomainObject> {

	Collection<T> read(String fileName) throws IOException;
}
