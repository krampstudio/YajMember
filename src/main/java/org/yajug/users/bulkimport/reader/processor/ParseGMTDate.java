package org.yajug.users.bulkimport.reader.processor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Parse date from CSV to a GMT date
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class ParseGMTDate extends CellProcessorAdaptor {

	/** The format to give to {@link SimpleDateFormat} */
	private String dateFormat;
	
	public ParseGMTDate(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public ParseGMTDate(String dateFormat, DateCellProcessor next) {
		super(next);
		this.dateFormat = dateFormat;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		if( !(value instanceof String) ) {
			throw new SuperCsvCellProcessorException(String.class, value, context, this);
		}
		try {
			final SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			formatter.setTimeZone(TimeZone.getTimeZone("GMT-00:00"));
			final Date result = formatter.parse((String) value);
			
			return next.execute(result, context);
		}
		catch(final ParseException e) {
			throw new SuperCsvCellProcessorException(String.format("'%s' could not be parsed as a Date", value),
				context, this, e);
		}
	}
}
