package com.samsung.ssc.dto;

public class PaymentModes {

	private boolean success;
	private String failedValidations;
	private String message;
	private String modeName;
	private String paymentModeID;

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getFailedValidations() {
		return failedValidations;
	}

	public void setFailedValidations(String failedValidations) {
		this.failedValidations = failedValidations;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getModeName() {
		return modeName;
	}

	public void setModeName(String modeName) {
		this.modeName = modeName;
	}

	public String getPaymentModeID() {
		return paymentModeID;
	}

	public void setPaymentModeID(String paymentModeID) {
		this.paymentModeID = paymentModeID;
	}

}
