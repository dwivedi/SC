package com.samsung.ssc.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class EMSExpenseDetail implements Serializable 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	
	public long miEMSExpenseDetailID;	
	public int miEMSExpenseTypeMasterID;		
	public String mBillableTo = "";
	public String mComment;		
	public int miExpenseStatus;
	public long mlSubmittedDate;		
	public int miCreatedBy;	
	public int miModifiedDate;	
	public int miModifiedBy;
	public long miEMSExpenseDetailIDServer;
	
	public boolean mbIsActive;
	public boolean mbIsDeleted;
	public boolean mbBillable;	
	
	public String mPendingWith = "";
	
	public int miUserID ;
	public int miRoleID ;
	
	public int miAssignedToUser;
	
	public int miAssignedStatus;
	
	
	public int miEMSExpenseDetailITabID;
	
	/*private boolean[] mbIsActiveArray;
	private boolean[] mbIsDeletedArray;
	private boolean[] mbBillableArray;*/
	
	public ArrayList<EMSBillDetail> mEMSBillDetailList;
	
	
	public EMSExpenseDetail() {
		// TODO Auto-generated constructor stub
	}
	
	public void addBill(EMSBillDetail inEMSBillDetail)
	{
		if(mEMSBillDetailList == null)
			mEMSBillDetailList = new ArrayList<EMSBillDetail>();
		
		mEMSBillDetailList.add(inEMSBillDetail);
		
	}

	public int getBillCount()
	{
		int count = 0;

		if(mEMSBillDetailList != null && mEMSBillDetailList.size() > 0) {

			for (int i = 0; i < mEMSBillDetailList.size(); i++) {
				EMSBillDetail emsBillDetail = mEMSBillDetailList.get(i);

				if (emsBillDetail.mIsDeleted == false)
					++count;
			}
		}

		return count;
	}
	
	public void updateBill(EMSBillDetail inEMSBillDetail)
	{
		int position = inEMSBillDetail.miBillPositionInParentLayout;
		
		if(mEMSBillDetailList != null)
		{
			for(int i = 0; i < mEMSBillDetailList.size() ; i++)
			{
				if(i == position)
				{
					if(inEMSBillDetail.mIsDeleted == true && inEMSBillDetail.mEMSBillDetailIDServer == 0)
					{
						mEMSBillDetailList.remove(position);
					}
					else
					{
						mEMSBillDetailList.set(position, inEMSBillDetail);
					}
					
					break;
				}
			}
		}
		
	}
	
	public void removeDocument(int position)
	{
		if(mEMSBillDetailList != null && mEMSBillDetailList.size() > 0)
		{		
			mEMSBillDetailList.remove(position);
		}
	
	}
	
	/* public EMSExpenseDetail(Parcel source) 
	    { 
		    mEMSExpenseDetailID = source.readInt();
		    mEMSExpenseTypeMasterID = source.readInt();
		    mBillableTo = source.readString();
		    mComment = source.readString();	
		    miStatus = source.readInt();
		    mlCreatedDate = source.readLong();		    
		    miCreatedBy = source.readInt();
		    miModifiedDate	= source.readInt(); 
		    miModifiedBy = source.readInt();	
		    
		    source.readBooleanArray(mbIsActiveArray);
		    source.readBooleanArray(mbIsDeletedArray);
		    source.readBooleanArray(mbBillableArray);
		    
		    source.readTypedList(mEMSBillDetailList, null);
		   
		}
	     
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			
			dest.writeInt(mEMSExpenseDetailID);
			dest.writeInt(mEMSExpenseTypeMasterID);		
			dest.writeString(mBillableTo);
			dest.writeString(mComment);			
			dest.writeInt(miStatus);
			dest.writeLong(mlCreatedDate);
			dest.writeInt(miCreatedBy);
			dest.writeInt(miModifiedDate);		
			dest.writeInt(miModifiedBy);		
			dest.writeBooleanArray(mbIsActiveArray);
			dest.writeBooleanArray(mbIsDeletedArray);
			dest.writeBooleanArray(mbBillableArray);		
			dest.writeTypedList(mEMSBillDetailList);
			
		}
		
		public static final Parcelable.Creator<EMSExpenseDetail> CREATOR = new Creator<EMSExpenseDetail>() {

			@Override
			public EMSExpenseDetail[] newArray(int size) {
				return new EMSExpenseDetail[size];
			}

			@Override
			public EMSExpenseDetail createFromParcel(Parcel source) {
				return new EMSExpenseDetail(source);
			}
		};
	*/
}
