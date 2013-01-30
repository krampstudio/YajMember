package org.yajug.users.bulkimport;

import org.yajug.users.bulkimport.importer.DomainImporter;
import org.yajug.users.bulkimport.importer.MembershipImporter;
import org.yajug.users.bulkimport.reader.CsvMembershipReader;
import org.yajug.users.bulkimport.reader.DomainReader;
import org.yajug.users.bulkimport.reader.processor.DomainCellProcessor;
import org.yajug.users.bulkimport.reader.processor.MembershipCellProcessor;
import org.yajug.users.domain.Membership;
import org.yajug.users.service.EventService;
import org.yajug.users.service.EventServiceImpl;
import org.yajug.users.service.MemberService;
import org.yajug.users.service.MemberServiceImpl;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

/**
 * Guice module that bind implementations for an event import
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class MembershipModule extends AbstractModule {

	@Override
	protected void configure() {
		//bind importer, readers and processors
		bind(DomainImporter.class).to(MembershipImporter.class);
		bind(new TypeLiteral<DomainReader<Membership>>(){}).to(CsvMembershipReader.class);
		bind(DomainCellProcessor.class).to(MembershipCellProcessor.class);
		
		//bind services
		bind(MemberService.class).to(MemberServiceImpl.class);
		bind(EventService.class).to(EventServiceImpl.class);
	}

}
