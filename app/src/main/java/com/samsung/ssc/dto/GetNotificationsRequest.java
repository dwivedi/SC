package com.samsung.ssc.dto;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.util.Helper;

public class GetNotificationsRequest {

	@SerializedName("userID")
	private String userID;

	@SerializedName("roleID")
	private int roleID;

	@SerializedName("NotificationType")
	private int notificationType;
	@SerializedName("LastNotificationServiceID")
	private int lastNotificationServiceID;
	@SerializedName("RowCounter")
	private int rowCounter;

	public GetNotificationsRequest(Context activity) {
		this.userID = Helper.getStringValuefromPrefs(activity,
				SharedPreferencesKey.PREF_USERID);
		this.roleID = Helper.getIntValueFromPrefs(activity,
				SharedPreferencesKey.PREF_ROLEID);
	}

	public String getUserID() {
		return userID;
	}

	public int getRoleID() {
		return roleID;
	}

	public int getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(int notificationType) {
		this.notificationType = notificationType;
	}

	public int getLastNotificationServiceID() {
		return lastNotificationServiceID;
	}

	public void setLastNotificationServiceID(int lastNotificationServiceID) {
		this.lastNotificationServiceID = lastNotificationServiceID;
	}

	public int getRowCounter() {
		return rowCounter;
	}

	public void setRowCounter(int rowCounter) {
		this.rowCounter = rowCounter;
	}
}
