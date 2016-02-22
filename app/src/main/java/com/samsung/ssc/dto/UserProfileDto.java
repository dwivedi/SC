package com.samsung.ssc.dto;

public class UserProfileDto {

	private boolean success;
	private String message;
	private String accountStatus;
	private String address;
	private String emailID;
	private String emplCode;
	private String isOfflineProfile;
	private String profilePictureFileName;
	private String statusCode;
	private String alternateEmailID;
	private String firstName;
	private String lastName;
	private String mobileCalling;
	private String mobileSD;
	private String pincode;
	private int userRoleid;
	private String roamingProfile;
	


	public String getRoamingProfile() {
		return roamingProfile;
	}

	public void setRoamingProfile(String roamingProfile) {
		this.roamingProfile = roamingProfile;
	}

	public int getUserRoleid() {
		return userRoleid;
	}

	public void setUserRoleid(int userRoleid) {
		this.userRoleid = userRoleid;
	}

	private String singleResult;
	

	public String getSingleResult() {
		return singleResult;
	}

	public void setSingleResult(String singleResult) {
		this.singleResult = singleResult;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getEmplCode() {
		return emplCode;
	}

	public void setEmplCode(String emplCode) {
		this.emplCode = emplCode;
	}

	public String getIsOfflineProfile() {
		return isOfflineProfile;
	}

	public void setIsOfflineProfile(String isOfflineProfile) {
		this.isOfflineProfile = isOfflineProfile;
	}

	public String getAlternateEmailID() {
		return alternateEmailID;
	}

	public void setAlternateEmailID(String alternateEmailID) {
		this.alternateEmailID = alternateEmailID;
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

	public String getMobileSD() {
		return mobileSD;
	}

	public void setMobileSD(String mobileSD) {
		this.mobileSD = mobileSD;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getProfilePictureFileName() {
		return profilePictureFileName;
	}

	public void setProfilePictureFileName(String profilePictureFileName) {
		this.profilePictureFileName = profilePictureFileName;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

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

}
