package com.samsung.ssc.dto;

import java.io.Serializable;

public class LoginResponse implements Serializable {
	private String apiKey;
	private String apiToken;
	private String apkURL;
	private String announcementMessage;
	private String attendanceType;
	private String companyId;
	private String employeeCode;
	private boolean isAttendanceMarked;
	private boolean isAttendanceMandate=true;
	private boolean isGeoFencingApplicable;
	
	private int roleId;
	

	private String userId;
	
	
	private boolean isApkUpdated;
	private boolean hasNewAnnouncment;
	private boolean isOfflineAccess;
	private boolean isRaceProfile;
	private boolean showPerformanceTab;
	private boolean isStoreProfileVisible;
	
	
	private int userRoleID;
	private String firstName;
	private String lastName;
	private String mobileCalling;
	private boolean isRoamingProfile;

	
	
	public boolean isAttendanceMandate() {
		return isAttendanceMandate;
	}

	public void setAttendanceMandate(boolean isAttendanceMandate) {
		this.isAttendanceMandate = isAttendanceMandate;
	}

	
	public int getUserRoleID() {
		return userRoleID;
	}

	public void setUserRoleID(int userRoleID) {
		this.userRoleID = userRoleID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileCalling() {
		return mobileCalling;
	}

	public void setMobileCalling(String mobileCalling) {
		this.mobileCalling = mobileCalling;
	}

	public boolean isRoamingProfile() {
		return isRoamingProfile;
	}

	public void setRoamingProfile(boolean isRoamingProfile) {
		this.isRoamingProfile = isRoamingProfile;
	}

	public boolean isStoreProfileVisible() {
		return isStoreProfileVisible;
	}

	public void setStoreProfileVisible(boolean isStoreProfileVisible) {
		this.isStoreProfileVisible = isStoreProfileVisible;
	}

	public boolean isApkUpdated() {
		return isApkUpdated;
	}

	public void setApkUpdated(boolean isApkUpdated) {
		this.isApkUpdated = isApkUpdated;
	}

	public boolean isHasNewAnnouncment() {
		return hasNewAnnouncment;
	}

	public void setHasNewAnnouncment(boolean hasNewAnnouncment) {
		this.hasNewAnnouncment = hasNewAnnouncment;
	}

	public boolean isOfflineAccess() {
		return isOfflineAccess;
	}

	public void setOfflineAccess(boolean isOfflineAccess) {
		this.isOfflineAccess = isOfflineAccess;
	}

	public boolean isRaceProfile() {
		return isRaceProfile;
	}

	public void setRaceProfile(boolean isRaceProfile) {
		this.isRaceProfile = isRaceProfile;
	}

	public boolean isShowPerformanceTab() {
		return showPerformanceTab;
	}

	public void setShowPerformanceTab(boolean showPerformanceTab) {
		this.showPerformanceTab = showPerformanceTab;
	}

	private boolean geoPhotoMandate;
	private boolean geoTagMandate;
 	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiToken() {
		return apiToken;
	}

	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}

	public String getApkURL() {
		return apkURL;
	}

	public void setApkURL(String apkURL) {
		this.apkURL = apkURL;
	}

	public String getAnnouncementMessage() {
		return announcementMessage;
	}

	public void setAnnouncementMessage(String announcementMessage) {
		this.announcementMessage = announcementMessage;
	}

	public String getAttendanceType() {
		return attendanceType;
	}

	public void setAttendanceType(String attendanceType) {
		this.attendanceType = attendanceType;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public boolean isAttendanceMarked() {
		return isAttendanceMarked;
	}

	public void setAttendanceMarked(boolean isAttendanceMarked) {
		this.isAttendanceMarked = isAttendanceMarked;
	}

	

	

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int userRoleId) {
		this.roleId = userRoleId;
	}

	

	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	

	public boolean isGeoPhotoMandate() {
		return geoPhotoMandate;
	}

	public void setGeoPhotoMandate(boolean geoPhotoMandate) {
		this.geoPhotoMandate = geoPhotoMandate;
	}

	public boolean isGeoTagMandate() {
		return geoTagMandate;
	}

	public void setGeoTagMandate(boolean geoTagMandate) {
		this.geoTagMandate = geoTagMandate;
	}

	 
	public boolean isGeoFencingApplicable() {
		return isGeoFencingApplicable;
	}

	public void setGeoFencingApplicable(boolean isGeoFencingApplicable) {
		this.isGeoFencingApplicable = isGeoFencingApplicable;
	}
	
	
	
}
