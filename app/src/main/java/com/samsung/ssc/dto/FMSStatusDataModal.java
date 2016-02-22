package com.samsung.ssc.dto;

public class FMSStatusDataModal {
	
	public FMSStatusDataModal(int statusId, String statusName) {

		this.stausId = statusId;
		this.statusName = statusName;
	
	}
	public int getStausId() {
		return stausId;
	}
	public void setStausId(int stausId) {
		this.stausId = stausId;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	 
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	private int stausId;
	private String statusName;
	private boolean isSelected;
	 
}
