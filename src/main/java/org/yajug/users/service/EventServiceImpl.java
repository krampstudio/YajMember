package org.yajug.users.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.imgscalr.Scalr;
import org.yajug.users.domain.Event;
import org.yajug.users.domain.Flyer;

/**
 * Implementation of the {@link MemberService} that use JPA to persist data.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class EventServiceImpl extends JPAService implements EventService {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Event> getAll() throws DataException {
		
		List<Event> events = new ArrayList<Event>();
		EntityManager em = getEntityManager();
		try {
			TypedQuery<Event> tq = em.createNamedQuery("Event.findAll", Event.class);
			events = tq.getResultList();
		} catch(PersistenceException pe) {
			throw new DataException("", pe);
		} finally {
			em.close();
		}
		return events;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Event getOne(long key) throws DataException {
		
		if (key <= 0) {
			throw new DataException("Unable to retrieve an event from a wrong id");
		}
		
		Event event = null;
		EntityManager em = getEntityManager();
		try {
			TypedQuery<Event> tq = em.createNamedQuery("Event.getOne", Event.class);
			tq.setParameter("key", key);
			event = tq.getSingleResult();
			
		} catch(PersistenceException pe) {
			throw new DataException("An error occured whil retrieving an event", pe);
		} finally {
			em.close();
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
		
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			for (Event event : events) {
				
				Event previousEvent = em.find(Event.class, event.getKey());
				if (previousEvent != null) {
					//update
					previousEvent.setTitle(event.getTitle());
					previousEvent.setDate(event.getDate());
					previousEvent.setDescription(event.getDescription());
					em.merge(previousEvent);
				} else {
					//add
					em.persist(event);
				}
			}
			em.getTransaction().commit();
		} catch (PersistenceException pe) {
			throw new DataException("An error occured while saving the event", pe);
		} finally {
			em.close();
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean saveFlyer(InputStream input, String format, Flyer flyer) throws DataException {
		boolean saved = false;

		//validate input format
		final List<String> allowedFormat = Arrays.asList("png", "jpg", "jpeg", "gif");
		if(StringUtils.isBlank(format) || !allowedFormat.contains(format.toLowerCase())){
			throw new ValidationException("Unsupported flyer format :" + format );
		}
		
		try {
			//save base flyer to format
			BufferedImage img = ImageIO.read(input);
			ImageIO.write(img, Flyer.TYPE, flyer.getFile());
			
			//and creates the thumbnail
			BufferedImage thumbnail = Scalr.resize(
					img, 
					Scalr.Method.SPEED, 
					Scalr.Mode.FIT_TO_WIDTH, 
					181, 
					256, 
					Scalr.OP_ANTIALIAS
				);
			ImageIO.write(thumbnail, Flyer.TYPE, flyer.getThumbnail().getFile());
			
		} catch (IOException e) {
			throw new DataException("An error occured while saving the flyer", e);
		}
		return saved;
	}
}
