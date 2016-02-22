package com.samsung.ssc.dto;


public class EOLSchemeDetailDTO {
	
	public int getSchemeID() {
		return schemeID;
	}
	public void setSchemeID(int schemeID) {
		this.schemeID = schemeID;
	}
	public String getBasicModelCode() {
		return basicModelCode;
	}
	public void setBasicModelCode(String basicModelCode) {
		this.basicModelCode = basicModelCode;
	}
	public int getQuatity() {
		return quatity;
	}
	public void setQuatity(int quatity) {
		this.quatity = quatity;
	}
	public int getSupport() {
		return support;
	}
	public void setSupport(int support) {
		this.support = support;
	}
	private int schemeID;
	private String basicModelCode;
	private int quatity;
	private int support;
	
	
	public int getUserDefineQuentity() {
		return userDefineQuentity;
	}
	public void setUserDefineQuentity(int userDefineQuentity) {
		this.userDefineQuentity = userDefineQuentity;
	}
	public int getUserDefineSupport() {
		return userDefineSupport;
	}
	public void setUserDefineSupport(int userDefineSupport) {
		this.userDefineSupport = userDefineSupport;
	}
	private int userDefineQuentity;
	private int userDefineSupport;
	private int schemeDetailsID;

	@Override
	public String toString() {
		return "EOLSchemeDetailDTO [schemeDetailsID=" + schemeDetailsID
				+ ", schemeID=" + schemeID + ", basicModelCode="
				+ basicModelCode + ", quatity=" + quatity + ", support="
				+ support + ", userDefineQuentity=" + userDefineQuentity
				+ ", userDefineSupport=" + userDefineSupport + "]";
	}
	
	
	

}

