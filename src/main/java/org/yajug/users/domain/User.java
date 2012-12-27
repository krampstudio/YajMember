package org.yajug.users.domain;

import java.util.Locale;

public class User {

	private String id;
	private String email; 
	private boolean verifiedEmail;
	private String name; 
	private String  givenName; 
	private String familyName;
	private Locale locale; 
	private String  hd;
	
	public User() {
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean isVerifiedEmail() {
		return verifiedEmail;
	}
	
	public void setVerifiedEmail(boolean verifiedEmail) {
		this.verifiedEmail = verifiedEmail;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getGivenName() {
		return givenName;
	}
	
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	
	public String getFamilyName() {
		return familyName;
	}
	
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public String getHd() {
		return hd;
	}
	
	public void setHd(String hd) {
		this.hd = hd;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", verifiedEmail="
				+ verifiedEmail + ", name=" + name + ", givenName=" + givenName
				+ ", familyName=" + familyName + ", locale=" + locale + ", hd="
				+ hd + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}
	
	
}
