package com.samsung.ssc.database;

public class DatabaseConstants {

    public static interface TableNames {

        String TABLE_USER_PROFILE = "UserProfile";
        String TABLE_SURVEY_QUESTION_MASTERS = "SurveyQuestionMaster";
        String TABLE_SURVEY_QUESTION_OPTION_MASTERS = "SurveyQuestionOptionMaster";
        String TABLE_STORE_BASIC_MASTER = "StoreBasicMaster";
        String TABLE_STORE_PERFORMANCE_MASTER = "StorePerformanceMaster";
        String TABLE_COMPETETION_PRODUCT_GROUP = "CompProductGroups";
        String TABLE_COMPETITOR_MASTER = "CompetitorMaster";
        String TABLE_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE_MASTER = "CounterShareDisplayShareResponseMaster";
        String TABLE_USER_MODULE_MASTER = "UserModuleMaster";
        String TABLE_PRODUCT_MASTER = "ProductMaster";
        String TABLE_PAYMENT_MODE_MASTER = "PaymentModeMaster"; // 2(B)
        String TABLE_FMS_TEAM_MASTER = "TeamMaster";
        String TABLE_FEEDBACK_CATEGORY_MASTER = "FeedbackCategoryMaster";
        String TABLE_FEEDBACK_TYPE_MASTER = "FeedbackTypeMaster";
        String TABLE_FEEDBACK_STATUS_MASTER = "FeedbackStatusMaster";
        String TABLE_COMPETITOR_PRODUCT_GROUP_MAPPING = "CompetitorProductGroupMapping";
        String TABLE_PLANOGRAM_PRODUCT_MASTER = "PlanogramProductMaster";
        String TABLE_PLANOGRAM_CLASS_MASTER = "PlanogramClassMaster";

        String TABLE_EOL_SCHEME_HEADER_MASTER = "EOLSchemeHeaderMaster";
        String TABLE_EOL_SCHEME_DETAILS_MASTER = "EOLSchemeDetailsMaster";
        String TABLE_EOL_ORDER_BOOKING_RESPONSE_MASTER = "EOLOrderBookingResponseMaster";

        String TABLE_DOWNLOAD_DATA_MASTER = "DownloadDataMaster";
        String TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER = "DownloadDataModuleMappingMaster";

        // Expense module
        String TABLE_EXPENSE_TYPE_MASTER = "ExpenseTypeMaster";


        // Response tables
        String TABLE_ACTIVITY_DATA_MASTER = "ActivityDataMaster";
        String TABLE_STORE_ASSESSMENT_MASTER = "StoreAssessmentMaster";
        String TABLE_QUESTION_ANSWER_RESPONSE_MASTER = "QuestionAnswerResponseMaster";
        String TABLE_COLLECTION_RESPONSE_MASTER = "CollectionResponseMaster";
        String TABLE_USER_FEEDBACK_MASTER = "UserFeedbackMaster";
        String TABLE_ORDER_RESPONSE_MASTER = "OrderResponseMaster";
        String TABLE_PLANOGRAM_RESPONSE_MASTER = "PlanogramResponseMaster";

        String TABLE_PLANOGRAM_PRODUCT_RESPONSE_MASTER = "PlanogramProductResponseMaster";
        String TABLE_PLANOGRAM_COMPITIOR_RESPONSE_MASTER = "PlanogramCompititorResponseMaster";
        String TABLE_STORE_GEO_TAG_RESPONSE = "StoreGeoTagMaster";

        String TABLE_RACE_BRAND_MASTER = "RaceBrandMaster";
        String TABLE_RACE_BRAND_PRODUCT_CATEGORY_MASTER = "RaceBrandProductCategoryMaster";
        String TABLE_RACE_BRAND_CATEGORY_MAPPING_MASTER = "RaceBrandCategoryMappingMaster";
        String TABLE_RACE_POSM_MASTER = "RacePOSMMaster";
        String TABLE_RACE_FIXTURE_MASTER = "RaceFixtureMaster";
        String TABLE_RACE_BRAND_PRODUCT_MASTER = "RaceProductMaster";
        String TABLE_RACE_PRODUCT_POSM_MAPPING_MASTER = "RaceProductPosmMappingMaster";
        String TABLE_RACE_PRODUCT_AUDIT_RESPONSE_MASTER = "RaceProductAuditResponse";
        String TABLE_RACE_POSM_DATA_RESPONSE_MASTER = "RacePosmDataResponse";

        //
        String TABLE_STORE_MODULE_MASTER = "StoreModuleMaster";

        String TABLE_MOM_DETAILS_MASTER = "MOMDetailsMaster";
        String TABLE_MOM_ATTENDEES_MASTER = "MOMAttendeesMaster";


        String TABLE_LMS_LEAVE_MASTER = "LMSLeaveMaster";
        String TABLE_LMS_LEAVE_DATE_MASTER = "LMSLeaveDateMaster";
        String TABLE_LMS_LEAVE_TYPE_MASTER = "LMSLeaveTypeMaster";
        String TABLE_LMS_STATUS_LOG = "LMSStatusLog";
        String TABLE_LMS_LEAVE_TYPE_CONFIGURATION = "LMSLeaveTypeConfiguration";

        // Expense module
        String TABLE_EMS_EXPENSE_DETAIL = "EMSExpenseDetail";
        String TABLE_EMS_BILL_DETAIL = "EMSBillDetail";
        String TABLE_EMS_BILL_DOCUMENT_DETAIL = "EMSBillDocumentDetail";

    }


    public interface LMSLeaveMasterColumn {

        String KEY_LEAVE_MASTER_ID = "LeaveMasterID";
        String KEY_LEAVE_SUBJECT = "LeaveSubject";
        String KEY_LEAVE_APPLIED_DATE = "AppliedDate";
        String KEY_LEAVE_CREATED_BY = "CreatedBy";
        String KEY_LEAVE_CREATED_BY_USER_NAME = "CreatedByUserName";
        String KEY_LEAVE_CREATED_DATE = "CreatedDate";
        String KEY_LEAVE_CURRENT_STATUS = "CurrentStatus";
        String KEY_LEAVE_MODIFIED_BY = "ModifiedBy";
        String KEY_LEAVE_MODIFIED_BY_USER_NAME = "ModifiedByUserName";
        String KEY_LEAVE_MODIFIED_DATE = "ModifiedDate";
        String KEY_LEAVE_NUMBER_OF_LEAVE = "NumberOfLeave";
        String KEY_LEAVE_PENDING_WITH = "PendingWith";
        String KEY_LEAVE_PENDING_WITH_USER_NAME = "PendingWithUserName";
        String KEY_LEAVE_REMARKS = "Remarks";
        String KEY_LEAVE_TYPE_ID = "LMSLeaveTypeMasterID";

    }

    public interface LMSLeaveDetailsMasterColumn {
        String KEY_LEAVE_DETAIL_ID = "LMSLeaveDetailID";
        String KEY_LEAVE_MASTER_ID = "LeaveMasterID";
        String KEY_LEAVE_CURRENT_STATUS = "CurrentStatus";
        String KEY_IS_HALF_DAY = "IsHalfDay";
        String KEY_LEAVE_DATE = "LeaveDate";
        String KEY_LEAVE_DATE_TEXT = "LeaveDateText";
        String KEY_LEAVE_MODIFIED_BY = "ModifiedBy";
        String KEY_LEAVE_MODIFIED_BY_USER_NAME = "ModifiedByUserName";
        String KEY_LEAVE_CREADTED_BY = "CreatedBy";

        String KEY_LEAVE_MODIFIED_DATE = "ModifiedDate";
    }

    public interface LMSLeaveTypeMasterColumn {
        String KEY_LEAVE_TYPE_ID = "LMSLeaveTypeMasterID";
        String KEY_LEAVE_TYPE = "LeaveType";
        String KEY_LEAVE_TYPE_CODE = "LeaveTypeCode";
        String KEY_LEAVE_TAKEN = "LeavesTaken";
     }

    public interface LMSStatusLogColumn {
        String KEY_LMS_STATUS_LOG_ID = "LMSStatusLogID";
        String KEY_LEAVE_MASTER_ID = "LeaveMasterID";
        String KEY_LMS_STATUS_CREATED_BY = "CreatedBy";
        String KEY_LMS_STATUS_CREATED_BY_USER_NAME = "CreatedByUserName";
        String KEY_LMS_STATUS_CREATED_DATE = "CreatedDate";
        String KEY_LMS_STATUS_CURRENT_STATUS = "CurrentStatus";
        String KEY_LMS_STATUS_REMARKS = "Remarks";

    }

    public interface LMSLeaveTypeConfigurationColumn {
        String KEY_LMS_LEAVE_TYPE_CONFIGURATION_ID = "LMSLeaveTypeConfigurationID";
        String KEY_LEAVE_TYPE_ID = "LMSLeaveTypeMasterID";
        String KEY_CONFIGURATION_VALUE = "ConfigurationValue";
        String KEY_CONFIGURATION_TEXT = "ConfigurationText";
        String KEY_CONFIG_VALUE = "ConfigValue";

    }


    public interface MOMDetailsMasterColumns {
        String KEY_MOM_ID = "MOMId";
        String KEY_MOM_TITLE = "MOMTitle";
        String KEY_MOM_DATE_TEXT = "MOMDate";
        String KEY_MOM_DATE_VALUE = "MOMDateValue";
        String KEY_MOM_LOCATION = "Location";
        String KEY_MOM_ACTION_ITEM = "ActionItem";
        String KEY_MOM_DISCRIPETION = "Description";
        String KEY_MOM_SERVER_ID = "MOMIdServer";
        String KEY_MOM_IS_DELETED = "MOMIsDeleted";
        String KEY_MOM_IS_UPDATED = "MOMIsUpdated";

    }

    public interface MOMAttendeesMasterColumns {
        String KEY_MOM_ID = "MOMId";
        String KEY_MOM_ATTENDEES_NAME = "AttendeeName";
    }

    public interface StoreModuleMasterColumns {
        String KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID = "ActivityID";
        String KEY_STORE_ID = "StoreID";
        String KEY_MODULE_PARENT_MODULE_ID = "ParentModuleID";
        String KEY_MODULE_CODE = "ModuleCode";
        String KEY_MODULE_ID = "ModuleID";
        String KEY_IS_MENDATORY = "IsMandatory";


    }

    private interface BasicUserColumns {
        String KEY_USER_ID = "UserID";
        String KEY_USER_ROLE_ID = "UserRoleID";
        String EMAIL_ID = "EmailID";
    }

    private interface BasicModuleColumns {
        String KEY_MODULE_CODE = "ModuleCode";
        String KEY_MODULE_ID = "ModuleID";
        String KEY_IS_MENDATORY = "IsMandatory";
    }

    private interface BasicQuestionColumns {
        String KEY_SURVEY_QUESTION_ID = "SurveyQuestionID";
        String KEY_SURVEY_QUESTION_TYPE_ID = "QuestionTypeID";
        String KEY_SURVEY_QUESTION_SEQUENCE = "Sequence";
        String KEY_SURVEY_QUESTION_PRODUCT_TYPE_ID = "ProductTypeID";
        String KEY_SURVEY_QUESTION_PRODUCT_GROUP_ID = "ProductGroupID";
    }

    private interface BasicResponseColumns {
        String KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID = "ActivityID";
        String KEY_ACTIVITY_DATA_MASTER_CREATED_DATE = "CreatedDate";
        String KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE = "ModifiedDate";

    }

    private interface BasicStoreColumns {
        String KEY_BEAT_STORE_ID = "StoreID";
    }

    private interface BasicCommonColumns {
        String _ID = "_id";

    }

    public static interface UserProfileTableColumns extends BasicCommonColumns,
            BasicUserColumns {

        String KEY_USER_PROFILE_EMPLOYEE_CODE = "EmplCode";
        String KEY_USER_PROFILE_USER_CODE = "UserCode";
        String KEY_USER_PROFILE_USER_ROLE_ID_ACTUAL = "UserRoleIDActual";
        String KEY_USER_PROFILE_ALTERNATE_EMAIL_ID = "AlternateEmailID";
        String KEY_USER_PROFILE_FIRST_NAME = "FirstName";
        String KEY_USER_PROFILE_LAST_NAME = "LastName";
        String KEY_USER_PROFILE_MOBILE_CALLING = "Mobile_Calling";
        String KEY_USER_PROFILE_MOBILE_SD = "Mobile_SD";
        String KEY_USER_PROFILE_PIN_CODE = "Pincode";
        String KEY_USER_PROFILE_IS_OFFLINE_PROFILE = "IsOfflineProfile";
        String KEY_USER_PROFILE_ADDRESS = "Address";
        String KEY_USER_PROFILE_IS_ROAMING_PRIFLE = "IsRoamingProfile";
        String KEY_USER_PROFILE_PICTURE_FILE_NAME = "ProfilePictureFileName";

    }

    public static interface SurveyQuestionColumns extends BasicQuestionColumns,
            BasicCommonColumns, BasicModuleColumns {

        String KEY_SURVEY_QUESTION_TEXT_LENGTH = "TextLength";
        String KEY_SURVEY_QUESTION = "Question";
        String KEY_SURVEY_QUESTION_IMAGE = "QuestionImage";
        String KEY_SURVEY_QUESTION_DEPENDENT_OPTION_ID = "DependentOptionID";
        String KEY_SURVEY_QUESTION_REPEATET_MAX_TIMES = "RepeatMaxTimes";
        String KEY_SURVEY_QUESTION_REPEATER_TEXT = "RepeaterText";
        String KEY_SURVEY_QUESTION_REPEATER_TYPE_ID = "RepeaterTypeID";
        // String KEY_DATE_TIME_STAMP = "DateTimeStamp";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";
        String KEY_IS_DELETED = "IsDeleted";


    }

    public static interface SurveyQuestionOptionsColumns extends
            BasicQuestionColumns, BasicCommonColumns {

        String KEY_SURVEY_QUESTION_OPTION_VALUE = "OptionValue";
        String KEY_SURVEY_QUESTION_OPTION_ID = "SurveyOptionID";
        String KEY_SURVEY_QUESTION_OPTION_IS_AFFIRMATIVE = "IsAffirmative";
        // String KEY_DATE_TIME_STAMP = "DateTimeStamp";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";
        String KEY_IS_DELETED = "IsDeleted";

    }

    public static interface QuestionAnswerResponseColumns extends
            BasicCommonColumns, BasicResponseColumns, BasicQuestionColumns {

        String KEY_SURVEY_QUESTION_USER_RESPONSE = "UserResponse";
        String KEY_SURVEY_QUESTION_SUB_ID = "SurveyQuestionSubID";

        public static interface FeedbackStatusMasterColumns extends
                BasicCommonColumns {
            String KEY_FEEDBACK_STATUS_MASTER_ID = "FeedbackStatusID";
            String KEY_FEEDBACK_STATUS_MASTER_NAME = "FeedbackStatusName";
            // String KEY_DATE_TIME_STAMP = "DateTimeStamp";
            String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
            String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";
            String KEY_IS_DELETED = "IsDeleted";

        }

        public static interface TeamMasterColumns extends BasicCommonColumns {
            String KEY_TEAM_MASTER_ID = "TeamId";
            String KEY_TEAM_MASTER_NAME = "TeamName";
            // String KEY_DATE_TIME_STAMP = "DateTimeStamp";
            String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
            String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";
            String KEY_IS_DELETED = "IsDeleted";
        }

        public static interface FeedbackCategoryMasterColumns extends
                BasicCommonColumns {

            String KEY_FEEDBACK_CATEGORY_MASTER_ID = "FeedbackCatID";
            String KEY_FEEDBACK_CATEGORY_MASTER_TEAM_ID = "TeamID";
            String KEY_FEEDBACK_CATEGORY_MASTER_NAME = "FeedbackCategoryName";
            // String KEY_DATE_TIME_STAMP = "DateTimeStamp";
            String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
            String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";
            String KEY_IS_DELETED = "IsDeleted";
        }

        public static interface FeedbackTypeMasterColumns extends
                BasicCommonColumns {
            String KEY_FEEDBACK_TYPE_MASTER_ID = "FeedbackTypeID";
            String KEY_FEEDBACK_CATEGORY_MASTER_ID = "FeedbackCatID";
            String KEY_FEEDBACK_TYPE_MASTER_NAME = "FeedbackTypeName";
            String KEY_FEEDBACK_TYPE_MASTER_SAMPLE_IMAGE_NAME = "SampleImageName";
            // String KEY_DATE_TIME_STAMP = "DateTimeStamp";
            String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
            String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";
            String KEY_IS_DELETED = "IsDeleted";
        }

        public static interface UserFeedbackMasterColumns extends
                BasicCommonColumns {
            String KEY_USER_FEEDBACK_MASTER_USER_FEEDBACK_ID = "UserFeedbackID";
            String KEY_FEEDBACK_CATEGORY_MASTER_TEAM_ID = "TeamID";
            String KEY_FEEDBACK_CATEGORY_MASTER_ID = "FeedbackCatID";
            String KEY_USER_FEEDBACK_MASTER_TYPE_ID = "FeedbackTypeID";
            String KEY_USER_FEEDBACK_MASTER_REMARK = "Remark";
            String KEY_USER_FEEDBACK_MASTER_IMAGE_PATH = "ImagePath";
            String KEY_USER_FEEDBACK_MASTER_STORE_ID = "StoreID";

        }

    }

    public static interface StoreBasicColulmns extends BasicCommonColumns,
            BasicStoreColumns {

        String KEY_BEAT_STORE_NAME = "StoreName";
        String KEY_BEAT_STORE_CODE = "StoreCode";
        String KEY_BEAT_CITY = "City";
        String KEY_BEAT_CHINNEL_TYPE = "ChannelType";
        String KEY_BEAT_CONTACT_PERSION = "ContactPerson";
        String KEY_BEAT_COVERAGE_ID = "CoverageID";
        String KEY_BEAT_EMAIL_ID = "EmailID";
        String KEY_STORE_ADDRESS = "StoreAddress";
        String KEY_BEAT_IS_COVERAGE = "IsCoverage";
        String KEY_BEAT_IS_FREEZE = "IsFreeze";
        String KEY_BEAT_FREEZE_LATTITUDE = "FreezeLattitude";
        String KEY_BEAT_FREEZE_LONGITUDE = "FreezeLongitude";
        String KEY_BEAT_MOBILE_NUMBER = "MobileNo";
        String KEY_BEAT_PICTURE_FILE_NAME = "PictureFileName";
        String KEY_BEAT_STORE_SIZE = "StoreSize";
        String KEY_BEAT_IS_DISPLAY_COUNTER_SHARE = "IsDisplayCounterShare";
        String KEY_BEAT_IS_PLANOGRAM = "IsPlanogram";
        String KEY_BEAT_STORE_CLASS = "StoreClass";
        String KEY_BEAT_TYPE = "Type";
        // String KEY_DATE_TIME_STAMP = "DateTimeStamp";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";
        String KEY_IS_DELETED = "IsDeleted";

		/* @ankit 
         * New Field Added in Other Store
		 */

        String KEY_LAND_LINE_NUMBER = "LandLineNumber";
        String KEY_SPC_NAME = "SPCName";
        String KEY_SPC_CATEGORY = "SPCCategory";
        String KEY_STORE_MANAGER_NAME = "StoreManagerName";
        String KEY_SM_MOBILE = "SMMobile";
        String KEY_SM_EMAIL_ID = "SMEmailID";
        String KEY_ALTERNAMTE_EMAIL_ID = "AlternateEmailId";
        String KEY_TIN_NUMBER = "TINNumber";
        String KEY_PIN_CODE = "PinCode";


    }

    public static interface StorePerformaceColumns extends BasicCommonColumns,
            BasicStoreColumns {

        String KEY_BEAT_ACH = "ACH";
        String KEY_BEAT_TARGET = "Target";
        String KEY_BEAT_ACMTD_PURCHASE = "ACMTDPurchase";
        String KEY_BEAT_ACMTD_SALE = "ACMTDSale";
        String KEY_BEAT_AVMTD_PURCHASE = "AVMTDPurchase";
        String KEY_BEAT_AVMTD_SALE = "AVMTDSale";
        String KEY_BEAT_AVLMTD_SALE = "AVLMTDSale";
        String KEY_BEAT_AVLMTD_PURCHASE = "AVLMTDPurchase";
        String KEY_BEAT_HALMTD_SALE = "HALMTDSale";
        String KEY_BEAT_HALMTD_PURCHASE = "HALMTDPurchase";
        String KEY_BEAT_ACLMTD_SALE = "ACLMTDSale";
        String KEY_BEAT_ACLMTD_PURCHASE = "ACLMTDPurchase";
        String KEY_BEAT_HAMTD_PURCHASE = "HAMTDPurchase";
        String KEY_BEAT_HAMTD_SALE = "HAMTDSale";
        String KEY_BEAT_LAST_VISITED_DATE = "LastVisitedDate";


        String KEY_OUTLET_PERFORMANCE_ID = "OutletPerformanceID";
        String KEY_REGION = "Region";
        String KEY_BRANCH = "Branch";
        String KEY_CHANNEL_TYPE = "ChannelType";

        String KEY_PRODUCT_TYPE = "ProductType";
        String KEY_PRODUCT_GROUP = "ProductGroup";
        String KEY_TRANSACTION_TYPE = "TransactionType";
        String KEY_SALES_TYPE = "SalesType";
        String KEY_VOLUME = "Volume";
        String KEY_VALUE = "Value_K_";

        String KEY_MTD_UNIT = "MTDUnit";
        String KEY_MTD_VALUE = "MTDvalue";
        String KEY_LMTD_UNIT = "LMTDUnit";
        String KEY_LMTD_VALUE = "LMTDvalue";


    }

    public static interface CompetetionProductGroupColumns {

        String KEY_COMPETETION_PRODUCT_GROUP_ID = "CompProductGroupID";
        String KEY_COMPETETION_PRODUCT_GROUP_CODE = "CompetetionProductGroupCode";
        String KEY_COMPETETION_PRODUCT_GROUP_NAME = "CompetetionProductGroupName";
        // String KEY_DATE_TIME_STAMP = "DateTimeStamp";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";
        String KEY_IS_DELETED = "IsDeleted";

    }

    public static interface CompetitorColumns {
        String KEY_COMETITOR_CODE = "CompetitorCode";
        String KEY_COMETITOR_ID = "CompetitorID";
        String KEY_COMETITOR_NAME = "CompetitorName";
        String KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID = "ProductTypeID";
        // String KEY_DATE_TIME_STAMP = "DateTimeStamp";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";
        String KEY_IS_DELETED = "IsDeleted";
    }

    public static interface CounterShareDisplayShareResponseMasterColumns {

        String KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID = "ActivityID";
        String KEY_USER_ROLE_ID = "UserRoleID";
        String KEY_SURVEY_QUESTION_ID = "SurveyQuestionID";
        String KEY_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE_COMPETITION_TYPE = "CompetitionType";
        String KEY_SURVEY_QUESTION_PRODUCT_GROUP_ID = "ProductGroupID";
        String KEY_SURVEY_QUESTION_USER_RESPONSE = "UserResponse";
        String KEY_BEAT_STORE_ID = "StoreID";
        String KEY_USER_ID = "UserID";
        String KEY_COMETITOR_ID = "CompetitorID";
        String KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID = "ProductTypeID";
        String KEY_ACTIVITY_DATA_MASTER_CREATED_DATE = "CreatedDate";
        String KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE = "ModifiedDate";
        String KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID = "CoverageID";

    }

    public static interface ActivityDataMasterColumns {

        String KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID = "ActivityID";
        String KEY_USER_ID = "UserID";
        String KEY_BEAT_STORE_ID = "StoreID";
        String KEY_MODULE_CODE = "ModuleCode";
        String KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID = "CoverageID";
        String KEY_ACTIVITY_DATA_MASTER_CREATED_DATE = "CreatedDate";
        String KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE = "ModifiedDate";
        String KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS = "SyncStatus";
        String KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID = "SurveyResponseID";

    }


    public static interface DownloadDataModuleMappingTableColums {
        String KEY_MODULE_CODE = "ModuleCode";
        String KEY_DOWNLOAD_DATA_MASTER_DATA_CODE = "DataCode";
        String KEY_DOWNLOAD_DATA_MASTER_MAPPING_ID = "MappingID";
    }


    public static interface UserModuleTableColumns {

        String KEY_MODULE_CODE = "ModuleCode";
        String KEY_MODULE_ID = "ModuleID";
        String KEY_IS_MENDATORY = "IsMandatory";

        String KEY_NAME = "Name";
        String KEY_MODULE_PARENT_MODULE_ID = "ParentModuleID";
        String KEY_MODULE_SEQUENCE = "Sequence";
        String KEY_MODULE_ICON_NAME = "Icon";
        String KEY_MODULE_IS_QUESTION_MODULE = "IsQuestionModule";
        String KEY_MODULE_TYPE = "ModuleType";
        String KEY_MODULE_IS_STORE_WISE = "IsStoreWise";
        String KEY_UPDATE_DATE = "SYNC_DATE_TIME";
        // String KEY_DATE_TIME_STAMP = "DateTimeStamp";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";
        String KEY_IS_DELETED = "IsDeleted";

    }

    public static interface CollectionResponseTableColumns {

        String KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID = "ActivityID";
        String KEY_USER_ROLE_ID = "UserRoleID";
        String KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID = "CoverageID";
        String KEY_COLLECTION_TRANSACTION_ID = "TransactionID";
        String KEY_COLLECTION_COMMENTS = "Comments";
        String KEY_COLLECTION_AMOUNT = "Amount";
        String KEY_COLLECTION_PAYMENT_DATE = "PaymentDate";
        String KEY_PAYMENT_MODE_ID = "PaymentModeID";
        String KEY_BEAT_STORE_ID = "StoreID";
        String KEY_USER_ID = "UserID";
        String KEY_ACTIVITY_DATA_MASTER_CREATED_DATE = "CreatedDate";
        String KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE = "ModifiedDate";

    }

    public static interface ProductMasterTableColumns {

        String KEY_PRODUCT_MASTER_PRODUCT_ID = "ProductID";
        String KEY_PRODUCT_MASTER_BASIC_MODEL_CODE = "BasicModelCode";
        String KEY_PRODUCT_MASTER_BASIC_MODEL_NAME = "BasicModelName";
        String KEY_PRODUCT_MASTER_CATEGORY_CODE = "CategoryCode";
        String KEY_PRODUCT_MASTER_CATEGORY_NAME = "CategoryName";
        String KEY_PRODUCT_MASTER_DEALER_PRICE = "DealerPrice";
        String KEY_PRODUCT_MASTER_MODEL_TYPE_ID = "ModelTypeID";
        String KEY_PRODUCT_MASTER_PRODUCT_CAEGORY_ID = "ProductCategoryID";
        String KEY_PRODUCT_MASTER_PRODUCT_GROUP_CODE = "ProductGroupCode";
        String KEY_PRODUCT_MASTER_PRODUCT_GROUP_ID = "ProductGroupID";
        String KEY_PRODUCT_MASTER_PRODUCT_GROUP_NAME = "ProductGroupName";
        String KEY_PRODUCT_MASTER_PRODUCT_TYPE_CODE = "ProductTypeCode";
        String KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID = "ProductTypeID";
        String KEY_PRODUCT_MASTER_PRODUCT_TYPE_NAME = "ProductTypeName";
        String KEY_PRODUCT_MASTER_SKU_CODE = "SKUCode";
        String KEY_PRODUCT_MASTER_SKU_NAME = "SKUName";
        // String KEY_DATE_TIME_STAMP = "DateTimeStamp";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";
        String KEY_IS_DELETED = "IsDeleted";


    }

    public static interface PaymentModeMasterTableColumns {

        String KEY_PAYMENT_MODE_NAME = "PaymentModeName";
        String KEY_PAYMENT_MODE_ID = "PaymentModeID";
        // String KEY_DATE_TIME_STAMP = "DateTimeStamp";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";
        String KEY_IS_DELETED = "IsDeleted";

    }

    public interface OrderResponseMasterTableColumns {

        String KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID = "ActivityID";
        String KEY_USER_ROLE_ID = "UserRoleID";
        String KEY_ORDER_MASTER_QUANTITY = "Quantity";
        String KEY_ORDER_MASTER_ORDERNO = "OrderNo";
        String KEY_PRODUCT_MASTER_PRODUCT_GROUP_ID = "ProductGroupID";
        String KEY_BEAT_STORE_ID = "StoreID";
        String KEY_PRODUCT_MASTER_PRODUCT_ID = "ProductID";
        String KEY_USER_ID = "UserID";
        String KEY_ORDER_MASTER_ORDER_BOOKING_TYPE = "OrderBookingType";
        String KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID = "ProductTypeID";
        String KEY_ORDER_MASTER_ORDER_BOOKING_ID = "OrderBookingID";
        String KEY_PRODUCT_MASTER_PRODUCT_CAEGORY_ID = "ProductCategoryID";
        String KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID = "CoverageID";
        String KEY_ORDER_MASTER_PRICE = "Price";
        String KEY_PRODUCT_MASTER_SKU_CODE = "SKUCode";
        String KEY_ACTIVITY_DATA_MASTER_CREATED_DATE = "CreatedDate";
        String KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE = "ModifiedDate";

    }

    public interface CompetitorProductGroupMappingTableColumns {
        String KEY_COMPETITOR_PRODUCT_GROUP_MAPPING_MAPPING_ID = "MappingID";
        String KEY_COMETITOR_ID = "CompetitorID";
        String KEY_COMPETETION_PRODUCT_GROUP_ID = "CompProductGroupID";
        // String KEY_DATE_TIME_STAMP = "DateTimeStamp";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";
        String KEY_IS_DELETED = "IsDeleted";
    }

    public interface PlanogramProductMasterTableColumns {
        String KEY_PLANOGRAM_PRODUCT_MASTER_ID = "PlanogramProductMasterID";
        String KEY_PLANOGRAM_PRODUCT_MASTER_CLASS = "Class";
        String KEY_PLANOGRAM_PRODUCT_MASTER_CHANNEL_TYPE = "ChannelType";
        String KEY_PLANOGRAM_PRODUCT_MASTER_COMP_PRODUCT_GROUP = "CompProductGroup";
        String KEY_PLANOGRAM_PRODUCT_MASTER_PRODUCT_CODE = "ProductCode";
        // String KEY_DATE_TIME_STAMP = "DateTimeStamp";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";
        String KEY_IS_DELETED = "IsDeleted";
    }

    public interface PlanogramClassMasterTableColumns {
        String KEY_PLANOGRAM_CLASS_MASTER_CLASS_ID = "ClassID";
        String KEY_PLANOGRAM_CLASS_MASTER_START_RANGE = "StartRange";
        String KEY_PLANOGRAM_CLASS_MASTER_END_RANGE = "EndRange";
        String KEY_PLANOGRAM_PRODUCT_MASTER_CLASS = "Class";
        String KEY_PLANOGRAM_CLASS_MASTER_COMP_PRODUCT_GROUP_ID = "CompProdGroupID";
        String KEY_PLANOGRAM_PRODUCT_MASTER_CHANNEL_TYPE = "ChannelType";
        // String KEY_DATE_TIME_STAMP = "DateTimeStamp";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";
        String KEY_IS_DELETED = "IsDeleted";

    }

    public interface PlanogramResponseMasterTableColumns {
        String KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID = "ActivityID";
        String KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID = "PlanResponseId";
        String KEY_COMPETETION_PRODUCT_GROUP_ID = "CompProductGroupID";
        String KEY_PLANOGRAM_PRODUCT_MASTER_CLASS = "Class";
        String KEY_PLANOGRAM_CLASS_MASTER_CLASS_ID = "ClassID";
        String KEY_PLANOGRAM_RESPONSE_MASTER_ADHERENCE = "Adherence";
    }

    public interface PlanogramProductResponseMasterColumns {
        String KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID = "ActivityID";
        String KEY_PLANOGRAM_PRODUCT_RESPONSE_MASTER_PLAN_PRODUCT_ID = "PlanProductId";
        String KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID = "PlanResponseId";
        String KEY_PLANOGRAM_PRODUCT_MASTER_PRODUCT_CODE = "ProductCode";
        String KEY_PLANOGRAM_PRODUCT_RESPONSE_MASTER_IS_AVAILABLE = "IsAvailable";
    }

    public interface PlanogramCompitiorResponseMasterColumns {
        String KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID = "ActivityID";
        String KEY_PLANOGRAM_COMPITITORS_RESPONSE_MASTER_PLAN_COMPITITOR_ID = "PlanCompititorId";
        String KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID = "PlanResponseId";
        String KEY_COMETITOR_ID = "CompetitorID";
        String KEY_PLANOGRAM_COMPITITORS_RESPONSE_MASTER_PLAN_VALUE = "Value";
    }

    public interface EOLSchemeHeaderMasterMasterColumns {

        String KEY_EOL_SCHEME_ID = "SchemeID";
        String KEY_EOL_SCHEME_NUMBER = "SchemeNumber";
        String KEY_EOL_SCHEME_FROM = "SchemeFrom";
        String KEY_EOL_SCHEME_TO = "SchemeTo";
        String KEY_EOL_ORDER_FROM = "OrderFrom";
        String KEY_EOL_ORDER_TO = "OrderTo";
        String KEY_EOL_PUMI_NUMBER = "PUMINumber";
        String KEY_EOL_PUMI_DATE = "PUMIDate";
        String KEY_EOL_PRODUCT_TYPE = "ProductType";
        String KEY_EOL_PRODUCT_GROUP = "ProductGroup";
        String KEY_PRODUCT_CATEGORY = "ProductCategory";
        String KEY_ACTIVITY_DATA_MASTER_CREATED_DATE = "CreatedDate";
        String KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE = "ModifiedDate";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";


    }


    public interface EOLSchemeDetailsMasterColoums {

        String KEY_EOL_SCHEME_ID = "SchemeID";
        String KEY_EOL_DETAILS_BASIC_MODEL_CODE = "BasicModelCode";
        String KEY_EOL_DETAILS_ORDER_QUATITY = "OrderQuantity";
        String KEY_EOL_DETAILS_ACTUAL_SUPPORT = "ActualSupport";


    }

    public interface StoreAssessmentMasterColumns {
        String KEY_BEAT_STORE_ID = "StoreID";
        String KEY_STORE_ASSESSMENT_MASTER_STORE_ASSESSMENT_ID = "StoreAssessmentID";
        String KEY_STORE_ASSESSMENT_MASTER_ASSESSMENT_START_TIME = "AssessmentStartTime";
        String KEY_STORE_ASSESSMENT_MASTER_ASSESSMENT_END_TIME = "AssessmentEndTime";
        String KEY_STORE_ASSESSMENT_MASTER_UPLOADED_ON = "UploadedOn";
    }

    public interface StoreGeoTagResponseMasterColumns {
        String KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID = "ActivityID";
        String KEY_BEAT_STORE_ID = "StoreID";
        String KEY_STORE_GEO_TAG_LATITUDE = "Latitude";
        String KEY_STORE_GEO_TAG_LONGITUDE = "Longitude";
        String KEY_STORE_GEO_TAG_GEO_IMAGE = "GeoImage";
        String KEY_STORE_GEO_TAG_GEO_USER_OPTION = "UserOption";
        String KEY_STORE_GEO_TAG_MENDATORY = "IsGeoTagMandatory";
        String KEY_STORE_GEO_TAG_PHOTO_MENDATORY = "IsGeoPhotoMandatory";
        String KEY_STORE_GEO_TAG_IS_GEO_FREEZED = "IsGeoTagFreezed";
        String KEY_STORE_GEO_TAG_IS_GEO_FREEZED_LATITUDE = "FreezedLatitude";
        String KEY_STORE_GEO_TAG_IS_GEO_FREEZED_LONGITUDE = "FreezedLongitude";

    }

    public interface DownloadDataMasterColumns {
        String KEY_DOWNLOAD_DATA_MASTER_DATA_NAME = "DataName";
        String KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME = "DataServiceName";
        // String KEY_DOWNLOAD_DATA_MASTER_CREATED_DATE = "CreatedDate";
        // String KEY_DOWNLOAD_DATA_MASTER_MODIFY_DATE = "MOdifyDate";
        String KEY_DOWNLOAD_DATA_MASTER_DATA_CODE = "DataCode";
        String KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS = "DownloadStatus";
        String KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED = "isDownloadNeeded";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";


    }

    public interface RaceBrandMasterColumns {
        String KEY_BRAND_ID = "BrandID";
        String KEY_BRAND_CODE = "BrandCode";
        String KEY_BRAND_NAME = "BrandName";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";

        String KEY_IS_DELETED = "IsDeleted";
    }

    public interface RaceBrandProductCategoryMasterColumns {
        String KEY_COMP_PRODUCT_GROUP_ID = "CompProductGroupID";
        String KEY_PRODUCT_GROUP_NAME = "ProductGroupName";
        String KEY_PRODUCT_GROUP_CODE = "ProductGroupCode";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";

        String KEY_IS_DELETED = "IsDeleted";
    }

    public interface RaceBrandCategoryMappingMasterColumns {
        String KEY_BRAND_CATEGORY_MAPING_ID = "BrandCategoryMappingID";
        String KEY_BRAND_ID = "BrandID";
        String KEY_COMP_PRODUCT_GROUP_ID = "CompProductGroupID";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";

        String KEY_IS_DELETED = "IsDeleted";
    }

    public interface RacePosmMasterColumns {
        String KEY_POSM_ID = "POSMID";
        String KEY_POSM_CODE = "POSMCode";
        String KEY_POSM_NAME = "POSMName";
        String KEY_POSM_CATEGORY = "POSMCategory";
        String KEY_POSM_GROUP = "POSMGroup";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";

        String KEY_IS_DELETED = "IsDeleted";
    }

    public interface RaceFixtureMasterColumns {
        String KEY_FIXTURE_ID = "FixtureID";
        String KEY_FIXTURE_CATEGORY_NAME = "Category";
        String KEY_FIXTURE_SUB_CATEGORY = "SubCategory";
        String KEY_FIXTURE_PRODUCT_GROUPS = "CategoryGroups";
        String KEY_FIXTURE_IS_COMPETITOR_AVAILBALE = "IsCompetitorAvailable";
        String KEY_FIXTURE_IS_WALL_AVAILABLE = "IsColumnAvailable";
        String KEY_FIXTURE_IS_ROW_AVAILABLE = "IsRowAvailable";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";

        String KEY_IS_DELETED = "IsDeleted";
    }

    public interface RaceBrandProductMasterColumns {
        String KEY_BRAND_ID = "BrandID";
        String KEY_NAME = "Name";
        String KEY_PRODUCT_CATEGORY = "ProductCategory";
        String KEY_PRODUCT_GROUP = "ProductGroup";
        String KEY_PRODUCT_ID = "ProductID";
        String KEY_PRODUCT_TYPE = "ProductType";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";

        String KEY_IS_DELETED = "IsDeleted";
    }

    public interface RacePOSMProductMappingMasterColumns {
        String KEY_POSM_PRODUCT_MAPPING_ID = "POSMProductMappingID";
        String KEY_POSM_ID = "POSMID";
        String KEY_PRODUCT_ID = "ProductID";
        String KEY_CREATED_DATE_TIME_STAMP = "CreatedDateTimeStamp";
        String KEY_MODIFIED_DATE_TIME_STAMP = "ModifyDateTimeStamp";

        String KEY_IS_DELETED = "IsDeleted";
    }

    public interface RaceProductAuditResponseMasterColumns {
        String KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID = "ActivityID";
        String KEY_FIXTURE_ID = "FixtureID";
        String KEY_PRODUCT_ID = "ProductID";
        String KEY_WALL_NUMBER = "WallNumber";
        String KEY_ROW_NUMBER = "RowNumber";
        String KEY_TOPPER = "Topper";
        String KEY_SWITCHED_ON = "SwitchedOn";
        String KEY_BRAND_ID = "BrandID";
        String KEY_PRICE_TAG = "PriceTag";
        String KEY_STOCK_AUDIT_ID = "StockAuditID";
    }

    public interface RacePOSMDataResponseMasterColumns {
        String KEY_POSM_MAPPING_ID = "PosmMappingId";
        String KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID = "ActivityID";
        String KEY_STOCK_AUDIT_ID = "StockAuditID";
        String KEY_POSM_ID = "POSMID";
        String KEY_POSM_TYPE = "POSMType";

    }

    public interface EOLOrderBookingResponseMasterColoums {

        String KEY_BEAT_STORE_ID = "StoreID";
        String KEY_EOL_SCHEME_ID = "SchemeID";
        String KEY_EOL_DETAILS_BASIC_MODEL_CODE = "BasicModelCode";
        String KEY_EOL_DETAILS_ORDER_QUATITY = "OrderQuantity";
        String KEY_EOL_DETAILS_ACTUAL_SUPPORT = "ActualSupport";
        String KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID = "ActivityID";
        String KEY_EOL_ORDER_BOOKING_RESPONSE_MASTER_ORDER_BOOKING_ID = "OrderBookingID";

    }

    // Expense Module Tables

    public  interface ExpenseTypeMasterTableColumns {

        String KEY_EXPENSE_TYPE_MASTER_ID = "EMSExpenseTypeMasterId";
        String KEY_EXPENSE_TYPE_MASTER_NAME = "Name";
        String KEY_EXPENSE_TYPE_MASTER_CODE = "Code";
        String KEY_EXPENSE_TYPE_MASTER_DESCRIPTION = "Description";
        String KEY_EXPENSE_TYPE_MASTER_COMPANY_ID = "CompanyId";
        String KEY_EXPENSE_TYPE_MASTER_SEQUENCE = "Sequence";
        String KEY_EXPENSE_TYPE_MASTER_IS_ACTIVE = "IsActive";
        String KEY_EXPENSE_TYPE_MASTER_IS_DELETED = "IsDeleted";
    }


    public  interface EMSExpenseDetailTableColumns {

        String KEY_EMS_EXPENSE_DETAIL_ID = "EMSExpenseDetailID";
        String KEY_EMS_EXPENSE_TYPE_MASTER_ID = "EMSExpenseTypeMasterID";
        String KEY_IS_ACTIVE = "IsActive";
        String KEY_IS_DELETED = "IsDeleted";
        String KEY_BILLABLE = "Billable";
        String KEY_BILLABLE_TO = "BillableTo";
        String KEY_EMS_EXPENSE_COMMENT = "Comment";
        String KEY_STATUS = "Status";
        String KEY_CREATED_DATE = "CreatedDate";
        String KEY_SUBMITTED_DATE = "SubmittedDate";
        String KEY_MODIFIED_DATE = "ModifiedDate";
        String KEY_MODIFIED_BY = "ModifiedBy";
        String KEY_EMSEXPENSE_DETAIL_ID_SERVER = "EMSExpenseDetailIDServer";
        String KEY_IS_EXPENSE_UPDATED = "IsExpenseUpdated";
        String KEY_EMS_EXPENSE_DETAIL_TAB_ID = "EMSExpenseDetailITabID";

        String KEY_EMS_EXPENSE_PENDING_WITH = "PendingWith";
        String KEY_EMS_EXPENSE_ASSIGNED_TO_USER = "AssignedToUser";
        String KEY_EMS_EXPENSE_ASSIGNED_STATUS = "AssignedStatus";

        String KEY_EMS_EXPENSE_CREATED_BY = "CreatedBy";


    }

    public  interface EMSBillDetailTableColumns {

        String KEY_EMS_BILL_DETAIL_ID = "EMSBillDetailID";
        String KEY_EMS_BILL_DETAIL_ID_SERVER = "EMSBillDetailIDServer";
        String KEY_EMS_EXPENSE_DETAIL_ID = "EMSExpenseDetailID";
        String KEY_BILL_DATE = "BillDate";
        String KEY_BILL_NO = "BillNo";
        String KEY_DESCRIPTION = "Description";
        String KEY_AMOUNT = "Amount";
        String KEY_IS_ACTIVE = "IsActive";
        String KEY_IS_DELETED = "IsDeleted";
        String KEY_CREATED_DATE = "CreatedDate";
        String KEY_CREATED_BY = "CreatedBy";
        String KEY_MODIFIED_DATE = "ModifiedDate";
        String KEY_MODIFIED_BY = "ModifiedBy";
    }


    public  interface EMSBillDocumentDetailTableColumns {

        String KEY_EMS_BILL_DOCUMENT_DETAIL_ID = "EMSBillDocumentDetailID";
        String KEY_EMS_BILL_DOCUMENT_DETAIL_ID_SERVER = "EMSBillDocumentDetailIDServer";
        String KEY_EMS_BILL_DETAIL_ID = "EMSBillDetailID";
        String KEY_DOCUMENT_NAME = "DocumentName";
        String KEY_DOCUMENT_PATH = "DocumentPath";
        String KEY_IS_ACTIVE = "IsActive";
        String KEY_IS_DELETED = "IsDeleted";
        String KEY_CREATED_DATE = "CreatedDate";
        String KEY_CREATED_BY = "CreatedBy";
        String KEY_MODIFIED_DATE = "ModifiedDate";
        String KEY_MODIFIED_BY = "ModifiedBy";

    }
}
