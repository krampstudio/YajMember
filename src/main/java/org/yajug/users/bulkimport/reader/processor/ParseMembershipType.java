package org.yajug.users.bulkimport.reader.processor;

import org.apache.commons.lang3.StringUtils;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.util.CsvContext;
import org.yajug.users.domain.Membership;

public class ParseMembershipType extends CellProcessorAdaptor {

	@Override
	public Object execute(Object value, CsvContext context) {
		if(value != null && StringUtils.isNotBlank(value.toString())){
			
			return Membership.Type.valueOf(value.toString());
		}
		return null;
	}

}
