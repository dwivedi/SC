package com.samsung.ssc.dto;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class GetNotificationResponse {

	@SerializedName("StatusCode")
	private String statusCode;

	@SerializedName("FailedValidations")
	private String failedValidations;

	@SerializedName("IsSuccess")
	private boolean isSuccess;

	@SerializedName("Message")
	private String message;

	@SerializedName("Result")
	private ArrayList<NotificationData> notificationDatas = new ArrayList<NotificationData>();

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getFailedValidations() {
		return failedValidations;
	}

	public void setFailedValidations(String failedValidations) {
		this.failedValidations = failedValidations;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ArrayList<NotificationData> getNotificationDatas() {
		return notificationDatas;
	}

	public void setNotificationDatas(
			ArrayList<NotificationData> notificationDatas) {
		this.notificationDatas = notificationDatas;
	}
}
