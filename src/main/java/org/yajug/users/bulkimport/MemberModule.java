package org.yajug.users.bulkimport;

import org.yajug.users.bulkimport.importer.DomainImporter;
import org.yajug.users.bulkimport.importer.MemberImporter;
import org.yajug.users.bulkimport.reader.CsvMemberReader;
import org.yajug.users.bulkimport.reader.DomainReader;
import org.yajug.users.bulkimport.reader.processor.DomainCellProcessor;
import org.yajug.users.bulkimport.reader.processor.MemberCellProcessor;
import org.yajug.users.config.ModuleHelper;
import org.yajug.users.domain.Member;
import org.yajug.users.service.MemberService;
import org.yajug.users.service.MemberServiceImpl;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

/**
 * Guice module that bind implementations for an member import
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class MemberModule extends AbstractModule {

	@Override
	protected void configure() {
		
		ModuleHelper.bindProperties(binder());
		
		//bind importer, readers and processors
		bind(DomainImporter.class).to(MemberImporter.class);
		bind(new TypeLiteral<DomainReader<Member>>(){}).to(CsvMemberReader.class);
		bind(DomainCellProcessor.class).to(MemberCellProcessor.class);
		
		//bind services
		bind(MemberService.class).to(MemberServiceImpl.class);
	}
}
