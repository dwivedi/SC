package com.samsung.ssc.dto;


import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ResponseTypeDto<T> {
	
	@SerializedName("FailedValidations")
	private String failedValidations;

	@SerializedName("IsSuccess")
	private boolean success;
	
	@SerializedName("Result")
	public ArrayList<T> result;
	
	@SerializedName("SingleResult")
	public T singleResult;
	
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

	public ArrayList<T> getResult() {
		return result;
	}

	public void setResult(ArrayList<T> result) {
		this.result = result;
	}

	public T getSingleResult() {
		return singleResult;
	}

	public void setSingleResult(T singleResult) {
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
