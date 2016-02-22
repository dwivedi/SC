package com.samsung.ssc.dto;

import java.io.Serializable;

public class ActivityDataMasterModel  implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 6400095510349650744L;
	private long activityID;
	private int userID;
	private int storeID;
	private String storeName;
	//private int moduleID;
	private int moduleCode;
	private String moduleName;
	private long coverageID;
	private String createdDate;
	private String modifiedDate;
	private int syncStatus;
	private int surveyResponseID;
	private boolean isQuestionModule;
	
	private String assessmentStartTime;
	private String assessmentEndTime;
	
	


	

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		
		this.userID = userID;
	}

	public int getStoreID() {
		return storeID;
	}

	public void setStoreID(int storeID) {
		this.storeID = storeID;
	}

	/*public int getModuleID() {
		return moduleID;
	}

	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}
*/
	public long getCoverageID() {
		return coverageID;
	}

	public void setCoverageID(long coverageID) {
		this.coverageID = coverageID;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(int syncStatus) {
		this.syncStatus = syncStatus;
	}

	public int getSurveyResponseID() {
		return surveyResponseID;
	}

	public void setSurveyResponseID(int surveyResponseID) {
		this.surveyResponseID = surveyResponseID;
	}

	public long getActivityID() {
		return activityID;
	}

	public void setActivityID(int activityID) {
		this.activityID = activityID;
	}

	public int getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(int moduleCode) {
		this.moduleCode = moduleCode;
	}

	public boolean isQuestionModule() {
		return isQuestionModule;
	}

	public void setQuestionModule(boolean isQuestionModule) {
		this.isQuestionModule = isQuestionModule;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getAssessmentStartTime() {
		return assessmentStartTime;
	}

	public void setAssessmentStartTime(String assessmentStartTime) {
		this.assessmentStartTime = assessmentStartTime;
	}

	public String getAssessmentEndTime() {
		return assessmentEndTime;
	}

	public void setAssessmentEndTime(String assessmentEndTime) {
		this.assessmentEndTime = assessmentEndTime;
	}


}
