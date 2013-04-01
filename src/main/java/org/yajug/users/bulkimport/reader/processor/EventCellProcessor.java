package org.yajug.users.bulkimport.reader.processor;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Provides cell processors for an {@link Event}
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class EventCellProcessor implements DomainCellProcessor {

	private CellProcessor[] cellProcessors;
	
	public EventCellProcessor() {
		cellProcessors = new CellProcessor[] { 
                new NotNull(), 							// title
                new Optional(), 						// description
                new ParseGMTDate("yyyy-MM-dd") 			// date
        };
	}
	
	public CellProcessor[] getProcessors(){
		return cellProcessors;
	}
}
