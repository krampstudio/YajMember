package org.yajug.users.bulkimport;

import org.yajug.users.bulkimport.importer.DomainImporter;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;


public class BulkImport {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length < 2){
			//throw an exception or show help message
		}
		
		AbstractModule module = null;
			
		switch(args[0].toLowerCase()){
			case "event" : module = new EventModule(); break;
			case "member" : module = new MemberModule(); break;
		}
		
		if(module != null){
			Injector injector = Guice.createInjector(module);
			DomainImporter importer = injector.getInstance(DomainImporter.class);
		
			importer.doImport(args[1]);
		}
	}

}
