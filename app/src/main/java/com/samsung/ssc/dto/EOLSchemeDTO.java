package com.samsung.ssc.dto;

import java.util.ArrayList;

import java.util.LinkedHashMap;

public class EOLSchemeDTO {
	
	
	private int schemeID;
	private String schemeNumber;
	private String PUMINumber;
	private String ProductType;
	private String ProductGroup;
	private String ProductCategory;
	private String schemeFrom;
	private String schemeTo;
	private String orderFrom;
	private String orderTo;
	private String createdDate;
	private String modifiedDate;
	private String pumiDate;
	
	private ArrayList<EOLSchemeDetailDTO> scheemDetails;
	private ArrayList<EOLSchemeOrderDTO> schemeOrders;
	
	
	public ArrayList<EOLSchemeOrderDTO> getSchemeOrders() {
		return schemeOrders;
	}
	public void setSchemeOrders(ArrayList<EOLSchemeOrderDTO> schemeOrders) {
		this.schemeOrders = schemeOrders;
	}
	public int getSchemeID() {
		return schemeID;
	}
	public void setSchemeID(int schemeID) {
		this.schemeID = schemeID;
	}
	public String getSchemeNumber() {
		return schemeNumber;
	}
	public void setSchemeNumber(String schemeNumber) {
		this.schemeNumber = schemeNumber;
	}
	public String getPUMINumber() {
		return PUMINumber;
	}
	public void setPUMINumber(String pUMINumber) {
		PUMINumber = pUMINumber;
	}
	public String getProductType() {
		return ProductType;
	}
	public void setProductType(String productType) {
		ProductType = productType;
	}
	public String getProductGroup() {
		return ProductGroup;
	}
	public void setProductGroup(String productGroup) {
		ProductGroup = productGroup;
	}
	public String getProductCategory() {
		return ProductCategory;
	}
	public void setProductCategory(String productCategory) {
		ProductCategory = productCategory;
	}
	public String getSchemeFrom() {
		return schemeFrom;
	}
	public void setSchemeFrom(String schemeFrom) {
		this.schemeFrom = schemeFrom;
	}
	public String getSchemeTo() {
		return schemeTo;
	}
	public void setSchemeTo(String schemeTo) {
		this.schemeTo = schemeTo;
	}
	public String getOrderFrom() {
		return orderFrom;
	}
	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}
	public String getOrderTo() {
		return orderTo;
	}
	public void setOrderTo(String orderTo) {
		this.orderTo = orderTo;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getPumiDate() {
		return pumiDate;
	}
	public void setPumiDate(String pumiDate) {
		this.pumiDate = pumiDate;
	}
	public ArrayList<EOLSchemeDetailDTO> getScheemDetails() {
		return scheemDetails;
	}
	public void setScheemDetails(ArrayList<EOLSchemeDetailDTO> scheemDetails) {
		this.scheemDetails = scheemDetails;
	}
	

	@Override
	public String toString() {
		return "EOLSchemeDTO [schemeID=" + schemeID + ", schemeNumber="
				+ schemeNumber + ", PUMINumber=" + PUMINumber
				+ ", ProductType=" + ProductType + ", ProductGroup="
				+ ProductGroup + ", ProductCategory=" + ProductCategory
				+ ", schemeFrom=" + schemeFrom + ", schemeTo=" + schemeTo
				+ ", orderFrom=" + orderFrom + ", orderTo=" + orderTo
				+ ", createdDate=" + createdDate + ", modifiedDate="
				+ modifiedDate + ", pumiDate=" + pumiDate + ", scheemDetails="
				+ scheemDetails + "]";
	}
	
	
	
	public LinkedHashMap<String, String> getValueMap() {

		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("Scheme Period", schemeFrom+" to "+schemeTo);
		map.put("Order Submission Period", orderFrom+" to "+orderTo);
		map.put("My Pumi Number", PUMINumber);
		map.put("My Pumi Date", pumiDate);
		map.put("Product Type", ProductType);
		map.put("Product Group", ProductGroup);
		map.put("Product Category", ProductCategory);
		map.put("Scheme Number", schemeNumber);
			
		return map;
		
	}
	

}


 