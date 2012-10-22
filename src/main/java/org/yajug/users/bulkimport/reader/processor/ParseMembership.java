package org.yajug.users.bulkimport.reader.processor;

import org.apache.commons.lang.StringUtils;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;
import org.yajug.users.domain.Membership;

public class ParseMembership extends CellProcessorAdaptor {

	private int year;
	
	public ParseMembership(int year) {
		super();
	}
	
	public ParseMembership(int year, CellProcessor next) {
		//to allow chaining
		super(next);
		this.year = year;
	}
	
	
	@Override
	public Object execute(Object value, CsvContext context) {
		
		if(value != null && StringUtils.isNotBlank(value.toString())){
			
			Membership membership = new Membership();
			membership.setYear(year);
			
			context.getRowSource();
			
			return next.execute(membership, context);
			
		}
		
		return null;
	}

}
