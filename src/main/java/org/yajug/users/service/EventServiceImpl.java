package org.yajug.users.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr;
import org.yajug.users.domain.Event;
import org.yajug.users.domain.Flyer;
import org.yajug.users.persistence.dao.EventMongoDao;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * Implementation of the {@link MemberService} that use JPA to persist data.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class EventServiceImpl implements EventService {

	@Inject private EventMongoDao eventMongoDao;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Event> getAll() throws DataException {
		
		return eventMongoDao.getAll();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Event getOne(long key) throws DataException {
		
		if (key <= 0) {
			throw new DataException("Unable to retrieve an event from a wrong id");
		}
		return eventMongoDao.getOne(key);
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
		
		for (Event event : events) {
			eventMongoDao.save(event);
		} 
		return true;
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
}
