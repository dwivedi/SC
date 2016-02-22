package com.samsung.ssc.dto;

import com.google.gson.annotations.SerializedName;

public class FeedbackDetailsDTO {

	@SerializedName("FeedbackNumber")
	private String feedbackNumber;

	@SerializedName("TeamIDs")
	private String teamIDs;

	@SerializedName("FeedbackCatIDs")
	private String feedbackCatIDs;

	@SerializedName("FeedbackTypeIDs")
	private String feedbackTypeIDs;

	@SerializedName("TimeTaken")
	private String timeTaken;

	@SerializedName("StatusID")
	private String StatusID;

	@SerializedName("responseDate")
	private String ResponseDate;

	@SerializedName("Remarks")
	private String remarks;

	@SerializedName("CreatedBy")
	private String createdBy;

	@SerializedName("Createdon")
	private String createdon;

	@SerializedName("PendingWith")
	private String pendingWith;

	public String getFeedbackNumber() {
		return feedbackNumber;
	}

	public void setFeedbackNumber(String feedbackNumber) {
		this.feedbackNumber = feedbackNumber;
	}

	public String getTeamIDs() {
		return teamIDs;
	}

	public void setTeamIDs(String teamIDs) {
		this.teamIDs = teamIDs;
	}

	public String getFeedbackCatIDs() {
		return feedbackCatIDs;
	}

	public void setFeedbackCatIDs(String feedbackCatIDs) {
		this.feedbackCatIDs = feedbackCatIDs;
	}

	public String getFeedbackTypeIDs() {
		return feedbackTypeIDs;
	}

	public void setFeedbackTypeIDs(String feedbackTypeIDs) {
		this.feedbackTypeIDs = feedbackTypeIDs;
	}

	public String getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(String timeTaken) {
		this.timeTaken = timeTaken;
	}

	public String getStatusID() {
		return StatusID;
	}

	public void setStatusID(String statusID) {
		StatusID = statusID;
	}

	public String getResponseDate() {
		return ResponseDate;
	}

	public void setResponseDate(String responseDate) {
		ResponseDate = responseDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedon() {
		return createdon;
	}

	public void setCreatedon(String createdon) {
		this.createdon = createdon;
	}

	public String getPendingWith() {
		return pendingWith;
	}

	public void setPendingWith(String pendingWith) {
		this.pendingWith = pendingWith;
	}
}
