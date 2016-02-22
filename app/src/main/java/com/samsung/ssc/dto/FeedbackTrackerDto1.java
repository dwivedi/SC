package com.samsung.ssc.dto;

public class FeedbackTrackerDto1 {
	
	
	public long getFeedbackID() {
		return FeedbackID;
	}
	
	
	public void setFeedbackID(long feedbackID) {
		FeedbackID = feedbackID;
	}
	public int getFeedbackTeamID() {
		return FeedbackTeamID;
	}
	public void setFeedbackTeamID(int feedbackTeamID) {
		FeedbackTeamID = feedbackTeamID;
	}
	public int getFeedbackCatID() {
		return FeedbackCatID;
	}
	public void setFeedbackCatID(int feedbackCatID) {
		FeedbackCatID = feedbackCatID;
	}
	public int getFeedbackTypeID() {
		return FeedbackTypeID;
	}
	public void setFeedbackTypeID(int feedbackTypeID) {
		FeedbackTypeID = feedbackTypeID;
	}
	public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
	}
	public int getSpocID() {
		return SpocID;
	}
	public void setSpocID(int spocID) {
		SpocID = spocID;
	}
	public String getCreatedBy() {
		return CreatedBy;
	}
	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}
	public String getCreatedOn() {
		return CreatedOn;
	}
	public void setCreatedOn(String createdOn) {
		CreatedOn = createdOn;
	}
	public int getPendingSince() {
		return PendingSince;
	}
	public void setPendingSince(int pendingSince) {
		PendingSince = pendingSince;
	}
	public String getRemarks() {
		return Remarks;
	}
	public void setRemarks(String remarks) {
		Remarks = remarks;
	}
	public int getCurrentStatusID() {
		return CurrentStatusID;
	}
	public void setCurrentStatusID(int currentStatusID) {
		CurrentStatusID = currentStatusID;
	}
	public String getLastUpdatedOn() {
		return LastUpdatedOn;
	}
	public void setLastUpdatedOn(String lastUpdatedOn) {
		LastUpdatedOn = lastUpdatedOn;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public String getTeamName() {
		return TeamName;
	}


	public void setTeamName(String teamName) {
		TeamName = teamName;
	}
	public String getFeedbackCategoryName() {
		return FeedbackCategoryName;
	}


	public void setFeedbackCategoryName(String feedbackCategoryName) {
		FeedbackCategoryName = feedbackCategoryName;
	}
	public String getFeedbackTypeName() {
		return FeedbackTypeName;
	}


	public void setFeedbackTypeName(String feedbackTypeName) {
		FeedbackTypeName = feedbackTypeName;
	}
	private long FeedbackID;

	private int FeedbackTeamID ;
	private String TeamName;
	private int FeedbackCatID ;
	private String FeedbackCategoryName;
	
	private int FeedbackTypeID ;
	private String FeedbackTypeName;
	
	private int UserID;
	private int SpocID ;
	private String CreatedBy ;
	private String CreatedOn  ;
	private int PendingSince  ;
	private String Remarks  ;
	private int CurrentStatusID;
	private String LastUpdatedOn;
	
	private boolean isChecked;


	
	
	

}
