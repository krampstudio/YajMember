package org.yajug.users.bulkimport.reader.processor;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

public class MemberCellProcessor implements DomainCellProcessor {

	private CellProcessor[] cellProcessors;
	
	public MemberCellProcessor() {
		cellProcessors = new CellProcessor[] { 
				new NotNull(), 							// lastname
                new NotNull(), 							// firstname
                new NotNull(), 							// company
                new NotNull(),							// email
                new Optional(new ParseMembership(2010)),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        };
	}
	
	public CellProcessor[] getProcessors(){
		return cellProcessors;
	}
}
