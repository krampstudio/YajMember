package org.yajug.users.bulkimport.importer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.yajug.users.bulkimport.reader.DomainReader;
import org.yajug.users.domain.Member;

import com.google.inject.Inject;

public class MemberImporter implements DomainImporter {

	@Inject
	private DomainReader<Member> reader;
	
	@Override
	public int doImport(String fileName) {
		
		int imported = 0;
		
		Collection<Member> members = new ArrayList<Member>();
		try {
			members = reader.read(fileName);
		
			imported = members.size();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return imported;
	}

}
