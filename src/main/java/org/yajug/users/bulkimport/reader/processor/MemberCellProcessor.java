package org.yajug.users.bulkimport.reader.processor;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Provides cell processors for an {@link Member}
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class MemberCellProcessor implements DomainCellProcessor {

	private CellProcessor[] cellProcessors;
	
	public MemberCellProcessor() {
		cellProcessors = new CellProcessor[] { 
				new UniqueHashCode(new ParseLong()), 	// key (must be unique)
				new NotNull(), 							// lastname
                new NotNull(), 							// firstname
                new Optional(), 						// company
                new Optional()							// email
        };
	}
	
	public CellProcessor[] getProcessors(){
		return cellProcessors;
	}
}
