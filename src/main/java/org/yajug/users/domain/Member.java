package org.yajug.users.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This domain pojo represent a member of the jug.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
//	@NamedQuery(name="Member.findAll", query="select m from Member m")
public class Member extends DomainObject implements Comparable<Member>{

	private String firstName;
	private String lastName;
	private String email;
	private String company;
	private List<Role> roles;
	private List<MemberShip> memberships;
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
	
	public Member(String firstName, String lastName ,String email, String company, List<Role> roles ,List<MemberShip> memberships) {
		this(firstName, lastName, email, company, roles);
		this.setMemberships(memberships);
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
	
	public void setRole(Role role){
		if(this.roles == null){
			this.roles = new ArrayList<Role>();
		}
		if(!this.roles.contains(role)){
			this.roles.add(role);
		}
	}
	
	/**
	 * @param role the role to set
	 */
	public void setRoles(List<Role> role) {
		this.roles = role;
	}

	/**
	 * @return the memberships
	 */
	public List<MemberShip> getMemberships() {
		return memberships;
	}
	
	/**
	 * 
	 * @param membership
	 */
	public void setMembership(MemberShip membership){
		if(this.memberships == null){
			this.memberships = new ArrayList<MemberShip>();
		}
		this.memberships.add(membership);
		if(Calendar.getInstance().get(Calendar.YEAR) == membership.getYear()){
			this.valid = true;
		}
	}

	/**
	 * @param memberships the memberships to set
	 */
	public void setMemberships(List<MemberShip> memberships) {
		this.memberships = memberships;
		if(memberships != null){
			this.valid = isValidFor(Calendar.getInstance().get(Calendar.YEAR));
		}
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
	 * Check if this member instance has 
	 * a valid membership for the current year.
	 * 
	 * @return true if valid
	 */
	public boolean checkValidity(){
		this.valid = isValidFor(Calendar.getInstance().get(Calendar.YEAR));
		return this.valid;
	}
	
	/**
	 * Check if this member instance has 
	 * a valid membership for the specified year.
	 * 
	 * @param year the year we check for validity
	 * @return true if valid
	 */
	public boolean isValidFor(int year) {
		boolean validFor = false;
		
		if(this.memberships == null){
			validFor = false;
		} else {
			for(MemberShip ms : this.memberships){
				if(ms.getYear() == year){
					validFor = true;
					break;
				}
			}
		}
		return validFor;
	}

	
	@Override
	public String toString() {
		return "Member [firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", company=" + company + ", roles="
				+ roles + ", memberships=" + memberships + ", valid=" + valid
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
