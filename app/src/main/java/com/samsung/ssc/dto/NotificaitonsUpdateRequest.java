package com.samsung.ssc.dto;

import java.util.ArrayList;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.util.Helper;

public class NotificaitonsUpdateRequest {

	@SerializedName("userID")
	private String userID;
	@SerializedName("roleID")
	private int roleID;

	@SerializedName("Notifications")
	private ArrayList<NotificationData> notificationDatas = new ArrayList<NotificationData>();

	public NotificaitonsUpdateRequest(Context activity) {
		super();
		this.userID = Helper.getStringValuefromPrefs(activity,
				SharedPreferencesKey.PREF_USERID);
		this.roleID = Helper.getIntValueFromPrefs(activity,
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

	public ArrayList<NotificationData> getNotificationDatas() {
		return notificationDatas;
	}

	public void setNotificationDatas(
			ArrayList<NotificationData> notificationDatas) {
		this.notificationDatas = notificationDatas;
	}
}
