package org.yajug.users.bulkimport;

import org.yajug.users.bulkimport.importer.DomainImporter;
import org.yajug.users.bulkimport.importer.MemberImporter;
import org.yajug.users.bulkimport.reader.CsvMemberReader;
import org.yajug.users.bulkimport.reader.DomainReader;
import org.yajug.users.bulkimport.reader.processor.DomainCellProcessor;
import org.yajug.users.bulkimport.reader.processor.MemberCellProcessor;
import org.yajug.users.domain.Member;
import org.yajug.users.service.EventService;
import org.yajug.users.service.EventServiceImpl;
import org.yajug.users.service.MemberService;
import org.yajug.users.service.MemberServiceImpl;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class MemberModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DomainImporter.class).to(MemberImporter.class);
		bind(new TypeLiteral<DomainReader<Member>>(){}).to(CsvMemberReader.class);
		bind(DomainCellProcessor.class).to(MemberCellProcessor.class);
		
		bind(MemberService.class).to(MemberServiceImpl.class);
        bind(EventService.class).to(EventServiceImpl.class);
	}

}
