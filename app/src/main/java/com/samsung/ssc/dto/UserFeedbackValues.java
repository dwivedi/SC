package com.samsung.ssc.dto;

public class UserFeedbackValues {

	private long rowId;
	private String remark,teamName,feedbackCatName,feedbackTypeName;
	

	

	public long getRowId() {
		return rowId;
	}

	public void setRowId(long rowId) {
		this.rowId = rowId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getFeedbackCatName() {
		return feedbackCatName;
	}

	public void setFeedbackCatName(String feedbackCatName) {
		this.feedbackCatName = feedbackCatName;
	}

	public String getFeedbackTypeName() {
		return feedbackTypeName;
	}

	public void setFeedbackTypeName(String feedbackTypeName) {
		this.feedbackTypeName = feedbackTypeName;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}



}
