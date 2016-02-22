package com.samsung.ssc.dto;

import java.util.ArrayList;

public class ProductMasterDto {

	private int statusCode;
	private String failedValidations;
	private boolean success;
	private String message;
	private ArrayList<ProductMasterItems> productMaster = new ArrayList<ProductMasterItems>();
	
	
	public ArrayList<ProductMasterItems> getProductMaster() {
		return productMaster;
	}

	public void setProductMaster(ArrayList<ProductMasterItems> productMaster) {
		this.productMaster = productMaster;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getFailedValidations() {
		return failedValidations;
	}

	public void setFailedValidations(String failedValidations) {
		this.failedValidations = failedValidations;
	}

	public boolean getSuccess() {
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
