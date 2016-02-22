package com.samsung.ssc.util;

public class Constants {
	public static final String SENDER_ID = "181299290963";
	public static final String IS_SESSION_LOGOUT = "is_session_logout_true";
	public static final String TAG = "CE";
	public final static int QUESTION_IMAGE_WIDTH = 800;
	public final static int QUESTION_IMAGE_HEIGHT = 850;
	public final static String STORE_TYPE_TODAY = "today";
	public final static String STORE_TYPE_OTHER = "other";
	public final static String KEY_RACE_FIXTURE = "key_race_fixture";
	public final static double STORE_FENCE_DISTANCE=1.0;

//	public final static int storePermissionQuestionID = 1220; // test
	public final static int storePermissionQuestionID = 2557; // live

	// public final static int storePermissionModuleCode =

	public static class HTTPResponseCode {
		public static final int RESPONSE_CODE_OK = 200;
		public static final int RESPONSE_CODE_CREATED = 201;
		public static final int RESPONSE_CODE_ACCEPTED = 202;
	}
	
	public static class ImageSize {
		public static final int QUESTIONNAIRE_IMAGE_WIDTH = 800;
		public static final int QUESTIONNAIRE_IMAGE_HEIGHT = 850;
		public static final int QUESTIONNAIRE_IMAGE_WIDTH_THUMBNAIL = 120;
		public static final int QUESTIONNAIRE_IMAGE_HEIGHT_THUMBNAIL = 120;
	}

	public final static int ROW_COUNT_DOWNLOAD_DATA = 1000;
	
	
	public final static int SYNC_FREQUENCY=60*120; // in seconds
	
	// An account type, in the form of a domain name
		public static final String ACCOUNT_TYPE = "ssc.com";
		// The account name
		public static final String ACCOUNT = "SSC";
	
}
