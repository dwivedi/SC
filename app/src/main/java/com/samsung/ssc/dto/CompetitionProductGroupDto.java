package com.samsung.ssc.dto;
public class CompetitionProductGroupDto {

	private String failedValidations;

	private boolean success;
	private String message;
	private String result;
	private String compProductGroupID;
	private String productGroupCode;
	private String productGroupName;

	public boolean isSuccess() {
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	 public String getCompProductGroupID() {
		return compProductGroupID;
	}

	public void setCompProductGroupID(String compProductGroupID) {
		this.compProductGroupID = compProductGroupID;
	}

	public String getProductGroupCode() {
		return productGroupCode;
	}

	public void setProductGroupCode(String productGroupCode) {
		this.productGroupCode = productGroupCode;
	}

	public String getProductGroupName() {
		return productGroupName;
	}

	public void setProductGroupName(String productGroupName) {
		this.productGroupName = productGroupName;
	}

}
