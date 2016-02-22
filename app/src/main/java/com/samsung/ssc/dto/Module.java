package com.samsung.ssc.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class Module implements Parcelable {

	private String message;
	private boolean isMandatory; 
	private boolean isActivityIDAvailable;
	private int syncStatus;
	
	
	public boolean isActivityIDAvailable() {
		return isActivityIDAvailable;
	}

	public void setActivityIDAvailable(boolean isActivityIDAvailable) {
		this.isActivityIDAvailable = isActivityIDAvailable;
	}

	

	public int getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(int syncStatus) {
		this.syncStatus = syncStatus;
	}



	private int moduleID;
	private int moduleCode;
	private int parentModuleID;
	private int sequence;
	private String name;
	private boolean isStoreWise;
	private String iconName;
	
 	private boolean isQuestionType;
 	private int moduleType;
 	


	public int getModuleType() {
		return moduleType;
	}

	public void setModuleType(int moduleType) {
		this.moduleType = moduleType;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public boolean isQuestionType() {
		return isQuestionType;
	}

	public void setQuestionType(boolean isQuestionType) {
		this.isQuestionType = isQuestionType;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public int getModuleID() {
		return moduleID;
	}

	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}

	public int getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(int moduleCode) {
		this.moduleCode = moduleCode;
	}

	public int getParentModuleID() {
		return parentModuleID;
	}

	public void setParentModuleID(int parentModuleID) {
		this.parentModuleID = parentModuleID;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStoreWise() {
		return isStoreWise;
	}

	public void setStoreWise(boolean isStoreWise) {
		this.isStoreWise = isStoreWise;
	}

	public Module(Parcel in) {

		// TODO Auto-generated constructor stub
		this.message = in.readString();
		this.name = in.readString();
		this.iconName = in.readString();

		this.isStoreWise = in.readByte() != 0;
 		this.isQuestionType = in.readByte()!=0;
		this.isMandatory = in.readByte() != 0;
        
		this.isActivityIDAvailable=in.readByte() != 0;
		this.syncStatus= in.readInt();
		
		this.moduleID = in.readInt();
		this.moduleCode = in.readInt();
		this.parentModuleID = in.readInt();
		this.sequence = in.readInt();
		this.moduleType = in.readInt();
		
		

	}

	public Module() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(message);
		dest.writeString(name);
		dest.writeString(iconName);

		dest.writeByte((byte) (isStoreWise ? 1 : 0));
		dest.writeByte((byte) (isQuestionType ? 1 : 0));
		dest.writeByte((byte) (isMandatory ? 1 : 0));

		dest.writeByte((byte) (isActivityIDAvailable ? 1 : 0));
		dest.writeInt(syncStatus);
		
		dest.writeInt(moduleID);
		dest.writeInt(moduleCode);
		dest.writeInt(parentModuleID);
		dest.writeInt(sequence);
		dest.writeInt(moduleType);

	}

	public static final Parcelable.Creator<Module> CREATOR = new Creator<Module>() {

		@Override
		public Module[] newArray(int size) {
			return new Module[size];
		}

		@Override
		public Module createFromParcel(Parcel source) {
			return new Module(source);
		}
	};

}
