package org.yajug.users.bulkimport;

import org.yajug.users.bulkimport.importer.DomainImporter;
import org.yajug.users.bulkimport.importer.EventImporter;
import org.yajug.users.bulkimport.reader.CsvEventReader;
import org.yajug.users.bulkimport.reader.DomainReader;
import org.yajug.users.bulkimport.reader.processor.DomainCellProcessor;
import org.yajug.users.bulkimport.reader.processor.EventCellProcessor;
import org.yajug.users.domain.Event;
import org.yajug.users.service.EventService;
import org.yajug.users.service.EventServiceImpl;
import org.yajug.users.service.MemberService;
import org.yajug.users.service.MemberServiceImpl;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class EventModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DomainImporter.class).to(EventImporter.class);
		bind(new TypeLiteral<DomainReader<Event>>(){}).to(CsvEventReader.class);
		bind(DomainCellProcessor.class).to(EventCellProcessor.class);
		
		bind(MemberService.class).to(MemberServiceImpl.class);
        bind(EventService.class).to(EventServiceImpl.class);
	}

}
