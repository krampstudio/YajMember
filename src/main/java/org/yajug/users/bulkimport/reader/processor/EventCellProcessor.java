package org.yajug.users.bulkimport.reader.processor;

import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;

public class EventCellProcessor implements DomainCellProcessor {

	private CellProcessor[] cellProcessors;
	
	public EventCellProcessor() {
		cellProcessors = new CellProcessor[] { 
                new UniqueHashCode(new ParseLong()), 	// key (must be unique)
                new NotNull(), 							// title
                new NotNull(), 							// description
                new ParseDate("yyyy-MM-dd") 			// date
        };
	}
	
	public CellProcessor[] getProcessors(){
		return cellProcessors;
	}
}
