package org.yajug.users.api.helper;

import java.beans.Statement;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
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
	
	private final static Logger logger = LoggerFactory.getLogger(MemberImportHelper.class);
	
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
			List<String> fields, 
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
			try(ICsvMapReader  mapReader = new CsvMapReader(
					new StringReader(content), 
					preferences
				)){
				
				if(ignoreHeader){
					int columnSize =  mapReader.getHeader(true).length;
					if(columnSize > fields.size()){
						for(int i = fields.size(); i < columnSize; i++){
							fields.add(i, null);
						}
					}
				}
				List<CellProcessor> processors = new ArrayList<>(fields.size());
				for(String field : fields){
					if(!AVAILABLE_FIELDS.contains(field)){
						processors.add(null); //ignore
					} else {
						processors.add(new Optional());
					}
				}
				
				
				Map<String, Object> row;
	            while( (row = mapReader.read(
	            				fields.toArray(new String[fields.size()]), 
	            				processors.toArray(new CellProcessor[processors.size()])
	            			)
	            		) != null ) {
	            	
	                boolean atLeastOne = false;
	                Member member = new Member();
	                
	                //assign the values to the member
	                for(String field : AVAILABLE_FIELDS){
	                	if(row.containsKey(field) && row.get(field) != null){
	                		String value = row.get(field).toString().trim();
	                		if(StringUtils.isNotBlank(value)){
		                		if("name".equals(field)){
		                			//special case for the name
		                			parseMemberName(value, member);
		                			atLeastOne = true;
		                		} else {
		                			//call the setter
			                		try {
										new Statement(member, 
												"set" + field.substring(0, 1).toUpperCase() + field.substring(1), 
												new Object[]{value}
											).execute();
									} catch (Exception e) {
										logger.error(e.getMessage(), e);
									}
			                		atLeastOne = true;
			                	}
	                		}
		                }
	                }
	                if(atLeastOne){
	                	members.add(member);
	                }
	            }
	            
			} catch (IOException e) {
				throw new DataException("Error while reading csv", e);
			}
		} 
		return members;
	}
	
	/**
	 * Parse a string composed by first name and last name and assign them to the Member
	 * TODO the split is not satisfying and could be improved (ie. case with a space in the lastName)
	 * @param name the string to parse and split
	 * @param member the member instance to assign the name
	 */
	private void parseMemberName(String name, Member member){

		final Pattern upperCase = Pattern.compile("^[A-Z\\p{P}\\s]+$");
		
		int spaceIndex = name.indexOf(' ');
		if(spaceIndex > 0){
			String seq1 = name.substring(0,spaceIndex);
			String seq2 = name.substring(spaceIndex);
			//upper case is lastName
			if(upperCase.matcher(seq1).find() && !upperCase.matcher(seq1).find()){
				member.setLastName(seq1);
				member.setFirstName(seq2);
			} else {
				member.setFirstName(seq1);
				member.setLastName(seq2);
			}
		} else {
			member.setLastName(name);
		}
	}
}
