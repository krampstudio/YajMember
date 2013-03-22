package org.yajug.users.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr;
import org.yajug.users.domain.Flyer;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;

/**
 * Implementation of the {@link EventService} API that delegates
 * persistence calls to DAOs.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Singleton
public class FlyerServiceImpl implements FlyerService {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean save(InputStream input, String format, Flyer flyer) throws DataException {
		boolean saved = false;

		//validate input format
		final List<String> allowedFormat = Lists.newArrayList("png", "jpg", "jpeg", "gif");
		
		if(StringUtils.isBlank(format) || !allowedFormat.contains(format.toLowerCase())){
			throw new ValidationException("Unsupported flyer format :" + format );
		}
		
		try {
			//save base flyer to format
			BufferedImage img = ImageIO.read(input);
			saved = ImageIO.write(img, Flyer.FORMAT, flyer.getFile());
			
			//and creates the thumbnail
			BufferedImage thumbnail = Scalr.resize(
					img, 
					Scalr.Method.SPEED, 
					Scalr.Mode.FIT_TO_WIDTH, 
					181, 
					256, 
					Scalr.OP_ANTIALIAS
				);
			saved = saved && ImageIO.write(thumbnail, Flyer.FORMAT, flyer.getThumbnail().getFile());
			
		} catch (IOException e) {
			throw new DataException("An error occured while saving the flyer", e);
		}
		return saved;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Flyer flyer) throws DataException {
		
		boolean removed = false;
		
		if (flyer == null || flyer.getFile() == null) {
			throw new ValidationException("Trying to remove a null flyer.");
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
