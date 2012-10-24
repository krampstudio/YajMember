package org.yajug.users.bulkimport.reader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.yajug.users.bulkimport.reader.processor.DomainCellProcessor;
import org.yajug.users.domain.Event;

import com.google.inject.Inject;

public class CsvEventReader implements DomainReader<Event> {

	@Inject
	private DomainCellProcessor cellProcessor;
	
	@Override
	public Collection<Event> read(String fileName) {
		Collection<Event> events = new ArrayList<Event>();
        try (ICsvBeanReader beanReader = new CsvBeanReader(
            		new FileReader(fileName), 
            		CsvPreference.STANDARD_PREFERENCE
            	)){
            
            // the header elements are used to map the values to the bean (names must match)
            final String[] header = beanReader.getHeader(true);
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
