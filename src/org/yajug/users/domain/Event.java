package org.yajug.users.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class Event  extends DomainObject{

	
	@Basic private String title;
	@Basic private String description;
	@Basic private Date date;
	@Basic private List<User> participants;
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the participants
	 */
	public List<User> getParticipants() {
		return participants;
	}
	/**
	 * @param participants the participants to set
	 */
	public void setParticipants(List<User> participants) {
		this.participants = participants;
	}
	
	
}
