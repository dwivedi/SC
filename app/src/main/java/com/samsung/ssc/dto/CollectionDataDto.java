package com.samsung.ssc.dto;

public class CollectionDataDto {

	private String amount;
	private String mode;
	private String modeId;
	private String transId;
	//private String transDate;
	private String paymentDate;
	private String description;

	public String getModeId() {
		return modeId;
	}

	public void setModeId(String modeId) {
		this.modeId = modeId;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setDisplayDate(String displayDate) {
		this.paymentDate = displayDate;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}
/*
	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String string) {
		this.transDate = string;
	}*/

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
