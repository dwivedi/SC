package com.samsung.ssc.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ResponseDto implements Parcelable {

	private String failedValidations;
	private boolean success;
	private String message;
	private String result;
	private String singleResult;
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
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public ResponseDto(Parcel source) {
		
		failedValidations = source.readString();
		success = source.readByte()==1?true:false;
		message  = source.readString();
		result  = source.readString();
		singleResult  = source.readString();
		statusCode  = source.readString();
		 
	}
	
	public ResponseDto() {
 	}
	
	
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(failedValidations);
		dest.writeByte((byte) (success ? 1 : 0));
		dest.writeString(message);
		dest.writeString(result);
		dest.writeString(singleResult);
		dest.writeString(statusCode);
		 
		
		
		
	}
	

	
	public static final Parcelable.Creator<ResponseDto> CREATOR = new Creator<ResponseDto>() {

		@Override
		public ResponseDto[] newArray(int size) {
			return new ResponseDto[size];
		}

		@Override
		public ResponseDto createFromParcel(Parcel source) {
			return new ResponseDto(source);
		}
	};
}
