package org.yajug.users.bulkimport.reader.processor;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Provides cell processors for an {@link Member}
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class MembershipCellProcessor implements DomainCellProcessor {

	private CellProcessor[] cellProcessors;
	
	public MembershipCellProcessor() {
		cellProcessors = new CellProcessor[] { 
				new UniqueHashCode(new ParseLong()), 		// key (must be unique)
				new NotNull(new ParseInt()), 				// year
                new Optional(new ParseInt()), 				// amount
                new Optional(new ParseDate("yyyy-MM-dd")), 	// paiementDate
                new NotNull(new ParseMember())				// member's mail
        };
	}
	
	public CellProcessor[] getProcessors(){
		return cellProcessors;
	}
}
