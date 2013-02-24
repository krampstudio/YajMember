package org.yajug.users.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr;
import org.yajug.users.domain.Event;
import org.yajug.users.domain.Flyer;
import org.yajug.users.domain.utils.MappingHelper;
import org.yajug.users.persistence.dao.EventMongoDao;
import org.yajug.users.persistence.dao.MemberMongoDao;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * Implementation of the {@link MemberService} that use JPA to persist data.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class EventServiceImpl implements EventService {

	@Inject private EventMongoDao eventMongoDao;
	@Inject private MemberMongoDao memberMongoDao;
	@Inject private MappingHelper mappingHelper;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Event> getAll() throws DataException {
		Collection<Event> events = eventMongoDao.getAll();
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
	public Event getOne(long key) throws DataException {
		
		if (key <= 0) {
			throw new DataException("Unable to retrieve an event from a wrong id");
		}
		return loadParticipants(eventMongoDao.getOne(key));
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
			Set<Long> participantKeys = mappingHelper.extractKeys(event.getParticipants());
			event.setParticipants(memberMongoDao.getAllIn(participantKeys));
		}
		if(event.getRegistrants() != null && event.getRegistrants().size() > 0){
			Set<Long> registrantKeys = mappingHelper.extractKeys(event.getRegistrants());
			event.setRegistrants(memberMongoDao.getAllIn(registrantKeys));
		}
		return event;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean save(Event event) throws DataException{
		
		if (event == null) {
			throw new DataException("Cannot save a null event");
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
			throw new DataException("Cannot save null events");
		}
		
		int expected = events.size();
		int saved = 0;
		for (Event event : events) {
			if(eventMongoDao.isNew(event)){
				if(eventMongoDao.insert(event)){
					saved++;
				}
			} else {
				if(eventMongoDao.update(event)){
					saved++;
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

		if (event == null || event.getKey() <= 0) {
			throw new DataException("Trying to remove a null or non-identified event.");
		}
		
		return eventMongoDao.remove(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean saveFlyer(InputStream input, String format, Flyer flyer) throws DataException {
		boolean saved = false;

		//validate input format
		final List<String> allowedFormat = Lists.newArrayList("png", "jpg", "jpeg", "gif");
		
		if(StringUtils.isBlank(format) || !allowedFormat.contains(format.toLowerCase())){
			throw new ValidationException("Unsupported flyer format :" + format );
		}
		
		try {
			//save base flyer to format
			BufferedImage img = ImageIO.read(input);
			saved = ImageIO.write(img, Flyer.TYPE, flyer.getFile());
			
			//and creates the thumbnail
			BufferedImage thumbnail = Scalr.resize(
					img, 
					Scalr.Method.SPEED, 
					Scalr.Mode.FIT_TO_WIDTH, 
					181, 
					256, 
					Scalr.OP_ANTIALIAS
				);
			saved = saved && ImageIO.write(thumbnail, Flyer.TYPE, flyer.getThumbnail().getFile());
			
		} catch (IOException e) {
			throw new DataException("An error occured while saving the flyer", e);
		}
		return saved;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeFlyer(Flyer flyer) throws DataException {
		
		boolean removed = false;
		
		if (flyer == null || flyer.getFile() == null) {
			throw new DataException("Trying to remove a null flyer.");
		}
		
		try{
			removed = flyer.getFile().delete();
			
			//removes also the thumbnail
			if(flyer.getThumbnail() != null){
				removed = removed && flyer.getThumbnail().getFile().delete();
			}
		} catch(SecurityException se){
			throw new DataException("It seems there is a persmission issue while removing the flyer", se);
		}
		return removed;
	}
	
	
}
