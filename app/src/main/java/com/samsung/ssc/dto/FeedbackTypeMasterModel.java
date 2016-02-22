package com.samsung.ssc.dto;

public class FeedbackTypeMasterModel {

	private int feedbackTypeID, feedbackCatID;
	private String feedbackTypeName;
	private String sampleImageName;
	private boolean isSelected;

	public boolean isSelected() {
		return isSelected;
	}

	public String getSampleImageName() {
		return sampleImageName;
	}

	public void setSampleImageName(String sampleImageName) {
		this.sampleImageName = sampleImageName;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public int getFeedbackTypeID() {
		return feedbackTypeID;
	}

	public void setFeedbackTypeID(int feedbackTypeID) {
		this.feedbackTypeID = feedbackTypeID;
	}

	public int getFeedbackCatID() {
		return feedbackCatID;
	}

	public void setFeedbackCatID(int feedbackCatID) {
		this.feedbackCatID = feedbackCatID;
	}

	public String getFeedbackTypeName() {
		return feedbackTypeName;
	}

	public void setFeedbackTypeName(String feedbackTypeName) {
		this.feedbackTypeName = feedbackTypeName;
	}
	
	

}
