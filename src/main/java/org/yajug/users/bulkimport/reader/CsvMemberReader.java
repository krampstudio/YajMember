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
import org.yajug.users.domain.Member;

import com.google.inject.Inject;

/**
 * Reads {@link Member} from CSV
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class CsvMemberReader implements DomainReader<Member> {

	@Inject
	private DomainCellProcessor cellProcessor;
	
	
	@Override
	public Collection<Member> read(String fileName) {
		Collection<Member> members = new ArrayList<Member>();
        try(ICsvBeanReader  beanReader = new CsvBeanReader(
        			new BufferedReader(new InputStreamReader(new FileInputStream(fileName), CHARSET)), 
            		CsvPreference.STANDARD_PREFERENCE
            	)){
            
            final String[] headers = beanReader.getHeader(true);
            final CellProcessor[] processors = cellProcessor.getProcessors();
            
            Member member;
            while( (member = beanReader.read(Member.class, headers, processors)) != null ) {
                members.add(member);
            }
        } catch(IOException e) {
        	e.printStackTrace();
        } 
		return members;
	}
}
