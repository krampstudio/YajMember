package org.yajug.users.bulkimport.reader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.yajug.users.bulkimport.reader.processor.DomainCellProcessor;
import org.yajug.users.domain.Member;

import com.google.inject.Inject;

public class CsvMemberReader implements DomainReader<Member> {

	@Inject
	private DomainCellProcessor cellProcessor;
	
	@Override
	public Collection<Member> read(String fileName) {
		Collection<Member> members = new ArrayList<Member>();
		ICsvBeanReader beanReader = null;
        try {
            beanReader = new CsvBeanReader(
            		new FileReader(fileName), 
            		CsvPreference.STANDARD_PREFERENCE
            	);
            
            String[] header = beanReader.getHeader(true);
            for(int i = 5; i < header.length; i++){
            	header[i] = null;
            }
            System.out.println("\n" + Arrays.deepToString(header) + "\n");
            
            final CellProcessor[] processors = cellProcessor.getProcessors();
            
            Member member;
            while( (member = beanReader.read(Member.class, header, processors)) != null ) {
                System.out.println(
                		String.format("lineNo=%s, rowNo=%s, customer=%s", 
                				beanReader.getLineNumber(),
                				beanReader.getRowNumber(), member));
                members.add(member);
            }
                
        } catch(IOException e) {
        	e.printStackTrace();
        } finally {
            if( beanReader != null ) {
                try {
					beanReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
		return members;
	}
}
