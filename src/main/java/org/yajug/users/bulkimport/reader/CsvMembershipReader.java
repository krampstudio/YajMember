package org.yajug.users.bulkimport.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.yajug.users.bulkimport.reader.processor.DomainCellProcessor;
import org.yajug.users.domain.Membership;

import com.google.inject.Inject;

/**
 * Reads {@link Membership} from CSV
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class CsvMembershipReader implements DomainReader<Membership> {

	@Inject
	private DomainCellProcessor cellProcessor;
	
	@Override
	public Collection<Membership> read(String fileName) {
	
		Collection<Membership> memberships = new ArrayList<>();
        try( ICsvBeanReader  beanReader = new CsvBeanReader(
        			new BufferedReader(new InputStreamReader(new FileInputStream(fileName), CHARSET)), 
            		CsvPreference.STANDARD_PREFERENCE
            	)){
            
            final String[] headers = beanReader.getHeader(true);
            final CellProcessor[] processors = cellProcessor.getProcessors();
            
            Membership membership;
            while( (membership = beanReader.read(Membership.class, headers, processors)) != null ) {
                memberships.add(membership);
            }
            
        } catch(IOException e) {
        	e.printStackTrace();
        } 
		return memberships;
	}
}
