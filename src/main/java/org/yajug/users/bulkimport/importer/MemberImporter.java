package org.yajug.users.bulkimport.importer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.yajug.users.bulkimport.reader.DomainReader;
import org.yajug.users.domain.Member;
import org.yajug.users.service.DataException;
import org.yajug.users.service.MemberService;

import com.google.inject.Inject;

/**
 * Importer for {@link Member}
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class MemberImporter implements DomainImporter {

	@Inject
	private DomainReader<Member> reader;
	
	@Inject
	private MemberService memberService;
	
	@Override
	public int doImport(String fileName) {
		
		int imported = 0;
		
		Collection<Member> members = new ArrayList<Member>();
		try {
			members = reader.read(fileName);
		
			if(memberService.save(members)){
				imported = members.size();
			}
			
		} catch (IOException | DataException e) {
			e.printStackTrace();
		}

		return imported;
	}

}
