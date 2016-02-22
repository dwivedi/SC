package com.samsung.ssc.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class EMSExpenseType implements Parcelable 
 {

	public int miExpenseTypeMasterId;	
	public String mName;
	public int miCode;
	public String mDescription;
	public int miCompanyId;
	public int miSequence;
	
	
	public EMSExpenseType() {
		// TODO Auto-generated constructor stub
	}
	
	
	 public EMSExpenseType(Parcel source) 
	    { 
		    miExpenseTypeMasterId = source.readInt();	    
		    mName = source.readString();
		    miCode = source.readInt();		    
		    mDescription = source.readString();   
		    miCompanyId = source.readInt();
		    miSequence	= source.readInt(); 
		    	    
		}
	     
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			
			dest.writeInt(miExpenseTypeMasterId);
			dest.writeString(mName);
			dest.writeInt(miCode);			
			dest.writeString(mDescription);		
			dest.writeInt(miCompanyId);
			dest.writeInt(miSequence);		
		
		}
		
		public static final Creator<EMSExpenseType> CREATOR = new Creator<EMSExpenseType>() {

			@Override
			public EMSExpenseType[] newArray(int size) {
				return new EMSExpenseType[size];
			}

			@Override
			public EMSExpenseType createFromParcel(Parcel source) {
				return new EMSExpenseType(source);
			}
		};
	
}
