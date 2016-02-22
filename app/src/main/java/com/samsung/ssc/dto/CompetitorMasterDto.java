package com.samsung.ssc.dto;

import java.util.ArrayList;

/**
 * 
 * @author vasingh
 *
 */
public class CompetitorMasterDto {

	private String failedValidations;
	private boolean success;
	private String message;
	private String result;
	private int statusCode;
	private ArrayList<CompetitosrList> competitors = new ArrayList<CompetitosrList>();

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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public ArrayList<CompetitosrList> getCompetitors() {
		return competitors;
	}

	public void setCompetitors(ArrayList<CompetitosrList> competitors) {
		this.competitors = competitors;
	}

}
