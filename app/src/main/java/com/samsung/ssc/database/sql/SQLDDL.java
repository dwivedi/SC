package com.samsung.ssc.database.sql;

import java.util.ArrayList;
import java.util.List;

import com.samsung.ssc.database.DatabaseConstants;
import com.samsung.ssc.database.DatabaseConstants.ActivityDataMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.CompetetionProductGroupColumns;
import com.samsung.ssc.database.DatabaseConstants.CompetitorColumns;
import com.samsung.ssc.database.DatabaseConstants.CounterShareDisplayShareResponseMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.EOLOrderBookingResponseMasterColoums;
import com.samsung.ssc.database.DatabaseConstants.EOLSchemeDetailsMasterColoums;
import com.samsung.ssc.database.DatabaseConstants.OrderResponseMasterTableColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.FeedbackCategoryMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.FeedbackStatusMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.FeedbackTypeMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.TeamMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.UserFeedbackMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.StoreBasicColulmns;
import com.samsung.ssc.database.DatabaseConstants.StorePerformaceColumns;
import com.samsung.ssc.database.DatabaseConstants.UserProfileTableColumns;

public final class SQLDDL {

    private SQLDDL() {
    }

    public static List<String> getSQLTableCreateStatements() {
        List<String> statements = new ArrayList<String>();

        statements.add(getCreateSurveyQuestionMasterTableDDL());
        statements.add(getCreateSurveyQuestionOptionsMasterTableDDL());
        statements.add(getCreateQuestionAnswerResponseMasterTableDDL());
        statements.add(getCreateUserProfileTableDDL());
        statements.add(getCreateStoreBasicMasterTableDDL());
        statements.add(getCreateStorePerformanceMasterTableDDL());
        statements.add(getCreateCompetetionProductGroupTableDDL());
        statements.add(getCreateCompetitorTableDDL());
        statements.add(getCounterShareDisplayShareTableDDL());
        statements.add(getCreateActivityDataMasterTableDDL());
        statements.add(getCreateUserModuleTableDDL());
        statements.add(getCreateCollectionResponseTableDDL());
        statements.add(getCreateProductMasterTableDDL());
        statements.add(getCreatePaymentModeMasterTableDDL());

        statements.add(getCreateFeedbackStatusMasterTableDDL());
        statements.add(getCreateTeamMasterTableDDL());
        statements.add(getCreateFeedbackCategortMasterTableDDL());
        statements.add(getCreateFeedbackTypeMasterTableDDL());
        statements.add(getCreateUserFeedbackMasterTableDDL());

        statements.add(getCreateOrderResponseTableDDL());
        statements.add(getCreateCompetitorProductGroupMappingTableDDL());
        statements.add(getCreatePlanogramProductMasterTableDDL());
        statements.add(getCreatePlanogramClassMasterTableDDL());
        statements.add(getCreatePlanogramResponseMasterTableDDL());
        statements.add(getCreatePlanogramProductResponseTableDDL());
        statements.add(getCreatePlanogramPranogramProductResponseTableDDL());

        statements.add(getCreateStoreAssesmentTableDDL());
        statements.add(getCreateStoreGeoTagMasterTableDDL());
        statements.add(getCreateDownloadDataMasterTableDDL());
        statements.add(getCreateDownloadDataMasterMappingTableDDL());

        statements.add(getCreateRaceBrandMasterTableDDL());
        statements.add(getCreateRaceBrandProductCategoryMasterTableDDL());
        statements.add(getCreateRaceBrandCategoryMappingMasterTableDDL());
        statements.add(getCreateRacePOSMMasterTableDDL());
        statements.add(getCreateRaceFixtureMasterTableDDL());
        statements.add(getCreateRaceBrandProductMasterTableDDL());
        statements.add(getCreateRacePOSMProductMappingMasterTableDDL());
        statements.add(getCreateRaceProductAuditResponseTableDDL());
        statements.add(getCreateRacePOSMDataResponseMasterTableDDL());

        statements.add(getCreateEOLSchemeHeaderTableDDL());
        statements.add(getCreateEOLSchemeDetailTableDDL());
        statements.add(getCreateEOLOrderBookingResponseMasterTableDDL());

        statements.add(getCreateStoreModuleMasterTableDDL());

        statements.add(getCreateMOMAttendeesMasterTableDDL());
        statements.add(getCreateMOMDetailsMasterTableDDL());

        statements.add(getCreateLMSLeaveMasterDDL());
        statements.add(getCreateLMSLeaveDateMasterDDL());
        statements.add(getCreateLMSLeaveTypeMasterDDL());
        statements.add(getCreateLMSStatusLogDDL());
        statements.add(getCreateLMSLeaveTypeConfigurationDDL());

        statements.add(getExpenseTypeMasterTableDDL());
        statements.add(getExpenseDetailsTableDDL());
        statements.add(getEMSBillDetailTableDDL());
        statements.add(getEMSBillDocumentDetailTableDDL());

        return statements;
    }


    private static String getCreateLMSLeaveMasterDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();

        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MASTER_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_SUBJECT,
                sqlHelper.getSQLTypeString())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_APPLIED_DATE,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CREATED_BY,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CREATED_BY_USER_NAME,
                sqlHelper.getSQLTypeString())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CREATED_DATE,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CURRENT_STATUS,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MODIFIED_BY,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MODIFIED_BY_USER_NAME,
                sqlHelper.getSQLTypeString())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MODIFIED_DATE,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());


        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_NUMBER_OF_LEAVE,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_PENDING_WITH,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_PENDING_WITH_USER_NAME,
                sqlHelper.getSQLTypeString())
                .createSQLColumn());


        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_REMARKS,
                sqlHelper.getSQLTypeString())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_TYPE_ID,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_LMS_LEAVE_MASTER,
                        columns);
    }

    private static String getCreateLMSLeaveDateMasterDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();


        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_DETAIL_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MASTER_ID,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_CURRENT_STATUS,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_IS_HALF_DAY,
                sqlHelper.getSQLTypeBoolean())
                .createSQLColumn());


        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_DATE,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());


        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_DATE_TEXT,
                sqlHelper.getSQLTypeString())
                .createSQLColumn());


        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_CREADTED_BY,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());


        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MODIFIED_BY,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MODIFIED_BY_USER_NAME,
                sqlHelper.getSQLTypeString())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MODIFIED_DATE,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_LMS_LEAVE_DATE_MASTER,
                        columns);
    }

    private static String getCreateLMSLeaveTypeMasterDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TYPE_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TYPE,
                sqlHelper.getSQLTypeString())
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TYPE_CODE,
                sqlHelper.getSQLTypeString())
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TAKEN,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_LMS_LEAVE_TYPE_MASTER,
                        columns);
    }

    private static String getCreateLMSStatusLogDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_LOG_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSStatusLogColumn.KEY_LEAVE_MASTER_ID,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CREATED_BY,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CREATED_BY_USER_NAME,
                sqlHelper.getSQLTypeString())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CREATED_DATE,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CURRENT_STATUS,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());


        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_REMARKS,
                sqlHelper.getSQLTypeString())
                .createSQLColumn());


        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_LMS_STATUS_LOG,
                        columns);
    }

    private static String getCreateLMSLeaveTypeConfigurationDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveTypeConfigurationColumn.KEY_LMS_LEAVE_TYPE_CONFIGURATION_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveTypeConfigurationColumn.KEY_LEAVE_TYPE_ID,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveTypeConfigurationColumn.KEY_CONFIGURATION_VALUE,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveTypeConfigurationColumn.KEY_CONFIGURATION_TEXT,
                sqlHelper.getSQLTypeString())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.LMSLeaveTypeConfigurationColumn.KEY_CONFIG_VALUE,
                sqlHelper.getSQLTypeInteger())
                .createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_LMS_LEAVE_TYPE_CONFIGURATION,
                        columns);
    }


    private static String getCreateEOLOrderBookingResponseMasterTableDDL() {
        // TODO Auto-generated method stub
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                EOLOrderBookingResponseMasterColoums.KEY_BEAT_STORE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                EOLOrderBookingResponseMasterColoums.KEY_EOL_SCHEME_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                EOLOrderBookingResponseMasterColoums.KEY_EOL_DETAILS_BASIC_MODEL_CODE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                EOLOrderBookingResponseMasterColoums.KEY_EOL_DETAILS_ORDER_QUATITY,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                EOLOrderBookingResponseMasterColoums.KEY_EOL_DETAILS_ACTUAL_SUPPORT,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                EOLOrderBookingResponseMasterColoums.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                EOLOrderBookingResponseMasterColoums.KEY_EOL_ORDER_BOOKING_RESPONSE_MASTER_ORDER_BOOKING_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .isNotNull(true).createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_EOL_ORDER_BOOKING_RESPONSE_MASTER,
                        columns);
    }

    public static List<String> getSQLTableDropStatements() {
        SQLHelper sqlHelper = new SQLiteHelper();

        List<String> statements = new ArrayList<String>();

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_SURVEY_QUESTION_MASTERS)); //
        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_SURVEY_QUESTION_OPTION_MASTERS));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_USER_PROFILE));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_STORE_BASIC_MASTER));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_STORE_PERFORMANCE_MASTER));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_COMPETETION_PRODUCT_GROUP));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_COMPETITOR_MASTER));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_USER_MODULE_MASTER)); //

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_FEEDBACK_STATUS_MASTER));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_FMS_TEAM_MASTER));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_FEEDBACK_CATEGORY_MASTER));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_FEEDBACK_TYPE_MASTER));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_PLANOGRAM_PRODUCT_MASTER));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_PLANOGRAM_CLASS_MASTER));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_DOWNLOAD_DATA_MASTER));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_PRODUCT_MASTER));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_PAYMENT_MODE_MASTER));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_COMPETITOR_PRODUCT_GROUP_MAPPING));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_USER_FEEDBACK_MASTER));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_RACE_POSM_MASTER));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_RACE_FIXTURE_MASTER));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_RACE_BRAND_CATEGORY_MAPPING_MASTER));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_RACE_BRAND_MASTER));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_EOL_SCHEME_DETAILS_MASTER));//

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_EOL_SCHEME_HEADER_MASTER));//
        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_RACE_BRAND_PRODUCT_MASTER));//
        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_RACE_BRAND_PRODUCT_CATEGORY_MASTER));//
        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_RACE_PRODUCT_POSM_MAPPING_MASTER));//

		/*
         * db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE_ASSESSMENT_MASTER);
		 * db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY_DATA_MASTER);
		 * db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE_ASSESSMENT_MASTER);
		 * db.execSQL("DROP TABLE IF EXISTS " +
		 * TABLE_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE_MASTER);
		 * db.execSQL("DROP TABLE IF EXISTS " +
		 * TABLE_QUESTION_ANSWER_RESPONSE_MASTER);
		 * db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_RESPONSE_MASTER);
		 * db.execSQL("DROP TABLE IF EXISTS " +
		 * TABLE_COLLECTION_RESPONSE_MASTER); db.execSQL("DROP TABLE IF EXISTS "
		 * + TABLE_PLANOGRAM_RESPONSE_MASTER);
		 * db.execSQL("DROP TABLE IF EXISTS " +
		 * TABLE_PLANOGRAM_PRODUCT_RESPONSE_MASTER);
		 * db.execSQL("DROP TABLE IF EXISTS " +
		 * TABLE_PLANOGRAM_COMPITIOR_RESPONSE_MASTER);
		 * db.execSQL("DROP TABLE IF EXISTS " +
		 * TABLE_EOL_ORDER_BOOKING_RESPONSE_MASTER);
		 * db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE_GEO_TAG_RESPONSE);
		 */

        return statements;
    }

    private static String getCreateQuestionAnswerResponseMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                QuestionAnswerResponseColumns._ID, sqlHelper
                .getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                QuestionAnswerResponseColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
                sqlHelper.getSQLTypeInteger()).isNotNull(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_ID, sqlHelper
                .getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_SUB_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_USER_RESPONSE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                QuestionAnswerResponseColumns.KEY_ACTIVITY_DATA_MASTER_CREATED_DATE,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                QuestionAnswerResponseColumns.KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_TYPE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_QUESTION_ANSWER_RESPONSE_MASTER,
                        columns);

    }

    private static String getCreateSurveyQuestionOptionsMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionOptionsColumns._ID, sqlHelper
                .getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_ID,
                sqlHelper.getSQLTypeInteger()).isNotNull(true).isUnique(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_VALUE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_SEQUENCE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_IS_AFFIRMATIVE,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionOptionsColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionOptionsColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionOptionsColumns.KEY_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_SURVEY_QUESTION_OPTION_MASTERS,
                        columns);

    }

    private static String getCreateSurveyQuestionMasterTableDDL() {

        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns._ID, sqlHelper
                .getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns.KEY_SURVEY_QUESTION_ID,
                sqlHelper.getSQLTypeInteger()).isNotNull(true).isUnique(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns.KEY_SURVEY_QUESTION,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns.KEY_MODULE_CODE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns.KEY_MODULE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns.KEY_SURVEY_QUESTION_SEQUENCE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns.KEY_SURVEY_QUESTION_PRODUCT_GROUP_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns.KEY_SURVEY_QUESTION_PRODUCT_TYPE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns.KEY_SURVEY_QUESTION_TYPE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns.KEY_SURVEY_QUESTION_TEXT_LENGTH,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns.KEY_SURVEY_QUESTION_DEPENDENT_OPTION_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns.KEY_SURVEY_QUESTION_IMAGE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATET_MAX_TIMES,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATER_TEXT,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATER_TYPE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns.KEY_IS_MENDATORY,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns.KEY_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_SURVEY_QUESTION_MASTERS,
                columns);

    }

    private static String getCreateUserProfileTableDDL() {

        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.SurveyQuestionColumns._ID, sqlHelper
                .getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserProfileTableColumns.KEY_USER_ID, sqlHelper
                .getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserProfileTableColumns.KEY_USER_PROFILE_EMPLOYEE_CODE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserProfileTableColumns.KEY_USER_PROFILE_USER_CODE, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserProfileTableColumns.KEY_USER_ROLE_ID, sqlHelper
                .getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserProfileTableColumns.KEY_USER_PROFILE_USER_ROLE_ID_ACTUAL,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserProfileTableColumns.EMAIL_ID, sqlHelper.getSQLTypeString())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserProfileTableColumns.KEY_USER_PROFILE_ALTERNATE_EMAIL_ID,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserProfileTableColumns.KEY_USER_PROFILE_FIRST_NAME, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserProfileTableColumns.KEY_USER_PROFILE_LAST_NAME, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserProfileTableColumns.KEY_USER_PROFILE_MOBILE_CALLING,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserProfileTableColumns.KEY_USER_PROFILE_MOBILE_SD, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserProfileTableColumns.KEY_USER_PROFILE_PIN_CODE, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserProfileTableColumns.KEY_USER_PROFILE_IS_OFFLINE_PROFILE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserProfileTableColumns.KEY_USER_PROFILE_ADDRESS, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserProfileTableColumns.KEY_USER_PROFILE_PICTURE_FILE_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserProfileTableColumns.KEY_USER_PROFILE_IS_ROAMING_PRIFLE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_USER_PROFILE, columns);

    }

    private static String getCreateStoreBasicMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(StoreBasicColulmns._ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_BEAT_STORE_ID, sqlHelper
                .getSQLTypeInteger()).isNotNull(true).isUnique(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_BEAT_STORE_NAME, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_BEAT_STORE_CODE, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_BEAT_CITY, sqlHelper.getSQLTypeString())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_BEAT_CHINNEL_TYPE, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_BEAT_CONTACT_PERSION, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_BEAT_COVERAGE_ID, sqlHelper
                .getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_BEAT_EMAIL_ID, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_STORE_ADDRESS, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_BEAT_IS_COVERAGE, sqlHelper
                .getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_BEAT_IS_FREEZE, sqlHelper
                .getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_BEAT_FREEZE_LATTITUDE, sqlHelper
                .getSQLTypeReal()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_BEAT_FREEZE_LONGITUDE, sqlHelper
                .getSQLTypeReal()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_BEAT_MOBILE_NUMBER, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_BEAT_PICTURE_FILE_NAME, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_BEAT_STORE_SIZE, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_BEAT_IS_DISPLAY_COUNTER_SHARE, sqlHelper
                .getSQLTypeBoolean()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_BEAT_IS_PLANOGRAM, sqlHelper
                .getSQLTypeBoolean()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_BEAT_STORE_CLASS, sqlHelper
                .getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_BEAT_TYPE, sqlHelper.getSQLTypeString())
                .createSQLColumn());

		/*
         * @ankit New Added Column in Store Basic
		 */

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_LAND_LINE_NUMBER, sqlHelper
                .getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_SPC_NAME, sqlHelper.getSQLTypeString())
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_SPC_CATEGORY, sqlHelper
                .getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_STORE_MANAGER_NAME, sqlHelper
                .getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_SM_MOBILE, sqlHelper.getSQLTypeString())
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_SM_EMAIL_ID, sqlHelper
                .getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_ALTERNAMTE_EMAIL_ID, sqlHelper
                .getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_TIN_NUMBER, sqlHelper.getSQLTypeString())
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_PIN_CODE, sqlHelper.getSQLTypeInteger())
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_CREATED_DATE_TIME_STAMP, sqlHelper
                .getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_MODIFIED_DATE_TIME_STAMP, sqlHelper
                .getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StoreBasicColulmns.KEY_IS_DELETED, sqlHelper
                .getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_STORE_BASIC_MASTER, columns);

    }

    private static String getCreateStorePerformanceMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();

        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(StorePerformaceColumns._ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StorePerformaceColumns.KEY_BEAT_STORE_ID, sqlHelper
                .getSQLTypeInteger()).isNotNull(true).createSQLColumn());

		/*
         * columns.add(new SQLColumn.SQLColumnBuilder(
		 * StorePerformaceColumns.KEY_BEAT_ACH, sqlHelper.getSQLTypeReal())
		 * .createSQLColumn());
		 * 
		 * columns.add(new SQLColumn.SQLColumnBuilder(
		 * StorePerformaceColumns.KEY_BEAT_TARGET, sqlHelper
		 * .getSQLTypeInteger()).createSQLColumn());
		 * 
		 * columns.add(new SQLColumn.SQLColumnBuilder(
		 * StorePerformaceColumns.KEY_BEAT_ACMTD_PURCHASE, sqlHelper
		 * .getSQLTypeReal()).createSQLColumn());
		 * 
		 * columns.add(new SQLColumn.SQLColumnBuilder(
		 * StorePerformaceColumns.KEY_BEAT_ACMTD_SALE, sqlHelper
		 * .getSQLTypeString()).createSQLColumn());
		 * 
		 * columns.add(new SQLColumn.SQLColumnBuilder(
		 * StorePerformaceColumns.KEY_BEAT_AVMTD_PURCHASE, sqlHelper
		 * .getSQLTypeReal()).createSQLColumn());
		 * 
		 * columns.add(new SQLColumn.SQLColumnBuilder(
		 * StorePerformaceColumns.KEY_BEAT_AVMTD_SALE, sqlHelper
		 * .getSQLTypeReal()).createSQLColumn());
		 * 
		 * columns.add(new SQLColumn.SQLColumnBuilder(
		 * StorePerformaceColumns.KEY_BEAT_AVLMTD_SALE, sqlHelper
		 * .getSQLTypeReal()).createSQLColumn());
		 * 
		 * columns.add(new SQLColumn.SQLColumnBuilder(
		 * StorePerformaceColumns.KEY_BEAT_AVLMTD_PURCHASE, sqlHelper
		 * .getSQLTypeReal()).createSQLColumn());
		 * 
		 * columns.add(new SQLColumn.SQLColumnBuilder(
		 * StorePerformaceColumns.KEY_BEAT_HALMTD_SALE, sqlHelper
		 * .getSQLTypeReal()).createSQLColumn());
		 * 
		 * columns.add(new SQLColumn.SQLColumnBuilder(
		 * StorePerformaceColumns.KEY_BEAT_HALMTD_PURCHASE, sqlHelper
		 * .getSQLTypeReal()).createSQLColumn());
		 * 
		 * columns.add(new SQLColumn.SQLColumnBuilder(
		 * StorePerformaceColumns.KEY_BEAT_ACLMTD_SALE, sqlHelper
		 * .getSQLTypeReal()).createSQLColumn());
		 * 
		 * columns.add(new SQLColumn.SQLColumnBuilder(
		 * StorePerformaceColumns.KEY_BEAT_ACLMTD_PURCHASE, sqlHelper
		 * .getSQLTypeReal()).createSQLColumn());
		 * 
		 * columns.add(new SQLColumn.SQLColumnBuilder(
		 * StorePerformaceColumns.KEY_BEAT_HAMTD_PURCHASE, sqlHelper
		 * .getSQLTypeReal()).createSQLColumn());
		 * 
		 * columns.add(new SQLColumn.SQLColumnBuilder(
		 * StorePerformaceColumns.KEY_BEAT_HAMTD_SALE, sqlHelper
		 * .getSQLTypeReal()).createSQLColumn());
		 * 
		 * columns.add(new SQLColumn.SQLColumnBuilder(
		 * StorePerformaceColumns.KEY_BEAT_LAST_VISITED_DATE, sqlHelper
		 * .getSQLTypeString()).createSQLColumn());
		 */

        // new columns

        columns.add(new SQLColumn.SQLColumnBuilder(
                StorePerformaceColumns.KEY_OUTLET_PERFORMANCE_ID, sqlHelper
                .getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StorePerformaceColumns.KEY_REGION, sqlHelper.getSQLTypeString())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StorePerformaceColumns.KEY_BRANCH, sqlHelper.getSQLTypeString())
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StorePerformaceColumns.KEY_CHANNEL_TYPE, sqlHelper
                .getSQLTypeString()).createSQLColumn());


        columns.add(new SQLColumn.SQLColumnBuilder(
                StorePerformaceColumns.KEY_PRODUCT_TYPE, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StorePerformaceColumns.KEY_PRODUCT_GROUP, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StorePerformaceColumns.KEY_TRANSACTION_TYPE, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StorePerformaceColumns.KEY_SALES_TYPE, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StorePerformaceColumns.KEY_VOLUME, sqlHelper
                .getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                StorePerformaceColumns.KEY_VALUE, sqlHelper.getSQLTypeReal())
                .createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_STORE_PERFORMANCE_MASTER,
                columns);

    }

    private static String getCreateCompetetionProductGroupTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                CompetetionProductGroupColumns.KEY_COMPETETION_PRODUCT_GROUP_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                CompetetionProductGroupColumns.KEY_COMPETETION_PRODUCT_GROUP_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                CompetetionProductGroupColumns.KEY_COMPETETION_PRODUCT_GROUP_CODE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                CompetetionProductGroupColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                CompetetionProductGroupColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                CompetetionProductGroupColumns.KEY_IS_DELETED, sqlHelper
                .getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_COMPETETION_PRODUCT_GROUP,
                columns);

    }

    private static String getCreateCompetitorTableDDL() {

        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                CompetitorColumns.KEY_COMETITOR_ID, sqlHelper
                .getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                CompetitorColumns.KEY_COMETITOR_NAME, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                CompetitorColumns.KEY_COMETITOR_CODE, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                CompetitorColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID, sqlHelper
                .getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                CompetitorColumns.KEY_CREATED_DATE_TIME_STAMP, sqlHelper
                .getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                CompetitorColumns.KEY_MODIFIED_DATE_TIME_STAMP, sqlHelper
                .getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.CompetitorColumns.KEY_IS_DELETED, sqlHelper
                .getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_COMPETITOR_MASTER, columns);
    }

    private static String getCounterShareDisplayShareTableDDL() {

        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                CounterShareDisplayShareResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                CounterShareDisplayShareResponseMasterColumns.KEY_USER_ROLE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                CounterShareDisplayShareResponseMasterColumns.KEY_SURVEY_QUESTION_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                CounterShareDisplayShareResponseMasterColumns.KEY_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE_COMPETITION_TYPE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                CounterShareDisplayShareResponseMasterColumns.KEY_SURVEY_QUESTION_PRODUCT_GROUP_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                CounterShareDisplayShareResponseMasterColumns.KEY_SURVEY_QUESTION_USER_RESPONSE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                CounterShareDisplayShareResponseMasterColumns.KEY_BEAT_STORE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                CounterShareDisplayShareResponseMasterColumns.KEY_USER_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                CounterShareDisplayShareResponseMasterColumns.KEY_COMETITOR_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                CounterShareDisplayShareResponseMasterColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                CounterShareDisplayShareResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE_MASTER,
                        columns);

    }

    private static String getCreateActivityDataMasterTableDDL() {

        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                ActivityDataMasterColumns.KEY_USER_ID, sqlHelper
                .getSQLTypeInteger()).isNotNull(true).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                ActivityDataMasterColumns.KEY_BEAT_STORE_ID, sqlHelper
                .getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                ActivityDataMasterColumns.KEY_MODULE_CODE, sqlHelper
                .getSQLTypeInteger()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_CREATED_DATE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS,
                sqlHelper.getSQLTypeInteger()).isNotNull(true)
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID,
                sqlHelper.getSQLTypeInteger()).isNotNull(true)
                .createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_ACTIVITY_DATA_MASTER,
                columns);

    }

    private static String getCreateUserModuleTableDDL() {

        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.UserModuleTableColumns.KEY_MODULE_CODE,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.UserModuleTableColumns.KEY_MODULE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.UserModuleTableColumns.KEY_NAME, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.UserModuleTableColumns.KEY_MODULE_PARENT_MODULE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.UserModuleTableColumns.KEY_MODULE_SEQUENCE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.UserModuleTableColumns.KEY_MODULE_ICON_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.UserModuleTableColumns.KEY_IS_MENDATORY,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.UserModuleTableColumns.KEY_MODULE_IS_QUESTION_MODULE,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.UserModuleTableColumns.KEY_MODULE_TYPE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.UserModuleTableColumns.KEY_MODULE_IS_STORE_WISE,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.UserModuleTableColumns.KEY_UPDATE_DATE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.UserModuleTableColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.UserModuleTableColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.UserModuleTableColumns.KEY_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_USER_MODULE_MASTER, columns);
    }

    private static String getCreateCollectionResponseTableDDL() {

        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.CollectionResponseTableColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
                sqlHelper.getSQLTypeInteger()).isNotNull(true)
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.CollectionResponseTableColumns.KEY_USER_ROLE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.CollectionResponseTableColumns.KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.CollectionResponseTableColumns.KEY_COLLECTION_TRANSACTION_ID,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.CollectionResponseTableColumns.KEY_COLLECTION_COMMENTS,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.CollectionResponseTableColumns.KEY_COLLECTION_AMOUNT,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.CollectionResponseTableColumns.KEY_PAYMENT_MODE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.CollectionResponseTableColumns.KEY_BEAT_STORE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.CollectionResponseTableColumns.KEY_USER_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.CollectionResponseTableColumns.KEY_COLLECTION_PAYMENT_DATE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.CollectionResponseTableColumns.KEY_ACTIVITY_DATA_MASTER_CREATED_DATE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.CollectionResponseTableColumns.KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_COLLECTION_RESPONSE_MASTER,
                columns);
    }

    private static String getCreateProductMasterTableDDL() {

        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ProductMasterTableColumns.KEY_PRODUCT_MASTER_BASIC_MODEL_CODE,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ProductMasterTableColumns.KEY_PRODUCT_MASTER_BASIC_MODEL_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ProductMasterTableColumns.KEY_PRODUCT_MASTER_CATEGORY_CODE,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ProductMasterTableColumns.KEY_PRODUCT_MASTER_CATEGORY_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ProductMasterTableColumns.KEY_PRODUCT_MASTER_DEALER_PRICE,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ProductMasterTableColumns.KEY_PRODUCT_MASTER_MODEL_TYPE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_CAEGORY_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_CODE,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_CODE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ProductMasterTableColumns.KEY_PRODUCT_MASTER_SKU_CODE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ProductMasterTableColumns.KEY_PRODUCT_MASTER_SKU_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ProductMasterTableColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ProductMasterTableColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ProductMasterTableColumns.KEY_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_PRODUCT_MASTER, columns);
    }

    private static String getCreatePaymentModeMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PaymentModeMasterTableColumns.KEY_PAYMENT_MODE_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PaymentModeMasterTableColumns.KEY_PAYMENT_MODE_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PaymentModeMasterTableColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PaymentModeMasterTableColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PaymentModeMasterTableColumns.KEY_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_PAYMENT_MODE_MASTER,
                        columns);

    }

    private static String getCreateFeedbackStatusMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackStatusMasterColumns._ID, sqlHelper.getSQLTypeInteger())
                .isPrimaryKey(true).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackStatusMasterColumns.KEY_FEEDBACK_STATUS_MASTER_ID,
                sqlHelper.getSQLTypeInteger()).isNotNull(true).isUnique(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackStatusMasterColumns.KEY_FEEDBACK_STATUS_MASTER_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackStatusMasterColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackStatusMasterColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackStatusMasterColumns.KEY_IS_DELETED, sqlHelper
                .getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_FEEDBACK_STATUS_MASTER,
                columns);

    }

    private static String getCreateTeamMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(TeamMasterColumns._ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                TeamMasterColumns.KEY_TEAM_MASTER_ID, sqlHelper
                .getSQLTypeInteger()).isNotNull(true).isUnique(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                TeamMasterColumns.KEY_TEAM_MASTER_NAME, sqlHelper
                .getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                TeamMasterColumns.KEY_CREATED_DATE_TIME_STAMP, sqlHelper
                .getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                TeamMasterColumns.KEY_MODIFIED_DATE_TIME_STAMP, sqlHelper
                .getSQLTypeDateTime()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                TeamMasterColumns.KEY_IS_DELETED, sqlHelper.getSQLTypeBoolean())
                .createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_FMS_TEAM_MASTER, columns);

    }

    private static String getCreateFeedbackCategortMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackCategoryMasterColumns._ID, sqlHelper
                .getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_ID,
                sqlHelper.getSQLTypeInteger()).isNotNull(true).isUnique(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_TEAM_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackCategoryMasterColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackCategoryMasterColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackCategoryMasterColumns.KEY_IS_DELETED, sqlHelper
                .getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_FEEDBACK_CATEGORY_MASTER,
                columns);

    }

    private static String getCreateFeedbackTypeMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackTypeMasterColumns._ID, sqlHelper.getSQLTypeInteger())
                .isPrimaryKey(true).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_ID,
                sqlHelper.getSQLTypeInteger()).isNotNull(true).isUnique(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackTypeMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_SAMPLE_IMAGE_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackTypeMasterColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackTypeMasterColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                FeedbackTypeMasterColumns.KEY_IS_DELETED, sqlHelper
                .getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_FEEDBACK_TYPE_MASTER,
                columns);

    }

    private static String getCreateUserFeedbackMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserFeedbackMasterColumns._ID, sqlHelper.getSQLTypeInteger())
                .isPrimaryKey(true).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserFeedbackMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_TEAM_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserFeedbackMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_TYPE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_REMARK,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_IMAGE_PATH,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_STORE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_USER_FEEDBACK_MASTER,
                columns);

    }

    private static String getCreateOrderResponseTableDDL() {

        SQLiteHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                OrderResponseMasterTableColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
                sqlHelper.getSQLTypeInteger()).isNotNull(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                OrderResponseMasterTableColumns.KEY_USER_ROLE_ID, sqlHelper
                .getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                OrderResponseMasterTableColumns.KEY_ORDER_MASTER_QUANTITY,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDERNO,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                OrderResponseMasterTableColumns.KEY_BEAT_STORE_ID, sqlHelper
                .getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                OrderResponseMasterTableColumns.KEY_USER_ID, sqlHelper
                .getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDER_BOOKING_TYPE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        // product type id
        columns.add(new SQLColumn.SQLColumnBuilder(
                OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDER_BOOKING_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_CAEGORY_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        // covergge
        columns.add(new SQLColumn.SQLColumnBuilder(
                OrderResponseMasterTableColumns.KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                OrderResponseMasterTableColumns.KEY_ORDER_MASTER_PRICE,
                sqlHelper.getSQLTypeReal()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_SKU_CODE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                OrderResponseMasterTableColumns.KEY_ACTIVITY_DATA_MASTER_CREATED_DATE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                OrderResponseMasterTableColumns.KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_ORDER_RESPONSE_MASTER,
                columns);

    }

    private static String getCreateCompetitorProductGroupMappingTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.CompetitorProductGroupMappingTableColumns.KEY_COMPETITOR_PRODUCT_GROUP_MAPPING_MAPPING_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.CompetitorProductGroupMappingTableColumns.KEY_COMETITOR_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.CompetitorProductGroupMappingTableColumns.KEY_COMPETETION_PRODUCT_GROUP_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.CompetitorProductGroupMappingTableColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.CompetitorProductGroupMappingTableColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.CompetitorProductGroupMappingTableColumns.KEY_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_COMPETITOR_PRODUCT_GROUP_MAPPING,
                        columns);
    }

    private static String getCreatePlanogramProductMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramProductMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_ID,
                sqlHelper.getSQLTypeInteger()).isNotNull(true)
                .isPrimaryKey(true).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramProductMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_CLASS,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramProductMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_CHANNEL_TYPE,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramProductMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_COMP_PRODUCT_GROUP,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramProductMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_PRODUCT_CODE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramProductMasterTableColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramProductMasterTableColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramProductMasterTableColumns.KEY_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_PLANOGRAM_PRODUCT_MASTER,
                columns);
    }

    private static String getCreatePlanogramClassMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramClassMasterTableColumns.KEY_PLANOGRAM_CLASS_MASTER_CLASS_ID,
                sqlHelper.getSQLTypeInteger()).isNotNull(true)
                .isPrimaryKey(true).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramClassMasterTableColumns.KEY_PLANOGRAM_CLASS_MASTER_START_RANGE,
                sqlHelper.getSQLTypeInteger()).isNotNull(true)
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramClassMasterTableColumns.KEY_PLANOGRAM_CLASS_MASTER_END_RANGE,
                sqlHelper.getSQLTypeInteger()).isNotNull(true)
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramClassMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_CLASS,
                sqlHelper.getSQLTypeString()).isNotNull(true).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramClassMasterTableColumns.KEY_PLANOGRAM_CLASS_MASTER_COMP_PRODUCT_GROUP_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramClassMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_CHANNEL_TYPE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramClassMasterTableColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramClassMasterTableColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramClassMasterTableColumns.KEY_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_PLANOGRAM_CLASS_MASTER,
                columns);
    }

    private static String getCreatePlanogramResponseMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramResponseMasterTableColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
                sqlHelper.getSQLTypeInteger()).isNotNull(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramResponseMasterTableColumns.KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramResponseMasterTableColumns.KEY_COMPETETION_PRODUCT_GROUP_ID,
                sqlHelper.getSQLTypeInteger()).isNotNull(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramResponseMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_CLASS,
                sqlHelper.getSQLTypeString()).isNotNull(true).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramResponseMasterTableColumns.KEY_PLANOGRAM_CLASS_MASTER_CLASS_ID,
                sqlHelper.getSQLTypeInteger()).isNotNull(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramResponseMasterTableColumns.KEY_PLANOGRAM_RESPONSE_MASTER_ADHERENCE,
                sqlHelper.getSQLTypeReal()).isNotNull(true).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_PLANOGRAM_RESPONSE_MASTER,
                columns);
    }

    private static String getCreatePlanogramProductResponseTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramProductResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
                sqlHelper.getSQLTypeInteger()).isNotNull(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramProductResponseMasterColumns.KEY_PLANOGRAM_PRODUCT_RESPONSE_MASTER_PLAN_PRODUCT_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramProductResponseMasterColumns.KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID,
                sqlHelper.getSQLTypeInteger()).isNotNull(true)
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramProductResponseMasterColumns.KEY_PLANOGRAM_PRODUCT_MASTER_PRODUCT_CODE,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramProductResponseMasterColumns.KEY_PLANOGRAM_PRODUCT_RESPONSE_MASTER_IS_AVAILABLE,
                sqlHelper.getSQLTypeInteger()).isNotNull(true)
                .createSQLColumn());
        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_PLANOGRAM_PRODUCT_RESPONSE_MASTER,
                        columns);

    }

    private static String getCreatePlanogramPranogramProductResponseTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramCompitiorResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
                sqlHelper.getSQLTypeInteger()).isNotNull(true)
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramCompitiorResponseMasterColumns.KEY_PLANOGRAM_COMPITITORS_RESPONSE_MASTER_PLAN_COMPITITOR_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramCompitiorResponseMasterColumns.KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID,
                sqlHelper.getSQLTypeInteger()).isNotNull(true)
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramCompitiorResponseMasterColumns.KEY_COMETITOR_ID,
                sqlHelper.getSQLTypeInteger()).isNotNull(true)
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.PlanogramCompitiorResponseMasterColumns.KEY_PLANOGRAM_COMPITITORS_RESPONSE_MASTER_PLAN_VALUE,
                sqlHelper.getSQLTypeInteger()).isNotNull(true)
                .createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_PLANOGRAM_COMPITIOR_RESPONSE_MASTER,
                        columns);

    }

    private static String getCreateEOLSchemeHeaderTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EOLSchemeHeaderMasterMasterColumns.KEY_EOL_SCHEME_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .isNotNull(true).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EOLSchemeHeaderMasterMasterColumns.KEY_EOL_SCHEME_NUMBER,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EOLSchemeHeaderMasterMasterColumns.KEY_EOL_SCHEME_FROM,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EOLSchemeHeaderMasterMasterColumns.KEY_EOL_SCHEME_TO,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EOLSchemeHeaderMasterMasterColumns.KEY_EOL_ORDER_FROM,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EOLSchemeHeaderMasterMasterColumns.KEY_EOL_ORDER_TO,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EOLSchemeHeaderMasterMasterColumns.KEY_EOL_PUMI_NUMBER,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EOLSchemeHeaderMasterMasterColumns.KEY_EOL_PUMI_DATE,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EOLSchemeHeaderMasterMasterColumns.KEY_EOL_PRODUCT_TYPE,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EOLSchemeHeaderMasterMasterColumns.KEY_EOL_PRODUCT_GROUP,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EOLSchemeHeaderMasterMasterColumns.KEY_PRODUCT_CATEGORY,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EOLSchemeHeaderMasterMasterColumns.KEY_ACTIVITY_DATA_MASTER_CREATED_DATE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EOLSchemeHeaderMasterMasterColumns.KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EOLSchemeHeaderMasterMasterColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EOLSchemeHeaderMasterMasterColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_EOL_SCHEME_HEADER_MASTER,
                columns);

    }

    private static String getCreateStoreAssesmentTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreAssessmentMasterColumns.KEY_STORE_ASSESSMENT_MASTER_STORE_ASSESSMENT_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreAssessmentMasterColumns.KEY_BEAT_STORE_ID,
                sqlHelper.getSQLTypeInteger()).isUnique(true).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreAssessmentMasterColumns.KEY_STORE_ASSESSMENT_MASTER_ASSESSMENT_START_TIME,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreAssessmentMasterColumns.KEY_STORE_ASSESSMENT_MASTER_ASSESSMENT_END_TIME,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreAssessmentMasterColumns.KEY_STORE_ASSESSMENT_MASTER_UPLOADED_ON,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_STORE_ASSESSMENT_MASTER,
                columns);

    }

    private static String getCreateStoreGeoTagMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreGeoTagResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreGeoTagResponseMasterColumns.KEY_BEAT_STORE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_LATITUDE,
                sqlHelper.getSQLTypeReal()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_LONGITUDE,
                sqlHelper.getSQLTypeReal()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_GEO_IMAGE,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_GEO_USER_OPTION,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_MENDATORY,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_PHOTO_MENDATORY,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_IS_GEO_FREEZED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_IS_GEO_FREEZED_LATITUDE,
                sqlHelper.getSQLTypeReal()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_IS_GEO_FREEZED_LONGITUDE,
                sqlHelper.getSQLTypeReal()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_STORE_GEO_TAG_RESPONSE,
                columns);

    }

    private static String getCreateDownloadDataMasterTableDDL() {

        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        /*
		 * columns.add(new SQLColumn.SQLColumnBuilder(
		 * DatabaseConstants.DownloadDataMasterColumns
		 * .KEY_DOWNLOAD_DATA_MASTER_CREATED_DATE,
		 * sqlHelper.getSQLTypeString()).createSQLColumn());
		 * 
		 * columns.add(new SQLColumn.SQLColumnBuilder(
		 * DatabaseConstants.DownloadDataMasterColumns
		 * .KEY_DOWNLOAD_DATA_MASTER_MODIFY_DATE,
		 * sqlHelper.getSQLTypeString()).createSQLColumn());
		 */
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
                sqlHelper.getSQLTypeString()).isPrimaryKey(true)
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.DownloadDataMasterColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.DownloadDataMasterColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_DOWNLOAD_DATA_MASTER,
                columns);

    }

    private static String getCreateDownloadDataMasterMappingTableDDL() {

        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_MAPPING_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER,
                        columns);

    }

    private static String getCreateRaceBrandMasterTableDDL() {

        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandMasterColumns.KEY_BRAND_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandMasterColumns.KEY_BRAND_CODE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandMasterColumns.KEY_BRAND_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandMasterColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandMasterColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandMasterColumns.KEY_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_RACE_BRAND_MASTER, columns);

    }

    private static String getCreateRaceBrandProductCategoryMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandProductCategoryMasterColumns.KEY_COMP_PRODUCT_GROUP_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandProductCategoryMasterColumns.KEY_PRODUCT_GROUP_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandProductCategoryMasterColumns.KEY_PRODUCT_GROUP_CODE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandProductCategoryMasterColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandProductCategoryMasterColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandProductCategoryMasterColumns.KEY_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_RACE_BRAND_PRODUCT_CATEGORY_MASTER,
                        columns);

    }

    private static String getCreateRaceBrandCategoryMappingMasterTableDDL() {

        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandCategoryMappingMasterColumns.KEY_BRAND_CATEGORY_MAPING_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandCategoryMappingMasterColumns.KEY_BRAND_ID,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandCategoryMappingMasterColumns.KEY_COMP_PRODUCT_GROUP_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandCategoryMappingMasterColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandCategoryMappingMasterColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandCategoryMappingMasterColumns.KEY_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_RACE_BRAND_CATEGORY_MAPPING_MASTER,
                        columns);

    }

    private static String getCreateRacePOSMMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RacePosmMasterColumns.KEY_POSM_ID, sqlHelper
                .getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RacePosmMasterColumns.KEY_POSM_CODE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RacePosmMasterColumns.KEY_POSM_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RacePosmMasterColumns.KEY_POSM_CATEGORY,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RacePosmMasterColumns.KEY_POSM_GROUP,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RacePosmMasterColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RacePosmMasterColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RacePosmMasterColumns.KEY_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_RACE_POSM_MASTER, columns);

    }

    private static String getCreateRaceFixtureMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceFixtureMasterColumns.KEY_FIXTURE_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceFixtureMasterColumns.KEY_FIXTURE_CATEGORY_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceFixtureMasterColumns.KEY_FIXTURE_SUB_CATEGORY,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceFixtureMasterColumns.KEY_FIXTURE_PRODUCT_GROUPS,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceFixtureMasterColumns.KEY_FIXTURE_IS_COMPETITOR_AVAILBALE,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceFixtureMasterColumns.KEY_FIXTURE_IS_ROW_AVAILABLE,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceFixtureMasterColumns.KEY_FIXTURE_IS_WALL_AVAILABLE,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceFixtureMasterColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceFixtureMasterColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceFixtureMasterColumns.KEY_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_RACE_FIXTURE_MASTER,
                        columns);

    }

    private static String getCreateRaceBrandProductMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandProductMasterColumns.KEY_BRAND_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandProductMasterColumns.KEY_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandProductMasterColumns.KEY_PRODUCT_CATEGORY,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandProductMasterColumns.KEY_PRODUCT_GROUP,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandProductMasterColumns.KEY_PRODUCT_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandProductMasterColumns.KEY_PRODUCT_TYPE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandProductMasterColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandProductMasterColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceBrandProductMasterColumns.KEY_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_RACE_BRAND_PRODUCT_MASTER,
                columns);

    }

    private static String getCreateRacePOSMProductMappingMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RacePOSMProductMappingMasterColumns.KEY_POSM_PRODUCT_MAPPING_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RacePOSMProductMappingMasterColumns.KEY_POSM_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RacePOSMProductMappingMasterColumns.KEY_PRODUCT_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RacePOSMProductMappingMasterColumns.KEY_CREATED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RacePOSMProductMappingMasterColumns.KEY_MODIFIED_DATE_TIME_STAMP,
                sqlHelper.getSQLTypeDateTime()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RacePOSMProductMappingMasterColumns.KEY_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_RACE_PRODUCT_POSM_MAPPING_MASTER,
                        columns);

    }

    private static String getCreateRaceProductAuditResponseTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceProductAuditResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceProductAuditResponseMasterColumns.KEY_FIXTURE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceProductAuditResponseMasterColumns.KEY_PRODUCT_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceProductAuditResponseMasterColumns.KEY_WALL_NUMBER,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceProductAuditResponseMasterColumns.KEY_ROW_NUMBER,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceProductAuditResponseMasterColumns.KEY_TOPPER,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceProductAuditResponseMasterColumns.KEY_SWITCHED_ON,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceProductAuditResponseMasterColumns.KEY_BRAND_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceProductAuditResponseMasterColumns.KEY_PRICE_TAG,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RaceProductAuditResponseMasterColumns.KEY_STOCK_AUDIT_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_RACE_PRODUCT_AUDIT_RESPONSE_MASTER,
                        columns);

    }

    private static String getCreateRacePOSMDataResponseMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RacePOSMDataResponseMasterColumns.KEY_POSM_MAPPING_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RacePOSMDataResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RacePOSMDataResponseMasterColumns.KEY_STOCK_AUDIT_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RacePOSMDataResponseMasterColumns.KEY_POSM_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.RacePOSMDataResponseMasterColumns.KEY_POSM_TYPE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_RACE_POSM_DATA_RESPONSE_MASTER,
                        columns);

    }

    private static String getCreateEOLSchemeDetailTableDDL() {
        // TODO Auto-generated method stub
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                EOLSchemeDetailsMasterColoums.KEY_EOL_SCHEME_ID, sqlHelper
                .getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                EOLSchemeDetailsMasterColoums.KEY_EOL_DETAILS_BASIC_MODEL_CODE,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                EOLSchemeDetailsMasterColoums.KEY_EOL_DETAILS_ORDER_QUATITY,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                EOLSchemeDetailsMasterColoums.KEY_EOL_DETAILS_ACTUAL_SUPPORT,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_EOL_SCHEME_DETAILS_MASTER,
                        columns,
                        new String[]{
                                EOLSchemeDetailsMasterColoums.KEY_EOL_SCHEME_ID,
                                EOLSchemeDetailsMasterColoums.KEY_EOL_DETAILS_BASIC_MODEL_CODE});
    }

    public static String getCreateStoreModuleMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreModuleMasterColumns.KEY_STORE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreModuleMasterColumns.KEY_MODULE_CODE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreModuleMasterColumns.KEY_MODULE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreModuleMasterColumns.KEY_MODULE_PARENT_MODULE_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreModuleMasterColumns.KEY_IS_MENDATORY,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.StoreModuleMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_STORE_MODULE_MASTER,
                        columns,
                        new String[]{
                                DatabaseConstants.StoreModuleMasterColumns.KEY_STORE_ID,
                                DatabaseConstants.StoreModuleMasterColumns.KEY_MODULE_CODE});

    }

    public static String getCreateMOMAttendeesMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.MOMAttendeesMasterColumns.KEY_MOM_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.MOMAttendeesMasterColumns.KEY_MOM_ATTENDEES_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_MOM_ATTENDEES_MASTER,
                        columns);

    }


    public static String getCreateMOMDetailsMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();
        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.MOMDetailsMasterColumns.KEY_MOM_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.MOMDetailsMasterColumns.KEY_MOM_TITLE,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.MOMDetailsMasterColumns.KEY_MOM_DATE_TEXT,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.MOMDetailsMasterColumns.KEY_MOM_DATE_VALUE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.MOMDetailsMasterColumns.KEY_MOM_LOCATION,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.MOMDetailsMasterColumns.KEY_MOM_ACTION_ITEM,
                sqlHelper.getSQLTypeString()).createSQLColumn());
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.MOMDetailsMasterColumns.KEY_MOM_DISCRIPETION,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.MOMDetailsMasterColumns.KEY_MOM_SERVER_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.MOMDetailsMasterColumns.KEY_MOM_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.MOMDetailsMasterColumns.KEY_MOM_IS_UPDATED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());


        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_MOM_DETAILS_MASTER,
                        columns);

    }

    public static List<String> getSQLTableCreateNewTablesOnUpgradeStatements() {

        List<String> statements = new ArrayList<String>();
        // statements.add(getCreateStoreModuleMasterTableDDL());
        statements.add(getCreateUserModuleTableDDL());
        statements.add(getCreateSurveyQuestionMasterTableDDL());
        statements.add(getCreateSurveyQuestionOptionsMasterTableDDL());

        return statements;
    }

    public static List<String> getSQLTableDropStatementsOnUpgrade() {
        SQLHelper sqlHelper = new SQLiteHelper();

        List<String> statements = new ArrayList<String>();

		/*
		 * statements .add(sqlHelper
		 * .getTableDropDDL(DatabaseConstants.TableNames
		 * .TABLE_SURVEY_QUESTION_MASTERS)); // statements .add(sqlHelper
		 * .getTableDropDDL
		 * (DatabaseConstants.TableNames.TABLE_SURVEY_QUESTION_OPTION_MASTERS
		 * ));//
		 * 
		 * statements .add(sqlHelper
		 * .getTableDropDDL(DatabaseConstants.TableNames
		 * .TABLE_USER_MODULE_MASTER));//
		 */

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_PRODUCT_MASTER));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_PAYMENT_MODE_MASTER));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_COMPETITOR_MASTER));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_COMPETETION_PRODUCT_GROUP));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_SURVEY_QUESTION_MASTERS));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_SURVEY_QUESTION_OPTION_MASTERS));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_PLANOGRAM_PRODUCT_MASTER));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_PLANOGRAM_CLASS_MASTER));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_COMPETITOR_PRODUCT_GROUP_MAPPING));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_FEEDBACK_STATUS_MASTER));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_FMS_TEAM_MASTER));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_FEEDBACK_CATEGORY_MASTER));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_FEEDBACK_TYPE_MASTER));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_EOL_SCHEME_HEADER_MASTER));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_EOL_SCHEME_DETAILS_MASTER));

        // -- User Module
        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_USER_MODULE_MASTER));

        // --- Download Data Master

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_DOWNLOAD_DATA_MASTER));

        // Race Module

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_RACE_BRAND_CATEGORY_MAPPING_MASTER));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_RACE_BRAND_MASTER));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_RACE_FIXTURE_MASTER));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_RACE_POSM_MASTER));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_RACE_BRAND_PRODUCT_CATEGORY_MASTER));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_RACE_PRODUCT_POSM_MAPPING_MASTER));

        statements
                .add(sqlHelper
                        .getTableDropDDL(DatabaseConstants.TableNames.TABLE_RACE_BRAND_PRODUCT_MASTER));

        return statements;

    }

    // Expense Modules


    public static String getExpenseTypeMasterTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();

        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_CODE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_DESCRIPTION,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_COMPANY_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_SEQUENCE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_IS_ACTIVE,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        return sqlHelper
                .getTableCreateDDL(
                        DatabaseConstants.TableNames.TABLE_EXPENSE_TYPE_MASTER,
                        columns);

    }

    public static String getExpenseDetailsTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();

        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_TYPE_MASTER_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSExpenseDetailTableColumns.KEY_IS_ACTIVE,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSExpenseDetailTableColumns.KEY_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSExpenseDetailTableColumns.KEY_BILLABLE,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSExpenseDetailTableColumns.KEY_BILLABLE_TO,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_COMMENT,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSExpenseDetailTableColumns.KEY_STATUS,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSExpenseDetailTableColumns.KEY_CREATED_DATE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSExpenseDetailTableColumns.KEY_SUBMITTED_DATE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        //
        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSExpenseDetailTableColumns.KEY_MODIFIED_DATE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSExpenseDetailTableColumns.KEY_MODIFIED_BY,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMSEXPENSE_DETAIL_ID_SERVER,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSExpenseDetailTableColumns.KEY_IS_EXPENSE_UPDATED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_TAB_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());


        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_PENDING_WITH,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_ASSIGNED_TO_USER,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());


        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_ASSIGNED_STATUS,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());


        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_CREATED_BY,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());



        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_EMS_EXPENSE_DETAIL, columns);

    }

    public static String getEMSBillDetailTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();

        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDetailTableColumns.KEY_EMS_BILL_DETAIL_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDetailTableColumns.KEY_EMS_BILL_DETAIL_ID_SERVER,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDetailTableColumns.KEY_BILL_DATE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDetailTableColumns.KEY_BILL_NO,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDetailTableColumns.KEY_DESCRIPTION,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDetailTableColumns.KEY_AMOUNT,
                sqlHelper.getSQLTypeReal()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDetailTableColumns.KEY_IS_ACTIVE,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDetailTableColumns.KEY_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDetailTableColumns.KEY_CREATED_DATE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDetailTableColumns.KEY_CREATED_BY,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDetailTableColumns.KEY_MODIFIED_DATE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDetailTableColumns.KEY_MODIFIED_BY,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_EMS_BILL_DETAIL, columns);

    }

    public static String getEMSBillDocumentDetailTableDDL() {
        SQLHelper sqlHelper = new SQLiteHelper();

        List<SQLColumn> columns = new ArrayList<SQLColumn>();

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DOCUMENT_DETAIL_ID,
                sqlHelper.getSQLTypeInteger()).isPrimaryKey(true)
                .createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DOCUMENT_DETAIL_ID_SERVER,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DETAIL_ID,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_DOCUMENT_NAME,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_DOCUMENT_PATH,
                sqlHelper.getSQLTypeString()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_IS_ACTIVE,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_IS_DELETED,
                sqlHelper.getSQLTypeBoolean()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_CREATED_DATE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_CREATED_BY,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_MODIFIED_DATE,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        columns.add(new SQLColumn.SQLColumnBuilder(
                DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_MODIFIED_BY,
                sqlHelper.getSQLTypeInteger()).createSQLColumn());

        return sqlHelper.getTableCreateDDL(
                DatabaseConstants.TableNames.TABLE_EMS_BILL_DOCUMENT_DETAIL,
                columns);

    }

}
