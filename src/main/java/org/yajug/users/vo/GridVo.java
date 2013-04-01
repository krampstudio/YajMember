package org.yajug.users.vo;

import java.util.List;

import org.yajug.users.domain.Member;

/**
 * Value Object : special bean format according to the widget grid.
 * 
 * @author Bertrand Chevrier <bertrand.chevrier@yajug.org>
 */
public class GridVo {

	private List<Member> list;
	private int total;
	
	/**
	 * Build it from a list of members
	 * @param members the members' list
	 */
	public GridVo(List<Member> members, int total) {
		if(members != null){
			this.list = members;
		}
		this.total = total;
	}
	
	/**
	 * @return the list
	 */
	public List<Member> getList() {
		return list;
	}
	
	/**
	 * @param list the list to set
	 */
	public void setList(List<Member> list) {
		this.list = list;
	}
	
	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}
	
	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}
}
