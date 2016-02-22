package com.samsung.ssc.dto;

public class UserFeedback {

	private int feedbackCatID, feedbackTypeID, teamID,storeId;
	private String remark, imagePath;




	public int getFeedbackCatID() {
		return feedbackCatID;
	}

	public void setFeedbackCatID(int feedbackCatID) {
		this.feedbackCatID = feedbackCatID;
	}
 
	public int getFeedbackTypeID() {
		return feedbackTypeID;
	}

	public void setFeedbackTypeID(int feedbackTypeID) {
		this.feedbackTypeID = feedbackTypeID;
	}

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

}
