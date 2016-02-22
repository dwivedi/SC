package com.samsung.ssc.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class UploadMuliplePartImageRepeaterDataModal  implements Parcelable {
	
	
	private String imagePath;
	private String imageSubID;
	
	
	public UploadMuliplePartImageRepeaterDataModal() {
	}
	
	
	public UploadMuliplePartImageRepeaterDataModal(Parcel in) {
		this.imagePath = in.readString();
		this.imageSubID = in.readString();
		
	}


	public String getImagePath() {
		return imagePath;
	}


	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}


	public String getImageSubID() {
		return imageSubID;
	}


	public void setImageSubID(String imageSubID) {
		this.imageSubID = imageSubID;
	}
	

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
 
		dest.writeString(imagePath);
		dest.writeString(imageSubID);
		 

	}
	
	public static final Parcelable.Creator<UploadMuliplePartImageRepeaterDataModal> CREATOR = new Creator<UploadMuliplePartImageRepeaterDataModal>() {

		@Override
		public UploadMuliplePartImageRepeaterDataModal[] newArray(int size) {
			return new UploadMuliplePartImageRepeaterDataModal[size];
		}

		@Override
		public UploadMuliplePartImageRepeaterDataModal createFromParcel(Parcel source) {
			return new UploadMuliplePartImageRepeaterDataModal(source);
		}
	};


}
