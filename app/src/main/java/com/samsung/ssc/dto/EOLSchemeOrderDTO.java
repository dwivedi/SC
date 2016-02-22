package com.samsung.ssc.dto;

public class EOLSchemeOrderDTO implements Cloneable  
{

	int actualSupport;
	String basicModelCode;
	int orderQunatity;
	int schemeID;
	int storeID;
	String storeName;
	boolean headerType;
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public boolean isHeaderType() {
		return headerType;
	}

	public void setHeaderType(boolean headerType) {
		this.headerType = headerType;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public int getActualSupport() {
		return actualSupport;
	}

	public void setActualSupport(int actualSupport) {
		this.actualSupport = actualSupport;
	}

	public String getBasicModelCode() {
		return basicModelCode;
	}

	public void setBasicModelCode(String basicModelCode) {
		this.basicModelCode = basicModelCode;
	}

	public int getOrderQunatity() {
		return orderQunatity;
	}

	public void setOrderQunatity(int orderQunatity) {
		this.orderQunatity = orderQunatity;
	}

	public int getSchemeID() {
		return schemeID;
	}

	public void setSchemeID(int schemeID) {
		this.schemeID = schemeID;
	}

	public int getStoreID() {
		return storeID;
	}

	public void setStoreID(int storeID) {
		this.storeID = storeID;
	}
	

	

}
