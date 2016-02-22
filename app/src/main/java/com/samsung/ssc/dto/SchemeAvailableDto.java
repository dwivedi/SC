package com.samsung.ssc.dto;

public class SchemeAvailableDto {
	private boolean success;
	private String result;
	private String singleResult;
	private String statusCode;
	private String message;
	private String description;
	private String schemeExpiryDate;
	private String schemeStartDate;
	private String title;
	private boolean active;
	private String schemeID;
	public String getSchemeID() {
		return schemeID;
	}
	public void setSchemeID(String schemeID) {
		this.schemeID = schemeID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSchemeStartDate() {
		return schemeStartDate;
	}
	public void setSchemeStartDate(String schemeStartDate) {
		this.schemeStartDate = schemeStartDate;
	}
	public String getSchemeExpiryDate() {
		return schemeExpiryDate;
	}
	public void setSchemeExpiryDate(String schemeExpiryDate) {
		this.schemeExpiryDate = schemeExpiryDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getSingleResult() {
		return singleResult;
	}
	public void setSingleResult(String singleResult) {
		this.singleResult = singleResult;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
