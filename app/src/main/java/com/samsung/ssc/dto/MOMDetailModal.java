package com.samsung.ssc.dto;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

 
public class MOMDetailModal implements Parcelable {
	/**
	 * 
	 */
	public int momID;
	public String momTitle;
	public String momDate;
	public long momDateValue;
	public String momDiscription;
	public String momLocaton;
	public String momActionItem;
	public int activityID;
	public ArrayList<String> momAttendees ;
	public int momServerID;
	public boolean momIsDeleted;
	public boolean momIsUpdated;
	
	
	public MOMDetailModal() {
		// TODO Auto-generated constructor stub
	}
	public MOMDetailModal(Parcel source) {
		
		momID = source.readInt();
		momTitle = source.readString();
		momDate = source.readString();
		momDateValue = source.readLong();
		momDiscription = source.readString();
		momLocaton = source.readString();
		momActionItem = source.readString();
		activityID = source.readInt();
		momAttendees = source.createStringArrayList();
		momServerID = source.readInt();
		momIsDeleted = source.readByte() != 0; 
		momIsUpdated = source.readByte() != 0;
		 
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(momID);
		dest.writeString(momTitle);
		dest.writeString(momDate);
		dest.writeLong(momDateValue);
		dest.writeString(momDiscription);
		dest.writeString(momLocaton);
		dest.writeString(momActionItem);
		dest.writeInt(activityID);
		dest.writeStringList(momAttendees);
		dest.writeInt(momServerID);
		dest.writeByte((byte) (momIsDeleted ? 1 : 0));
		dest.writeByte((byte) (momIsUpdated ? 1 : 0)); 
		
		
	}
	
	public static final Parcelable.Creator<MOMDetailModal> CREATOR = new Creator<MOMDetailModal>() {

		@Override
		public MOMDetailModal[] newArray(int size) {
			return new MOMDetailModal[size];
		}

		@Override
		public MOMDetailModal createFromParcel(Parcel source) {
			return new MOMDetailModal(source);
		}
	};
}