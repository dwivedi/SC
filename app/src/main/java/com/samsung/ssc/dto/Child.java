package com.samsung.ssc.dto;

public class Child {

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCompetitorID() {
		return competitorID;
	}

	public void setCompetitorID(String competitorID) {
		this.competitorID = competitorID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProductTypeID() {
		return productTypeID;
	}

	public void setProductTypeID(String productTypeID) {
		this.productTypeID = productTypeID;
	}

	public String getValue() {
		return value==null?"":value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private String code;
	private String competitorID;
	private String name;
	private String productTypeID;

	private String value;
	
}