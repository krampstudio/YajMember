package org.yajug.users.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * This domain pojo represent a member of the jug.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class Member extends DomainObject implements Comparable<Member>{

	private String firstName;
	private String lastName;
	private String email;
	private String company;
	private List<Role> roles;
	private boolean valid;
	
	/**
	 * Default constructor needed
	 */
	public Member() {
	}
	
	/**
	 * Key based constructor
	 * @param key
	 */
	public Member(long key){
		super(key);
	}
	
	/**
	 * Convenient constructor
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param company
	 * @param roles
	 */
	public Member(String firstName, String lastName ,String email, String company, List<Role> roles) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.company = company;
		this.roles = roles;
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
	 * @return the role
	 */
	public List<Role> getRoles() {
		return roles;
	}
	
	/**
	 * @param role
	 */
	public void setRole(Role role){
		if(this.roles == null){
			this.roles = new ArrayList<Role>();
		}
		if(!this.roles.contains(role)){
			this.roles.add(role);
			
			//try to keep some consistency between the roles when updated by the app
			if(role.equals(Role.MEMBER) && roles.contains(Role.OLD_MEMBER)){
				roles.remove(Role.OLD_MEMBER);
			}
			if(role.equals(Role.OLD_MEMBER) && roles.contains(Role.MEMBER)){
				roles.remove(Role.MEMBER);
			}
			if(role.equals(Role.BOARD) && roles.contains(Role.OLD_BOARD)){
				roles.remove(Role.OLD_BOARD);
			}
			if(role.equals(Role.OLD_BOARD) && roles.contains(Role.BOARD)){
				roles.remove(Role.BOARD);
			}
		}
	}
	
	/**
	 * @param role the role to set
	 */
	public void setRoles(List<Role> role) {
		this.roles = role;
	}

	/**
	 * Get the membership status for the current year.
	 * 
	 * @return true if the user is a valid member
	 */
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * Set the membership status for the current year.
	 * @param valid if the user is a valid member
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	@Override
	public String toString() {
		return "Member [key=" + key + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", company=" + company + ", roles="
				+ roles + ", valid=" + valid
				+ "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Member))
			return false;
		Member other = (Member) obj;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

	@Override
	public int compareTo(Member o) {
		if(o == null){
			return 1;
		}
		return (int)(this.key - o.getKey());
	}
}
