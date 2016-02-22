package com.samsung.ssc.dto;

import com.google.gson.annotations.SerializedName;

public class DealerCreationfinalRequest {
	
	@SerializedName("dealerCreation")
	private SubmitDealerCreationDto  creationDto;
	
	@SerializedName("emplCode")
	private String emplcode;

	public SubmitDealerCreationDto getCreationDto() {
		return creationDto;
	}

	public void setCreationDto(SubmitDealerCreationDto creationDto) {
		this.creationDto = creationDto;
	}

	public String getEmplcode() {
		return emplcode;
	}

	public void setEmplcode(String emplcode) {
		this.emplcode = emplcode;
	}

}
