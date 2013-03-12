package org.yajug.users.domain;

import java.util.Date;
import java.util.List;

/**
 * This domain pojo represents an event of the jug
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class Event  extends DomainObject{

	private String title;
	private String description;
	private Date date;
	private List<Member> participants;
	private List<Member> registrants;
	
	/**
	 * Default constructor
	 */
	public Event(){
	}
	
	/**
	 * Key based constructor
	 * @param key
	 */
	public Event(String key){
		super(key);
	}
	
	public Event(String title, String description, Date date) {
		super();
		this.title = title;
		this.description = description;
		this.date = date;
	}

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
	public List<Member> getParticipants() {
		return participants;
	}
	
	/**
	 * @param participants the participants to set
	 */
	public void setParticipants(List<Member> participants) {
		this.participants = participants;
	}
	
	public List<Member> getRegistrants() {
		return registrants;
	}

	public void setRegistrants(List<Member> registrants) {
		this.registrants = registrants;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Event [title=" + title + ", description=" + description
				+ ", date=" + date + ", participants=" + participants
				+ ", registrants=" + registrants
				+ ", key=" + key + "]";
	}
}
