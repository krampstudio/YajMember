package org.yajug.users.api;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yajug.users.domain.Event;
import org.yajug.users.domain.Flyer;
import org.yajug.users.service.DataException;
import org.yajug.users.service.EventService;
import org.yajug.users.service.FlyerService;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;

/**
 * This controller expose the Flyers API over HTTP
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Path("flyer")
public class FlyerController extends RestController {

	private final static Logger logger = LoggerFactory.getLogger(FlyerController.class);
	
	/** The service instance that manages events */
	@Inject private EventService eventService;
	
	/** The service instance that manages flyers */
	@Inject private FlyerService flyerService;
	
	/** Enables you to access the current servlet context within an action (it's thread safe) */
	@Context private ServletContext servletContext;
	
	/** The path where the flyers are saved */
	@Inject @Named("flyer.path") private String flyerPath;
	
	
	/**
	 * Update the event flyer's file. The method handles an HTTP file upload.
	 * 
	 * @param stream uploaded file data
	 * @param contentDisposition uploaded file meta
	 * @param bodyPart uploaded file meta
	 * @param id the event identifier the flyer is linked to
	 * @return  a JSON object that contains the 'saved' property
	 */
	@POST
	@Path("/save/{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/plain; charset=UTF-8")
	public String updateFlyer(
			@FormDataParam("flyer") InputStream stream,
			@FormDataParam("flyer") FormDataContentDisposition contentDisposition,
			@FormDataParam("flyer") FormDataBodyPart bodyPart, 
			@PathParam("id") String id) {
		
		JsonObject response = new JsonObject();
		boolean saved = false;
		
		//check MIME TYPE
		if(bodyPart == null || !bodyPart.getMediaType().isCompatible(new MediaType("image", "*"))){
			response.addProperty("error", "Unknow or unsupported file type");
		}
		
		try {
			//get the flyer's event
			Event event = eventService.getOne(id);
			if(event != null){
			
				//create a Flyer instance based on context
				final String flyerFullPath = servletContext.getRealPath(flyerPath);
				Flyer flyer = new Flyer(flyerFullPath, event);
				String format = bodyPart.getMediaType().getSubtype().toLowerCase();
				
				//and save the file
				if(flyerService.save(stream, format, flyer)){
					saved = true;
					response.addProperty("flyer", flyerPath + flyer.getFile().getName());
					response.addProperty("thumb", flyerPath + flyer.getThumbnail().getFile().getName());
				}
			}
		} catch (DataException e) {
			logger.error(e.getLocalizedMessage(), e);
			response.addProperty("error", serializeException(e));
		} 
		response.addProperty("saved", saved);
		
		return serializer.get().toJson(response);
	}
	
	/**
	 * Removes a flyer.<br>
	 * 
	 * Example:<br>
	 * {@code curl -i -H "Accept: application/json" -X DELETE  http://localhost:8000/YajMember/api/event/removeFlyer/1}
	 * 
	 * @param id the event identifier the flyer belongs to
	 * @return  a JSON object that contains the 'removed' property
	 */
	@DELETE
	@Path("/remove/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public String removeFlyer(@PathParam("id") String id){
		JsonObject response = new JsonObject();
		boolean removed = false;
		
		try {
			Event event = eventService.getOne(id);
			if(event != null){
				final String flyerFullPath = servletContext.getRealPath(flyerPath);
				Flyer flyer = new Flyer(flyerFullPath, event);
				
				removed = flyerService.remove(flyer);
			}
		} catch (DataException e) {
			logger.error(e.getLocalizedMessage(), e);
			response.addProperty("error", serializeException(e));
		}
		response.addProperty("removed", removed);
		return serializer.get().toJson(response);
	}
}
