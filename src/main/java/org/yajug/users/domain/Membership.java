package org.yajug.users.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This domain POJO represents an annual membership paid by a {@link Member}.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class Membership  extends DomainObject {

	public static final int ANNUAL_FEE = 40;
	
	private int year;
	private Type type;
	private Date paiementDate;
	private int amount;
	private String company;
	private Event event;
	private Member member;
	
	/** Available memberships types */
	public enum Type { PERSONNAL, SPONSORED }
	
	/**
	 * Default constructor
	 */
	public Membership(){
	}
	
	public Membership(String key) {
		super(key);
	}
	
	public Membership(String key, int year) {
		super(key);
		this.year = year;
	}

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
		if(this.year <= 0 && this.paiementDate != null){
			this.year = Integer.parseInt(new SimpleDateFormat("yyyy").format(this.paiementDate));
		}
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
		if(this.event != null && this.event.getDate() != null){
			this.paiementDate = this.event.getDate();
		}
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

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Membership [key = " + key + ", year=" + year + ", paiementDate=" + paiementDate
				+ ", amount=" + amount + ", event=" + event +  ", member=" + member
				+ ", type=" + type + ", company=" + company + "]";
	}
	
	
}
