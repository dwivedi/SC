package com.samsung.ssc.dto;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.util.Helper;

public class GetKPIRequest {

	@SerializedName("userID") 
	private String userID;
	
	@SerializedName("roleID") 
	private int roleID;

	public GetKPIRequest(Context context) {
		this.userID = Helper.getStringValuefromPrefs(context,
				SharedPreferencesKey.PREF_USERID);
		this.roleID = Helper.getIntValueFromPrefs(context,
				SharedPreferencesKey.PREF_ROLEID);
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}
}
