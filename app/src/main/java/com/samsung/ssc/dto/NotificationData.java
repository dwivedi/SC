package com.samsung.ssc.dto;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.google.gson.annotations.SerializedName;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.util.Helper;

public class NotificationData implements Cloneable {

	@SerializedName("Body")
	private String body;

	@SerializedName("Title")
	private String title;

	@SerializedName("PushNotificationMessage")
	private String pushNotificationMessage;

	@SerializedName("ReadStatus")
	private int readStatus;

	@SerializedName("NotificationServiceID")
	private int notificationServiceID;

	@SerializedName("NotificationType")
	private String NotificationType;

	@SerializedName("NotificationDate")
	private String NotificationDate;

	@SerializedName("IMEINumber")
	private String imeiNumber;

	@SerializedName("UserID")
	private int userID;

	@SerializedName("NotificationID")
	private int notificationID;
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	private boolean isHeaderType;

	public boolean isHeaderType() {
		return isHeaderType;
	}

	public void setHeaderType(boolean isHeaderType) {
		this.isHeaderType = isHeaderType;
	}

	public NotificationData() {

	}

	public NotificationData(Context context) {

		this.readStatus = 1;
		this.userID = Integer.parseInt(Helper.getStringValuefromPrefs(context,
				SharedPreferencesKey.PREF_USERID));
		this.imeiNumber = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPushNotificationMessage() {
		return pushNotificationMessage;
	}

	public void setPushNotificationMessage(String pushNotificationMessage) {
		this.pushNotificationMessage = pushNotificationMessage;
	}

	public int getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(int readStatus) {
		this.readStatus = readStatus;
	}

	public int getNotificationServiceID() {
		return notificationServiceID;
	}

	public void setNotificationServiceID(int notificationServiceID) {
		this.notificationServiceID = notificationServiceID;
	}

	public String getNotificationType() {
		return NotificationType;
	}

	public void setNotificationType(String notificationType) {
		NotificationType = notificationType;
	}

	public String getNotificationDate() {
		return NotificationDate;
	}

	public void setNotificationDate(String notificationDate) {
		NotificationDate = notificationDate;
	}

	public String getImeiNumber() {
		return imeiNumber;
	}

	public void setImeiNumber(String imeiNumber) {
		this.imeiNumber = imeiNumber;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getNotificationID() {
		return notificationID;
	}

	public void setNotificationID(int notificationID) {
		this.notificationID = notificationID;
	}

	@Override
	public String toString() {
		return "NotificationData [body=" + body + ", title=" + title
				+ ", pushNotificationMessage=" + pushNotificationMessage
				+ ", readStatus=" + readStatus + ", notificationServiceID="
				+ notificationServiceID + ", NotificationType="
				+ NotificationType + ", NotificationDate=" + NotificationDate
				+ ", imeiNumber=" + imeiNumber + ", userID=" + userID
				+ ", notificationID=" + notificationID + "]";
	}
	
	
}
