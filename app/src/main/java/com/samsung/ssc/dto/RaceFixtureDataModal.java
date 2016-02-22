package com.samsung.ssc.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class RaceFixtureDataModal implements Parcelable {
	
	public RaceFixtureDataModal() {
		// TODO Auto-generated constructor stub
	}

	public int getFixtureID() {
		return fixtureID;
	}

	public void setFixtureID(int fixtureID) {
		this.fixtureID = fixtureID;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getProductGroup() {
		return productGroup;
	}

	public void setProductGroup(String productGroup) {
		this.productGroup = productGroup;
	}

	public boolean isCompetitorAvailable() {
		return isCompetitorAvailable;
	}

	public void setCompetitorAvailable(boolean isCompetitorAvailable) {
		this.isCompetitorAvailable = isCompetitorAvailable;
	}

	public boolean isRowAvailable() {
		return isRowAvailable;
	}

	public void setRowAvailable(boolean isRowAvailable) {
		this.isRowAvailable = isRowAvailable;
	}

	public boolean isWallAvailable() {
		return isWallAvailable;
	}

	public void setWallAvailable(boolean isWallAvailable) {
		this.isWallAvailable = isWallAvailable;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public static Parcelable.Creator<RaceFixtureDataModal> getCreator() {
		return CREATOR;
	}

	private int fixtureID = -1;
	private String category = "";
	private String subCategory = "";
	private String productGroup = "";
	private boolean isCompetitorAvailable = false;
	private boolean isRowAvailable = false;
	private boolean isWallAvailable = false;
	private String brandName = "";
	private int rowNumber = -1;
	private int wallNumber = -1;
	private int brandID=-1;

	public int getBrandID() {
		return brandID;
	}

	public void setBrandID(int brandID) {
		this.brandID = brandID;
	}

	protected RaceFixtureDataModal(Parcel in) {
		fixtureID = in.readInt();
		category = in.readString();
		subCategory = in.readString();
		productGroup = in.readString();
		isCompetitorAvailable = in.readByte() != 0x00;
		isRowAvailable = in.readByte() != 0x00;
		isWallAvailable = in.readByte() != 0x00;
		brandName = in.readString();
		rowNumber = in.readInt();
		wallNumber = in.readInt();
		brandID=in.readInt();
	}

	@Override
	public String toString() {
		return "RaceFixtureDataModal [fixtureID=" + fixtureID + ", category="
				+ category + ", subCategory=" + subCategory + ", productGroup="
				+ productGroup + ", isCompetitorAvailable="
				+ isCompetitorAvailable + ", isRowAvailable=" + isRowAvailable
				+ ", isWallAvailable=" + isWallAvailable + ", brandName="
				+ brandName + ", rowNumber=" + rowNumber + ", wallNumber="
				+ wallNumber + ", brandID=" + brandID + "]";
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public int getWallNumber() {
		return wallNumber;
	}

	public void setWallNumber(int wallNumber) {
		this.wallNumber = wallNumber;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(fixtureID);
		dest.writeString(category);
		dest.writeString(subCategory);
		dest.writeString(productGroup);
		dest.writeByte((byte) (isCompetitorAvailable ? 0x01 : 0x00));
		dest.writeByte((byte) (isRowAvailable ? 0x01 : 0x00));
		dest.writeByte((byte) (isWallAvailable ? 0x01 : 0x00));
		dest.writeString(brandName);
		dest.writeInt(rowNumber);
		dest.writeInt(wallNumber);
		dest.writeInt(brandID);
	}

	public static final Parcelable.Creator<RaceFixtureDataModal> CREATOR = new Parcelable.Creator<RaceFixtureDataModal>() {
		@Override
		public RaceFixtureDataModal createFromParcel(Parcel in) {
			return new RaceFixtureDataModal(in);
		}

		@Override
		public RaceFixtureDataModal[] newArray(int size) {
			return new RaceFixtureDataModal[size];
		}
	};
}