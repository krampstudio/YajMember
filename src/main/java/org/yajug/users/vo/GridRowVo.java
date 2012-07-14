package org.yajug.users.vo;

import java.util.Map;

public class GridRowVo {

	private String id;
	private Map<String, String> cell;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the cell
	 */
	public Map<String, String> getCell() {
		return cell;
	}
	/**
	 * @param cell the cell to set
	 */
	public void setCell(Map<String, String> cell) {
		this.cell = cell;
	}
	
	
}
