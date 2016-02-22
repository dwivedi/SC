package com.samsung.ssc.constants;

public class WebConfig {

	public static class WebMethod {

		public static String LOGIN = "CommonLoginService";
		public static String FORGET_PASSWORD = "ForgetPassword";
		public static String GET_PRODUCT_LIST = "GetProductList";
		public static String GET_PAYMENT_MODES = "GetPaymentModes";
		public static String GET_COMPETITORS = "GetCompetitors";
		public static String GET_COMPETITION_PRODUCT_GROUP = "GetCompetitionProductGroup";
		public static String DEALERS_LIST_BASED_ON_CITY = "DealersListBasedOnCity";
		public static String GET_SURVEY_QUESTIONS = "GetSurveyQuestions";
		public static String GET_PLANOGRAM_CLASS_MASTERS = "GetPlanogramClassMasters";
		public static String GET_PLANOGRAM_PRODUCT_MASTERS = "GetPlanogramProductMasters";
		public static String GET_COMPETITOR_PRODUCT_GROUP_MAPPING = "GetCompetitorProductGroupMapping";
		public static String GET_FMS_MASTERS = "GetFMSMasters";
		
		public static String GET_MSS_CATEGORY_MASTER = "GetMSSCategoryMaster";
		public static String GET_MSS_TEAM_MASTER = "GetMSSTeamMaster";
		public static String GET_MSS_TYPE_MASTER = "GetMSSTypeMaster";
		public static String GET_MSS_STATUS_MASTER = "GetMSSStatusMaster";
		
		
		public static String GET_EOL_SCHEMES = "GetEOLSchemes";
		
		public static String GET_RACE_POSM_MASTERS = "GetRACEPOSMMaster";
		public static String GET_RACE_FIXTURE_MASTER="GetRACEFixtureMaster";
		public static String GET_RACE_POSM_PRODUCT_MAPPING="GetRACEPOSMProductMapping";
		public static String GET_RACE_BRAND_MASTER="GetRaceBrandMaster";
		public static String GET_RACE_PRODUCT_CATEGORY="GetRaceProductCategory";
		public static String GET_RACE_BRAND_CATEGORY_MAPPING="GetRaceBrandCategoryMapping";
		
		
		
		public static String GET_RACE_PRODUCT_MASTERS = "GetRaceProductMasters";
		public static String SUBMIT_PLANOGRAM = "SubmitPlanogram";
		public static String SUBMIT_COLLECTION_SURVEY = "SubmitCollectionSurvey";
		public static String SUBMIT_EOL_ORDER = "SubmitEOLOrder";
		public static String UPLOAD_GEO_IMAGE_STREAM = "UploadGeoImageStream";
		public static String SAVE_STORE_SURVEY_RESPONSES = "SaveStoreSurveyResponses";
		public static String SUBMIT_PRODUCT_AUDIT = "SubmitAuditResponse";
		public static String UPDATE_PENDING_COVERAGE = "UpdatePendingCoverage";
		public static String UPDATE_USER_PROFILE = "UpdateUserProfile";
		
		public static final String GET_RLSSO_DETAILS = "GetRLSSODetails";
		public static final String GET_TODAY_SCHEMES = "GetTodaySchemes";
		public static final String DISPLAY_STORE_PROFILE = "DisplayStoreProfile";
		public static final String SUBMIT_FEEDBACKS = "SubmitFeedbacks";
		public static final String SEARCH_FEEDBACKS = "SearchFeedbacks";
		public static final String UPDATE_FEEDBACKS = "UpdateFeedbacks";
		public static final String GET_USER_STORES = "GetUserStores";
		public static final String INSERT_USER_BEAT_DETAIL_INFO = "InsertUserBeatDetailsInfo";
		public static final String GET_COVERAGE_USER = "GetCoverageUsers";

		public static final String GET_MDM_DISTRICT = "GetMDMDistrict";
		public static final String GET_MDM_CITY = "GetMDMCity";
		public static final String GET_MDM_PINCODE = "GetMDMPinCode";
		public static final String GET_MDM_PARENT_DEALER_CODE = "GetMDMParentDealerCode";
		public static final String SUBMIT_MDM_DEALER_CREATION = "SubmitMDMDealerCreation";
		public static final String UPLOAD_MDM_DEALER_CREATIONIMAGE = "UploadMDMDealerCreationImage";
		public static final String GET_MDM_DUPLICATE_CHECK = "GetMDMDuplicateCheck";
		public static final String GET_MDMDEALER_HISTORY = "GetMDMDealerHistory";
		public static final String GET_PARENT_DISTRIBUTER = "GetParentDistributer";
		public static final String LAST_SAVED_EOL_ACTIVITY = "LastsavedEOLActivity";
		
		
		public static final String UPDATE_STORE_PROFILE="UpdateStoreProfile";
		public static final String GET_USER_MODULES="GetUserModules";
		
		
		
		public static final String SUBMIT_MOM_LIST="SubmitMOMList";
		public static final String GET_USER_MOM_DATA="GetUserMOMData";
		

		public static final String GET_STORE_PERFORMANCE="GetStorePerformanceByStoreID";

		public static String GET_LEAVE_DATA = "GetLeaves";
		public static String GET_LEAVE_TYPE_COFIGURATION = "GetLeaveTypeCofiguration";
		public static String GET_LEAVE_TYPE_MASTER = "GetLeaveTypeMaster";


		public static String SUMBIT_LEAVE_REQUEST = "SumbitLeaveRequest";

		// Expense module

		public static final String GET_EXPENCE_MASTER_DATA ="GetExpenseMasterData";
		public static final String GET_SUBMIT_EXPENSE_AND_BILL ="SubmitExpenseAndBill";
		public static final String GET_UPLOAD_BILL_IMAGE_STREAM ="UploadBillImageStream";
		public static final String GET_EXPENSE_LIST ="GetExpenseList";
	}

	public static class WebParams {

		public static final String IMEI = "imei";
		public static final String CONFIRMATION_ID = "employeeID";
		public static final String NEW_PASSWORD = "newPassword";
		public static final String APK_VERSION = "apkVersion";
		public static final String PASSWORD = "password";
		public static final String LATTITUDE = "Lattitude";
		public static final String LONGITUDE = "Longitude";
		public static final String MODEL_NAME = "ModelName";
		public static final String IP_ADDRESS = "IPAddress";
		public static final String BROWSER_NAME = "BrowserName";
		public static final String REQUIRE_ANNOUNCEMENT = "RequireAnnouncement";
		public static final String SIDTO = "SIDTO";
		public static final String USER_ID = "userID";
		public static final String USER_ID_CAPS = "UserID";
		public static final String COMPANY_ID = "companyID";
		public static final String COMPANY_ID_CAPS = "CompanyID";
		public static final String ROLE_ID = "roleID";
		public static final String ROLE_ID_CAPS = "RoleID";
		public static final String APIKEY = "APIKey";
		public static final String APITOKEN = "APIToken";
		public static final String LAST_CATEGORY_ID = "LastCategoryID";
		public static final String LAST_TYPE_ID = "LastTypeID";
		public static final String ROW_COUNTER = "rowcounter";
		public static final String LAST_TEAM_ID = "LastTeamID";
		public static final String SURVEY_RESPONSE_ID = "SurveyResponseID";
		public static final String AUDIT_RESPONSE = "auditResponse";
		public static final String STOCK_AUDIT_SUMMARY = "StockAuditSummary";
		public static final String QUESTION_TYPE_ID = "QuestionTypeID";
		public static final String SURVEYRESPONSEID = "SurveyResponseID";
		public static final String SURVEYQUESTIONID = "SurveyQuestionID";
		public static final String SURVEY_QUESTION_SUB_ID = "SurveyQuestionSubID";
		public static final String USERRESPONSE = "UserResponse";
		public static final String ORDERBOOKINGID = "OrderBookingID";
		public static final String PRODUCTTYPEID = "ProductTypeID";
		public static final String PRODUCTGROUPID = "ProductGroupID";
		public static final String PRODUCTCATEGORYID = "ProductCategoryID";
		public static final String ORDERNO = "OrderNo";
		public static final String PRODUCTID = "ProductID";
		public static final String QUANTITY = "Quantity";
		public static final String ORDER_TYPE = "OrderBookingType";
		public static final String PRICE = "Price";
		public static final String SKUCODE = "SKUCode";
		public static final String STOREID = "storeID";
		public static final String STORE_ID_CAPS = "StoreID";
		public static final String ISATTENDANCE = "IsAttendance";
		public static final String NOOFDAYS = "numberOfDays";
		public static final String REMARKS = "Remarks";
		public static final String COVERAGEPLANID = "CoverageID";
		public static final String USERATTENDANCE = "userAttendance";
		public static final String PLANOGRAM_PRODUCT_MASTER_ID = "PlanogramProductMasterID";
		public static final String IMAGE = "image";
		public static final String USERROLEID = "userRoleID";
		public static final String USER_ROLE_ID_CAPS = "UserRoleID";
		public static final String USER_ROLE_ID = "userRoleID";
		public static final String FIRST_NAME = "FirstName";
		public static final String LAST_NAME = "LastName";
		public static final String MOBILE_CALLING = "Mobile_Calling";
		public static final String Mobile_SD = "Mobile_SD";
		public static final String EMAIL_ID = "EmailID";
		public static final String ALTERNATE_EMAIL_ID = "AlternateEmailID";
		public static final String ADDRESS = "Address";
		public static final String PINCODE = "Pincode";
		public static final String PROFILE_PICTURE_FILE_NAME = "ProfilePictureFileName";
		public static final String USER_PROFILE = "userProfile";
		public static final String KEY_SO_NUMBER = "soNumber";
		public static final String RACE_PROFILE = "RaceProfile";
		public static final String ASSESSMENT_START_TIME = "strAssesmentStartTime";
		public static final String ASSESSMENT_END_TIME = "strAssesmentEndTime";
		public static final String STORE_SURVEY = "storeSurvey";

		public static final String DISTRICT = "district";
		public static final String CITY = "city";
		public static final String DEALERCREATION = "dealerCreation";
		public static final String EMPLCODE = "emplCode";
		public static final String FIRM_MOBILE = "FirmMobile";
		public static final String FIRM_EMAIL = "FirmEmail";
		public static final String PAN = "Pan";
		public static final String TIN = "Tin";
		public static final String IS_CHILD = "Ischild";
		public static final String RETURN_ALL_SCHEMES = "returnAllSchemes";
		public static final String SCHEME_ID = "schemeID";
		public static final String ROW_COUNT = "RowCount";
		public static final String START_ROW_INDEX = "StartRowIndex";
		public static final String LAST_UPDATED_DATE = "LastUpdatedDate";
		
		
		public static final String PICTURE_FILE_NAME = "PictureFileName";
		public static final String STORE_ID = "StoreID";
		public static final String CONTACT_PERSON = "ContactPerson";
		public static final String MOBILE_NO = "MobileNo";
		public static final String STORE_EMAIL_ID = "EmailID";
		public static final String STORE_ADDRESS = "storeAddress";
		
		public static final String LAND_LINE_NUMBER ="LandLineNumber";
		public static final String SPC_NAME = "SPCName";
		public static final String SPC_CATEGORY = "SPCCategory";
		public static final String STORE_MANAGER_NAME = "StoreManagerName";
		public static final String SM_MOBILE = "SMMobile";
		public static final String SM_EMAILID = "SMEmailID";
		
		public static final String STORE_PROFILE = "storeProfile";
		
		
		
		
		
		
		
		

	}

}
