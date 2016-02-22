package com.samsung.ssc.dto;

import com.google.gson.annotations.SerializedName;

public class DealerHistoryDataDto {
	
	@SerializedName("NAMEOFFIRM")
	private String nameoffirm;

	@SerializedName("CITYTOWN")
	private String citytown;
	
	@SerializedName("NAMEOFOWNER")
	private String ownerName;
	
	@SerializedName("MOBILEOFOWNER")
	private String ownerMob;
	
	@SerializedName("APPROVALSTATUS")
	private int approvalstatus;

	public String getNameoffirm() {
		return nameoffirm;
	}

	public void setNameoffirm(String nameoffirm) {
		this.nameoffirm = nameoffirm;
	}

	public String getCitytown() {
		return citytown;
	}

	public void setCitytown(String citytown) {
		this.citytown = citytown;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerMob() {
		return ownerMob;
	}

	public void setOwnerMob(String ownerMob) {
		this.ownerMob = ownerMob;
	}

	public int getApprovalstatus() {
		return approvalstatus;
	}

	public void setApprovalstatus(int approvalstatus) {
		this.approvalstatus = approvalstatus;
	}



}
