package com.samsung.ssc.dto;

public class BeatCoverageDto {
	private String failedValidations;
	
	private boolean success;
	private String message;
	private String result;
	private SingleResult singleResult;
	private String statusCode;
	
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
	public SingleResult getSingleResult() {
		return singleResult;
	}
	public void setSingleResult(SingleResult singleResult) {
		this.singleResult = singleResult;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	
	
	public class SingleResult {
		private String coverageDate;
		private boolean exceptionFlag;
		private boolean isCurrentMonth;

		public String getCoverageDate() {
			return coverageDate;
		}

		public void setCoverageDate(String coverageDate) {
			this.coverageDate = coverageDate;
		}

		public boolean isExceptionFlag() {
			return exceptionFlag;
		}

		public void setExceptionFlag(boolean exceptionFlag) {
			this.exceptionFlag = exceptionFlag;
		}

		public boolean isCurrentMonth() {
			return isCurrentMonth;
		}

		public void setCurrentMonth(boolean isCurrentMonth) {
			this.isCurrentMonth = isCurrentMonth;
		}

	}

	

}
