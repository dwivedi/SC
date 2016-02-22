package com.samsung.ssc.dto;

public class FeedbackCategoryMasterModel {

	private int feedbackCatID;
	private String feedbackCatName;
	private int teamID;

	public int getFeedbackCatID() {
		return feedbackCatID;
	}

	public void setFeedbackCatID(int feedbackCatID) {
		this.feedbackCatID = feedbackCatID;
	}

	public String getFeedbackCatName() {
		return feedbackCatName;
	}

	public void setFeedbackCatName(String feedbackCatName) {
		this.feedbackCatName = feedbackCatName;
	}

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}



	private boolean isSelected;
}
