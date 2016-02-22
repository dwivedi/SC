package com.samsung.ssc.dto;


import com.google.gson.annotations.SerializedName;

public class DealerCreationMultiPartUpload {
	@SerializedName("dealerCreationID")
	private int DealerCreationId;
	
	@SerializedName("columnName")
	private String columnName;
	
	private long userId;
	private String imagePath;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getDealerCreationId() {
		return DealerCreationId;
	}

	public void setDealerCreationId(int dealerCreationId) {
		DealerCreationId = dealerCreationId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

}
