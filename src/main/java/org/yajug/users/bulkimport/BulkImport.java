package org.yajug.users.bulkimport;

import org.yajug.users.bulkimport.importer.DomainImporter;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Main entry point for the CLI bulk import
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class BulkImport {

	/**
	 * @param args path target
	 */
	public static void main(String[] args) {
		
		if(args.length < 2){
			//throw an exception or show help message
		}
		
		//load the guice module regarding the args
		AbstractModule module = null;
		switch(args[0].toLowerCase()){
			case "event" : module = new EventModule(); break;
			case "member" : module = new MemberModule(); break;
			case "membership" : module = new MembershipModule(); break;
		}
		
		if(module != null){
			Injector injector = Guice.createInjector(module);
			DomainImporter importer = injector.getInstance(DomainImporter.class);
		
			int imported = importer.doImport(args[1]);
			
			System.out.println(imported + " " + args[0] + " imported");
		}
	}

}
