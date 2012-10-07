package org.yajug.users.vo;

import java.util.List;

import org.yajug.users.domain.Member;

public class GridVo {

	private List<Member> list;
	private int total;
	
	public GridVo(List<Member> list) {
		this.list = list;
		this.total = list.size();
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
