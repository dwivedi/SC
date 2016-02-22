package com.samsung.ssc.dto;

import java.util.ArrayList;

public class UserBeatDetailDto {

	private boolean success;
	private String message;
	private String dateRange;
	private String planMonth;
	private String statusID;
	private String storeCode;
	private String storeID;
	private String storeName;
	private String userID;
	private String city;
	private String totalOutletPlanned;
	private String totalWorkingDays;
	private String totalOff;
	private String day;
	private String leaveDetail;
	private String totalAssignedOutlet;
	private ArrayList<String> storeList = new ArrayList<String>();
	private String coverageDate; 
	

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDateRange() {
		return dateRange;
	}

	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}

	public String getPlanMonth() {
		return planMonth;
	}

	public void setPlanMonth(String planMonth) {
		this.planMonth = planMonth;
	}

	public String getStatusID() {
		return statusID;
	}

	public void setStatusID(String statusID) {
		this.statusID = statusID;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getStoreID() {
		return storeID;
	}

	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the totalOutletPlanned
	 */
	public String getTotalOutletPlanned() {
		return totalOutletPlanned;
	}

	/**
	 * @param totalOutletPlanned the totalOutletPlanned to set
	 */
	public void setTotalOutletPlanned(String totalOutletPlanned) {
		this.totalOutletPlanned = totalOutletPlanned;
	}

	/**
	 * @return the totalWorkingDays
	 */
	public String getTotalWorkingDays() {
		return totalWorkingDays;
	}

	/**
	 * @param totalWorkingDays the totalWorkingDays to set
	 */
	public void setTotalWorkingDays(String totalWorkingDays) {
		this.totalWorkingDays = totalWorkingDays;
	}

	/**
	 * @return the totalOff
	 */
	public String getTotalOff() {
		return totalOff;
	}

	/**
	 * @param totalOff the totalOff to set
	 */
	public void setTotalOff(String totalOff) {
		this.totalOff = totalOff;
	}

	/**
	 * @return the day
	 */
	public String getDay() {
		return day;
	}

	/**
	 * @param day the day to set
	 */
	public void setDay(String day) {
		this.day = day;
	}

	/**
	 * @return the leaveDetail
	 */
	public String getLeaveDetail() {
		return leaveDetail;
	}

	/**
	 * @param leaveDetail the leaveDetail to set
	 */
	public void setLeaveDetail(String leaveDetail) {
		this.leaveDetail = leaveDetail;
	}

	/**
	 * @return the totalAssignedOutlet
	 */
	public String getTotalAssignedOutlet() {
		return totalAssignedOutlet;
	}

	/**
	 * @param totalAssignedOutlet the totalAssignedOutlet to set
	 */
	public void setTotalAssignedOutlet(String totalAssignedOutlet) {
		this.totalAssignedOutlet = totalAssignedOutlet;
	}

	/**
	 * @return the storeList
	 */
	public ArrayList<String> getStoreList() {
		return storeList;
	}

	/**
	 * @param storeList the storeList to set
	 */
	public void setStoreList(ArrayList<String> storeList) {
		this.storeList = storeList;
	}

	/**
	 * @return the coverageDate
	 */
	public String getCoverageDate() {
		return coverageDate;
	}

	/**
	 * @param coverageDate the coverageDate to set
	 */
	public void setCoverageDate(String coverageDate) {
		this.coverageDate = coverageDate;
	}

}
