package org.yajug.users.bulkimport.importer;

/**
 * Import of domain objects.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public interface DomainImporter {

	/**
	 * Import data from a file
	 * @param fileName the file that contains the data
	 * @return the number of domain instances imported from the file
	 */
	int doImport(String fileName);
}
