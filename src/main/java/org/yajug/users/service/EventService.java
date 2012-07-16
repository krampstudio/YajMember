package org.yajug.users.service;

import java.util.List;

import org.yajug.users.domain.Event;

public interface EventService {

	List<Event> getAll() throws DataException;
}
