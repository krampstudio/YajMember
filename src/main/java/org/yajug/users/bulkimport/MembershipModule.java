package org.yajug.users.bulkimport;

import org.yajug.users.bulkimport.importer.DomainImporter;
import org.yajug.users.bulkimport.importer.MembershipImporter;
import org.yajug.users.bulkimport.reader.CsvMembershipReader;
import org.yajug.users.bulkimport.reader.DomainReader;
import org.yajug.users.bulkimport.reader.processor.DomainCellProcessor;
import org.yajug.users.bulkimport.reader.processor.MembershipCellProcessor;
import org.yajug.users.config.ModuleHelper;
import org.yajug.users.domain.Membership;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

/**
 * Guice module that bind implementations for a membership import
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class MembershipModule extends AbstractModule {

	@Override
	protected void configure() {
		
		ModuleHelper.bindProperties(binder());
		ModuleHelper.bindApis(binder());
		
		//bind importer, readers and processors
		bind(DomainImporter.class).to(MembershipImporter.class);
		bind(new TypeLiteral<DomainReader<Membership>>(){}).to(CsvMembershipReader.class);
		bind(DomainCellProcessor.class).to(MembershipCellProcessor.class);
	}
}
