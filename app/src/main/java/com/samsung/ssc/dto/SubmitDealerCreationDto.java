package com.samsung.ssc.dto;


import com.google.gson.annotations.SerializedName;


public class SubmitDealerCreationDto {
	@SerializedName("REASONOFAPPOINTMENT")
	private int reasonofappointment;

	@SerializedName("TYPEOFFIRM")
	private int typeofFirm;

	@SerializedName("NAMEOFFIRM")
	private String nameoffirm;

	@SerializedName("CITYTOWN")
	private String citytown;

	@SerializedName("DISTRICT")
	private String district;

	@SerializedName("PINCODE")
	private String pinCode;

	@SerializedName("LANDLINENUMBER")
	private String landlineNo;

	@SerializedName("STREETNAME")
	private String streetName;

	@SerializedName("FIRMMOBILE")
	private String firmMob;

	@SerializedName("FIRMEMAIL")
	private String firmEmail;

	@SerializedName("TYPEOFDEALER")
	private String typeFfDealer;

	@SerializedName("PARENTDEALERCODE")
	private String parentDealerCode;

	@SerializedName("PAN")
	private String pan;

	@SerializedName("TIN")
	private String tin;

	@SerializedName("NAMEOFOWNER")
	private String ownerName;

	@SerializedName("MOBILEOFOWNER")
	private String ownerMob;

	@SerializedName("CONTACTPERSONNAME")
	private String contactPersonName;

	@SerializedName("CONTPERSONMOBILE")
	private String contactPersonMob;

	@SerializedName("DAYOFF")
	private int dayOff;

	@SerializedName("TOTALCOUNTERSIZE")
	private int totalcountersize;
	@SerializedName("STORESIZECODE")
	private String storeCode;

	@SerializedName("DMSPRMCODE")
	private String PrmCode;

	@SerializedName("PROMOTERREQUIRED")
	private String promoterrequired;

	@SerializedName("CONSUMERFINANCEAVAILABLE")
	private String consumerFinanceAvailable;

	@SerializedName("OWNERDOB")
	private String ownerDOB;

	@SerializedName("LONGITUDE")
	private double longitude;

	@SerializedName("LATITUDE")
	private double lattitude;
	
	@SerializedName("PARENTCOMPANY")
	private String parentCompany;

	public String getParentCompany() {
		return parentCompany;
	}

	public void setParentCompany(String parentCompany) {
		this.parentCompany = parentCompany;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLattitude() {
		return lattitude;
	}

	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}

	public int getReasonofappointment() {
		return reasonofappointment;
	}

	public void setReasonofappointment(int reasonofappointment) {
		this.reasonofappointment = reasonofappointment;
	}

	public int getTypeofFirm() {
		return typeofFirm;
	}

	public void setTypeofFirm(int typeofFirm) {
		this.typeofFirm = typeofFirm;
	}

	public String getNameoffirm() {
		return nameoffirm;
	}

	public void setNameoffirm(String nameoffirm) {
		this.nameoffirm = nameoffirm;
	}

	public String getCitytown() {
		return citytown;
	}

	public void setCitytown(String citytown) {
		this.citytown = citytown;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getLandlineNo() {
		return landlineNo;
	}

	public void setLandlineNo(String landlineNo) {
		this.landlineNo = landlineNo;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getFirmMob() {
		return firmMob;
	}

	public void setFirmMob(String firmMob) {
		this.firmMob = firmMob;
	}

	public String getFirmEmail() {
		return firmEmail;
	}

	public void setFirmEmail(String firmEmail) {
		this.firmEmail = firmEmail;
	}

	public String getTypeFfDealer() {
		return typeFfDealer;
	}

	public void setTypeFfDealer(String typeFfDealer) {
		this.typeFfDealer = typeFfDealer;
	}

	public String getParentDealerCode() {
		return parentDealerCode;
	}

	public void setParentDealerCode(String parentDealerCode) {
		this.parentDealerCode = parentDealerCode;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getTin() {
		return tin;
	}

	public void setTin(String tin) {
		this.tin = tin;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerMob() {
		return ownerMob;
	}

	public void setOwnerMob(String ownerMob) {
		this.ownerMob = ownerMob;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getContactPersonMob() {
		return contactPersonMob;
	}

	public void setContactPersonMob(String contactPersonMob) {
		this.contactPersonMob = contactPersonMob;
	}

	public int getDayOff() {
		return dayOff;
	}

	public void setDayOff(int dayOff) {
		this.dayOff = dayOff;
	}

	public int getTotalcountersize() {
		return totalcountersize;
	}

	public void setTotalcountersize(int totalcountersize) {
		this.totalcountersize = totalcountersize;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getPrmCode() {
		return PrmCode;
	}

	public void setPrmCode(String prmCode) {
		PrmCode = prmCode;
	}

	public String getPromoterrequired() {
		return promoterrequired;
	}

	public void setPromoterrequired(String promoterrequired) {
		this.promoterrequired = promoterrequired;
	}

	public String getConsumerFinanceAvailable() {
		return consumerFinanceAvailable;
	}

	public void setConsumerFinanceAvailable(String consumerFinanceAvailable) {
		this.consumerFinanceAvailable = consumerFinanceAvailable;
	}

	public String getOwnerDOB() {
		return ownerDOB;
	}

	public void setOwnerDOB(String ownerDOB) {
		this.ownerDOB = ownerDOB;
	}

}
