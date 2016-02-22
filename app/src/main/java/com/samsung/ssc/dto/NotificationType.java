package com.samsung.ssc.dto;

public enum NotificationType {

	General(1), Beat(2), Coverage_Notification(3), FMS(4);

	private int NOTIFICATION_TYPE;

	NotificationType(int NOTIFICATION_TYPE) {
		this.NOTIFICATION_TYPE = NOTIFICATION_TYPE;
	}

	public int getNotificationType() {
		return this.NOTIFICATION_TYPE;
	}
	
	public void setnotificationType(int NOTIFICATION_TYPE) {
		this.NOTIFICATION_TYPE = NOTIFICATION_TYPE;
	}
	
	
}
