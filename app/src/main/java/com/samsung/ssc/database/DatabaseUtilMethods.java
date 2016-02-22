package com.samsung.ssc.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.location.Location;

import com.samsung.ssc.R;
import com.samsung.ssc.activitymodule.TeamMasterModel;
import com.samsung.ssc.constants.DownloadCode;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseConstants.ActivityDataMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.CollectionResponseTableColumns;
import com.samsung.ssc.database.DatabaseConstants.CompetetionProductGroupColumns;
import com.samsung.ssc.database.DatabaseConstants.CompetitorColumns;
import com.samsung.ssc.database.DatabaseConstants.CompetitorProductGroupMappingTableColumns;
import com.samsung.ssc.database.DatabaseConstants.CounterShareDisplayShareResponseMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.DownloadDataMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.DownloadDataModuleMappingTableColums;
import com.samsung.ssc.database.DatabaseConstants.EOLOrderBookingResponseMasterColoums;
import com.samsung.ssc.database.DatabaseConstants.EOLSchemeDetailsMasterColoums;
import com.samsung.ssc.database.DatabaseConstants.EOLSchemeHeaderMasterMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.OrderResponseMasterTableColumns;
import com.samsung.ssc.database.DatabaseConstants.PaymentModeMasterTableColumns;
import com.samsung.ssc.database.DatabaseConstants.PlanogramClassMasterTableColumns;
import com.samsung.ssc.database.DatabaseConstants.PlanogramCompitiorResponseMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.PlanogramProductMasterTableColumns;
import com.samsung.ssc.database.DatabaseConstants.PlanogramProductResponseMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.PlanogramResponseMasterTableColumns;
import com.samsung.ssc.database.DatabaseConstants.ProductMasterTableColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.FeedbackCategoryMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.FeedbackStatusMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.FeedbackTypeMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.TeamMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.UserFeedbackMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RaceBrandCategoryMappingMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RaceBrandMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RaceBrandProductCategoryMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RaceBrandProductMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RaceFixtureMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RacePOSMDataResponseMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RacePOSMProductMappingMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RacePosmMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RaceProductAuditResponseMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.StoreBasicColulmns;
import com.samsung.ssc.database.DatabaseConstants.StorePerformaceColumns;
import com.samsung.ssc.database.DatabaseConstants.SurveyQuestionColumns;
import com.samsung.ssc.database.DatabaseConstants.SurveyQuestionOptionsColumns;
import com.samsung.ssc.database.DatabaseConstants.UserModuleTableColumns;
import com.samsung.ssc.database.DatabaseConstants.UserProfileTableColumns;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.dto.CompetitionProductGroupDto;
import com.samsung.ssc.dto.EMSExpenseType;
import com.samsung.ssc.dto.EOLSchemeDTO;
import com.samsung.ssc.dto.EOLSchemeDetailDTO;
import com.samsung.ssc.dto.FMSStatusDataModal;
import com.samsung.ssc.dto.FeedbackCategoryMasterModel;
import com.samsung.ssc.dto.FeedbackTypeMasterModel;
import com.samsung.ssc.dto.Module;
import com.samsung.ssc.dto.Option;
import com.samsung.ssc.dto.P1CategoryList;
import com.samsung.ssc.dto.P2CategoryList;
import com.samsung.ssc.dto.PaymentModes;
import com.samsung.ssc.dto.PlanogramProductDataModal;
import com.samsung.ssc.dto.Question;
import com.samsung.ssc.dto.RaceFixtureDataModal;
import com.samsung.ssc.dto.RacePOSMMasterDTO;
import com.samsung.ssc.dto.RacePoductAuditResponseDTO;
import com.samsung.ssc.dto.RaceProductAddToCartDTO;
import com.samsung.ssc.dto.RaceProductDataModal;
import com.samsung.ssc.dto.SKUProductList;
import com.samsung.ssc.dto.StoreBasicModel;
import com.samsung.ssc.dto.StorePerformanceModel;
import com.samsung.ssc.dto.UserFeedback;
import com.samsung.ssc.dto.UserFeedbackValues;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Constants;
import com.samsung.ssc.util.Helper;

public class DatabaseUtilMethods {

    public static JSONObject getUserProfileFromCursor(Cursor cursor) {

        try {
            cursor.moveToFirst();

            JSONObject jsonObject = new JSONObject();
            jsonObject
                    .put(UserProfileTableColumns.KEY_USER_PROFILE_ADDRESS,
                            cursor.getString(cursor
                                    .getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_ADDRESS)));
            jsonObject
                    .put(UserProfileTableColumns.KEY_USER_PROFILE_ALTERNATE_EMAIL_ID,
                            cursor.getString(cursor
                                    .getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_ALTERNATE_EMAIL_ID)));
            jsonObject.put(UserProfileTableColumns.EMAIL_ID, cursor
                    .getString(cursor
                            .getColumnIndex(UserProfileTableColumns.EMAIL_ID)));
            jsonObject
                    .put(UserProfileTableColumns.KEY_USER_PROFILE_EMPLOYEE_CODE,
                            cursor.getString(cursor
                                    .getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_EMPLOYEE_CODE)));
            jsonObject
                    .put(UserProfileTableColumns.KEY_USER_PROFILE_FIRST_NAME,
                            cursor.getString(cursor
                                    .getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_FIRST_NAME)));
            jsonObject
                    .put(UserProfileTableColumns.KEY_USER_PROFILE_LAST_NAME,
                            cursor.getString(cursor
                                    .getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_LAST_NAME)));
            jsonObject
                    .put(UserProfileTableColumns.KEY_USER_PROFILE_IS_OFFLINE_PROFILE,
                            cursor.getInt(cursor
                                    .getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_FIRST_NAME)) == 1 ? true
                                    : false);
            jsonObject
                    .put(UserProfileTableColumns.KEY_USER_PROFILE_IS_ROAMING_PRIFLE,
                            cursor.getInt(cursor
                                    .getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_IS_ROAMING_PRIFLE)) == 1 ? true
                                    : false);
            jsonObject
                    .put(UserProfileTableColumns.KEY_USER_PROFILE_MOBILE_CALLING,
                            cursor.getString(cursor
                                    .getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_MOBILE_CALLING)));
            jsonObject
                    .put(UserProfileTableColumns.KEY_USER_PROFILE_MOBILE_SD,
                            cursor.getString(cursor
                                    .getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_MOBILE_SD)));
            jsonObject
                    .put(UserProfileTableColumns.KEY_USER_PROFILE_USER_ROLE_ID_ACTUAL,
                            cursor.getString(cursor
                                    .getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_USER_ROLE_ID_ACTUAL)));
            jsonObject
                    .put(UserProfileTableColumns.KEY_USER_PROFILE_PICTURE_FILE_NAME,
                            cursor.getString(cursor
                                    .getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_PICTURE_FILE_NAME)));

            jsonObject
                    .put(UserProfileTableColumns.KEY_USER_PROFILE_PIN_CODE,
                            cursor.getString(cursor
                                    .getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_PIN_CODE)));
            jsonObject
                    .put(UserProfileTableColumns.KEY_USER_PROFILE_USER_CODE,
                            cursor.getInt(cursor
                                    .getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_USER_CODE)));
            jsonObject
                    .put(UserProfileTableColumns.KEY_USER_ID,
                            cursor.getInt(cursor
                                    .getColumnIndex(UserProfileTableColumns.KEY_USER_ID)));
            jsonObject
                    .put(UserProfileTableColumns.KEY_USER_ROLE_ID,
                            cursor.getInt(cursor
                                    .getColumnIndex(UserProfileTableColumns.KEY_USER_ROLE_ID)));

            return jsonObject;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public static ContentValues[] getQuestionsContentValues(
            ArrayList<Question> ques) {
        ContentValues[] bulkToInsert = null;
        List<ContentValues> mValueList = new ArrayList<ContentValues>();

        int size = ques.size();
        for (int i = 0; i < size; i++) {
            try {

                ContentValues initialValues = new ContentValues();
                Question question = ques.get(i);
                initialValues.put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_ID,
                        question.getSurveyQuestionId());
                initialValues.put(SurveyQuestionColumns.KEY_SURVEY_QUESTION,
                        question.getQuestion());
                initialValues.put(SurveyQuestionColumns.KEY_MODULE_CODE,
                        question.getModuleCode());
                initialValues.put(SurveyQuestionColumns.KEY_MODULE_ID,
                        question.getModuleId());
                initialValues.put(
                        SurveyQuestionColumns.KEY_SURVEY_QUESTION_SEQUENCE,
                        question.getSequence());
                initialValues
                        .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_PRODUCT_GROUP_ID,
                                question.getProductGroupId());
                initialValues
                        .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_PRODUCT_TYPE_ID,
                                question.getProductTypeId());
                initialValues.put(
                        SurveyQuestionColumns.KEY_SURVEY_QUESTION_TYPE_ID,
                        question.getQuestionTypeId());
                initialValues.put(
                        SurveyQuestionColumns.KEY_SURVEY_QUESTION_TEXT_LENGTH,
                        question.getTextLength());

                initialValues
                        .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_DEPENDENT_OPTION_ID,
                                question.getDependentOptionID());

                initialValues.put(
                        SurveyQuestionColumns.KEY_SURVEY_QUESTION_IMAGE,
                        question.getSurveyQuestionImage());

                initialValues
                        .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATET_MAX_TIMES,
                                question.getRepeatMaxTimes());
                initialValues
                        .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATER_TEXT,
                                question.getRepeaterText());
                initialValues
                        .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATER_TYPE_ID,
                                question.getRepeaterTypeID());
                initialValues.put(SurveyQuestionColumns.KEY_IS_MENDATORY,
                        question.isMandatory());
                mValueList.add(initialValues);

            } catch (Exception e) {
                Helper.printStackTrace(e);
            }

        }

        bulkToInsert = new ContentValues[mValueList.size()];
        bulkToInsert = mValueList.toArray(bulkToInsert);

        return bulkToInsert;
    }

    public static ContentValues[] getQuestionOptionsContentValues(
            ArrayList<Option> questionOptions) {
        ContentValues[] bulkToInsert = null;
        List<ContentValues> mValueList = new ArrayList<ContentValues>();

        int size = questionOptions.size();
        for (int i = 0; i < size; i++) {
            try {

                ContentValues initialValues = new ContentValues();
                Option option = questionOptions.get(i);

                initialValues
                        .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_ID,
                                option.getSurveyOptionId());
                initialValues
                        .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_VALUE,
                                option.getOptionValue());

                initialValues
                        .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_SEQUENCE,
                                option.getSequence());
                initialValues.put(
                        SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_ID,
                        option.getSurveyQuestionId());

                mValueList.add(initialValues);

            } catch (Exception e) {
                Helper.printStackTrace(e);
            }

        }

        bulkToInsert = new ContentValues[mValueList.size()];
        bulkToInsert = mValueList.toArray(bulkToInsert);

        return bulkToInsert;
    }

    public static List<ContentValues[]> getSurveyQuestionData(
            JSONArray jsonArray) {

        List<ContentValues[]> list_contentVlaues = new ArrayList<ContentValues[]>();

        ContentValues[] arr_questions = null;
        ContentValues[] arr_questionOptions = null;
        List<ContentValues> list_questions = new ArrayList<ContentValues>();
        List<ContentValues> list_questionOptions = new ArrayList<ContentValues>();
        try {

            int length = jsonArray.length();

            for (int i = 0; i < length; i++) {

                JSONObject itemJson = jsonArray.getJSONObject(i);
                JSONArray questionJson = itemJson.getJSONArray("Questions");

                int questionJsonLength = questionJson.length();

                for (int j = 0; j < questionJsonLength; j++) {
                    JSONObject jsonObject = questionJson.getJSONObject(j);

                    try {

                        ContentValues initialValues = new ContentValues();

                        initialValues.put(
                                SurveyQuestionColumns.KEY_SURVEY_QUESTION_ID,
                                jsonObject.getInt("SurveyQuestionID"));

                        initialValues.put(
                                SurveyQuestionColumns.KEY_SURVEY_QUESTION,
                                jsonObject.getString("Question"));

                        try {
                            initialValues.put(
                                    SurveyQuestionColumns.KEY_MODULE_CODE,
                                    jsonObject.getInt("ModuleCode"));

                        } catch (Exception e) {
                            initialValues.put(
                                    SurveyQuestionColumns.KEY_MODULE_CODE, -1);
                        }

                        try {
                            initialValues.put(
                                    SurveyQuestionColumns.KEY_MODULE_ID,
                                    jsonObject.getInt("ModuleID"));

                        } catch (Exception e4) {
                            initialValues.put(
                                    SurveyQuestionColumns.KEY_MODULE_ID, -1);
                        }

                        try {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_SEQUENCE,
                                            jsonObject.getInt("Sequence"));

                        } catch (Exception e3) {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_SEQUENCE,
                                            0);
                        }

                        try {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_PRODUCT_GROUP_ID,
                                            jsonObject.getInt("ProductGroupID"));

                        } catch (Exception e2) {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_PRODUCT_GROUP_ID,
                                            -1);

                        }

                        try {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_PRODUCT_TYPE_ID,
                                            jsonObject.getInt("ProductTypeID"));

                        } catch (Exception e1) {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_PRODUCT_TYPE_ID,
                                            -1);

                        }

                        try {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_TYPE_ID,
                                            jsonObject.getInt("QuestionTypeID"));

                        } catch (Exception e1) {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_TYPE_ID,
                                            -1);

                        }

                        try {

                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_IMAGE,
                                            jsonObject
                                                    .getString("QuestionImage"));

                        } catch (Exception e1) {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_IMAGE,
                                            -1);

                        }

                        try {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_TEXT_LENGTH,
                                            jsonObject.getInt("TextLength"));

                        } catch (Exception e4) {
                        }

                        try {

                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_DEPENDENT_OPTION_ID,
                                            jsonObject
                                                    .getInt("DependentOptionID"));

                        } catch (Exception e1) {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_DEPENDENT_OPTION_ID,
                                            -1);

                        }

                        try {
                            initialValues.put(
                                    SurveyQuestionColumns.KEY_IS_MENDATORY,
                                    jsonObject.getBoolean("IsMandatory"));

                        } catch (Exception e3) {
                            initialValues.put(
                                    SurveyQuestionColumns.KEY_IS_MENDATORY,
                                    false);

                        }

                        try {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATER_TEXT,
                                            jsonObject
                                                    .getString("RepeaterText"));

                        } catch (Exception e) {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATER_TEXT,
                                            "");

                        }

                        try {

                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATET_MAX_TIMES,
                                            jsonObject.getInt("RepeatMaxTimes"));

                        } catch (Exception e) {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATET_MAX_TIMES,
                                            0);

                        }

                        try {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATER_TYPE_ID,
                                            jsonObject.getInt("RepeaterTypeID"));

                        } catch (Exception e) {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATER_TYPE_ID,
                                            0);

                        }

                        try {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_IS_DELETED,
                                            jsonObject
                                                    .getBoolean(SurveyQuestionColumns.KEY_IS_DELETED));

                        } catch (Exception e) {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_IS_DELETED,
                                            false);

                        }

                        list_questions.add(initialValues);

                        JSONArray optionJsonArray = jsonObject
                                .getJSONArray("Options");

                        int optionArrayLength = optionJsonArray.length();

                        for (int k = 0; k < optionArrayLength; k++) {
                            try {

                                JSONObject optionJsonItem = optionJsonArray
                                        .getJSONObject(k);

                                ContentValues optionsValues = new ContentValues();

                                optionsValues
                                        .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_VALUE,
                                                optionJsonItem
                                                        .getString("OptionValue"));

                                try {
                                    optionsValues
                                            .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_SEQUENCE,
                                                    optionJsonItem
                                                            .getInt("Sequence"));

                                } catch (Exception e2) {
                                    optionsValues
                                            .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_SEQUENCE,
                                                    0);

                                }

                                try {
                                    optionsValues
                                            .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_ID,
                                                    optionJsonItem
                                                            .getInt("SurveyOptionID"));

                                } catch (Exception e1) {
                                    optionsValues
                                            .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_ID,
                                                    -1);

                                }

                                try {
                                    optionsValues
                                            .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_ID,
                                                    optionJsonItem
                                                            .getInt("SurveyQuestionID"));

                                } catch (Exception e) {
                                    optionsValues
                                            .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_ID,
                                                    -1);
                                }

                                try {
                                    optionsValues
                                            .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_IS_AFFIRMATIVE,
                                                    optionJsonItem
                                                            .getBoolean("IsAffirmative"));

                                } catch (Exception e) {
                                    optionsValues
                                            .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_IS_AFFIRMATIVE,
                                                    false);
                                }

                                try {
                                    optionsValues
                                            .put(SurveyQuestionOptionsColumns.KEY_IS_DELETED,
                                                    jsonObject
                                                            .getBoolean(SurveyQuestionOptionsColumns.KEY_IS_DELETED));

                                } catch (Exception e) {
                                    optionsValues
                                            .put(SurveyQuestionOptionsColumns.KEY_IS_DELETED,
                                                    false);

                                }
                                list_questionOptions.add(optionsValues);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        arr_questions = new ContentValues[list_questions.size()];
        arr_questionOptions = new ContentValues[list_questionOptions.size()];

        arr_questions = list_questions.toArray(arr_questions);
        arr_questionOptions = list_questionOptions.toArray(arr_questionOptions);

        list_contentVlaues.add(arr_questions);
        list_contentVlaues.add(arr_questionOptions);

        return list_contentVlaues;

    }

    public static List<ContentValues[]> getSurveyQuestionData(String result) {

        List<ContentValues[]> list_contentVlaues = new ArrayList<ContentValues[]>();

        ContentValues[] arr_questions = null;
        ContentValues[] arr_questionOptions = null;
        List<ContentValues> list_questions = new ArrayList<ContentValues>();
        List<ContentValues> list_questionOptions = new ArrayList<ContentValues>();
        try {
            JSONArray jsonArray = new JSONArray(result);

            int length = jsonArray.length();

            for (int i = 0; i < length; i++) {

                JSONObject itemJson = jsonArray.getJSONObject(i);
                JSONArray questionJson = itemJson.getJSONArray("Questions");

                int questionJsonLength = questionJson.length();

                for (int j = 0; j < questionJsonLength; j++) {
                    JSONObject jsonObject = questionJson.getJSONObject(j);

                    try {

                        ContentValues initialValues = new ContentValues();

                        initialValues.put(
                                SurveyQuestionColumns.KEY_SURVEY_QUESTION_ID,
                                jsonObject.getInt("SurveyQuestionID"));

                        initialValues.put(
                                SurveyQuestionColumns.KEY_SURVEY_QUESTION,
                                jsonObject.getString("Question"));

                        try {
                            initialValues.put(
                                    SurveyQuestionColumns.KEY_MODULE_CODE,
                                    jsonObject.getInt("ModuleCode"));

                        } catch (Exception e) {
                            initialValues.put(
                                    SurveyQuestionColumns.KEY_MODULE_CODE, -1);
                        }

                        try {
                            initialValues.put(
                                    SurveyQuestionColumns.KEY_MODULE_ID,
                                    jsonObject.getInt("ModuleID"));

                        } catch (Exception e4) {
                            initialValues.put(
                                    SurveyQuestionColumns.KEY_MODULE_ID, -1);
                        }

                        try {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_SEQUENCE,
                                            jsonObject.getInt("Sequence"));

                        } catch (Exception e3) {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_SEQUENCE,
                                            0);
                        }

                        try {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_PRODUCT_GROUP_ID,
                                            jsonObject.getInt("ProductGroupID"));

                        } catch (Exception e2) {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_PRODUCT_GROUP_ID,
                                            -1);

                        }

                        try {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_PRODUCT_TYPE_ID,
                                            jsonObject.getInt("ProductTypeID"));

                        } catch (Exception e1) {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_PRODUCT_TYPE_ID,
                                            -1);

                        }

                        try {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_TYPE_ID,
                                            jsonObject.getInt("QuestionTypeID"));

                        } catch (Exception e1) {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_TYPE_ID,
                                            -1);

                        }

                        try {

                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_IMAGE,
                                            jsonObject
                                                    .getString("QuestionImage"));

                        } catch (Exception e1) {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_IMAGE,
                                            -1);

                        }

                        try {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_TEXT_LENGTH,
                                            jsonObject.getInt("TextLength"));

                        } catch (Exception e4) {
                        }

                        try {

                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_DEPENDENT_OPTION_ID,
                                            jsonObject
                                                    .getInt("DependentOptionID"));

                        } catch (Exception e1) {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_DEPENDENT_OPTION_ID,
                                            -1);

                        }

                        try {
                            initialValues.put(
                                    SurveyQuestionColumns.KEY_IS_MENDATORY,
                                    jsonObject.getBoolean("IsMandatory"));

                        } catch (Exception e3) {
                            initialValues.put(
                                    SurveyQuestionColumns.KEY_IS_MENDATORY,
                                    false);

                        }

                        try {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATER_TEXT,
                                            jsonObject
                                                    .getString("RepeaterText"));

                        } catch (Exception e) {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATER_TEXT,
                                            "");

                        }

                        try {

                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATET_MAX_TIMES,
                                            jsonObject.getInt("RepeatMaxTimes"));

                        } catch (Exception e) {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATET_MAX_TIMES,
                                            0);

                        }

                        try {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATER_TYPE_ID,
                                            jsonObject.getInt("RepeaterTypeID"));

                        } catch (Exception e) {
                            initialValues
                                    .put(SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATER_TYPE_ID,
                                            0);

                        }

                        list_questions.add(initialValues);

                        JSONArray optionJsonArray = jsonObject
                                .getJSONArray("Options");

                        int optionArrayLength = optionJsonArray.length();

                        for (int k = 0; k < optionArrayLength; k++) {
                            try {

                                JSONObject optionJsonItem = optionJsonArray
                                        .getJSONObject(k);

                                ContentValues optionsValues = new ContentValues();

                                optionsValues
                                        .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_VALUE,
                                                optionJsonItem
                                                        .getString("OptionValue"));

                                try {
                                    optionsValues
                                            .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_SEQUENCE,
                                                    optionJsonItem
                                                            .getInt("Sequence"));

                                } catch (Exception e2) {
                                    optionsValues
                                            .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_SEQUENCE,
                                                    0);

                                }

                                try {
                                    optionsValues
                                            .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_ID,
                                                    optionJsonItem
                                                            .getInt("SurveyOptionID"));

                                } catch (Exception e1) {
                                    optionsValues
                                            .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_ID,
                                                    -1);

                                }

                                try {
                                    optionsValues
                                            .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_ID,
                                                    optionJsonItem
                                                            .getInt("SurveyQuestionID"));

                                } catch (Exception e) {
                                    optionsValues
                                            .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_ID,
                                                    -1);
                                }

                                try {
                                    optionsValues
                                            .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_IS_AFFIRMATIVE,
                                                    optionJsonItem
                                                            .getBoolean("IsAffirmative"));

                                } catch (Exception e) {
                                    optionsValues
                                            .put(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_IS_AFFIRMATIVE,
                                                    false);
                                }
                                list_questionOptions.add(optionsValues);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        arr_questions = new ContentValues[list_questions.size()];
        arr_questionOptions = new ContentValues[list_questionOptions.size()];

        arr_questions = list_questions.toArray(arr_questions);
        arr_questionOptions = list_questionOptions.toArray(arr_questionOptions);

        list_contentVlaues.add(arr_questions);
        list_contentVlaues.add(arr_questionOptions);

        return list_contentVlaues;

    }

    public static ArrayList<Question> getQuestionsData(Cursor cursor) {
        ArrayList<Question> questions = new ArrayList<Question>();
        ArrayList<Option> options = null;
        Question question = null;

        try {

            if (cursor.moveToFirst()) {
                do {
                    // Flag to check if Survey questions has any options.
                    boolean isSurveyQuestionAdded = false;
                    int SurveyQuestionID = cursor
                            .getInt(cursor
                                    .getColumnIndex(SurveyQuestionColumns.KEY_SURVEY_QUESTION_ID));
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
                                .getColumnIndex(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_VALUE)));
                        option.setSequence(cursor.getInt(cursor
                                .getColumnIndex("SequenceOption")));
                        option.setSurveyOptionId(cursor.getInt(cursor
                                .getColumnIndex(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_ID)));

                        option.setIsAffirmative(cursor.getInt(cursor
                                .getColumnIndex(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_IS_AFFIRMATIVE)) == 1 ? true
                                : false);
                        options.add(option);
                        question.setOptions(options);
                        // remove previous filled question model
                        questions.remove(question);
                        questions.add(question);

                    } else {
                        question = new Question();

                        question.setSurveyQuestionId(cursor.getInt(cursor
                                .getColumnIndex(SurveyQuestionColumns.KEY_SURVEY_QUESTION_ID)));

                        question.setQuestion(cursor.getString(cursor
                                .getColumnIndex(SurveyQuestionColumns.KEY_SURVEY_QUESTION)));

                        question.setModuleCode(cursor.getInt(cursor
                                .getColumnIndex(SurveyQuestionColumns.KEY_MODULE_CODE)));

                        question.setModuleId(cursor.getInt(cursor
                                .getColumnIndex(SurveyQuestionColumns.KEY_MODULE_ID)));

                        question.setSequence(cursor.getInt(cursor
                                .getColumnIndex(SurveyQuestionColumns.KEY_SURVEY_QUESTION_SEQUENCE)));

                        question.setQuestionTypeId(cursor.getInt(cursor
                                .getColumnIndex(SurveyQuestionColumns.KEY_SURVEY_QUESTION_TYPE_ID)));

                        question.setTextLength(cursor.getInt(cursor
                                .getColumnIndex(SurveyQuestionColumns.KEY_SURVEY_QUESTION_TEXT_LENGTH)));

                        question.setDependentOptionID(cursor.getInt(cursor
                                .getColumnIndex(SurveyQuestionColumns.KEY_SURVEY_QUESTION_DEPENDENT_OPTION_ID)));

                        question.setSurveyQuestionImage(cursor.getString(cursor
                                .getColumnIndex(SurveyQuestionColumns.KEY_SURVEY_QUESTION_IMAGE)));
                        question.setRepeaterText(cursor.getString(cursor
                                .getColumnIndex(SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATER_TEXT)));

                        question.setRepeaterTypeID(cursor.getInt(cursor
                                .getColumnIndex(SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATER_TYPE_ID)));

                        question.setRepeatMaxTimes(cursor.getInt(cursor
                                .getColumnIndex(SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATET_MAX_TIMES)));
                        question.setMandatory(cursor.getInt(cursor
                                .getColumnIndex(SurveyQuestionColumns.KEY_IS_MENDATORY)) == 1 ? true
                                : false);

                        try {
                            question.setUserResponse(cursor.getString(cursor
                                    .getColumnIndex(QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_USER_RESPONSE)) != null ? cursor.getString(cursor
                                    .getColumnIndex(QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_USER_RESPONSE))
                                    : "");
                        } catch (Exception e1) {
                            question.setUserResponse("");
                        }

                        int isOption = cursor
                                .getInt(cursor
                                        .getColumnIndex(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_ID));
                        try {
                            if (isOption != 0) {
                                Option option = new Option();
                                options = new ArrayList<Option>();
                                option.setOptionValue(cursor.getString(cursor
                                        .getColumnIndex(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_VALUE)));
                                option.setSequence(cursor.getInt(cursor
                                        .getColumnIndex("SequenceOption")));
                                option.setSurveyOptionId(cursor.getInt(cursor
                                        .getColumnIndex(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_ID)));
                                option.setIsAffirmative(cursor.getInt(cursor
                                        .getColumnIndex(SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_IS_AFFIRMATIVE)) == 1 ? true
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

        } catch (SQLException e) {

        } finally {

        }
        return questions;
    }

    public static ContentValues[] getQuestionAnswerResponse(JSONArray rootJson,
                                                            long activityID) {

        int count = rootJson.length();
        ContentValues values_arr[] = new ContentValues[count];

        for (int i = 0; i < count; i++) {
            try {
                JSONObject itemJson = rootJson.getJSONObject(i);

                ContentValues initialValues = new ContentValues();

                initialValues
                        .put(QuestionAnswerResponseColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
                                activityID);

                int surveyQuestionID = itemJson.getInt("SurveyQuestionID");
                initialValues.put(
                        QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_ID,
                        surveyQuestionID);
                if (surveyQuestionID == -1) {
                    initialValues
                            .put(QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_SUB_ID,
                                    itemJson.getString("SurveyQuestionSubID"));
                }

                initialValues
                        .put(QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_USER_RESPONSE,
                                itemJson.getString("UserResponse"));
                initialValues
                        .put(QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_TYPE_ID,
                                itemJson.getInt("QuestionTypeID"));

                values_arr[i] = initialValues;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return values_arr;

    }

    public static List<StoreBasicModel> getStoresBasics(Cursor cursor,
                                                        String storeType) {

        List<StoreBasicModel> basicBeatList = new ArrayList<StoreBasicModel>();

        if (cursor.moveToFirst()) {
            do {
                try {
                    StoreBasicModel model = new StoreBasicModel();

                    model.setStoreID(cursor.getLong(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_STORE_ID)));
                    model.setStoreCode(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_STORE_CODE)));
                    model.setStoreName(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_STORE_NAME)));
                    model.setCityName(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_CITY)));
                    model.setChannelType(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_CHINNEL_TYPE)));
                    model.setContactPerson(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_CONTACT_PERSION)));

                    if (storeType.equalsIgnoreCase(Constants.STORE_TYPE_TODAY)) {
                        model.setCoverage((cursor.getInt(cursor
                                .getColumnIndex(StoreBasicColulmns.KEY_BEAT_IS_COVERAGE))) == 1 ? true
                                : false);
                        model.setCoverageID(cursor.getLong(cursor
                                .getColumnIndex(StoreBasicColulmns.KEY_BEAT_COVERAGE_ID)));

                    }

                    model.setEmailID(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_EMAIL_ID)));
                    model.setStoreAddress(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_STORE_ADDRESS)));

                    model.setIsFreeze((cursor.getInt(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_IS_FREEZE))) == 1 ? true
                            : false);

                    model.setFreezeLattitude(cursor.getDouble(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_FREEZE_LATTITUDE)));

                    model.setFreezeLongitude(cursor.getDouble(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_FREEZE_LONGITUDE)));

                    model.setMobileNo(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_MOBILE_NUMBER)));

                    model.setPictureFileName(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_PICTURE_FILE_NAME)));

                    model.setStoreSize(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_STORE_SIZE)));

                    model.setIsDisplayCounterShare((cursor.getInt(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_IS_DISPLAY_COUNTER_SHARE))) == 1 ? true
                            : false);

                    model.setIsPlanogram(cursor.getInt(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_IS_PLANOGRAM)) == 1 ? true
                            : false);

                    model.setStoreClass(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_STORE_CLASS)));

                    basicBeatList.add(model);

                } catch (SQLException e) {

                    Helper.printStackTrace(e);
                }

            } while (cursor.moveToNext());
        }

        return basicBeatList;
    }

    public static List<StoreBasicModel> getStoresBasics(Context context,
                                                        Cursor cursor, boolean isGeoFancingApplicable, Location mLocation) {

        List<StoreBasicModel> basicBeatList = new ArrayList<StoreBasicModel>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    StoreBasicModel model = new StoreBasicModel();

                    model.setStoreID(cursor.getLong(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_STORE_ID)));
                    model.setStoreCode(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_STORE_CODE)));
                    model.setStoreName(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_STORE_NAME)));
                    model.setCityName(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_CITY)));
                    model.setChannelType(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_CHINNEL_TYPE)));
                    model.setContactPerson(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_CONTACT_PERSION)));

                    model.setCoverage((cursor.getInt(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_IS_COVERAGE))) == 1 ? true
                            : false);

                    try {
                        model.setCoverageID(cursor.getLong(cursor
                                .getColumnIndex(StoreBasicColulmns.KEY_BEAT_COVERAGE_ID)));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    model.setEmailID(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_EMAIL_ID)));
                    model.setStoreAddress(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_STORE_ADDRESS)));

                    model.setIsFreeze((cursor.getInt(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_IS_FREEZE))) == 1 ? true
                            : false);
                    double freezedLatitude = cursor
                            .getDouble(cursor
                                    .getColumnIndex(StoreBasicColulmns.KEY_BEAT_FREEZE_LATTITUDE));
                    model.setFreezeLattitude(freezedLatitude);

                    double freezedLongitude = cursor
                            .getDouble(cursor
                                    .getColumnIndex(StoreBasicColulmns.KEY_BEAT_FREEZE_LONGITUDE));
                    model.setFreezeLongitude(freezedLongitude);

                    model.setMobileNo(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_MOBILE_NUMBER)));

                    model.setPictureFileName(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_PICTURE_FILE_NAME)));

                    model.setStoreSize(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_STORE_SIZE)));

                    model.setIsDisplayCounterShare((cursor.getInt(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_IS_DISPLAY_COUNTER_SHARE))) == 1 ? true
                            : false);

                    model.setIsPlanogram(cursor.getInt(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_IS_PLANOGRAM)) == 1 ? true
                            : false);

                    model.setStoreClass(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_STORE_CLASS)));

                    String storeType = cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_BEAT_TYPE));
                    model.setStoreType(storeType);

					/*
                     * @ankit Add New field
					 */

                    model.setLandlineNumber(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_LAND_LINE_NUMBER)));
                    model.setSPCName(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_SPC_NAME)));
                    model.setSPCCategory(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_SPC_CATEGORY)));
                    model.setStoreMangerName(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_STORE_MANAGER_NAME)));
                    model.setSMMobile(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_SM_MOBILE)));
                    model.setSMEmailID(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_SM_EMAIL_ID)));
                    model.setAlternateEmailID(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_ALTERNAMTE_EMAIL_ID)));
                    model.setTinNumber(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_TIN_NUMBER)));
                    model.setPinCode(cursor.getString(cursor
                            .getColumnIndex(StoreBasicColulmns.KEY_PIN_CODE)));

                    if (storeType.equals(Constants.STORE_TYPE_TODAY)) {

                        model.setStoreColor(context.getResources().getColor(
                                R.color.blue_pressed));

                        model.setStoreColorIndex(1);

                        if (model.isIsFreeze()) {
                            double distance = Helper.distance(freezedLatitude,
                                    freezedLongitude, mLocation.getLatitude(),
                                    mLocation.getLongitude(), "K");
                            model.setStoreDistance(distance);
                        } else {
                            model.setStoreDistance(1000);
                        }
                        basicBeatList.add(model);
                    }

                    // conditions for Other Store
                    else if (!model.isIsFreeze()) {
                        model.setStoreColor(context.getResources().getColor(
                                android.R.color.black));

                        model.setStoreColorIndex(3);

                        model.setStoreDistance(1000);

                        basicBeatList.add(model);
                    } else if (!isGeoFancingApplicable) {
                        model.setStoreColor(context.getResources().getColor(
                                R.color.holo_orange_light));

                        model.setStoreColorIndex(2);
                        // model.setStoreDistance(1000);
                        double distance = Helper.distance(freezedLatitude,
                                freezedLongitude, mLocation.getLatitude(),
                                mLocation.getLongitude(), "K");
                        model.setStoreDistance(distance);
                        basicBeatList.add(model);
                    } else if (isGeoFancingApplicable) {

                        double distance = Helper.distance(freezedLatitude,
                                freezedLongitude, mLocation.getLatitude(),
                                mLocation.getLongitude(), "K");
                        if (distance <= Constants.STORE_FENCE_DISTANCE) {
                            model.setStoreColor(context.getResources()
                                    .getColor(R.color.holo_orange_light));

                            model.setStoreColorIndex(2);

                            model.setStoreDistance(distance);
                            basicBeatList.add(model);
                        }
                    }

                } catch (SQLException e) {

                    Helper.printStackTrace(e);
                }

            } while (cursor.moveToNext());
        }

        return basicBeatList;
    }

    public static ContentValues[] getContentValuesStoresBaic(
            JSONArray jsonArray, String type, ContentResolver contentResolver,
            Context context) {

        ContentValues[] contentValuesArr = null;
        try {

            int count = jsonArray.length();
            contentValuesArr = new ContentValues[count];

            for (int i = 0; i < count; i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                if (type.equalsIgnoreCase(Constants.STORE_TYPE_TODAY)) {

                    contentResolver
                            .delete(ProviderContract.DELETE_OTHER_STORE_URI,
                                    StoreBasicColulmns.KEY_BEAT_STORE_ID + "=?",
                                    new String[]{String.valueOf(item
                                            .getInt(StoreBasicColulmns.KEY_BEAT_STORE_ID))});




                }

                ContentValues contentValues = new ContentValues();

                contentValues.put(StoreBasicColulmns.KEY_BEAT_STORE_ID,
                        item.getInt(StoreBasicColulmns.KEY_BEAT_STORE_ID));

                contentValues
                        .put(StoreBasicColulmns.KEY_BEAT_STORE_NAME,
                                item.isNull(StoreBasicColulmns.KEY_BEAT_STORE_NAME) ? ""
                                        : item.getString(StoreBasicColulmns.KEY_BEAT_STORE_NAME));

                contentValues
                        .put(StoreBasicColulmns.KEY_BEAT_STORE_CODE,
                                item.isNull(StoreBasicColulmns.KEY_BEAT_STORE_CODE) ? ""
                                        : item.getString(StoreBasicColulmns.KEY_BEAT_STORE_CODE));

                contentValues
                        .put(StoreBasicColulmns.KEY_BEAT_CITY,
                                item.isNull(StoreBasicColulmns.KEY_BEAT_CITY) ? ""
                                        : item.getString(StoreBasicColulmns.KEY_BEAT_CITY));

                // enter coverage id and is coverage only for
                // today's store
                if (type.equalsIgnoreCase(Constants.STORE_TYPE_TODAY)) {

                    contentValues
                            .put(StoreBasicColulmns.KEY_BEAT_COVERAGE_ID,
                                    item.getLong(StoreBasicColulmns.KEY_BEAT_COVERAGE_ID));

                    contentValues
                            .put(StoreBasicColulmns.KEY_BEAT_IS_COVERAGE,
                                    item.getBoolean(StoreBasicColulmns.KEY_BEAT_IS_COVERAGE));

                }

                contentValues
                        .put(StoreBasicColulmns.KEY_BEAT_CHINNEL_TYPE,
                                item.isNull(StoreBasicColulmns.KEY_BEAT_CHINNEL_TYPE) ? ""
                                        : item.get(
                                        StoreBasicColulmns.KEY_BEAT_CHINNEL_TYPE)
                                        .toString());

                contentValues
                        .put(StoreBasicColulmns.KEY_BEAT_CONTACT_PERSION,
                                item.isNull(StoreBasicColulmns.KEY_BEAT_CONTACT_PERSION) ? ""
                                        : item.get(
                                        StoreBasicColulmns.KEY_BEAT_CONTACT_PERSION)
                                        .toString());

                contentValues.put(StoreBasicColulmns.KEY_BEAT_EMAIL_ID, item
                        .isNull(StoreBasicColulmns.KEY_BEAT_EMAIL_ID) ? ""
                        : item.get(StoreBasicColulmns.KEY_BEAT_EMAIL_ID)
                        .toString());

                contentValues.put(StoreBasicColulmns.KEY_STORE_ADDRESS, item
                        .isNull(StoreBasicColulmns.KEY_STORE_ADDRESS) ? ""
                        : item.get(StoreBasicColulmns.KEY_STORE_ADDRESS)
                        .toString());

                contentValues
                        .put(StoreBasicColulmns.KEY_BEAT_IS_FREEZE,
                                item.isNull(StoreBasicColulmns.KEY_BEAT_IS_FREEZE) ? false
                                        : item.getBoolean(StoreBasicColulmns.KEY_BEAT_IS_FREEZE));

                contentValues
                        .put(StoreBasicColulmns.KEY_BEAT_FREEZE_LATTITUDE,
                                item.isNull(StoreBasicColulmns.KEY_BEAT_FREEZE_LATTITUDE) ? 0.0
                                        : item.getDouble(StoreBasicColulmns.KEY_BEAT_FREEZE_LATTITUDE));
                contentValues
                        .put(StoreBasicColulmns.KEY_BEAT_FREEZE_LONGITUDE,
                                item.isNull(StoreBasicColulmns.KEY_BEAT_FREEZE_LONGITUDE) ? 0.0
                                        : item.getDouble(StoreBasicColulmns.KEY_BEAT_FREEZE_LONGITUDE));

                contentValues
                        .put(StoreBasicColulmns.KEY_BEAT_MOBILE_NUMBER,
                                item.isNull(StoreBasicColulmns.KEY_BEAT_MOBILE_NUMBER) ? ""
                                        : item.get(
                                        StoreBasicColulmns.KEY_BEAT_MOBILE_NUMBER)
                                        .toString());

                // modify by dwivedi date 05 Aug
                String imagePath = item
                        .isNull(StoreBasicColulmns.KEY_BEAT_PICTURE_FILE_NAME) ? ""
                        : context
                        .getString(R.string.url_file_processor_servicebase)
                        + "id="
                        + item.get(
                        StoreBasicColulmns.KEY_BEAT_PICTURE_FILE_NAME)
                        .toString() + "&type=Store";

                contentValues.put(
                        StoreBasicColulmns.KEY_BEAT_PICTURE_FILE_NAME,
                        imagePath);

                contentValues.put(StoreBasicColulmns.KEY_BEAT_STORE_SIZE, item
                        .isNull(StoreBasicColulmns.KEY_BEAT_STORE_SIZE) ? ""
                        : item.get(StoreBasicColulmns.KEY_BEAT_STORE_SIZE)
                        .toString());

                contentValues
                        .put(StoreBasicColulmns.KEY_BEAT_IS_DISPLAY_COUNTER_SHARE,
                                item.getBoolean(StoreBasicColulmns.KEY_BEAT_IS_DISPLAY_COUNTER_SHARE));
                contentValues
                        .put(StoreBasicColulmns.KEY_BEAT_IS_PLANOGRAM,
                                item.getBoolean(StoreBasicColulmns.KEY_BEAT_IS_PLANOGRAM));

                contentValues.put(StoreBasicColulmns.KEY_BEAT_STORE_CLASS, item
                        .isNull(StoreBasicColulmns.KEY_BEAT_STORE_CLASS) ? ""
                        : item.get(StoreBasicColulmns.KEY_BEAT_STORE_CLASS)
                        .toString());

                try {
                    contentValues.put(StoreBasicColulmns.KEY_IS_DELETED,
                            item.getBoolean(StoreBasicColulmns.KEY_IS_DELETED));
                } catch (Exception e) {
                    contentValues.put(StoreBasicColulmns.KEY_IS_DELETED, 0);
                }

                contentValues.put(StoreBasicColulmns.KEY_BEAT_TYPE, type);

				/*
                 * @ankit Date :4 Dec 2015 Add new feild
				 */
                contentValues.put(StoreBasicColulmns.KEY_LAND_LINE_NUMBER, item
                        .isNull(StoreBasicColulmns.KEY_LAND_LINE_NUMBER) ? ""
                        : item.get(StoreBasicColulmns.KEY_LAND_LINE_NUMBER)
                        .toString());

                contentValues.put(StoreBasicColulmns.KEY_SPC_NAME, item
                        .isNull(StoreBasicColulmns.KEY_SPC_NAME) ? "" : item
                        .get(StoreBasicColulmns.KEY_SPC_NAME).toString());

                contentValues.put(StoreBasicColulmns.KEY_SPC_CATEGORY, item
                        .isNull(StoreBasicColulmns.KEY_SPC_CATEGORY) ? ""
                        : item.get(StoreBasicColulmns.KEY_SPC_CATEGORY)
                        .toString());

                contentValues
                        .put(StoreBasicColulmns.KEY_STORE_MANAGER_NAME,
                                item.isNull(StoreBasicColulmns.KEY_STORE_MANAGER_NAME) ? ""
                                        : item.get(
                                        StoreBasicColulmns.KEY_STORE_MANAGER_NAME)
                                        .toString());

                contentValues.put(StoreBasicColulmns.KEY_SM_MOBILE, item
                        .isNull(StoreBasicColulmns.KEY_SM_MOBILE) ? "" : item
                        .get(StoreBasicColulmns.KEY_SM_MOBILE).toString());

                contentValues.put(StoreBasicColulmns.KEY_SM_EMAIL_ID, item
                        .isNull(StoreBasicColulmns.KEY_SM_EMAIL_ID) ? "" : item
                        .get(StoreBasicColulmns.KEY_SM_EMAIL_ID).toString());

                contentValues
                        .put(StoreBasicColulmns.KEY_ALTERNAMTE_EMAIL_ID,
                                item.isNull(StoreBasicColulmns.KEY_ALTERNAMTE_EMAIL_ID) ? ""
                                        : item.get(
                                        StoreBasicColulmns.KEY_ALTERNAMTE_EMAIL_ID)
                                        .toString());

                contentValues.put(StoreBasicColulmns.KEY_TIN_NUMBER, item
                        .isNull(StoreBasicColulmns.KEY_TIN_NUMBER) ? "" : item
                        .get(StoreBasicColulmns.KEY_TIN_NUMBER).toString());

                contentValues.put(StoreBasicColulmns.KEY_PIN_CODE, item
                        .isNull(StoreBasicColulmns.KEY_PIN_CODE) ? "" : item
                        .get(StoreBasicColulmns.KEY_PIN_CODE).toString());

                // insert store type i'e today/other

                contentValuesArr[i] = contentValues;

            }
        } catch (Exception e) {
            Helper.printStackTrace(e);
        }

        return contentValuesArr;

    }

    public static boolean checkIfEntryAvailable(Cursor cursor) {

        try {

            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);

                if (count > 0) {
                    return true;
                }
            }

            cursor.close();
        } catch (Exception e) {

        }

        return false;
    }

    public static ArrayList<CompetitionProductGroupDto> getCounterShareDisplayShareCompProductGroupListFromCursor(
            Cursor cursor) {
        ArrayList<CompetitionProductGroupDto> prodItems = new ArrayList<CompetitionProductGroupDto>();
        if (cursor.moveToFirst()) {
            do {
                try {
                    CompetitionProductGroupDto obj = new CompetitionProductGroupDto();
                    obj.setCompProductGroupID(""
                            + cursor.getInt(cursor
                            .getColumnIndex(CompetetionProductGroupColumns.KEY_COMPETETION_PRODUCT_GROUP_ID)));
                    obj.setProductGroupCode(cursor.getString(cursor
                            .getColumnIndex(CompetetionProductGroupColumns.KEY_COMPETETION_PRODUCT_GROUP_CODE)));
                    obj.setProductGroupName(cursor.getString(cursor
                            .getColumnIndex(CompetetionProductGroupColumns.KEY_COMPETETION_PRODUCT_GROUP_NAME)));
                    obj.setSuccess(true);

                    prodItems.add(obj);
                } catch (Exception e) {

                    Helper.printStackTrace(e);

                }
            } while (cursor.moveToNext());
        }
        return prodItems;
    }

    public static JSONArray getCounterShareDisplayShareResponseFromCursor(
            Cursor cursor) {
        JSONArray jsonArray = new JSONArray();

        do {
            try {

                JSONObject jsonObject = new JSONObject();
                jsonObject
                        .put("UserRoleID",
                                cursor.getInt(cursor
                                        .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_USER_ROLE_ID)));
                jsonObject
                        .put("SurveyQuestionID",
                                cursor.getInt(cursor
                                        .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_SURVEY_QUESTION_ID)));
                jsonObject
                        .put("CompetitionType",
                                cursor.getInt(cursor
                                        .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE_COMPETITION_TYPE)));
                jsonObject
                        .put("ProductGroupID",
                                cursor.getInt(cursor
                                        .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_SURVEY_QUESTION_PRODUCT_GROUP_ID)));
                jsonObject
                        .put("UserResponse",
                                cursor.getInt(cursor
                                        .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_SURVEY_QUESTION_USER_RESPONSE)));

                jsonObject
                        .put("StoreID",
                                cursor.getInt(cursor
                                        .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_BEAT_STORE_ID)));
                jsonObject
                        .put("SurveyResponseID",
                                cursor.getInt(cursor
                                        .getColumnIndex(ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID)));
                jsonObject
                        .put("UserID",
                                cursor.getInt(cursor
                                        .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_USER_ID)));
                jsonObject
                        .put("CompetitorID",
                                cursor.getInt(cursor
                                        .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_COMETITOR_ID)));
                jsonObject
                        .put("ProductTypeID",
                                cursor.getInt(cursor
                                        .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID)));
                try {
                    jsonObject
                            .put(WebConfig.WebParams.COVERAGEPLANID,
                                    cursor.getInt(cursor
                                            .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID)));
                } catch (Exception e) {
                    Helper.printStackTrace(e);
                }

                jsonArray.put(jsonObject);

            } catch (Exception e) {
                Helper.printStackTrace(e);
            }
        } while (cursor.moveToNext());

        return jsonArray;
    }

    public static List<Module> getUserModuleFromCursor(Cursor mCursor) {

        List<Module> list = null;
        list = new ArrayList<Module>();
        try {
            if (mCursor.moveToFirst()) {

                do {
                    Module module = new Module();

                    module.setModuleID(mCursor.getInt(mCursor
                            .getColumnIndex(UserModuleTableColumns.KEY_MODULE_ID)));

                    module.setModuleCode(mCursor.getInt(mCursor
                            .getColumnIndex(UserModuleTableColumns.KEY_MODULE_CODE)));
                    module.setIconName(mCursor.getString(mCursor
                            .getColumnIndex(UserModuleTableColumns.KEY_MODULE_ICON_NAME)));
                    module.setIsMandatory(mCursor.getInt((mCursor
                            .getColumnIndex(UserModuleTableColumns.KEY_IS_MENDATORY))) == 1 ? true
                            : false);
                    module.setName(mCursor.getString(mCursor
                            .getColumnIndex(UserModuleTableColumns.KEY_NAME)));
                    module.setParentModuleID(mCursor.getInt(mCursor
                            .getColumnIndex(UserModuleTableColumns.KEY_MODULE_PARENT_MODULE_ID)));
                    module.setQuestionType(mCursor.getInt((mCursor
                            .getColumnIndex(UserModuleTableColumns.KEY_MODULE_IS_QUESTION_MODULE))) == 1 ? true
                            : false);
                    module.setSequence(mCursor.getInt(mCursor
                            .getColumnIndex(UserModuleTableColumns.KEY_MODULE_SEQUENCE)));
                    module.setStoreWise(mCursor.getInt((mCursor
                            .getColumnIndex(UserModuleTableColumns.KEY_MODULE_IS_STORE_WISE))) == 1 ? true
                            : false);

                    int activtyID = mCursor
                            .getInt((mCursor
                                    .getColumnIndex(ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID)));

                    if (activtyID != 0) {
                        module.setActivityIDAvailable(true);
                    } else {
                        module.setActivityIDAvailable(false);
                    }
                    module.setSyncStatus(mCursor.getInt(mCursor
                            .getColumnIndex(ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS)));

                    list.add(module);
                } while (mCursor.moveToNext());
            }

        } catch (Exception e) {
        }

        return list;

    }

    public static int getNumberOfStoresWithSubmittedData(Cursor mCursor) {
        // TODO Auto-generated method stub
        int count = 0;
        try {

            if (mCursor.moveToFirst()) {
                count = mCursor.getInt(0);
            }
            mCursor.close();
        } catch (Exception e) {

        }

        return count;
    }

    public static ArrayList<P1CategoryList> getProductCatagoryP1(Cursor cursor) {
        // TODO Auto-generated method stub

        ArrayList<P1CategoryList> prodList = new ArrayList<P1CategoryList>();
        if (cursor.moveToFirst()) {
            do {
                try {
                    P1CategoryList prod = new P1CategoryList();

                    prod.setProductTypeName(cursor.getString(cursor
                            .getColumnIndex(ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_NAME)));
                    prod.setProductTypeID(String.valueOf(cursor.getInt(cursor
                            .getColumnIndex(ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID))));
                    prod.setProductTypeCode(cursor.getString(cursor
                            .getColumnIndex(ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_CODE)));
                    prodList.add(prod);

                } catch (Exception e) {
                    Helper.printLog("getP1ProductList", "" + e.getMessage());
                    Helper.printStackTrace(e);
                    ;
                }

            } while (cursor.moveToNext());

        }
        return prodList;
    }

    public static ArrayList<P2CategoryList> getProductCatagoryP2(Cursor cursor) {
        // TODO Auto-generated method stub
        ArrayList<P2CategoryList> prodList = new ArrayList<P2CategoryList>();

        if (cursor.moveToFirst()) {
            do {
                try {
                    P2CategoryList prod = new P2CategoryList();
                    prod.setProductGroupCode(cursor.getString(cursor
                            .getColumnIndex(ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_CODE)));
                    prod.setProductGroupName(cursor.getString(cursor
                            .getColumnIndex(ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_NAME)));
                    prod.setProductGroupID(String.valueOf(cursor.getInt(cursor
                            .getColumnIndex(ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_ID))));
                    prodList.add(prod);
                } catch (Exception e) {
                    Helper.printLog("getP2ProductList", "" + e.getMessage());

                }

            } while (cursor.moveToNext());
        }

        return prodList;
    }

    public static JSONArray getOrderResposeJSONFormCursor(Cursor cursor,
                                                          boolean isProductPriceNeeded) {
        // TODO Auto-generated method stub
        JSONArray jsonArray = new JSONArray();

        if (cursor != null && cursor.moveToFirst()) {

            int coverageID;
            do {
                try {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject
                            .put(OrderResponseMasterTableColumns.KEY_USER_ROLE_ID,
                                    cursor.getInt(cursor
                                            .getColumnIndex(OrderResponseMasterTableColumns.KEY_USER_ROLE_ID)));
                    jsonObject
                            .put(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_QUANTITY,
                                    cursor.getInt(cursor
                                            .getColumnIndex(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_QUANTITY)));
                    jsonObject
                            .put(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDERNO,
                                    cursor.getString(cursor
                                            .getColumnIndex(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDERNO)));
                    jsonObject
                            .put(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_ID,
                                    cursor.getInt(cursor
                                            .getColumnIndex(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_ID)));
                    jsonObject
                            .put(OrderResponseMasterTableColumns.KEY_BEAT_STORE_ID,
                                    cursor.getInt(cursor
                                            .getColumnIndex(OrderResponseMasterTableColumns.KEY_BEAT_STORE_ID)));

                    jsonObject
                            .put(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_ID,
                                    cursor.getInt(cursor
                                            .getColumnIndex(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_ID)));
                    jsonObject
                            .put(ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID,
                                    cursor.getInt(cursor
                                            .getColumnIndex(ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID)));
                    jsonObject
                            .put(OrderResponseMasterTableColumns.KEY_USER_ID,
                                    cursor.getInt(cursor
                                            .getColumnIndex(OrderResponseMasterTableColumns.KEY_USER_ID)));
                    jsonObject
                            .put(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDER_BOOKING_TYPE,
                                    cursor.getInt(cursor
                                            .getColumnIndex(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDER_BOOKING_TYPE)));

                    jsonObject
                            .put(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID,
                                    cursor.getInt(cursor
                                            .getColumnIndex(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID)));

                    jsonObject
                            .put(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDER_BOOKING_ID,
                                    cursor.getInt(cursor
                                            .getColumnIndex(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDER_BOOKING_ID)));

                    jsonObject
                            .put(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_CAEGORY_ID,
                                    cursor.getInt(cursor

                                            .getColumnIndex(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_CAEGORY_ID)));

                    coverageID = cursor
                            .getInt(cursor
                                    .getColumnIndex(OrderResponseMasterTableColumns.KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID));

                    // wrap coverage id in json string only if coverage id
                    // not equals to zero
                    if (coverageID != 0) {
                        jsonObject
                                .put(OrderResponseMasterTableColumns.KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID,
                                        coverageID);
                    }

                    if (isProductPriceNeeded) {
                        jsonObject
                                .put(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_PRICE,
                                        cursor.getDouble(cursor
                                                .getColumnIndex(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_PRICE)));

                        jsonObject
                                .put(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_SKU_CODE,
                                        cursor.getString(cursor
                                                .getColumnIndex(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_SKU_CODE)));

                    }

                    jsonArray.put(jsonObject);

                } catch (Exception e) {
                    Helper.printStackTrace(e);

                }
            } while (cursor.moveToNext());

            cursor.close();
        }
        return jsonArray;
    }

    public static ArrayList<SKUProductList> getSKUProductList(Cursor cursor) {

        ArrayList<SKUProductList> prodList = new ArrayList<SKUProductList>();

        if (cursor.moveToFirst()) {
            do {
                try {
                    SKUProductList prod = new SKUProductList();
                    prod.setProductID(String.valueOf(cursor.getInt(cursor
                            .getColumnIndex(ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_ID))));
                    prod.setSKUCode(cursor.getString(cursor
                            .getColumnIndex(ProductMasterTableColumns.KEY_PRODUCT_MASTER_SKU_CODE)));
                    prod.setDealerPrice(cursor.getString(cursor
                            .getColumnIndex(ProductMasterTableColumns.KEY_PRODUCT_MASTER_DEALER_PRICE)));
                    prod.setProductCategoryID(String.valueOf(cursor.getInt(cursor
                            .getColumnIndex(ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_CAEGORY_ID))));
                    prod.setProductTypeID(String.valueOf(cursor.getInt(cursor
                            .getColumnIndex(ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID))));
                    prod.setProductGroupID(String.valueOf(cursor.getInt(cursor
                            .getColumnIndex(ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_ID))));

                    prodList.add(prod);
                } catch (Exception e) {
                    Helper.printLog("getSKUProductList", "" + e.getMessage());
                    Helper.printStackTrace(e);
                    ;
                }

            } while (cursor.moveToNext());

        }
        return prodList;

    }

    public static List<String> getOtherStoreCitiesfromCursor(Cursor cursor) {
        List<String> listCities = new ArrayList<String>();

        if (cursor.moveToFirst()) {
            do {
                String city = cursor.getString(cursor
                        .getColumnIndex(StoreBasicColulmns.KEY_BEAT_CITY));
                listCities.add(city);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return listCities;
    }

    public static StorePerformanceModel getStorePerformanceFromCursor(
            Cursor cursor) {
        StorePerformanceModel storePerformanceModel = null;
        try {

            if (cursor.moveToFirst() == false) {
                return storePerformanceModel;
            }

            storePerformanceModel = new StorePerformanceModel();

            storePerformanceModel.setAch(cursor.getFloat(cursor
                    .getColumnIndex(StorePerformaceColumns.KEY_BEAT_ACH)));

            storePerformanceModel
                    .setACMTDPurchase(cursor.getFloat(cursor
                            .getColumnIndex(StorePerformaceColumns.KEY_BEAT_ACMTD_PURCHASE)));

            storePerformanceModel
                    .setACMTDSale(cursor.getFloat(cursor
                            .getColumnIndex(StorePerformaceColumns.KEY_BEAT_ACMTD_SALE)));

            storePerformanceModel
                    .setAVMTDPurchase(cursor.getFloat(cursor
                            .getColumnIndex(StorePerformaceColumns.KEY_BEAT_AVMTD_PURCHASE)));

            storePerformanceModel
                    .setAVMTDPurchase(cursor.getFloat(cursor
                            .getColumnIndex(StorePerformaceColumns.KEY_BEAT_AVMTD_SALE)));

            storePerformanceModel
                    .setAVLMTDSale(cursor.getFloat(cursor
                            .getColumnIndex(StorePerformaceColumns.KEY_BEAT_AVLMTD_SALE)));

            storePerformanceModel
                    .setAVLMTDPurchase(cursor.getFloat(cursor
                            .getColumnIndex(StorePerformaceColumns.KEY_BEAT_AVLMTD_PURCHASE)));

            storePerformanceModel
                    .setHALMTDSale(cursor.getFloat(cursor
                            .getColumnIndex(StorePerformaceColumns.KEY_BEAT_HALMTD_SALE)));

            storePerformanceModel
                    .setHALMTDPurchase(cursor.getFloat(cursor
                            .getColumnIndex(StorePerformaceColumns.KEY_BEAT_HALMTD_PURCHASE)));

            storePerformanceModel
                    .setACLMTDSale(cursor.getFloat(cursor
                            .getColumnIndex(StorePerformaceColumns.KEY_BEAT_ACLMTD_SALE)));

            storePerformanceModel
                    .setACLMTDPurchase(cursor.getFloat(cursor
                            .getColumnIndex(StorePerformaceColumns.KEY_BEAT_ACLMTD_PURCHASE)));

            storePerformanceModel
                    .setHAMTDPurchase(cursor.getFloat(cursor
                            .getColumnIndex(StorePerformaceColumns.KEY_BEAT_HAMTD_PURCHASE)));

            storePerformanceModel
                    .setHAMTDSale(cursor.getFloat(cursor
                            .getColumnIndex(StorePerformaceColumns.KEY_BEAT_HAMTD_SALE)));

            storePerformanceModel
                    .setLastVisitedDate(cursor.getString(cursor
                            .getColumnIndex(StorePerformaceColumns.KEY_BEAT_LAST_VISITED_DATE)));

            storePerformanceModel.setTarget(cursor.getInt(cursor
                    .getColumnIndex(StorePerformaceColumns.KEY_BEAT_TARGET)));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return storePerformanceModel;
    }

    public static ContentValues getContentValuePerformance(JSONObject jobj) {
        ContentValues mContentValues = null;

        try {
            mContentValues = new ContentValues();

            mContentValues
                    .put(StorePerformaceColumns.KEY_BEAT_ACH, (float) jobj
                            .getDouble(StorePerformaceColumns.KEY_BEAT_ACH));
            mContentValues
                    .put(StorePerformaceColumns.KEY_BEAT_ACMTD_PURCHASE,
                            (float) jobj
                                    .getDouble(StorePerformaceColumns.KEY_BEAT_ACMTD_PURCHASE));
            mContentValues
                    .put(StorePerformaceColumns.KEY_BEAT_ACMTD_SALE,
                            (float) jobj
                                    .getDouble(StorePerformaceColumns.KEY_BEAT_ACMTD_SALE));
            mContentValues
                    .put(StorePerformaceColumns.KEY_BEAT_AVMTD_PURCHASE,
                            (float) jobj
                                    .getDouble(StorePerformaceColumns.KEY_BEAT_AVMTD_PURCHASE));
            mContentValues
                    .put(StorePerformaceColumns.KEY_BEAT_AVMTD_SALE,
                            (float) jobj
                                    .getDouble(StorePerformaceColumns.KEY_BEAT_AVMTD_SALE));

            mContentValues
                    .put(StorePerformaceColumns.KEY_BEAT_AVLMTD_SALE,
                            (float) jobj
                                    .getDouble(StorePerformaceColumns.KEY_BEAT_AVLMTD_SALE));
            mContentValues
                    .put(StorePerformaceColumns.KEY_BEAT_AVLMTD_PURCHASE,
                            (float) jobj
                                    .getDouble(StorePerformaceColumns.KEY_BEAT_AVLMTD_PURCHASE));
            mContentValues
                    .put(StorePerformaceColumns.KEY_BEAT_HALMTD_SALE,
                            (float) jobj
                                    .getDouble(StorePerformaceColumns.KEY_BEAT_HALMTD_SALE));
            mContentValues
                    .put(StorePerformaceColumns.KEY_BEAT_HALMTD_PURCHASE,
                            (float) jobj
                                    .getDouble(StorePerformaceColumns.KEY_BEAT_HALMTD_PURCHASE));
            mContentValues
                    .put(StorePerformaceColumns.KEY_BEAT_ACLMTD_SALE,
                            (float) jobj
                                    .getDouble(StorePerformaceColumns.KEY_BEAT_ACLMTD_SALE));
            mContentValues
                    .put(StorePerformaceColumns.KEY_BEAT_ACLMTD_PURCHASE,
                            (float) jobj
                                    .getDouble(StorePerformaceColumns.KEY_BEAT_ACLMTD_PURCHASE));

            mContentValues
                    .put(StorePerformaceColumns.KEY_BEAT_HAMTD_PURCHASE,
                            (float) jobj
                                    .getDouble(StorePerformaceColumns.KEY_BEAT_HAMTD_PURCHASE));
            mContentValues
                    .put(StorePerformaceColumns.KEY_BEAT_HAMTD_SALE,
                            (float) jobj
                                    .getDouble(StorePerformaceColumns.KEY_BEAT_HAMTD_SALE));

            mContentValues.put(
                    StorePerformaceColumns.KEY_BEAT_LAST_VISITED_DATE,
                    jobj.get(StorePerformaceColumns.KEY_BEAT_LAST_VISITED_DATE)
                            .toString());

            mContentValues.put(StorePerformaceColumns.KEY_BEAT_TARGET,
                    jobj.getInt(StorePerformaceColumns.KEY_BEAT_TARGET));

            mContentValues.put(StorePerformaceColumns.KEY_BEAT_STORE_ID,
                    jobj.getLong(StorePerformaceColumns.KEY_BEAT_STORE_ID));
        } catch (JSONException e) {

            e.printStackTrace();
        }

        return mContentValues;

    }

    public static ContentValues[] getContentValuePerformanceArray(
            JSONArray jArr, long storeID) {
        ContentValues mContentValues = null;
        JSONObject jobj = null;
        ContentValues arrValues[] = new ContentValues[jArr.length()];

        try {

            for (int i = 0; i < jArr.length(); i++) {
                jobj = jArr.getJSONObject(i);

                mContentValues = new ContentValues();

                mContentValues.put(StorePerformaceColumns.KEY_BEAT_STORE_ID,
                        storeID);

                mContentValues
                        .put(StorePerformaceColumns.KEY_BRANCH,
                                jobj.isNull(StorePerformaceColumns.KEY_BRANCH) ? ""
                                        : jobj.getString(StorePerformaceColumns.KEY_BRANCH));

                mContentValues
                        .put(StorePerformaceColumns.KEY_CHANNEL_TYPE,
                                jobj.isNull(StorePerformaceColumns.KEY_CHANNEL_TYPE) ? ""
                                        : jobj.getString(StorePerformaceColumns.KEY_CHANNEL_TYPE));

                mContentValues
                        .put(StorePerformaceColumns.KEY_OUTLET_PERFORMANCE_ID,
                                jobj.getInt(StorePerformaceColumns.KEY_OUTLET_PERFORMANCE_ID));

                mContentValues
                        .put(StorePerformaceColumns.KEY_PRODUCT_GROUP,
                                jobj.isNull(StorePerformaceColumns.KEY_PRODUCT_GROUP) ? ""
                                        : jobj.getString(StorePerformaceColumns.KEY_PRODUCT_GROUP));

                mContentValues
                        .put(StorePerformaceColumns.KEY_PRODUCT_TYPE,
                                jobj.isNull(StorePerformaceColumns.KEY_PRODUCT_TYPE) ? ""
                                        : jobj.getString(StorePerformaceColumns.KEY_PRODUCT_TYPE));

                mContentValues
                        .put(StorePerformaceColumns.KEY_REGION,
                                jobj.isNull(StorePerformaceColumns.KEY_REGION) ? ""
                                        : jobj.getString(StorePerformaceColumns.KEY_REGION));

                mContentValues
                        .put(StorePerformaceColumns.KEY_SALES_TYPE,
                                jobj.isNull(StorePerformaceColumns.KEY_SALES_TYPE) ? ""
                                        : jobj.getString(StorePerformaceColumns.KEY_SALES_TYPE));

                mContentValues
                        .put(StorePerformaceColumns.KEY_TRANSACTION_TYPE,
                                jobj.isNull(StorePerformaceColumns.KEY_TRANSACTION_TYPE) ? ""
                                        : jobj.getString(StorePerformaceColumns.KEY_TRANSACTION_TYPE));

                mContentValues.put(StorePerformaceColumns.KEY_VALUE,
                        jobj.getDouble(StorePerformaceColumns.KEY_VALUE));

                mContentValues.put(StorePerformaceColumns.KEY_VOLUME,
                        jobj.getInt(StorePerformaceColumns.KEY_VOLUME));

                arrValues[i] = mContentValues;

            }

        } catch (JSONException e) {

            e.printStackTrace();
        }

        return arrValues;

    }

    public static boolean isStorePerformanceExist(Cursor cursor) {
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            if (count != 0) {
                return true;
            }
        }

        return false;
    }

    public static ContentValues[] getContentValueMSSCategory(
            JSONArray jArrayFeedbackCatagory) {

        int count = jArrayFeedbackCatagory.length();
        ContentValues[] categoryContentValues = new ContentValues[count];
        ContentValues categoryValue;
        for (int i = 0; i < count; i++) {
            try {
                JSONObject itemJson = jArrayFeedbackCatagory.getJSONObject(i);
                categoryValue = new ContentValues();
                categoryValue
                        .put(FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_ID,
                                itemJson.getInt(FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_ID));
                categoryValue
                        .put(FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_TEAM_ID,
                                itemJson.getInt(FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_TEAM_ID));
                categoryValue
                        .put(FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_NAME,
                                itemJson.getString(FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_NAME));
                categoryValue
                        .put(FeedbackCategoryMasterColumns.KEY_IS_DELETED,
                                itemJson.getBoolean(FeedbackCategoryMasterColumns.KEY_IS_DELETED));

                categoryContentValues[i] = categoryValue;
            } catch (Exception e) {
                Helper.printStackTrace(e);
            }
        }

        return categoryContentValues;
    }

    public static ContentValues[] getContentValueMSSTeam(
            JSONArray jArrayFeedbackTeam) {

        int count = jArrayFeedbackTeam.length();
        ContentValues[] teamContentValues = new ContentValues[count];
        ContentValues teamValue;
        for (int i = 0; i < count; i++) {
            try {
                JSONObject itemJson = jArrayFeedbackTeam.getJSONObject(i);

                teamValue = new ContentValues();
                teamValue.put(TeamMasterColumns.KEY_TEAM_MASTER_ID,
                        itemJson.getInt(TeamMasterColumns.KEY_TEAM_MASTER_ID));
                teamValue.put(TeamMasterColumns.KEY_TEAM_MASTER_NAME, itemJson
                        .getString(TeamMasterColumns.KEY_TEAM_MASTER_NAME));
                teamValue.put(TeamMasterColumns.KEY_IS_DELETED,
                        itemJson.getBoolean(TeamMasterColumns.KEY_IS_DELETED));

                teamContentValues[i] = teamValue;

            } catch (Exception e) {
                Helper.printStackTrace(e);
            }

        }

        return teamContentValues;
    }

    public static ContentValues[] getContentValueMSSType(
            JSONArray jArrayFeedbackType) {

        int count = jArrayFeedbackType.length();
        ContentValues[] typeContentValues = new ContentValues[count];
        ContentValues typeValue;

        for (int i = 0; i < count; i++) {

            try {
                typeValue = new ContentValues();
                JSONObject itemObject = jArrayFeedbackType.getJSONObject(i);
                typeValue
                        .put(FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_ID,
                                itemObject
                                        .getInt(FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_ID));
                typeValue
                        .put(FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_NAME,
                                itemObject
                                        .getString(FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_NAME));
                typeValue
                        .put(FeedbackTypeMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_ID,
                                itemObject
                                        .getInt(FeedbackTypeMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_ID));

                typeValue
                        .put(FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_SAMPLE_IMAGE_NAME,
                                itemObject
                                        .getString(FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_SAMPLE_IMAGE_NAME));
                typeValue
                        .put(FeedbackTypeMasterColumns.KEY_IS_DELETED,
                                itemObject
                                        .getBoolean(FeedbackTypeMasterColumns.KEY_IS_DELETED));

                typeContentValues[i] = typeValue;

            } catch (Exception e) {
                Helper.printStackTrace(e);
            }

        }

        return typeContentValues;
    }

    public static ContentValues[] getContentValueMSSStatus(
            JSONArray jArrayFeedbackStatus) {

        int count = jArrayFeedbackStatus.length();
        ContentValues statusContentValues[] = new ContentValues[count];
        ContentValues statusValue;
        for (int i = 0; i < count; i++) {

            try {
                statusValue = new ContentValues();
                JSONObject itemObject = jArrayFeedbackStatus.getJSONObject(i);

                statusValue
                        .put(FeedbackStatusMasterColumns.KEY_FEEDBACK_STATUS_MASTER_ID,
                                itemObject
                                        .getInt(FeedbackStatusMasterColumns.KEY_FEEDBACK_STATUS_MASTER_ID));
                statusValue
                        .put(FeedbackStatusMasterColumns.KEY_FEEDBACK_STATUS_MASTER_NAME,
                                itemObject
                                        .getString(FeedbackStatusMasterColumns.KEY_FEEDBACK_STATUS_MASTER_NAME));

                statusValue
                        .put(FeedbackStatusMasterColumns.KEY_IS_DELETED,
                                itemObject
                                        .getBoolean(FeedbackStatusMasterColumns.KEY_IS_DELETED));

                statusContentValues[i] = statusValue;

            } catch (Exception e) {
                Helper.printStackTrace(e);
            }
        }

        return statusContentValues;
    }

    public static List<ContentValues[]> getContentValuesFMS(
            JSONObject jsonDataRoot) {
        List<ContentValues[]> list = null;
        try {
            list = new ArrayList<ContentValues[]>();

            JSONArray jArrayStatus = jsonDataRoot.getJSONArray("Status");
            int count = jArrayStatus.length();
            ContentValues statusContentValues[] = new ContentValues[count];
            ContentValues statusValue;
            for (int i = 0; i < count; i++) {

                try {
                    statusValue = new ContentValues();
                    JSONObject itemObject = jArrayStatus.getJSONObject(i);

                    statusValue
                            .put(FeedbackStatusMasterColumns.KEY_FEEDBACK_STATUS_MASTER_ID,
                                    itemObject
                                            .getInt(FeedbackStatusMasterColumns.KEY_FEEDBACK_STATUS_MASTER_ID));
                    statusValue
                            .put(FeedbackStatusMasterColumns.KEY_FEEDBACK_STATUS_MASTER_NAME,
                                    itemObject
                                            .getString(FeedbackStatusMasterColumns.KEY_FEEDBACK_STATUS_MASTER_NAME));

                    statusValue
                            .put(FeedbackStatusMasterColumns.KEY_IS_DELETED,
                                    itemObject
                                            .getBoolean(FeedbackStatusMasterColumns.KEY_IS_DELETED));

                    statusContentValues[i] = statusValue;

                } catch (Exception e) {
                    Helper.printStackTrace(e);
                }
            }

            JSONArray jArrayTeam = jsonDataRoot.getJSONArray("Teams");

            count = jArrayTeam.length();
            ContentValues teamContentValues[] = new ContentValues[count];
            ContentValues teamValue;
            for (int i = 0; i < count; i++) {
                try {
                    JSONObject itemJson = jArrayTeam.getJSONObject(i);

                    teamValue = new ContentValues();
                    teamValue
                            .put(TeamMasterColumns.KEY_TEAM_MASTER_ID,
                                    itemJson.getInt(TeamMasterColumns.KEY_TEAM_MASTER_ID));
                    teamValue
                            .put(TeamMasterColumns.KEY_TEAM_MASTER_NAME,
                                    itemJson.getString(TeamMasterColumns.KEY_TEAM_MASTER_NAME));
                    teamValue.put(TeamMasterColumns.KEY_IS_DELETED, itemJson
                            .getBoolean(TeamMasterColumns.KEY_IS_DELETED));

                    teamContentValues[i] = teamValue;

                } catch (Exception e) {
                    Helper.printStackTrace(e);
                }

            }

            JSONArray jArrayFeedbackCatagory = jsonDataRoot
                    .getJSONArray("FeedbackCategories");
            count = jArrayFeedbackCatagory.length();
            ContentValues categoryContentValues[] = new ContentValues[count];
            ContentValues categoryValue;
            for (int i = 0; i < count; i++) {
                try {
                    JSONObject itemJson = jArrayFeedbackCatagory
                            .getJSONObject(i);
                    categoryValue = new ContentValues();
                    categoryValue
                            .put(FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_ID,
                                    itemJson.getInt(FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_ID));
                    categoryValue
                            .put(FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_TEAM_ID,
                                    itemJson.getInt(FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_TEAM_ID));
                    categoryValue
                            .put(FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_NAME,
                                    itemJson.getString(FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_NAME));
                    categoryValue
                            .put(FeedbackCategoryMasterColumns.KEY_IS_DELETED,
                                    itemJson.getBoolean(FeedbackCategoryMasterColumns.KEY_IS_DELETED));

                    categoryContentValues[i] = categoryValue;
                } catch (Exception e) {
                    Helper.printStackTrace(e);
                }
            }
            JSONArray jArrayType = jsonDataRoot.getJSONArray("FeedbackTypes");

            count = jArrayType.length();

            ContentValues typeContentValues[] = new ContentValues[count];
            ContentValues typeValue;

            for (int i = 0; i < count; i++) {

                try {
                    typeValue = new ContentValues();
                    JSONObject itemObject = jArrayType.getJSONObject(i);
                    typeValue
                            .put(FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_ID,
                                    itemObject
                                            .getInt(FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_ID));
                    typeValue
                            .put(FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_NAME,
                                    itemObject
                                            .getString(FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_NAME));
                    typeValue
                            .put(FeedbackTypeMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_ID,
                                    itemObject
                                            .getInt(FeedbackTypeMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_ID));

                    typeValue
                            .put(FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_SAMPLE_IMAGE_NAME,
                                    itemObject
                                            .getString(FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_SAMPLE_IMAGE_NAME));
                    typeValue
                            .put(FeedbackTypeMasterColumns.KEY_IS_DELETED,
                                    itemObject
                                            .getBoolean(FeedbackTypeMasterColumns.KEY_IS_DELETED));

                    typeContentValues[i] = typeValue;

                } catch (Exception e) {
                    Helper.printStackTrace(e);
                }

            }

            list.add(statusContentValues);
            list.add(teamContentValues);
            list.add(categoryContentValues);
            list.add(typeContentValues);

        } catch (JSONException e) {

            e.printStackTrace();
        }

        return list;

    }

    public static List<TeamMasterModel> getFMSTeamsFromCursor(Cursor cursor) {

        List<TeamMasterModel> teams = new ArrayList<TeamMasterModel>();

        try {

            if (cursor.moveToFirst()) {

                TeamMasterModel team;
                do {
                    team = new TeamMasterModel();

                    String teamName = cursor
                            .getString(cursor
                                    .getColumnIndex(TeamMasterColumns.KEY_TEAM_MASTER_NAME));
                    int teamID = cursor
                            .getInt(cursor
                                    .getColumnIndex(TeamMasterColumns.KEY_TEAM_MASTER_ID));

                    team.setTeamName(teamName);
                    team.setTeamID(teamID);

                    teams.add(team);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {

        }

        return teams;
    }

    public static List<FeedbackCategoryMasterModel> getFMSCategoriesFromCursor(
            Cursor cursor) {
        List<FeedbackCategoryMasterModel> feedbackCategories = new ArrayList<FeedbackCategoryMasterModel>();

        try {

            if (cursor.moveToFirst()) {

                FeedbackCategoryMasterModel feedbackCategory;
                do {
                    feedbackCategory = new FeedbackCategoryMasterModel();

                    String categoryName = cursor
                            .getString(cursor
                                    .getColumnIndex(FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_NAME));
                    int categoryID = cursor
                            .getInt(cursor
                                    .getColumnIndex(FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_ID));

                    feedbackCategory.setFeedbackCatName(categoryName);
                    feedbackCategory.setFeedbackCatID(categoryID);

                    feedbackCategories.add(feedbackCategory);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {

        }

        return feedbackCategories;
    }

    public static List<FeedbackTypeMasterModel> getFMSTypeFromCursor(
            Cursor cursor) {
        List<FeedbackTypeMasterModel> feedbackTypes = new ArrayList<FeedbackTypeMasterModel>();

        try {

            FeedbackTypeMasterModel feedbackType;
            do {
                feedbackType = new FeedbackTypeMasterModel();

                String feedbackTypeName = cursor
                        .getString(cursor
                                .getColumnIndex(FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_NAME));
                int feedbackTypeID = cursor
                        .getInt(cursor
                                .getColumnIndex(FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_ID));

                String sampleImageName = cursor
                        .getString(cursor
                                .getColumnIndex(FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_SAMPLE_IMAGE_NAME));

                feedbackType.setFeedbackTypeName(feedbackTypeName);
                feedbackType.setFeedbackTypeID(feedbackTypeID);
                feedbackType.setSampleImageName(sampleImageName);

                feedbackTypes.add(feedbackType);
            } while (cursor.moveToNext());

        } catch (Exception e) {

        }
        return feedbackTypes;
    }

    public static List<UserFeedbackValues> getUserFeedbackStoreWiseFromCursor(
            Cursor cursor) {
        List<UserFeedbackValues> userFeedbacks = new ArrayList<UserFeedbackValues>();

        UserFeedbackValues userFeedbackValues;
        do {
            userFeedbackValues = new UserFeedbackValues();

            userFeedbackValues.setTeamName(cursor.getString(cursor
                    .getColumnIndex(TeamMasterColumns.KEY_TEAM_MASTER_NAME)));
            userFeedbackValues
                    .setFeedbackCatName(cursor.getString(cursor
                            .getColumnIndex(FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_NAME)));
            userFeedbackValues
                    .setFeedbackTypeName(cursor.getString(cursor
                            .getColumnIndex(FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_NAME)));

            try {
                userFeedbackValues.setRowId(cursor.getLong(cursor
                        .getColumnIndex(UserFeedbackMasterColumns._ID)));
            } catch (Exception e) {
                userFeedbackValues.setRowId(-1);
            }

            userFeedbacks.add(userFeedbackValues);
        } while (cursor.moveToNext());

        return userFeedbacks;

    }

    public static List<UserFeedback> getUserFeedbackAllFromCursor(Cursor cursor) {
        List<UserFeedback> userFeedbacks = new ArrayList<UserFeedback>();

        try {

            UserFeedback userFeedback;
            if (cursor.moveToFirst()) {
                do {
                    userFeedback = new UserFeedback();

                    int teamID = cursor
                            .getInt(cursor
                                    .getColumnIndex(UserFeedbackMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_TEAM_ID));
                    int catID = cursor
                            .getInt(cursor
                                    .getColumnIndex(UserFeedbackMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_ID));
                    int typeID = cursor
                            .getInt(cursor
                                    .getColumnIndex(UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_TYPE_ID));

                    int storeID = cursor
                            .getInt(cursor
                                    .getColumnIndex(UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_STORE_ID));
                    String remarks = cursor
                            .getString(cursor
                                    .getColumnIndex(UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_REMARK));
                    String imagePath = cursor
                            .getString(cursor
                                    .getColumnIndex(UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_IMAGE_PATH));

                    userFeedback.setTeamID(teamID);
                    userFeedback.setFeedbackCatID(catID);
                    userFeedback.setFeedbackTypeID(typeID);
                    userFeedback.setRemark(remarks);
                    userFeedback.setStoreId(storeID);

                    if (imagePath != null) {
                        userFeedback.setImagePath(imagePath);
                    }

                    userFeedbacks.add(userFeedback);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return userFeedbacks;

    }

    public static List<FMSStatusDataModal> getFeedbackStatusFromCursor(
            Cursor cursor) {
        List<FMSStatusDataModal> dataModals = new ArrayList<FMSStatusDataModal>();
        try {

            if (cursor.moveToFirst()) {

                do {

                    String statusName = cursor
                            .getString(cursor
                                    .getColumnIndex(FeedbackStatusMasterColumns.KEY_FEEDBACK_STATUS_MASTER_NAME));
                    int statusId = cursor
                            .getInt(cursor
                                    .getColumnIndex(FeedbackStatusMasterColumns.KEY_FEEDBACK_STATUS_MASTER_ID));
                    dataModals
                            .add(new FMSStatusDataModal(statusId, statusName));

                } while (cursor.moveToNext());

            }

            cursor.close();
        } catch (Exception e) {
            Helper.printStackTrace(e);
        }

        return dataModals;
    }

    public static String makePlaceholders(int len) {
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

    public static ContentValues[] getInitialValueForDownloadData() {

        ContentValues[] arr_values = new ContentValues[13];
        // for A
        ContentValues initialValues_A = new ContentValues();
        initialValues_A.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
                "Products");
        initialValues_A
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
                        "GetProductList");
        initialValues_A.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
                DownloadCode.PRODUCT_LIST);
        initialValues_A
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
                        false);
        initialValues_A
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
                        false);
        arr_values[0] = initialValues_A;

        // for B
        ContentValues initialValues_B = new ContentValues();
        initialValues_B.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
                "PaymentModes");
        initialValues_B
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
                        "GetPaymentModes");
        initialValues_B.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
                DownloadCode.PAYMENT_MODE);
        initialValues_B
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
                        false);
        initialValues_B
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
                        false);
        arr_values[1] = initialValues_B;

        // for C
        ContentValues initialValues_C = new ContentValues();
        initialValues_C.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
                "Competitors");
        initialValues_C
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
                        "GetCompetitors");
        initialValues_C.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
                DownloadCode.COMPETITOR);
        initialValues_C
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
                        false);
        initialValues_C
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
                        false);
        arr_values[2] = initialValues_C;

        // for D
        ContentValues initialValues_D = new ContentValues();
        initialValues_D.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
                "Product Category");
        initialValues_D
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
                        "GetCompetitionProductGroup");
        initialValues_D.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
                DownloadCode.COMPETITION_PRODUCT_GROUP);
        initialValues_D
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
                        false);
        initialValues_D
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
                        false);

        arr_values[3] = initialValues_D;
        // for E
        ContentValues initialValues_E = new ContentValues();
        initialValues_E.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
                "Stores");
        initialValues_E
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
                        "DealersListBasedOnCity");
        initialValues_E.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
                DownloadCode.OTHER_BEAT);
        initialValues_E
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
                        false);
        initialValues_E
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
                        false);
        arr_values[4] = initialValues_E;
        // for F
        ContentValues initialValues_F = new ContentValues();
        initialValues_F.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
                "Questions");
        initialValues_F
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
                        "GetSurveyQuestions");
        initialValues_F.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
                DownloadCode.SURVEY_QUESTIONS);
        initialValues_F
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
                        false);
        initialValues_F
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
                        false);
        arr_values[5] = initialValues_F;
        // for G
        ContentValues initialValues_G = new ContentValues();
        initialValues_G.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
                "Planogram Class");
        initialValues_G
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
                        "GetPlanogramClassMasters");
        initialValues_G.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
                DownloadCode.PLANOGRAM_CLASS);
        initialValues_G
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
                        false);
        initialValues_G
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
                        false);
        arr_values[6] = initialValues_G;
        // for H
        ContentValues initialValues_H = new ContentValues();
        initialValues_H.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
                "Planogram Product");
        initialValues_H
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
                        "GetPlanogramProductMasters");
        initialValues_H.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
                DownloadCode.PLANOGRAM_PRODUCT);
        initialValues_H
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
                        false);
        initialValues_H
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
                        false);
        arr_values[7] = initialValues_H;
        // for I
        ContentValues initialValues_I = new ContentValues();
        initialValues_I.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
                "Competitor Product Mapping");
        initialValues_I
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
                        "GetCompetitorProductGroupMapping");
        initialValues_I.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
                DownloadCode.COMPETITOR_PRODUCT_GROUP_MAPPING);
        initialValues_I
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
                        false);
        initialValues_I
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
                        false);

        arr_values[8] = initialValues_I;
        // for J
        ContentValues initialValues_J = new ContentValues();
        initialValues_J.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
                "MSS");
        initialValues_J
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
                        "GetFMSMasters");
        initialValues_J.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
                DownloadCode.FMS);
        initialValues_J
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
                        false);
        initialValues_J
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
                        false);
        arr_values[9] = initialValues_J;
        // for K
        ContentValues initialValues_K = new ContentValues();
        initialValues_K.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
                "EOL");
        initialValues_K
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
                        "GetEOLSchemes");
        initialValues_K.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
                DownloadCode.EOL_SCHEMES);
        initialValues_K
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
                        false);
        initialValues_K
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
                        false);

        arr_values[10] = initialValues_K;
        // for L
        ContentValues initialValues_L = new ContentValues();
        initialValues_L.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
                "RACE MASTER");
        initialValues_L
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
                        "GetRaceMasters");
        initialValues_L.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
                DownloadCode.RACE_MASTER);
        initialValues_L
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
                        false);
        initialValues_L
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
                        false);
        arr_values[11] = initialValues_L;

        // for N
        ContentValues initialValues_N = new ContentValues();
        initialValues_N.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_NAME,
                "RACE PRODUCT");
        initialValues_N
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_SERVICE_NAME,
                        "GetRaceProductMasters");
        initialValues_N.put(
                DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
                DownloadCode.RACE_PRODUCT);
        initialValues_N
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
                        false);
        initialValues_N
                .put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED,
                        false);
        arr_values[12] = initialValues_N;

        return arr_values;
    }

    public static ArrayList<Character> getDownloadableDataCodesFromCursor(
            Cursor cursor) {
        ArrayList<Character> al_download_data_code = new ArrayList<Character>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    try {

                        al_download_data_code
                                .add(cursor
                                        .getString(
                                                cursor.getColumnIndex(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE))
                                        .charAt(0));
                    } catch (Exception e) {

                        Helper.printStackTrace(e);
                    }

                } while (cursor.moveToNext());
            }

        } catch (SQLException e) {
            Helper.printStackTrace(e);
        } catch (Exception e) {
            Helper.printStackTrace(e);
        } finally {
            cursor.close();
        }

        return al_download_data_code;

    }

    public static LinkedHashMap<Character, String> getDownloadableDataCodesFromCursor1(
            Cursor cursor) {
        LinkedHashMap<Character, String> map = new LinkedHashMap<Character, String>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    try {

                        map.put(cursor
                                        .getString(
                                                cursor.getColumnIndex(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE))
                                        .charAt(0),
                                cursor.getString(cursor
                                        .getColumnIndex(DownloadDataMasterColumns.KEY_MODIFIED_DATE_TIME_STAMP)));

                    } catch (Exception e) {

                        Helper.printStackTrace(e);
                    }

                } while (cursor.moveToNext());
            }

        } catch (SQLException e) {
            Helper.printStackTrace(e);
        } catch (Exception e) {
            Helper.printStackTrace(e);
        } finally {
            cursor.close();
        }

        return map;

    }

    public static ArrayList<PaymentModes> getPaymentModesFromCursor(
            Cursor cursor) {

        ArrayList<PaymentModes> prodList = new ArrayList<PaymentModes>();

        do {
            try {
                PaymentModes prod = new PaymentModes();
                prod.setPaymentModeID(String.valueOf(cursor.getInt(cursor
                        .getColumnIndex(PaymentModeMasterTableColumns.KEY_PAYMENT_MODE_ID))));
                prod.setModeName(cursor.getString(cursor
                        .getColumnIndex(PaymentModeMasterTableColumns.KEY_PAYMENT_MODE_NAME)));
                prodList.add(prod);
            } catch (SQLException e) {
                Helper.printLog("getPaymentModeList", "" + e.getMessage());
                Helper.printStackTrace(e);
                ;
            }

        } while (cursor.moveToNext());

        return prodList;
    }

    public static JSONArray getCollectionResponseFromCursor(Context context,
                                                            Cursor cursor) {
        // TODO Auto-generated method stub
        JSONArray elements = new JSONArray();
        try {
            if (cursor.moveToFirst()) {
                do {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put(
                            "UserRoleID",
                            cursor.getInt(cursor
                                    .getColumnIndex(CollectionResponseTableColumns.KEY_USER_ROLE_ID)));

                    if (!Helper.isNullOrEmpty(Helper.getStringValuefromPrefs(
                            context, SharedPreferencesKey.PREF_COVERAGEID))) {
                        jsonObj.put(
                                "CoverageID",
                                cursor.getInt(cursor
                                        .getColumnIndex(CollectionResponseTableColumns.KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID)));
                    }

                    jsonObj.put(
                            "TransactionID",
                            cursor.getString(cursor
                                    .getColumnIndex(CollectionResponseTableColumns.KEY_COLLECTION_TRANSACTION_ID)));
                    jsonObj.put(
                            "Comments",
                            cursor.getString(cursor
                                    .getColumnIndex(CollectionResponseTableColumns.KEY_COLLECTION_COMMENTS)));

                    jsonObj.put(
                            "Amount",
                            cursor.getInt(cursor
                                    .getColumnIndex(CollectionResponseTableColumns.KEY_COLLECTION_AMOUNT)));
                    jsonObj.put(
                            "PaymentModeID",
                            cursor.getInt(cursor
                                    .getColumnIndex(CollectionResponseTableColumns.KEY_PAYMENT_MODE_ID)));
                    jsonObj.put(
                            "PaymentModeName",
                            cursor.getString(cursor
                                    .getColumnIndex(PaymentModeMasterTableColumns.KEY_PAYMENT_MODE_NAME)));

                    jsonObj.put(
                            "StoreID",
                            cursor.getInt(cursor
                                    .getColumnIndex(CollectionResponseTableColumns.KEY_BEAT_STORE_ID)));
                    jsonObj.put(
                            "SurveyResponseID",
                            cursor.getInt(cursor
                                    .getColumnIndex(ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID)));
                    jsonObj.put(
                            "UserID",
                            cursor.getInt(cursor
                                    .getColumnIndex(CollectionResponseTableColumns.KEY_USER_ID)));
                    jsonObj.put(
                            "PaymentDate",
                            cursor.getString(cursor
                                    .getColumnIndex(CollectionResponseTableColumns.KEY_COLLECTION_PAYMENT_DATE)));
                    elements.put(jsonObj);

                } while (cursor.moveToNext());
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return elements;
    }

    public static ContentValues[] getContentValueCollectionResponse(
            Context context, long activityID, JSONArray jsonArrayResponse) {
        int count = jsonArrayResponse.length();

        ContentValues[] values = new ContentValues[count];

        for (int i = 0; i < count; i++) {
            try {
                JSONObject itemJson = jsonArrayResponse.getJSONObject(i);

                ContentValues initialValues = new ContentValues();

                initialValues
                        .put(CollectionResponseTableColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
                                activityID);
                initialValues.put(
                        CollectionResponseTableColumns.KEY_USER_ROLE_ID,
                        itemJson.getInt("UserRoleID"));
                // Condn..
                try {
                    if (!Helper.isNullOrEmpty(Helper.getStringValuefromPrefs(
                            context, SharedPreferencesKey.PREF_COVERAGEID))) {
                        initialValues
                                .put(CollectionResponseTableColumns.KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID,
                                        itemJson.getInt("CoverageID"));
                    }
                } catch (Exception e) {
                    Helper.printStackTrace(e);
                    ;
                }
                initialValues
                        .put(CollectionResponseTableColumns.KEY_COLLECTION_TRANSACTION_ID,
                                itemJson.getString("TransactionID"));
                initialValues.put(
                        CollectionResponseTableColumns.KEY_COLLECTION_COMMENTS,
                        itemJson.getString("Comments"));
                initialValues.put(
                        CollectionResponseTableColumns.KEY_COLLECTION_AMOUNT,
                        itemJson.getInt("Amount"));
                initialValues.put(
                        CollectionResponseTableColumns.KEY_PAYMENT_MODE_ID,
                        itemJson.getInt("PaymentModeID"));
                initialValues.put(
                        CollectionResponseTableColumns.KEY_BEAT_STORE_ID,
                        itemJson.getInt("StoreID"));

                initialValues.put(CollectionResponseTableColumns.KEY_USER_ID,
                        itemJson.getInt("UserID"));
                initialValues
                        .put(CollectionResponseTableColumns.KEY_COLLECTION_PAYMENT_DATE,
                                itemJson.getString("PaymentDate"));
                values[i] = initialValues;
            } catch (Exception e) {
            }
        }

        return values;
    }

    public static ContentValues[] getContentValueCounterShareDisplayShareResponse(
            Context context, long mActivityID, JSONArray jArray) {

        int count = jArray.length();

        ContentValues[] values = new ContentValues[count];
        for (int i = 0; i < count; i++) {
            try {
                ContentValues initialValues = new ContentValues();
                JSONObject jsonObject = jArray.getJSONObject(i);

                initialValues
                        .put(CounterShareDisplayShareResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
                                mActivityID);
                initialValues
                        .put(CounterShareDisplayShareResponseMasterColumns.KEY_USER_ROLE_ID,
                                jsonObject.getInt("UserRoleID"));
                initialValues
                        .put(CounterShareDisplayShareResponseMasterColumns.KEY_SURVEY_QUESTION_ID,
                                jsonObject.getInt("SurveyQuestionID"));
                initialValues
                        .put(CounterShareDisplayShareResponseMasterColumns.KEY_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE_COMPETITION_TYPE,
                                jsonObject.getInt("CompetitionType"));
                initialValues
                        .put(CounterShareDisplayShareResponseMasterColumns.KEY_SURVEY_QUESTION_PRODUCT_GROUP_ID,
                                jsonObject.getInt("ProductGroupID"));
                initialValues
                        .put(CounterShareDisplayShareResponseMasterColumns.KEY_SURVEY_QUESTION_USER_RESPONSE,
                                jsonObject.getInt("UserResponse"));
                initialValues
                        .put(CounterShareDisplayShareResponseMasterColumns.KEY_BEAT_STORE_ID,
                                jsonObject.getInt("StoreID"));

                initialValues
                        .put(CounterShareDisplayShareResponseMasterColumns.KEY_USER_ID,
                                jsonObject.getInt("UserID"));
                initialValues
                        .put(CounterShareDisplayShareResponseMasterColumns.KEY_COMETITOR_ID,
                                jsonObject.getInt("CompetitorID"));

                initialValues
                        .put(CounterShareDisplayShareResponseMasterColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID,
                                jsonObject.getInt("ProductTypeID"));
                try {
                    if (!Helper.isNullOrEmpty(Helper.getStringValuefromPrefs(
                            context, SharedPreferencesKey.PREF_COVERAGEID))) {
                        initialValues
                                .put(CounterShareDisplayShareResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID,
                                        jsonObject.getInt("CoverageID"));
                    }
                } catch (Exception e) {
                    Helper.printStackTrace(e);
                    ;
                }

                values[i] = initialValues;

            } catch (Exception e) {
                Helper.printStackTrace(e);
                ;
            }

        }

        return values;
    }

    public static JSONArray getPlanogramRootResponseFromCursor(Cursor cursorRoot) {
        // TODO Auto-generated method stub

        JSONArray rootJSONArray = new JSONArray();
        if (cursorRoot.moveToFirst()) {

            do {
                JSONObject rootJsonObject = new JSONObject();

                try {
                    int planResponseID = cursorRoot
                            .getInt(cursorRoot

                                    .getColumnIndex(PlanogramResponseMasterTableColumns.KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID));
                    rootJsonObject.put("PlanResponseID", planResponseID);

                    rootJsonObject
                            .put("CompProductGroupID",
                                    cursorRoot.getInt(cursorRoot
                                            .getColumnIndex(PlanogramResponseMasterTableColumns.KEY_COMPETETION_PRODUCT_GROUP_ID)));
                    rootJsonObject
                            .put("Class",
                                    cursorRoot.getString(cursorRoot
                                            .getColumnIndex(PlanogramResponseMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_CLASS)));
                    rootJsonObject
                            .put("ClassID",
                                    cursorRoot.getInt(cursorRoot
                                            .getColumnIndex(PlanogramResponseMasterTableColumns.KEY_PLANOGRAM_CLASS_MASTER_CLASS_ID)));

                    rootJsonObject
                            .put("Adherence",
                                    cursorRoot.getDouble(cursorRoot
                                            .getColumnIndex(PlanogramResponseMasterTableColumns.KEY_PLANOGRAM_RESPONSE_MASTER_ADHERENCE)));

                    rootJSONArray.put(rootJsonObject);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            while (cursorRoot.moveToNext());

        }

        return rootJSONArray;
    }

    public static JSONArray getPlanogramProductResponseFromCursor(
            Cursor cursorProduct) {

        JSONArray jsonArray = new JSONArray();
        if (cursorProduct.moveToFirst()) {

            do {

                try {
                    JSONObject jsonObjectProduct = new JSONObject();

                    int planResponseID = cursorProduct
                            .getInt(cursorProduct

                                    .getColumnIndex(PlanogramProductResponseMasterColumns.KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID));
                    jsonObjectProduct.put("PlanResponseID", planResponseID);

                    jsonObjectProduct
                            .put("ProductCode",
                                    cursorProduct.getString(cursorProduct
                                            .getColumnIndex(PlanogramProductResponseMasterColumns.KEY_PLANOGRAM_PRODUCT_MASTER_PRODUCT_CODE)));
                    jsonObjectProduct
                            .put("IsAvailable",
                                    cursorProduct.getInt(cursorProduct
                                            .getColumnIndex(PlanogramProductResponseMasterColumns.KEY_PLANOGRAM_PRODUCT_RESPONSE_MASTER_IS_AVAILABLE)) == 1 ? true
                                            : false);

                    jsonArray.put(jsonObjectProduct);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } while (cursorProduct.moveToNext());

        }
        return jsonArray;
    }

    public static JSONArray getPlanogramCompetitorsResponseFromCursor(
            Cursor cursorComptitior) {
        JSONArray jsonArray = new JSONArray();
        if (cursorComptitior.moveToFirst()) {

            do {
                try {
                    JSONObject jsonObject = new JSONObject();
                    int planResponseID = cursorComptitior
                            .getInt(cursorComptitior

                                    .getColumnIndex(PlanogramCompitiorResponseMasterColumns.KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID));
                    jsonObject.put("PlanResponseID", planResponseID);

                    jsonObject
                            .put("CompetitorID",
                                    cursorComptitior.getInt(cursorComptitior
                                            .getColumnIndex(PlanogramCompitiorResponseMasterColumns.KEY_COMETITOR_ID)));
                    jsonObject
                            .put("Value",
                                    cursorComptitior.getInt(cursorComptitior
                                            .getColumnIndex(PlanogramCompitiorResponseMasterColumns.KEY_PLANOGRAM_COMPITITORS_RESPONSE_MASTER_PLAN_VALUE)));

                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } while (cursorComptitior.moveToNext());

        }
        return jsonArray;
    }

    public static JSONArray getCounterShareDisplayShareJSONResponseFromCursor(
            Cursor cursor) {

        JSONArray jsonArray = new JSONArray();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject
                            .put("UserRoleID",
                                    cursor.getInt(cursor
                                            .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_USER_ROLE_ID)));
                    jsonObject
                            .put("SurveyQuestionID",
                                    cursor.getInt(cursor
                                            .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_SURVEY_QUESTION_ID)));
                    jsonObject
                            .put("CompetitionType",
                                    cursor.getInt(cursor
                                            .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE_COMPETITION_TYPE)));
                    jsonObject
                            .put("ProductGroupID",
                                    cursor.getInt(cursor
                                            .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_SURVEY_QUESTION_PRODUCT_GROUP_ID)));
                    jsonObject
                            .put("UserResponse",
                                    cursor.getInt(cursor
                                            .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_SURVEY_QUESTION_USER_RESPONSE)));

                    jsonObject
                            .put("StoreID",
                                    cursor.getInt(cursor
                                            .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_BEAT_STORE_ID)));
                    jsonObject
                            .put("SurveyResponseID",
                                    cursor.getInt(cursor
                                            .getColumnIndex(ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID)));
                    jsonObject
                            .put("UserID",
                                    cursor.getInt(cursor
                                            .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_USER_ID)));
                    jsonObject
                            .put("CompetitorID",
                                    cursor.getInt(cursor
                                            .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_COMETITOR_ID)));
                    jsonObject
                            .put("ProductTypeID",
                                    cursor.getInt(cursor
                                            .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID)));
                    try {
                        jsonObject
                                .put(WebConfig.WebParams.COVERAGEPLANID,
                                        cursor.getInt(cursor
                                                .getColumnIndex(CounterShareDisplayShareResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID)));
                    } catch (Exception e) {
                        Helper.printStackTrace(e);
                    }

                    jsonArray.put(jsonObject);

                } catch (Exception e) {
                    Helper.printStackTrace(e);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        return jsonArray;

    }

    public static JSONArray getSurveyQuestionAnswerResponseFromCursor(
            Cursor cursor) {

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {

                try {
                    JSONObject questionJsonObject = new JSONObject();
                    questionJsonObject
                            .put("SurveyResponseID",
                                    cursor.getInt(cursor
                                            .getColumnIndex(ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID)));
                    questionJsonObject
                            .put("SurveyQuestionID",
                                    cursor.getInt(cursor
                                            .getColumnIndex(QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_ID)));

                    questionJsonObject
                            .put("SurveyQuestionSubID",
                                    cursor.getString(cursor
                                            .getColumnIndex(QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_SUB_ID)));
                    questionJsonObject
                            .put("UserResponse",
                                    cursor.getString(cursor
                                            .getColumnIndex(QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_USER_RESPONSE)));
                    questionJsonObject
                            .put("QuestionTypeID",
                                    cursor.getInt(cursor
                                            .getColumnIndex(QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_TYPE_ID)));

                    try {
                        questionJsonObject
                                .put("RepeaterTypeID",
                                        cursor.getInt(cursor
                                                .getColumnIndex(SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATER_TYPE_ID)));
                    } catch (Exception e) {
                        questionJsonObject.put("RepeaterTypeID", 0);
                    }

                    jsonArray.put(questionJsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            while (cursor.moveToNext());
        }

        return jsonArray;

    }

    public static long getActivityID(Cursor cursor) {
        // TODO Auto-generated method stub

        int activityID = -1;
        if (cursor.moveToFirst()) {
            try {

                activityID = cursor
                        .getInt(cursor
                                .getColumnIndex(ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID));

            } catch (Exception e) {
                Helper.printStackTrace(e);
            }
        }

        return activityID;
    }

    public static ContentValues getActivityDataContetnValueArray(
            ActivityDataMasterModel activityData) {
        // TODO Auto-generated method stub

        ContentValues initialValues = null;
        try {

            initialValues = new ContentValues();

            initialValues.put(ActivityDataMasterColumns.KEY_USER_ID,
                    activityData.getUserID());
            initialValues.put(ActivityDataMasterColumns.KEY_BEAT_STORE_ID,
                    activityData.getStoreID());
            initialValues.put(ActivityDataMasterColumns.KEY_MODULE_CODE,
                    activityData.getModuleCode());
            initialValues
                    .put(ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID,
                            activityData.getCoverageID());
            initialValues
                    .put(ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_CREATED_DATE,
                            activityData.getCreatedDate());
            initialValues
                    .put(ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE,
                            activityData.getModifiedDate());
            initialValues
                    .put(ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS,
                            activityData.getSyncStatus());
            initialValues
                    .put(ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID,
                            activityData.getSurveyResponseID());
            /*
			 * activityID = db.insert(TableNames.TABLE_ACTIVITY_DATA_MASTER,
			 * null, initialValues);
			 */

        } catch (Exception e) {
            // TODO: handle exception
        }

        return initialValues;
    }

    public static ContentValues getActivityDataContentValueUpdateStatus(
            int syncStatus) {
        // TODO Auto-generated method stub

        ContentValues contentValues = null;
        try {
            contentValues = new ContentValues();
            contentValues
                    .put(ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS,
                            syncStatus);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return contentValues;
    }

    public static List<String> getStoresFromCity(Cursor cursor) {
        // TODO Auto-generated method stub

        List<String> storeList = new ArrayList<String>();
        if (cursor.moveToFirst()) {

            do {
                String storeName = cursor
                        .getString(cursor
                                .getColumnIndex(StoreBasicColulmns.KEY_BEAT_STORE_NAME));

                String storeCode = cursor
                        .getString(cursor
                                .getColumnIndex(StoreBasicColulmns.KEY_BEAT_STORE_CODE));

                storeName = storeName.concat("[" + storeCode + "]");

                storeList.add(storeName);
            } while (cursor.moveToNext());

        }
        return storeList;
    }

    public static JSONObject getStoreIDByStoreCode(Cursor cursor) {
        // TODO Auto-generated method stub
        JSONObject jObj = null;
        try {
            jObj = new JSONObject();
            cursor.moveToFirst();
            int storeID = cursor.getInt(cursor
                    .getColumnIndex(StoreBasicColulmns.KEY_BEAT_STORE_ID));

            String cityName = cursor.getString(cursor
                    .getColumnIndex(StoreBasicColulmns.KEY_BEAT_CITY));

            jObj.put("StoreID", storeID);
            jObj.put("CityName", cityName);

            cursor.close();

        } catch (Exception e) {
            // TODO: handle exception
        }
        return jObj;

    }

    public static ContentValues[] getBrandCategorymappingMasterContentValuesFromJson(
            JSONArray jsonResponseBrandCategoryMappingArray) {
        ContentValues[] values = null;
        try {

            if (jsonResponseBrandCategoryMappingArray != null) {

                int count = jsonResponseBrandCategoryMappingArray.length();

                values = new ContentValues[count];

                for (int i = 0; i < count; i++) {

                    ContentValues mContentValues = new ContentValues();
                    JSONObject jsonObject = jsonResponseBrandCategoryMappingArray
                            .getJSONObject(i);
                    mContentValues
                            .put(RaceBrandCategoryMappingMasterColumns.KEY_BRAND_CATEGORY_MAPING_ID,
                                    jsonObject.getInt("BrandCategoryMappingID"));
                    mContentValues.put(
                            RaceBrandCategoryMappingMasterColumns.KEY_BRAND_ID,
                            jsonObject.getInt("BrandID"));
                    mContentValues
                            .put(RaceBrandCategoryMappingMasterColumns.KEY_COMP_PRODUCT_GROUP_ID,
                                    jsonObject.getInt("CompProductGroupID"));

                    mContentValues
                            .put(RaceBrandCategoryMappingMasterColumns.KEY_IS_DELETED,
                                    jsonObject
                                            .getBoolean(RaceBrandCategoryMappingMasterColumns.KEY_IS_DELETED));

                    values[i] = mContentValues;

                }

            }

        } catch (JSONException e) {
            Helper.printStackTrace(e);
        }

        return values;

    }

    public static ContentValues[] getBrandMasterContentValuesFromJson(
            JSONArray jsonResponseBrandMasterArray) {
        ContentValues[] values = null;

        try {

            if (jsonResponseBrandMasterArray != null) {

                int count = jsonResponseBrandMasterArray.length();

                values = new ContentValues[count];

                for (int i = 0; i < count; i++) {

                    ContentValues mContentValues = new ContentValues();
                    JSONObject jsonObject = jsonResponseBrandMasterArray
                            .getJSONObject(i);
                    mContentValues.put(RaceBrandMasterColumns.KEY_BRAND_ID,
                            jsonObject.getInt("BrandID"));
                    mContentValues.put(RaceBrandMasterColumns.KEY_BRAND_CODE,
                            jsonObject.getString("BrandCode"));
                    mContentValues.put(RaceBrandMasterColumns.KEY_BRAND_NAME,
                            jsonObject.getString("BrandName"));
                    mContentValues
                            .put(RaceBrandMasterColumns.KEY_IS_DELETED,
                                    jsonObject
                                            .getBoolean(RaceBrandMasterColumns.KEY_IS_DELETED));

                    values[i] = mContentValues;

                }
            }

        } catch (JSONException e) {
            Helper.printStackTrace(e);
        }

        return values;

    }

    public static ContentValues[] getFixtureMasterContentValuesFromJson(
            JSONArray jsonResonceFixtureArray) {
        ContentValues[] values = null;
        try {

            if (jsonResonceFixtureArray != null) {

                int count = jsonResonceFixtureArray.length();

                values = new ContentValues[count];

                for (int i = 0; i < count; i++) {

                    ContentValues mContentValues = new ContentValues();
                    JSONObject jsonObject = jsonResonceFixtureArray
                            .getJSONObject(i);
                    mContentValues.put(RaceFixtureMasterColumns.KEY_FIXTURE_ID,
                            jsonObject.getInt("FixtureID"));
                    mContentValues.put(
                            RaceFixtureMasterColumns.KEY_FIXTURE_CATEGORY_NAME,
                            jsonObject.getString("Category"));
                    mContentValues.put(
                            RaceFixtureMasterColumns.KEY_FIXTURE_SUB_CATEGORY,
                            jsonObject.getString("SubCategory"));

                    // temp insertion data
                    mContentValues
                            .put(RaceFixtureMasterColumns.KEY_FIXTURE_PRODUCT_GROUPS,
                                    jsonObject
                                            .getString(RaceFixtureMasterColumns.KEY_FIXTURE_PRODUCT_GROUPS));
                    mContentValues
                            .put(RaceFixtureMasterColumns.KEY_FIXTURE_IS_COMPETITOR_AVAILBALE,
                                    jsonObject
                                            .getBoolean(RaceFixtureMasterColumns.KEY_FIXTURE_IS_COMPETITOR_AVAILBALE));
                    mContentValues
                            .put(RaceFixtureMasterColumns.KEY_FIXTURE_IS_ROW_AVAILABLE,
                                    jsonObject
                                            .getBoolean(RaceFixtureMasterColumns.KEY_FIXTURE_IS_ROW_AVAILABLE));
                    mContentValues
                            .put(RaceFixtureMasterColumns.KEY_FIXTURE_IS_WALL_AVAILABLE,
                                    jsonObject
                                            .getBoolean(RaceFixtureMasterColumns.KEY_FIXTURE_IS_WALL_AVAILABLE));

                    mContentValues
                            .put(RaceFixtureMasterColumns.KEY_IS_DELETED,
                                    jsonObject
                                            .getBoolean(RaceFixtureMasterColumns.KEY_IS_DELETED));

                    values[i] = mContentValues;

                }

            }

        } catch (JSONException e) {
            Helper.printStackTrace(e);
        }

        return values;

    }

    public static ContentValues[] getPosmMasterContentValuesFromJson(
            JSONArray jsonResoncePOSMArray) {
        ContentValues[] values = null;
        try {

            if (jsonResoncePOSMArray != null) {

                int count = jsonResoncePOSMArray.length();

                values = new ContentValues[count];

                for (int i = 0; i < count; i++) {

                    ContentValues mContentValues = new ContentValues();
                    JSONObject jsonObject = jsonResoncePOSMArray
                            .getJSONObject(i);
                    mContentValues.put(RacePosmMasterColumns.KEY_POSM_ID,
                            jsonObject.getInt("POSMID"));
                    mContentValues.put(
                            RacePosmMasterColumns.KEY_POSM_CODE,
                            jsonObject.isNull("POSMCode") ? "" : jsonObject
                                    .getString("POSMCode"));
                    mContentValues.put(
                            RacePosmMasterColumns.KEY_POSM_NAME,
                            jsonObject.isNull("POSMName") ? "" : jsonObject
                                    .getString("POSMName"));

                    mContentValues.put(
                            RacePosmMasterColumns.KEY_POSM_CATEGORY,
                            jsonObject.isNull("POSMCategory") ? "" : jsonObject
                                    .getString("POSMCategory"));

                    mContentValues.put(
                            RacePosmMasterColumns.KEY_POSM_GROUP,
                            jsonObject.isNull("POSMGroup") ? "" : jsonObject
                                    .getString("POSMGroup"));

                    mContentValues
                            .put(RacePosmMasterColumns.KEY_IS_DELETED,
                                    jsonObject
                                            .isNull(RacePosmMasterColumns.KEY_IS_DELETED));

                    values[i] = mContentValues;

                }

            }

        } catch (JSONException e) {
            Helper.printStackTrace(e);
        }

        return values;

    }

    public static ContentValues[] getBrandProductCategoryMasterContentValuesFromJson(
            JSONArray jsonResonceProductCategoryArray) {
        ContentValues[] values = null;
        try {

            if (jsonResonceProductCategoryArray != null) {

                int count = jsonResonceProductCategoryArray.length();

                values = new ContentValues[count];

                for (int i = 0; i < count; i++) {

                    ContentValues mContentValues = new ContentValues();
                    JSONObject jsonObject = jsonResonceProductCategoryArray
                            .getJSONObject(i);
                    mContentValues
                            .put(RaceBrandProductCategoryMasterColumns.KEY_COMP_PRODUCT_GROUP_ID,
                                    jsonObject.getInt("CompProductGroupID"));
                    mContentValues
                            .put(RaceBrandProductCategoryMasterColumns.KEY_PRODUCT_GROUP_NAME,
                                    jsonObject.getString("ProductGroupName"));
                    mContentValues
                            .put(RaceBrandProductCategoryMasterColumns.KEY_PRODUCT_GROUP_CODE,
                                    jsonObject.getString("ProductGroupCode"));

                    mContentValues
                            .put(RaceBrandProductCategoryMasterColumns.KEY_IS_DELETED,
                                    jsonObject
                                            .getBoolean(RaceBrandProductCategoryMasterColumns.KEY_IS_DELETED));

                    values[i] = mContentValues;

                }

            }

        } catch (JSONException e) {
            Helper.printStackTrace(e);
        }

        return values;

    }

    public static ContentValues[] getPOSMProductMappingMasterContentValuesFromJson(
            JSONArray jsonResoncePosmProductMappingArray) {
        ContentValues[] values = null;
        try {

            if (jsonResoncePosmProductMappingArray != null) {

                int count = jsonResoncePosmProductMappingArray.length();

                values = new ContentValues[count];

                for (int i = 0; i < count; i++) {

                    ContentValues mContentValues = new ContentValues();
                    JSONObject jsonObject = jsonResoncePosmProductMappingArray
                            .getJSONObject(i);
                    mContentValues
                            .put(RacePOSMProductMappingMasterColumns.KEY_POSM_ID,
                                    jsonObject
                                            .getInt(RacePOSMProductMappingMasterColumns.KEY_POSM_ID));
                    mContentValues
                            .put(RacePOSMProductMappingMasterColumns.KEY_POSM_PRODUCT_MAPPING_ID,
                                    jsonObject
                                            .getString(RacePOSMProductMappingMasterColumns.KEY_POSM_PRODUCT_MAPPING_ID));
                    mContentValues
                            .put(RacePOSMProductMappingMasterColumns.KEY_PRODUCT_ID,
                                    jsonObject
                                            .getString(RacePOSMProductMappingMasterColumns.KEY_PRODUCT_ID));
                    mContentValues
                            .put(RacePOSMProductMappingMasterColumns.KEY_IS_DELETED,
                                    jsonObject
                                            .getBoolean(RacePOSMProductMappingMasterColumns.KEY_IS_DELETED));

                    values[i] = mContentValues;

                }

            }

        } catch (JSONException e) {
            Helper.printStackTrace(e);
        }

        return values;

    }

    public static ContentValues[] getProductMasterContentValuesFromJson(
            JSONArray jsonResponseProductArray) {
        ContentValues[] values = null;
        try {

            if (jsonResponseProductArray != null) {

                int count = jsonResponseProductArray.length();

                values = new ContentValues[count];

                for (int i = 0; i < count; i++) {

                    ContentValues mContentValues = new ContentValues();
                    JSONObject jsonObject = jsonResponseProductArray
                            .getJSONObject(i);
                    mContentValues.put(
                            RaceBrandProductMasterColumns.KEY_BRAND_ID,
                            jsonObject.getInt("BrandID"));
                    mContentValues.put(RaceBrandProductMasterColumns.KEY_NAME,
                            jsonObject.getString("ModelName").replace("_", ""));
                    mContentValues.put(
                            RaceBrandProductMasterColumns.KEY_PRODUCT_CATEGORY,
                            jsonObject.getString("ProductCategory"));
                    mContentValues.put(
                            RaceBrandProductMasterColumns.KEY_PRODUCT_GROUP,
                            jsonObject.getString("ProductGroup"));
                    mContentValues.put(
                            RaceBrandProductMasterColumns.KEY_PRODUCT_ID,
                            jsonObject.getInt("ProductID"));
                    mContentValues.put(
                            RaceBrandProductMasterColumns.KEY_PRODUCT_TYPE,
                            jsonObject.getString("ProductType"));

                    mContentValues
                            .put(RaceBrandProductMasterColumns.KEY_IS_DELETED,
                                    jsonObject
                                            .getBoolean(RaceBrandProductMasterColumns.KEY_IS_DELETED));

                    values[i] = mContentValues;

                }

            }

        } catch (JSONException e) {
            Helper.printStackTrace(e);
        }

        return values;

    }

    public static ArrayList<String> getRaceCategoryListFromCursor(Cursor cursor) {

        ArrayList<String> prodList = new ArrayList<String>();
        try {

            if (cursor.moveToFirst()) {
                do {
                    try {

                        // RaceFixtureMasterDTO prod = new
                        // RaceFixtureMasterDTO();
                        prodList.add(cursor.getString(cursor
                                .getColumnIndex(RaceFixtureMasterColumns.KEY_FIXTURE_CATEGORY_NAME)));

                    } catch (SQLException e) {
                        Helper.printLog("Race Product List",
                                "" + e.getMessage());
                        Helper.printStackTrace(e);
                    }

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
        }

        return prodList;
    }

    public static ArrayList<RaceFixtureDataModal> getRaceFixtureDataModelListFromCursor(
            Cursor cursor) {

        ArrayList<RaceFixtureDataModal> fixtureDataModelList = new ArrayList<RaceFixtureDataModal>();
        try {

            if (cursor.moveToFirst()) {
                do {
                    try {

                        RaceFixtureDataModal dateModal = new RaceFixtureDataModal();

                        dateModal
                                .setFixtureID(cursor.getInt(cursor
                                        .getColumnIndex(RaceFixtureMasterColumns.KEY_FIXTURE_ID)));
                        dateModal
                                .setSubCategory(cursor.getString(cursor
                                        .getColumnIndex(RaceFixtureMasterColumns.KEY_FIXTURE_SUB_CATEGORY)));
                        dateModal
                                .setProductGroup(cursor.getString(cursor
                                        .getColumnIndex(RaceFixtureMasterColumns.KEY_FIXTURE_PRODUCT_GROUPS)));
                        dateModal
                                .setCompetitorAvailable(cursor.getInt(cursor
                                        .getColumnIndex(RaceFixtureMasterColumns.KEY_FIXTURE_IS_COMPETITOR_AVAILBALE)) == 1 ? true
                                        : false);

                        dateModal
                                .setRowAvailable(cursor.getInt(cursor
                                        .getColumnIndex(RaceFixtureMasterColumns.KEY_FIXTURE_IS_ROW_AVAILABLE)) == 1 ? true
                                        : false);

                        dateModal
                                .setWallAvailable(cursor.getInt(cursor
                                        .getColumnIndex(RaceFixtureMasterColumns.KEY_FIXTURE_IS_WALL_AVAILABLE)) == 1 ? true
                                        : false);

                        fixtureDataModelList.add(dateModal);

                    } catch (SQLException e) {
                        Helper.printLog("Race Product List",
                                "" + e.getMessage());
                        Helper.printStackTrace(e);
                    }

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
        }
        return fixtureDataModelList;
    }

    public static Map<Integer, String> getRaceBrandFromCursor(Cursor cursor) {
        Map<Integer, String> map = new HashMap<Integer, String>();
        try {

            if (cursor.moveToFirst()) {
                do {
                    try {

                        map.put(cursor
                                        .getInt(cursor
                                                .getColumnIndex(RaceBrandMasterColumns.KEY_BRAND_ID)),
                                cursor.getString(cursor
                                        .getColumnIndex(RaceBrandMasterColumns.KEY_BRAND_NAME)));

                    } catch (SQLException e) {
                        Helper.printLog("Race Product List",
                                "" + e.getMessage());
                        Helper.printStackTrace(e);
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
        }
        return map;
    }

    public static List<String> getRaceProductCategoryListFromCursor(
            Cursor cursor) {
        List<String> strings = new ArrayList<String>();
        try {

            if (cursor.moveToFirst()) {
                do {
                    try {

                        strings.add(cursor.getString(cursor
                                .getColumnIndex(RaceBrandProductMasterColumns.KEY_PRODUCT_CATEGORY)));

                    } catch (SQLException e) {
                        Helper.printLog("Race Product List",
                                "" + e.getMessage());
                        Helper.printStackTrace(e);
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
        }

        return strings;
    }

    public static ArrayList<RaceProductDataModal> getRaceProductListFromCursor(
            Cursor cursor) {
        ArrayList<RaceProductDataModal> prodList = new ArrayList<RaceProductDataModal>();
        try {

            if (cursor.moveToFirst()) {
                do {
                    try {

                        RaceProductDataModal prod = new RaceProductDataModal();
                        prod.setBrandID(cursor.getInt(cursor
                                .getColumnIndex(RaceBrandProductMasterColumns.KEY_BRAND_ID)));
                        prod.setProductName(cursor.getString(cursor
                                .getColumnIndex(RaceBrandProductMasterColumns.KEY_NAME)));
                        prod.setProductCategory(cursor.getString(cursor
                                .getColumnIndex(RaceBrandProductMasterColumns.KEY_PRODUCT_CATEGORY)));
                        prod.setProductGroup(cursor.getString(cursor
                                .getColumnIndex(RaceBrandProductMasterColumns.KEY_PRODUCT_GROUP)));
                        prod.setProductID(cursor.getInt(cursor
                                .getColumnIndex(RaceBrandProductMasterColumns.KEY_PRODUCT_ID)));
                        prod.setProductType(cursor.getString(cursor
                                .getColumnIndex(RaceBrandProductMasterColumns.KEY_PRODUCT_TYPE)));

                        prodList.add(prod);
                    } catch (SQLException e) {
                        Helper.printLog("Race Product List",
                                "" + e.getMessage());
                        Helper.printStackTrace(e);
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
        }
        return prodList;
    }

    public static ArrayList<RacePOSMMasterDTO> getPOSMbyProduct(Cursor cursor) {
        ArrayList<RacePOSMMasterDTO> list = new ArrayList<RacePOSMMasterDTO>();
        try {

            if (cursor.moveToFirst()) {
                do {
                    try {
                        RacePOSMMasterDTO single_data = new RacePOSMMasterDTO();

                        single_data
                                .setPOSMID(cursor.getInt(cursor
                                        .getColumnIndex(RacePosmMasterColumns.KEY_POSM_ID)));
                        single_data
                                .setPOSMName(cursor.getString(cursor
                                        .getColumnIndex(RacePosmMasterColumns.KEY_POSM_NAME)));

                        list.add(single_data);
                    } catch (SQLException e) {
                        Helper.printLog("Race Product List",
                                "" + e.getMessage());
                        Helper.printStackTrace(e);
                    }

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ContentValues getContentValueForRaceProductAuditResponseFromJSON(
            long mActivity, JSONObject jsonResponseProductAudit) {
        ContentValues mContentValues = null;

        try {
            if (jsonResponseProductAudit != null) {

                mContentValues = new ContentValues();

                mContentValues
                        .put(RaceProductAuditResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
                                mActivity);
                mContentValues.put(
                        RaceProductAuditResponseMasterColumns.KEY_FIXTURE_ID,
                        jsonResponseProductAudit.getInt("FixtureId"));
                mContentValues.put(
                        RaceProductAuditResponseMasterColumns.KEY_PRODUCT_ID,
                        jsonResponseProductAudit.getInt("ProductId"));
                mContentValues.put(
                        RaceProductAuditResponseMasterColumns.KEY_WALL_NUMBER,
                        jsonResponseProductAudit.getInt("WallNumber"));
                mContentValues.put(
                        RaceProductAuditResponseMasterColumns.KEY_ROW_NUMBER,
                        jsonResponseProductAudit.getInt("RowNumber"));
                mContentValues.put(
                        RaceProductAuditResponseMasterColumns.KEY_BRAND_ID,
                        jsonResponseProductAudit.getInt("BrandId"));
                mContentValues.put(
                        RaceProductAuditResponseMasterColumns.KEY_TOPPER,
                        jsonResponseProductAudit.getBoolean("BolTopper"));
                mContentValues.put(
                        RaceProductAuditResponseMasterColumns.KEY_SWITCHED_ON,
                        jsonResponseProductAudit.getBoolean("BolOn"));
                mContentValues.put(
                        RaceProductAuditResponseMasterColumns.KEY_PRICE_TAG,
                        jsonResponseProductAudit.getBoolean("BolPriceTag"));

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mContentValues;
    }

    public static ContentValues[] getContentValuesPOSMResponseFromJSON(
            long mActivity, long stockAuditID,
            ArrayList<RacePOSMMasterDTO> posmResponseArray) {
        int count = posmResponseArray.size();
        ArrayList<ContentValues> al_ContentValues = new ArrayList<ContentValues>();

        try {

            for (int i = 0; i < count; i++) {

                RacePOSMMasterDTO modal = posmResponseArray.get(i);

                if (modal.isStickerSelected()) {

                    ContentValues mContentValues = new ContentValues();
                    mContentValues
                            .put(RacePOSMDataResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
                                    mActivity);
                    mContentValues
                            .put(RacePOSMDataResponseMasterColumns.KEY_STOCK_AUDIT_ID,
                                    stockAuditID);
                    mContentValues.put(
                            RacePOSMDataResponseMasterColumns.KEY_POSM_ID,
                            posmResponseArray.get(i).getPOSMID());
                    mContentValues.put(
                            RacePOSMDataResponseMasterColumns.KEY_POSM_TYPE, 1);

                    al_ContentValues.add(mContentValues);

                }

            }

        } catch (Exception e) {
            Helper.printStackTrace(e);
        }

        ContentValues[] arrContentValues = al_ContentValues
                .toArray(new ContentValues[al_ContentValues.size()]);
        return arrContentValues;
    }

    public static RacePoductAuditResponseDTO getProductAuditResponseFromCursor(
            Cursor cursor) {
        RacePoductAuditResponseDTO response = new RacePoductAuditResponseDTO();
        if (cursor.moveToFirst()) {

            try {

                response.activittyID = cursor
                        .getInt(cursor
                                .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID));

                response.fixtureID = cursor
                        .getInt(cursor
                                .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_FIXTURE_ID));

                int productID = cursor
                        .getInt(cursor
                                .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_PRODUCT_ID));

                response.productID = productID;

                response.rowNumber = cursor
                        .getInt(cursor
                                .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_ROW_NUMBER));
                response.wallNumber = cursor
                        .getInt(cursor
                                .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_WALL_NUMBER));

                response.brandID = cursor
                        .getInt(cursor
                                .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_BRAND_ID));

                response.isTopperAvailble = cursor
                        .getInt(cursor
                                .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_TOPPER)) == 1 ? true
                        : false;

                response.isSwithcedOn = cursor
                        .getInt(cursor
                                .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_SWITCHED_ON)) == 1 ? true
                        : false;

                response.isPriceTag = cursor
                        .getInt(cursor
                                .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_PRICE_TAG)) == 1 ? true
                        : false;

                response.category = cursor
                        .getString(cursor
                                .getColumnIndex(RaceFixtureMasterColumns.KEY_FIXTURE_CATEGORY_NAME));

                response.subCategory = cursor
                        .getString(cursor
                                .getColumnIndex(RaceFixtureMasterColumns.KEY_FIXTURE_SUB_CATEGORY));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return response;
    }

    public static ArrayList<RacePOSMMasterDTO> getPosmResponseFormCursor(
            Cursor cursor, int stockAuditID) {
        ArrayList<RacePOSMMasterDTO> al_posm = new ArrayList<RacePOSMMasterDTO>();

        try {
            if (cursor.moveToFirst()) {

                do {

                    RacePOSMMasterDTO posmDataModal = new RacePOSMMasterDTO();

                    posmDataModal
                            .setPOSMID(cursor.getInt(cursor
                                    .getColumnIndex(RacePOSMDataResponseMasterColumns.KEY_POSM_ID)));

                    posmDataModal
                            .setPosmType(cursor.getInt(cursor
                                    .getColumnIndex(RacePOSMDataResponseMasterColumns.KEY_POSM_TYPE)));

                    posmDataModal
                            .setPOSMName(cursor.getString(cursor
                                    .getColumnIndex(RacePosmMasterColumns.KEY_POSM_NAME)));

                    int productAuditID = cursor
                            .getInt(cursor
                                    .getColumnIndex(RacePOSMDataResponseMasterColumns.KEY_STOCK_AUDIT_ID));
                    if (productAuditID == stockAuditID) {
                        posmDataModal.setStickerSelected(true);
                    } else {
                        posmDataModal.setStickerSelected(false);
                    }
                    al_posm.add(posmDataModal);

                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return al_posm;
    }

    public static ContentValues getContentValueUpdateForRaceProductAuditResponseFromJSON(
            JSONObject jsonResponseProductAudit) {
        ContentValues mContentValues = null;

        try {
            if (jsonResponseProductAudit != null) {

                mContentValues = new ContentValues();

                mContentValues.put(
                        RaceProductAuditResponseMasterColumns.KEY_FIXTURE_ID,
                        jsonResponseProductAudit.getInt("FixtureId"));

                mContentValues.put(
                        RaceProductAuditResponseMasterColumns.KEY_WALL_NUMBER,
                        jsonResponseProductAudit.getInt("WallNumber"));
                mContentValues.put(
                        RaceProductAuditResponseMasterColumns.KEY_ROW_NUMBER,
                        jsonResponseProductAudit.getInt("RowNumber"));
                mContentValues.put(
                        RaceProductAuditResponseMasterColumns.KEY_BRAND_ID,
                        jsonResponseProductAudit.getInt("BrandId"));
                mContentValues.put(
                        RaceProductAuditResponseMasterColumns.KEY_TOPPER,
                        jsonResponseProductAudit.getBoolean("BolTopper"));
                mContentValues.put(
                        RaceProductAuditResponseMasterColumns.KEY_SWITCHED_ON,
                        jsonResponseProductAudit.getBoolean("BolOn"));
                mContentValues.put(
                        RaceProductAuditResponseMasterColumns.KEY_PRICE_TAG,
                        jsonResponseProductAudit.getBoolean("BolPriceTag"));

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mContentValues;
    }

    public static ArrayList<RaceProductAddToCartDTO> getProductAuditCartFromCursor(
            Cursor cursor) {

        ArrayList<RaceProductAddToCartDTO> prodList = new ArrayList<RaceProductAddToCartDTO>();
        try {

            if (cursor.moveToFirst()) {
                do {
                    try {

                        RaceProductAddToCartDTO prodCart = new RaceProductAddToCartDTO();
                        prodCart.setProductName(cursor.getString(cursor
                                .getColumnIndex(RaceBrandProductMasterColumns.KEY_NAME)));
                        prodCart.setStockAuditid(cursor.getInt(cursor
                                .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_STOCK_AUDIT_ID)));

						/*
						 * prodCart.wallNumber=(cursor.getInt(cursor
						 * .getColumnIndex(KEY_WALL_NUMBER)));
						 * prodCart.rowNumber=(cursor.getInt(cursor
						 * .getColumnIndex(KEY_ROW_NUMBER)));
						 * prodCart.categoryName=(cursor.getString(cursor
						 * .getColumnIndex(KEY_FIXTURE_CATEGORY_NAME)));
						 */

                        prodList.add(prodCart);

                    } catch (SQLException e) {
                        Helper.printLog("Race Product cart",
                                "" + e.getMessage());
                        Helper.printStackTrace(e);
                    }

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
        }

        return prodList;
    }

    public static HashMap<String, HashMap<Integer, ArrayList<RaceProductAddToCartDTO>>> getProductAuditCartForAVFromCursor(
            Cursor cursor) {

        HashMap<String, HashMap<Integer, ArrayList<RaceProductAddToCartDTO>>> category_map = null;
        try {

            String categoryName;
            if (cursor.moveToFirst()) {

                category_map = new HashMap<String, HashMap<Integer, ArrayList<RaceProductAddToCartDTO>>>();

                do {
                    try {

                        String category = cursor
                                .getString(cursor
                                        .getColumnIndex(RaceFixtureMasterColumns.KEY_FIXTURE_CATEGORY_NAME));

                        int wallNumber = cursor
                                .getInt(cursor
                                        .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_WALL_NUMBER));

                        int rowNumber = cursor
                                .getInt(cursor
                                        .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_ROW_NUMBER));

                        String productName = cursor
                                .getString(cursor
                                        .getColumnIndex(RaceBrandProductMasterColumns.KEY_NAME));

                        int stockAuditID = cursor
                                .getInt(cursor
                                        .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_STOCK_AUDIT_ID));

                        RaceProductAddToCartDTO product = new RaceProductAddToCartDTO();

                        product.setProductName(productName);
                        product.setStockAuditid(stockAuditID);

                        categoryName = category + " " + wallNumber;

                        // if category map contains the category
                        if (category_map.containsKey(categoryName)) {

                            HashMap<Integer, ArrayList<RaceProductAddToCartDTO>> row_map = category_map
                                    .get(categoryName);

                            // if row map contains the row number
                            if (row_map.containsKey(rowNumber)) {
                                ArrayList<RaceProductAddToCartDTO> product_al = row_map
                                        .get(rowNumber);

                                product_al.add(product);

                            }
                            // if row map does not contains the row number
                            else {

                                ArrayList<RaceProductAddToCartDTO> new_product_al = new ArrayList<RaceProductAddToCartDTO>();
                                new_product_al.add(product);

                                row_map.put(rowNumber, new_product_al);

                            }

                        }

                        // if category map does not contains the category
                        else {

                            // create new product array list
                            ArrayList<RaceProductAddToCartDTO> new_product_al = new ArrayList<RaceProductAddToCartDTO>();
                            new_product_al.add(product);

                            // create new row map
                            HashMap<Integer, ArrayList<RaceProductAddToCartDTO>> new_row_map = new HashMap<Integer, ArrayList<RaceProductAddToCartDTO>>();
                            new_row_map.put(rowNumber, new_product_al);

                            // put new category in category map
                            category_map.put(categoryName, new_row_map);
                        }

                    } catch (SQLException e) {
                        Helper.printLog("Race Product cart",
                                "" + e.getMessage());
                        Helper.printStackTrace(e);
                    }

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return category_map;

    }

    public static HashMap<String, HashMap<String, ArrayList<RaceProductAddToCartDTO>>> getProductAuditCartForHAFromCursor(
            Cursor cursor) {

        HashMap<String, HashMap<String, ArrayList<RaceProductAddToCartDTO>>> root_brand_map = null;
        try {

            if (cursor.moveToFirst()) {

                root_brand_map = new HashMap<String, HashMap<String, ArrayList<RaceProductAddToCartDTO>>>();

                do {
                    try {

                        String brand = cursor
                                .getString(cursor
                                        .getColumnIndex(RaceBrandMasterColumns.KEY_BRAND_NAME));

                        String productGroup = cursor
                                .getString(cursor
                                        .getColumnIndex(RaceBrandProductMasterColumns.KEY_PRODUCT_GROUP));

                        String productName = cursor
                                .getString(cursor
                                        .getColumnIndex(RaceBrandProductMasterColumns.KEY_NAME));

                        int stockAuditID = cursor
                                .getInt(cursor
                                        .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_STOCK_AUDIT_ID));

                        RaceProductAddToCartDTO product = new RaceProductAddToCartDTO();

                        product.setProductName(productName);
                        product.setStockAuditid(stockAuditID);

                        // if category map contains the category
                        if (root_brand_map.containsKey(brand)) {

                            HashMap<String, ArrayList<RaceProductAddToCartDTO>> productGroupMap_map = root_brand_map
                                    .get(brand);

                            // if row map contains the row number
                            if (productGroupMap_map.containsKey(productGroup)) {
                                ArrayList<RaceProductAddToCartDTO> product_al = productGroupMap_map
                                        .get(productGroup);

                                product_al.add(product);

                            }
                            // if row map does not contains the row number
                            else {

                                ArrayList<RaceProductAddToCartDTO> new_product_al = new ArrayList<RaceProductAddToCartDTO>();
                                new_product_al.add(product);

                                productGroupMap_map.put(productGroup,
                                        new_product_al);

                            }

                        }

                        // if category map does not contains the category
                        else {

                            // create new product array list
                            ArrayList<RaceProductAddToCartDTO> new_product_al = new ArrayList<RaceProductAddToCartDTO>();
                            new_product_al.add(product);

                            // create new row map
                            HashMap<String, ArrayList<RaceProductAddToCartDTO>> new_product_group_map = new HashMap<String, ArrayList<RaceProductAddToCartDTO>>();
                            new_product_group_map.put(productGroup,
                                    new_product_al);

                            // put new category in category map
                            root_brand_map.put(brand, new_product_group_map);
                        }

                    } catch (SQLException e) {
                        Helper.printLog("Race Product cart",
                                "" + e.getMessage());
                        Helper.printStackTrace(e);
                    }

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return root_brand_map;

    }

    public static JSONArray getJSONArrayRaceAuditResponseFromCursor(
            Cursor cursor) {
        JSONArray rootJOSNArray = new JSONArray();

        int counter = 0;
        if (cursor.moveToFirst()) {
            do {
                try {

                    int stockAuditID = cursor
                            .getInt(cursor
                                    .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_STOCK_AUDIT_ID));

                    if (rootJOSNArray.length() > 0) {
                        JSONObject existingArrayEntry = (JSONObject) rootJOSNArray
                                .get(counter - 1);
                        int existingStockID = existingArrayEntry
                                .getInt(RaceProductAuditResponseMasterColumns.KEY_STOCK_AUDIT_ID);

                        if (existingStockID == stockAuditID) {// means entry
                            // already
                            // present in
                            // array, i'e we
                            // only need to
                            // add posm
                            // entry in posm
                            // array of that
                            // item

                            int posmID = cursor
                                    .getInt(cursor
                                            .getColumnIndex(RacePOSMDataResponseMasterColumns.KEY_POSM_ID));

                            if (posmID != 0) {

                                JSONArray existingPOSMArray = existingArrayEntry
                                        .getJSONArray("StockAuditPOSMResponse");

                                JSONObject jsonObject2 = new JSONObject();

                                jsonObject2
                                        .put(RacePOSMDataResponseMasterColumns.KEY_POSM_ID,
                                                posmID);
                                jsonObject2
                                        .put(RacePOSMDataResponseMasterColumns.KEY_POSM_TYPE,
                                                cursor.getInt(cursor
                                                        .getColumnIndex(RacePOSMDataResponseMasterColumns.KEY_POSM_TYPE)));

                                existingPOSMArray.put(jsonObject2);

                                // break the current loop
                                continue;
                            }

                        }

                    }

                    JSONObject jsonObject = new JSONObject();

                    jsonObject
                            .put(RaceProductAuditResponseMasterColumns.KEY_STOCK_AUDIT_ID,
                                    stockAuditID);

                    jsonObject
                            .put(RaceProductAuditResponseMasterColumns.KEY_PRODUCT_ID,
                                    cursor.getInt(cursor
                                            .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_PRODUCT_ID)));
                    jsonObject
                            .put(RaceProductAuditResponseMasterColumns.KEY_FIXTURE_ID,
                                    cursor.getInt(cursor
                                            .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_FIXTURE_ID)));

                    int rowNumner = cursor
                            .getInt(cursor
                                    .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_ROW_NUMBER));

                    int wallNumber = cursor
                            .getInt(cursor
                                    .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_WALL_NUMBER));

                    int brandID = cursor
                            .getInt(cursor
                                    .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_BRAND_ID));

                    if (rowNumner != -1) {
                        jsonObject
                                .put(RaceProductAuditResponseMasterColumns.KEY_ROW_NUMBER,
                                        rowNumner);
                    }
                    if (wallNumber != -1) {
                        jsonObject
                                .put(RaceProductAuditResponseMasterColumns.KEY_WALL_NUMBER,
                                        wallNumber);
                    }
                    if (brandID != -1) {
                        jsonObject
                                .put(RaceProductAuditResponseMasterColumns.KEY_BRAND_ID,
                                        brandID);
                    }

                    jsonObject
                            .put(RaceProductAuditResponseMasterColumns.KEY_TOPPER,
                                    cursor.getInt(cursor
                                            .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_TOPPER)) == 1 ? true
                                            : false);
                    jsonObject
                            .put(RaceProductAuditResponseMasterColumns.KEY_SWITCHED_ON,
                                    cursor.getInt(cursor
                                            .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_SWITCHED_ON)) == 1 ? true
                                            : false);
                    jsonObject
                            .put(RaceProductAuditResponseMasterColumns.KEY_PRICE_TAG,
                                    cursor.getInt(cursor
                                            .getColumnIndex(RaceProductAuditResponseMasterColumns.KEY_PRICE_TAG)) == 1 ? true
                                            : false);

                    int posmID = cursor
                            .getInt(cursor
                                    .getColumnIndex(RacePOSMDataResponseMasterColumns.KEY_POSM_ID));

                    if (posmID != 0) {

                        JSONArray posmArray = new JSONArray();

                        JSONObject jsonObject2 = new JSONObject();

                        jsonObject2.put(
                                RacePOSMDataResponseMasterColumns.KEY_POSM_ID,
                                posmID);
                        jsonObject2
                                .put(RacePOSMDataResponseMasterColumns.KEY_POSM_TYPE,
                                        cursor.getInt(cursor
                                                .getColumnIndex(RacePOSMDataResponseMasterColumns.KEY_POSM_TYPE)));

                        posmArray.put(jsonObject2);

                        jsonObject.put("StockAuditPOSMResponse", posmArray);
                    }

                    // increment counter
                    counter++;

                    rootJOSNArray.put(jsonObject);

                } catch (SQLException e) {
                    Helper.printLog("Race Product List", "" + e.getMessage());
                    Helper.printStackTrace(e);
                } catch (Exception e) {

                }

            } while (cursor.moveToNext());
        }
        return rootJOSNArray;
    }

    public static ArrayList<EOLSchemeDTO> getEOLSchemeHeaderResponseFromCursor(
            Cursor cursorRoot) {

        ArrayList<EOLSchemeDTO> eolSchemeDTOs = new ArrayList<EOLSchemeDTO>();

        if (cursorRoot.moveToFirst()) {

            do {
                EOLSchemeDTO rootJsonObject = new EOLSchemeDTO();

                int scheemID = cursorRoot
                        .getInt(cursorRoot
                                .getColumnIndex(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_SCHEME_ID));

                rootJsonObject.setSchemeID(scheemID);
                rootJsonObject
                        .setSchemeNumber(cursorRoot.getString(cursorRoot
                                .getColumnIndex(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_SCHEME_NUMBER)));
                rootJsonObject
                        .setSchemeFrom(cursorRoot.getString(cursorRoot
                                .getColumnIndex(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_SCHEME_FROM)));
                rootJsonObject
                        .setSchemeTo(cursorRoot.getString(cursorRoot
                                .getColumnIndex(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_SCHEME_TO)));
                rootJsonObject
                        .setOrderFrom(cursorRoot.getString(cursorRoot
                                .getColumnIndex(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_ORDER_FROM)));
                rootJsonObject
                        .setOrderTo(cursorRoot.getString(cursorRoot
                                .getColumnIndex(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_ORDER_TO)));
                rootJsonObject
                        .setPUMINumber(cursorRoot.getString(cursorRoot
                                .getColumnIndex(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_PUMI_NUMBER)));
                rootJsonObject
                        .setPumiDate(cursorRoot.getString(cursorRoot
                                .getColumnIndex(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_PUMI_DATE)));
                rootJsonObject
                        .setProductType(cursorRoot.getString(cursorRoot
                                .getColumnIndex(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_PRODUCT_TYPE)));
                rootJsonObject
                        .setProductGroup(cursorRoot.getString(cursorRoot
                                .getColumnIndex(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_PRODUCT_GROUP)));
                rootJsonObject
                        .setProductCategory(cursorRoot.getString(cursorRoot
                                .getColumnIndex(EOLSchemeHeaderMasterMasterColumns.KEY_PRODUCT_CATEGORY)));
                rootJsonObject
                        .setCreatedDate(cursorRoot.getString(cursorRoot
                                .getColumnIndex(EOLSchemeHeaderMasterMasterColumns.KEY_ACTIVITY_DATA_MASTER_CREATED_DATE)));
                rootJsonObject
                        .setModifiedDate(cursorRoot.getString(cursorRoot
                                .getColumnIndex(EOLSchemeHeaderMasterMasterColumns.KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE)));

                eolSchemeDTOs.add(rootJsonObject);
            }

            while (cursorRoot.moveToNext());
        }

        return eolSchemeDTOs;

    }

    public static ArrayList<EOLSchemeDetailDTO> getEOLSchemeDetailsOrderResponsFromCursor(
            Cursor cursorSchemeDetails) {
        ArrayList<EOLSchemeDetailDTO> detailDTOs = new ArrayList<EOLSchemeDetailDTO>();
        if (cursorSchemeDetails.moveToFirst()) {

            do {

                EOLSchemeDetailDTO detailDTO = new EOLSchemeDetailDTO();
                try {
                    detailDTO
                            .setSchemeID(cursorSchemeDetails.getInt(cursorSchemeDetails
                                    .getColumnIndex(EOLSchemeDetailsMasterColoums.KEY_EOL_SCHEME_ID)));
                    detailDTO
                            .setBasicModelCode(cursorSchemeDetails.getString(cursorSchemeDetails
                                    .getColumnIndex(EOLSchemeDetailsMasterColoums.KEY_EOL_DETAILS_BASIC_MODEL_CODE)));
                    detailDTO
                            .setUserDefineQuentity(cursorSchemeDetails.getInt(cursorSchemeDetails
                                    .getColumnIndex(EOLOrderBookingResponseMasterColoums.KEY_EOL_DETAILS_ORDER_QUATITY)));
                    detailDTO
                            .setUserDefineSupport(cursorSchemeDetails.getInt(cursorSchemeDetails
                                    .getColumnIndex(EOLOrderBookingResponseMasterColoums.KEY_EOL_DETAILS_ACTUAL_SUPPORT)));

                    detailDTOs.add(detailDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } while (cursorSchemeDetails.moveToNext());

            cursorSchemeDetails.close();

        }

        return detailDTOs;
    }

    public static ContentValues[] getProductsContentValuesFromJson(
            JSONArray productArray) {
        ContentValues[] contentValues = null;
        try {
            if (productArray != null) {
                int productCount = productArray.length();

                contentValues = new ContentValues[productCount];
                for (int i = 0; i < productCount; i++) {
                    JSONObject productObject = productArray.getJSONObject(i);

                    ContentValues mContentValues = new ContentValues();

                    mContentValues
                            .put(ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_ID,
                                    productObject.getInt("ProductID"));
                    mContentValues
                            .put(ProductMasterTableColumns.KEY_PRODUCT_MASTER_BASIC_MODEL_CODE,
                                    productObject.getString("BasicModelCode"));
                    mContentValues
                            .put(ProductMasterTableColumns.KEY_PRODUCT_MASTER_BASIC_MODEL_NAME,
                                    productObject.getString("BasicModelName"));
                    mContentValues
                            .put(ProductMasterTableColumns.KEY_PRODUCT_MASTER_CATEGORY_CODE,
                                    productObject.getString("CategoryCode"));
                    mContentValues
                            .put(ProductMasterTableColumns.KEY_PRODUCT_MASTER_CATEGORY_NAME,
                                    productObject.getString("CategoryName"));
                    mContentValues
                            .put(ProductMasterTableColumns.KEY_PRODUCT_MASTER_DEALER_PRICE,
                                    productObject.get("DealerPrice").toString());
                    mContentValues
                            .put(ProductMasterTableColumns.KEY_PRODUCT_MASTER_MODEL_TYPE_ID,
                                    productObject.getInt("ModelTypeID"));
                    mContentValues
                            .put(ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_CAEGORY_ID,
                                    productObject.getInt("ProductCategoryID"));
                    mContentValues
                            .put(ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_CODE,
                                    productObject.getString("ProductGroupCode"));
                    mContentValues
                            .put(ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_ID,
                                    productObject.getInt("ProductGroupID"));
                    mContentValues
                            .put(ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_NAME,
                                    productObject.getString("ProductGroupName"));
                    mContentValues
                            .put(ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_CODE,
                                    productObject.getString("ProductTypeCode"));
                    mContentValues
                            .put(ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID,
                                    productObject.getInt("ProductTypeID"));
                    mContentValues
                            .put(ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_NAME,
                                    productObject.getString("ProductTypeName"));
                    mContentValues
                            .put(ProductMasterTableColumns.KEY_PRODUCT_MASTER_SKU_CODE,
                                    productObject.getString("SKUCode"));
                    mContentValues
                            .put(ProductMasterTableColumns.KEY_PRODUCT_MASTER_SKU_NAME,
                                    productObject.getString("SKUName"));

                    mContentValues
                            .put(ProductMasterTableColumns.KEY_IS_DELETED,
                                    productObject
                                            .getBoolean(ProductMasterTableColumns.KEY_IS_DELETED));
                    contentValues[i] = mContentValues;

                }

            }

        } catch (SQLException e) {
            Helper.printStackTrace(e);
        } catch (Exception e) {
            Helper.printStackTrace(e);
        }

        return contentValues;
    }

    public static ContentValues[] getsContentValuesPaymentModseFromJson(
            JSONArray paymentModeArray) {
        ContentValues[] contentValues = null;
        try {
            if (paymentModeArray != null) {
                int productCount = paymentModeArray.length();

                contentValues = new ContentValues[productCount];
                for (int i = 0; i < productCount; i++) {
                    JSONObject productObject = paymentModeArray
                            .getJSONObject(i);

                    ContentValues mContentValues = new ContentValues();

                    mContentValues.put(
                            PaymentModeMasterTableColumns.KEY_PAYMENT_MODE_ID,
                            Integer.parseInt(productObject.get("PaymentModeID")
                                    .toString()));

                    mContentValues
                            .put(PaymentModeMasterTableColumns.KEY_PAYMENT_MODE_NAME,
                                    productObject.getString("ModeName"));

                    mContentValues
                            .put(PaymentModeMasterTableColumns.KEY_IS_DELETED,
                                    productObject
                                            .getBoolean(PaymentModeMasterTableColumns.KEY_IS_DELETED));

                    contentValues[i] = mContentValues;

                }

            }

        } catch (SQLException e) {
            Helper.printStackTrace(e);
        } catch (Exception e) {
            Helper.printStackTrace(e);
        }

        return contentValues;
    }

    public static ContentValues[] getContentValuesCompetitorsFromJson(
            JSONArray competitorArray) {
        ContentValues[] contentValues = null;

        try {
            if (competitorArray != null) {
                int productCount = competitorArray.length();

                contentValues = new ContentValues[productCount];
                for (int i = 0; i < productCount; i++) {
                    JSONObject jsonObject = competitorArray.getJSONObject(i);

                    ContentValues mContentValues = new ContentValues();

                    mContentValues.put(CompetitorColumns.KEY_COMETITOR_ID,
                            jsonObject.getInt("CompetitorID"));
                    mContentValues.put(CompetitorColumns.KEY_COMETITOR_CODE,
                            jsonObject.getString("Code"));
                    mContentValues.put(CompetitorColumns.KEY_COMETITOR_NAME,
                            jsonObject.getString("Name"));
                    mContentValues
                            .put(CompetitorColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID,
                                    jsonObject.getInt("ProductTypeID"));

                    mContentValues
                            .put(CompetitorColumns.KEY_IS_DELETED,
                                    jsonObject
                                            .getBoolean(CompetitorColumns.KEY_IS_DELETED));

                    contentValues[i] = mContentValues;

                }

            }

        } catch (SQLException e) {
            Helper.printStackTrace(e);
        } catch (Exception e) {
            Helper.printStackTrace(e);
        }

        return contentValues;

    }

    public static ContentValues[] getContentValuesCompProductGroupFromJson(
            JSONArray compProductGroupArray) {
        ContentValues[] contentValues = null;

        try {
            if (compProductGroupArray != null) {
                int productCount = compProductGroupArray.length();

                contentValues = new ContentValues[productCount];
                for (int i = 0; i < productCount; i++) {

                    JSONObject jobj = compProductGroupArray.getJSONObject(i);

                    ContentValues mContentValue = new ContentValues();

                    mContentValue
                            .put(CompetetionProductGroupColumns.KEY_COMPETETION_PRODUCT_GROUP_ID,
                                    jobj.get("CompProductGroupID").toString());
                    mContentValue
                            .put(CompetetionProductGroupColumns.KEY_COMPETETION_PRODUCT_GROUP_CODE,
                                    jobj.get("ProductGroupCode").toString());
                    mContentValue
                            .put(CompetetionProductGroupColumns.KEY_COMPETETION_PRODUCT_GROUP_NAME,
                                    jobj.get("ProductGroupName").toString());

                    mContentValue
                            .put(CompetetionProductGroupColumns.KEY_IS_DELETED,
                                    jobj.getBoolean(CompetetionProductGroupColumns.KEY_IS_DELETED));

                    contentValues[i] = mContentValue;

                }

            }

        } catch (SQLException e) {
            Helper.printStackTrace(e);
        } catch (Exception e) {
            Helper.printStackTrace(e);
        }

        return contentValues;

    }

    public static ContentValues[] getContentValuesPalnogramClassFromJson(
            JSONArray compProductGroupArray) {
        ContentValues[] contentValues = null;

        try {
            if (compProductGroupArray != null) {
                int productCount = compProductGroupArray.length();

                contentValues = new ContentValues[productCount];
                for (int i = 0; i < productCount; i++) {
                    JSONObject itemJson = compProductGroupArray
                            .getJSONObject(i);

                    ContentValues initialValues = new ContentValues();

                    initialValues
                            .put(PlanogramClassMasterTableColumns.KEY_PLANOGRAM_CLASS_MASTER_CLASS_ID,
                                    itemJson.getInt("ClassID"));
                    initialValues
                            .put(PlanogramClassMasterTableColumns.KEY_PLANOGRAM_CLASS_MASTER_END_RANGE,
                                    itemJson.getInt("EndRange"));
                    initialValues
                            .put(PlanogramClassMasterTableColumns.KEY_PLANOGRAM_CLASS_MASTER_START_RANGE,
                                    itemJson.getInt("StartRange"));
                    initialValues
                            .put(PlanogramClassMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_CLASS,
                                    itemJson.getString("Class"));
                    initialValues
                            .put(PlanogramClassMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_CHANNEL_TYPE,
                                    itemJson.getString("ChannelType"));

                    initialValues
                            .put(PlanogramClassMasterTableColumns.KEY_PLANOGRAM_CLASS_MASTER_COMP_PRODUCT_GROUP_ID,
                                    itemJson.getInt("CompProdGroupID"));

                    initialValues
                            .put(PlanogramClassMasterTableColumns.KEY_IS_DELETED,
                                    itemJson.getBoolean(PlanogramClassMasterTableColumns.KEY_IS_DELETED));

                    contentValues[i] = initialValues;

                }

            }

        } catch (SQLException e) {
            Helper.printStackTrace(e);
        } catch (Exception e) {
            Helper.printStackTrace(e);
        }

        return contentValues;

    }

    public static ContentValues[] getContentValuesPlanogramProduct(
            ArrayList<PlanogramProductDataModal> dataModals) {
        ContentValues[] contentValues = null;

        try {
            if (dataModals != null) {
                int productCount = dataModals.size();

                contentValues = new ContentValues[productCount];
                for (int i = 0; i < productCount; i++) {
                    PlanogramProductDataModal dataModal = dataModals.get(i);

                    ContentValues initialValues = new ContentValues();

                    initialValues
                            .put(PlanogramProductMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_ID,
                                    dataModal.getPlanogramProductMasterID());
                    initialValues
                            .put(PlanogramProductMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_CHANNEL_TYPE,
                                    dataModal.getChannelType());
                    initialValues
                            .put(PlanogramProductMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_CLASS,
                                    dataModal.getClassName());
                    initialValues
                            .put(PlanogramProductMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_COMP_PRODUCT_GROUP,
                                    dataModal.getCompProductGroup());
                    initialValues
                            .put(PlanogramProductMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_PRODUCT_CODE,
                                    dataModal.getProductCode());

                    initialValues.put(
                            PlanogramProductMasterTableColumns.KEY_IS_DELETED,
                            dataModal.isDelected());

                    contentValues[i] = initialValues;

                }

            }

        } catch (SQLException e) {
            Helper.printStackTrace(e);
        } catch (Exception e) {
            Helper.printStackTrace(e);
        }

        return contentValues;

    }

    public static ContentValues[] getContentValuesPlanogramProduct1(
            JSONArray jArray) {
        ContentValues[] contentValues = null;

        try {
            if (jArray != null) {
                int productCount = jArray.length();

                contentValues = new ContentValues[productCount];
                for (int i = 0; i < productCount; i++) {
                    JSONObject dataModal = jArray.getJSONObject(i);

                    ContentValues initialValues = new ContentValues();

                    initialValues
                            .put(PlanogramProductMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_ID,
                                    dataModal
                                            .getInt("PlanogramProductMasterID"));
                    initialValues
                            .put(PlanogramProductMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_CHANNEL_TYPE,
                                    dataModal.getString("ChannelType"));
                    initialValues
                            .put(PlanogramProductMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_CLASS,
                                    dataModal.getString("Class"));
                    initialValues
                            .put(PlanogramProductMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_COMP_PRODUCT_GROUP,
                                    dataModal.getString("CompProductGroup"));
                    initialValues
                            .put(PlanogramProductMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_PRODUCT_CODE,
                                    dataModal.getString("ProductCode"));

                    initialValues.put(
                            PlanogramProductMasterTableColumns.KEY_IS_DELETED,
                            dataModal.getBoolean("IsDeleted"));

                    contentValues[i] = initialValues;

                }

            }

        } catch (SQLException e) {
            Helper.printStackTrace(e);
        } catch (Exception e) {
            Helper.printStackTrace(e);
        }

        return contentValues;

    }

    public static ContentValues[] getContentValuesCompetitorProductGroupMappingFromJson(
            JSONArray compProductGroupArray) {
        ContentValues[] contentValues = null;

        try {
            if (compProductGroupArray != null) {

                int productCount = compProductGroupArray.length();

                contentValues = new ContentValues[productCount];
                for (int i = 0; i < productCount; i++) {
                    JSONObject item = compProductGroupArray.getJSONObject(i);
                    ContentValues contentValue = new ContentValues();
                    contentValue
                            .put(CompetitorProductGroupMappingTableColumns.KEY_COMPETITOR_PRODUCT_GROUP_MAPPING_MAPPING_ID,
                                    item.getInt("MappingID"));
                    contentValue
                            .put(CompetitorProductGroupMappingTableColumns.KEY_COMETITOR_ID,
                                    item.getInt("CompetitorID"));
                    contentValue
                            .put(CompetitorProductGroupMappingTableColumns.KEY_COMPETETION_PRODUCT_GROUP_ID,
                                    item.getInt("CompProductGroupID"));
                    contentValue
                            .put(CompetitorProductGroupMappingTableColumns.KEY_IS_DELETED,
                                    item.getBoolean(CompetitorProductGroupMappingTableColumns.KEY_IS_DELETED));

                    contentValues[i] = contentValue;

                }

            }

        } catch (SQLException e) {
            Helper.printStackTrace(e);
        } catch (Exception e) {
            Helper.printStackTrace(e);
        }

        return contentValues;

    }

    public static ArrayList<ContentValues[]> getContentValuesEOLSchemeDetailAndHeaderFromJson(
            JSONArray jsonResponseJsonArray) {

        ArrayList<ContentValues> headers = new ArrayList<ContentValues>();
        ArrayList<ContentValues> schemeDetails = new ArrayList<ContentValues>();

        ContentValues initialValues;
        ContentValues initialValuesDetails;

        try {

            if (jsonResponseJsonArray != null) {
                int count = jsonResponseJsonArray.length();

                for (int i = 0; i < count; i++) {

                    initialValues = new ContentValues();

                    JSONObject jsonObject = jsonResponseJsonArray
                            .getJSONObject(i);

                    initialValues
                            .put(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_SCHEME_ID,
                                    jsonObject.getInt("SchemeID"));
                    initialValues
                            .put(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_SCHEME_NUMBER,
                                    jsonObject.getString("SchemeNumber"));
                    initialValues
                            .put(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_SCHEME_FROM,
                                    jsonObject.getString("strSchemeFrom"));
                    initialValues
                            .put(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_SCHEME_TO,
                                    jsonObject.getString("strSchemeTo"));
                    initialValues
                            .put(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_SCHEME_FROM,
                                    jsonObject.getString("strSchemeFrom"));
                    initialValues
                            .put(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_ORDER_FROM,
                                    jsonObject.getString("strOrderFrom"));
                    initialValues
                            .put(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_ORDER_TO,
                                    jsonObject.getString("strOrderTo"));
                    initialValues
                            .put(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_PUMI_NUMBER,
                                    jsonObject.getString("PUMINumber"));
                    initialValues
                            .put(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_PUMI_DATE,
                                    jsonObject.getString("strPUMIDate"));
                    initialValues
                            .put(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_PRODUCT_TYPE,
                                    jsonObject.getString("ProductType"));
                    initialValues
                            .put(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_PRODUCT_GROUP,
                                    jsonObject.getString("strOrderTo"));
                    initialValues
                            .put(EOLSchemeHeaderMasterMasterColumns.KEY_PRODUCT_CATEGORY,
                                    jsonObject.getString("ProductCategory"));
                    initialValues
                            .put(EOLSchemeHeaderMasterMasterColumns.KEY_EOL_PRODUCT_GROUP,
                                    jsonObject.getString("ProductGroup"));
                    initialValues
                            .put(EOLSchemeHeaderMasterMasterColumns.KEY_ACTIVITY_DATA_MASTER_CREATED_DATE,
                                    jsonObject.getString("strCreatedDate"));
                    initialValues
                            .put(EOLSchemeHeaderMasterMasterColumns.KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE,
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

                                initialValuesDetails
                                        .put(EOLSchemeDetailsMasterColoums.KEY_EOL_SCHEME_ID,
                                                jsonObjectDetails
                                                        .getInt("SchemeID"));
                                initialValuesDetails
                                        .put(EOLSchemeDetailsMasterColoums.KEY_EOL_DETAILS_BASIC_MODEL_CODE,
                                                jsonObjectDetails
                                                        .getString("BasicModelCode"));
                                initialValuesDetails
                                        .put(EOLSchemeDetailsMasterColoums.KEY_EOL_DETAILS_ORDER_QUATITY,
                                                jsonObjectDetails
                                                        .getInt("Quantity"));
                                initialValuesDetails
                                        .put(EOLSchemeDetailsMasterColoums.KEY_EOL_DETAILS_ACTUAL_SUPPORT,
                                                jsonObjectDetails
                                                        .getInt("Support"));

                                schemeDetails.add(initialValuesDetails);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    headers.add(initialValues);
                }

            }

        } catch (SQLException e) {
            Helper.printStackTrace(e);
        } catch (Exception e) {
            Helper.printStackTrace(e);
        }

        ArrayList<ContentValues[]> listContentValuesArr = new ArrayList<ContentValues[]>();

        listContentValuesArr.add(headers.toArray(new ContentValues[headers
                .size()]));
        listContentValuesArr.add(schemeDetails
                .toArray(new ContentValues[schemeDetails.size()]));

        return listContentValuesArr;

    }

    public static ContentValues[] getEOLOrderBookingResponseContantValue(
            long mActivityID, JSONArray rootJsonArray) throws JSONException {

        int countProduct = rootJsonArray.length();
        ContentValues[] initialValues = new ContentValues[countProduct];
        for (int i = 0; i < countProduct; i++) {
            JSONObject rootJsonItem = rootJsonArray.getJSONObject(i);

            if (rootJsonItem != null) {
                ContentValues initialValue = new ContentValues();
                initialValue
                        .put(EOLOrderBookingResponseMasterColoums.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
                                mActivityID);
                initialValue.put(
                        EOLOrderBookingResponseMasterColoums.KEY_BEAT_STORE_ID,
                        rootJsonItem.getInt("StoreID"));
                initialValue.put(
                        EOLOrderBookingResponseMasterColoums.KEY_EOL_SCHEME_ID,
                        rootJsonItem.getInt("SchemeID"));
                initialValue
                        .put(EOLOrderBookingResponseMasterColoums.KEY_EOL_DETAILS_BASIC_MODEL_CODE,
                                rootJsonItem.getString("BasicModelCode"));
                initialValue
                        .put(EOLOrderBookingResponseMasterColoums.KEY_EOL_DETAILS_ORDER_QUATITY,
                                rootJsonItem.getInt("OrderQuantity"));
                initialValue
                        .put(EOLOrderBookingResponseMasterColoums.KEY_EOL_DETAILS_ACTUAL_SUPPORT,
                                rootJsonItem.getInt("ActualSupport"));

                initialValues[i] = initialValue;
            }
        }

        return initialValues;
    }

    public static ContentValues[] getContetnValueUserModulesFromJSON(
            JSONArray jArray) {

        int count = jArray.length();

        ContentValues[] mContentValueArray = new ContentValues[count];

        for (int i = 0; i < count; i++) {

            try {

                ContentValues contentValues = new ContentValues();
                JSONObject jsonObject = jArray.getJSONObject(i);
                contentValues
                        .put(DatabaseConstants.UserModuleTableColumns.KEY_MODULE_CODE,
                                jsonObject
                                        .getInt(DatabaseConstants.UserModuleTableColumns.KEY_MODULE_CODE));

                try {
                    contentValues
                            .put(DatabaseConstants.UserModuleTableColumns.KEY_MODULE_ID,
                                    jsonObject
                                            .getInt(DatabaseConstants.UserModuleTableColumns.KEY_MODULE_ID));

                } catch (Exception e) {
                    contentValues
                            .put(DatabaseConstants.UserModuleTableColumns.KEY_MODULE_ID,
                                    0);
                }

                try {
                    contentValues
                            .put(DatabaseConstants.UserModuleTableColumns.KEY_MODULE_PARENT_MODULE_ID,
                                    jsonObject
                                            .getInt(DatabaseConstants.UserModuleTableColumns.KEY_MODULE_PARENT_MODULE_ID));

                } catch (JSONException e1) {

                    contentValues
                            .put(DatabaseConstants.UserModuleTableColumns.KEY_MODULE_PARENT_MODULE_ID,
                                    0);
                }

                contentValues
                        .put(DatabaseConstants.UserModuleTableColumns.KEY_IS_MENDATORY,
                                jsonObject
                                        .getBoolean(DatabaseConstants.UserModuleTableColumns.KEY_IS_MENDATORY));

                contentValues
                        .put(DatabaseConstants.UserModuleTableColumns.KEY_MODULE_IS_QUESTION_MODULE,
                                jsonObject
                                        .getBoolean(DatabaseConstants.UserModuleTableColumns.KEY_MODULE_IS_QUESTION_MODULE));

                try {

                    contentValues
                            .put(DatabaseConstants.UserModuleTableColumns.KEY_MODULE_IS_STORE_WISE,
                                    jsonObject
                                            .getBoolean(DatabaseConstants.UserModuleTableColumns.KEY_MODULE_IS_STORE_WISE));

                } catch (Exception e) {
                    // TODO: handle exception
                    contentValues
                            .put(DatabaseConstants.UserModuleTableColumns.KEY_MODULE_IS_STORE_WISE,
                                    0);

                }

                contentValues
                        .put(DatabaseConstants.UserModuleTableColumns.KEY_NAME,
                                jsonObject
                                        .getString(DatabaseConstants.UserModuleTableColumns.KEY_NAME));

                contentValues
                        .put(DatabaseConstants.UserModuleTableColumns.KEY_MODULE_ICON_NAME,
                                jsonObject
                                        .getString(DatabaseConstants.UserModuleTableColumns.KEY_MODULE_ICON_NAME));

                contentValues
                        .put(DatabaseConstants.UserModuleTableColumns.KEY_MODULE_SEQUENCE,
                                jsonObject
                                        .getInt(DatabaseConstants.UserModuleTableColumns.KEY_MODULE_SEQUENCE));

                contentValues
                        .put(DatabaseConstants.UserModuleTableColumns.KEY_MODULE_TYPE,
                                jsonObject
                                        .getInt(DatabaseConstants.UserModuleTableColumns.KEY_MODULE_TYPE));

                try {
                    contentValues
                            .put(DatabaseConstants.UserModuleTableColumns.KEY_IS_DELETED,
                                    jsonObject
                                            .getBoolean(DatabaseConstants.UserModuleTableColumns.KEY_IS_DELETED));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    contentValues
                            .put(DatabaseConstants.UserModuleTableColumns.KEY_IS_DELETED,
                                    false);
                }

                mContentValueArray[i] = contentValues;

            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        return mContentValueArray;
    }

    public static boolean getDownloadStatusFromCursor(Cursor cursor) {

        try {
            if (cursor.moveToFirst()) {

                try {

                    int status = cursor
                            .getInt(cursor
                                    .getColumnIndex(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS));

                    if (status == 1) {
                        return true;
                    }

                } catch (Exception e) {

                    Helper.printStackTrace(e);
                }
            }

        } catch (SQLException e) {
            Helper.printStackTrace(e);
        } catch (Exception e) {
            Helper.printStackTrace(e);
        } finally {
            cursor.close();
        }

        return false;

    }

    public static Map<Integer, Integer> getDownloadStatusMapFromCursor(
            Cursor cursor) {

        Map<Integer, Integer> map = null;

        try {
            if (cursor.moveToFirst()) {

                map = new HashMap<Integer, Integer>();
                try {

                    do {
                        int moduleCode = cursor
                                .getInt(cursor
                                        .getColumnIndex(DownloadDataModuleMappingTableColums.KEY_MODULE_CODE));

                        // download status of single data code for the module
                        int status = cursor
                                .getInt(cursor
                                        .getColumnIndex(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS));

                        if (map.containsKey(moduleCode)) {
                            // Multiplied(if more than one datacode exist
                            // previously) download status of the datacodes for
                            // the module
                            int lastStatus = map.get(moduleCode);

                            map.put(moduleCode, status * lastStatus);
                        } else {
                            map.put(moduleCode, status);
                        }
                    }

                    while (cursor.moveToNext());

                } catch (Exception e) {

                    Helper.printStackTrace(e);
                }
            }

        } catch (SQLException e) {
            Helper.printStackTrace(e);
        } catch (Exception e) {
            Helper.printStackTrace(e);
        } finally {
            cursor.close();
        }

        return map;

    }

    public static List<ContentValues[]> getContentValueLMSLeaveResponse(JSONArray jsonResponseJsonArray) {


        ArrayList<ContentValues> lmsData = new ArrayList<ContentValues>();
        ArrayList<ContentValues> lmsDateData = new ArrayList<ContentValues>();
        ArrayList<ContentValues> lmsStatusLogData = new ArrayList<ContentValues>();


        ContentValues initialValuesLMSData;
        ContentValues initialValuesLMSDateData;
        ContentValues initialValuesLMSStatusLogData;

        try {

            if (jsonResponseJsonArray != null) {
                int count = jsonResponseJsonArray.length();

                for (int i = 0; i < count; i++) {

                    initialValuesLMSData = new ContentValues();

                    JSONObject jsonObjectLMSList = jsonResponseJsonArray
                            .getJSONObject(i);
                    int masterID = jsonObjectLMSList.getInt(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MASTER_ID);

                    initialValuesLMSData.put(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MASTER_ID, masterID);

                    initialValuesLMSData.put(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_SUBJECT,
                            jsonObjectLMSList.getString(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_SUBJECT));

                    initialValuesLMSData.put(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_APPLIED_DATE,
                            Helper.getDateFromString(jsonObjectLMSList.getString(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_APPLIED_DATE), "dd-MMM-yyyy"));

                    initialValuesLMSData.put(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CREATED_BY,
                            jsonObjectLMSList.getInt(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CREATED_BY));

                    initialValuesLMSData.put(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CREATED_BY_USER_NAME,
                            jsonObjectLMSList.getString(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CREATED_BY_USER_NAME));

                    initialValuesLMSData.put(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CREATED_DATE,
                            Helper.getDateFromString(jsonObjectLMSList.getString(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CREATED_DATE), "dd-MMM-yyyy hh:mm:ss.SSS"));

                    initialValuesLMSData.put(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CURRENT_STATUS,
                            jsonObjectLMSList.getInt(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_CURRENT_STATUS));

                    try {
                        initialValuesLMSData.put(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MODIFIED_BY,
                                jsonObjectLMSList.getInt(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MODIFIED_BY));
                    } catch (JSONException e) {
                        initialValuesLMSData.put(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MODIFIED_BY,0);
                    }

                    initialValuesLMSData.put(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MODIFIED_BY_USER_NAME,
                            jsonObjectLMSList.getString(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MODIFIED_BY_USER_NAME));

                    initialValuesLMSData.put(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MODIFIED_DATE,
                            Helper.getDateFromString(jsonObjectLMSList.getString(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_MODIFIED_DATE), "dd-MMM-yyyy hh:mm:ss.SSS"));

                    initialValuesLMSData.put(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_NUMBER_OF_LEAVE,
                            jsonObjectLMSList.getInt(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_NUMBER_OF_LEAVE));

                    initialValuesLMSData.put(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_PENDING_WITH,
                            jsonObjectLMSList.getInt(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_PENDING_WITH));


                    initialValuesLMSData.put(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_PENDING_WITH_USER_NAME,
                            jsonObjectLMSList.getString(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_PENDING_WITH_USER_NAME));

                    initialValuesLMSData.put(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_REMARKS,
                            jsonObjectLMSList.getString(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_REMARKS));

                    initialValuesLMSData.put(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_TYPE_ID,
                            jsonObjectLMSList.getInt(DatabaseConstants.LMSLeaveMasterColumn.KEY_LEAVE_TYPE_ID));



                    JSONArray lmsLeaveDateJSONArray = jsonObjectLMSList
                            .getJSONArray("LMSLeaveDetails");
                    if (lmsLeaveDateJSONArray != null) {
                        int countDate = lmsLeaveDateJSONArray.length();
                        for (int j = 0; j < countDate; j++) {
                            try {
                                JSONObject jsonObjectDateList = lmsLeaveDateJSONArray
                                        .getJSONObject(j);

                                initialValuesLMSDateData = new ContentValues();

                                initialValuesLMSDateData.put(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_DETAIL_ID,
                                        jsonObjectDateList.getInt(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_DETAIL_ID));

                                initialValuesLMSDateData.put(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MASTER_ID,
                                        masterID);

                                initialValuesLMSDateData.put(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_CURRENT_STATUS,
                                        jsonObjectDateList.getInt(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_CURRENT_STATUS));

                                initialValuesLMSDateData.put(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_IS_HALF_DAY,
                                        jsonObjectDateList.getBoolean(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_IS_HALF_DAY));

                                initialValuesLMSDateData.put(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_DATE,
                                        Helper.getDateFromString(jsonObjectDateList.getString(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_DATE), "dd-MMM-yyyy"));

                                initialValuesLMSDateData.put(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_DATE_TEXT,
                                        jsonObjectDateList.getString(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_DATE));

                                initialValuesLMSDateData.put(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_CREADTED_BY,
                                        jsonObjectDateList.getInt(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_CREADTED_BY));

                                try {
                                    initialValuesLMSDateData.put(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MODIFIED_BY,
                                            jsonObjectDateList.getInt(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MODIFIED_BY));
                                } catch (JSONException e) {
                                    initialValuesLMSDateData.put(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MODIFIED_BY,0);
                                }

                                initialValuesLMSDateData.put(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MODIFIED_BY_USER_NAME,
                                        jsonObjectDateList.getString(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MODIFIED_BY_USER_NAME));

                                initialValuesLMSDateData.put(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MODIFIED_DATE,
                                        Helper.getDateFromString(jsonObjectDateList.getString(DatabaseConstants.LMSLeaveDetailsMasterColumn.KEY_LEAVE_MODIFIED_DATE), "dd-MMM-yyyy hh:mm:ss.SSS"));

                                lmsDateData.add(initialValuesLMSDateData);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }


                    JSONArray lmsLeaveStatusLogJSONArray = jsonObjectLMSList
                            .getJSONArray("LMSStatusLogs");
                    if (lmsLeaveStatusLogJSONArray != null) {
                        int countStatusLog = lmsLeaveStatusLogJSONArray.length();
                        for (int j = 0; j < countStatusLog; j++) {
                            try {
                                JSONObject jsonObjectStatusLog = lmsLeaveStatusLogJSONArray
                                        .getJSONObject(j);

                                initialValuesLMSStatusLogData = new ContentValues();
                                initialValuesLMSStatusLogData.put(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_LOG_ID,
                                        jsonObjectStatusLog.getInt(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_LOG_ID));

                                initialValuesLMSStatusLogData.put(DatabaseConstants.LMSStatusLogColumn.KEY_LEAVE_MASTER_ID, masterID);

                                initialValuesLMSStatusLogData.put(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CREATED_BY,
                                        jsonObjectStatusLog.getInt(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CREATED_BY));

                                initialValuesLMSStatusLogData.put(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CREATED_BY_USER_NAME,
                                        jsonObjectStatusLog.getString(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CREATED_BY_USER_NAME));

                                initialValuesLMSStatusLogData.put(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CREATED_DATE,
                                        Helper.getDateFromString(jsonObjectStatusLog.getString(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CREATED_DATE), "dd-MMM-yyyy hh:mm:ss.SSS"));

                                initialValuesLMSStatusLogData.put(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CURRENT_STATUS,
                                        jsonObjectStatusLog.getInt(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_CURRENT_STATUS));

                                initialValuesLMSStatusLogData.put(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_REMARKS,
                                        jsonObjectStatusLog.getString(DatabaseConstants.LMSStatusLogColumn.KEY_LMS_STATUS_REMARKS));



                                lmsStatusLogData.add(initialValuesLMSStatusLogData);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    lmsData.add(initialValuesLMSData);
                }

            }

        } catch (SQLException e) {
            Helper.printStackTrace(e);
        } catch (Exception e) {
            Helper.printStackTrace(e);
        }

        ArrayList<ContentValues[]> listContentValuesArr = new ArrayList<ContentValues[]>();

        listContentValuesArr.add(lmsData.toArray(new ContentValues[lmsData
                .size()]));
        listContentValuesArr.add(lmsDateData
                .toArray(new ContentValues[lmsDateData.size()]));

        listContentValuesArr.add(lmsStatusLogData
                .toArray(new ContentValues[lmsStatusLogData.size()]));

        return listContentValuesArr;


    }


    public static ContentValues[] getContentValueLMSLeaveConfiguration(JSONArray rootJSONArray) {
        int count = rootJSONArray.length();

        ContentValues[] values = new ContentValues[count];

        try {
            for (int i = 0; i < count; i++) {
                JSONObject item = rootJSONArray.getJSONObject(i);
                ContentValues value = new ContentValues();
                value.put(DatabaseConstants.LMSLeaveTypeConfigurationColumn.KEY_LMS_LEAVE_TYPE_CONFIGURATION_ID, item.getInt(DatabaseConstants.LMSLeaveTypeConfigurationColumn.KEY_LMS_LEAVE_TYPE_CONFIGURATION_ID));
                value.put(DatabaseConstants.LMSLeaveTypeConfigurationColumn.KEY_LEAVE_TYPE_ID, item.getInt(DatabaseConstants.LMSLeaveTypeConfigurationColumn.KEY_LEAVE_TYPE_ID));
                value.put(DatabaseConstants.LMSLeaveTypeConfigurationColumn.KEY_CONFIGURATION_TEXT, item.getString(DatabaseConstants.LMSLeaveTypeConfigurationColumn.KEY_CONFIGURATION_TEXT));
                value.put(DatabaseConstants.LMSLeaveTypeConfigurationColumn.KEY_CONFIGURATION_VALUE, item.getInt(DatabaseConstants.LMSLeaveTypeConfigurationColumn.KEY_CONFIGURATION_VALUE));
                value.put(DatabaseConstants.LMSLeaveTypeConfigurationColumn.KEY_CONFIG_VALUE, item.getInt(DatabaseConstants.LMSLeaveTypeConfigurationColumn.KEY_CONFIG_VALUE));


                values[i] = value;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return values;
    }

    public static ContentValues[] getContentValueLMSLeaveType(JSONArray rootJSONArray) {
        int count = rootJSONArray.length();

        ContentValues[] values = new ContentValues[count];

        try {
            for (int i = 0; i < count; i++) {
                JSONObject item = rootJSONArray.getJSONObject(i);
                ContentValues value = new ContentValues();
                value.put(DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TYPE_ID, item.getInt(DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TYPE_ID));
                value.put(DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TYPE, item.getString(DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TYPE));
                value.put(DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TYPE_CODE, item.getString(DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TYPE_CODE));
                value.put(DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TAKEN, item.getInt(DatabaseConstants.LMSLeaveTypeMasterColumn.KEY_LEAVE_TAKEN));
                values[i] = value;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return values;
    }

    public static ArrayList<EMSExpenseType> getExpenseTypesFromCursor(
            Cursor cursor) {

        ArrayList<EMSExpenseType> expenseTypeList = new ArrayList<EMSExpenseType>();

        do {
            try {

                EMSExpenseType expenseType = new EMSExpenseType();

                expenseType.miExpenseTypeMasterId = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_ID));
                expenseType.mName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_NAME));
                expenseType.miCode = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_CODE));
                expenseType.mDescription = cursor.getString(cursor.getColumnIndex(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_DESCRIPTION));
                expenseType.miCompanyId = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_COMPANY_ID));
                expenseType.miSequence = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_SEQUENCE));


                expenseTypeList.add(expenseType);
            } catch (SQLException e) {
                Helper.printLog("GetExpenseMasterData", "" + e.getMessage());
                Helper.printStackTrace(e);
                ;
            }

        } while (cursor.moveToNext());

        return expenseTypeList;
    }


    public static ContentValues[] getExpenseTypeMasterContentValuesFromJson(
            JSONArray jsonResonceProductCategoryArray) {
        ContentValues[] values = null;
        try {

            if (jsonResonceProductCategoryArray != null) {

                int count = jsonResonceProductCategoryArray.length();

                values = new ContentValues[count];

                for (int i = 0; i < count; i++) {

                    ContentValues mContentValues = new ContentValues();
                    JSONObject jsonObject = jsonResonceProductCategoryArray.getJSONObject(i);

                    mContentValues.put(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_ID, jsonObject.getInt(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_ID));


                    mContentValues
                            .put(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_NAME,
                                    jsonObject.getString(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_NAME));

                    mContentValues
                            .put(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_CODE,
                                    jsonObject.getInt(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_CODE));


                    mContentValues
                            .put(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_DESCRIPTION,
                                    jsonObject.getString(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_DESCRIPTION));

                    mContentValues
                            .put(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_COMPANY_ID,
                                    jsonObject.getInt(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_COMPANY_ID));

                    mContentValues
                            .put(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_SEQUENCE,
                                    jsonObject.getInt(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_SEQUENCE));


                    mContentValues
                            .put(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_IS_ACTIVE,
                                    jsonObject.getBoolean(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_IS_ACTIVE));

                    mContentValues
                            .put(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_IS_DELETED,
                                    jsonObject.getBoolean(DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_IS_DELETED));


                    values[i] = mContentValues;

                }

            }

        } catch (JSONException e) {
            Helper.printStackTrace(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return values;

    }
}