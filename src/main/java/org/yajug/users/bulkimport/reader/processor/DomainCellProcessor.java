package org.yajug.users.bulkimport.reader.processor;

import org.supercsv.cellprocessor.ift.CellProcessor;

public interface DomainCellProcessor {
	
	CellProcessor[] getProcessors();
}