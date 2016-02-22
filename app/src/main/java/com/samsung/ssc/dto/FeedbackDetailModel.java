package com.samsung.ssc.dto;

import java.util.ArrayList;

public class FeedbackDetailModel {

	private int feedbackCatID, feedbackTypeID, teamID, feedbackUserID, spocID,currentFeedbackStatusID;
	

	public int getFeedbackUserID() {
		return feedbackUserID;
	}

	public void setFeedbackUserID(int feedbackUserID) {
		this.feedbackUserID = feedbackUserID;
	}

	public int getSpocID() {
		return spocID;
	}

	public void setSpocID(int spocID) {
		this.spocID = spocID;
	}

	private double timeTaken;
	private String feedbackNumber;
	private String feedbackImageURL;
	private ArrayList<FeedbackLogModel> feedbackLogs = new ArrayList<FeedbackLogModel>();

	public ArrayList<FeedbackLogModel> getFeedbackLogs() {
		return feedbackLogs;
	}

	public void setFeedbackLogs(ArrayList<FeedbackLogModel> feedbackLogs) {
		this.feedbackLogs = feedbackLogs;
	}

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

	public double getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(double timeTaken) {
		this.timeTaken = timeTaken;
	}

	public String getFeedbackNumber() {
		return feedbackNumber;
	}

	public void setFeedbackNumber(String feedbackNumber) {
		this.feedbackNumber = feedbackNumber;
	}

	public String getFeedbackImageURL() {
		return feedbackImageURL;
	}

	public void setFeedbackImageURL(String feedbackImageURL) {
		this.feedbackImageURL = feedbackImageURL;
	}

	public int getCurrentFeedbackStatusID() {
		return currentFeedbackStatusID;
	}

	public void setCurrentFeedbackStatusID(int currentFeedbackStatusID) {
		this.currentFeedbackStatusID = currentFeedbackStatusID;
	}

}
