package org.yajug.users.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This domain pojo represent a annual membership paid by a member.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
//	name="Membership.getAllByMember", 
//	query="select ms from Membership ms where ms.member = :member"
public class MemberShip  extends DomainObject {

	public static final int ANNUAL_FEE = 40;
	private int year;
	private Date paiementDate;
	private int amount;
	private Event event;
	private Member member;
	
	/**
	 * Default constructor
	 */
	public MemberShip(){
	}
	
	public MemberShip(long key) {
		super(key);
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Membership [year=" + year + ", paiementDate=" + paiementDate
				+ ", amount=" + amount + ", event=" + event + "]";
	}
	
	
}
