package com.samsung.ssc.dto;

import com.google.gson.annotations.SerializedName;

public class NotificationDTO {

	@SerializedName("NotificationID")
	private String notificationID;

	@SerializedName("NotificationType")
	private String notificationType;

	@SerializedName("NotificationServiceID")
	private String notificationServiceID;

	@SerializedName("NotificationData")
	private NotificationData notificationData;

	public String getNotificationID() {
		return notificationID;
	}

	public void setNotificationID(String notificationID) {
		this.notificationID = notificationID;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getNotificationServiceID() {
		return notificationServiceID;
	}

	public void setNotificationServiceID(String notificationServiceID) {
		this.notificationServiceID = notificationServiceID;
	}

	public NotificationData getNotificationData() {
		return notificationData;
	}

	public void setNotificationData(NotificationData notificationData) {
		this.notificationData = notificationData;
	}
}
