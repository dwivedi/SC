package com.samsung.ssc.dto;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class GetKASResponse {

	@SerializedName("FailedValidations")
	private String failedValidations;

	@SerializedName("IsSuccess")
	private boolean isSuccess;

	@SerializedName("Message")
	private String message;

	@SerializedName("Result")
	private ArrayList<KPIData> kasDatas = new ArrayList<KPIData>();

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

	public ArrayList<KPIData> getNotificationDatas() {
		return kasDatas;
	}

	public void setNotificationDatas(ArrayList<KPIData> notificationDatas) {
		this.kasDatas = notificationDatas;
	}
}
