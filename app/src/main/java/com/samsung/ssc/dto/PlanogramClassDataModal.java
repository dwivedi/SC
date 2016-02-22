package com.samsung.ssc.dto;

public class PlanogramClassDataModal {
	
	public String getClassName() {
		return Class;
	}
	public void setClassName(String className) {
		Class = className;
	}
	public int getClassID() {
		return ClassID;
	}
	public void setClassID(int classID) {
		ClassID = classID;
	}
	public int getCompProdGroupID() {
		return CompProdGroupID;
	}
	public void setCompProdGroupID(int compProdGroupID) {
		CompProdGroupID = compProdGroupID;
	}
	public int getEndRange() {
		return EndRange;
	}
	public void setEndRange(int endRange) {
		EndRange = endRange;
	}
	public int getStartRange() {
		return StartRange;
	}
	public void setStartRange(int startRange) {
		StartRange = startRange;
	}
	private String Class;
	private int ClassID;
	private int CompProdGroupID;
	private int EndRange;
	private int StartRange;
 
	

}
