package com.samsung.ssc.dto;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class UploadImageMultipartDataModal implements Parcelable {

	private int type;
	private int surveyResponseId;
	private int surveyQuestionId;
	private long userId;
	private String userResponse;
	private String userOption;
	private double latitude;
	private double longitude;
	private int storeID;

	private List<UploadMuliplePartImageRepeaterDataModal> arrayListMulitpleRepeater = new ArrayList<UploadMuliplePartImageRepeaterDataModal>();

	public UploadImageMultipartDataModal(Parcel in) {

		this.type = in.readInt();
		this.surveyResponseId = in.readInt();
		this.surveyQuestionId = in.readInt();
		this.userId = in.readLong();
		this.userResponse = in.readString();
		this.userOption = in.readString();
		this.latitude = in.readDouble();
		this.longitude = in.readDouble();
		this.storeID = in.readInt();
		
		in.readTypedList(arrayListMulitpleRepeater, UploadMuliplePartImageRepeaterDataModal.CREATOR);

	}

	public List<UploadMuliplePartImageRepeaterDataModal> getArrList() {
		return arrayListMulitpleRepeater;
	}

	public void setArrList(
			List<UploadMuliplePartImageRepeaterDataModal> arrayListMulitpleRepeater) {
		this.arrayListMulitpleRepeater = arrayListMulitpleRepeater;
	}

	public UploadImageMultipartDataModal() {

		this.arrayListMulitpleRepeater = new ArrayList<UploadMuliplePartImageRepeaterDataModal>();
	}

	public int getStoreID() {
		return storeID;
	}

	public void setStoreID(int storeID) {
		this.storeID = storeID;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getUserOption() {
		return userOption;
	}

	public void setUserOption(String userOption) {
		this.userOption = userOption;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSurveyResponseId() {
		return surveyResponseId;
	}

	public void setSurveyResponseId(int surveyResponseId) {
		this.surveyResponseId = surveyResponseId;
	}

	public int getSurveyQuestionId() {
		return surveyQuestionId;
	}

	public void setSurveyQuestionId(int surveyQuestionId) {
		this.surveyQuestionId = surveyQuestionId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserResponse() {
		return userResponse;
	}

	public void setUserResponse(String imagePath) {
		this.userResponse = imagePath;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(type);
		dest.writeInt(surveyResponseId);
		dest.writeInt(surveyQuestionId);
		dest.writeLong(userId);
		dest.writeString(userResponse);
		dest.writeString(userOption);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeInt(storeID);
		dest.writeTypedList(arrayListMulitpleRepeater);

	}

	public static final Parcelable.Creator<UploadImageMultipartDataModal> CREATOR = new Creator<UploadImageMultipartDataModal>() {

		@Override
		public UploadImageMultipartDataModal[] newArray(int size) {
			return new UploadImageMultipartDataModal[size];
		}

		@Override
		public UploadImageMultipartDataModal createFromParcel(Parcel source) {
			return new UploadImageMultipartDataModal(source);
		}
	};
	
	
	

}
