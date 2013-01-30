package org.yajug.users.bulkimport.importer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.yajug.users.bulkimport.reader.DomainReader;
import org.yajug.users.domain.Member;
import org.yajug.users.domain.Membership;
import org.yajug.users.service.DataException;
import org.yajug.users.service.MemberService;

import com.google.inject.Inject;

/**
 * Importer for {@link Member} 
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class MembershipImporter implements DomainImporter {

	@Inject
	private DomainReader<Membership> reader;
	
	@Inject
	private MemberService memberService;
	
	@Override
	public int doImport(String fileName) {
		
		int imported = 0;
		
		Collection<Membership> memberships = new ArrayList<>();
		try {
			memberships = reader.read(fileName);
		
			for(Membership membership : memberships){
				if(membership.getMember() != null){
					//get member
					Collection<Member> foundMembers = memberService.findAll(membership.getMember().getEmail());
					if(foundMembers.size() == 0){
						System.out.println("No members found for email " + membership.getMember().getEmail() + ", skipped.");
					} else if(foundMembers.size() > 1){
						System.out.println("Multiple members found for email " + membership.getMember().getEmail() + ", skipped.");
					} else {
						Member member = foundMembers.iterator().next();
						if(member.getKey() <= 0){
							System.out.println("Invalid key " + member.getKey() + " for member " + membership.getMember().getEmail() + ", skipped.");
						} else {
							membership.setMember(member);
						}
					}
				}
			}
			
			if(memberService.saveMemberships(memberships)){
				imported = memberships.size();
			}
			
		} catch (IOException | DataException e) {
			e.printStackTrace();
		}

		return imported;
	}

}
