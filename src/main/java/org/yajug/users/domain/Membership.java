package org.yajug.users.domain;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
@Entity
public class Membership  extends DomainObject {

	public static final int ANNUAL_FEE = 40;
	
	@Basic private int year;
	@Basic private Date paiementDate;
	@Basic private int amount;
	@Embedded private Event event;
	@Embedded private Member member;
	
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	
	/**
	 * @return the paiement
	 */
	public Date getPaiementDate() {
		return paiementDate;
	}
	
	/**
	 * @param paiement the paiement to set
	 */
	public void setPaiementDate(Date paiement) {
		this.paiementDate = paiement;
	}
	
	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	/**
	 * @return the event
	 */
	public Event getEvent() {
		return event;
	}
	
	/**
	 * @param event the event to set
	 */
	public void setEvent(Event event) {
		this.event = event;
	}
	
	/**
	 * @return the member
	 */
	public Member getMember() {
		return member;
	}
	
	/**
	 * @param member the member to set
	 */
	public void setMember(Member member) {
		this.member = member;
	}
	
}
