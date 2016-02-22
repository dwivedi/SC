package com.samsung.ssc.dto;

import com.google.gson.annotations.SerializedName;

public class KPIData {

	@SerializedName("NotificationType") 
	private byte NotificationType;
	@SerializedName("NotificationTypeDescription") 
	private String NotificationTypeDescription;
	@SerializedName("TotalCount") 
	private int TotalCount;
	@SerializedName("UnreadCount") 
	private int UnreadCount;

	public byte getNotificationType() {
		return NotificationType;
	}

	public void setNotificationType(byte notificationType) {
		NotificationType = notificationType;
	}

	public String getNotificationTypeDescription() {
		return NotificationTypeDescription;
	}

	public void setNotificationTypeDescription(
			String notificationTypeDescription) {
		NotificationTypeDescription = notificationTypeDescription;
	}

	public int getTotalCount() {
		return TotalCount;
	}

	public void setTotalCount(int totalCount) {
		TotalCount = totalCount;
	}

	public int getUnreadCount() {
		return UnreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		UnreadCount = unreadCount;
	}
}
