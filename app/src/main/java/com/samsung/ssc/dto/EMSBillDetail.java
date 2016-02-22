package com.samsung.ssc.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class EMSBillDetail implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public long mEMSBillDetailID;
	public long mEMSBillDetailIDServer;
	public long mEMSExpenseDetailID;
	public long mBillDate;		
	public String mBillNo = "";
	public String mDescription = "";	
	public double mAmount;
	public long mlCreatedDate;
	public int miCreatedBy;
	public long mlModifiedDate;
	public int miModifiedBy;
	
	public boolean mIsActive;	
	public boolean mIsDeleted;
	
	
	//public View mBillayout;
	
	public int  miBillPositionInParentLayout ;
	
	/*private boolean[] mbIsActiveArray ;
	private boolean[] mbIsDeletedArray ;
	*/
	public ArrayList<EMSBillDocumentDetail> mEMSBillDocumentDetailList;
	
	public EMSBillDetail() {
		
        
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
	}*/
	
	
	public void addDocument(EMSBillDocumentDetail inEMSBillDocumentDetail)
	{
		if(mEMSBillDocumentDetailList == null)
			mEMSBillDocumentDetailList = new ArrayList<EMSBillDocumentDetail>();
		
		mEMSBillDocumentDetailList.add(inEMSBillDocumentDetail);
		
	}
	
	
	public void removeDocument(int position)
	{
		if(mEMSBillDocumentDetailList != null && mEMSBillDocumentDetailList.size() > 0)
		{		
			mEMSBillDocumentDetailList.remove(position);
		}
	
	}
	
	
	public void softDeleteDocument(EMSBillDocumentDetail inEMSBillDocumentDetail)
	{
		if(mEMSBillDocumentDetailList != null && mEMSBillDocumentDetailList.size() > 0)
		{	
			for(int i = 0 ; i < mEMSBillDocumentDetailList.size() ; i++)
			{
				EMSBillDocumentDetail emsBillDocumentDetail = mEMSBillDocumentDetailList.get(i);
				
				
			
				if(emsBillDocumentDetail.mEMSBillDocumentDetailID == inEMSBillDocumentDetail.mEMSBillDocumentDetailID)
				{				
					emsBillDocumentDetail.mIsDeleted = true;
				   // mEMSBillDocumentDetailList.remove(i);		    
				    break;			    
				}
			}	
			
		}
	}
	
	
	public int getDocumentCount()
	{
		int count = 0;
		
		if(mEMSBillDocumentDetailList != null && mEMSBillDocumentDetailList.size() > 0)
		{	
			
			Iterator<EMSBillDocumentDetail> it = mEMSBillDocumentDetailList.iterator(); 
			while(it.hasNext())
			{
				EMSBillDocumentDetail emsBillDocumentDetail = it.next();
				
				if(emsBillDocumentDetail.mEMSBillDocumentDetailID == 0 && emsBillDocumentDetail.mIsDeleted == true)
				{
			        it.remove();
			    }
			}
			
			
			for(int i = 0 ; i < mEMSBillDocumentDetailList.size() ; i++)
			{
				EMSBillDocumentDetail emsBillDocumentDetail = mEMSBillDocumentDetailList.get(i);
				
				if(emsBillDocumentDetail.mIsDeleted == false)
					++count;		
			}
		}
		
		return count;
	}
	
	
	
	
	/* public EMSBillDetail(Parcel source) 
	    { 
		    mEMSBillDetailID = source.readInt();
		    mEMSExpenseDetailID = source.readInt();
		    mBillDate = source.readLong();	
		    mBillNo = source.readString();
		    mDescription = source.readString();	
		    mAmount = source.readDouble();		    
		    mlCreatedDate = source.readLong();		    
		    miCreatedBy = source.readInt();
		    mlModifiedDate	= source.readInt(); 
		    miModifiedBy = source.readInt();	    	    
		    
		    source.readBooleanArray(mbIsActiveArray);
		    this.mIsActive = mbIsActiveArray[0];
		    source.readBooleanArray(mbIsDeletedArray);
		    this.mIsDeleted = mbIsDeletedArray[0];
		    
		    source.readList(mEMSBillDocumentDetailList, EMSBillDocumentDetail.class.getClassLoader());
    
		}*/
	     
	/*	@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			
			dest.writeInt(mEMSBillDetailID);
			dest.writeInt(mEMSExpenseDetailID);	
			dest.writeLong(mBillDate);		
			dest.writeString(mBillNo);
			dest.writeString(mDescription);	
			dest.writeDouble(mAmount);		
			dest.writeLong(mlCreatedDate);				
			dest.writeInt(miCreatedBy);				
			dest.writeLong(mlModifiedDate);	
			dest.writeInt(miModifiedBy);	
			
			dest.writeBooleanArray(mbIsActiveArray);
			dest.writeBooleanArray(mbIsDeletedArray);	
			
			dest.writeList(mEMSBillDocumentDetailList);
		}
		
		public static final Parcelable.Creator<EMSBillDetail> CREATOR = new Creator<EMSBillDetail>() {

			@Override
			public EMSBillDetail[] newArray(int size) {
				return new EMSBillDetail[size];
			}

			@Override
			public EMSBillDetail createFromParcel(Parcel source) {
				return new EMSBillDetail(source);
			}
		};*/
	
}
