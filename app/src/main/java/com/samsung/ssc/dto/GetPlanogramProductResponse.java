package com.samsung.ssc.dto;

import com.google.gson.annotations.SerializedName;

public class GetPlanogramProductResponse {

	@SerializedName("FailedValidations")
	private String failedValidations;

	@SerializedName("IsSuccess")
	private boolean success;

	@SerializedName("SingleResult")
	public GetPlanogramProductMasterSingleResult masterSingleResult;

	@SerializedName("StatusCode")
	public String statusCode;

	@SerializedName("Message")
	private String message;

	public String getFailedValidations() {
		return failedValidations;
	}

	public void setFailedValidations(String failedValidations) {
		this.failedValidations = failedValidations;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public GetPlanogramProductMasterSingleResult getMasterSingleResult() {
		return masterSingleResult;
	}

	public void setMasterSingleResult(
			GetPlanogramProductMasterSingleResult masterSingleResult) {
		this.masterSingleResult = masterSingleResult;
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
