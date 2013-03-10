package org.yajug.users.api.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.yajug.users.domain.Member;
import org.yajug.users.service.DataException;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.inject.Singleton;

/**
 * Helps you to import {@link Member} from an external source
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Singleton
public class MemberImportHelper {
	
	private final static List<String> AVAILABLE_FIELDS = Lists.newArrayList(
		"firstName", "lastName", "name", "company", "email"
	);

	/**
	 * Parse a CSV stream to read {@link Member}s 
	 * @param input the stream of CSV data
	 * @param fields contains the CSV fields names to map to the member's attributes 
	 * 			(the fields order must reflect the CSV columns)
	 * @param ignoreHeader if the 1st row of the file should be ignored
	 * @param delimiter the CSV delimiter 
	 * @param wrapper the CSV cell content wrapper
	 * @return a list of Members 
	 * @throws DataException
	 */
	public Collection<Member> readFromCsv(
			InputStream input, 
			String[] fields, 
			boolean ignoreHeader, 
			char delimiter,
			char wrapper) throws DataException {
		
		
		final CsvPreference preferences = new CsvPreference.Builder(
				wrapper, 
				delimiter, 
				"\r\n"
			).build();
		
		Collection<Member> members = new ArrayList<Member>();
		
		String content = null;
		try (Reader in = new InputStreamReader(input, Charsets.UTF_8)) {
			content = CharStreams.toString(in);
		} catch (IOException e) {
			throw new DataException("Error while reading csv stream", e);
		}
		if(content != null){
			try(ICsvBeanReader beanReader = new CsvBeanReader(
					new StringReader(content), 
					preferences
				)){
				
				if(ignoreHeader){
					beanReader.getHeader(true);
				}
				CellProcessor[] processors = new CellProcessor[fields.length];
				for(int i = 0; i < fields.length ; i++){
					if(!AVAILABLE_FIELDS.contains(fields[i])){
						processors[i] = null; //ignore
					} else {
						if("name".equals(fields[i])){
							//TODO create a cellprocessor
						}
						processors[i] = new NotNull();
					}
				}
				
				Member member;
	            while( (member = beanReader.read(Member.class, fields, processors)) != null ) {
	                members.add(member);
	            }
	            
			} catch (IOException e) {
				throw new DataException("Error while reading csv", e);
			}
		} 
		return members;
	}
}
