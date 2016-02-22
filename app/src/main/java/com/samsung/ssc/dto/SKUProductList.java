package com.samsung.ssc.dto;

public class SKUProductList {
	private String productID;
	private String SKUCode;
	private String productCategoryID;
	private String quantity;
	private String ProductTypeID;
	private String ProductGroupID;
	private long activtyID;
	
	public String getProductGroupID() {
		return ProductGroupID;
	}
	public void setProductGroupID(String productGroupID) {
		ProductGroupID = productGroupID;
	}
	public String getProductTypeID() {
		return ProductTypeID;
	}
	public long getActivtyID() {
		return activtyID;
	}
	public void setActivtyID(long activtyID) {
		this.activtyID = activtyID;
	}
	public void setProductTypeID(String productTypeID) {
		ProductTypeID = productTypeID;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getProductCategoryID() {
		return productCategoryID;
	}
	public void setProductCategoryID(String productCategoryID) {
		this.productCategoryID = productCategoryID;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public String getSKUCode() {
		return SKUCode;
	}
	public void setSKUCode(String sKUCode) {
		SKUCode = sKUCode;
	}
	public String getDealerPrice() {
		return dealerPrice;
	}
	public void setDealerPrice(String dealerPrice) {
		this.dealerPrice = dealerPrice;
	}
	private String dealerPrice;
	
	
}
