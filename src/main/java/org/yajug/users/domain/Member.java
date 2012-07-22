/**
 * 
 */
package org.yajug.users.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 *
 */
@Entity
public class Member extends DomainObject {

	@Basic private String firstName;
	@Basic private String lastName;
	@Basic private String email;
	@Basic private String company;
	@Basic private List<Membership> memberships;
	@Enumerated(EnumType.STRING) private List<Role> roles;
	
	public Member(){
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}
	
	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	
	/**
	 * @return the memberships
	 */
	public List<Membership> getMemberships() {
		return memberships;
	}
	
	/**
	 * @param membership
	 */
	public void addMembership(Membership membership){
		if(this.memberships == null){
			this.memberships = new ArrayList<Membership>();
		}
		this.memberships.add(membership);
	}
	
	/**
	 * @param memberships the memberships to set
	 */
	public void setMemberships(List<Membership> memberships) {
		this.memberships = memberships;
	}
	
	/**
	 * @return the role
	 */
	public List<Role> getRoles() {
		return roles;
	}
	
	/**
	 * @param role the role to set
	 */
	public void setRoles(List<Role> role) {
		this.roles = role;
	}

}
