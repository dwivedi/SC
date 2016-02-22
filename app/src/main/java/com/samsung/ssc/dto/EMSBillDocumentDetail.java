package com.samsung.ssc.dto;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class EMSBillDocumentDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public long mEMSBillDocumentDetailID;
	public long mEMSBillDocumentDetailIDServer;
	public long mEMSBillDetailID;	
	public String mDocumentName = "";	
	public long mlCreatedDate;
	public int miCreatedBy;
	public long mlModifiedDate;
	public int miModifiedBy;	
	
	public boolean mIsActive;	
	public boolean mIsDeleted;	

	public String mDocumentFilePath = "";	
	
	/*private boolean[] mbIsActiveArray;
	private boolean[] mbIsDeletedArray;*/
	
	
	public EMSBillDocumentDetail() {
		
	}
	
/*	public void setIsActive(boolean initialValue) {
		this.mIsActive = initialValue;
		mbIsActiveArray = new boolean[1];
		mbIsActiveArray[0] = this.mIsActive;
	}
	
	public void setIsDeleted(boolean initialValue) {
		this.mIsDeleted = initialValue;
 		mbIsDeletedArray = new boolean[1];
		mbIsDeletedArray[0] = this.mIsDeleted;
	}
	*/
	
	/*
	 public EMSBillDocumentDetail(Parcel source) 
	    { 
		    mEMSBillDocumentDetailID = source.readInt();
		    mEMSBillDetailID = source.readInt();
		    mDocumentName = source.readString();		   
		    mlCreatedDate = source.readLong();		    
		    miCreatedBy = source.readInt();
		    mlModifiedDate	= source.readInt(); 
		    miModifiedBy = source.readInt();		    
		   
		    source.readBooleanArray(mbIsActiveArray);
		    this.mIsActive = mbIsActiveArray[0];
		    source.readBooleanArray(mbIsDeletedArray);
		    this.mIsDeleted = mbIsDeletedArray[0];
		    
		}
	     
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			
			dest.writeInt(mEMSBillDocumentDetailID);
			dest.writeInt(mEMSBillDetailID);
			dest.writeString(mDocumentName);		
			dest.writeLong(mlCreatedDate);				
			dest.writeInt(miCreatedBy);				
			dest.writeLong(mlModifiedDate);	
			dest.writeInt(miModifiedBy);	
			
			dest.writeBooleanArray(mbIsActiveArray);
			dest.writeBooleanArray(mbIsDeletedArray);						
		}
		
		public static final Parcelable.Creator<EMSBillDocumentDetail> CREATOR = new Creator<EMSBillDocumentDetail>() {

			@Override
			public EMSBillDocumentDetail[] newArray(int size) {
				return new EMSBillDocumentDetail[size];
			}

			@Override
			public EMSBillDocumentDetail createFromParcel(Parcel source) {
				return new EMSBillDocumentDetail(source);
			}
		};*/
	
}
