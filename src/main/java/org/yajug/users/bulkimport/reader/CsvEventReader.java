package org.yajug.users.bulkimport.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.yajug.users.bulkimport.reader.processor.DomainCellProcessor;
import org.yajug.users.domain.Event;

import com.google.inject.Inject;

/**
 * Reads {@link Event} from CSV
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class CsvEventReader implements DomainReader<Event> {

	@Inject
	private DomainCellProcessor cellProcessor;
	
	@Override
	public Collection<Event> read(String fileName) {
		Collection<Event> events = new ArrayList<Event>();
        try (ICsvBeanReader beanReader = new CsvBeanReader(
        			new BufferedReader(new InputStreamReader(new FileInputStream(fileName), CHARSET)), 
            		CsvPreference.STANDARD_PREFERENCE
            	)){
            
            // the header elements are used to map the values to the bean (names must match)
            final String[] header = beanReader.getHeader(true);
            System.out.println(Arrays.deepToString(header));
            final CellProcessor[] processors = cellProcessor.getProcessors();
            
            Event event;
            while( (event = beanReader.read(Event.class, header, processors)) != null ) {
                events.add(event);
            }
                
        } catch(IOException e) {
        	e.printStackTrace();
        } 
		return events;
	}
}
