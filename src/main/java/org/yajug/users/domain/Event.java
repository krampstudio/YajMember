package org.yajug.users.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

/**
 * This domain pojo represents an event of the jug
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Entity
@Access(AccessType.FIELD)
@Inheritance(strategy=InheritanceType.JOINED)
@NamedQuery(name="Event.findAll", query="select e from Event e")
public class Event  extends DomainObject{

	@Basic private String title;
	@Lob private String description;
	@Basic private Date date;
	@ManyToMany private List<Member> participants;
	
	/**
	 * Default constructor needed by openjpa.
	 */
	public Event(){
	}
	
	public Event(String title, String description, Date date, List<Member> participants) {
		super();
		this.title = title;
		this.description = description;
		this.date = date;
		this.participants = participants;
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

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Event [title=" + title + ", description=" + description
				+ ", date=" + date + ", participants=" + participants
				+ ", key=" + key + "]";
	}
	
	
}
