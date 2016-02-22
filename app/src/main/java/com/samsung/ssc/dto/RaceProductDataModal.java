package com.samsung.ssc.dto;

public class RaceProductDataModal {
	
	int ProductID ;
	String ProductType;
	String ProductGroup ;
	String ProductCategory ;
	String prodctName;
	int  BrandID;
	
	public int getProductID() {
		return ProductID;
	}
	public void setProductID(int productID) {
		ProductID = productID;
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
	public String getProductName() {
		return prodctName;
	}
	public void setProductName(String modelName) {
		prodctName = modelName;
	}
	public int getBrandID() {
		return BrandID;
	}
	public void setBrandID(int brandID) {
		BrandID = brandID;
	}

	

}
