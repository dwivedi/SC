package com.samsung.ssc.dto;

import com.google.gson.annotations.SerializedName;

public class PlanogramProductDataModal {
	
	@SerializedName("ChannelType")
	private String ChannelType;
	@SerializedName("Class")
	private String Class;
	@SerializedName("CompProductGroup")
	private String CompProductGroup;
	@SerializedName("PlanogramProductMasterID")
	private int PlanogramProductMasterID;
	@SerializedName("ProductCode")
	private String ProductCode;
	@SerializedName("isSelected")
	private boolean isSelected;
	@SerializedName("IsDelected")
	private boolean isDelected;
	
	
	public String getChannelType() {
		return ChannelType;
	}

	public void setChannelType(String channelType) {
		ChannelType = channelType;
	}

	public String getClassName() {
		return Class;
	}

	public void setClassName(String class1) {
		Class = class1;
	}

	public String getCompProductGroup() {
		return CompProductGroup;
	}

	public void setCompProductGroup(String compProductGroup) {
		CompProductGroup = compProductGroup;
	}

	public int getPlanogramProductMasterID() {
		return PlanogramProductMasterID;
	}

	public void setPlanogramProductMasterID(int planogramProductMasterID) {
		PlanogramProductMasterID = planogramProductMasterID;
	}

	public String getProductCode() {
		return ProductCode;
	}

	public void setProductCode(String productCode) {
		ProductCode = productCode;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isDelected() {
		return isDelected;
	}

	public void setDelected(boolean isDelected) {
		this.isDelected = isDelected;
	}
}
