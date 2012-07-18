package org.yajug.users.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.yajug.users.domain.Event;

public class EventServiceImpl extends Service implements EventService {

	@Override
	public List<Event> getAll() throws DataException {
		
		EntityManager em = getEntityManager();
		TypedQuery<Event> tq = em.createNamedQuery("Event.findAll", Event.class);
		
		return tq.getResultList();
	}

}
