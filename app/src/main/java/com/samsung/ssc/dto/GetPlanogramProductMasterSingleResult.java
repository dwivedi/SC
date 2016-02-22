package com.samsung.ssc.dto;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class GetPlanogramProductMasterSingleResult {

	@SerializedName("PlanogramProductMasterList")
	private ArrayList<PlanogramProductDataModal> dataModals = new ArrayList<PlanogramProductDataModal>();

	@SerializedName("TotalRow")
	private int totalRow;

	public ArrayList<PlanogramProductDataModal> getDataModals() {
		return dataModals;
	}

	public void setDataModals(ArrayList<PlanogramProductDataModal> dataModals) {
		this.dataModals = dataModals;
	}

	public int getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}

}
