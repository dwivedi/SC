package com.samsung.ssc.dto;

import java.util.HashMap;

public class FeedbackLogModel {

	private String createdBy, createdOn, pendingWith, remarks, responseDate;
	private int feedbackStatusID;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getPendingWith() {
		return pendingWith;
	}

	public void setPendingWith(String pendingWith) {
		this.pendingWith = pendingWith;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "createdBy:     " + createdBy + "\ncreatedOn:     "
				+ createdOn + "\npendingWith:     " + pendingWith + "\nremarks:     "
				+ remarks + "\nresponseDate:    " + responseDate + "\nstatusID:     "
				+ feedbackStatusID + "";
	}

	public String getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(String responseDate) {
		this.responseDate = responseDate;
	}

	public int getStatusID() {
		return feedbackStatusID;
	}

	public void setStatusID(int statusID) {
		this.feedbackStatusID = statusID;
	}
	
	
	
	public HashMap<String, String> getValueMap() {

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("By", createdBy);
		map.put("On", createdOn);
		map.put("Pending With", pendingWith);
		map.put("ETR Date", responseDate);
		//map.put("Remark", remarks);
			
		return map;
		
	}
	

}
