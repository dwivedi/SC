package com.samsung.ssc.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class ExpenseItem implements Parcelable{

	public String mSubmittedDate;
	public String mExpenseType;
	public String mPendingWith;
	public int miExpenseStatus;

	public ExpenseItem() {
		// TODO Auto-generated constructor stub
	}
	
    public ExpenseItem(Parcel source) 
    { 
		mSubmittedDate = source.readString();
		mExpenseType = source.readString();
		mPendingWith = source.readString();
		miExpenseStatus = source.readInt();		 
	}
     
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(mSubmittedDate);
		dest.writeString(mExpenseType);
		dest.writeString(mPendingWith);
		dest.writeInt(miExpenseStatus);
		
	}
	
	public static final Creator<ExpenseItem> CREATOR = new Creator<ExpenseItem>() {

		@Override
		public ExpenseItem[] newArray(int size) {
			return new ExpenseItem[size];
		}

		@Override
		public ExpenseItem createFromParcel(Parcel source) {
			return new ExpenseItem(source);
		}
	};
	
}
