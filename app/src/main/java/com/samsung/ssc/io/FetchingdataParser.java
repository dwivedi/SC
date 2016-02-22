package com.samsung.ssc.io;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.Gson;
import com.samsung.ssc.R;
import com.samsung.ssc.dto.BeatCoverageDto;
import com.samsung.ssc.dto.BeatCreationDto;
import com.samsung.ssc.dto.CompetitionProductGroupDto;
import com.samsung.ssc.dto.EmployeeListDto;
import com.samsung.ssc.dto.FeedbackDetailModel;
import com.samsung.ssc.dto.FeedbackLogModel;
import com.samsung.ssc.dto.GetPlanogramProductResponse;
import com.samsung.ssc.dto.Module;
import com.samsung.ssc.dto.PaymentModes;
import com.samsung.ssc.dto.ProductListDto;
import com.samsung.ssc.dto.ResponseDto;
import com.samsung.ssc.dto.SchemeAvailableDto;
import com.samsung.ssc.dto.UserBeatDetailDto;
import com.samsung.ssc.util.Helper;

public class FetchingdataParser {

	private Context mContext;

	public FetchingdataParser(Context mContext) {
		this.mContext = mContext;
	}

	public ResponseDto getResponseResult(String data) {
		ResponseDto obj = new ResponseDto();

		try {
			JSONObject jObject = new JSONObject(data);

			obj.setSuccess(jObject.getBoolean("IsSuccess"));
			obj.setFailedValidations(jObject.get("FailedValidations")
					.toString());
			obj.setMessage(jObject.get("Message").toString());
			obj.setResult(jObject.get("Result").toString());
			obj.setSingleResult(jObject.get("SingleResult").toString());
			obj.setStatusCode(jObject.get("StatusCode").toString());

		} catch (JSONException e) {
			Helper.printStackTrace(e);
			obj.setSuccess(false);
			obj.setMessage(mContext.getString(R.string.server_not_responding));
		}
		return obj;
	}

	/**
	 * 
	 * @param data
	 *            Competitor Master data to be parse
	 * @return CompetitorMaster DTO
	 */

	public JSONArray getCompetitorList(String responce) {

		try {

			JSONObject jsonObject = new JSONObject(responce);

			if (jsonObject.getBoolean("IsSuccess")) {

				JSONArray resultJsonArray = jsonObject.getJSONArray("Result");
				return resultJsonArray;
			}

		} catch (JSONException e) {

		} catch (Exception e) {
		}

		return null;
	}

	public ArrayList<Module> getDisplayModules(String data) {

		ArrayList<Module> list = new ArrayList<Module>();
		try {
			JSONObject jObject = new JSONObject(data);

			if (Boolean.valueOf(jObject.get("IsSuccess").toString())) {

				JSONArray array = jObject.getJSONArray("Result");
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject = array.getJSONObject(i);
					Module module = new Module();
					module.setIsMandatory(jsonObject.getBoolean("IsMandatory"));
					module.setSequence(jsonObject.getInt("Sequence"));
					module.setModuleCode(jsonObject.getInt("ModuleCode"));
					int moduleId;
					try {
						moduleId = jsonObject.getInt("ModuleID");

					} catch (Exception e) {
						moduleId = 0;
					}
					module.setModuleID(moduleId);
					module.setName(jsonObject.getString("Name"));

					int moduleParentId;
					try {
						moduleParentId = jsonObject.getInt("ParentModuleID");

					} catch (NumberFormatException e1) {

						moduleParentId = 0;
					}

					catch (Exception e1) {
						e1.printStackTrace();
						moduleParentId = 0;
					}

					module.setParentModuleID(moduleParentId);
					// obj.setUserRoleID(jsonObject.getString("UserRoleID"));

					/**
					 * modify by ashish dwivedi
					 */

					module.setIconName(jsonObject.getString("Icon"));
					module.setQuestionType(jsonObject
							.getBoolean("IsQuestionModule"));
					try {
						boolean storeWise = jsonObject.getString("IsStoreWise") == null ? false
								: jsonObject.getBoolean("IsStoreWise");
						module.setStoreWise(storeWise);
					} catch (Exception e) {
						module.setStoreWise(false);
					}

					list.add(module);
				}
			} else {
				Module obj = new Module();
				// obj.setSuccess(jObject.getBoolean("IsSuccess"));
				obj.setMessage(jObject.getString("Message"));
				list.add(obj);
			}

		} catch (JSONException e) {
			Helper.printStackTrace(e);
			Module obj = new Module();
			// obj.setSuccess(false);
			obj.setMessage(mContext.getString(R.string.server_not_responding));
			list.add(obj);
		}
		return list;
	}

	public JSONArray getProductListData(String responce) {

		try {
			JSONObject jObject = new JSONObject(responce);
			if (jObject.get("IsSuccess").toString().equals("true")) {
				JSONArray productArray = jObject.getJSONArray("Result");
				if (productArray != null) {
					return productArray;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public ArrayList<ProductListDto> parseOrderBooking(String data) {

		ArrayList<ProductListDto> list = new ArrayList<ProductListDto>();
		try {
			JSONObject jObject = new JSONObject(data);

			if (jObject.get("IsSuccess").toString().equals("true")) {
				JSONArray arr = jObject.getJSONArray("Result");

				for (int i = 0; i < arr.length(); i++) {
					ProductListDto obj = new ProductListDto();
					obj.setSuccess(Boolean.valueOf(jObject.get("IsSuccess")
							.toString()));
					JSONObject jobj = arr.getJSONObject(i);
					obj.setBasicModelCode(jobj.get("BasicModelCode").toString());
					obj.setBasicModelName(jobj.get("BasicModelName").toString());
					obj.setCategoryCode(jobj.get("CategoryCode").toString());
					obj.setCategoryName(jobj.get("CategoryName").toString());
					obj.setDealerPrice(jobj.get("DealerPrice").toString());
					obj.setModelTypeID(jobj.get("ModelTypeID").toString());
					obj.setProductCategoryID(jobj.get("ProductCategoryID")
							.toString());
					obj.setProductGroupCode(jobj.get("ProductGroupCode")
							.toString());
					obj.setProductGroupID(jobj.get("ProductGroupID").toString());
					obj.setProductGroupName(jobj.get("ProductGroupName")
							.toString());
					obj.setProductID(jobj.get("ProductID").toString());
					obj.setProductTypeCode(jobj.get("ProductTypeCode")
							.toString());
					obj.setProductTypeID(jobj.get("ProductTypeID").toString());
					obj.setProductTypeName(jobj.get("ProductTypeName")
							.toString());
					obj.setsKUCode(jobj.get("SKUCode").toString());
					obj.setsKUName(jobj.get("SKUName").toString());
					list.add(obj);

				}

			} else {
				ProductListDto obj = new ProductListDto();
				obj.setSuccess(Boolean.valueOf(jObject.get("IsSuccess")
						.toString()));
				obj.setMessage(jObject.get("Message").toString());
				list.add(obj);
			}

		} catch (JSONException e) {
			Helper.printStackTrace(e);
			ProductListDto obj = new ProductListDto();
			obj.setSuccess(false);
			obj.setMessage(mContext.getString(R.string.server_not_responding));
			list.add(obj);
		}
		return list;
	}

	public ArrayList<PaymentModes> getPaymentModes(String data) {

		ArrayList<PaymentModes> list = new ArrayList<PaymentModes>();
		try {
			JSONObject jObject = new JSONObject(data);

			if (jObject.get("IsSuccess").toString().equals("true")) {
				JSONArray arr = jObject.getJSONArray("Result");

				for (int i = 0; i < arr.length(); i++) {
					PaymentModes obj = new PaymentModes();
					JSONObject jobj = arr.getJSONObject(i);
					obj.setPaymentModeID(jobj.get("PaymentModeID").toString());
					obj.setModeName(jobj.get("ModeName").toString());
					list.add(obj);
				}

			} else {
				PaymentModes obj = new PaymentModes();
				obj.setSuccess(Boolean.valueOf(jObject.get("IsSuccess")
						.toString()));
				obj.setMessage(jObject.get("Message").toString());
				list.add(obj);
			}

		} catch (JSONException e) {
			Helper.printStackTrace(e);
			PaymentModes modes = new PaymentModes();
			modes.setSuccess(false);
			modes.setMessage(mContext.getString(R.string.server_not_responding));
			list.add(modes);
		}
		return list;
	}

	public ArrayList<SchemeAvailableDto> parsSchemeAvailable(String data) {

		ArrayList<SchemeAvailableDto> list = new ArrayList<SchemeAvailableDto>();
		try {
			JSONObject jObject = new JSONObject(data);

			if (jObject.get("IsSuccess").toString().equals("true")) {
				JSONArray arr = jObject.getJSONArray("Result");

				for (int i = 0; i < arr.length(); i++) {
					SchemeAvailableDto obj = new SchemeAvailableDto();
					obj.setSingleResult(jObject.get("SingleResult").toString());
					obj.setStatusCode(jObject.get("StatusCode").toString());
					obj.setMessage(jObject.get("Message").toString());
					obj.setSuccess(Boolean.valueOf(jObject.get("IsSuccess")
							.toString()));
					JSONObject jobj = arr.getJSONObject(i);
					obj.setDescription(jobj.get("Description").toString());
					// obj.setActive(Boolean.valueOf(jObject.get("IsActive").toString()));
					obj.setSchemeExpiryDate(jobj.get("SchemeExpiryDate")
							.toString());
					obj.setSchemeID(jobj.get("SchemeID").toString());
					obj.setSchemeStartDate(jobj.get("SchemeStartDate")
							.toString());
					obj.setTitle(jobj.get("Title").toString());
					list.add(obj);
				}

			} else {
				SchemeAvailableDto obj = new SchemeAvailableDto();
				obj.setSuccess(Boolean.valueOf(jObject.get("IsSuccess")
						.toString()));
				obj.setMessage(jObject.get("Message").toString());
				list.add(obj);
			}

		} catch (JSONException e) {
			Helper.printStackTrace(e);
			SchemeAvailableDto obj = new SchemeAvailableDto();
			obj.setSuccess(false);
			obj.setMessage(mContext.getString(R.string.server_not_responding));
			list.add(obj);
		}
		return list;
	}

	/**
	 * CompetitionProductGroup WS Parser
	 * 
	 * @param res
	 */

	public ArrayList<CompetitionProductGroupDto> parseCompetitionProductGroupDto(
			String res) {

		ArrayList<CompetitionProductGroupDto> list = new ArrayList<CompetitionProductGroupDto>();
		try {
			JSONObject jObject = new JSONObject(res);

			if (jObject.get("IsSuccess").toString().equals("true")) {
				JSONArray arr = jObject.getJSONArray("Result");

				for (int i = 0; i < arr.length(); i++) {
					CompetitionProductGroupDto obj = new CompetitionProductGroupDto();
					obj.setSuccess(Boolean.valueOf(jObject.get("IsSuccess")
							.toString()));
					JSONObject jobj = arr.getJSONObject(i);

					obj.setCompProductGroupID(jobj.get("CompProductGroupID")
							.toString());
					obj.setProductGroupCode(jobj.get("ProductGroupCode")
							.toString());
					obj.setProductGroupName(jobj.get("ProductGroupName")
							.toString());

					list.add(obj);
				}

			} else {
				CompetitionProductGroupDto obj = new CompetitionProductGroupDto();
				obj.setSuccess(Boolean.valueOf(jObject.get("IsSuccess")
						.toString()));
				obj.setMessage(jObject.get("Message").toString());
				list.add(obj);
			}

		} catch (JSONException e) {
			Helper.printStackTrace(e);
			CompetitionProductGroupDto obj = new CompetitionProductGroupDto();
			obj.setSuccess(false);
			obj.setMessage(mContext.getString(R.string.server_not_responding));
			list.add(obj);
		}

		return list;

	}

	/**
	 * 
	 * @param response
	 * @return
	 */

	public ArrayList<BeatCreationDto> getStorelist(String response) {

		ArrayList<BeatCreationDto> list = new ArrayList<BeatCreationDto>();
		try {
			JSONObject jObject = new JSONObject(response);

			if (jObject.get("IsSuccess").toString().equals("true")) {
				JSONArray arr = jObject.getJSONArray("Result");

				for (int i = 0; i < arr.length(); i++) {
					BeatCreationDto obj = new BeatCreationDto();
					JSONObject jobj = arr.getJSONObject(i);
					if (jobj.getBoolean("IsActive")) {
						obj.setSuccess(Boolean.valueOf(jObject.get("IsSuccess")
								.toString()));
						obj.setStoreId(jobj.get("StoreID").toString());
						obj.setStoreCode(jobj.get("StoreCode").toString());
						obj.setStoreName(jobj.get("StoreName").toString().replace(',', ' '));
						obj.setBeatSummary(jobj.get("BeatSummary").toString());
						obj.setCity(jobj.get("City").toString());
						obj.setChannelType(jobj.get("ChannelType").toString());
						obj.setStatus(false);
						list.add(obj);
					}

				}

			} else {
				BeatCreationDto obj = new BeatCreationDto();
				obj.setSuccess(Boolean.valueOf(jObject.get("IsSuccess")
						.toString()));
				obj.setMessage(jObject.get("Message").toString());
				list.add(obj);
			}

		} catch (JSONException e) {
			Helper.printStackTrace(e);
			BeatCreationDto obj = new BeatCreationDto();
			obj.setSuccess(false);
			obj.setMessage(mContext.getString(R.string.server_not_responding));
			list.add(obj);
		}

		return list;

	}

	/**
	 * 
	 * @param response
	 * @return
	 */

	public HashMap<Integer, EmployeeListDto> getEmployeeList(String response) {

		HashMap<Integer, EmployeeListDto> list = new HashMap<Integer, EmployeeListDto>();
		try {
			JSONObject jObject = new JSONObject(response);

			if (jObject.get("IsSuccess").toString().equals("true")) {
				JSONArray arr = jObject.getJSONArray("Result");

				for (int i = 0; i < arr.length(); i++) {
					EmployeeListDto obj = new EmployeeListDto();
					JSONObject jobj = arr.getJSONObject(i);
					obj.setSuccess(Boolean.valueOf(jObject.get("IsSuccess")
							.toString()));
					obj.setCoverageType(jobj.get("CoverageType").toString());
					obj.setFirstName(jobj.get("FirstName").toString());
					obj.setLastName(jobj.get("LastName").toString());
					obj.setMarketOffDays(jobj.get("MarketOffDays").toString());
					obj.setStatusID(jobj.get("StatusID").toString());
					obj.setUserId(jobj.get("UserID").toString());
					obj.setSuccess(true);
					list.put(Integer.valueOf(i), obj);

				}

			} else {
				EmployeeListDto obj = new EmployeeListDto();
				obj.setSuccess(Boolean.valueOf(jObject.get("IsSuccess")
						.toString()));
				obj.setMessage(jObject.get("Message").toString());
				obj.setSuccess(false);
				list.put(0, obj);
			}

		} catch (JSONException e) {
			Helper.printStackTrace(e);
			EmployeeListDto obj = new EmployeeListDto();
			obj.setSuccess(false);
			obj.setMessage(mContext.getString(R.string.server_not_responding));
			list.put(0, obj);
		}

		return list;

	}

	public ArrayList<UserBeatDetailDto> getUserBeatDetailList(String response) {

		ArrayList<UserBeatDetailDto> list = new ArrayList<UserBeatDetailDto>();
		try {
			JSONObject jObject = new JSONObject(response);

			if (jObject.get("IsSuccess").toString().equals("true")) {

				JSONObject sObj = jObject.getJSONObject("SingleResult");
				JSONArray arr = sObj.getJSONArray("UserBeatDetails");

				for (int i = 0; i < arr.length(); i++) {
					UserBeatDetailDto obj = new UserBeatDetailDto();
					JSONObject jobj = arr.getJSONObject(i);

					JSONArray storeObj = jobj.getJSONArray("StoreData");

					obj.setSuccess(Boolean.valueOf(jObject.get("IsSuccess")
							.toString()));

					ArrayList<String> storeList = new ArrayList<String>();
					for (int j = 0; j < storeObj.length(); j++) {
						JSONObject ob = storeObj.getJSONObject(j);
						storeList.add(ob.get("StoreName").toString() + " - "
								+ ob.get("StoreCode").toString() + " - "
								+ ob.get("City").toString());
					}
					obj.setStoreList(storeList);

					obj.setCoverageDate(jobj.get("CoverageDate").toString());

					obj.setTotalOutletPlanned(sObj.get("TotalOutletPlanned")
							.toString());
					obj.setTotalWorkingDays(sObj.get("TotalWorkingDays")
							.toString());
					obj.setTotalOff(sObj.get("TotalOff").toString());
					obj.setLeaveDetail(sObj.get("LeaveDetail").toString());
					obj.setTotalAssignedOutlet(sObj.get("TotalAssignedOutlet")
							.toString());
					list.add(obj);

				}

			} else {
				UserBeatDetailDto obj = new UserBeatDetailDto();
				obj.setSuccess(Boolean.valueOf(jObject.get("IsSuccess")
						.toString()));
				obj.setMessage(jObject.get("Message").toString());
				list.add(obj);
			}

		} catch (JSONException e) {
			Helper.printStackTrace(e);
			UserBeatDetailDto obj = new UserBeatDetailDto();
			obj.setSuccess(false);
			obj.setMessage(mContext.getString(R.string.server_not_responding));
			list.add(obj);
		}

		return list;

	}

	public BeatCoverageDto getCoverageWindow(String data) {
		BeatCoverageDto obj = new BeatCoverageDto();

		try {
			JSONObject jObject = new JSONObject(data);

			obj.setSuccess(jObject.getBoolean("IsSuccess"));
			obj.setFailedValidations(jObject.get("FailedValidations")
					.toString());
			obj.setMessage(jObject.get("Message").toString());
			obj.setResult(jObject.get("Result").toString());
			obj.setStatusCode(jObject.get("StatusCode").toString());

			JSONObject singleObj = jObject.getJSONObject("SingleResult");
			BeatCoverageDto.SingleResult singleResult = obj.new SingleResult();
			singleResult.setCoverageDate(singleObj.getString("CoverageDate"));
			singleResult
					.setCurrentMonth(singleObj.getBoolean("IsCurrentMonth"));
			singleResult
					.setExceptionFlag(singleObj.getBoolean("ExceptionFlag"));

			obj.setSingleResult(singleResult);
		} catch (JSONException e) {
			Helper.printStackTrace(e);
			obj.setSuccess(false);
			obj.setMessage(mContext.getString(R.string.server_not_responding));
		}
		return obj;
	}

	public JSONArray getPlanogramDataListData(String res) {

		try {
			JSONObject rootJson = new JSONObject(res);

			boolean isSuccess = rootJson.getBoolean("IsSuccess");
			if (isSuccess) {
				JSONArray resultJson = rootJson.getJSONArray("Result");
				return resultJson;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public JSONArray getPlanogramDataListData1(String res) {

		try {
			JSONObject rootJson = new JSONObject(res);

			boolean isSuccess = rootJson.getBoolean("IsSuccess");
			if (isSuccess) {
				JSONArray resultJson = rootJson.getJSONArray("Result");
				return resultJson;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public GetPlanogramProductResponse getPlanogramDataResponse(String res) {
		GetPlanogramProductResponse planogramProductResponse = null;

		try {

			Gson gson = new Gson();
			planogramProductResponse = (GetPlanogramProductResponse) gson
					.fromJson(res, GetPlanogramProductResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return planogramProductResponse;
	}

	public FeedbackDetailModel getFeedbackDetailModel(JSONObject jsonObject) {

		FeedbackDetailModel feedbackDetailModel = new FeedbackDetailModel();

		try {

			feedbackDetailModel.setFeedbackCatID(jsonObject
					.getInt("FeedbackCatID"));
			feedbackDetailModel.setFeedbackNumber(jsonObject
					.getString("FeedbackNumber"));
			feedbackDetailModel.setFeedbackTypeID(jsonObject
					.getInt("FeedbackTypeID"));
			feedbackDetailModel.setTeamID(jsonObject.getInt("TeamID"));
			feedbackDetailModel.setTimeTaken(jsonObject.getDouble("TimeTaken"));
			feedbackDetailModel.setFeedbackUserID(jsonObject.getInt("UserID"));
			feedbackDetailModel.setSpocID(jsonObject.getInt("SpocID"));
			feedbackDetailModel.setFeedbackImageURL(jsonObject
					.getString("ImageURL"));
			feedbackDetailModel.setCurrentFeedbackStatusID(jsonObject
					.getInt("CurrentFeedbackStatusID"));

			JSONArray logListJsonArray = jsonObject
					.getJSONArray("FeedbackDetailLog");

			ArrayList<FeedbackLogModel> feedbacKLogArrayList = new ArrayList<FeedbackLogModel>();

			int logCount = logListJsonArray.length();

			for (int i = 0; i < logCount; i++) {

				FeedbackLogModel logDataModal = new FeedbackLogModel();

				JSONObject object = logListJsonArray.getJSONObject(i);

				logDataModal.setCreatedBy(object.getString("CreatedBy"));
				logDataModal.setCreatedOn(object.getString("CreatedOn"));
				logDataModal.setPendingWith(object.getString("PendingWith"));
				logDataModal.setRemarks(object.getString("Remarks"));
				logDataModal.setResponseDate(object.getString("ResponseDate"));
				logDataModal.setStatusID(object.getInt("FeedbackStatusID"));
				feedbacKLogArrayList.add(logDataModal);
			}

			feedbackDetailModel.setFeedbackLogs(feedbacKLogArrayList);

			return feedbackDetailModel;

		} catch (JSONException e) {

			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

 
}
