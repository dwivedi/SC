package com.samsung.ssc.database;

/**
 * @author d.ashish
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.samsung.ssc.LMS.LMSConstants;
import com.samsung.ssc.constants.DownloadMasterCodes;
import com.samsung.ssc.EMS.EMSConstants;
import com.samsung.ssc.constants.ModuleCode;
import com.samsung.ssc.constants.QuestionType;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseConstants.ActivityDataMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.DownloadDataMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.DownloadDataModuleMappingTableColums;
import com.samsung.ssc.database.DatabaseConstants.MOMAttendeesMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.MOMDetailsMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.StoreBasicColulmns;
import com.samsung.ssc.database.DatabaseConstants.StoreModuleMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.TableNames;
import com.samsung.ssc.database.DatabaseConstants.UserModuleTableColumns;
import com.samsung.ssc.database.sql.SQLDDL;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.dto.BillDocumentSubmitResponse;
import com.samsung.ssc.dto.BillSubmitResponse;
import com.samsung.ssc.dto.Child;
import com.samsung.ssc.dto.CompetitionProductGroupDto;
import com.samsung.ssc.dto.CompetitosrList;
import com.samsung.ssc.dto.EMSBillDetail;
import com.samsung.ssc.dto.EMSBillDocumentDetail;
import com.samsung.ssc.dto.EMSExpenseDetail;
import com.samsung.ssc.dto.EOLSchemeDTO;
import com.samsung.ssc.dto.EOLSchemeDetailDTO;
import com.samsung.ssc.dto.ExpenseBillDocumentMultipartUpload;
import com.samsung.ssc.dto.ExpenseSubmitResponse;
import com.samsung.ssc.dto.Group;
import com.samsung.ssc.dto.LMSLeaveDataModal;
import com.samsung.ssc.dto.LMSLeaveDateModal;
import com.samsung.ssc.dto.LMSLeaveStatusLogModal;
import com.samsung.ssc.dto.LMSLeaveTypeModal;
import com.samsung.ssc.dto.MOMDetailModal;
import com.samsung.ssc.dto.Module;
import com.samsung.ssc.dto.Option;
import com.samsung.ssc.dto.P1CategoryList;
import com.samsung.ssc.dto.PlanogramProductDataModal;
import com.samsung.ssc.dto.Question;
import com.samsung.ssc.dto.ResponseDto;
import com.samsung.ssc.sync.SyncUtils;
import com.samsung.ssc.util.Constants;
import com.samsung.ssc.util.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static DatabaseHelper dbHelper;
	private static final String DATABASE_NAME = "SSC_DB";
	// RELEASE_CHECK
	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_STORE_BASIC_MASTER = "StoreBasicMaster";
	private static final String TABLE_STORE_PERFORMANCE_MASTER = "StorePerformanceMaster";
	private static final String TABLE_USER_MODULE_MASTER = "UserModuleMaster";
	private static final String TABLE_SURVEY_QUESTION_MASTERS = "SurveyQuestionMaster";
	private static final String TABLE_SURVEY_QUESTION_OPTION_MASTERS = "SurveyQuestionOptionMaster";
	private static final String TABLE_PRODUCT_MASTER = "ProductMaster";
	private static final String TABLE_PAYMENT_MODE_MASTER = "PaymentModeMaster";
	private static final String TABLE_COMPETITOR_MASTER = "CompetitorMaster";
	private static final String TABLE_COMPETETION_PRODUCT_GROUP = "CompProductGroups";
	private static final String TABLE_PLANOGRAM_PRODUCT_MASTER = "PlanogramProductMaster";
	private static final String TABLE_PLANOGRAM_CLASS_MASTER = "PlanogramClassMaster";
	private static final String TABLE_COMPETITOR_PRODUCT_GROUP_MAPPING = "CompetitorProductGroupMapping";
	private static final String TABLE_FMS_TEAM_MASTER = "TeamMaster";
	private static final String TABLE_FEEDBACK_CATEGORY_MASTER = "FeedbackCategoryMaster";
	private static final String TABLE_FEEDBACK_TYPE_MASTER = "FeedbackTypeMaster";
	private static final String TABLE_USER_FEEDBACK_MASTER = "UserFeedbackMaster";
	private static final String TABLE_FEEDBACK_STATUS_MASTER = "FeedbackStatusMaster";
	private static final String TABLE_EOL_SCHEME_HEADER_MASTER = "EOLSchemeHeaderMaster";
	private static final String TABLE_EOL_SCHEME_DETAILS_MASTER = "EOLSchemeDetailsMaster";
	private static final String TABLE_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE_MASTER = "CounterShareDisplayShareResponseMaster";
	private static final String TABLE_QUESTION_ANSWER_RESPONSE_MASTER = "QuestionAnswerResponseMaster";
	private static final String TABLE_ORDER_RESPONSE_MASTER = "OrderResponseMaster";
	private static final String TABLE_COLLECTION_RESPONSE_MASTER = "CollectionResponseMaster";
	private static final String TABLE_PLANOGRAM_RESPONSE_MASTER = "PlanogramResponseMaster";
	private static final String TABLE_PLANOGRAM_PRODUCT_RESPONSE_MASTER = "PlanogramProductResponseMaster";
	private static final String TABLE_PLANOGRAM_COMPITIOR_RESPONSE_MASTER = "PlanogramCompititorResponseMaster";
	private static final String TABLE_EOL_ORDER_BOOKING_RESPONSE_MASTER = "EOLOrderBookingResponseMaster";

	private static final String TABLE_STORE_GEO_TAG_RESPONSE = "StoreGeoTagMaster";
	private static final String TABLE_ACTIVITY_DATA_MASTER = "ActivityDataMaster";
	private static final String TABLE_STORE_ASSESSMENT_MASTER = "StoreAssessmentMaster";
	private static final String TABLE_RACE_BRAND_MASTER = "RaceBrandMaster";
	private static final String TABLE_RACE_BRAND_PRODUCT_CATEGORY_MASTER = "RaceBrandProductCategoryMaster";
	private static final String TABLE_RACE_BRAND_CATEGORY_MAPPING_MASTER = "RaceBrandCategoryMappingMaster";
	private static final String TABLE_RACE_POSM_MASTER = "RacePOSMMaster";
	private static final String TABLE_RACE_FIXTURE_MASTER = "RaceFixtureMaster";
	private static final String TABLE_RACE_BRAND_PRODUCT_MASTER = "RaceProductMaster";
	private static final String TABLE_RACE_PRODUCT_POSM_MAPPING_MASTER = "RaceProductPosmMappingMaster";
	private static final String TABLE_RACE_PRODUCT_AUDIT_RESPONSE_MASTER = "RaceProductAuditResponse";
	private static final String TABLE_RACE_POSM_DATA_RESPONSE_MASTER = "RacePosmDataResponse";

	private static final String KEY_EOL_SCHEME_ID = "SchemeID";
	private static final String KEY_EOL_SCHEME_NUMBER = "SchemeNumber";
	private static final String KEY_EOL_SCHEME_FROM = "SchemeFrom";
	private static final String KEY_EOL_SCHEME_TO = "SchemeTo";
	private static final String KEY_EOL_ORDER_FROM = "OrderFrom";
	private static final String KEY_EOL_ORDER_TO = "OrderTo";
	private static final String KEY_EOL_PUMI_NUMBER = "PUMINumber";
	private static final String KEY_EOL_PUMI_DATE = "PUMIDate";
	private static final String KEY_EOL_PRODUCT_TYPE = "ProductType";
	private static final String KEY_EOL_PRODUCT_GROUP = "ProductGroup";
	private static final String KEY_PRODUCT_CATEGORY = "ProductCategory";

	private static final String KEY_EOL_DETAILS_BASIC_MODEL_CODE = "BasicModelCode";
	private static final String KEY_EOL_DETAILS_ORDER_QUATITY = "OrderQuantity";
	private static final String KEY_EOL_DETAILS_ACTUAL_SUPPORT = "ActualSupport";

	private static final String KEY_MODULE_ICON_NAME = "Icon";
	private static final String KEY_IS_MENDATORY = "IsMandatory";
	private static final String KEY_MODULE_IS_QUESTION_MODULE = "IsQuestionModule";
	private static final String KEY_MODULE_IS_STORE_WISE = "IsStoreWise";
	private static final String KEY_MODULE_TYPE = "ModuleType";
	private static final String KEY_MODULE_CODE = "ModuleCode";
	private static final String KEY_MODULE_ID = "ModuleID";
	private static final String KEY_NAME = "Name";
	private static final String KEY_MODULE_PARENT_MODULE_ID = "ParentModuleID";
	private static final String KEY_MODULE_SEQUENCE = "Sequence";

	private static final String KEY_SURVEY_QUESTION_ID = "SurveyQuestionID";
	private static final String KEY_SURVEY_QUESTION_TEXT_LENGTH = "TextLength";
	private static final String KEY_SURVEY_QUESTION_SEQUENCE = "Sequence";
	private static final String KEY_SURVEY_QUESTION_TYPE_ID = "QuestionTypeID";
	private static final String KEY_SURVEY_QUESTION = "Question";
	private static final String KEY_SURVEY_QUESTION_PRODUCT_TYPE_ID = "ProductTypeID";
	private static final String KEY_SURVEY_QUESTION_PRODUCT_GROUP_ID = "ProductGroupID";
	private static final String KEY_SURVEY_QUESTION_IMAGE = "QuestionImage";
	private static final String KEY_SURVEY_QUESTION_DEPENDENT_OPTION_ID = "DependentOptionID";
	private static final String KEY_SURVEY_QUESTION_REPEATET_MAX_TIMES = "RepeatMaxTimes";
	private static final String KEY_SURVEY_QUESTION_REPEATER_TEXT = "RepeaterText";
	private static final String KEY_SURVEY_QUESTION_REPEATER_TYPE_ID = "RepeaterTypeID";

	private static final String KEY_SURVEY_QUESTION_OPTION_VALUE = "OptionValue";
	private static final String KEY_SURVEY_QUESTION_OPTION_ID = "SurveyOptionID";
	private static final String KEY_SURVEY_QUESTION_OPTION_IS_AFFIRMATIVE = "IsAffirmative";

	private static final String KEY_BEAT_IS_COVERAGE = "IsCoverage";

	private static final String KEY_BEAT_STORE_ID = "StoreID";
	private static final String KEY_BEAT_STORE_NAME = "StoreName";
	private static final String KEY_BEAT_TYPE = "Type";

	private static final String KEY_PRODUCT_MASTER_PRODUCT_TYPE_CODE = "ProductTypeCode";
	// also use in competitore table
	private static final String KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID = "ProductTypeID";

	private static final String KEY_PRODUCT_MASTER_PRODUCT_TYPE_NAME = "ProductTypeName";

	// COLUM COMPETETION PRODUCT GROUP
	private static final String KEY_COMPETETION_PRODUCT_GROUP_ID = "CompProductGroupID";
	private static final String KEY_COMPETETION_PRODUCT_GROUP_CODE = "CompetetionProductGroupCode";
	private static final String KEY_COMPETETION_PRODUCT_GROUP_NAME = "CompetetionProductGroupName";

	// column for competitor
	private static final String KEY_COMETITOR_CODE = "CompetitorCode";
	private static final String KEY_COMETITOR_ID = "CompetitorID";
	private static final String KEY_COMETITOR_NAME = "CompetitorName";

	// COLOMN TABLE FOR PLANOGRAM_PRODUCT_MASTER
	private static final String KEY_PLANOGRAM_PRODUCT_MASTER_ID = "PlanogramProductMasterID";
	private static final String KEY_PLANOGRAM_PRODUCT_MASTER_CLASS = "Class";
	private static final String KEY_PLANOGRAM_PRODUCT_MASTER_CHANNEL_TYPE = "ChannelType";
	private static final String KEY_PLANOGRAM_PRODUCT_MASTER_COMP_PRODUCT_GROUP = "CompProductGroup";
	private static final String KEY_PLANOGRAM_PRODUCT_MASTER_PRODUCT_CODE = "ProductCode";

	private static final String KEY_PLANOGRAM_CLASS_MASTER_CLASS_ID = "ClassID";
	private static final String KEY_PLANOGRAM_CLASS_MASTER_COMP_PRODUCT_GROUP_ID = "CompProdGroupID";

	// COLUMN TABLE FOR TEAM MASTER
	private static final String KEY_TEAM_MASTER_ID = "TeamId";
	private static final String KEY_TEAM_MASTER_NAME = "TeamName";

	// COLUMN TABLE FOR FEEDBACK CATEGORY MASTER
	private static final String KEY_FEEDBACK_CATEGORY_MASTER_ID = "FeedbackCatID";
	private static final String KEY_FEEDBACK_CATEGORY_MASTER_NAME = "FeedbackCategoryName";

	// COLUMN TABLE FOR FEEDBACK TYPE MASTER
	private static final String KEY_FEEDBACK_TYPE_MASTER_ID = "FeedbackTypeID";
	private static final String KEY_FEEDBACK_TYPE_MASTER_NAME = "FeedbackTypeName";

	private static final String KEY_USER_FEEDBACK_MASTER_STORE_ID = "StoreID";

	// COLUMN TABLE FOR FEEDBACK STATUS MASTER
	private static final String KEY_FEEDBACK_STATUS_MASTER_ID = "FeedbackStatusID";
	private static final String KEY_FEEDBACK_STATUS_MASTER_NAME = "FeedbackStatusName";

	// COLUMN FOR ACTIVITY DATA MASTER
	private static final String KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID = "ActivityID";
	private static final String KEY_USER_ID = "UserID";
	private static final String KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID = "SurveyResponseID";
	private static final String KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID = "CoverageID";
	private static final String KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS = "SyncStatus";
	private static final String KEY_ACTIVITY_DATA_MASTER_CREATED_DATE = "CreatedDate";
	private static final String KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE = "ModifiedDate";

	private static final String KEY_STORE_ASSESSMENT_MASTER_ASSESSMENT_START_TIME = "AssessmentStartTime";
	private static final String KEY_STORE_ASSESSMENT_MASTER_ASSESSMENT_END_TIME = "AssessmentEndTime";

	private static final String KEY_SURVEY_QUESTION_USER_RESPONSE = "UserResponse";
	private static final String KEY_SURVEY_QUESTION_SUB_ID = "SurveyQuestionSubID";

	private static final String KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID = "PlanResponseId";
	private static final String KEY_PLANOGRAM_RESPONSE_MASTER_ADHERENCE = "Adherence";
	private static final String KEY_PLANOGRAM_PRODUCT_RESPONSE_MASTER_IS_AVAILABLE = "IsAvailable";
	private static final String KEY_PLANOGRAM_COMPITITORS_RESPONSE_MASTER_PLAN_VALUE = "Value";

	private static final String KEY_STORE_GEO_TAG_LATITUDE = "Latitude";
	private static final String KEY_STORE_GEO_TAG_LONGITUDE = "Longitude";
	private static final String KEY_STORE_GEO_TAG_GEO_IMAGE = "GeoImage";
	private static final String KEY_STORE_GEO_TAG_GEO_USER_OPTION = "UserOption";

	public static synchronized DatabaseHelper getConnection(Context context) {
		if (dbHelper == null) {
			dbHelper = new DatabaseHelper(context);
		}
		return dbHelper;
	}

	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.setLockingEnabled(false);

		for (String stmt : SQLDDL.getSQLTableCreateStatements()) {
			db.execSQL(stmt);
		}

		insertDataInDownloadDataMasterTable(db);
		insertDataInDownloadMappingModuleDataMasterTable(db);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { 
		
	}

	public ArrayList<CompetitosrList> getCompititorByCompProductGroupId(
			int compProductGroupID) {

		SQLiteDatabase db = this.getReadableDatabase();

		ArrayList<CompetitosrList> competitors = new ArrayList<CompetitosrList>();
		try {

			String selectQuery = "SELECT * FROM  " + TABLE_COMPETITOR_MASTER
					+ " AS CM INNER JOIN  "
					+ TABLE_COMPETITOR_PRODUCT_GROUP_MAPPING + " CGM on CM."
					+ KEY_COMETITOR_ID + "=CGM." + KEY_COMETITOR_ID
					+ "  WHERE CGM." + KEY_COMPETETION_PRODUCT_GROUP_ID + "="
					+ compProductGroupID;

			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					try {
						CompetitosrList obj = new CompetitosrList();
						obj.setCode(cursor.getString(cursor
								.getColumnIndex(KEY_COMETITOR_CODE)));
						obj.setCompetitorID(String.valueOf(cursor.getInt(cursor
								.getColumnIndex(KEY_COMETITOR_ID))));
						obj.setName(cursor.getString(cursor
								.getColumnIndex(KEY_COMETITOR_NAME)));
						obj.setProductTypeID(String.valueOf(cursor.getInt(cursor
								.getColumnIndex(KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID))));
						competitors.add(obj);
					} catch (Exception e) {
						Helper.printLog("getCompetitorsList",
								"" + e.getMessage());
						Helper.printStackTrace(e);
					}

				} while (cursor.moveToNext());
			}
			cursor.close();

		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			db.close();
		}
		return competitors;

	}

	public ArrayList<CompetitionProductGroupDto> getCompetitionProductGroup() {

		SQLiteDatabase db = this.getReadableDatabase();

		ArrayList<CompetitionProductGroupDto> prodItems = new ArrayList<CompetitionProductGroupDto>();
		try {
			String selectQuery = "SELECT " + KEY_COMPETETION_PRODUCT_GROUP_ID
					+ "," + KEY_COMPETETION_PRODUCT_GROUP_CODE + ","
					+ KEY_COMPETETION_PRODUCT_GROUP_NAME + " FROM "
					+ TABLE_COMPETETION_PRODUCT_GROUP;
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					try {
						CompetitionProductGroupDto obj = new CompetitionProductGroupDto();
						obj.setCompProductGroupID(""
								+ cursor.getInt(cursor
								.getColumnIndex(KEY_COMPETETION_PRODUCT_GROUP_ID)));
						obj.setProductGroupCode(cursor.getString(cursor
								.getColumnIndex(KEY_COMPETETION_PRODUCT_GROUP_CODE)));
						obj.setProductGroupName(cursor.getString(cursor
								.getColumnIndex(KEY_COMPETETION_PRODUCT_GROUP_NAME)));
						obj.setSuccess(true);

						prodItems.add(obj);
					} catch (Exception e) {

						Helper.printStackTrace(e);
						/*
						 * Helper.printLog("getCompetitionProductGroup", "" +
						 * e.getMessage()); Helper.printStackTrace(e);
						 */
					}
				} while (cursor.moveToNext());
			}
			cursor.close();

		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			db.close();
		}
		return prodItems;

	}

	private ArrayList<Child> getChildList(int comProductGroupId) {

		SQLiteDatabase db = this.getReadableDatabase();

		ArrayList<Child> childs = new ArrayList<Child>();
		try {

			String selectQuery = "SELECT * FROM  " + TABLE_COMPETITOR_MASTER
					+ " AS CM INNER JOIN  "
					+ TABLE_COMPETITOR_PRODUCT_GROUP_MAPPING + " CGM on CM."
					+ KEY_COMETITOR_ID + "= CGM." + KEY_COMETITOR_ID
					+ "  WHERE CGM." + KEY_COMPETETION_PRODUCT_GROUP_ID + "="
					+ comProductGroupId;

			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					try {
						Child obj = new Child();
						obj.setCode(cursor.getString(cursor
								.getColumnIndex(KEY_COMETITOR_CODE)));
						obj.setCompetitorID(String.valueOf(cursor.getInt(cursor
								.getColumnIndex(KEY_COMETITOR_ID))));
						obj.setName(cursor.getString(cursor
								.getColumnIndex(KEY_COMETITOR_NAME)));
						obj.setProductTypeID(String.valueOf(cursor.getInt(cursor
								.getColumnIndex(KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID))));
						childs.add(obj);
					} catch (Exception e) {
						Helper.printLog("getCompetitorsList",
								"" + e.getMessage());
						Helper.printStackTrace(e);
					}

				} while (cursor.moveToNext());
			}
			cursor.close();

		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			db.close();
		}
		return childs;

	}

	public ArrayList<Module> getUserModuleByParentId(
			int moduleParentId, long storeID) {

		ArrayList<Module> moduleList = new ArrayList<Module>();

		SQLiteDatabase db = this.getReadableDatabase();
		try {

			String query = "SELECT UM." + KEY_MODULE_ICON_NAME + ",UM."
					+ KEY_IS_MENDATORY + ",UM." + KEY_MODULE_IS_QUESTION_MODULE
					+ " ,UM." + KEY_MODULE_TYPE + ",UM."
					+ KEY_MODULE_IS_STORE_WISE + ",UM." + KEY_MODULE_CODE
					+ ",UM." + KEY_MODULE_ID + ",UM." + KEY_NAME + ",UM."
					+ KEY_MODULE_PARENT_MODULE_ID + ",UM."
					+ KEY_MODULE_SEQUENCE + ",ADM."
					+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + ",ADM."
					+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " FROM "
					+ TABLE_USER_MODULE_MASTER + " UM LEFT JOIN "
					+ TABLE_ACTIVITY_DATA_MASTER + " ADM ON UM."
					+ KEY_MODULE_CODE + "=ADM." + KEY_MODULE_CODE + " AND adm."
					+ KEY_BEAT_STORE_ID + " = " + storeID + " AND "
					+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " != "
					+ SyncUtils.SYNC_STATUS_SYNC_COMPLETED + " WHERE UM."
					+ KEY_MODULE_PARENT_MODULE_ID + " = " + moduleParentId
					+ " ORDER BY " + "UM." + KEY_MODULE_SEQUENCE + " ASC "
					+ ",UM." + KEY_NAME + " ASC ;";

			Cursor mCursor = db.rawQuery(query, null);
			if (mCursor.moveToFirst()) {
				do {
					Module module = new Module();

					module.setModuleID(mCursor.getInt(mCursor
							.getColumnIndex(KEY_MODULE_ID)));

					module.setModuleType(mCursor.getInt(mCursor
							.getColumnIndex(KEY_MODULE_TYPE)));

					module.setModuleCode(mCursor.getInt(mCursor
							.getColumnIndex(KEY_MODULE_CODE)));
					module.setIconName(mCursor.getString(mCursor
							.getColumnIndex(KEY_MODULE_ICON_NAME)));
					module.setIsMandatory(mCursor.getInt((mCursor
							.getColumnIndex(KEY_IS_MENDATORY))) == 1 ? true
							: false);
					module.setName(mCursor.getString(mCursor
							.getColumnIndex(KEY_NAME)));
					module.setParentModuleID(mCursor.getInt(mCursor
							.getColumnIndex(KEY_MODULE_PARENT_MODULE_ID)));
					module.setQuestionType(mCursor.getInt((mCursor
							.getColumnIndex(KEY_MODULE_IS_QUESTION_MODULE))) == 1 ? true
							: false);
					module.setSequence(mCursor.getInt(mCursor
							.getColumnIndex(KEY_MODULE_SEQUENCE)));
					module.setStoreWise(mCursor.getInt((mCursor
							.getColumnIndex(KEY_MODULE_IS_STORE_WISE))) == 1 ? true
							: false);

					int activtyID = mCursor
							.getInt((mCursor
									.getColumnIndex(KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID)));

					if (activtyID != 0) {
						module.setActivityIDAvailable(true);
					} else {
						module.setActivityIDAvailable(false);
					}
					module.setSyncStatus(mCursor.getInt(mCursor
							.getColumnIndex(KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS)));

					moduleList.add(module);
				} while (mCursor.moveToNext());
			}

		} catch (Exception e) {
		}

		return moduleList;

	}

	public ArrayList<Module> getUserModuleByParentId(
			int moduleParentId) {

		ArrayList<Module> moduleList = new ArrayList<Module>();

		SQLiteDatabase db = this.getReadableDatabase();
		try {

			String query = "SELECT UM." + KEY_MODULE_ICON_NAME + ",UM."
					+ KEY_IS_MENDATORY + ",UM." + KEY_MODULE_IS_QUESTION_MODULE
					+ ",UM." + KEY_MODULE_TYPE + ",UM."
					+ KEY_MODULE_IS_STORE_WISE + ",UM." + KEY_MODULE_CODE
					+ ",UM." + KEY_MODULE_ID + ",UM." + KEY_NAME + ",UM."
					+ KEY_MODULE_PARENT_MODULE_ID + ",UM."
					+ KEY_MODULE_SEQUENCE + ",ADM."
					+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + ",ADM."
					+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " FROM "
					+ TABLE_USER_MODULE_MASTER + " UM LEFT JOIN "
					+ TABLE_ACTIVITY_DATA_MASTER + " ADM ON UM."
					+ KEY_MODULE_CODE + "=ADM." + KEY_MODULE_CODE + " AND ADM."
					+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + "!="
					+ SyncUtils.SYNC_STATUS_SYNC_COMPLETED + " WHERE UM."
					+ KEY_MODULE_PARENT_MODULE_ID + " = " + moduleParentId
					+ " ORDER BY " + "UM." + KEY_MODULE_SEQUENCE + " ASC "
					+ ",UM." + KEY_NAME + " ASC ;";

			Cursor mCursor = db.rawQuery(query, null);
			if (mCursor.moveToFirst()) {
				do {
					Module module = new Module();

					module.setModuleID(mCursor.getInt(mCursor
							.getColumnIndex(KEY_MODULE_ID)));

					module.setModuleCode(mCursor.getInt(mCursor
							.getColumnIndex(KEY_MODULE_CODE)));

					module.setModuleType(mCursor.getInt(mCursor
							.getColumnIndex(KEY_MODULE_TYPE)));

					module.setIconName(mCursor.getString(mCursor
							.getColumnIndex(KEY_MODULE_ICON_NAME)));
					module.setIsMandatory(mCursor.getInt((mCursor
							.getColumnIndex(KEY_IS_MENDATORY))) == 1 ? true
							: false);
					module.setName(mCursor.getString(mCursor
							.getColumnIndex(KEY_NAME)));
					module.setParentModuleID(mCursor.getInt(mCursor
							.getColumnIndex(KEY_MODULE_PARENT_MODULE_ID)));
					module.setQuestionType(mCursor.getInt((mCursor
							.getColumnIndex(KEY_MODULE_IS_QUESTION_MODULE))) == 1 ? true
							: false);
					module.setSequence(mCursor.getInt(mCursor
							.getColumnIndex(KEY_MODULE_SEQUENCE)));
					module.setStoreWise(mCursor.getInt((mCursor
							.getColumnIndex(KEY_MODULE_IS_STORE_WISE))) == 1 ? true
							: false);

					int activtyID = mCursor
							.getInt((mCursor
									.getColumnIndex(KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID)));

					if (activtyID != 0) {
						module.setActivityIDAvailable(true);
					} else {
						module.setActivityIDAvailable(false);
					}
					module.setSyncStatus(mCursor.getInt(mCursor
							.getColumnIndex(KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS)));

					moduleList.add(module);
				} while (mCursor.moveToNext());
			}

		} catch (Exception e) {
		}

		return moduleList;

	}

	public ArrayList<Module> getStoreWisePendingMandatoryModule1(
			long storeID) {

		ArrayList<Module> moduleList = new ArrayList<Module>();

		SQLiteDatabase db = null;
		try {
			db = this.getReadableDatabase();

			synchronized (db) {
				String query = "SELECT  UMM.Name," + "UMM." + KEY_MODULE_ID
						+ "," + "UMM." + KEY_MODULE_CODE + "," + "UMM."
						+ KEY_MODULE_ICON_NAME + "," + "UMM."
						+ KEY_IS_MENDATORY + "," + "UMM." + KEY_NAME + ","
						+ "UMM." + KEY_MODULE_PARENT_MODULE_ID + "," + "UMM."
						+ KEY_MODULE_IS_QUESTION_MODULE + "," + "UMM."
						+ KEY_MODULE_SEQUENCE + "," + "UMM."
						+ KEY_MODULE_IS_STORE_WISE + "," + "ADM."
						+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + "," + "ADM."
						+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " FROM "
						+ TableNames.TABLE_USER_MODULE_MASTER + " UMM "
						+ "INNER JOIN " + TableNames.TABLE_STORE_MODULE_MASTER
						+ " SMM " + "ON UMM." + KEY_MODULE_ID + " = SMM."
						+ KEY_MODULE_ID + " LEFT JOIN "
						+ TableNames.TABLE_ACTIVITY_DATA_MASTER + " ADM "
						+ "ON ADM." + KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " = SMM." + KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " WHERE CASE  WHEN SMM.IsMandatory = 1 THEN CASE "
						+ "WHEN SMM." + KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " IS NULL THEN SMM." + KEY_IS_MENDATORY + " = 1  "
						+ "WHEN SMM." + KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " IS NOT NULL THEN ADM."
						+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " NOT IN ( "
						+ SyncUtils.SYNC_STATUS_SUBMIT + ","
						+ SyncUtils.SYNC_STATUS_ERROR
						+ " ) END END AND SMM.STOREID = " + storeID;

				Cursor mCursor = db.rawQuery(query, null);
				if (mCursor.moveToFirst()) {
					do {
						Module module = new Module();

						module.setModuleID(mCursor.getInt(mCursor
								.getColumnIndex(KEY_MODULE_ID)));

						module.setModuleCode(mCursor.getInt(mCursor
								.getColumnIndex(KEY_MODULE_CODE)));
						module.setIconName(mCursor.getString(mCursor
								.getColumnIndex(KEY_MODULE_ICON_NAME)));
						module.setIsMandatory(mCursor.getInt((mCursor
								.getColumnIndex(KEY_IS_MENDATORY))) == 1 ? true
								: false);
						module.setName(mCursor.getString(mCursor
								.getColumnIndex(KEY_NAME)));
						module.setParentModuleID(mCursor.getInt(mCursor
								.getColumnIndex(KEY_MODULE_PARENT_MODULE_ID)));
						module.setQuestionType(mCursor.getInt((mCursor
								.getColumnIndex(KEY_MODULE_IS_QUESTION_MODULE))) == 1 ? true
								: false);
						module.setSequence(mCursor.getInt(mCursor
								.getColumnIndex(KEY_MODULE_SEQUENCE)));
						module.setStoreWise(mCursor.getInt((mCursor
								.getColumnIndex(KEY_MODULE_IS_STORE_WISE))) == 1 ? true
								: false);

						int activtyID = mCursor
								.getInt((mCursor
										.getColumnIndex(KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID)));

						if (activtyID != 0) {
							module.setActivityIDAvailable(true);
						} else {
							module.setActivityIDAvailable(false);
						}

						module.setSyncStatus(mCursor.getInt(mCursor
								.getColumnIndex(KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS)));

						moduleList.add(module);
					} while (mCursor.moveToNext());
				}

			}
		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}
		}

		return moduleList;
	}

	public ArrayList<P1CategoryList> getProductTypeList() {

		ArrayList<P1CategoryList> prodList = new ArrayList<P1CategoryList>();

		SQLiteDatabase db = this.getReadableDatabase();

		try {
			String selectQuery = "SELECT DISTINCT "
					+ KEY_PRODUCT_MASTER_PRODUCT_TYPE_NAME + ", "
					+ KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID + ", "
					+ KEY_PRODUCT_MASTER_PRODUCT_TYPE_CODE + " FROM "
					+ TABLE_PRODUCT_MASTER;

			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					try {
						P1CategoryList prod = new P1CategoryList();
						prod.setProductTypeName(cursor.getString(cursor
								.getColumnIndex(KEY_PRODUCT_MASTER_PRODUCT_TYPE_NAME)));
						prod.setProductTypeID(String.valueOf(cursor.getInt(cursor
								.getColumnIndex(KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID))));
						prod.setProductTypeCode(cursor.getString(cursor
								.getColumnIndex(KEY_PRODUCT_MASTER_PRODUCT_TYPE_CODE)));
						prodList.add(prod);
					} catch (Exception e) {
						Helper.printLog("getP1ProductList", "" + e.getMessage());
						Helper.printStackTrace(e);
					}

				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
		} finally {
			db.close();
		}

		return prodList;
	}

	public ArrayList<Group> getGroupsDataByComProductGroupId(int moduleCode,
			int comProductGroupId, Context context) {
		ArrayList<Group> groups = new ArrayList<Group>();
		String query = "select * from " + TABLE_SURVEY_QUESTION_MASTERS
				+ " where " + KEY_MODULE_CODE + " = " + moduleCode + " AND "
				+ KEY_SURVEY_QUESTION_PRODUCT_GROUP_ID + "="
				+ comProductGroupId;
		SQLiteDatabase db = this.getReadableDatabase();
		try {

			Cursor cursor = db.rawQuery(query, null);
			if (cursor.moveToFirst()) {
				do {
					try {
						Group question = new Group();
						question.setModuleId(cursor.getInt(cursor
								.getColumnIndex(KEY_MODULE_ID)));
						// question.setModuleCode(cursor.getInt(cursor
						// .getColumnIndex(KEY_MODULE_CODE)));
						question.setQuestion(cursor.getString(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION)));
						question.setQuestionTypeId(cursor.getInt(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION_TYPE_ID)));
						question.setSequence(cursor.getInt(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION_SEQUENCE)));
						question.setSurveyQuestionId(cursor.getInt(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION_ID)));
						// question.setDependentOptionID(cursor.getInt(cursor
						// .getColumnIndex(KEY_SURVEY_QUESTION_DEPENDENT_OPTION_ID)));
						question.setProductGroupId(cursor.getInt(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION_PRODUCT_GROUP_ID)));
						question.setProductTypeId(cursor.getInt(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION_PRODUCT_TYPE_ID)));

						ArrayList<Child> childs = DatabaseHelper.getConnection(
								context).getChildList(comProductGroupId);

						question.setChilds(childs);

						groups.add(question);
					} catch (Exception e) {
						Helper.printStackTrace(e);
					}

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
		} finally {
			db.close();
		}

		return groups;

	}

	public ArrayList<Question> getQuestionsData2(int moduleID,
			int dependentOptionId, long mActivityID) {
		ArrayList<Question> questions = new ArrayList<Question>();
		ArrayList<Option> options = null;
		Question question = null;

		String query = "SELECT sq." + KEY_SURVEY_QUESTION_ID + ", " + "sq."
				+ KEY_SURVEY_QUESTION + ", " + "sq." + KEY_MODULE_CODE + ", "
				+ "sq." + KEY_MODULE_ID + ", " + "sq."
				+ KEY_SURVEY_QUESTION_SEQUENCE + ", " + "sq."
				+ KEY_SURVEY_QUESTION_TYPE_ID + ", " + "sq."
				+ KEY_SURVEY_QUESTION_TEXT_LENGTH + ", " + "sq."
				+ KEY_SURVEY_QUESTION_DEPENDENT_OPTION_ID + ", " + "sq."
				+ KEY_SURVEY_QUESTION_IMAGE + ", " + "sq."
				+ KEY_SURVEY_QUESTION_REPEATET_MAX_TIMES + ", " + "sq."
				+ KEY_SURVEY_QUESTION_REPEATER_TEXT + ", " + "sq."
				+ KEY_SURVEY_QUESTION_REPEATER_TYPE_ID + ", " + "sq."
				+ KEY_IS_MENDATORY + ", " + "so."
				+ KEY_SURVEY_QUESTION_OPTION_VALUE + ", " + "so."
				+ KEY_SURVEY_QUESTION_OPTION_IS_AFFIRMATIVE + ", " + "so."
				+ KEY_SURVEY_QUESTION_SEQUENCE + " AS SequenceOption, "
				+ "IFNULL(so." + KEY_SURVEY_QUESTION_OPTION_ID + ",0) AS "
				+ KEY_SURVEY_QUESTION_OPTION_ID + ", " + "qarm."
				+ KEY_SURVEY_QUESTION_USER_RESPONSE + " " + "FROM "
				+ TABLE_SURVEY_QUESTION_MASTERS + " sq " + "LEFT JOIN "
				+ TABLE_SURVEY_QUESTION_OPTION_MASTERS + " so " + "ON " + "sq."
				+ KEY_SURVEY_QUESTION_ID + " = so." + KEY_SURVEY_QUESTION_ID
				+ " " + "LEFT JOIN " + ""
				+ TABLE_QUESTION_ANSWER_RESPONSE_MASTER + " qarm " + "ON qarm."
				+ KEY_SURVEY_QUESTION_ID + " = sq." + KEY_SURVEY_QUESTION_ID
				+ "  AND qarm." + KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " = "
				+ mActivityID + " WHERE " + "sq." + KEY_MODULE_ID + "="
				+ moduleID + " " + "AND " + "sq."
				+ KEY_SURVEY_QUESTION_DEPENDENT_OPTION_ID + " = "
				+ dependentOptionId + " " + "ORDER BY " + "sq."
				+ KEY_SURVEY_QUESTION_SEQUENCE + " ," + "so."
				+ KEY_SURVEY_QUESTION_SEQUENCE + "," + "sq."
				+ KEY_SURVEY_QUESTION_ID + "";

		SQLiteDatabase db = this.getReadableDatabase();
		try {
			Cursor cursor = db.rawQuery(query, null);

			if (cursor.moveToFirst()) {
				do {
					// Flag to check if Survey questions has any options.
					boolean isSurveyQuestionAdded = false;
					int SurveyQuestionID = cursor.getInt(cursor
							.getColumnIndex(KEY_SURVEY_QUESTION_ID));
					for (Question ques : questions) {

						if (SurveyQuestionID == ques.getSurveyQuestionId()) {
							isSurveyQuestionAdded = true;
							break;
						} else {
							isSurveyQuestionAdded = false;
						}
					}
					if (isSurveyQuestionAdded) {
						Option option = new Option();
						option.setOptionValue(cursor.getString(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION_OPTION_VALUE)));
						option.setSequence(cursor.getInt(cursor
								.getColumnIndex("SequenceOption")));
						option.setSurveyOptionId(cursor.getInt(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION_OPTION_ID)));
						option.setIsAffirmative(cursor.getInt(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION_OPTION_IS_AFFIRMATIVE)) == 1 ? true
								: false);
						options.add(option);
						question.setOptions(options);
						// remove previous filled question model
						questions.remove(question);
						questions.add(question);

					} else {
						question = new Question();

						question.setSurveyQuestionId(cursor.getInt(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION_ID)));

						question.setQuestion(cursor.getString(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION)));

						question.setModuleCode(cursor.getInt(cursor
								.getColumnIndex(KEY_MODULE_CODE)));

						question.setModuleId(cursor.getInt(cursor
								.getColumnIndex(KEY_MODULE_ID)));

						question.setSequence(cursor.getInt(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION_SEQUENCE)));

						question.setQuestionTypeId(cursor.getInt(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION_TYPE_ID)));

						question.setTextLength(cursor.getInt(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION_TEXT_LENGTH)));

						question.setDependentOptionID(cursor.getInt(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION_DEPENDENT_OPTION_ID)));

						question.setSurveyQuestionImage(cursor.getString(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION_IMAGE)));
						question.setRepeaterText(cursor.getString(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION_REPEATER_TEXT)));

						question.setRepeaterTypeID(cursor.getInt(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION_REPEATER_TYPE_ID)));

						question.setRepeatMaxTimes(cursor.getInt(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION_REPEATET_MAX_TIMES)));
						question.setMandatory(cursor.getInt(cursor
								.getColumnIndex(KEY_IS_MENDATORY)) == 1 ? true
								: false);

						try {
							question.setUserResponse(cursor.getString(cursor
									.getColumnIndex(KEY_SURVEY_QUESTION_USER_RESPONSE)) != null ? cursor.getString(cursor
									.getColumnIndex(KEY_SURVEY_QUESTION_USER_RESPONSE))
									: "");
						} catch (Exception e1) {
							question.setUserResponse("");
						}

						int isOption = cursor.getInt(cursor
								.getColumnIndex(KEY_SURVEY_QUESTION_OPTION_ID));
						try {
							if (isOption != 0) {
								Option option = new Option();
								options = new ArrayList<Option>();
								option.setOptionValue(cursor.getString(cursor
										.getColumnIndex(KEY_SURVEY_QUESTION_OPTION_VALUE)));
								option.setSequence(cursor.getInt(cursor
										.getColumnIndex("SequenceOption")));
								option.setSurveyOptionId(cursor.getInt(cursor
										.getColumnIndex(KEY_SURVEY_QUESTION_OPTION_ID)));
								option.setIsAffirmative(cursor.getInt(cursor
										.getColumnIndex(KEY_SURVEY_QUESTION_OPTION_IS_AFFIRMATIVE)) == 1 ? true
										: false);
								options.add(option);

							}
						} catch (Exception e) {

							System.out.println(e);
						}
						questions.add(question);
					}

				} while (cursor.moveToNext());
			}
			cursor.close();

		} catch (SQLException e) {

		} finally {
			db.close();
		}
		return questions;
	}

	public synchronized void deleteMasters() {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();

			synchronized (db) {

				db.delete(TABLE_PRODUCT_MASTER, null, null);// A
				db.delete(TABLE_PAYMENT_MODE_MASTER, null, null);// B
				db.delete(TABLE_COMPETITOR_MASTER, null, null);// C
				db.delete(TABLE_COMPETETION_PRODUCT_GROUP, null, null);// D
				db.delete(TABLE_SURVEY_QUESTION_MASTERS, null, null);// F
				db.delete(TABLE_SURVEY_QUESTION_OPTION_MASTERS, null, null);// F
				db.delete(TABLE_PLANOGRAM_CLASS_MASTER, null, null);// G
				db.delete(TABLE_PLANOGRAM_PRODUCT_MASTER, null, null);// H
				db.delete(TABLE_COMPETITOR_PRODUCT_GROUP_MAPPING, null, null);// I

				db.delete(TABLE_USER_MODULE_MASTER, null, null);// MAIN MENU
																// ACTIVITY

				// FMS tables
				db.delete(TABLE_FMS_TEAM_MASTER, null, null);
				db.delete(TABLE_FEEDBACK_CATEGORY_MASTER, null, null);
				db.delete(TABLE_FEEDBACK_TYPE_MASTER, null, null);
				db.delete(TABLE_USER_FEEDBACK_MASTER, null, null);
				db.delete(TABLE_FEEDBACK_STATUS_MASTER, null, null);

				db.delete(TABLE_EOL_SCHEME_HEADER_MASTER, null, null);
				db.delete(TABLE_EOL_SCHEME_DETAILS_MASTER, null, null);

				db.delete(TABLE_STORE_BASIC_MASTER, null, null);
				db.delete(TABLE_STORE_PERFORMANCE_MASTER, null, null);

				// Race module data tables

				db.delete(TABLE_RACE_BRAND_MASTER, null, null);
				db.delete(TABLE_RACE_BRAND_PRODUCT_CATEGORY_MASTER, null, null);

				db.delete(TABLE_RACE_BRAND_CATEGORY_MAPPING_MASTER, null, null);
				db.delete(TABLE_RACE_POSM_MASTER, null, null);
				db.delete(TABLE_RACE_PRODUCT_POSM_MAPPING_MASTER, null, null);

				db.delete(TABLE_RACE_FIXTURE_MASTER, null, null);
				db.delete(TABLE_RACE_BRAND_PRODUCT_MASTER, null, null);
				db.delete(DatabaseConstants.TableNames.TABLE_USER_PROFILE,
						null, null);

				resetDownloadDataMaster1();
			}

		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}
		}
	}

	public String[] getPlanogramClassName(int compGroupId, int classRange,
			String channelType) {
		String query = "select " + KEY_PLANOGRAM_PRODUCT_MASTER_CLASS + ","
				+ KEY_PLANOGRAM_CLASS_MASTER_CLASS_ID + " from "
				+ TABLE_PLANOGRAM_CLASS_MASTER + " where "
				+ KEY_PLANOGRAM_CLASS_MASTER_COMP_PRODUCT_GROUP_ID + "="
				+ compGroupId + " AND " + classRange
				+ " BETWEEN StartRange AND EndRange AND "
				+ KEY_PLANOGRAM_PRODUCT_MASTER_CHANNEL_TYPE + "= '"
				+ channelType + "';";
		SQLiteDatabase db = this.getReadableDatabase();
		String[] classData = new String[2];
		try {
			Cursor cursor = db.rawQuery(query, null);
			cursor.moveToFirst();
			classData[0] = cursor.getString(cursor
					.getColumnIndex(KEY_PLANOGRAM_PRODUCT_MASTER_CLASS));
			classData[1] = String.valueOf(cursor.getInt(cursor
					.getColumnIndex(KEY_PLANOGRAM_CLASS_MASTER_CLASS_ID)));

		} catch (Exception exception) {
			classData[0] = "N/A";
			classData[1] = "-1";
		} finally {
			db.close();
		}

		return classData;
	}

	public ArrayList<PlanogramProductDataModal> getProductCodeList(
			String className, String channelType, int comProductGroupId) {

		SQLiteDatabase db = this.getReadableDatabase();

		ArrayList<PlanogramProductDataModal> productList = new ArrayList<PlanogramProductDataModal>();
		try {

			/*
			 * String selectQuery = "select ppm.* from " +
			 * TABLE_PLANOGRAM_PRODUCT_MASTER + " as ppm join " +
			 * TABLE_COMPETETION_PRODUCT_GROUP + " as cpg on ppm." +
			 * KEY_PLANOGRAM_PRODUCT_MASTER_COMP_PRODUCT_GROUP + "= cpg." +
			 * KEY_COMPETETION_PRODUCT_GROUP_CODE + " WHERE  ppm." +
			 * KEY_PLANOGRAM_PRODUCT_MASTER_CLASS + "='" + className +
			 * "' AND ppm." + KEY_PLANOGRAM_PRODUCT_MASTER_CHANNEL_TYPE +
			 * " LIKE '%" + channelType + "%' AND cpg." +
			 * KEY_COMPETETION_PRODUCT_GROUP_ID + "=" + comProductGroupId + ";";
			 */

			String selectQuery = "select ppm.*  from  "
					+ TABLE_PLANOGRAM_PRODUCT_MASTER + " as ppm  join "
					+ TABLE_COMPETETION_PRODUCT_GROUP + " as cpg  on ppm."
					+ KEY_PLANOGRAM_PRODUCT_MASTER_COMP_PRODUCT_GROUP
					+ " like  cpg." + KEY_COMPETETION_PRODUCT_GROUP_CODE
					+ "|| '%' " + "WHERE  ppm."
					+ KEY_PLANOGRAM_PRODUCT_MASTER_CLASS + "='" + className
					+ "' AND ppm." + KEY_PLANOGRAM_PRODUCT_MASTER_CHANNEL_TYPE
					+ " LIKE '" + channelType + "%' AND cpg."
					+ KEY_COMPETETION_PRODUCT_GROUP_ID + "="
					+ comProductGroupId + ";";

			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					try {
						PlanogramProductDataModal obj = new PlanogramProductDataModal();
						obj.setPlanogramProductMasterID(cursor.getInt(cursor
								.getColumnIndex(KEY_PLANOGRAM_PRODUCT_MASTER_ID)));

						obj.setClassName(cursor.getString(cursor
								.getColumnIndex(KEY_PLANOGRAM_PRODUCT_MASTER_CLASS)));

						obj.setChannelType(cursor.getString(cursor
								.getColumnIndex(KEY_PLANOGRAM_PRODUCT_MASTER_CHANNEL_TYPE)));

						obj.setCompProductGroup(cursor.getString(cursor
								.getColumnIndex(KEY_PLANOGRAM_PRODUCT_MASTER_COMP_PRODUCT_GROUP)));

						obj.setProductCode(cursor.getString(cursor
								.getColumnIndex(KEY_PLANOGRAM_PRODUCT_MASTER_PRODUCT_CODE)));
						productList.add(obj);

					} catch (Exception e) {
						Helper.printStackTrace(e);

					}
				} while (cursor.moveToNext());
			}
			cursor.close();

		} catch (Exception e) {

		} finally {
			db.close();
		}
		return productList;

	}

	String makePlaceholders(int len) {
		if (len < 1) {
			// It will lead to an invalid query anyway ..
			throw new RuntimeException("No placeholders");
		} else {
			StringBuilder sb = new StringBuilder(len * 2 - 1);
			sb.append("?");
			for (int i = 1; i < len; i++) {
				sb.append(",?");
			}
			return sb.toString();
		}
	}

	public String[] getFeedbackDetail(int... ids) {

		String[] values = new String[3];

		String team_query = "SELECT " + KEY_TEAM_MASTER_NAME + " FROM "
				+ TABLE_FMS_TEAM_MASTER + " where " + KEY_TEAM_MASTER_ID
				+ " = " + ids[0];

		String cat_query = "SELECT " + KEY_FEEDBACK_CATEGORY_MASTER_NAME
				+ " FROM " + TABLE_FEEDBACK_CATEGORY_MASTER + " where "
				+ KEY_FEEDBACK_CATEGORY_MASTER_ID + " = " + ids[1];

		String type_query = "SELECT " + KEY_FEEDBACK_TYPE_MASTER_NAME
				+ " FROM " + TABLE_FEEDBACK_TYPE_MASTER + " where "
				+ KEY_FEEDBACK_TYPE_MASTER_ID + " = " + ids[2];

		SQLiteDatabase db = this.getReadableDatabase();

		try {

			// / Team
			Cursor cursor = db.rawQuery(team_query, null);

			if (cursor.moveToFirst()) {

				values[0] = cursor.getString(cursor
						.getColumnIndex(KEY_TEAM_MASTER_NAME));

			}
			cursor.close();

			// Category
			cursor = db.rawQuery(cat_query, null);

			if (cursor.moveToFirst()) {

				values[1] = cursor.getString(cursor
						.getColumnIndex(KEY_FEEDBACK_CATEGORY_MASTER_NAME));

			}
			cursor.close();

			// Type
			cursor = db.rawQuery(type_query, null);

			if (cursor.moveToFirst()) {

				values[2] = cursor.getString(cursor
						.getColumnIndex(KEY_FEEDBACK_TYPE_MASTER_NAME));

			}
			cursor.close();

		} catch (Exception e) {

		} finally {
			db.close();
		}

		return values;

	}

	public HashMap<Integer, String> getFeedbackStatus() {
		SQLiteDatabase db = this.getReadableDatabase();
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		try {

			String selectQuery = "SELECT " + KEY_FEEDBACK_STATUS_MASTER_ID
					+ "," + KEY_FEEDBACK_STATUS_MASTER_NAME + " FROM "
					+ TABLE_FEEDBACK_STATUS_MASTER + ";";
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					try {
						int key = cursor.getInt(cursor
								.getColumnIndex(KEY_FEEDBACK_STATUS_MASTER_ID));
						String value = cursor
								.getString(cursor
										.getColumnIndex(KEY_FEEDBACK_STATUS_MASTER_NAME));

						map.put(key, value);

					} catch (Exception e) {
						Helper.printStackTrace(e);
					}
				} while (cursor.moveToNext());
			}
			cursor.close();

		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			db.close();
		}
		return map;

	}

	public int getActivityIdIfExist(ActivityDataMasterModel activityData) {
		SQLiteDatabase db = this.getReadableDatabase();
		int activityID = -1;
		try {

			String selectQuery = "SELECT "
					+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " FROM "
					+ TABLE_ACTIVITY_DATA_MASTER + " WHERE "
					+ KEY_BEAT_STORE_ID + " = " + activityData.getStoreID()
					+ " AND " + KEY_MODULE_CODE + " = "
					+ activityData.getModuleCode() + " AND "
					+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " <> "
					+ SyncUtils.SYNC_STATUS_SYNC_COMPLETED;

			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				try {

					activityID = cursor
							.getInt(cursor
									.getColumnIndex(KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID));

				} catch (Exception e) {
					Helper.printStackTrace(e);
				}
			}
			cursor.close();

		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			db.close();
		}

		return activityID;
	}

	public int getActivityIdIfExistForEOL(ActivityDataMasterModel activityData) {
		SQLiteDatabase db = this.getReadableDatabase();
		int activityID = -1;
		try {

			String selectQuery = "SELECT "
					+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " FROM "
					+ TABLE_ACTIVITY_DATA_MASTER + " WHERE "
					+ KEY_BEAT_STORE_ID + " = " + activityData.getStoreID()
					+ " AND " + KEY_MODULE_CODE + " = "
					+ activityData.getModuleCode();

			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				try {

					activityID = cursor
							.getInt(cursor
									.getColumnIndex(KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID));

				} catch (Exception e) {
					Helper.printStackTrace(e);
				}
			}
			cursor.close();

		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			db.close();
		}

		return activityID;
	}

	public synchronized ArrayList<ActivityDataMasterModel> getActivityDataForSync() {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "select adm." + KEY_USER_ID + ", adm."
				+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + ", adm."
				+ KEY_BEAT_STORE_ID + ", adm." + KEY_MODULE_CODE + ", adm."
				+ KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID + ", adm."
				+ KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID + ", adm."
				+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " ,um. "
				+ KEY_MODULE_IS_QUESTION_MODULE + " ,um." + KEY_NAME + ",obm."
				+ KEY_BEAT_STORE_NAME + " FROM " + TABLE_ACTIVITY_DATA_MASTER
				+ " adm LEFT JOIN " + TABLE_USER_MODULE_MASTER + " um ON um."
				+ KEY_MODULE_CODE + " = adm." + KEY_MODULE_CODE + " LEFT JOIN "
				+ TABLE_STORE_BASIC_MASTER + " obm ON obm." + KEY_BEAT_STORE_ID
				+ " = adm." + KEY_BEAT_STORE_ID + " WHERE adm."
				+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " IN ("
				+ SyncUtils.SYNC_STATUS_SUBMIT + ","
				+ SyncUtils.SYNC_STATUS_ERROR + ")";

		ArrayList<ActivityDataMasterModel> activityDataMasterModels = new ArrayList<ActivityDataMasterModel>();

		try {
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					try {
						ActivityDataMasterModel modal = new ActivityDataMasterModel();

						modal.setUserID(cursor.getInt(cursor
								.getColumnIndex(KEY_USER_ID)));
						modal.setActivityID(cursor.getInt(cursor
								.getColumnIndex(KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID)));
						modal.setStoreID(cursor.getInt(cursor
								.getColumnIndex(KEY_BEAT_STORE_ID)));
						modal.setStoreName(cursor.getString(cursor
								.getColumnIndex(KEY_BEAT_STORE_NAME)));
						modal.setModuleCode(cursor.getInt(cursor
								.getColumnIndex(KEY_MODULE_CODE)));
						modal.setModuleName(cursor.getString(cursor
								.getColumnIndex(KEY_NAME)) != null ? cursor
								.getString(cursor.getColumnIndex(KEY_NAME))
								: "");
						modal.setCoverageID(cursor.getInt(cursor
								.getColumnIndex(KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID)));
						modal.setSyncStatus(cursor.getInt(cursor
								.getColumnIndex(KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS)));
						modal.setSurveyResponseID(cursor.getInt(cursor
								.getColumnIndex(KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID)));
						modal.setQuestionModule(cursor.getInt(cursor
								.getColumnIndex(KEY_MODULE_IS_QUESTION_MODULE)) == 1 ? true
								: false);

						activityDataMasterModels.add(modal);
					} catch (Exception e) {
						Helper.printStackTrace(e);
					}
				} while (cursor.moveToNext());
			}
			cursor.close();

		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			db.close();
		}

		return activityDataMasterModels;
	}

	public synchronized long insertActivtyDataMaster(
			ActivityDataMasterModel activityData) {

		SQLiteDatabase db = null;
		long activityID = -1;
		try {
			db = this.getWritableDatabase();
			synchronized (db) {

				ContentValues initialValues = new ContentValues();

				initialValues.put(KEY_USER_ID, activityData.getUserID());
				initialValues.put(KEY_USER_FEEDBACK_MASTER_STORE_ID,
						activityData.getStoreID());
				initialValues
						.put(KEY_MODULE_CODE, activityData.getModuleCode());
				initialValues.put(KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID,
						activityData.getCoverageID());
				initialValues.put(KEY_ACTIVITY_DATA_MASTER_CREATED_DATE,
						activityData.getCreatedDate());
				initialValues.put(KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE,
						activityData.getModifiedDate());
				initialValues.put(KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS,
						activityData.getSyncStatus());
				initialValues.put(KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID,
						activityData.getSurveyResponseID());
				activityID = db.insert(TABLE_ACTIVITY_DATA_MASTER, null,
						initialValues);

			}
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}

		return activityID;

	}

	public synchronized void updateActivtyDataMaster(long activityID,
			int syncStatus) {

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();

			synchronized (db) {
				ContentValues initialValues = new ContentValues();

				initialValues.put(KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS,
						syncStatus);

				String whereClause = KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ "=?";
				String[] whereArgu = new String[] { String.valueOf(activityID) };
				db.update(TABLE_ACTIVITY_DATA_MASTER, initialValues,
						whereClause, whereArgu);

			}
		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}
		}

	}

	public synchronized void insertQuestionAnswerResponse(long activityID,
			JSONArray rootJson) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();

			synchronized (db) {

				// *************************
				// first delete data
				String whereClause = KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ "=?";
				String[] whereArgs = new String[] { String.valueOf(activityID) };

				db.delete(TABLE_QUESTION_ANSWER_RESPONSE_MASTER, whereClause,
						whereArgs);

				ContentValues initialValues;

				int count = rootJson.length();

				for (int i = 0; i < count; i++) {
					try {
						JSONObject itemJson = rootJson.getJSONObject(i);

						initialValues = new ContentValues();

						initialValues.put(KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
								activityID);

						int surveyQuestionID = itemJson
								.getInt("SurveyQuestionID");
						initialValues.put(KEY_SURVEY_QUESTION_ID,
								surveyQuestionID);
						if (surveyQuestionID == -1) {
							initialValues.put(KEY_SURVEY_QUESTION_SUB_ID,
									itemJson.getString("SurveyQuestionSubID"));
						}

						initialValues.put(KEY_SURVEY_QUESTION_USER_RESPONSE,
								itemJson.getString("UserResponse"));
						initialValues.put(KEY_SURVEY_QUESTION_TYPE_ID,
								itemJson.getInt("QuestionTypeID"));

						db.insert(TABLE_QUESTION_ANSWER_RESPONSE_MASTER, null,
								initialValues);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}
		}

	}

	public boolean deleteDataFromCollectionResponseAndActivityDataMasterTable(
			long activityID) {

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();

			synchronized (db) {

				// *************************
				// delete data from Order Table
				String whereClause = KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ "=?";
				String[] whereArgs = new String[] { String.valueOf(activityID) };

				int rowEffectedInCollectionTable = db.delete(
						TABLE_COLLECTION_RESPONSE_MASTER, whereClause,
						whereArgs);

				int rowEffectedInActivtyDataTable = deleteActvityDataMaster(
						activityID, db);

				if (rowEffectedInCollectionTable != 0
						&& rowEffectedInActivtyDataTable != 0) {
					return true;
				}

				return false;

			}
		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}
		}

		return false;

	}

	private int deleteActvityDataMaster(long activityID, SQLiteDatabase db) {
		// delete data from activity data Table
		String dataTableWhereClause = KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
				+ "=?";
		String[] dataTableWhereArgs = new String[] { String.valueOf(activityID) };

		int rowEffectedInActivtyDataTable = db.delete(
				TABLE_ACTIVITY_DATA_MASTER, dataTableWhereClause,
				dataTableWhereArgs);
		return rowEffectedInActivtyDataTable;
	}

	public HashMap<String, JSONArray> getQuestionAnswerResponseForSync(
			long activityID) {

		String query = "SELECT ad."
				+ KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID + ",qar."
				+ KEY_SURVEY_QUESTION_ID + ",qar."
				+ KEY_SURVEY_QUESTION_USER_RESPONSE + " ,qar."
				+ KEY_SURVEY_QUESTION_SUB_ID + ", qar."
				+ KEY_SURVEY_QUESTION_TYPE_ID + " FROM "
				+ TABLE_QUESTION_ANSWER_RESPONSE_MASTER + " qar inner join "
				+ TABLE_ACTIVITY_DATA_MASTER + " ad on qar."
				+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + "=ad."
				+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " WHERE ad."
				+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + "=" + activityID;
		JSONArray questionJsonArrayText = new JSONArray();
		JSONArray questionJsonArrayImage = new JSONArray();

		SQLiteDatabase db = this.getReadableDatabase();
		try {
			Cursor cursor = db.rawQuery(query, null);
			if (cursor.moveToFirst()) {
				do {
					JSONObject questionJsonObject = new JSONObject();
					questionJsonObject
							.put("SurveyResponseID",
									cursor.getInt(cursor
											.getColumnIndex(KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID)));
					questionJsonObject.put("SurveyQuestionID", cursor
							.getInt(cursor
									.getColumnIndex(KEY_SURVEY_QUESTION_ID)));

					questionJsonObject
							.put("SurveyQuestionSubID",
									cursor.getString(cursor
											.getColumnIndex(KEY_SURVEY_QUESTION_SUB_ID)));
					questionJsonObject
							.put("UserResponse",
									cursor.getString(cursor
											.getColumnIndex(KEY_SURVEY_QUESTION_USER_RESPONSE)));
					questionJsonObject
							.put("QuestionTypeID",
									cursor.getInt(cursor
											.getColumnIndex(KEY_SURVEY_QUESTION_TYPE_ID)));

					if (cursor.getInt(cursor
							.getColumnIndex(KEY_SURVEY_QUESTION_TYPE_ID)) == QuestionType.SURVEY_QUESTION_TYPE_PICTUREBOX) {
						questionJsonArrayImage.put(questionJsonObject);
					} else {
						questionJsonArrayText.put(questionJsonObject);
					}

				}

				while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception e) {

		} finally {
			db.close();
		}
		HashMap<String, JSONArray> map = new HashMap<String, JSONArray>();
		map.put("QUESTION_RESPONSE_TEXT", questionJsonArrayText);
		map.put("QUESTION_RESPONSE_IMAGE", questionJsonArrayImage);

		return map;
	}

	public boolean clearActivityDataAndAllResponceTableData() {

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();

			synchronized (db) {

				String[] valuesClouse = new String[] { String
						.valueOf(ModuleCode.MENU_OD_EOL) };

				db.delete(TABLE_ACTIVITY_DATA_MASTER, KEY_MODULE_CODE + "<>?",
						valuesClouse);
				db.delete(TABLE_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE_MASTER,
						"1", null);
				db.delete(TABLE_QUESTION_ANSWER_RESPONSE_MASTER, "1", null);
				db.delete(TABLE_ORDER_RESPONSE_MASTER, "1", null);

				db.delete(TABLE_COLLECTION_RESPONSE_MASTER, "1", null);

				db.delete(TABLE_PLANOGRAM_RESPONSE_MASTER, "1", null);
				db.delete(TABLE_PLANOGRAM_PRODUCT_RESPONSE_MASTER, "1", null);
				db.delete(TABLE_PLANOGRAM_COMPITIOR_RESPONSE_MASTER, "1", null);

			}
		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}
		}

		return true;

	}

	public synchronized boolean insertPlanogramResponse(long activityID,
			JSONArray rootJsonArray) {
		boolean flag = false;

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();

			synchronized (db) {

				// *************************
				// first delete data
				String whereClause = KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ "=?";
				String[] whereArgs = new String[] { String.valueOf(activityID) };

				int rowEffected = db.delete(TABLE_PLANOGRAM_RESPONSE_MASTER,
						whereClause, whereArgs);
				int rowEffectedProduct = db.delete(
						TABLE_PLANOGRAM_PRODUCT_RESPONSE_MASTER, whereClause,
						whereArgs);
				int rowEffectedComptitor = db.delete(
						TABLE_PLANOGRAM_COMPITIOR_RESPONSE_MASTER, whereClause,
						whereArgs);

				System.out.println("rowEffected" + rowEffected);
				System.out.println("rowEffected Product" + rowEffectedProduct);
				System.out.println("rowEffected Compitior"
						+ rowEffectedComptitor);

				ContentValues initialValues;
				ContentValues initialValuesProduct;
				ContentValues initialValuesCompitior;

				int countProduct = rootJsonArray.length();

				for (int i = 0; i < countProduct; i++) {
					JSONObject rootJsonItem = rootJsonArray.getJSONObject(i);
					if (rootJsonItem != null && rootJsonItem.length() > 0) {

						initialValues = new ContentValues();

						initialValues.put(KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
								activityID);

						initialValues.put(KEY_COMPETETION_PRODUCT_GROUP_ID,
								rootJsonItem.getInt("CompProductGroupID"));
						initialValues.put(KEY_PLANOGRAM_PRODUCT_MASTER_CLASS,
								rootJsonItem.getString("Class"));
						initialValues.put(
								KEY_PLANOGRAM_RESPONSE_MASTER_ADHERENCE,
								rootJsonItem.getDouble("Adherence"));
						initialValues.put(KEY_PLANOGRAM_CLASS_MASTER_CLASS_ID,
								rootJsonItem.getInt("ClassID"));

						long planResponseId = db.insert(
								TABLE_PLANOGRAM_RESPONSE_MASTER, null,
								initialValues);

						if (rootJsonItem.has("PlanogramProductResponses")) {
							JSONArray planogramProductResponses = rootJsonItem
									.getJSONArray("PlanogramProductResponses");

							if (planogramProductResponses != null) {
								int countPlanogramProductResponses = planogramProductResponses
										.length();

								for (int j = 0; j < countPlanogramProductResponses; j++) {
									initialValuesProduct = new ContentValues();
									JSONObject jsonObjectItemProduct = planogramProductResponses
											.getJSONObject(j);
									initialValuesProduct
											.put(KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
													activityID);
									initialValuesProduct
											.put(KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID,
													planResponseId);
									initialValuesProduct
											.put(KEY_PLANOGRAM_PRODUCT_MASTER_PRODUCT_CODE,
													jsonObjectItemProduct
															.getString("ProductCode"));
									initialValuesProduct
											.put(KEY_PLANOGRAM_PRODUCT_RESPONSE_MASTER_IS_AVAILABLE,
													jsonObjectItemProduct
															.getBoolean("IsAvailable") ? 1
															: 0);
									db.insert(
											TABLE_PLANOGRAM_PRODUCT_RESPONSE_MASTER,
											null, initialValuesProduct);
								}
							}

						}

						if (rootJsonItem.has("PlanogramCompititorResponses")) {
							JSONArray planogramCompititorResponses = rootJsonItem
									.getJSONArray("PlanogramCompititorResponses");

							if (planogramCompititorResponses != null) {
								int countPlanogramCompititorResponses = planogramCompititorResponses
										.length();

								for (int j = 0; j < countPlanogramCompititorResponses; j++) {
									initialValuesCompitior = new ContentValues();
									JSONObject jsonObjectItemCompititor = planogramCompititorResponses
											.getJSONObject(j);

									initialValuesCompitior
											.put(KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
													activityID);
									initialValuesCompitior
											.put(KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID,
													planResponseId);
									initialValuesCompitior.put(
											KEY_COMETITOR_ID,
											jsonObjectItemCompititor
													.getInt("CompetitorID"));
									initialValuesCompitior
											.put(KEY_PLANOGRAM_COMPITITORS_RESPONSE_MASTER_PLAN_VALUE,
													jsonObjectItemCompititor
															.getInt("Value"));
									db.insert(
											TABLE_PLANOGRAM_COMPITIOR_RESPONSE_MASTER,
											null, initialValuesCompitior);
								}
							}

						}

					}

				}
				flag = true;

			}
		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}
		}

		return flag;
	}

	public synchronized JSONArray getPlanogramResponse(long mActivityID) {

		SQLiteDatabase db = this.getReadableDatabase();

		String queryPlanogramResponse = "SELECT "
				+ KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID + ","
				+ KEY_COMPETETION_PRODUCT_GROUP_ID + ","
				+ KEY_PLANOGRAM_PRODUCT_MASTER_CLASS + ","
				+ KEY_PLANOGRAM_CLASS_MASTER_CLASS_ID + ","
				+ KEY_PLANOGRAM_RESPONSE_MASTER_ADHERENCE + " FROM "
				+ TABLE_PLANOGRAM_RESPONSE_MASTER + " WHERE "
				+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " = " + mActivityID;

		JSONArray rootJSONArray = new JSONArray();
		try {
			Cursor cursorRoot = db.rawQuery(queryPlanogramResponse, null);

			if (cursorRoot.moveToFirst()) {
				do {
					JSONObject rootJsonObject = new JSONObject();

					int planResponseID = cursorRoot
							.getInt(cursorRoot
									.getColumnIndex(KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID));

					rootJsonObject
							.put("CompProductGroupID",
									cursorRoot.getInt(cursorRoot
											.getColumnIndex(KEY_COMPETETION_PRODUCT_GROUP_ID)));
					rootJsonObject
							.put("Class",
									cursorRoot.getString(cursorRoot
											.getColumnIndex(KEY_PLANOGRAM_PRODUCT_MASTER_CLASS)));
					rootJsonObject
							.put("ClassID",
									cursorRoot.getInt(cursorRoot
											.getColumnIndex(KEY_PLANOGRAM_CLASS_MASTER_CLASS_ID)));

					rootJsonObject
							.put("Adherence",
									cursorRoot.getDouble(cursorRoot
											.getColumnIndex(KEY_PLANOGRAM_RESPONSE_MASTER_ADHERENCE)));

					String queryProductResponse = "SELECT  "
							+ KEY_PLANOGRAM_PRODUCT_MASTER_PRODUCT_CODE
							+ ","
							+ KEY_PLANOGRAM_PRODUCT_RESPONSE_MASTER_IS_AVAILABLE
							+ " FROM "
							+ TABLE_PLANOGRAM_PRODUCT_RESPONSE_MASTER
							+ " WHERE "
							+ KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID
							+ "=" + planResponseID;

					try {

						Cursor cursorProduct = db.rawQuery(
								queryProductResponse, null);
						JSONArray jsonArrayProduct = new JSONArray();

						if (cursorProduct.moveToFirst()) {

							do {

								JSONObject jsonObjectProduct = new JSONObject();
								jsonObjectProduct
										.put("ProductCode",
												cursorProduct
														.getString(cursorProduct
																.getColumnIndex(KEY_PLANOGRAM_PRODUCT_MASTER_PRODUCT_CODE)));
								jsonObjectProduct
										.put("IsAvailable",
												cursorProduct.getInt(cursorProduct
														.getColumnIndex(KEY_PLANOGRAM_PRODUCT_RESPONSE_MASTER_IS_AVAILABLE)) == 1 ? true
														: false);
								jsonArrayProduct.put(jsonObjectProduct);

							} while (cursorProduct.moveToNext());

							cursorProduct.close();
							rootJsonObject.put("PlanogramProductResponses",
									jsonArrayProduct);
						}

					} catch (Exception e) {
					}

					String cursorCompitior = "SELECT "
							+ KEY_COMETITOR_ID
							+ ","
							+ KEY_PLANOGRAM_COMPITITORS_RESPONSE_MASTER_PLAN_VALUE
							+ " FROM "
							+ TABLE_PLANOGRAM_COMPITIOR_RESPONSE_MASTER
							+ " WHERE "
							+ KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID
							+ "=" + planResponseID;

					try {

						Cursor cursorComptitior = db.rawQuery(cursorCompitior,
								null);
						JSONArray jsonArray = new JSONArray();

						if (cursorComptitior.moveToFirst()) {

							do {
								JSONObject jsonObject = new JSONObject();
								jsonObject
										.put("CompetitorID",
												cursorComptitior
														.getInt(cursorComptitior
																.getColumnIndex(KEY_COMETITOR_ID)));
								jsonObject
										.put("Value",
												cursorComptitior
														.getInt(cursorComptitior
																.getColumnIndex(KEY_PLANOGRAM_COMPITITORS_RESPONSE_MASTER_PLAN_VALUE)));

								jsonArray.put(jsonObject);

							} while (cursorComptitior.moveToNext());

							cursorComptitior.close();

							rootJsonObject.put("PlanogramCompititorResponses",
									jsonArray);
						}

					} catch (Exception exception) {

					}
					rootJSONArray.put(rootJsonObject);
				}

				while (cursorRoot.moveToNext());
			}
			cursorRoot.close();

		} catch (Exception e) {
		} finally {
			db.close();
		}
		return rootJSONArray;
	}

	public synchronized JSONArray getPlanogramResponseIsAvilable(
			long mActivityID) {

		SQLiteDatabase db = this.getReadableDatabase();

		/*
		 * String queryPlanogramResponse = "SELECT " +
		 * KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID + "," +
		 * KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID + "," +
		 * KEY_COMPETETION_PRODUCT_GROUP_ID + "," +
		 * KEY_PLANOGRAM_PRODUCT_MASTER_CLASS + "," +
		 * KEY_PLANOGRAM_CLASS_MASTER_CLASS_ID + "," +
		 * KEY_PLANOGRAM_RESPONSE_MASTER_ADHERENCE + " FROM " +
		 * TABLE_PLANOGRAM_RESPONSE_MASTER + " WHERE " +
		 * KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " = " + mActivityID;
		 */

		String queryPlanogramResponse = "SELECT prm."
				+ KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID + ",ad."
				+ KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID + ",prm."
				+ KEY_COMPETETION_PRODUCT_GROUP_ID + ",prm."
				+ KEY_PLANOGRAM_PRODUCT_MASTER_CLASS + ",prm."
				+ KEY_PLANOGRAM_CLASS_MASTER_CLASS_ID + ",prm."
				+ KEY_PLANOGRAM_RESPONSE_MASTER_ADHERENCE + " FROM "
				+ TABLE_PLANOGRAM_RESPONSE_MASTER + " prm INNER JOIN "
				+ TABLE_ACTIVITY_DATA_MASTER + " ad ON ad."
				+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " = prm."
				+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " WHERE ad."
				+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " = " + mActivityID;

		JSONArray rootJSONArray = new JSONArray();
		try {
			Cursor cursorRoot = db.rawQuery(queryPlanogramResponse, null);

			if (cursorRoot.moveToFirst()) {
				do {
					JSONObject rootJsonObject = new JSONObject();

					int planResponseID = cursorRoot
							.getInt(cursorRoot
									.getColumnIndex(KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID));
					rootJsonObject
							.put("SurveyResponseID",
									cursorRoot.getInt(cursorRoot
											.getColumnIndex(KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID)));

					rootJsonObject
							.put("CompProductGroupID",
									cursorRoot.getInt(cursorRoot
											.getColumnIndex(KEY_COMPETETION_PRODUCT_GROUP_ID)));
					rootJsonObject
							.put("Class",
									cursorRoot.getString(cursorRoot
											.getColumnIndex(KEY_PLANOGRAM_PRODUCT_MASTER_CLASS)));
					rootJsonObject
							.put("ClassID",
									cursorRoot.getInt(cursorRoot
											.getColumnIndex(KEY_PLANOGRAM_CLASS_MASTER_CLASS_ID)));

					rootJsonObject
							.put("Adherence",
									cursorRoot.getDouble(cursorRoot
											.getColumnIndex(KEY_PLANOGRAM_RESPONSE_MASTER_ADHERENCE)));

					String queryProductResponse = "SELECT  "
							+ KEY_PLANOGRAM_PRODUCT_MASTER_PRODUCT_CODE
							+ ","
							+ KEY_PLANOGRAM_PRODUCT_RESPONSE_MASTER_IS_AVAILABLE
							+ " FROM "
							+ TABLE_PLANOGRAM_PRODUCT_RESPONSE_MASTER
							+ " WHERE "
							+ KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID
							+ "="
							+ planResponseID
							+ " AND "
							+ KEY_PLANOGRAM_PRODUCT_RESPONSE_MASTER_IS_AVAILABLE
							+ " = 1;";

					try {

						Cursor cursorProduct = db.rawQuery(
								queryProductResponse, null);
						JSONArray jsonArrayProduct = new JSONArray();

						if (cursorProduct.moveToFirst()) {

							do {

								JSONObject jsonObjectProduct = new JSONObject();
								jsonObjectProduct
										.put("ProductCode",
												cursorProduct
														.getString(cursorProduct
																.getColumnIndex(KEY_PLANOGRAM_PRODUCT_MASTER_PRODUCT_CODE)));
								jsonObjectProduct
										.put("IsAvailable",
												cursorProduct.getInt(cursorProduct
														.getColumnIndex(KEY_PLANOGRAM_PRODUCT_RESPONSE_MASTER_IS_AVAILABLE)) == 1 ? true
														: false);
								jsonArrayProduct.put(jsonObjectProduct);

							} while (cursorProduct.moveToNext());

							cursorProduct.close();
							rootJsonObject.put("PlanogramProductResponses",
									jsonArrayProduct);
						}

					} catch (Exception e) {
					}

					String cursorCompitior = "SELECT "
							+ KEY_COMETITOR_ID
							+ ","
							+ KEY_PLANOGRAM_COMPITITORS_RESPONSE_MASTER_PLAN_VALUE
							+ " FROM "
							+ TABLE_PLANOGRAM_COMPITIOR_RESPONSE_MASTER
							+ " WHERE "
							+ KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID
							+ "=" + planResponseID;

					try {

						Cursor cursorComptitior = db.rawQuery(cursorCompitior,
								null);
						JSONArray jsonArray = new JSONArray();

						if (cursorComptitior.moveToFirst()) {

							do {
								JSONObject jsonObject = new JSONObject();
								jsonObject
										.put("CompetitorID",
												cursorComptitior
														.getInt(cursorComptitior
																.getColumnIndex(KEY_COMETITOR_ID)));
								jsonObject
										.put("Value",
												cursorComptitior
														.getInt(cursorComptitior
																.getColumnIndex(KEY_PLANOGRAM_COMPITITORS_RESPONSE_MASTER_PLAN_VALUE)));

								jsonArray.put(jsonObject);

							} while (cursorComptitior.moveToNext());

							cursorComptitior.close();

							rootJsonObject.put("PlanogramCompititorResponses",
									jsonArray);
						}

					} catch (Exception exception) {

					}
					rootJSONArray.put(rootJsonObject);
				}

				while (cursorRoot.moveToNext());
			}
			cursorRoot.close();

		} catch (Exception e) {
		} finally {
			db.close();
		}
		return rootJSONArray;
	}

	public synchronized void insertEOLScheemData(String json)
			throws JSONException {

		ContentValues initialValues;
		ContentValues initialValuesDetails;

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();

			synchronized (db) {

				if (json != null) {

					JSONObject jsonObject = new JSONObject(json);

					initialValues = new ContentValues();

					initialValues.put(KEY_EOL_SCHEME_ID,
							jsonObject.getInt("SchemeID"));
					initialValues.put(KEY_EOL_SCHEME_NUMBER,
							jsonObject.getString("SchemeNumber"));
					initialValues.put(KEY_EOL_SCHEME_FROM,
							jsonObject.getString("strSchemeFrom"));
					initialValues.put(KEY_EOL_SCHEME_TO,
							jsonObject.getString("strSchemeTo"));
					initialValues.put(KEY_EOL_SCHEME_FROM,
							jsonObject.getString("strSchemeFrom"));
					initialValues.put(KEY_EOL_ORDER_FROM,
							jsonObject.getString("strOrderFrom"));
					initialValues.put(KEY_EOL_ORDER_TO,
							jsonObject.getString("strOrderTo"));
					initialValues.put(KEY_EOL_PUMI_NUMBER,
							jsonObject.getString("PUMINumber"));
					initialValues.put(KEY_EOL_PUMI_DATE,
							jsonObject.getString("strPUMIDate"));
					initialValues.put(KEY_EOL_PRODUCT_TYPE,
							jsonObject.getString("ProductType"));
					initialValues.put(KEY_EOL_PRODUCT_GROUP,
							jsonObject.getString("strOrderTo"));
					initialValues.put(KEY_PRODUCT_CATEGORY,
							jsonObject.getString("ProductCategory"));
					initialValues.put(KEY_EOL_PRODUCT_GROUP,
							jsonObject.getString("ProductGroup"));
					initialValues.put(KEY_ACTIVITY_DATA_MASTER_CREATED_DATE,
							jsonObject.getString("strCreatedDate"));
					initialValues.put(KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE,
							jsonObject.getString("strModifiedDate"));

					JSONArray jsonDetailsObjectArray = jsonObject
							.getJSONArray("EOLSchemeDetails");
					if (jsonDetailsObjectArray != null) {
						int countDetails = jsonDetailsObjectArray.length();
						for (int j = 0; j < countDetails; j++) {
							try {
								JSONObject jsonObjectDetails = jsonDetailsObjectArray
										.getJSONObject(j);

								initialValuesDetails = new ContentValues();

								initialValuesDetails.put(KEY_EOL_SCHEME_ID,
										jsonObjectDetails.getInt("SchemeID"));
								initialValuesDetails.put(
										KEY_EOL_DETAILS_BASIC_MODEL_CODE,
										jsonObjectDetails
												.getString("BasicModelCode"));
								initialValuesDetails.put(
										KEY_EOL_DETAILS_ORDER_QUATITY,
										jsonObjectDetails.getInt("Quantity"));
								initialValuesDetails.put(
										KEY_EOL_DETAILS_ACTUAL_SUPPORT,
										jsonObjectDetails.getInt("Support"));

								db.insert(TABLE_EOL_SCHEME_DETAILS_MASTER,
										null, initialValuesDetails);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}

					db.insert(TABLE_EOL_SCHEME_HEADER_MASTER, null,
							initialValues);

				}

			}
		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}
		}

	}

	public synchronized EOLSchemeDTO getScheemData(int schemeID) {

		SQLiteDatabase db = this.getReadableDatabase();

		String queryScheme = "SELECT " + KEY_EOL_SCHEME_ID + ","
				+ KEY_EOL_SCHEME_NUMBER + "," + KEY_EOL_SCHEME_FROM + ","
				+ KEY_EOL_SCHEME_TO + "," + KEY_EOL_ORDER_FROM + ","
				+ KEY_EOL_ORDER_TO + "," + KEY_EOL_PUMI_NUMBER + ","
				+ KEY_EOL_PUMI_DATE + "," + KEY_EOL_PRODUCT_TYPE + ","
				+ KEY_EOL_PRODUCT_GROUP + "," + KEY_PRODUCT_CATEGORY + ","
				+ KEY_ACTIVITY_DATA_MASTER_CREATED_DATE + ","
				+ KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE + " FROM "
				+ TABLE_EOL_SCHEME_HEADER_MASTER + " WHERE "
				+ KEY_EOL_SCHEME_ID + "=" + schemeID + ";";

		Cursor cursorRoot = db.rawQuery(queryScheme, null);
		EOLSchemeDTO rootJsonObject = null;
		if (cursorRoot.moveToFirst()) {
			rootJsonObject = new EOLSchemeDTO();

			int scheemID = cursorRoot.getInt(cursorRoot
					.getColumnIndex(KEY_EOL_SCHEME_ID));

			rootJsonObject.setSchemeID(scheemID);
			rootJsonObject.setSchemeNumber(cursorRoot.getString(cursorRoot
					.getColumnIndex(KEY_EOL_SCHEME_NUMBER)));
			rootJsonObject.setSchemeFrom(cursorRoot.getString(cursorRoot
					.getColumnIndex(KEY_EOL_SCHEME_FROM)));
			rootJsonObject.setSchemeTo(cursorRoot.getString(cursorRoot
					.getColumnIndex(KEY_EOL_SCHEME_TO)));
			rootJsonObject.setOrderFrom(cursorRoot.getString(cursorRoot
					.getColumnIndex(KEY_EOL_ORDER_FROM)));
			rootJsonObject.setOrderTo(cursorRoot.getString(cursorRoot
					.getColumnIndex(KEY_EOL_ORDER_TO)));
			rootJsonObject.setPUMINumber(cursorRoot.getString(cursorRoot
					.getColumnIndex(KEY_EOL_PUMI_NUMBER)));
			rootJsonObject.setPumiDate(cursorRoot.getString(cursorRoot
					.getColumnIndex(KEY_EOL_PUMI_DATE)));
			rootJsonObject.setProductType(cursorRoot.getString(cursorRoot
					.getColumnIndex(KEY_EOL_PRODUCT_TYPE)));
			rootJsonObject.setProductGroup(cursorRoot.getString(cursorRoot
					.getColumnIndex(KEY_EOL_PRODUCT_GROUP)));
			rootJsonObject.setProductCategory(cursorRoot.getString(cursorRoot
					.getColumnIndex(KEY_PRODUCT_CATEGORY)));
			rootJsonObject.setCreatedDate(cursorRoot.getString(cursorRoot
					.getColumnIndex(KEY_ACTIVITY_DATA_MASTER_CREATED_DATE)));
			rootJsonObject.setModifiedDate(cursorRoot.getString(cursorRoot
					.getColumnIndex(KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE)));

			String querySchemeDetails = "SELECT "
			/* + KEY_EOL_DETAILS_SCHEME_DETAILS_ID + "," */
			+ KEY_EOL_SCHEME_ID + "," + KEY_EOL_DETAILS_BASIC_MODEL_CODE + ","
					+ KEY_EOL_DETAILS_ORDER_QUATITY + ","
					+ KEY_EOL_DETAILS_ACTUAL_SUPPORT + "  FROM "
					+ TABLE_EOL_SCHEME_DETAILS_MASTER + " WHERE "
					+ KEY_EOL_SCHEME_ID + "=" + schemeID;

			try {

				Cursor cursorSchemeDetails = db.rawQuery(querySchemeDetails,
						null);

				ArrayList<EOLSchemeDetailDTO> detailDTOs = new ArrayList<EOLSchemeDetailDTO>();

				if (cursorSchemeDetails.moveToFirst()) {

					do {

						EOLSchemeDetailDTO detailDTO = new EOLSchemeDetailDTO();
						/*
						 * detailDTO
						 * .setSchemeDetailsID(cursorSchemeDetails.getInt
						 * (cursorSchemeDetails
						 * .getColumnIndex(KEY_EOL_DETAILS_SCHEME_DETAILS_ID)));
						 */detailDTO.setSchemeID(cursorSchemeDetails
								.getInt(cursorSchemeDetails
										.getColumnIndex(KEY_EOL_SCHEME_ID)));
						detailDTO
								.setBasicModelCode(cursorSchemeDetails.getString(cursorSchemeDetails
										.getColumnIndex(KEY_EOL_DETAILS_BASIC_MODEL_CODE)));
						detailDTO
								.setQuatity(cursorSchemeDetails.getInt(cursorSchemeDetails
										.getColumnIndex(KEY_EOL_DETAILS_ORDER_QUATITY)));
						detailDTO
								.setSupport(cursorSchemeDetails.getInt(cursorSchemeDetails
										.getColumnIndex(KEY_EOL_DETAILS_ACTUAL_SUPPORT)));

						detailDTOs.add(detailDTO);

					} while (cursorSchemeDetails.moveToNext());
					cursorSchemeDetails.close();
					rootJsonObject.setScheemDetails(detailDTOs);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return rootJsonObject;
	}

	public synchronized boolean deleteSchemes(JSONArray arr,
			boolean shouldOrderClear) {

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();

			synchronized (db) {

				String[] whereArgs = new String[arr.length()];

				StringBuilder whereClause = new StringBuilder();

				int length = arr.length();

				for (int i = 0; i < length; i++) {
					whereArgs[i] = arr.getString(i);
					whereClause.append("OR " + KEY_EOL_SCHEME_ID + "= ? ");
				}

				String whereClause1 = whereClause.toString().substring(3);

				int noOfSchemeDetailDeleted = db.delete(
						TABLE_EOL_SCHEME_DETAILS_MASTER, whereClause1,
						whereArgs);

				db.delete(TABLE_EOL_SCHEME_HEADER_MASTER, whereClause1,
						whereArgs);

				if (shouldOrderClear) {
					db.delete(TABLE_EOL_ORDER_BOOKING_RESPONSE_MASTER,
							whereClause1, whereArgs);

				}

				System.out.println(noOfSchemeDetailDeleted);

			}
		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}
		}

		return true;
	}

	public synchronized JSONArray getEOLOrderBoolingResponseData(
			long mActivityID) {

		SQLiteDatabase db = this.getReadableDatabase();

		String queryEOLOrderBookingResponse = "SELECT " + KEY_BEAT_STORE_ID
				+ "," + KEY_EOL_SCHEME_ID + ","
				+ KEY_EOL_DETAILS_BASIC_MODEL_CODE + ","
				+ KEY_EOL_DETAILS_ORDER_QUATITY + ","
				+ KEY_EOL_DETAILS_ACTUAL_SUPPORT + " FROM "
				+ TABLE_EOL_ORDER_BOOKING_RESPONSE_MASTER + " WHERE "
				+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " = " + mActivityID;

		JSONArray rootJSONArray = new JSONArray();
		try {
			Cursor cursorRoot = db.rawQuery(queryEOLOrderBookingResponse, null);

			if (cursorRoot.moveToFirst()) {
				do {

					JSONObject jsonObject = new JSONObject();
					jsonObject.put("StoreID", cursorRoot.getInt(cursorRoot
							.getColumnIndex(KEY_BEAT_STORE_ID)));

					jsonObject.put("SchemeID", cursorRoot.getInt(cursorRoot
							.getColumnIndex(KEY_EOL_SCHEME_ID)));

					jsonObject
							.put("BasicModelCode",
									cursorRoot.getString(cursorRoot
											.getColumnIndex(KEY_EOL_DETAILS_BASIC_MODEL_CODE)));

					jsonObject
							.put("OrderQuantity",
									cursorRoot.getInt(cursorRoot
											.getColumnIndex(KEY_EOL_DETAILS_ORDER_QUATITY)));

					jsonObject
							.put("ActualSupport",
									cursorRoot.getInt(cursorRoot
											.getColumnIndex(KEY_EOL_DETAILS_ACTUAL_SUPPORT)));
					rootJSONArray.put(jsonObject);
				}

				while (cursorRoot.moveToNext());
			}
			cursorRoot.close();

		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			db.close();
		}
		return rootJSONArray;

	}

	public void updateStoreCoverge(long storeID) {

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();

			synchronized (db) {

				ContentValues values = new ContentValues();
				values.put(KEY_BEAT_IS_COVERAGE, true);
				db.update(TABLE_STORE_BASIC_MASTER, values, KEY_BEAT_STORE_ID
						+ "= ?", new String[]{String.valueOf(storeID)});

			}

		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}
		}

	}

	private void insertDataInDownloadMappingModuleDataMasterTable(
			SQLiteDatabase db) {

		ContentValues cv = new ContentValues();

		// Collection
		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.PAYMENT_MODE));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_COLLECTION);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		// Palnogram
		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.COMPETITOR));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_PALNOGRAM);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.PLANOGRAM_CLASS));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_PALNOGRAM);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.PLANOGRAM_PRODUCT));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_PALNOGRAM);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.COMPETITION_PRODUCT_GROUP));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_PALNOGRAM);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.COMPETITOR_PRODUCT_GROUP_MAPPING));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_PALNOGRAM);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		// Countershare
		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.COMPETITOR));

		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_COUNTERSHARE);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.COMPETITION_PRODUCT_GROUP));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_COUNTERSHARE);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.SURVEY_QUESTIONS));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_COUNTERSHARE);

		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		// Displayshare
		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.COMPETITOR));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_DISPLAYSHARE);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.COMPETITION_PRODUCT_GROUP));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_DISPLAYSHARE);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.SURVEY_QUESTIONS));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_DISPLAYSHARE);

		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		// Stock Escalation
		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.PRODUCT_LIST));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_STOCK_ESCALATION);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		// Sellout Projection
		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.PRODUCT_LIST));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_SELL_OUT_PROJECTION);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		// Order Booking
		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.PRODUCT_LIST));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_ORDER_BOOKING);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		// EOL
		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.EOL_SCHEMES));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_OD_EOL);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		// FMS Non Store Wise
		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.MSS_CATEGORY));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_FMS_CREATE_FMS_NON_STORE_WISE);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.MSS_TEAM));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_FMS_CREATE_FMS_NON_STORE_WISE);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.MSS_TYPE));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_FMS_CREATE_FMS_NON_STORE_WISE);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.MSS_STATUS));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_FMS_CREATE_FMS_NON_STORE_WISE);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		// FMS Store Wise
		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.MSS_CATEGORY));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_FMS_CREATE_FMS_STORE_WISE);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.MSS_TEAM));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_FMS_CREATE_FMS_STORE_WISE);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.MSS_TYPE));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_FMS_CREATE_FMS_STORE_WISE);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.MSS_STATUS));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_FMS_CREATE_FMS_STORE_WISE);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		// Store Working
		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.OTHER_BEAT));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_MARKETWORKING);

		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		// Race
		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.RACE_POSM_MASTER));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_PRODUCT_AUDIT);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.RACE_FIXTURE_MASTER));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_PRODUCT_AUDIT);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.RACE_POSM_PRODUCT_MAPPING));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_PRODUCT_AUDIT);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.RACE_BRAND_MASTER));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_PRODUCT_AUDIT);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.RACE_PRODUCT_CATEGORY));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_PRODUCT_AUDIT);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.RACE_BRAND_CATEGORY_MAPPING));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_PRODUCT_AUDIT);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.RACE_PRODUCT));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE,
				ModuleCode.MENU_PRODUCT_AUDIT);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		// Questioner

		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
				String.valueOf(DownloadMasterCodes.SURVEY_QUESTIONS));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE, -1);

		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

		// Expense module
		// Expense Type Master
		cv.put(DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE, String.valueOf(DownloadMasterCodes.EXPENSE_TYPE_MASTER));
		cv.put(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE, ModuleCode.MENU_EMS_MODULE);
		db.insert(TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER, null,
				cv);

	}

	private void insertDataInDownloadDataMasterTable(SQLiteDatabase db) {

		try {

			synchronized (db) {

				// for A
				ContentValues initialValues_A = new ContentValues();
				initialValues_A
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"Products");
				initialValues_A
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetProductList");
				initialValues_A
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.PRODUCT_LIST));
				initialValues_A
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_A
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_A);

				// for B
				ContentValues initialValues_B = new ContentValues();
				initialValues_B
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"PaymentModes");
				initialValues_B
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetPaymentModes");
				initialValues_B
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.PAYMENT_MODE));
				initialValues_B
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_B
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_B);

				// for C
				ContentValues initialValues_C = new ContentValues();
				initialValues_C
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"Competitors");
				initialValues_C
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetCompetitors");
				initialValues_C
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.COMPETITOR));
				initialValues_C
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_C
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_C);

				// for D
				ContentValues initialValues_D = new ContentValues();
				initialValues_D
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"Product Category");
				initialValues_D
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetCompetitionProductGroup");
				initialValues_D
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.COMPETITION_PRODUCT_GROUP));
				initialValues_D
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_D
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_D);

				// for E
				ContentValues initialValues_E = new ContentValues();
				initialValues_E
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"Stores");
				initialValues_E
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"DealersListBasedOnCity");
				initialValues_E
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.OTHER_BEAT));
				initialValues_E
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_E
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_E);

				// for F
				ContentValues initialValues_F = new ContentValues();
				initialValues_F
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"Questions");
				initialValues_F
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetSurveyQuestions");
				initialValues_F
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.SURVEY_QUESTIONS));
				initialValues_F
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_F
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_F);

				// for G
				ContentValues initialValues_G = new ContentValues();
				initialValues_G
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"Planogram Class");
				initialValues_G
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetPlanogramClassMasters");
				initialValues_G
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.PLANOGRAM_CLASS));
				initialValues_G
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_G
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_G);

				// for H
				ContentValues initialValues_H = new ContentValues();
				initialValues_H
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"Planogram Product");
				initialValues_H
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetPlanogramProductMasters");
				initialValues_H
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.PLANOGRAM_PRODUCT));
				initialValues_H
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_H
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_H);

				// for I
				ContentValues initialValues_I = new ContentValues();
				initialValues_I
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"Competitor Product Mapping");
				initialValues_I
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetCompetitorProductGroupMapping");
				initialValues_I
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.COMPETITOR_PRODUCT_GROUP_MAPPING));
				initialValues_I
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_I
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_I);

				// for J
				ContentValues initialValues_J = new ContentValues();
				initialValues_J
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"MSS Category");
				initialValues_J
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetMSSCategoryMaster");
				initialValues_J
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.MSS_CATEGORY));
				initialValues_J
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_J
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_J);

				// for O
				ContentValues initialValues_O = new ContentValues();
				initialValues_O
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"MSS Teams");
				initialValues_O
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetMSSTeamMaster");
				initialValues_O
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.MSS_TEAM));
				initialValues_O
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_O
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_O);

				// for P
				ContentValues initialValues_P = new ContentValues();
				initialValues_P
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"MSS Type");
				initialValues_P
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetMSSTypeMaster");
				initialValues_P
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.MSS_TYPE));
				initialValues_P
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_P
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_P);

				// for Q
				ContentValues initialValues_Q = new ContentValues();
				initialValues_Q
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"MSS Status");
				initialValues_Q
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetMSSStatusMaster");
				initialValues_Q
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.MSS_STATUS));
				initialValues_Q
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_Q
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_Q);

				// for K
				ContentValues initialValues_K = new ContentValues();
				initialValues_K
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"EOL");
				initialValues_K
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetEOLSchemes");
				initialValues_K
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.EOL_SCHEMES));
				initialValues_K
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_K
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_K);

				// for L
				ContentValues initialValues_L = new ContentValues();
				initialValues_L
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"RACE POSM MASTER");
				initialValues_L
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetRACEPOSMMaster");
				initialValues_L
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.RACE_POSM_MASTER));
				initialValues_L
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_L
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_L);

				// for R
				ContentValues initialValues_R = new ContentValues();
				initialValues_R
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"RACE FIXTURE MASTER");
				initialValues_R
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetRACEFixtureMaster");
				initialValues_R
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.RACE_FIXTURE_MASTER));
				initialValues_R
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_R
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_R);

				// for S
				ContentValues initialValues_S = new ContentValues();
				initialValues_S
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"RACE POSM PRODUCT MAPPING MASTER");
				initialValues_S
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetRACEPOSMProductMapping");
				initialValues_S
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.RACE_POSM_PRODUCT_MAPPING));
				initialValues_S
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_S
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_S);

				// for T
				ContentValues initialValues_T = new ContentValues();
				initialValues_T
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"RACE BRAND MASTER");
				initialValues_T
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetRaceBrandMaster");
				initialValues_T
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.RACE_BRAND_MASTER));
				initialValues_T
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_T
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_T);

				// for U
				ContentValues initialValues_U = new ContentValues();
				initialValues_U
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"RACE PRODUCT CATEGORY MASTER");
				initialValues_U
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetRaceProductCategory");
				initialValues_U
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.RACE_PRODUCT_CATEGORY));
				initialValues_U
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_U
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_U);

				// for V
				ContentValues initialValues_V = new ContentValues();
				initialValues_V
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"RACE BRAND CATEGORY MAPPING MASTER");
				initialValues_V
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetRaceBrandCategoryMapping");
				initialValues_V
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.RACE_BRAND_CATEGORY_MAPPING));
				initialValues_V
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_V
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_V);

				// for N
				ContentValues initialValues_N = new ContentValues();
				initialValues_N
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"RACE PRODUCT");
				initialValues_N
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetRaceProductMasters");
				initialValues_N
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.RACE_PRODUCT));
				initialValues_N
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_N
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_N);

				// for M
				ContentValues initialValues_M = new ContentValues();
				initialValues_M
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"User Modules");
				initialValues_M
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								"GetUserModules");
				initialValues_M
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.USER_MODULES));
				initialValues_M
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_M
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_M);

				// for W
				ContentValues initialValues_W = new ContentValues();
				initialValues_W
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
								"Expense Master");
				initialValues_W
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
								WebConfig.WebMethod.GET_EXPENCE_MASTER_DATA);
				initialValues_W
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
								String.valueOf(DownloadMasterCodes.EXPENSE_TYPE_MASTER));
				initialValues_W
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
								false);
				initialValues_W
						.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
								false);

				db.insert(TableNames.TABLE_DOWNLOAD_DATA_MASTER, null,
						initialValues_W);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void resetDownloadDataMaster1() {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();

			synchronized (db) {

				String query = "UPDATE "
						+ TableNames.TABLE_DOWNLOAD_DATA_MASTER
						+ " SET "
						+ DownloadDataMasterColumns.KEY_MODIFIED_DATE_TIME_STAMP
						+ " = "
						+ null
						+ " , "
						+ DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED
						+ " = 0 , "
						+ DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS
						+ " = 0";

				db.execSQL(query);

			}
		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
	}

	public List<ActivityDataMasterModel> getActivityStoreIDs() {

		SQLiteDatabase db = this.getReadableDatabase();

		/*
		 * String selectQuery = "SELECT DISTINCT adm." + KEY_USER_ID + ", adm."
		 * + KEY_BEAT_STORE_ID + ",sbm." + KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID
		 * + ",adm." + KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + ", sam." +
		 * KEY_STORE_ASSESSMENT_MASTER_ASSESSMENT_START_TIME + ",sam." +
		 * KEY_STORE_ASSESSMENT_MASTER_ASSESSMENT_END_TIME + " FROM " +
		 * TABLE_ACTIVITY_DATA_MASTER + " adm LEFT JOIN " +
		 * TABLE_STORE_BASIC_MASTER + " sbm ON adm." + KEY_BEAT_STORE_ID +
		 * " = sbm." + KEY_BEAT_STORE_ID + " INNER JOIN " +
		 * TABLE_STORE_ASSESSMENT_MASTER + " sam on sam." + KEY_BEAT_STORE_ID +
		 * " = adm." + KEY_BEAT_STORE_ID + "   WHERE adm." +
		 * KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " IN (" +
		 * SyncUtils.SYNC_STATUS_SUBMIT + "," + SyncUtils.SYNC_STATUS_ERROR +
		 * ")  AND adm." + KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID +
		 * " = -1  AND adm." + KEY_BEAT_STORE_ID + " != 0  ORDER BY adm." +
		 * KEY_BEAT_STORE_ID + " ASC";
		 */

		// Modify by ashish Dwivedi 19 Aug

		String selectQuery = "SELECT DISTINCT " + "adm." + KEY_USER_ID + ", "
				+ "adm." + KEY_BEAT_STORE_ID + "," + "sbm."
				+ KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID + "," + "adm."
				+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + "," + "IFNULL(sam."
				+ KEY_STORE_ASSESSMENT_MASTER_ASSESSMENT_START_TIME + ",'"
				+ Helper.getDateTime() + "') AS "
				+ KEY_STORE_ASSESSMENT_MASTER_ASSESSMENT_START_TIME + ","
				+ "IFNULL(sam."
				+ KEY_STORE_ASSESSMENT_MASTER_ASSESSMENT_END_TIME + ",'"
				+ Helper.getDateTime() + "')AS "
				+ KEY_STORE_ASSESSMENT_MASTER_ASSESSMENT_END_TIME + " FROM "
				+ TABLE_ACTIVITY_DATA_MASTER + " adm LEFT JOIN "
				+ TABLE_STORE_BASIC_MASTER + " sbm ON adm." + KEY_BEAT_STORE_ID
				+ " = sbm." + KEY_BEAT_STORE_ID + " LEFT JOIN "
				+ TABLE_STORE_ASSESSMENT_MASTER + " sam on sam."
				+ KEY_BEAT_STORE_ID + " = adm." + KEY_BEAT_STORE_ID
				+ "   WHERE adm." + KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS
				+ " IN (" + SyncUtils.SYNC_STATUS_SUBMIT + ","
				+ SyncUtils.SYNC_STATUS_ERROR + ")  AND adm."
				+ KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID
				+ " IN (0,-1)  AND adm." + KEY_BEAT_STORE_ID
				+ " != 0  ORDER BY adm." + KEY_BEAT_STORE_ID + " ASC";

		ArrayList<ActivityDataMasterModel> activityDataMasterModels = new ArrayList<ActivityDataMasterModel>();

		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					try {
						ActivityDataMasterModel modal = new ActivityDataMasterModel();

						modal.setUserID(cursor.getInt(cursor
								.getColumnIndex(KEY_USER_ID)));
						modal.setStoreID(cursor.getInt(cursor
								.getColumnIndex(KEY_BEAT_STORE_ID)));
						modal.setCoverageID(cursor.getInt(cursor
								.getColumnIndex(KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID)));
						modal.setSyncStatus(cursor.getInt(cursor
								.getColumnIndex(KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS)));
						modal.setAssessmentStartTime(cursor.getString(cursor
								.getColumnIndex(KEY_STORE_ASSESSMENT_MASTER_ASSESSMENT_START_TIME)));
						modal.setAssessmentEndTime(cursor.getString(cursor
								.getColumnIndex(KEY_STORE_ASSESSMENT_MASTER_ASSESSMENT_END_TIME)));

						modal.setSyncStatus(cursor.getInt(cursor
								.getColumnIndex(KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS)));

						activityDataMasterModels.add(modal);
					} catch (Exception e) {
						Helper.printStackTrace(e);
					}
				} while (cursor.moveToNext());
			}
			cursor.close();

		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			db.close();
		}

		return activityDataMasterModels;

	}

	public void updateActivtyDataMasterServeyResponseID(JSONArray jArray) {

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();

			synchronized (db) {
				int count = jArray.length();
				for (int i = 0; i < count; i++) {
					JSONObject jsonObject = jArray.getJSONObject(i);

					ContentValues contentValues = new ContentValues();
					contentValues.put(
							KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID,
							jsonObject.getInt("SurveyResponseID"));
					int updateRowCount = db.update(TABLE_ACTIVITY_DATA_MASTER,
							contentValues, KEY_BEAT_STORE_ID + "= ?",
							new String[] { String.valueOf(jsonObject
									.getInt("StoreID")) });
					System.out.println("Update Row " + updateRowCount);

				}

			}
		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}

	}

	public JSONObject getStoreGeoTagData(long mActivityID) {

		SQLiteDatabase db = null;
		JSONObject jsonObject = null;
		try {
			db = this.getWritableDatabase();

			synchronized (db) {

				String selectQuery = "SELECT sgm." + KEY_BEAT_STORE_ID
						+ ",sgm." + KEY_STORE_GEO_TAG_LATITUDE + ", sgm."
						+ KEY_STORE_GEO_TAG_LONGITUDE + ", sgm."
						+ KEY_STORE_GEO_TAG_GEO_IMAGE + ", sgm."
						+ KEY_STORE_GEO_TAG_GEO_USER_OPTION + ", sbm."
						+ KEY_BEAT_STORE_NAME + " FROM "
						+ TABLE_STORE_GEO_TAG_RESPONSE + " sgm"
						+ " INNER JOIN " + TABLE_STORE_BASIC_MASTER
						+ " sbm ON  sgm." + KEY_BEAT_STORE_ID + " =  sbm."
						+ KEY_BEAT_STORE_ID + " WHERE sgm."
						+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + "="
						+ mActivityID;

				Cursor cursor = db.rawQuery(selectQuery, null);

				if (cursor.moveToFirst()) {
					do {
						try {
							jsonObject = new JSONObject();
							jsonObject
									.put(KEY_BEAT_STORE_ID,
											cursor.getInt(cursor
													.getColumnIndex(KEY_BEAT_STORE_ID)));

							jsonObject
									.put(KEY_BEAT_STORE_NAME,
											cursor.getString(cursor
													.getColumnIndex(KEY_BEAT_STORE_NAME)));
							jsonObject
									.put(KEY_STORE_GEO_TAG_LATITUDE,
											cursor.getDouble(cursor
													.getColumnIndex(KEY_STORE_GEO_TAG_LATITUDE)));
							jsonObject
									.put(KEY_STORE_GEO_TAG_LONGITUDE,
											cursor.getDouble(cursor
													.getColumnIndex(KEY_STORE_GEO_TAG_LONGITUDE)));
							jsonObject
									.put(KEY_STORE_GEO_TAG_GEO_IMAGE,
											cursor.getString(cursor
													.getColumnIndex(KEY_STORE_GEO_TAG_GEO_IMAGE)) != null ? cursor.getString(cursor
													.getColumnIndex(KEY_STORE_GEO_TAG_GEO_IMAGE))
													: "");
							jsonObject
									.put(KEY_STORE_GEO_TAG_GEO_USER_OPTION,
											cursor.getInt(cursor
													.getColumnIndex(KEY_STORE_GEO_TAG_GEO_USER_OPTION)) == 1 ? true
													: cursor.getInt(cursor
															.getColumnIndex(KEY_STORE_GEO_TAG_GEO_USER_OPTION)) == 0 ? false
															: null);

						} catch (Exception e) {
							Helper.printStackTrace(e);
						}
					} while (cursor.moveToNext());
				}
				cursor.close();

				return jsonObject;

			}
		} catch (SQLException e) {
			Helper.printStackTrace(e);
			;
		} catch (Exception e) {
			Helper.printStackTrace(e);
			;
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}
		}
		return null;

	}

	public String getSurveyQuestionResponseData(SQLiteDatabase openedDatabase,
			String subQuestionID, long activityID) {

		String selectQuery = "SELECT " + KEY_SURVEY_QUESTION_USER_RESPONSE
				+ " FROM " + TABLE_QUESTION_ANSWER_RESPONSE_MASTER + " WHERE "
				+ KEY_SURVEY_QUESTION_SUB_ID + "='" + subQuestionID + "' AND "
				+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + "=" + activityID;

		Cursor cursor = openedDatabase.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				try {

					String userResponse = cursor.getString(cursor
							.getColumnIndex(KEY_SURVEY_QUESTION_USER_RESPONSE));
					if (userResponse != null) {
						return userResponse;
					} else {
						return "";
					}
				} catch (Exception e) {
					Helper.printStackTrace(e);
				}
			} while (cursor.moveToNext());
		}
		cursor.close();

		return "";

	}

	public boolean isSubModuleExists(int moduleParentId) {

		SQLiteDatabase db = this.getReadableDatabase();
		try {
			String query = "SELECT COUNT(" + KEY_NAME + ") FROM "
					+ TABLE_USER_MODULE_MASTER + " WHERE "
					+ KEY_MODULE_PARENT_MODULE_ID + " = " + moduleParentId
					+ ";";
			Cursor mCursor = db.rawQuery(query, null);

			if (mCursor.moveToFirst()) {

				int count = mCursor.getInt(0);
				if (count > 0) {
					return true;
				}

			}
			mCursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}

		}

		return false;

	}

	public void insertStoreAssessment(long storeID) {

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			synchronized (db) {

				ContentValues contentValues = new ContentValues();
				contentValues.put(KEY_BEAT_STORE_ID, storeID);
				contentValues.put(
						KEY_STORE_ASSESSMENT_MASTER_ASSESSMENT_START_TIME,
						Helper.getDateTime());

				long insertRow = db.insertWithOnConflict(
						TABLE_STORE_ASSESSMENT_MASTER, null, contentValues,
						SQLiteDatabase.CONFLICT_IGNORE);

				System.out
						.println("Insert row id " + String.valueOf(insertRow));

				Cursor cursor = db.rawQuery("SELECT "
						+ UserModuleTableColumns.KEY_MODULE_CODE + ","
						+ UserModuleTableColumns.KEY_MODULE_ID + ","
						+ UserModuleTableColumns.KEY_IS_MENDATORY + ","
						+ UserModuleTableColumns.KEY_MODULE_PARENT_MODULE_ID
						+ " FROM " + TableNames.TABLE_USER_MODULE_MASTER
						+ " WHERE " + UserModuleTableColumns.KEY_IS_MENDATORY
						+ " = ? AND "
						+ UserModuleTableColumns.KEY_MODULE_IS_STORE_WISE
						+ " = ?", new String[] { "1", "1" });

				if (cursor.moveToFirst()) {
					do {
						ContentValues values = new ContentValues();
						values.put(StoreModuleMasterColumns.KEY_STORE_ID,
								storeID);
						values.put(
								StoreModuleMasterColumns.KEY_MODULE_CODE,
								cursor.getInt(cursor
										.getColumnIndex(UserModuleTableColumns.KEY_MODULE_CODE)));
						values.put(
								StoreModuleMasterColumns.KEY_MODULE_ID,
								cursor.getInt(cursor
										.getColumnIndex(UserModuleTableColumns.KEY_MODULE_ID)));
						values.put(
								StoreModuleMasterColumns.KEY_IS_MENDATORY,
								cursor.getInt(cursor
										.getColumnIndex(UserModuleTableColumns.KEY_IS_MENDATORY)));
						values.put(
								StoreModuleMasterColumns.KEY_MODULE_PARENT_MODULE_ID,
								cursor.getInt(cursor
										.getColumnIndex(UserModuleTableColumns.KEY_MODULE_PARENT_MODULE_ID)));

						db.insertWithOnConflict(
								TableNames.TABLE_STORE_MODULE_MASTER, null,
								values, SQLiteDatabase.CONFLICT_IGNORE);

						/*
						 * db.execSQL("INSERT INTO " +
						 * TableNames.TABLE_STORE_MODULE_MASTER + "(" +
						 * StoreModuleMasterColumns.KEY_STORE_ID + "," +
						 * StoreModuleMasterColumns.KEY_MODULE_CODE + "," +
						 * StoreModuleMasterColumns.KEY_MODULE_ID + "," +
						 * StoreModuleMasterColumns.KEY_IS_MENDATORY + "," +
						 * StoreModuleMasterColumns.KEY_MODULE_PARENT_MODULE_ID
						 * +") " + "SELECT " + storeID + ",UMM." +
						 * UserModuleTableColumns.KEY_MODULE_CODE + ",UMM." +
						 * UserModuleTableColumns.KEY_MODULE_ID + ",UMM." +
						 * UserModuleTableColumns.KEY_IS_MENDATORY + ",UMM." +
						 * UserModuleTableColumns.KEY_MODULE_PARENT_MODULE_ID +
						 * " FROM " + TableNames.TABLE_USER_MODULE_MASTER +
						 * " UMM WHERE UMM." +
						 * UserModuleTableColumns.KEY_IS_MENDATORY + "=" + 1);
						 */

					} while (cursor.moveToNext());
				}
				cursor.close();
			}
		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			if (db != null && db.isOpen()) {
				// db.close();
			}
		}
	}

	public void updateStoreAssessment(long storeID) {

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();

			synchronized (db) {
				ContentValues initialValues = new ContentValues();

				initialValues.put(
						KEY_STORE_ASSESSMENT_MASTER_ASSESSMENT_END_TIME,
						Helper.getDateTime());

				String whereClause = KEY_BEAT_STORE_ID + "=?";
				String[] whereArgu = new String[] { String.valueOf(storeID) };
				db.update(TABLE_STORE_ASSESSMENT_MASTER, initialValues,
						whereClause, whereArgu);

			}
		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}
		}

	}

	public void deleteStoreAssessment() {

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();

			synchronized (db) {

				db.delete(TABLE_STORE_ASSESSMENT_MASTER, null, null);
			}

		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}
		}

	}

	public Map<String, ArrayList<String>> getPendingMendantoryModuleList() {

		Map<String, ArrayList<String>> mapStoreModules = new HashMap<String, ArrayList<String>>();

		SQLiteDatabase db = null;
		try {
			db = this.getReadableDatabase();
			synchronized (db) {
				String query = "SELECT SBM."
						+ StoreBasicColulmns.KEY_BEAT_STORE_NAME + ", UMM."
						+ UserModuleTableColumns.KEY_NAME + " FROM "
						+ TableNames.TABLE_USER_MODULE_MASTER + " UMM "
						+ " INNER JOIN " + TableNames.TABLE_STORE_MODULE_MASTER
						+ " SMM " + " ON UMM." + KEY_MODULE_ID + " = SMM."
						+ KEY_MODULE_ID + " LEFT JOIN "
						+ TableNames.TABLE_ACTIVITY_DATA_MASTER + " ADM "
						+ " ON ADM." + KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " = SMM." + KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " INNER JOIN " + TableNames.TABLE_STORE_BASIC_MASTER
						+ " SBM ON SBM.STOREID = SMM.STOREID "
						+ " WHERE CASE WHEN SMM.IsMandatory = 1 THEN CASE "
						+ " WHEN SMM." + KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " IS NULL THEN SMM." + KEY_IS_MENDATORY + " = 1  "
						+ " WHEN SMM." + KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " IS NOT NULL THEN ADM."
						+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " NOT IN ( "
						+ SyncUtils.SYNC_STATUS_SUBMIT + ","
						+ SyncUtils.SYNC_STATUS_ERROR + " ) END END ";

				Cursor cursor = db.rawQuery(query, null);
				if (cursor.moveToFirst()) {
					do {
						String key = cursor.getString(0);
						String moduleName = cursor.getString(1);
						ArrayList<String> moduleList = mapStoreModules.get(key);
						if (moduleList == null) {
							ArrayList<String> moudules = new ArrayList<String>();
							moudules.add(moduleName);
							mapStoreModules.put(key, moudules);
						} else {
							moduleList.add(moduleName);
						}
					} while (cursor.moveToNext());
				}

			}
		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
		return mapStoreModules;

	}

	public void clearCompletedDataFramResponseTables() {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			synchronized (db) {
				db.execSQL("DELETE FROM " + TABLE_STORE_BASIC_MASTER
						+ " WHERE " + KEY_BEAT_TYPE + "= '"
						+ Constants.STORE_TYPE_TODAY + "'");

				db.execSQL("DELETE FROM " + TABLE_STORE_PERFORMANCE_MASTER);

				db.execSQL("DELETE FROM " + TABLE_STORE_GEO_TAG_RESPONSE
						+ " WHERE " + KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " IN (SELECT " + KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " FROM " + TABLE_ACTIVITY_DATA_MASTER + " WHERE "
						+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " = "
						+ SyncUtils.SYNC_STATUS_SYNC_COMPLETED + ")");

				db.execSQL("DELETE FROM "
						+ TABLE_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE_MASTER
						+ " WHERE " + KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " IN (SELECT " + KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " FROM " + TABLE_ACTIVITY_DATA_MASTER + " WHERE "
						+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " = "
						+ SyncUtils.SYNC_STATUS_SYNC_COMPLETED + ")");

				db.execSQL("DELETE FROM "
						+ TABLE_QUESTION_ANSWER_RESPONSE_MASTER + " WHERE "
						+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " IN (SELECT "
						+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " FROM "
						+ TABLE_ACTIVITY_DATA_MASTER + " WHERE "
						+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " = "
						+ SyncUtils.SYNC_STATUS_SYNC_COMPLETED + ")");

				db.execSQL("DELETE FROM " + TABLE_COLLECTION_RESPONSE_MASTER
						+ " WHERE " + KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " IN (SELECT " + KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " FROM " + TABLE_ACTIVITY_DATA_MASTER + " WHERE "
						+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " = "
						+ SyncUtils.SYNC_STATUS_SYNC_COMPLETED + ")");

				db.execSQL("DELETE FROM " + TABLE_PLANOGRAM_RESPONSE_MASTER
						+ " WHERE " + KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " IN (SELECT " + KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " FROM " + TABLE_ACTIVITY_DATA_MASTER + " WHERE "
						+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " = "
						+ SyncUtils.SYNC_STATUS_SYNC_COMPLETED + ")");

				db.execSQL("DELETE FROM "
						+ TABLE_PLANOGRAM_PRODUCT_RESPONSE_MASTER + " WHERE "
						+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " IN (SELECT "
						+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " FROM "
						+ TABLE_ACTIVITY_DATA_MASTER + " WHERE "
						+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " = "
						+ SyncUtils.SYNC_STATUS_SYNC_COMPLETED + ")");

				db.execSQL("DELETE FROM "
						+ TABLE_PLANOGRAM_COMPITIOR_RESPONSE_MASTER + " WHERE "
						+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " IN (SELECT "
						+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " FROM "
						+ TABLE_ACTIVITY_DATA_MASTER + " WHERE "
						+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " = "
						+ SyncUtils.SYNC_STATUS_SYNC_COMPLETED + ")");

				db.execSQL("DELETE FROM " + TABLE_ORDER_RESPONSE_MASTER
						+ " WHERE " + KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " IN (SELECT " + KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " FROM " + TABLE_ACTIVITY_DATA_MASTER + " WHERE "
						+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " = "
						+ SyncUtils.SYNC_STATUS_SYNC_COMPLETED + ")");

				db.execSQL("DELETE FROM "
						+ TABLE_RACE_PRODUCT_AUDIT_RESPONSE_MASTER + " WHERE "
						+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " IN (SELECT "
						+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " FROM "
						+ TABLE_ACTIVITY_DATA_MASTER + " WHERE "
						+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " = "
						+ SyncUtils.SYNC_STATUS_SYNC_COMPLETED + ")");
				db.execSQL("DELETE FROM "
						+ TABLE_RACE_POSM_DATA_RESPONSE_MASTER + " WHERE "
						+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " IN (SELECT "
						+ KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + " FROM "
						+ TABLE_ACTIVITY_DATA_MASTER + " WHERE "
						+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " = "
						+ SyncUtils.SYNC_STATUS_SYNC_COMPLETED + ")");
				db.execSQL("DELETE FROM " + TABLE_STORE_ASSESSMENT_MASTER
						+ " WHERE " + KEY_BEAT_STORE_ID + " IN (SELECT  "
						+ KEY_BEAT_STORE_ID + " FROM "
						+ TABLE_ACTIVITY_DATA_MASTER + " WHERE "
						+ KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS + " = "
						+ SyncUtils.SYNC_STATUS_SYNC_COMPLETED + ")");

				db.execSQL("DELETE FROM " + TABLE_ACTIVITY_DATA_MASTER
						+ " WHERE " + KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS
						+ "= " + SyncUtils.SYNC_STATUS_SYNC_COMPLETED + " AND "
						+ KEY_MODULE_CODE + " <> " + ModuleCode.MENU_OD_EOL);
			}
		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
	}

	public void updateStoreWiseModuleMandatoryStatus(Module module,
			boolean isMandatoryEnable, long storeID) {

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();

			synchronized (db) {

				ArrayList<String> ids = new ArrayList<String>();
				int parentID = module.getModuleID();

				recursiveModules(parentID, db, ids, storeID);

				String selection = "";
				for (int i = 0; i < ids.size(); i++) {
					selection += ids.get(i) + ",";
				}

				selection = selection.substring(0, selection.length() - 1);

				String query = "UPDATE " + TableNames.TABLE_STORE_MODULE_MASTER
						+ " SET " + StoreModuleMasterColumns.KEY_IS_MENDATORY
						+ " = " + (isMandatoryEnable ? 1 : 0) + " WHERE  "
						+ StoreModuleMasterColumns.KEY_STORE_ID + "=" + storeID
						+ " AND " + StoreModuleMasterColumns.KEY_MODULE_ID
						+ " IN (" + selection + ")";

				db.execSQL(query);

				/*
				 * String quesy =
				 * "WITH RECURSIVE CTR AS ( SELECT m."+KEY_MODULE_ID
				 * +",m."+KEY_NAME+" ,m."+KEY_MODULE_PARENT_MODULE_ID+"  FROM "+
				 * TABLE_USER_MODULE_MASTER
				 * +" m  WHERE (m."+KEY_MODULE_PARENT_MODULE_ID+" = " +
				 * module.getModuleID() +
				 * " )  UNION ALL  SELECT m."+KEY_MODULE_ID
				 * +",m."+KEY_NAME+", m."
				 * +KEY_MODULE_PARENT_MODULE_ID+"  FROM "+
				 * TABLE_USER_MODULE_MASTER
				 * +" m  INNER JOIN CTR c ON c."+KEY_MODULE_ID+"=m."+
				 * KEY_MODULE_PARENT_MODULE_ID
				 * +" )UPDATE "+TABLE_STORE_MODULE_MASTER
				 * +" SET "+KEY_IS_MENDATORY+" = (SELECT " + (isMandatoryEnable
				 * ? 1 : 0) +
				 * " FROM CTR WHERE "+KEY_MODULE_ID+" = CTR."+KEY_MODULE_ID
				 * +") WHERE " + KEY_BEAT_STORE_ID + "= " + storeID;
				 * 
				 * db.execSQL(quesy);
				 * 
				 * quesy = "UPDATE StoreModuleMaster SET IsMandatory = " +
				 * (isMandatoryEnable ? 1 : 0) + " WHERE ModuleID =  " +
				 * module.getModuleID() + " and " + KEY_BEAT_STORE_ID + " = " +
				 * storeID;
				 * 
				 * db.execSQL(quesy);
				 */
			}
		} catch (SQLException e) {

			Helper.printStackTrace(e);
		} catch (Exception e) {

			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}
		}

	}

	private ArrayList<String> recursiveModules(int parentID, SQLiteDatabase db,
			ArrayList<String> ids, long storeID) {

		// String a =
		// "SELECT "+StoreModuleMasterColumns.KEY_MODULE_ID+" FROM "+TableNames.TABLE_STORE_MODULE_MASTER+" WHERE "+StoreModuleMasterColumns.KEY_MODULE_PARENT_MODULE_ID+" = "+parentID
		// +" AND "+StoreModuleMasterColumns.KEY_STORE_ID+"="+storeID;

		String a = "SELECT SMM.ModuleID, IFNULL (SQOM.IsAffirmative ,-1) AS IsAffirmative "
				+ "FROM StoreModuleMaster SMM "
				+ "INNER JOIN UserModuleMaster UMM "
				+ "ON UMM.ModuleCode = SMM.ModuleCode "
				+ "LEFT OUTER JOIN QuestionAnswerResponseMaster QARM "
				+ "ON SMM.ActivityID = QARM.ActivityID AND UMM.ModuleType = 1 "
				+ "LEFT JOIN SurveyQuestionOptionMaster SQOM "
				+ "ON ( SQOM.SurveyQuestionID = QARM.SurveyQuestionID AND  QARM.UserResponse  = SQOM.OptionValue) "
				+ "WHERE SMM.StoreID = "
				+ storeID
				+ "  AND SMM.ParentModuleID = " + parentID;

		ArrayList<Integer> tempIDs = new ArrayList<Integer>();
		Cursor cursor = db.rawQuery(a, null);
		if (cursor.moveToFirst()) {
			do {
				int moduleID = cursor.getInt(0);
				if (cursor.getInt(1) != 0) {
					ids.add(String.valueOf(moduleID));
					tempIDs.add(moduleID);
				}

			} while (cursor.moveToNext());
		}

		for (int i = 0; i < tempIDs.size(); i++) {
			recursiveModules(tempIDs.get(i), db, ids, storeID);
		}

		return ids;
	}

	public void updateStoreModulesActivity(long mStoreID, int moduleId,
			long mActivityID) {

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();

			synchronized (db) {
				db.execSQL("UPDATE "
						+ TableNames.TABLE_STORE_MODULE_MASTER
						+ " SET "
						+ StoreModuleMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " = " + mActivityID + " WHERE "
						+ StoreModuleMasterColumns.KEY_STORE_ID + " = "
						+ mStoreID + " AND "
						+ StoreModuleMasterColumns.KEY_MODULE_ID + " = "
						+ moduleId);

			}
		} catch (SQLException e) {

			Helper.printStackTrace(e);
		} catch (Exception e) {

			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}
		}

	}

	public void deleteCompletedDataFromStoreModuleMaster() {

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();

			synchronized (db) {
				String query = "DELETE FROM "
						+ TableNames.TABLE_STORE_MODULE_MASTER
						+ " WHERE  "
						+ StoreModuleMasterColumns.KEY_IS_MENDATORY
						+ " = 0   OR  "
						+ StoreModuleMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " IN (SELECT "
						+ ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ " FROM "
						+ TableNames.TABLE_ACTIVITY_DATA_MASTER
						+ " WHERE "
						+ ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS
						+ " = " + SyncUtils.SYNC_STATUS_SYNC_COMPLETED + " )";
				db.execSQL(query);

			}
		} catch (SQLException e) {

			Helper.printStackTrace(e);
		} catch (Exception e) {

			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}
		}

	}

	public void syncModulesMasterAndStoreModuleMaster() {

		String str = "DELETE FROM StoreModuleMaster  WHERE ModuleID NOT IN (SELECT UMM.ModuleID FROM UserModuleMaster UMM WHERE UMM.IsMandatory =1  )  ";

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();

			synchronized (db) {

				db.execSQL(str);
			}

		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}
		}

	}

	public boolean isTodaysBeatEmpty() {
		String query = "select  EXISTS (SELECT  1 FROM  StoreBasicMaster  WHERE Type = 'today'  limit 1)";
		SQLiteDatabase db = null;

		try {
			db = this.getWritableDatabase();

			synchronized (db) {

				Cursor cursor = db.rawQuery(query, null);
				cursor.moveToFirst();
				int count = cursor.getInt(0);
				if (count == 0) {
					return true;
				}
			}

		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {

			}
		}

		return false;
	}

	public boolean getAuthorizationCode(Module module) {
		int tempModuleCode = module.getModuleCode();
		if (module.isQuestionType()) {
			tempModuleCode = -1;
		}

		String query = "SELECT ddm.DownloadStatus, ddm. isDownloadNeeded FROM  DownloadDataModuleMappingMaster  "
				+ "ddmmm inner join  DownloadDataMaster ddm"
				+ " on ddm.DataCode = ddmmm.DataCodewhere  ddmmm.moduleCode = "
				+ tempModuleCode;

		SQLiteDatabase db = null;

		try {
			db = this.getReadableDatabase();

			synchronized (db) {

				Cursor cursor = db.rawQuery(query, null);
				if (cursor.moveToFirst()) {
					do {
						cursor.getInt(0);
						if (cursor.getInt(1) != 0) {

						}

					} while (cursor.moveToNext());
				}

			}

		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
			}
		}

		return false;

	}

	public boolean isDownloadComplete(Module module) {
		int tempModuleCode = module.getModuleCode();
		if (module.isQuestionType()) {
			tempModuleCode = -1;
		}

		String query = "SELECT ddm.DownloadStatus, ddm. isDownloadNeeded FROM  DownloadDataModuleMappingMaster  "
				+ "ddmmm inner join  DownloadDataMaster ddm"
				+ " on ddm.DataCode = ddmmm.DataCode where  ddmmm.moduleCode = "
				+ tempModuleCode;

		SQLiteDatabase db = null;

		try {
			db = this.getReadableDatabase();

			synchronized (db) {

				Cursor cursor = db.rawQuery(query, null);
				if (cursor.moveToFirst()) {
					do {
						cursor.getInt(0);
						if (cursor.getInt(1) != 0) {

							int status = cursor.getInt(0);
							if (status == 0) {
								return false;
							}
						}

					} while (cursor.moveToNext());
				}

			}

		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
			}
		}

		return true;

	}

	public JSONArray getMOMData(Context context) {

		JSONArray jArray = new JSONArray();
		SQLiteDatabase db = null;
		String query = "SELECT * FROM " + TableNames.TABLE_MOM_DETAILS_MASTER
				+ " WHERE " + MOMDetailsMasterColumns.KEY_MOM_IS_DELETED
				+ " = " + 0 + " AND "
				+ MOMDetailsMasterColumns.KEY_MOM_SERVER_ID + " = " + -1 +" OR "+MOMDetailsMasterColumns.KEY_MOM_IS_UPDATED +" = "+1;

		try {
			db = this.getReadableDatabase();

			synchronized (db) {

				Cursor cursor = db.rawQuery(query, null);
				if (cursor.moveToFirst()) {
					do {
						JSONObject jObject = new JSONObject();
						int momID = cursor
								.getInt(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_ID));

						jObject.put("MOMIdClient", momID);
						jObject.put("UserId", Long.valueOf(Helper
								.getStringValuefromPrefs(context,
										SharedPreferencesKey.PREF_USERID)));

						jObject.put(
								"MomDateStr",
								cursor.getString(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_DATE_TEXT)));
						jObject.put(
								"Location",
								cursor.getString(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_LOCATION)));
						jObject.put(
								"Description",
								cursor.getString(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_DISCRIPETION)));
						jObject.put(
								"MOMTitle",
								cursor.getString(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_TITLE)));
						jObject.put(
								"ActionItem",
								cursor.getString(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_ACTION_ITEM)));

						jObject.put(
								"MOMIdServer",
								cursor.getInt(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_SERVER_ID)));

						jObject.put(
								"IsDeleted",
								cursor.getInt(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_IS_DELETED)));

						String query1 = "SELECT "
								+ MOMAttendeesMasterColumns.KEY_MOM_ATTENDEES_NAME
								+ " FROM "
								+ TableNames.TABLE_MOM_ATTENDEES_MASTER
								+ " WHERE "
								+ MOMAttendeesMasterColumns.KEY_MOM_ID + " = "
								+ momID;

						Cursor cursor1 = db.rawQuery(query1, null);
						JSONArray jAttendees = new JSONArray();
						if (cursor1.moveToFirst()) {
							do {
								jAttendees
										.put(new JSONObject().put(
												"AttendeeName",
												cursor1.getString(cursor1
														.getColumnIndex(MOMAttendeesMasterColumns.KEY_MOM_ATTENDEES_NAME))));
							} while (cursor1.moveToNext());

							jObject.put("MOMAttendees", jAttendees);
						}

						jArray.put(jObject);

					} while (cursor.moveToNext());
				}

			}

		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {

			}
		}

		return jArray;
	}

	public ArrayList<MOMDetailModal> getMOMData() {

		ArrayList<MOMDetailModal> jArray = new ArrayList<MOMDetailModal>();
		SQLiteDatabase db = null;
		String query = "SELECT * FROM " + TableNames.TABLE_MOM_DETAILS_MASTER
				+ " WHERE " + MOMDetailsMasterColumns.KEY_MOM_IS_DELETED
				+ " = " + 0 + " ORDER BY "+MOMDetailsMasterColumns.KEY_MOM_DATE_VALUE+" DESC";

		try {
			db = this.getReadableDatabase();

			synchronized (db) {

				Cursor cursor = db.rawQuery(query, null);
				if (cursor.moveToFirst()) {
					do {
						MOMDetailModal jObject = new MOMDetailModal();

						int momID = cursor
								.getInt(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_ID));
						jObject.momID = momID;

						jObject.momTitle = cursor
								.getString(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_TITLE));

						jObject.momDate = cursor
								.getString(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_DATE_TEXT));
						jObject.momDateValue = cursor
								.getLong(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_DATE_VALUE));
						jObject.momLocaton = cursor
								.getString(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_LOCATION));
						jObject.momActionItem = cursor
								.getString(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_ACTION_ITEM));

						jObject.momDiscription = cursor
								.getString(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_DISCRIPETION));
						jObject.momServerID = cursor
								.getInt(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_SERVER_ID));
						jObject.momIsDeleted = cursor
								.getInt(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_IS_DELETED)) == 1?true:false;
						

						String query1 = "SELECT "
								+ MOMAttendeesMasterColumns.KEY_MOM_ATTENDEES_NAME
								+ " FROM "
								+ TableNames.TABLE_MOM_ATTENDEES_MASTER
								+ " WHERE "
								+ MOMAttendeesMasterColumns.KEY_MOM_ID + " = "
								+ momID;

						Cursor cursor1 = db.rawQuery(query1, null);
						ArrayList<String> jAttendees = new ArrayList<String>();
						if (cursor1.moveToFirst()) {
							do {
								jAttendees
										.add(cursor1.getString(cursor1
												.getColumnIndex(MOMAttendeesMasterColumns.KEY_MOM_ATTENDEES_NAME)));
							} while (cursor1.moveToNext());

							jObject.momAttendees = jAttendees;
						}

						jArray.add(jObject);

					} while (cursor.moveToNext());
				}

			}

		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {

			}
		}

		return jArray;
	}

	public void updateMOMData(JSONObject jObject) {

		try {

			SQLiteDatabase db = null;
			try {
				db = this.getWritableDatabase();

				synchronized (db) {
					
					
					int momID = jObject
							.getInt(MOMDetailsMasterColumns.KEY_MOM_ID);
					db.delete(TableNames.TABLE_MOM_DETAILS_MASTER,
							MOMDetailsMasterColumns.KEY_MOM_ID + " = ?",
							new String[] { String.valueOf(momID) });
					db.delete(TableNames.TABLE_MOM_ATTENDEES_MASTER,
							MOMAttendeesMasterColumns.KEY_MOM_ID + " = ?",
							new String[] { String.valueOf(momID) });

					ContentValues momDetailsValues = new ContentValues();
					momDetailsValues.put(MOMDetailsMasterColumns.KEY_MOM_ID,
							momID);

					momDetailsValues
							.put(MOMDetailsMasterColumns.KEY_MOM_DATE_TEXT,
									jObject.getString(MOMDetailsMasterColumns.KEY_MOM_DATE_TEXT));

					momDetailsValues
							.put(MOMDetailsMasterColumns.KEY_MOM_DATE_VALUE,
									jObject.getLong(MOMDetailsMasterColumns.KEY_MOM_DATE_VALUE));
					
					momDetailsValues
							.put(MOMDetailsMasterColumns.KEY_MOM_LOCATION,
									jObject.getString(MOMDetailsMasterColumns.KEY_MOM_LOCATION));
					momDetailsValues
							.put(MOMDetailsMasterColumns.KEY_MOM_TITLE,
									jObject.getString(MOMDetailsMasterColumns.KEY_MOM_TITLE));
					momDetailsValues
							.put(MOMDetailsMasterColumns.KEY_MOM_ACTION_ITEM,
									jObject.getString(MOMDetailsMasterColumns.KEY_MOM_ACTION_ITEM));
					momDetailsValues
							.put(MOMDetailsMasterColumns.KEY_MOM_DISCRIPETION,
									jObject.getString(MOMDetailsMasterColumns.KEY_MOM_DISCRIPETION));

					momDetailsValues
					.put(MOMDetailsMasterColumns.KEY_MOM_SERVER_ID,
							jObject.getInt(MOMDetailsMasterColumns.KEY_MOM_SERVER_ID));

					
					momDetailsValues
					.put(MOMDetailsMasterColumns.KEY_MOM_IS_DELETED,
							jObject.getBoolean(MOMDetailsMasterColumns.KEY_MOM_IS_DELETED));
					
					momDetailsValues
					.put(MOMDetailsMasterColumns.KEY_MOM_IS_UPDATED,
							true);


					db.insert(TableNames.TABLE_MOM_DETAILS_MASTER, null,
							momDetailsValues);

					JSONArray attendees = jObject.getJSONArray("Attendees");
					for (int i = 0; i < attendees.length(); i++) {
						String attendeeName = attendees
								.getJSONObject(i)
								.getString(
										MOMAttendeesMasterColumns.KEY_MOM_ATTENDEES_NAME);
						ContentValues contentValuesMOMAttendee = new ContentValues();
						contentValuesMOMAttendee.put(
								MOMAttendeesMasterColumns.KEY_MOM_ID, momID);
						contentValuesMOMAttendee
								.put(MOMAttendeesMasterColumns.KEY_MOM_ATTENDEES_NAME,
										attendeeName);
						db.insert(TableNames.TABLE_MOM_ATTENDEES_MASTER, null,
								contentValuesMOMAttendee);
					}
				}

			} catch (SQLException e) {
				Helper.printStackTrace(e);
			} catch (Exception e) {
				Helper.printStackTrace(e);
			} finally {

				if (db != null && db.isOpen()) {
					db.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void deleteMOMData(int[] momID) {

		try {

			SQLiteDatabase db = null;
			try {
				db = this.getWritableDatabase();

				synchronized (db) {

					String whereClause = MOMDetailsMasterColumns.KEY_MOM_ID
							+ " IN (";
					for (int i = 0; i < momID.length; i++) {
						whereClause += "?, ";
					}
					whereClause = whereClause.substring(0,
							whereClause.length() - 2) + ")";

					Arrays.sort(momID);
					String[] whereArgs = Arrays.toString(momID).split(
							"[\\[\\]]")[1].split(", ");

					ContentValues values = new ContentValues();
					values.put(MOMDetailsMasterColumns.KEY_MOM_IS_DELETED, true);

					String a = TextUtils.join(",", whereArgs);

					String deleteQueryUnsyncedData = "DELETE FROM "
							+ TableNames.TABLE_MOM_DETAILS_MASTER + " WHERE "
							+ MOMDetailsMasterColumns.KEY_MOM_ID + " IN ( " + a
							+ ") AND "
							+ MOMDetailsMasterColumns.KEY_MOM_SERVER_ID + " = "
							+ -1;

					db.execSQL(deleteQueryUnsyncedData);

					db.update(TableNames.TABLE_MOM_DETAILS_MASTER, values,
							whereClause, whereArgs);

					db.delete(TableNames.TABLE_MOM_ATTENDEES_MASTER,
							whereClause, whereArgs);

				}

			} catch (SQLException e) {
				Helper.printStackTrace(e);
			} catch (Exception e) {
				Helper.printStackTrace(e);
			} finally {

				if (db != null && db.isOpen()) {
					db.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateMOMIds(ResponseDto responseModal) {

		if (responseModal.isSuccess()) {

			SQLiteDatabase db = null;
			try {
				db = this.getWritableDatabase();

				synchronized (db) {
					JSONArray jArray = new JSONArray(responseModal.getResult());
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject momOutputJsonObject = jArray
								.getJSONObject(i);
						String query = "UPDATE "
								+ TableNames.TABLE_MOM_DETAILS_MASTER
								+ " SET "
								+ DatabaseConstants.MOMDetailsMasterColumns.KEY_MOM_SERVER_ID
								+ " = "
								+ momOutputJsonObject.getInt("MOMIdServer")
								+ " WHERE "
								+ DatabaseConstants.MOMDetailsMasterColumns.KEY_MOM_ID
								+ " = "
								+ momOutputJsonObject.getInt("MOMIdClient");

						db.execSQL(query);

					}

				}

			} catch (SQLException e) {
				Helper.printStackTrace(e);
			} catch (Exception e) {
				Helper.printStackTrace(e);
			} finally {

				if (db != null && db.isOpen()) {
					db.close();
				}
			}

		}

	}

	public HashMap<Integer, Integer> getAllDeletedMOM() {

		String deleteMOMQuery = "SELECT "
				+ MOMDetailsMasterColumns.KEY_MOM_SERVER_ID + ","+MOMDetailsMasterColumns.KEY_MOM_ID+" FROM "
				+ TableNames.TABLE_MOM_DETAILS_MASTER + " WHERE "
				+ MOMDetailsMasterColumns.KEY_MOM_SERVER_ID + " <> " + -1
				+ " AND " + MOMDetailsMasterColumns.KEY_MOM_IS_DELETED + " = "
				+ 1;
 		SQLiteDatabase db = null;
 		HashMap<Integer, Integer> mapMomClientServerID = new HashMap<Integer, Integer>();

		try {
			db = this.getReadableDatabase();

			synchronized (db) {

				Cursor cursor = db.rawQuery(deleteMOMQuery, null);
				if (cursor.moveToFirst()) {
					do {
						mapMomClientServerID.put(cursor.getInt(1), cursor.getInt(0));

					} while (cursor.moveToNext());
				}

			}

		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
			}
		}
		 
		return mapMomClientServerID;

	}

	public Integer[] getAllDeletedMOM1() {

		String deleteMOMQuery = "SELECT "
				+ MOMDetailsMasterColumns.KEY_MOM_SERVER_ID + " FROM "
				+ TableNames.TABLE_MOM_DETAILS_MASTER + " WHERE "
				+ MOMDetailsMasterColumns.KEY_MOM_SERVER_ID + " <> " + -1
				+ " AND " + MOMDetailsMasterColumns.KEY_MOM_IS_DELETED + " = "
				+ 1;
		ArrayList<Integer> serverMOMID = new ArrayList<Integer>();
		SQLiteDatabase db = null;

		try {
			db = this.getReadableDatabase();

			synchronized (db) {

				Cursor cursor = db.rawQuery(deleteMOMQuery, null);
				if (cursor.moveToFirst()) {
					do {
						serverMOMID.add(cursor.getInt(0));

					} while (cursor.moveToNext());
				}

			}

		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
			}
		}
		Integer[] stockArr = new Integer[serverMOMID.size()];
		stockArr = serverMOMID.toArray(stockArr);

		return stockArr;

	}

	public void deleteMOMDataPhicaly(HashMap<Integer, Integer> momServerIDsMap) {

		try {

			SQLiteDatabase db = null;
			try {
				db = this.getWritableDatabase();

				synchronized (db) {
					

					Set<Integer> keySetMomID = momServerIDsMap.keySet();
					Integer[] momClientIDs = keySetMomID.toArray(new Integer[keySetMomID.size()]);
					Arrays.sort(momClientIDs);
					String[] whereArgsClient = Arrays.toString(momClientIDs).split(
							"[\\[\\]]")[1].split(", ");
				
					
					
					Collection<Integer> valueSetMOMServerID = momServerIDsMap.values();
					Integer[] momServerIDs = valueSetMOMServerID.toArray(new Integer[valueSetMOMServerID.size()]);
					Arrays.sort(momServerIDs);
					String[] whereArgsServer = Arrays.toString(momServerIDs).split(
							"[\\[\\]]")[1].split(", ");
					
					
					
					
					String whereClauseServerMOMID = MOMDetailsMasterColumns.KEY_MOM_SERVER_ID
							+ " IN (";
					String whereClauseClinetMOMID = MOMDetailsMasterColumns.KEY_MOM_ID
							+ " IN (";
					
					for (Integer momID : keySetMomID) {
						whereClauseServerMOMID += "?, ";
						whereClauseClinetMOMID += "?, ";
					}				
					whereClauseServerMOMID = whereClauseServerMOMID.substring(0,
							whereClauseServerMOMID.length() - 2) + ")";
					
					whereClauseClinetMOMID = whereClauseClinetMOMID.substring(0,
							whereClauseClinetMOMID.length() - 2) + ")";
					
					
					
					db.delete(TableNames.TABLE_MOM_DETAILS_MASTER, whereClauseServerMOMID,
							whereArgsServer);

					db.delete(TableNames.TABLE_MOM_ATTENDEES_MASTER,
							whereClauseClinetMOMID, whereArgsClient);

				}

			} catch (SQLException e) {
				Helper.printStackTrace(e);
			} catch (Exception e) {
				Helper.printStackTrace(e);
			} finally {

				if (db != null && db.isOpen()) {
					db.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void deleteMOMDataPhicaly1(Integer[] momServerIDs) {

		try {

			SQLiteDatabase db = null;
			try {
				db = this.getWritableDatabase();

				synchronized (db) {

					String whereClause = MOMDetailsMasterColumns.KEY_MOM_SERVER_ID
							+ " IN (";
					for (int i = 0; i < momServerIDs.length; i++) {
						whereClause += "?, ";
					}
					whereClause = whereClause.substring(0,
							whereClause.length() - 2) + ")";

					Arrays.sort(momServerIDs);
					String[] whereArgs = Arrays.toString(momServerIDs).split(
							"[\\[\\]]")[1].split(", ");

				
					db.delete(TableNames.TABLE_MOM_DETAILS_MASTER, whereClause,
							whereArgs);

					db.delete(TableNames.TABLE_MOM_ATTENDEES_MASTER,
							whereClause, whereArgs);

				}

			} catch (SQLException e) {
				Helper.printStackTrace(e);
			} catch (Exception e) {
				Helper.printStackTrace(e);
			} finally {

				if (db != null && db.isOpen()) {
					db.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
 
	public void insertMOMServerData(ArrayList<MOMDetailModal> modals) {

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			synchronized (db) {

				for (MOMDetailModal momDetailModal : modals) {

					ContentValues initialValues = new ContentValues();
					initialValues.put(
							MOMDetailsMasterColumns.KEY_MOM_SERVER_ID,
							momDetailModal.momServerID);
					initialValues.put(MOMDetailsMasterColumns.KEY_MOM_TITLE,
							momDetailModal.momTitle);
					initialValues.put(MOMDetailsMasterColumns.KEY_MOM_DATE_TEXT,
							momDetailModal.momDate);
					initialValues.put(MOMDetailsMasterColumns.KEY_MOM_DATE_VALUE,
							momDetailModal.momDateValue);
					initialValues.put(MOMDetailsMasterColumns.KEY_MOM_LOCATION,
							momDetailModal.momLocaton);
					initialValues.put(
							MOMDetailsMasterColumns.KEY_MOM_ACTION_ITEM,
							momDetailModal.momActionItem);
					initialValues.put(
							MOMDetailsMasterColumns.KEY_MOM_DISCRIPETION,
							momDetailModal.momDiscription);
					initialValues.put(
							MOMDetailsMasterColumns.KEY_MOM_IS_DELETED,
							momDetailModal.momIsDeleted);
					initialValues.put(
							MOMDetailsMasterColumns.KEY_MOM_IS_UPDATED,
							momDetailModal.momIsUpdated);

					String sqlQuery = "SELECT  COUNT("
							+ MOMDetailsMasterColumns.KEY_MOM_SERVER_ID
							+ ") as MOMIdServerCount , "+MOMDetailsMasterColumns.KEY_MOM_ID+" FROM  " + TableNames.TABLE_MOM_DETAILS_MASTER
							+ " WHERE "
							+ MOMDetailsMasterColumns.KEY_MOM_SERVER_ID + " = "
							+ momDetailModal.momServerID;
					Cursor cursor = db.rawQuery(sqlQuery, null);
					if (cursor.moveToFirst()) {

						int count = cursor.getInt(cursor.getColumnIndex("MOMIdServerCount"));
						
						if (count < 1) {

							long momID = db.insert(
									TableNames.TABLE_MOM_DETAILS_MASTER, null,
									initialValues);
							
							ArrayList<String> attendeesList = momDetailModal.momAttendees;
							for (String momAttendeeName : attendeesList) {
								ContentValues v = new ContentValues();
								v.put(MOMAttendeesMasterColumns.KEY_MOM_ID,
										momID);
								v.put(MOMAttendeesMasterColumns.KEY_MOM_ATTENDEES_NAME,
										momAttendeeName);
								db.insert(
										TableNames.TABLE_MOM_ATTENDEES_MASTER,
										null, v);
							}
						} else{
							 
							int momID = cursor.getInt(1);
							db.update(TableNames.TABLE_MOM_DETAILS_MASTER, initialValues, MOMDetailsMasterColumns.KEY_MOM_ID+"= ? ", new String[]{String.valueOf(momID)});
							
							db.delete(TableNames.TABLE_MOM_ATTENDEES_MASTER, MOMAttendeesMasterColumns.KEY_MOM_ID+" = ?", new String[]{String.valueOf(momID)});
							
							ArrayList<String> attendeesList = momDetailModal.momAttendees;
							for (String momAttendeeName : attendeesList) {
								ContentValues v = new ContentValues();
								v.put(MOMAttendeesMasterColumns.KEY_MOM_ID,
										momID);
								v.put(MOMAttendeesMasterColumns.KEY_MOM_ATTENDEES_NAME,
										momAttendeeName);
								db.insert(
										TableNames.TABLE_MOM_ATTENDEES_MASTER,
										null, v);
							}
							
							
						}

					}

				}

			}
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}

	}

	public void updateMOMServerKey(HashMap<Integer, Integer> map) {

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			synchronized (db) {

				Set<Integer> keys = map.keySet();
				for (Integer integer : keys) {
					ContentValues values = new ContentValues();
					values.put(MOMDetailsMasterColumns.KEY_MOM_SERVER_ID,
							map.get(integer));
					
					values.put(MOMDetailsMasterColumns.KEY_MOM_IS_UPDATED,
							false);

					db.update(TableNames.TABLE_MOM_DETAILS_MASTER, values,
							MOMDetailsMasterColumns.KEY_MOM_ID + "= ?",
							new String[]{String.valueOf(integer)});

				}

			}
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}

	}

	public ArrayList<MOMDetailModal> getMOMDataSearch(String momSearchText) {

		ArrayList<MOMDetailModal> jArray = new ArrayList<MOMDetailModal>();
		SQLiteDatabase db = null;
		String query = "SELECT * FROM " + TableNames.TABLE_MOM_DETAILS_MASTER
				+ " WHERE " + MOMDetailsMasterColumns.KEY_MOM_IS_DELETED
				+ " = " + 0 + " AND " + MOMDetailsMasterColumns.KEY_MOM_TITLE
				+ " LIKE '%" + momSearchText + "%'";

		try {
			db = this.getReadableDatabase();

			synchronized (db) {

				Cursor cursor = db.rawQuery(query, null);
				if (cursor.moveToFirst()) {
					do {
						MOMDetailModal jObject = new MOMDetailModal();

						int momID = cursor
								.getInt(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_ID));
						jObject.momID = momID;

						jObject.momTitle = cursor
								.getString(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_TITLE));

						jObject.momDate = cursor
								.getString(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_DATE_TEXT));

						jObject.momDateValue = cursor
								.getLong(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_DATE_VALUE));
						jObject.momLocaton = cursor
								.getString(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_LOCATION));
						jObject.momActionItem = cursor
								.getString(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_ACTION_ITEM));

						jObject.momDiscription = cursor
								.getString(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_DISCRIPETION));
						jObject.momServerID = cursor
								.getInt(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_SERVER_ID));
						jObject.momIsDeleted = cursor
								.getInt(cursor
										.getColumnIndex(MOMDetailsMasterColumns.KEY_MOM_IS_DELETED)) == 1 ? true
								: false;

						String query1 = "SELECT "
								+ MOMAttendeesMasterColumns.KEY_MOM_ATTENDEES_NAME
								+ " FROM "
								+ TableNames.TABLE_MOM_ATTENDEES_MASTER
								+ " WHERE "
								+ MOMAttendeesMasterColumns.KEY_MOM_ID + " = "
								+ momID;

						Cursor cursor1 = db.rawQuery(query1, null);
						ArrayList<String> jAttendees = new ArrayList<String>();
						if (cursor1.moveToFirst()) {
							do {
								jAttendees
										.add(cursor1.getString(cursor1
												.getColumnIndex(MOMAttendeesMasterColumns.KEY_MOM_ATTENDEES_NAME)));
							} while (cursor1.moveToNext());

							jObject.momAttendees = jAttendees;
						}

						jArray.add(jObject);

					} while (cursor.moveToNext());
				}

			}

		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {

			}
		}

		return jArray;

	}

	public void insertLeaveData() {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			synchronized (db) {


			}
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
	}


    public List<LMSLeaveDataModal> getLeaveListData(int requestType, long userID, long dateFilter) {

		List<LMSLeaveDataModal> list = new ArrayList<LMSLeaveDataModal>();
		String query = null;

		SQLiteDatabase db = null;
		try {


			db = this.getReadableDatabase();
			synchronized (db) {
				switch (requestType){
					case LMSConstants.TAB_SUBMITTED:

						query = "SELECT " +
								"LM.LeaveMasterID, " +
								"LM.LeaveSubject,  " +
								"LM.AppliedDate, " +
								"LM.CreatedBy, " +
								"LM.CreatedByUserName, " +
								"LM.CreatedDate, " +
								"LM.CurrentStatus, " +
								"LM.ModifiedBy, " +
								"LM.ModifiedByUserName, " +
								"LM.ModifiedDate, " +
								"LM.NumberOfLeave, " +
								"LM.PendingWith, " +
								"LM.PendingWithUserName, " +
								"LM.Remarks, " +
								"LM.LMSLeaveTypeMasterID, " +
								"LTM.LeaveTypeCode, " +
								"LTM.LeaveType, " +
								"LTM.LeavesTaken " +
								"FROM LMSLeaveMaster LM  " +
								" INNER JOIN LMSLeaveTypeMaster LTM " +
								" ON LM.LMSLeaveTypeMasterID = LTM.LMSLeaveTypeMasterID " +
								" WHERE LM.CreatedBy = " +userID+
								" AND LM.CreatedDate >"+dateFilter+
								" ORDER BY LM.CreatedDate DESC ";


						break;
					case LMSConstants.TAB_PENDING:

						query = "SELECT " +
								"LM.LeaveMasterID, " +
								"LM.LeaveSubject,  " +
								"LM.AppliedDate, " +
								"LM.CreatedBy, " +
								"LM.CreatedByUserName, " +
								"LM.CreatedDate, " +
								"LM.CurrentStatus, " +
								"LM.ModifiedBy, " +
								"LM.ModifiedByUserName, " +
								"LM.ModifiedDate, " +
								"LM.NumberOfLeave, " +
								"LM.PendingWith, " +
								"LM.PendingWithUserName, " +
								"LM.Remarks, " +
								"LM.LMSLeaveTypeMasterID, " +
 								"LTM.LeaveTypeCode, " +
								"LTM.LeaveType, " +
								"LTM.LeavesTaken " +
								"FROM LMSLeaveMaster LM  " +
								" INNER JOIN LMSLeaveTypeMaster LTM " +
								" ON LM.LMSLeaveTypeMasterID = LTM.LMSLeaveTypeMasterID " +
								" WHERE LM.LeaveMasterID IN (SELECT LeaveMasterID FROM LMSStatusLog WHERE CreatedBy= "+userID+" AND CurrentStatus IN (1) )"+
								" AND LM.CreatedDate >"+dateFilter+
								" ORDER BY LM.CreatedDate DESC ";
						break;
					case LMSConstants.TAB_ACTED:
						query = "SELECT " +
								"LM.LeaveMasterID, " +
								"LM.LeaveSubject,  " +
								"LM.AppliedDate, " +
								"LM.CreatedBy, " +
								"LM.CreatedByUserName, " +
								"LM.CreatedDate, " +
								"LM.CurrentStatus, " +
								"LM.ModifiedBy, " +
								"LM.ModifiedByUserName, " +
								"LM.ModifiedDate, " +
								"LM.NumberOfLeave, " +
								"LM.PendingWith, " +
								"LM.PendingWithUserName, " +
								"LM.Remarks, " +
								"LM.LMSLeaveTypeMasterID, " +
 								"LTM.LeaveTypeCode, " +
								"LTM.LeaveType, " +
								"LTM.LeavesTaken " +
								"FROM LMSLeaveMaster LM  " +
								" INNER JOIN LMSLeaveTypeMaster LTM " +
								" ON LM.LMSLeaveTypeMasterID = LTM.LMSLeaveTypeMasterID " +
								" WHERE LM.LeaveMasterID IN (SELECT LeaveMasterID FROM LMSStatusLog WHERE CreatedBy= "+userID+" AND CurrentStatus IN (2,3) )"+
								" AND LM.CreatedDate >"+dateFilter+
								" ORDER BY LM.CreatedDate DESC ";

						break;

					case LMSConstants.TAB_NOTIFICATION:
						query = "SELECT " +
								" LM.LeaveMasterID, " +
								" LM.LeaveSubject,  " +
								" LM.AppliedDate, " +
								" LM.CreatedBy, " +
								" LM.CreatedByUserName, " +
								" LM.CreatedDate, " +
								" LM.CurrentStatus, " +
								" LM.ModifiedBy, " +
								" LM.ModifiedByUserName, " +
								" LM.ModifiedDate, " +
								" LM.NumberOfLeave, " +
								" LM.PendingWith, " +
								" LM.PendingWithUserName, " +
								" LM.Remarks, " +
								" LM.LMSLeaveTypeMasterID, " +
 								" LTM.LeaveTypeCode, " +
								" LTM.LeaveType, " +
								" LTM.LeavesTaken " +
								" FROM LMSLeaveMaster LM  " +
								" INNER JOIN LMSLeaveTypeMaster LTM " +
								" ON LM.LMSLeaveTypeMasterID = LTM.LMSLeaveTypeMasterID " +
								" WHERE LM.LeaveMasterID IN (SELECT LeaveMasterID FROM LMSStatusLog WHERE CreatedBy= "+userID+" AND CurrentStatus IN (6) )"+
								" AND LM.CreatedDate >"+dateFilter+
								" ORDER BY LM.CreatedDate DESC ";

						break;
				}
				Cursor cursor = db.rawQuery(query, null);

				if (cursor.moveToFirst()){

					do {
						LMSLeaveDataModal modal = new LMSLeaveDataModal();

						modal.LeaveMasterID = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MASTER_ID));
						modal.LeaveSubject = cursor.getString(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_SUBJECT));
						modal.AppliedDate = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_APPLIED_DATE));
                        modal.CreatedBy = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CREATED_BY));
                        modal.CreatedByUserName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CREATED_BY_USER_NAME));
						modal.CreatedDate = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CREATED_DATE));
						modal.CurrentStatus = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CURRENT_STATUS));
                        modal.ModifiedBy = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MODIFIED_BY));
                        modal.ModifiedByUserName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MODIFIED_BY_USER_NAME));
						modal.ModifiedDate = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MODIFIED_DATE));
						modal.NumberOfLeave= cursor.getInt(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_NUMBER_OF_LEAVE));
						modal.PendingWith = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_PENDING_WITH));
						modal.PendingWithUserName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_PENDING_WITH_USER_NAME));
						modal.Remarks = cursor.getString(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_REMARKS));
						modal.LMSLeaveTypeMasterID = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_TYPE_ID));
						modal.LeaveTypeCode= cursor.getString(cursor.getColumnIndex(DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TYPE_CODE));
						modal.LeaveType= cursor.getString(cursor.getColumnIndex(DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TYPE));
						modal.LeavesTaken= cursor.getInt(cursor.getColumnIndex(DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TAKEN));

						String queryDate ="SELECT * FROM LMSLeaveDateMaster LDM " +
								"WHERE LDM.LeaveMasterID = "+modal.LeaveMasterID;

						Cursor dateCursor = db.rawQuery(queryDate,null);
						ArrayList<LMSLeaveDateModal> leaveDateModals = new ArrayList<>();
						if (dateCursor.moveToFirst()){
							do {
								LMSLeaveDateModal leaveDateModal = new LMSLeaveDateModal();
								leaveDateModal.LMSLeaveDetailID = dateCursor.getInt(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_DETAIL_ID));
								leaveDateModal.LeaveMasterID = dateCursor.getInt(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MASTER_ID));
								leaveDateModal.CurrentStatus = dateCursor.getInt(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_CURRENT_STATUS));
								leaveDateModal.LeaveDate = dateCursor.getLong(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_DATE));
								leaveDateModal.LeaveDateText = dateCursor.getString(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_DATE_TEXT));
								leaveDateModal.IsHalfDay = dateCursor.getInt(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_IS_HALF_DAY)) == 1;
								leaveDateModal.CreatedBy = dateCursor.getInt(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_CREADTED_BY));
                                leaveDateModal.ModifiedBy = dateCursor.getInt(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MODIFIED_BY));
                                leaveDateModal.ModifiedByUserName = dateCursor.getString(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MODIFIED_BY_USER_NAME));
								leaveDateModal.ModifiedDate = dateCursor.getLong(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MODIFIED_DATE));
								leaveDateModals.add(leaveDateModal);

							}while (dateCursor.moveToNext());

							modal.leaveDateModals = leaveDateModals;
						}




						String queryStatusLog ="SELECT * FROM LMSStatusLog LSL " +
								"WHERE LSL.LeaveMasterID = "+modal.LeaveMasterID +" ORDER BY CreatedDate DESC";

						Cursor statusLogCursor = db.rawQuery(queryStatusLog,null);
						ArrayList<LMSLeaveStatusLogModal> leaveStatusLogModals = new ArrayList<>();
						if (statusLogCursor .moveToFirst()){
							do {

								LMSLeaveStatusLogModal leaveStatusLogModal = new LMSLeaveStatusLogModal();
								leaveStatusLogModal.LMSStatusLogID = statusLogCursor.getInt(statusLogCursor.getColumnIndex(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_LOG_ID));
								leaveStatusLogModal.LeaveMasterID = statusLogCursor.getInt(statusLogCursor.getColumnIndex(DatabaseConstants.LMSStatusLogColumn.KEY_LEAVE_MASTER_ID));
								leaveStatusLogModal.CurrentStatus = statusLogCursor.getInt(statusLogCursor.getColumnIndex(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CURRENT_STATUS));
								leaveStatusLogModal.CreatedBy = statusLogCursor.getInt(statusLogCursor.getColumnIndex(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CREATED_BY));
                                leaveStatusLogModal.CreatedByUserName = statusLogCursor.getString(statusLogCursor.getColumnIndex(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CREATED_BY_USER_NAME));
								leaveStatusLogModal.CreatedDate = statusLogCursor.getLong(statusLogCursor.getColumnIndex(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CREATED_DATE));
								leaveStatusLogModal.Remarks= statusLogCursor.getString(statusLogCursor.getColumnIndex(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_REMARKS));
								leaveStatusLogModals.add(leaveStatusLogModal);

							}while (statusLogCursor.moveToNext());

							modal.leaveStatusLogModals = leaveStatusLogModals;
						}
						list.add(modal);

					}while (cursor.moveToNext());
				}



			}
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}


		return list;
	}




	public List<LMSLeaveDataModal> getLeaveListData(int requestType, long userID, long startDate,long endDate) {

		List<LMSLeaveDataModal> list = new ArrayList<LMSLeaveDataModal>();
		String query = null;

		SQLiteDatabase db = null;
		try {


			db = this.getReadableDatabase();
			synchronized (db) {
				switch (requestType){
					case LMSConstants.TAB_SUBMITTED:

						query = "SELECT " +
								"LM.LeaveMasterID, " +
								"LM.LeaveSubject,  " +
								"LM.AppliedDate, " +
								"LM.CreatedBy, " +
								"LM.CreatedByUserName, " +
								"LM.CreatedDate, " +
								"LM.CurrentStatus, " +
								"LM.ModifiedBy, " +
								"LM.ModifiedByUserName, " +
								"LM.ModifiedDate, " +
								"LM.NumberOfLeave, " +
								"LM.PendingWith, " +
								"LM.PendingWithUserName, " +
								"LM.Remarks, " +
								"LM.LMSLeaveTypeMasterID, " +
								"LTM.LeaveTypeCode, " +
								"LTM.LeaveType, " +
								"LTM.LeavesTaken " +
 								"FROM LMSLeaveMaster LM  " +
								" INNER JOIN LMSLeaveTypeMaster LTM " +
								" ON LM.LMSLeaveTypeMasterID = LTM.LMSLeaveTypeMasterID " +
								" WHERE LM.CreatedBy = " +userID+
								" AND LM.CreatedDate between "+startDate+" and "+endDate+
								" ORDER BY LM.CreatedDate DESC ";


						break;
					case LMSConstants.TAB_PENDING:

						query = "SELECT " +
								"LM.LeaveMasterID, " +
								"LM.LeaveSubject,  " +
								"LM.AppliedDate, " +
								"LM.CreatedBy, " +
								"LM.CreatedByUserName, " +
								"LM.CreatedDate, " +
								"LM.CurrentStatus, " +
								"LM.ModifiedBy, " +
								"LM.ModifiedByUserName, " +
								"LM.ModifiedDate, " +
								"LM.NumberOfLeave, " +
								"LM.PendingWith, " +
								"LM.PendingWithUserName, " +
								"LM.Remarks, " +
								"LM.LMSLeaveTypeMasterID, " +
								"LTM.LeaveTypeCode, " +
								"LTM.LeaveType, " +
								"LTM.LeavesTaken " +
 								"FROM LMSLeaveMaster LM  " +
								" INNER JOIN LMSLeaveTypeMaster LTM " +
								" ON LM.LMSLeaveTypeMasterID = LTM.LMSLeaveTypeMasterID " +
								" WHERE LM.LeaveMasterID IN (SELECT LeaveMasterID FROM LMSStatusLog WHERE CreatedBy= "+userID+" AND CurrentStatus IN (1) )"+
								" AND LM.CreatedDate between "+startDate+" and "+endDate+
								" ORDER BY LM.CreatedDate DESC ";
						break;
					case LMSConstants.TAB_ACTED:
						query = "SELECT " +
								"LM.LeaveMasterID, " +
								"LM.LeaveSubject,  " +
								"LM.AppliedDate, " +
								"LM.CreatedBy, " +
								"LM.CreatedByUserName, " +
								"LM.CreatedDate, " +
								"LM.CurrentStatus, " +
								"LM.ModifiedBy, " +
								"LM.ModifiedByUserName, " +
								"LM.ModifiedDate, " +
								"LM.NumberOfLeave, " +
								"LM.PendingWith, " +
								"LM.PendingWithUserName, " +
								"LM.Remarks, " +
								"LM.LMSLeaveTypeMasterID, " +
								"LTM.LeaveTypeCode, " +
								"LTM.LeaveType, " +
								"LTM.LeavesTaken " +
 								"FROM LMSLeaveMaster LM  " +
								" INNER JOIN LMSLeaveTypeMaster LTM " +
								" ON LM.LMSLeaveTypeMasterID = LTM.LMSLeaveTypeMasterID " +
								" WHERE LM.LeaveMasterID IN (SELECT LeaveMasterID FROM LMSStatusLog WHERE CreatedBy= "+userID+" AND CurrentStatus IN (2,3) )"+
								" AND LM.CreatedDate between "+startDate+" and "+endDate+
								" ORDER BY LM.CreatedDate DESC ";

						break;

					case LMSConstants.TAB_NOTIFICATION:
						query = "SELECT " +
								" LM.LeaveMasterID, " +
								" LM.LeaveSubject,  " +
								" LM.AppliedDate, " +
								" LM.CreatedBy, " +
								" LM.CreatedByUserName, " +
								" LM.CreatedDate, " +
								" LM.CurrentStatus, " +
								" LM.ModifiedBy, " +
								" LM.ModifiedByUserName, " +
								" LM.ModifiedDate, " +
								" LM.NumberOfLeave, " +
								" LM.PendingWith, " +
								" LM.PendingWithUserName, " +
								" LM.Remarks, " +
								" LM.LMSLeaveTypeMasterID, " +
								" LTM.LeaveTypeCode, " +
								" LTM.LeaveType, " +
								" LTM.LeavesTaken " +
								" FROM LMSLeaveMaster LM  " +
								" INNER JOIN LMSLeaveTypeMaster LTM " +
								" ON LM.LMSLeaveTypeMasterID = LTM.LMSLeaveTypeMasterID " +
								" WHERE LM.LeaveMasterID IN (SELECT LeaveMasterID FROM LMSStatusLog WHERE CreatedBy= "+userID+" AND CurrentStatus IN (6) )"+
								" AND LM.CreatedDate between "+startDate+" and "+endDate+
								" ORDER BY LM.CreatedDate DESC ";

						break;
				}
				Cursor cursor = db.rawQuery(query, null);

				if (cursor.moveToFirst()){

					do {
						LMSLeaveDataModal modal = new LMSLeaveDataModal();

						modal.LeaveMasterID = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MASTER_ID));
						modal.LeaveSubject = cursor.getString(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_SUBJECT));
						modal.AppliedDate = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_APPLIED_DATE));
						modal.CreatedBy = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CREATED_BY));
						modal.CreatedByUserName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CREATED_BY_USER_NAME));
						modal.CreatedDate = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CREATED_DATE));
						modal.CurrentStatus = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CURRENT_STATUS));
						modal.ModifiedBy = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MODIFIED_BY));
						modal.ModifiedByUserName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MODIFIED_BY_USER_NAME));
						modal.ModifiedDate = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MODIFIED_DATE));
						modal.NumberOfLeave= cursor.getInt(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_NUMBER_OF_LEAVE));
						modal.PendingWith = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_PENDING_WITH));
						modal.PendingWithUserName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_PENDING_WITH_USER_NAME));
						modal.Remarks = cursor.getString(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_REMARKS));
						modal.LMSLeaveTypeMasterID = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_TYPE_ID));
						modal.LeaveTypeCode= cursor.getString(cursor.getColumnIndex(DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TYPE_CODE));
						modal.LeaveType= cursor.getString(cursor.getColumnIndex(DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TYPE));
						modal.LeavesTaken= cursor.getInt(cursor.getColumnIndex(DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TAKEN));

						String queryDate ="SELECT * FROM LMSLeaveDateMaster LDM " +
								"WHERE LDM.LeaveMasterID = "+modal.LeaveMasterID;

						Cursor dateCursor = db.rawQuery(queryDate,null);
						ArrayList<LMSLeaveDateModal> leaveDateModals = new ArrayList<>();
						if (dateCursor.moveToFirst()){
							do {
								LMSLeaveDateModal leaveDateModal = new LMSLeaveDateModal();
								leaveDateModal.LMSLeaveDetailID = dateCursor.getInt(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_DETAIL_ID));
								leaveDateModal.LeaveMasterID = dateCursor.getInt(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MASTER_ID));
								leaveDateModal.CurrentStatus = dateCursor.getInt(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_CURRENT_STATUS));
								leaveDateModal.LeaveDate = dateCursor.getLong(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_DATE));
								leaveDateModal.LeaveDateText = dateCursor.getString(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_DATE_TEXT));
								leaveDateModal.IsHalfDay = dateCursor.getInt(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_IS_HALF_DAY)) == 1;
								leaveDateModal.CreatedBy = dateCursor.getInt(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_CREADTED_BY));
								leaveDateModal.ModifiedBy = dateCursor.getInt(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MODIFIED_BY));
								leaveDateModal.ModifiedByUserName = dateCursor.getString(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MODIFIED_BY_USER_NAME));
								leaveDateModal.ModifiedDate = dateCursor.getLong(dateCursor.getColumnIndex(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MODIFIED_DATE));
								leaveDateModals.add(leaveDateModal);

							}while (dateCursor.moveToNext());

							modal.leaveDateModals = leaveDateModals;
						}




						String queryStatusLog ="SELECT * FROM LMSStatusLog LSL " +
								"WHERE LSL.LeaveMasterID = "+modal.LeaveMasterID +" ORDER BY CreatedDate DESC";

						Cursor statusLogCursor = db.rawQuery(queryStatusLog,null);
						ArrayList<LMSLeaveStatusLogModal> leaveStatusLogModals = new ArrayList<>();
						if (statusLogCursor .moveToFirst()){
							do {

								LMSLeaveStatusLogModal leaveStatusLogModal = new LMSLeaveStatusLogModal();
								leaveStatusLogModal.LMSStatusLogID = statusLogCursor.getInt(statusLogCursor.getColumnIndex(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_LOG_ID));
								leaveStatusLogModal.LeaveMasterID = statusLogCursor.getInt(statusLogCursor.getColumnIndex(DatabaseConstants.LMSStatusLogColumn.KEY_LEAVE_MASTER_ID));
								leaveStatusLogModal.CurrentStatus = statusLogCursor.getInt(statusLogCursor.getColumnIndex(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CURRENT_STATUS));
								leaveStatusLogModal.CreatedBy = statusLogCursor.getInt(statusLogCursor.getColumnIndex(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CREATED_BY));
								leaveStatusLogModal.CreatedByUserName = statusLogCursor.getString(statusLogCursor.getColumnIndex(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CREATED_BY_USER_NAME));
								leaveStatusLogModal.CreatedDate = statusLogCursor.getLong(statusLogCursor.getColumnIndex(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CREATED_DATE));
								leaveStatusLogModal.Remarks= statusLogCursor.getString(statusLogCursor.getColumnIndex(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_REMARKS));
								leaveStatusLogModals.add(leaveStatusLogModal);

							}while (statusLogCursor.moveToNext());

							modal.leaveStatusLogModals = leaveStatusLogModals;
						}
						list.add(modal);

					}while (cursor.moveToNext());
				}



			}
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}


		return list;
	}


	public List<LMSLeaveTypeModal> getLeaveTypeData() {

		List<LMSLeaveTypeModal> list = new ArrayList<LMSLeaveTypeModal>();


		SQLiteDatabase db = null;
		try {
			db = this.getReadableDatabase();
			synchronized (db) {


				String query ="SELECT LTM.LMSLeaveTypeMasterID, LTM.LeaveType, LTM.LeaveTypeCode ,LTM.LeavesTaken " +
						",(select  configvalue from LMSLeaveTypeConfiguration where configurationvalue=1 and LMSLeaveTypeMasterID=ltm.LMSLeaveTypeMasterID) MaxLimit"+
						",(select  configvalue from LMSLeaveTypeConfiguration where configurationvalue=2 and LMSLeaveTypeMasterID=ltm.LMSLeaveTypeMasterID) ConsecutiveLeaves"+
						" FROM  LMSLeaveTypeMaster LTM ";

				Cursor cursor = db.rawQuery(query, null);

				if (cursor.moveToFirst()){


					 do {
						 LMSLeaveTypeModal modal = new LMSLeaveTypeModal();

						 modal.LMSLeaveTypeMasterID = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TYPE_ID));
						 modal.LeaveType = cursor.getString(cursor.getColumnIndex(DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TYPE));
						 modal.LeaveTypeCode = cursor.getString(cursor.getColumnIndex(DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TYPE_CODE));
						 modal.MaxLimit = cursor.getInt(cursor.getColumnIndex("MaxLimit"));
						 modal.ConsecutiveLeaves = cursor.getInt(cursor.getColumnIndex("ConsecutiveLeaves"));
						 modal.LeavesTaken = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TAKEN));
						 modal.LeaveBalance = modal.MaxLimit-modal.LeavesTaken;

						list.add(modal);

					 }while (cursor.moveToNext());
				}



			}
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
		return list;

	}

	// Expense Modules Methods


	public String getExpenseTypeValue(int inEMSExpenseTypeMasterID)
	{
		String expenseType  = "";
		SQLiteDatabase db = null;
		Cursor expenseTypeCursor = null;


		String query = "SELECT "
				+ DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_NAME
				+ " FROM "
				+ TableNames.TABLE_EXPENSE_TYPE_MASTER
				+ " WHERE "
				+ DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_ID + " = "
				+ inEMSExpenseTypeMasterID;

		try {

			db = this.getReadableDatabase();

			synchronized (db)
			{

				expenseTypeCursor = db.rawQuery(query, null);
				if (expenseTypeCursor.moveToFirst())
				{
					return expenseTypeCursor.getString(expenseTypeCursor
							.getColumnIndex(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_NAME));
				}

			}

		}catch (SQLException e) {

			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}

			if(expenseTypeCursor != null)
				expenseTypeCursor.close();
		}
		return expenseType;
	}



	public ArrayList<EMSExpenseDetail> getLocalDBExpensesDataForGivenTab(int tabID){

		/*String emsExpenseDetailQuery = "SELECT * FROM " + TableNames.TABLE_EMS_EXPENSE_DETAIL
				+ " WHERE " + DatabaseConstants.EMSExpenseDetailTableColumns.KEY_IS_DELETED
				+ " = " + 0 + " ORDER BY "+DatabaseConstants.EMSExpenseDetailTableColumns.KEY_SUBMITTED_DATE+" DESC";*/


		String emsExpenseDetailQuery = "SELECT * FROM " + TableNames.TABLE_EMS_EXPENSE_DETAIL + " where " + DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_TAB_ID + " = " + tabID
				+ " ORDER BY "+DatabaseConstants.EMSExpenseDetailTableColumns.KEY_SUBMITTED_DATE+" DESC";

		return getExpensesData(emsExpenseDetailQuery); //, true

	}




	public ArrayList<EMSExpenseDetail> getSearchExpenseData( String expenseTypeText) { // int tabID,

		int expenseTyperMasterID = 0;

		String emsExpenseTypeMasterIDQuery = "SELECT " + DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_ID +  " from " + TableNames.TABLE_EXPENSE_TYPE_MASTER
				+ " WHERE " + DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_NAME
				+ " LIKE '%" + expenseTypeText + "%'";

		SQLiteDatabase db = null;
		Cursor emsSearchExpenseCursor = null;

		try {
			db = this.getReadableDatabase();

			synchronized (db) {

				emsSearchExpenseCursor = db.rawQuery(emsExpenseTypeMasterIDQuery, null);

				if (emsSearchExpenseCursor != null && emsSearchExpenseCursor.moveToFirst())
				{


					expenseTyperMasterID = emsSearchExpenseCursor.getInt(emsSearchExpenseCursor.getColumnIndex(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_ID));

				/*String emsSearchExpenseDetailQuery = "SELECT * FROM " + TableNames.TABLE_EMS_EXPENSE_DETAIL
				+ " WHERE " + DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_TAB_ID + " = " + tabID
			    + " AND " + DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_TYPE_MASTER_ID
			    + " = " + expenseTyperMasterID;*/

					String emsSearchExpenseDetailQuery = "SELECT * FROM " + TableNames.TABLE_EMS_EXPENSE_DETAIL
							+ " WHERE "
							+ DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_TYPE_MASTER_ID
							+ " = " + expenseTyperMasterID + " ORDER BY "+DatabaseConstants.EMSExpenseDetailTableColumns.KEY_SUBMITTED_DATE+" DESC";

					return getExpensesData(emsSearchExpenseDetailQuery);

				}

			}

		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}

			if (emsSearchExpenseCursor != null) {

				emsSearchExpenseCursor.close();
			}
		}

		return null;
	}



	public int getTotalExpenseCount()
	{
		int count = 0;
		String emsExpenseDetailQuery = "SELECT * FROM " + TableNames.TABLE_EMS_EXPENSE_DETAIL;

		SQLiteDatabase db = null;
		Cursor emsExpenseDetailCursor = null;

		try {
			db = this.getReadableDatabase();

			synchronized (db) {

				emsExpenseDetailCursor = db.rawQuery(emsExpenseDetailQuery, null);

				if (emsExpenseDetailCursor != null && emsExpenseDetailCursor.moveToFirst()) {

					count =  emsExpenseDetailCursor.getCount();
				}
			}

		}

		catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		}
		finally {

			if (db != null && db.isOpen()) {
				db.close();
			}

			if (emsExpenseDetailCursor != null) {

				emsExpenseDetailCursor.close();
			}

			return count;

		}

	}



	public ArrayList<EMSExpenseDetail> getExpensesData(String emsExpenseDetailQuery) { //, boolean inbIsLocalDBData

		ArrayList<EMSExpenseDetail> emsExpenseDetailArrayList = new ArrayList<EMSExpenseDetail>();

		SQLiteDatabase db = null;
		Cursor emsExpenseDetailCursor = null;

		try {
			db = this.getReadableDatabase();

			synchronized (db) {

				emsExpenseDetailCursor = db.rawQuery(emsExpenseDetailQuery, null);

				if (emsExpenseDetailCursor != null && emsExpenseDetailCursor.moveToFirst()) {

					do {


						EMSExpenseDetail emsExpenseDetailObject = new EMSExpenseDetail();

						emsExpenseDetailObject.miEMSExpenseDetailID  = emsExpenseDetailCursor
								.getInt(emsExpenseDetailCursor
										.getColumnIndex(DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_ID));

						emsExpenseDetailObject.miEMSExpenseTypeMasterID = emsExpenseDetailCursor
								.getInt(emsExpenseDetailCursor
										.getColumnIndex(DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_TYPE_MASTER_ID));

						emsExpenseDetailObject.mbBillable = emsExpenseDetailCursor.getInt(emsExpenseDetailCursor.getColumnIndex(DatabaseConstants.EMSExpenseDetailTableColumns.KEY_BILLABLE)) == 1 ? true:false;

						emsExpenseDetailObject.mBillableTo = emsExpenseDetailCursor
								.getString(emsExpenseDetailCursor
										.getColumnIndex(DatabaseConstants.EMSExpenseDetailTableColumns.KEY_BILLABLE_TO));

						emsExpenseDetailObject.mComment = emsExpenseDetailCursor
								.getString(emsExpenseDetailCursor
										.getColumnIndex(DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_COMMENT));

						emsExpenseDetailObject.mPendingWith = emsExpenseDetailCursor
								.getString(emsExpenseDetailCursor
										.getColumnIndex(DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_PENDING_WITH));



						emsExpenseDetailObject.miExpenseStatus = emsExpenseDetailCursor
								.getInt(emsExpenseDetailCursor
										.getColumnIndex(DatabaseConstants.EMSExpenseDetailTableColumns.KEY_STATUS));


						emsExpenseDetailObject.miEMSExpenseDetailIDServer = emsExpenseDetailCursor
								.getInt(emsExpenseDetailCursor
										.getColumnIndex(DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMSEXPENSE_DETAIL_ID_SERVER));

						emsExpenseDetailObject.mbIsDeleted = emsExpenseDetailCursor
								.getInt(emsExpenseDetailCursor
										.getColumnIndex(DatabaseConstants.EMSExpenseDetailTableColumns.KEY_IS_DELETED)) == 1?true:false;

						emsExpenseDetailObject.mbIsActive = emsExpenseDetailCursor
								.getInt(emsExpenseDetailCursor
										.getColumnIndex(DatabaseConstants.EMSExpenseDetailTableColumns.KEY_IS_ACTIVE)) == 1?true:false;

						emsExpenseDetailObject.mlSubmittedDate =   emsExpenseDetailCursor.getLong(emsExpenseDetailCursor.getColumnIndex(DatabaseConstants.EMSExpenseDetailTableColumns.KEY_SUBMITTED_DATE));

						emsExpenseDetailObject.miEMSExpenseDetailITabID =   emsExpenseDetailCursor.getInt(emsExpenseDetailCursor.getColumnIndex(DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_TAB_ID));


						/*String emsExpenseBillDetailQuery = "SELECT * FROM "
								+ TableNames.TABLE_EMS_BILL_DETAIL
								+ " WHERE "
								+ DatabaseConstants.EMSBillDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_ID + " = "
								+ emsExpenseDetailObject.miEMSExpenseDetailID;*/



						String emsExpenseBillDetailQuery;

					/*	if(inbIsLocalDBData)
						{
							emsExpenseBillDetailQuery = "SELECT * FROM "
								+ TableNames.TABLE_EMS_BILL_DETAIL
								+ " WHERE "
								+ DatabaseConstants.EMSBillDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_ID + " = "
								+ emsExpenseDetailObject.miEMSExpenseDetailID
								+ " AND " + DatabaseConstants.EMSBillDetailTableColumns.KEY_IS_DELETED + " = "
								+ 0 ;
						}
						else
						{
							emsExpenseBillDetailQuery = "SELECT * FROM "
									+ TableNames.TABLE_EMS_BILL_DETAIL
									+ " WHERE "
									+ DatabaseConstants.EMSBillDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_ID + " = "
									+ emsExpenseDetailObject.miEMSExpenseDetailID
								 ;
						}*/


						emsExpenseBillDetailQuery = "SELECT * FROM "
								+ TableNames.TABLE_EMS_BILL_DETAIL
								+ " WHERE "
								+ DatabaseConstants.EMSBillDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_ID + " = "
								+ emsExpenseDetailObject.miEMSExpenseDetailID
						;


						Cursor emsExpenseBillDetailCursor = db.rawQuery(emsExpenseBillDetailQuery, null);

						ArrayList<EMSBillDetail> emsBillDetailList = new ArrayList<EMSBillDetail>();

						if (emsExpenseBillDetailCursor != null && emsExpenseBillDetailCursor.moveToFirst()) {

							do {

								EMSBillDetail emsBillDetail = new EMSBillDetail();
								emsBillDetail.mEMSBillDetailID =   emsExpenseBillDetailCursor.getInt(emsExpenseBillDetailCursor.getColumnIndex(DatabaseConstants.EMSBillDetailTableColumns.KEY_EMS_BILL_DETAIL_ID));


								emsBillDetail.mEMSBillDetailIDServer =   emsExpenseBillDetailCursor.getInt(emsExpenseBillDetailCursor.getColumnIndex(DatabaseConstants.EMSBillDetailTableColumns.KEY_EMS_BILL_DETAIL_ID_SERVER));

								emsBillDetail.mEMSExpenseDetailID =   emsExpenseBillDetailCursor.getInt(emsExpenseBillDetailCursor.getColumnIndex(DatabaseConstants.EMSBillDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_ID));
								emsBillDetail.mBillDate =   emsExpenseBillDetailCursor.getLong(emsExpenseBillDetailCursor.getColumnIndex(DatabaseConstants.EMSBillDetailTableColumns.KEY_BILL_DATE));
								emsBillDetail.mBillNo =   emsExpenseBillDetailCursor.getString(emsExpenseBillDetailCursor.getColumnIndex(DatabaseConstants.EMSBillDetailTableColumns.KEY_BILL_NO));
								emsBillDetail.mDescription = emsExpenseBillDetailCursor.getString(emsExpenseBillDetailCursor.getColumnIndex(DatabaseConstants.EMSBillDetailTableColumns.KEY_DESCRIPTION));
								emsBillDetail.mAmount =   emsExpenseBillDetailCursor.getDouble(emsExpenseBillDetailCursor.getColumnIndex(DatabaseConstants.EMSBillDetailTableColumns.KEY_AMOUNT));
								emsBillDetail.mIsActive =   emsExpenseBillDetailCursor.getInt(emsExpenseBillDetailCursor.getColumnIndex(DatabaseConstants.EMSBillDetailTableColumns.KEY_IS_ACTIVE)) == 1 ? true:false;
								emsBillDetail.mIsDeleted =   emsExpenseBillDetailCursor.getInt(emsExpenseBillDetailCursor.getColumnIndex(DatabaseConstants.EMSBillDetailTableColumns.KEY_IS_DELETED)) == 1 ? true:false;

								/*String emsExpenseBillDocumentQuery = "SELECT * FROM "
										+ TableNames.TABLE_EMS_BILL_DOCUMENT_DETAIL
										+ " WHERE "
										+ DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DETAIL_ID + " = "
										+ emsBillDetail.mEMSBillDetailID;*/


								String emsExpenseBillDocumentQuery;

								/*if(inbIsLocalDBData)
								{
								  emsExpenseBillDocumentQuery = "SELECT * FROM "
										+ TableNames.TABLE_EMS_BILL_DOCUMENT_DETAIL
										+ " WHERE "
										+ DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DETAIL_ID + " = "
										+ emsBillDetail.mEMSBillDetailID
										+ " AND " + DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_IS_DELETED + " = "
										+ 0 ;
								}

								else
								{
									 emsExpenseBillDocumentQuery = "SELECT * FROM "
												+ TableNames.TABLE_EMS_BILL_DOCUMENT_DETAIL
												+ " WHERE "
												+ DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DETAIL_ID + " = "
												+ emsBillDetail.mEMSBillDetailID
												;
								}*/


								emsExpenseBillDocumentQuery = "SELECT * FROM "
										+ TableNames.TABLE_EMS_BILL_DOCUMENT_DETAIL
										+ " WHERE "
										+ DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DETAIL_ID + " = "
										+ emsBillDetail.mEMSBillDetailID
								;


								Cursor emsExpenseBillDocumentCursor = db.rawQuery(emsExpenseBillDocumentQuery, null);

								ArrayList<EMSBillDocumentDetail> emsExpenseBillDocumentList = new ArrayList<EMSBillDocumentDetail>();

								if (emsExpenseBillDocumentCursor != null && emsExpenseBillDocumentCursor.moveToFirst()) {

									do {

										EMSBillDocumentDetail emsBillDocumentDetail = new EMSBillDocumentDetail();

										emsBillDocumentDetail.mEMSBillDocumentDetailID = emsExpenseBillDocumentCursor.getInt(emsExpenseBillDocumentCursor.getColumnIndex(DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DOCUMENT_DETAIL_ID));

										emsBillDocumentDetail.mEMSBillDocumentDetailIDServer = emsExpenseBillDocumentCursor.getInt(emsExpenseBillDocumentCursor.getColumnIndex(DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DOCUMENT_DETAIL_ID_SERVER));


										emsBillDocumentDetail.mEMSBillDetailID = emsExpenseBillDocumentCursor.getInt(emsExpenseBillDocumentCursor.getColumnIndex(DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DETAIL_ID));
										emsBillDocumentDetail.mDocumentName = emsExpenseBillDocumentCursor.getString(emsExpenseBillDocumentCursor.getColumnIndex(DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_DOCUMENT_NAME));
										emsBillDocumentDetail.mDocumentFilePath = emsExpenseBillDocumentCursor.getString(emsExpenseBillDocumentCursor.getColumnIndex(DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_DOCUMENT_PATH));
										emsBillDocumentDetail.mIsActive =   emsExpenseBillDocumentCursor.getInt(emsExpenseBillDocumentCursor.getColumnIndex(DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_IS_ACTIVE)) == 1 ? true:false;
										emsBillDocumentDetail.mIsDeleted =   emsExpenseBillDocumentCursor.getInt(emsExpenseBillDocumentCursor.getColumnIndex(DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_IS_DELETED)) == 1 ? true:false;

										emsExpenseBillDocumentList.add(emsBillDocumentDetail);


									} while (emsExpenseBillDocumentCursor.moveToNext());
								}

								if(emsExpenseBillDocumentCursor != null)
									emsExpenseBillDocumentCursor.close();

								emsBillDetail.mEMSBillDocumentDetailList = emsExpenseBillDocumentList;
								emsBillDetailList.add(emsBillDetail);


							} while (emsExpenseBillDetailCursor.moveToNext());

							emsExpenseDetailObject.mEMSBillDetailList= emsBillDetailList;
						}

						if(emsExpenseBillDetailCursor != null)
							emsExpenseBillDetailCursor.close();

						emsExpenseDetailArrayList.add(emsExpenseDetailObject);

					} while (emsExpenseDetailCursor.moveToNext());
				}

			}

		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}

			if (emsExpenseDetailCursor != null) {

				emsExpenseDetailCursor.close();
			}
		}

		return emsExpenseDetailArrayList;
	}

	//  Upload Multipart document


	public ArrayList<ExpenseBillDocumentMultipartUpload> getUploadBillDocument(long iEMSExpenseDetailIDClient,long iEMSExpenseDetailIDServer, long iEMSBillDetailIDClient, long iEMSBillDetailIDServer, long[] emsBillDocumenteDetailIDArray)
	{

		ArrayList<ExpenseBillDocumentMultipartUpload>  expenseBillDocumentMultipartUploadList = new ArrayList<ExpenseBillDocumentMultipartUpload>();
		SQLiteDatabase db = null;
		Cursor billDocumentcursor = null;

		Arrays.sort(emsBillDocumenteDetailIDArray);

		String[] emsBillDocumentDetailWhereArgs = Arrays.toString(emsBillDocumenteDetailIDArray).split(
				"[\\[\\]]")[1].split(", ");

		String emsBillDocumentWhereArgsString = TextUtils.join(",", emsBillDocumentDetailWhereArgs);

		String query = "SELECT "
				+ DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_DOCUMENT_PATH
				+ ","
				+ DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DOCUMENT_DETAIL_ID
				+ ","
				+ DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DOCUMENT_DETAIL_ID_SERVER
				+ " FROM "
				+ TableNames.TABLE_EMS_BILL_DOCUMENT_DETAIL
				+ " WHERE "
				+ DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DOCUMENT_DETAIL_ID +  " IN ( " + emsBillDocumentWhereArgsString
				+ ")";


		try {

			db = this.getReadableDatabase();

			synchronized (db)
			{

				billDocumentcursor = db.rawQuery(query, null);

				if (billDocumentcursor != null && billDocumentcursor.moveToFirst())
				{

					do {

						ExpenseBillDocumentMultipartUpload expenseBillDocumentMultipartUpload = new ExpenseBillDocumentMultipartUpload();

						expenseBillDocumentMultipartUpload.mDocumentFilePath =    billDocumentcursor.getString(billDocumentcursor.getColumnIndex(DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_DOCUMENT_PATH));
						expenseBillDocumentMultipartUpload.miEMSBillDocumentDetailIDServer =  billDocumentcursor.getInt(billDocumentcursor.getColumnIndex(DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DOCUMENT_DETAIL_ID_SERVER));
						expenseBillDocumentMultipartUpload.miEMSBillDocumentDetailIDClient =  billDocumentcursor.getInt(billDocumentcursor.getColumnIndex(DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DOCUMENT_DETAIL_ID));

						expenseBillDocumentMultipartUpload.miEMSExpenseDetailIDClient = iEMSExpenseDetailIDClient;
						expenseBillDocumentMultipartUpload.miEMSExpenseDetailIDServer = iEMSExpenseDetailIDServer;

						expenseBillDocumentMultipartUpload.miEMSBillDetailIDClient = iEMSBillDetailIDClient;
						expenseBillDocumentMultipartUpload.miEMSBillDetailIDServer = iEMSBillDetailIDServer;

						expenseBillDocumentMultipartUploadList.add(expenseBillDocumentMultipartUpload);

					} while (billDocumentcursor.moveToNext());

				}
			}

		}catch (SQLException e) {

			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				db.close();
			}

			if(billDocumentcursor != null)
				billDocumentcursor.close();
		}
		return expenseBillDocumentMultipartUploadList;
	}


	private int getExpenseStatus(long miEMSExpenseDetailIDClient)
	{
		String emsExpenseStausQuery = "SELECT " + DatabaseConstants.EMSExpenseDetailTableColumns.KEY_STATUS + " FROM " + TableNames.TABLE_EMS_EXPENSE_DETAIL
				+ " WHERE "
				+ DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_ID
				+ " = " + miEMSExpenseDetailIDClient;

		SQLiteDatabase db = null;
		Cursor emsExpenseDetailCursor = null;

		try {
			db = this.getReadableDatabase();

			synchronized (db) {

				emsExpenseDetailCursor = db.rawQuery(emsExpenseStausQuery, null);

				if (emsExpenseDetailCursor != null && emsExpenseDetailCursor.moveToFirst()) {

					return emsExpenseDetailCursor.getInt(emsExpenseDetailCursor.getColumnIndex(DatabaseConstants.EMSExpenseDetailTableColumns.KEY_STATUS));

				}
			}

		} catch (SQLException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {

				//db.close();
			}

			if (emsExpenseDetailCursor != null) {

				emsExpenseDetailCursor.close();
			}
		}

		return -10;

	}

	//Pending from Neraj Expense submit response handling
	public ArrayList<ExpenseBillDocumentMultipartUpload> updateExpenseServerKeyAndGetUploadMultipartDocument(ArrayList<ExpenseSubmitResponse> expenseSubmitResponseList) {

		SQLiteDatabase db = null;
		ArrayList<ExpenseBillDocumentMultipartUpload>  expenseBillDocumentMultipartUploadList = new ArrayList<ExpenseBillDocumentMultipartUpload>();

		try {
			db = this.getWritableDatabase();
			synchronized (db) {


				for (ExpenseSubmitResponse expenseSubmitResponse : expenseSubmitResponseList)
				{
					ContentValues emsExpenseDetailValues = new ContentValues();

					emsExpenseDetailValues.put(DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMSEXPENSE_DETAIL_ID_SERVER,
							expenseSubmitResponse.miEMSExpenseDetailIDServer);

					emsExpenseDetailValues.put(DatabaseConstants.EMSExpenseDetailTableColumns.KEY_IS_EXPENSE_UPDATED,
							false);




					int expenseCurrentStatus = getExpenseStatus(expenseSubmitResponse.miEMSExpenseDetailIDClient);

					if(expenseCurrentStatus != EMSConstants.EXPENSE_STATUS_CANCELLED && expenseCurrentStatus != EMSConstants.EXPENSE_STATUS_APPROVED && expenseCurrentStatus != EMSConstants.EXPENSE_STATUS_REJECT)
					{
						emsExpenseDetailValues.put(DatabaseConstants.EMSExpenseDetailTableColumns.KEY_STATUS,
								EMSConstants.EXPENSE_STATUS_PENDING);
					}

					db.update(TableNames.TABLE_EMS_EXPENSE_DETAIL, emsExpenseDetailValues,
							DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_ID + "= ?",
							new String[] { String.valueOf(expenseSubmitResponse.miEMSExpenseDetailIDClient) });

					ArrayList<BillSubmitResponse> billSubmitResponseList = expenseSubmitResponse.mBillSubmitResponseList;

					for(int j = 0; j < billSubmitResponseList.size() ; j++)
					{
						BillSubmitResponse billSubmitResponse = billSubmitResponseList.get(j);


						ContentValues emsBillDetailValues = new ContentValues();

						emsBillDetailValues.put(DatabaseConstants.EMSBillDetailTableColumns.KEY_EMS_BILL_DETAIL_ID_SERVER,
								billSubmitResponse.miEMSBillDetailIDServer);

						db.update(TableNames.TABLE_EMS_BILL_DETAIL, emsBillDetailValues,
								DatabaseConstants.EMSBillDetailTableColumns.KEY_EMS_BILL_DETAIL_ID + "= ?",
								new String[] { String.valueOf(billSubmitResponse.miEMSBillDetailIDClient) });


						ArrayList<BillDocumentSubmitResponse> billDocumentSubmitResponseList = billSubmitResponse.mBillDocumentSubmitResponseList;

						//	int[] emsBillDocumenteDetailIDArray = new int[billDocumentSubmitResponseList.size()];

						for(int k = 0; k < billDocumentSubmitResponseList.size() ; k++)
						{
							BillDocumentSubmitResponse billDocumentSubmitResponse =  billDocumentSubmitResponseList.get(k);

							//	emsBillDocumenteDetailIDArray[k] = billDocumentSubmitResponse.miEMSBillDocumentDetailIDClient;

							ContentValues emsBillDocumentDetailValues = new ContentValues();

							emsBillDocumentDetailValues.put(DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DOCUMENT_DETAIL_ID_SERVER,
									billDocumentSubmitResponse.miEMSBillDocumentDetailIDServer);

							db.update(TableNames.TABLE_EMS_BILL_DOCUMENT_DETAIL, emsBillDocumentDetailValues,
									DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DOCUMENT_DETAIL_ID + "= ?",
									new String[] { String.valueOf(billDocumentSubmitResponse.miEMSBillDocumentDetailIDClient) });

						}

						//expenseBillDocumentMultipartUploadList.addAll(getUploadBillDocument(expenseSubmitResponse.miEMSExpenseDetailIDClient, expenseSubmitResponse.miEMSExpenseDetailIDServer, billSubmitResponse.miEMSBillDetailIDClient, billSubmitResponse.miEMSBillDetailIDServer, emsBillDocumenteDetailIDArray));

					}

				}


				db.execSQL("DELETE FROM " + TableNames.TABLE_EMS_BILL_DETAIL
						+ " WHERE " + DatabaseConstants.EMSBillDetailTableColumns.KEY_EMS_BILL_DETAIL_ID_SERVER
						+ " <> " + 0 + " AND "
						+ DatabaseConstants.EMSBillDetailTableColumns.KEY_IS_DELETED + " = " + 1);

				db.execSQL("DELETE FROM " + TableNames.TABLE_EMS_BILL_DOCUMENT_DETAIL
						+ " WHERE " + DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DOCUMENT_DETAIL_ID_SERVER
						+ " <> " + 0 + " AND "
						+ DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_IS_DELETED + " = " + 1);


				for (ExpenseSubmitResponse expenseSubmitResponse : expenseSubmitResponseList)
				{
					ArrayList<BillSubmitResponse> billSubmitResponseList = expenseSubmitResponse.mBillSubmitResponseList;

					for(int j = 0; j < billSubmitResponseList.size() ; j++)
					{
						BillSubmitResponse billSubmitResponse = billSubmitResponseList.get(j);

						ArrayList<BillDocumentSubmitResponse> billDocumentSubmitResponseList = billSubmitResponse.mBillDocumentSubmitResponseList;

						long[] emsBillDocumenteDetailIDArray = new long[billDocumentSubmitResponseList.size()];

						for(int k = 0; k < billDocumentSubmitResponseList.size() ; k++)
						{
							BillDocumentSubmitResponse billDocumentSubmitResponse =  billDocumentSubmitResponseList.get(k);

							emsBillDocumenteDetailIDArray[k] = billDocumentSubmitResponse.miEMSBillDocumentDetailIDClient;


						}

						expenseBillDocumentMultipartUploadList.addAll(getUploadBillDocument(expenseSubmitResponse.miEMSExpenseDetailIDClient, expenseSubmitResponse.miEMSExpenseDetailIDServer, billSubmitResponse.miEMSBillDetailIDClient, billSubmitResponse.miEMSBillDetailIDServer, emsBillDocumenteDetailIDArray));

					}

				}

			}
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}

		return expenseBillDocumentMultipartUploadList;

	}


	// Method to get Expenses to be uploaded


	public ArrayList<EMSExpenseDetail> getUploadExpenseData()
	{

	/*String emsExpenseDetailQuery = "SELECT * FROM " + TableNames.TABLE_EMS_EXPENSE_DETAIL
			+ " WHERE " + DatabaseConstants.EMSExpenseDetailTableColumns.KEY_IS_DELETED
			+ " = " + 0 + " AND "
			+ DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMSEXPENSE_DETAIL_ID_SERVER + " = " + 0 +" OR " + DatabaseConstants.EMSExpenseDetailTableColumns.KEY_IS_EXPENSE_UPDATED +" = "+1;
	*/


		String emsExpenseDetailQuery = "SELECT * FROM " + TableNames.TABLE_EMS_EXPENSE_DETAIL
				+ " WHERE "
				+ DatabaseConstants.EMSExpenseDetailTableColumns.KEY_EMSEXPENSE_DETAIL_ID_SERVER + " = " + 0 +" OR " + DatabaseConstants.EMSExpenseDetailTableColumns.KEY_IS_EXPENSE_UPDATED +" = "+1;


		return getExpensesData(emsExpenseDetailQuery); //, false

	}

	public ArrayList<Integer> getAllBillDetailsID(long iniEMSExpenseDetailID)
	{
		ArrayList<Integer>  mBillDetailsIDList = new ArrayList<Integer>();
		SQLiteDatabase db = null;
		Cursor billDetailsIDCursor = null;

		String query = "SELECT "
				+ DatabaseConstants.EMSBillDetailTableColumns.KEY_EMS_BILL_DETAIL_ID
				+ " FROM "
				+ TableNames.TABLE_EMS_BILL_DETAIL
				+ " WHERE "
				+ DatabaseConstants.EMSBillDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_ID + " = "
				+ iniEMSExpenseDetailID;

		try {

			db = this.getReadableDatabase();

			synchronized (db)
			{

				billDetailsIDCursor = db.rawQuery(query, null);

				if (billDetailsIDCursor != null && billDetailsIDCursor.moveToFirst())
				{

					do {

						mBillDetailsIDList.add(billDetailsIDCursor.getInt(billDetailsIDCursor.getColumnIndex(DatabaseConstants.EMSBillDetailTableColumns.KEY_EMS_BILL_DETAIL_ID)));

					} while (billDetailsIDCursor.moveToNext());

				}
			}

		}catch (SQLException e) {

			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				//  db.close(); Will close on calling function
			}

			if(billDetailsIDCursor != null)
				billDetailsIDCursor.close();
		}
		return mBillDetailsIDList;
	}

	public boolean isLeaveTypeData() {
		SQLiteDatabase db = null;

		String query = "SELECT EXISTS(SELECT * FROM  LMSLeaveTypeConfiguration)";

		try {

			db = this.getReadableDatabase();

			synchronized (db)
			{

				Cursor cursor = db.rawQuery(query, null);

				if (cursor != null && cursor.moveToFirst())
				{
					int count = cursor.getInt(0);

					return count==1?true:false;

				}
			}

		}catch (SQLException e) {

			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				 //db.close();
			}

		}
		return false;
	}


	public String getLastSyncDate() {
		SQLiteDatabase db = null;

		String query = "SELECT MAX(MODIFIEDDATE)  FROM LMSLEAVEMASTER";

		try {

			db = this.getReadableDatabase();

			synchronized (db)
			{

				Cursor cursor = db.rawQuery(query, null);

				if (cursor != null && cursor.moveToFirst())
				{
					int maxDate = cursor.getInt(0);
                    if(maxDate == 0){
                        return null;
                    }
					Date date = new Date(maxDate);

					return Helper.getDateStringFromDate(date,"dd-MMM-yyyy hh:mm:ss a");

				}
			}

		}catch (SQLException e) {

			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				//db.close();
			}

		}
		return null;
	}




	public ArrayList<Date> getReservedDate(int userID){
		SQLiteDatabase db = null;

		String query = "SELECT DISTINCT LDM.LEAVEDATE FROM LMSLEAVEMASTER LM " +
				"INNER JOIN LMSLEAVEDATEMASTER LDM ON LM.LEAVEMASTERID = LDM.LEAVEMASTERID " +
				"WHERE  LDM.CURRENTSTATUS IN (1,2) AND LM.CREATEDBY =  "+userID;

		ArrayList<Date> dateList = null;
		try {

			db = this.getReadableDatabase();

			synchronized (db)
			{
				dateList = new ArrayList<>();
				Cursor cursor = db.rawQuery(query, null);

				if (cursor != null && cursor.moveToFirst())
				{
					do{
						long dateValue = cursor.getLong(0);
						Date date = new Date();
						date.setTime(dateValue);
						dateList.add(date);

					}
					while (cursor.moveToNext());
				}
			}

		}catch (SQLException e) {

			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		} finally {

			if (db != null && db.isOpen()) {
				//db.close();
			}

		}


		return  dateList;
	}

}