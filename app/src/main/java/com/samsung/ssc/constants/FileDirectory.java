package com.samsung.ssc.constants;

import android.os.Environment;

public class FileDirectory {

	private static final String DIRECTORY_BASE = Environment
			.getExternalStorageDirectory() + "/SMARTDOST/";
	public static final String DIRECTORY_QUESTIONNAIRE_IMAGES = DIRECTORY_BASE
			+ "DIRECTORY_QUESTIONNAIRE_IMAGES";
	public static final String DIRECTORY_GEOTAG_IMAGE = DIRECTORY_BASE
			+ "DIRECTORY_GEOTAG_IMAGE";
	public static final String DIRECTORY_ERROR_LOG = DIRECTORY_BASE
			+ "DIRECTORY_ERROR_LOG";
	public static final String DIRECTORY_FEEDBACK_IMAGES = DIRECTORY_BASE
			+ "DIRECTORY_FEEDBACK_IMAGES";
	public static final String DIRECTORY_OUTLET_IMAGES = DIRECTORY_BASE
			+ "DIRECTORY_OUTLET_IMAGES";
	public static final String DIRECTORY_SURVEY_QUESTION_IMAGES = DIRECTORY_BASE
			+ "DIRECTORY_SURVEY_QUESTION_IMAGES";
	public static final String DIRECTORY_DEALER_CREATION = DIRECTORY_BASE
			+ "DIRECTORY_DEALER_CREATION";
	public static final String DIRECTORY_DATABASE = DIRECTORY_BASE
			+ "DIRECTORY_DEALER_DATABASE";
	public static final String DIRECTORY_EXPENSE_BILL_DOCUMENTS = DIRECTORY_BASE
			+ "DIRECTORY_EXPENSE_BILL_DOCUMENTS";
	
	
}
