package org.yajug.users.bulkimport;

import org.yajug.users.bulkimport.importer.DomainImporter;
import org.yajug.users.bulkimport.importer.EventImporter;
import org.yajug.users.bulkimport.reader.CsvEventReader;
import org.yajug.users.bulkimport.reader.DomainReader;
import org.yajug.users.bulkimport.reader.processor.DomainCellProcessor;
import org.yajug.users.bulkimport.reader.processor.EventCellProcessor;
import org.yajug.users.config.ModuleHelper;
import org.yajug.users.domain.Event;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

/**
 * Guice module that bind implementations for an event import
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class EventModule extends AbstractModule {

	@Override
	protected void configure() {
		
		ModuleHelper.bindProperties(binder());
		ModuleHelper.bindApis(binder());
		
		//bind importer, readers and processors
		bind(DomainImporter.class).to(EventImporter.class);
		bind(new TypeLiteral<DomainReader<Event>>(){}).to(CsvEventReader.class);
		bind(DomainCellProcessor.class).to(EventCellProcessor.class);
	}
}
