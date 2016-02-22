package com.samsung.ssc.provider;

import android.net.Uri;
import android.provider.BaseColumns;

import com.samsung.ssc.database.DatabaseConstants;

/**
 * The contract between clients and the lentitems content provider.
 * 
 * @author Wolfram Rittmeyer
 */
public final class ProviderContract {

	public static String PATH_USER_PROFILE = "UserProfile";

	public static String PATH_INSERT_SURVET_QUESTIONS = "InsertSurveyQuestions";

	public static String PATH_INSERT_SURVET_QUESTION_OPTIONS = "InsertSurveyQuestionOptions";

	public static String PATH_GET_SURVEY_QUESTIONS = "GetSurveyQuestions";

	public static String PATH_DELETE_SURVREY_QUESTION_ANSWER_RESPONSE = "DeleteSurveyQuestionAnswerResponse";

	public static String PATH_INSERT_SURVEY_QUESTION_ANSWER_RESPONSE = "InsertSurveyQuestionAnswerResponse";

	public static String PATH_SURVEY_QUESTION_ANSWER_RESPONSE = "QuestionAnswerResponse";

	public static String PATH_STORES_BASICS = "StoresBasics";
	public static String PATH_GET_OTHER_STORES_BASICS_BY_CITY = "OtherStoresBasicsByCity";
	public static String PATH_GET_OTHER_STORES_CITIES = "OtherStroesCities";
	public static String PATH_DELETE_OTHER_STORE = "DeleteOtherStore";

	public static String PATH_STORE_PERFORAMCE = "PathStorePerformace";

	public static String PATH_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE = "CounterShareDisplayShareResponse";

	public static String PATH_COMP_PRODUCT_GROUP = "CompProductGroup";

	public static String PATH_STOCK_ESCALATION_PRODUCT_CATAGORY_P1 = "ProductCatagoryP1";

	public static String PATH_STOCK_ESCALATION_PRODUCT_CATAGORY_P2 = "ProductCatagoryP2";

	public static String PATH_STOCK_ESCALATION_ORDER_RESPONSE_URI = "StockEscalationOrderResponse";

	public static String PATH_STOCK_ESCALATION_SKU = "StockEscalationSKU";

	public static String PATH_STOCK_ESCALATION_RESPONSE = "StockEscalationResponse";

	public static String PATH_DELETE_STOCK_ESCALATION_RESPONSE = "DeleteStockEscalationResponse";

	public static String PATH_USER_MODULE = "UserModule";

	public static String PATH_STORE_COUNT = "StoreCount";

	public static String PATH_PLANOGRAM_RESPONSE = "PlanogramResponse";

	public static String PATH_PLANOGRAM_PRODUCT_RESPONSE = "PlanogramProductResponse";

	public static String PATH_PLANOGRAM_COMPETITORS_RESPONSE = "PlanogramCompetitorsResponse";

	public static String PATH_EOL_SCHEME_RESPONSE = "EOL_SCHEME_RESPONSE";
	public static String PATH_EOL_SCHEME_DETAILS_RESPONSE = "EOL_SCHEME_DETAILS_RESPONSE";

	public static String PATH_FEEDBACK_STATUS = "FeebackStatus";

	public static String PATH_FEEDBACK_TEAM = "FeebackTeam";

	public static String PATH_FEEDBACK_CATEGORY = "FeednackCategory";

	public static String PATH_FEEDBACK_TYPE = "FeedbackType";

	public static String PATH_USER_FEEDBACK_STORE_WISE = "UserFeedbackStoreWise";

	public static String PATH_USER_FEEDBACK_ENTRY_CHECK = "UserFeedbackEntryCheck";

	public static String PATH_USER_FEEDBACK_ALL = "UserFeedbackAll";

	public static String PATH_PAYMENT_MODES = "PaymentModes";

	public static String PATH_COLLECTION_RESPONSE = "CollectionResponse";

	public static String PATH_DOWNLOAD_DATA = "DownloadData";

	public static String PATH_STORE_GEO_TAG_RESPONSE = "StoreGeoTagResponse";

	public static String PATH_ACTIVITY_DATA_RESPONSE = "ActivityDataMaster";

	// race
	public static String PATH_RACE_BRAND_CATEGORY_MAPPING = "BrandCategoryMapping";

	public static String PATH_RACE_BRAND = "Brand";

	public static String PATH_RACE_FIXTURE = "Fixture";

	public static String PATH_RACE_POSM = "POSM";

	public static String PATH_RACE_PRODUCT_CATEGORY = "ProductCategory";

	public static String PATH_RACE_POSM_PRODUCT_MAPPING = "POSMProductMapping";

	public static String PATH_RACE_BRAND_PRODUCT = "BrandProduct";

	public static String PATH_RACE_PRODUCT_AUDIT_RESPONSE = "ProductAuditResponse";

	public static String PATH_RACE_PRODUCT_AUDIT_RESPONSE_BY_AUDIT_ID = "ProductAuditResponseByAuditID";

	public static String PATH_RACE_POSM_RESPONSE = "POSMResponse";

	public static String PATH_RACE_POSM_RESPONSE_WITH_ID = "POSMResponseWithID";

	public static String PATH_RACE_PRODUCT_AUDIT_CART_AV_PRODUCTS = "RaceProductAuditCartAVProducts";

	public static String PATH_RACE_PRODUCT_AUDIT_CART_HA_PRODUCTS = "RaceProductAuditCartHAProducts";

	public static String PATH_RACE_PRODUCT_AUDIT_RESPONSE_FOR_SYNC = "RaceProductAuditResponseForSync";

	public static String PATH_EOL_ORDER_BOOKING_RESPONSE_MASTER = "PathEolOrderBookingResponseMaster";

	public static String PATH_PRODUCTS = "Products";

	public static String PATH_DOWNLOAD_DATA_SINGLE_SERVICE = "DownloadDataSingleService";

	public static String PATH_COMPETITORS = "Competitors";

	public static String PATH_PLANOGRAM_CLASS = "PalnogramClass";

	public static String PATH_PLANOGRAM_PRODUCT = "PlanogramProduct";

	public static String PATH_COMPETITOR_PRODUCT_GRUOP_MAPPING = "CompetitorProductGroupMapping";

	public static String PATH_EOL_SCHEME_DETAIL = "EOLSchemeDetail";

	public static String PATH_EOL_SCHEME_HEADER = "EOLSchemeHeader";

	public static String PATH_STORE_MODULE_MASTER = "StoreModuleMaster";

	public static String PATH_MODULE_DATA_DOWNLOAD_STATUS = "ModuleDataDownloadStatus";
	
	public static String PATH_STORE_PERFORAMCE_SECTION_1 = "PathStorePerformaceSection1";
	
	public static String PATH_STORE_PERFORAMCE_SECTION_2 = "PathStorePerformaceSection2";
	
	public static String PATH_STORE_PERFORAMCE_SECTION_3 = "PathStorePerformaceSection3";

	public static String PATH_STORE_PERFORAMCE_SECTION_4 = "PathStorePerformaceSection4";
	
	public static String PATH_MOM_DETAILS_MASTER = "MOMDetailsMaster";
	
	public static String PATH_MOM_ATTENDEES_MASTER = "MOMAttendeesMaster";


	public static String PATH_LMS_LIST = "LMSLeaveMaster";

	public static String PATH_LMS_DATE_LIST = "LMSLeaveDateMaster";

	public static String PATH_LMS_STATUS_LOG = "LMSStatusLog";

	public static String PATH_LMS_LEAVE_TYPE = "LMSLeaveTypeMaster";

	public static String PATH_LMS_LEAVE_CONFIGURATION = "LMSLeaveTypeConfiguration";


	public static String PATH_EXPENSE_TYPE_MASTER = "ExpenseTypeMaster";
	public static String PATH_EMS_EXPENSE_DETAIL = "EMSExpenseDetail";
	public static String PATH_EMS_BILL_DETAIL = "EMSBillDetail";
	public static String PATH_EMS_BILL_DOCUMENT_DETAIL = "EMSBillDocumentDetail";

	/**
	 * The authority of the lentitems provider.
	 */
	public static final String AUTHORITY = "com.samsung.ssc.provider";
	/**
	 * The content URI for the top-level lentitems authority.
	 */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
	/**
	 * A selection clause for ID based queries.
	 */
	public static final String SELECTION_ID_BASED = BaseColumns._ID + " = ? ";

	public static final Uri SURVEY_QUESTION_CONTENT_URI = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI, PATH_INSERT_SURVET_QUESTIONS);

	public static final Uri SURVEY_QUESTION_OPTIONS_CONTENT_URI = Uri
			.withAppendedPath(ProviderContract.CONTENT_URI,
					PATH_INSERT_SURVET_QUESTION_OPTIONS);

	public static final Uri GET_SURVEY_QUESTIONS_CONTENT_URI = Uri
			.withAppendedPath(ProviderContract.CONTENT_URI,
					PATH_GET_SURVEY_QUESTIONS);

	public static final Uri DELELTE_SURVEY_QUESTION_ANSWER_RESPONSE_URI = Uri
			.withAppendedPath(ProviderContract.CONTENT_URI,
					PATH_DELETE_SURVREY_QUESTION_ANSWER_RESPONSE);

	public static final Uri INSERT_SURVEY_QUESTION_ANSWER_RESPONSE_URI = Uri
			.withAppendedPath(ProviderContract.CONTENT_URI,
					PATH_INSERT_SURVEY_QUESTION_ANSWER_RESPONSE);

	public static final Uri URI_SURVEY_QUESTION_ANSWER_RESPONSE = Uri
			.withAppendedPath(CONTENT_URI, PATH_SURVEY_QUESTION_ANSWER_RESPONSE);

	public static final Uri URI_STORES_BASICS = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI, PATH_STORES_BASICS);

	public static final Uri URI_OTHER_STORES_CITIES = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI, PATH_GET_OTHER_STORES_CITIES);
	public static final Uri URI_GET_OTHER_STORES_BASICS_BY_CITY = Uri
			.withAppendedPath(ProviderContract.CONTENT_URI,
					PATH_GET_OTHER_STORES_BASICS_BY_CITY);

	public static final Uri DELETE_OTHER_STORE_URI = Uri.withAppendedPath(
			CONTENT_URI, PATH_DELETE_OTHER_STORE);

	public static final Uri USER_PROFILE_URI = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI,
			DatabaseConstants.TableNames.TABLE_USER_PROFILE);

	public static final Uri URI_STORE_PERFORMANCE = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI, PATH_STORE_PERFORAMCE);

	public static final Uri URI_COUTER_SHARE_DISPLAY_SHARE_RESPONSE = Uri
			.withAppendedPath(ProviderContract.CONTENT_URI,
					PATH_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE);

	public static final Uri URI_COMP_PRODUCT_GROUP = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI, PATH_COMP_PRODUCT_GROUP);

	public static final Uri STOCK_ESCALATION_PRODUCT_CATAGORY_P1_URI = Uri
			.withAppendedPath(ProviderContract.CONTENT_URI,
					PATH_STOCK_ESCALATION_PRODUCT_CATAGORY_P1);

	public static final Uri STOCK_ESCALATION_PRODUCT_CATAGORY_P2_URI = Uri
			.withAppendedPath(ProviderContract.CONTENT_URI,
					PATH_STOCK_ESCALATION_PRODUCT_CATAGORY_P2);

	public static final Uri STOCK_ESCALATION_ORDER_RESPONSE_URI = Uri
			.withAppendedPath(ProviderContract.CONTENT_URI,
					PATH_STOCK_ESCALATION_ORDER_RESPONSE_URI);

	public static final Uri STOCK_ESCALATION_FIND_SKU = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI, PATH_STOCK_ESCALATION_SKU);

	public static final Uri INSERT_STOCK_ESCALATION_ORDER_RESPONSE = Uri
			.withAppendedPath(ProviderContract.CONTENT_URI,
					PATH_STOCK_ESCALATION_RESPONSE);

	public static final Uri DELETE_STOCK_ESCALATION_ORDER_RESPONSE = Uri
			.withAppendedPath(ProviderContract.CONTENT_URI,
					PATH_DELETE_STOCK_ESCALATION_RESPONSE);

	public static final Uri STORE_COUNT_WITH_SUBMIT_DATA_URI = Uri
			.withAppendedPath(ProviderContract.CONTENT_URI, PATH_STORE_COUNT);

	public static final Uri USER_MODULES_URI = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI, PATH_USER_MODULE);

	public static final Uri URI_PLANOGRAM_RESPONSE_URI = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI, PATH_PLANOGRAM_RESPONSE);

	public static final Uri URI_PLANOGRAM_PRODUCT_RESPONSE_URI = Uri
			.withAppendedPath(ProviderContract.CONTENT_URI,
					PATH_PLANOGRAM_PRODUCT_RESPONSE);

	public static final Uri URI_PLANOGRAM_COMPETITORS_RESPONSE_URI = Uri
			.withAppendedPath(ProviderContract.CONTENT_URI,
					PATH_PLANOGRAM_COMPETITORS_RESPONSE);

	public static final Uri URI_EOL_SCHEME_RESPONSE = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI, PATH_EOL_SCHEME_RESPONSE);
	public static final Uri URI_EOL_SCHEME_DETAILS_RESPONSE_URI = Uri
			.withAppendedPath(ProviderContract.CONTENT_URI,
					PATH_EOL_SCHEME_DETAILS_RESPONSE);

	public static final Uri URI_FEEDBACK_STATUS = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI, PATH_FEEDBACK_STATUS);

	public static final Uri URI_FEEDBACK_TEAMS = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI, PATH_FEEDBACK_TEAM);

	public static final Uri URI_FEEDBACK_CATEGORY = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI, PATH_FEEDBACK_CATEGORY);

	public static final Uri URI_FEEDBACK_TYPE = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI, PATH_FEEDBACK_TYPE);

	public static final Uri URI_USER_FEEDBACK = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI, PATH_USER_FEEDBACK_STORE_WISE);

	public static final Uri URI_USER_FEEDBACK_ENTRY_CHECK = Uri
			.withAppendedPath(ProviderContract.CONTENT_URI,
					PATH_USER_FEEDBACK_ENTRY_CHECK);

	public static final Uri URI_USER_FEEDBACK_ALL = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI, PATH_USER_FEEDBACK_ALL);

	public static final Uri URI_DOWNLOAD_DATA = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI, PATH_DOWNLOAD_DATA);

	public static final Uri URI_STORE_GEO_TAG_RESPONSE = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI, PATH_STORE_GEO_TAG_RESPONSE);

	public static final Uri URI_COLLECTION_RESPONSE = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI,
			ProviderContract.PATH_COLLECTION_RESPONSE);

	public static final Uri URI_PAYMENT_MODES = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI, ProviderContract.PATH_PAYMENT_MODES);

	public static final Uri URI_ACTIVITY_DATA_RESPONSE = Uri.withAppendedPath(
			ProviderContract.CONTENT_URI, PATH_ACTIVITY_DATA_RESPONSE);

	// Race URI

	public static final Uri URI_RACE_BRAND_CATEGORY_MAPPING = Uri
			.withAppendedPath(CONTENT_URI, PATH_RACE_BRAND_CATEGORY_MAPPING);

	public static final Uri URI_RACE_BRAND = Uri.withAppendedPath(CONTENT_URI,
			PATH_RACE_BRAND);

	public static final Uri URI_RACE_FIXTURE = Uri.withAppendedPath(
			CONTENT_URI, PATH_RACE_FIXTURE);

	public static final Uri URI_RACE_POSM = Uri.withAppendedPath(CONTENT_URI,
			PATH_RACE_POSM);

	public static final Uri URI_RACE_PRODUCT_CATEGORY = Uri.withAppendedPath(
			CONTENT_URI, PATH_RACE_PRODUCT_CATEGORY);

	public static final Uri URI_RACE_POSM_PRODUCT_MAPPING = Uri
			.withAppendedPath(CONTENT_URI, PATH_RACE_POSM_PRODUCT_MAPPING);

	public static final Uri URI_RACE_BRAND_PRODUCT = Uri.withAppendedPath(
			CONTENT_URI, PATH_RACE_BRAND_PRODUCT);

	public static final Uri URI_RACE_PRODUCT_AUDIT_RESPONSE = Uri
			.withAppendedPath(CONTENT_URI, PATH_RACE_PRODUCT_AUDIT_RESPONSE);

	public static final Uri URI_RACE_PRODUCT_AUDIT_RESPONSE_BY_AUDIT_ID = Uri
			.withAppendedPath(CONTENT_URI,
					PATH_RACE_PRODUCT_AUDIT_RESPONSE_BY_AUDIT_ID);

	public static final Uri URI_RACE_POSM_RESPONSE = Uri.withAppendedPath(
			CONTENT_URI, PATH_RACE_POSM_RESPONSE);

	public static final Uri URI_RACE_POSM_RESPONSE_WITH_ID = Uri
			.withAppendedPath(CONTENT_URI, PATH_RACE_POSM_RESPONSE_WITH_ID);

	public static final Uri URI_RACE_PRODUCT_AUDIT_CART_AV_PRODUCTS = Uri
			.withAppendedPath(CONTENT_URI,
					PATH_RACE_PRODUCT_AUDIT_CART_AV_PRODUCTS);

	public static final Uri URI_RACE_PRODUCT_AUDIT_CART_HA_PRODUCTS = Uri
			.withAppendedPath(CONTENT_URI,
					PATH_RACE_PRODUCT_AUDIT_CART_HA_PRODUCTS);

	public static final Uri URI_RACE_PRODUCT_AUDIT_RESPONSE_FOR_SYNC = Uri
			.withAppendedPath(CONTENT_URI,
					PATH_RACE_PRODUCT_AUDIT_RESPONSE_FOR_SYNC);
	public static final Uri URI_EOL_ORDER_BOOKING_RESPONE = Uri
			.withAppendedPath(CONTENT_URI,
					PATH_EOL_ORDER_BOOKING_RESPONSE_MASTER);

	public static final Uri URI_PRODUCTS = Uri.withAppendedPath(CONTENT_URI,
			PATH_PRODUCTS);

	public static final Uri URI_DOWNLOAD_DATA_SINGLE_SERVICE = Uri
			.withAppendedPath(CONTENT_URI, PATH_DOWNLOAD_DATA_SINGLE_SERVICE);

	public static final Uri URI_COMPETITORS = Uri.withAppendedPath(CONTENT_URI,
			PATH_COMPETITORS);

	public static final Uri URI_PALNOGRAM_CLASS = Uri.withAppendedPath(
			CONTENT_URI, PATH_PLANOGRAM_CLASS);

	public static final Uri URI_PALNOGRAM_PRODUCT = Uri.withAppendedPath(
			CONTENT_URI, PATH_PLANOGRAM_PRODUCT);

	public static final Uri URI_COMPETITOR_PRODUCT_GROUP_MAPPING = Uri
			.withAppendedPath(CONTENT_URI,
					PATH_COMPETITOR_PRODUCT_GRUOP_MAPPING);

	public static final Uri URI_EOL_SCHEME_DETAIL = Uri.withAppendedPath(
			CONTENT_URI, PATH_EOL_SCHEME_DETAIL);

	public static final Uri URI_EOL_SCHEME_HEADER = Uri.withAppendedPath(
			CONTENT_URI, PATH_EOL_SCHEME_HEADER);

	public static final Uri URI_STORE_MODULE_MASTER = Uri.withAppendedPath(
			CONTENT_URI, PATH_STORE_MODULE_MASTER);

	public static final Uri URI_MODULE_DATA_DOWNLOAD_STATUS = Uri.withAppendedPath(
			CONTENT_URI, PATH_MODULE_DATA_DOWNLOAD_STATUS);
	
	
	public static final Uri URI_STORE_PERFORMANCE_SECTION_1 = Uri.withAppendedPath(
			CONTENT_URI, PATH_STORE_PERFORAMCE_SECTION_1);
	
	
	public static final Uri URI_STORE_PERFORMANCE_SECTION_2 = Uri.withAppendedPath(
			CONTENT_URI, PATH_STORE_PERFORAMCE_SECTION_2);
	
	
	public static final Uri URI_STORE_PERFORMANCE_SECTION_3 = Uri.withAppendedPath(
			CONTENT_URI, PATH_STORE_PERFORAMCE_SECTION_3);

	public static final Uri URI_STORE_PERFORMANCE_SECTION_4 = Uri.withAppendedPath(
			CONTENT_URI, PATH_STORE_PERFORAMCE_SECTION_4);
	
	public static final Uri URI_MOM_DETAILS = Uri.withAppendedPath(
			CONTENT_URI, PATH_MOM_DETAILS_MASTER);
	
	public static final Uri URI_MOM_ATTENDEES = Uri.withAppendedPath(
			CONTENT_URI, PATH_MOM_ATTENDEES_MASTER);


	public static final Uri URI_LMS_LIST = Uri.withAppendedPath(
			CONTENT_URI, PATH_LMS_LIST);


	public static final Uri URI_LMS_DATE_LIST = Uri.withAppendedPath(
			CONTENT_URI, PATH_LMS_DATE_LIST);

	public static final Uri URI_LMS_STATUS_LOG= Uri.withAppendedPath(
			CONTENT_URI, PATH_LMS_STATUS_LOG);

	public static final Uri URI_LMS_LEAVE_CONFIGURATION= Uri.withAppendedPath(
			CONTENT_URI, PATH_LMS_LEAVE_CONFIGURATION);

	public static final Uri URI_LMS_LEAVE_TYPE= Uri.withAppendedPath(
			CONTENT_URI, PATH_LMS_LEAVE_TYPE);


	public static final Uri URI_EXPENSE_TYPE_MASTER = Uri.withAppendedPath(CONTENT_URI,
			PATH_EXPENSE_TYPE_MASTER);

	public static final Uri URI_EMS_EXPENSE_DETAIL = Uri.withAppendedPath(CONTENT_URI,
			PATH_EMS_EXPENSE_DETAIL);

	public static final Uri URI_EMS_BILL_DETAIL = Uri.withAppendedPath(CONTENT_URI,
			PATH_EMS_BILL_DETAIL);

	public static final Uri URI_EMS_BILL_DOCUMENT_DETAIL = Uri.withAppendedPath(CONTENT_URI,
			PATH_EMS_BILL_DOCUMENT_DETAIL);


}
