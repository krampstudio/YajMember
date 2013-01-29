package org.yajug.users.bulkimport.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.yajug.users.bulkimport.reader.processor.DomainCellProcessor;
import org.yajug.users.domain.Event;
import org.yajug.users.domain.Member;
import org.yajug.users.domain.Role;
import org.yajug.users.service.DataException;
import org.yajug.users.service.EventService;

import com.google.inject.Inject;

/**
 * Reads {@link Member} from CSV
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class CsvMemberReader implements DomainReader<Member> {

	@Inject
	private DomainCellProcessor cellProcessor;
	
	private EventService eventService;
	
//	private int currentYear;
	
	private Map<String, Event> events;
	
	/**
	 * Do some initializations : current year and events
	 */
	@Inject
	public CsvMemberReader(EventService eventService) {
		
		this.eventService = eventService;
		
		//init the current year to be consistent across the imports
//		this.currentYear = Calendar.getInstance().get(Calendar.YEAR);
		
		//we get the list of event to do the mapping from what's on the file
		try {
			if(loadEvents().size() == 0){
				System.out.println("No events founds! Members should be imported once events are done.");
			}
		} catch (DataException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Build a map of events 
	 * @return the map with the event date as key (formated in a string) 
	 * @throws DataException
	 */
	private Map<String, Event> loadEvents() throws DataException{
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		events = new HashMap<String, Event>();
		for(Event event : eventService.getAll()){
			if(event.getDate() != null){
				events.put(
					dateFormatter.format(event.getDate()),
					event
				);
			}
		}
		return events;
	}
	
	@Override
	public Collection<Member> read(String fileName) {
		Collection<Member> members = new ArrayList<Member>();
        try(ICsvBeanReader  beanReader = new CsvBeanReader(
        			new BufferedReader(new InputStreamReader(new FileInputStream(fileName), CHARSET)), 
            		CsvPreference.STANDARD_PREFERENCE
            	)){
            
            final String[] baseHeaders = beanReader.getHeader(true);
            String[] headers = baseHeaders.clone();
            for(int i = 7; i < headers.length; i++){
            	headers[i] = null;
            }
            final CellProcessor[] processors = cellProcessor.getProcessors();
            
            Member member;
            while( (member = beanReader.read(Member.class, headers, processors)) != null ) {
                
            	//update memberships of the member
//            	MemberShip currentMembership = null;
//            	for(MemberShip membership : member.getMemberships()){
//            		membership.setAmount(MemberShip.ANNUAL_FEE);
//            		membership.setMember(member);
//            		if(currentYear == membership.getYear()){
//            			currentMembership = membership;
//            		}
//            	}
//            	//check if the member has subscribed during an event for the current year
//            	if(currentMembership != null){
//            		member.setRole(Role.MEMBER);
//            		 for(int i = 7; i < 12; i++){
//            			 String eventMembership = beanReader.get(i+1);
//            			 if(StringUtils.isNotBlank(eventMembership)
//            					 && "MBR".equalsIgnoreCase(eventMembership)){
//            				 Event event = events.get(baseHeaders[i]);
//            				 currentMembership.setEvent(event);
//            			 }
//                     }
//            	} else{
            		member.setRole(Role.OLD_MEMBER);
//	         	}
            	
                members.add(member);
            }
        } catch(IOException e) {
        	e.printStackTrace();
        } 
		return members;
	}
}
