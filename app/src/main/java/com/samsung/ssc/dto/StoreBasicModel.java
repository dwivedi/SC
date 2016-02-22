package com.samsung.ssc.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class StoreBasicModel implements Parcelable {

	private String storeName;
	private String storeCode;
	private long storeID;
	private boolean isCoverage;
	private long coverageID;
	private String cityName;

	private String ChannelType;
	private String StoreSize;
	private String ContactPerson = "";
	private String mobileNo = "";
	private String emailID = "";
	private String storeAddress = "";
	private String pictureFileName;

	private boolean IsFreeze;
	private double FreezeLattitude;
	private double FreezeLongitude;
	
	private boolean IsPlanogram;
	private String storeClass;

	private boolean IsDisplayCounterShare;
	private long userRoleID;

	private String target;

	
	
	private double storeDistance;
	private String storeType ; 
	private int storeColor;
	
	// use for sorting store name according to color
	private int storeColorIndex;
	
	private String landlineNumber="";
	private String SPCName="";
	private String SPCCategory="";
	private String StoreMangerName="";
	private String SMMobile="";
	private String SMEmailID="";
	private String AlternateEmailID="";
	private String TinNumber="";
	private String PinCode="";
	
	public String getLandlineNumber() {
		return landlineNumber;
	}

	public void setLandlineNumber(String landlineNumber) {
		this.landlineNumber = landlineNumber;
	}

	public String getSPCName() {
		return SPCName;
	}

	public void setSPCName(String sPCName) {
		SPCName = sPCName;
	}

	public String getSPCCategory() {
		return SPCCategory;
	}

	public void setSPCCategory(String sPCCategory) {
		SPCCategory = sPCCategory;
	}

	public String getStoreMangerName() {
		return StoreMangerName;
	}

	public void setStoreMangerName(String storeMangerName) {
		StoreMangerName = storeMangerName;
	}

	public String getSMMobile() {
		return SMMobile;
	}

	public void setSMMobile(String sMMobile) {
		SMMobile = sMMobile;
	}

	public String getSMEmailID() {
		return SMEmailID;
	}

	public void setSMEmailID(String sMEmailID) {
		SMEmailID = sMEmailID;
	}

	public String getAlternateEmailID() {
		return AlternateEmailID;
	}

	public void setAlternateEmailID(String alternateEmailID) {
		AlternateEmailID = alternateEmailID;
	}

	public String getTinNumber() {
		return TinNumber;
	}

	public void setTinNumber(String tinNumber) {
		TinNumber = tinNumber;
	}

	public String getPinCode() {
		return PinCode;
	}

	public void setPinCode(String pinCode) {
		PinCode = pinCode;
	}

	public static Parcelable.Creator<StoreBasicModel> getCreator() {
		return CREATOR;
	}


	public int getStoreColorIndex() {
		return storeColorIndex;
	}

	public void setStoreColorIndex(int storeColorIndex) {
		this.storeColorIndex = storeColorIndex;
	}

	public String getChannelType() {
		return ChannelType;
	}

	public void setChannelType(String channelType) {
		ChannelType = channelType;
	}

	public String getStoreSize() {
		return StoreSize;
	}

	public void setStoreSize(String storeSize) {
		StoreSize = storeSize;
	}

	public String getContactPerson() {
		return ContactPerson;
	}

	public void setContactPerson(String contactPerson) {
		ContactPerson = contactPerson;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	
	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getPictureFileName() {
		return pictureFileName;
	}

	public void setPictureFileName(String pictureFileName) {
		this.pictureFileName = pictureFileName;
	}

	public boolean isIsFreeze() {
		return IsFreeze;
	}

	public void setIsFreeze(boolean isFreeze) {
		IsFreeze = isFreeze;
	}

	public double getFreezeLattitude() {
		return FreezeLattitude;
	}

	public void setFreezeLattitude(double freezeLattitude) {
		FreezeLattitude = freezeLattitude;
	}

	public double getFreezeLongitude() {
		return FreezeLongitude;
	}

	public void setFreezeLongitude(double freezeLongitude) {
		FreezeLongitude = freezeLongitude;
	}

	public boolean isIsPlanogram() {
		return IsPlanogram;
	}

	public void setIsPlanogram(boolean isPlanogram) {
		IsPlanogram = isPlanogram;
	}

	public String getStoreClass() {
		return storeClass;
	}

	public void setStoreClass(String storeClass) {
		this.storeClass = storeClass;
	}

	
	public boolean isIsDisplayCounterShare() {
		return IsDisplayCounterShare;
	}

	public void setIsDisplayCounterShare(boolean isDisplayCounterShare) {
		IsDisplayCounterShare = isDisplayCounterShare;
	}

	public long getUserRoleID() {
		return userRoleID;
	}

	public void setUserRoleID(long userRoleID) {
		this.userRoleID = userRoleID;
	}

	
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public long getStoreID() {
		return storeID;
	}

	public void setStoreID(long storeID) {
		this.storeID = storeID;
	}

	public boolean isCoverage() {
		return isCoverage;
	}

	public void setCoverage(boolean isCoverage) {
		this.isCoverage = isCoverage;
	}

	public long getCoverageID() {
		return coverageID;
	}

	public void setCoverageID(long coverageID) {
		this.coverageID = coverageID;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public double getStoreDistance() {
		return storeDistance;
	}

	public void setStoreDistance(double storeDistance) {
		this.storeDistance = storeDistance;
	}
	

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public int getStoreColor() {
		return storeColor;
	}

	public void setStoreColor(int storeColor) {
		this.storeColor = storeColor;
	}

	protected StoreBasicModel(Parcel in) {
		
		storeName = in.readString();
		storeCode = in.readString();
		storeID = in.readLong();
		isCoverage = in.readByte() != 0x00;
		coverageID = in.readLong();
		cityName = in.readString();
		ChannelType = in.readString();
		StoreSize = in.readString();
		ContactPerson = in.readString();
		mobileNo = in.readString();
		emailID = in.readString();
		storeAddress = in.readString();
		pictureFileName = in.readString();
		IsFreeze = in.readByte() != 0x00;
		FreezeLattitude = in.readDouble();
		FreezeLongitude = in.readDouble();
		storeDistance = in.readDouble();
		IsPlanogram = in.readByte() != 0x00;
		storeClass = in.readString();
	
		IsDisplayCounterShare = in.readByte() != 0x00;
		userRoleID = in.readLong();
		
		target = in.readString();
		storeType = in.readString();	
		setStoreColor(in.readInt());
		storeColorIndex=in.readInt();
		
		/*
		 *  New Added field
		 */
		
		landlineNumber = in.readString();
		SPCName= in.readString();
		SPCCategory= in.readString();
		StoreMangerName = in.readString();
		SMMobile = in.readString();
		SMEmailID = in.readString();
		AlternateEmailID = in.readString();
		TinNumber = in.readString();
		PinCode = in.readString();
		
	}

	public StoreBasicModel()
	{
		
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(storeName);
		dest.writeString(storeCode);
		dest.writeLong(storeID);
		dest.writeByte((byte) (isCoverage ? 0x01 : 0x00));
		dest.writeLong(coverageID);
		dest.writeString(cityName);
		dest.writeString(ChannelType);
		dest.writeString(StoreSize);
		dest.writeString(ContactPerson);
		dest.writeString(mobileNo);
		dest.writeString(emailID);
		dest.writeString(storeAddress);
		
		dest.writeString(pictureFileName);
		dest.writeByte((byte) (IsFreeze ? 0x01 : 0x00));
		dest.writeDouble(FreezeLattitude);
		dest.writeDouble(FreezeLongitude);
		dest.writeDouble(storeDistance);
		dest.writeByte((byte) (IsPlanogram ? 0x01 : 0x00));
		dest.writeString(storeClass);
		
		dest.writeByte((byte) (IsDisplayCounterShare ? 0x01 : 0x00));
		dest.writeLong(userRoleID);
		
		dest.writeString(target);
		dest.writeString(storeType);
		dest.writeInt(getStoreColor());
		dest.writeInt(storeColorIndex);
		
		/*
		 * New Added Field
		 */
		dest.writeString(landlineNumber);
		dest.writeString(SPCName);
		dest.writeString(SPCCategory);
		dest.writeString(StoreMangerName);
		dest.writeString(SMMobile);
		dest.writeString(SMEmailID);
		dest.writeString(AlternateEmailID);
		dest.writeString(TinNumber);
		dest.writeString(PinCode);
	}

	


	public static final Parcelable.Creator<StoreBasicModel> CREATOR = new Parcelable.Creator<StoreBasicModel>() {
		@Override
		public StoreBasicModel createFromParcel(Parcel in) {
			return new StoreBasicModel(in);
		}

		@Override
		public StoreBasicModel[] newArray(int size) {
			return new StoreBasicModel[size];
		}
	};
}