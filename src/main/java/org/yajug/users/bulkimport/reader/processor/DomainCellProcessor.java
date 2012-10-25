package org.yajug.users.bulkimport.reader.processor;

import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Provides cell processors for a Domain object
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public interface DomainCellProcessor {
	
	/**
	 * Get the processors
	 * @return the cell processors in the right order
	 */
	CellProcessor[] getProcessors();
}