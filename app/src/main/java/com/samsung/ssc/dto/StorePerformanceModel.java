package com.samsung.ssc.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class StorePerformanceModel implements Parcelable {
	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public float getAch() {
		return ach;
	}

	public void setAch(float ach) {
		this.ach = ach;
	}

	public float getACMTDSale() {
		return ACMTDSale;
	}

	public void setACMTDSale(float aCMTDSale) {
		ACMTDSale = aCMTDSale;
	}

	public float getACMTDPurchase() {
		return ACMTDPurchase;
	}

	public void setACMTDPurchase(float aCMTDPurchase) {
		ACMTDPurchase = aCMTDPurchase;
	}

	public float getAVMTDSale() {
		return AVMTDSale;
	}

	public void setAVMTDSale(float aVMTDSale) {
		AVMTDSale = aVMTDSale;
	}

	public float getAVMTDPurchase() {
		return AVMTDPurchase;
	}

	public void setAVMTDPurchase(float aVMTDPurchase) {
		AVMTDPurchase = aVMTDPurchase;
	}

	public float getHAMTDSale() {
		return HAMTDSale;
	}

	public void setHAMTDSale(float hAMTDSale) {
		HAMTDSale = hAMTDSale;
	}

	public float getHAMTDPurchase() {
		return HAMTDPurchase;
	}

	public void setHAMTDPurchase(float hAMTDPurchase) {
		HAMTDPurchase = hAMTDPurchase;
	}

	public float getACLMTDSale() {
		return ACLMTDSale;
	}

	public void setACLMTDSale(float aCLMTDSale) {
		ACLMTDSale = aCLMTDSale;
	}

	public float getACLMTDPurchase() {
		return ACLMTDPurchase;
	}

	public void setACLMTDPurchase(float aCLMTDPurchase) {
		ACLMTDPurchase = aCLMTDPurchase;
	}

	public float getAVLMTDSale() {
		return AVLMTDSale;
	}

	public void setAVLMTDSale(float aVLMTDSale) {
		AVLMTDSale = aVLMTDSale;
	}

	public float getAVLMTDPurchase() {
		return AVLMTDPurchase;
	}

	public void setAVLMTDPurchase(float aVLMTDPurchase) {
		AVLMTDPurchase = aVLMTDPurchase;
	}

	public float getHALMTDSale() {
		return HALMTDSale;
	}

	public void setHALMTDSale(float hALMTDSale) {
		HALMTDSale = hALMTDSale;
	}

	public float getHALMTDPurchase() {
		return HALMTDPurchase;
	}

	public void setHALMTDPurchase(float hALMTDPurchase) {
		HALMTDPurchase = hALMTDPurchase;
	}

	public StorePerformanceModel() {

	}

	private int target;
	private float ach;
	private float ACMTDSale;
	private float ACMTDPurchase;
	private float AVMTDSale;
	private float AVMTDPurchase;
	private float HAMTDSale;
	private float HAMTDPurchase;
	private float ACLMTDSale;

	private float ACLMTDPurchase;
	private float AVLMTDSale;
	private float AVLMTDPurchase;
	private float HALMTDSale;
	private float HALMTDPurchase;
	private String lastVisitedDate;

	public String getLastVisitedDate() {
		return lastVisitedDate;
	}

	public void setLastVisitedDate(String lastVisitedDate) {
		this.lastVisitedDate = lastVisitedDate;
	}

	protected StorePerformanceModel(Parcel in) {
		target = in.readInt();
		lastVisitedDate=in.readString();
		ach = in.readFloat();
		ACMTDSale = in.readFloat();
		ACMTDPurchase = in.readFloat();
		AVMTDSale = in.readFloat();
		AVMTDPurchase = in.readFloat();
		HAMTDSale = in.readFloat();
		HAMTDPurchase = in.readFloat();
		ACLMTDSale = in.readFloat();
		ACLMTDPurchase = in.readFloat();
		AVLMTDSale = in.readFloat();
		AVLMTDPurchase = in.readFloat();
		HALMTDSale = in.readFloat();
		HALMTDPurchase = in.readFloat();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(target);
		dest.writeString(lastVisitedDate);
		dest.writeFloat(ach);
		dest.writeFloat(ACMTDSale);
		dest.writeFloat(ACMTDPurchase);
		dest.writeFloat(AVMTDSale);
		dest.writeFloat(AVMTDPurchase);
		dest.writeFloat(HAMTDSale);
		dest.writeFloat(HAMTDPurchase);
		dest.writeFloat(ACLMTDSale);
		dest.writeFloat(ACLMTDPurchase);
		dest.writeFloat(AVLMTDSale);
		dest.writeFloat(AVLMTDPurchase);
		
		dest.writeFloat(HALMTDSale);
		dest.writeFloat(HALMTDPurchase);
	}

	public static final Parcelable.Creator<StorePerformanceModel> CREATOR = new Parcelable.Creator<StorePerformanceModel>() {
		@Override
		public StorePerformanceModel createFromParcel(Parcel in) {
			return new StorePerformanceModel(in);
		}

		@Override
		public StorePerformanceModel[] newArray(int size) {
			return new StorePerformanceModel[size];
		}
	};
}