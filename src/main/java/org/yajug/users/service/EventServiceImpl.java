package org.yajug.users.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.yajug.users.domain.Event;
import org.yajug.users.domain.Membership;
import org.yajug.users.domain.utils.KeyValidator;
import org.yajug.users.domain.utils.MappingHelper;
import org.yajug.users.persistence.dao.EventDao;
import org.yajug.users.persistence.dao.MemberDao;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Implementation of the {@link EventService} API that delegates
 * persistence calls to DAOs.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Singleton
public class EventServiceImpl implements EventService {

	/**
	 * To manage the {@link Event} related data against the store.
	 */
	@Inject private EventDao eventDao;
	
	/**
	 * To manage the {@link Membership} related data against the store.
	 */
	@Inject private MemberDao memberDao;
	
	/**
	 * Helps you to map/extract some fields
	 */
	@Inject private MappingHelper mappingHelper;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Event> getAll() throws DataException {
		Collection<Event> events = eventDao.getAll();
		return Collections2.transform(events, new Function<Event, Event>(){
			@Override public Event apply(Event input) {
				return loadParticipants(input);
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Event getOne(String key) throws DataException {
		if (!KeyValidator.validate(key)) {
			throw new ValidationException("Unable to retrieve an event from a wrong id");
		}
		return loadParticipants(eventDao.getOne(key));
	}
	
	/**
	 * The participants and registrants comes with only the key field set, 
	 * so we initialize those lists
	 * 
	 * @param event 
	 * @return the event
	 */
	private Event loadParticipants(Event event){
		if(event.getParticipants() != null && event.getParticipants().size() > 0){
			Set<String> participantKeys = mappingHelper.extractKeys(event.getParticipants());
			event.setParticipants(memberDao.getAllIn(participantKeys));
		}
		if(event.getRegistrants() != null && event.getRegistrants().size() > 0){
			Set<String> registrantKeys = mappingHelper.extractKeys(event.getRegistrants());
			event.setRegistrants(memberDao.getAllIn(registrantKeys));
		}
		return event;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean save(Event event) throws DataException{
		
		if (event == null) {
			throw new ValidationException("Cannot save a null event");
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
		
		if (events == null) {
			throw new ValidationException("Cannot save null events");
		}
		
		int expected = events.size();
		int saved = 0;
		for (Event event : events) {
			if(event != null){
				if (KeyValidator.validate(event.getKey())){
					if(eventDao.update(event)){
						saved++;
					}
				} else {
					if(eventDao.insert(event)){
						saved++;
					}
				}
			}
		}
		return saved == expected;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Event event) throws DataException {

		if (event == null || !KeyValidator.validate(event.getKey())) {
			throw new ValidationException("Trying to remove a null or non-identified event.");
		}
		
		return eventDao.remove(event);
	}
}
