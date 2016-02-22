package com.samsung.ssc.dto;

public class RacePOSMMasterDTO implements Cloneable {
	
	@Override
	public String toString() {
		return "RacePOSMMasterDTO [POSMID=" + POSMID + ", POSMName=" + POSMName
				+ ", isStickerSelected=" + isStickerSelected + ", posmType="
				+ posmType + "]";
	}
	private int POSMID;
	//private String POSMCode;
	private String POSMName;
	//private String POSMGroup;
//	private String POSMCategory;
	private boolean isStickerSelected;
	
	private int posmType;
	 
	public boolean isStickerSelected() {
		return isStickerSelected;
	}
	public void setStickerSelected(boolean isStickerSelected) {
		this.isStickerSelected = isStickerSelected;
	}
	public int getPOSMID() {
		return POSMID;
	}
	public void setPOSMID(int pOSMID) {
		POSMID = pOSMID;
	}
/*	public String getPOSMCode() {
		return POSMCode;
	}
	public void setPOSMCode(String pOSMCode) {
		POSMCode = pOSMCode;
	}*/
	public String getPOSMName() {
		return POSMName;
	}
	public void setPOSMName(String pOSMName) {
		POSMName = pOSMName;
	}
/*	public String getPOSMGroup() {
		return POSMGroup;
	}
	public void setPOSMGroup(String pOSMGroup) {
		POSMGroup = pOSMGroup;
	}
	public String getPOSMCategory() {
		return POSMCategory;
	}
	public void setPOSMCategory(String pOSMCategory) {
		POSMCategory = pOSMCategory;
	}*/
	public int getPosmType() {
		return posmType;
	}
	public void setPosmType(int posmType) {
		this.posmType = posmType;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		return super.clone();
	}
}
