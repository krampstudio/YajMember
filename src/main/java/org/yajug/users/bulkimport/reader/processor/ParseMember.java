package org.yajug.users.bulkimport.reader.processor;

import org.apache.commons.lang3.StringUtils;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;
import org.yajug.users.domain.Member;

/**
 * Parse and generate a member instance from an email field
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class ParseMember extends CellProcessorAdaptor {

	public ParseMember() {
	}
	
	public ParseMember(CellProcessor next) {
		super(next);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(Object value, CsvContext context) {
		if(value != null && StringUtils.isNotBlank(value.toString())){
			
			Member member = new Member();
			member.setEmail(value.toString());
			
			return next.execute(member, context);
		}
		return null;
	}

}
