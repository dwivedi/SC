package com.samsung.ssc.EMS;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samsung.ssc.CustomUI.SSCProgressDialog;
import com.samsung.ssc.R;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig.WebMethod;
import com.samsung.ssc.database.DatabaseConstants.EMSBillDetailTableColumns;
import com.samsung.ssc.database.DatabaseConstants.EMSBillDocumentDetailTableColumns;
import com.samsung.ssc.database.DatabaseConstants.EMSExpenseDetailTableColumns;
import com.samsung.ssc.database.DatabaseConstants.TableNames;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.dto.BillDocumentSubmitResponse;
import com.samsung.ssc.dto.BillSubmitResponse;
import com.samsung.ssc.dto.EMSBillDetail;
import com.samsung.ssc.dto.EMSBillDocumentDetail;
import com.samsung.ssc.dto.EMSExpenseDetail;
import com.samsung.ssc.dto.ExpenseBillDocumentMultipartUpload;
import com.samsung.ssc.dto.ExpenseSubmitResponse;
import com.samsung.ssc.dto.ResponseDto;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class ExpenseListingFragment extends Fragment {
	
	private int miTabID;
	private ExpenseListAdapter mAdapter;
	
 
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
      @return A new instance of fragment ExpenseListingFragment.
     */
    public static ExpenseListingFragment newInstance(int iniTabID) {
      
    	return new ExpenseListingFragment(iniTabID);
    }
 
    public ExpenseListingFragment(int iniTabID) {

    	miTabID = iniTabID;
    	
    }
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
    	View expenseListParentView = inflater.inflate(R.layout.fragment_expense_listing, container, false);
    	
    	setUpView(expenseListParentView) ;
    	
        return expenseListParentView;
    }
    
    
    private void setUpView(View expenseListParentView) {

		RecyclerView mExpenseItemListRecyclerview = (RecyclerView) expenseListParentView.findViewById(R.id.expense_list_recycler_view);
		mExpenseItemListRecyclerview.setHasFixedSize(true);
		// use a linear layout manager
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		mExpenseItemListRecyclerview.setLayoutManager(mLayoutManager);

		mAdapter = new ExpenseListAdapter(getActivity(),true);
		mExpenseItemListRecyclerview.setAdapter(mAdapter);

	}
    
    @Override
    public void onResume() {
    	super.onResume();
    	getExpensesDataFromDatabase();
    }
    
    
    public void getExpensesDataFromDatabase() {
		
    	ArrayList<EMSExpenseDetail> emsExpenseDetailArrayList = DatabaseHelper.getConnection(
				getActivity()).getLocalDBExpensesDataForGivenTab(miTabID);
    	
		mAdapter.clear();
		
		if(emsExpenseDetailArrayList != null && emsExpenseDetailArrayList.size() > 0)
		    mAdapter.addItems(emsExpenseDetailArrayList);
		
	}

    public void expenseDataNotAvailableFetchFromDB()
    {
    	if( mAdapter.getItemCount() == 0)
    		getExpensesDataFromDatabase();
    }

    
    private HttpURLConnection getServerConn(String upLoadServerUrl,
			String boundary) throws MalformedURLException, IOException,
			ProtocolException {
		HttpURLConnection conn;
		// open a URL connection to the server
		URL url = new URL(upLoadServerUrl);
		// Open a HTTP connection to the URL
		conn = (HttpURLConnection) url.openConnection();

		conn.setDoInput(true); // Allow Inputs
		conn.setDoOutput(true); // Allow Outputs
		conn.setUseCaches(false); // Don't use a Cached Copy
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("ENCTYPE", "multipart/form-data");
		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="
				+ boundary);

		return conn;
	}
    
    private final InputStream getStream(File file) throws FileNotFoundException {

		return new FileInputStream(file);
	}

    private void closeInputStream(final InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (Exception exc) {
			}
		}
	}
    
    public void performExpensesSync()
	{
 	
        ArrayList<EMSExpenseDetail> emsExpenseDetailArrayList = DatabaseHelper.getConnection(getActivity()).getUploadExpenseData();	
   		    
        if(emsExpenseDetailArrayList != null && emsExpenseDetailArrayList.size() > 0)
            uploadExpensesData(emsExpenseDetailArrayList);
        else
        {
        	//isGetAllExpenseRequiredThenSync();

			callWebServiceForGetALLServerExpenses();
        }

	}
    
    private void showAlertDialog(String message) 
    {
    	  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
  	   
    	    alertDialogBuilder.setMessage(message);
    	    alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
    	    
    	    alertDialogBuilder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) {
                       	
    	        	dialog.dismiss();
    	      
    	      } });
    	    
    	   
    	    alertDialogBuilder.show();
	  }
    
    private void isGetAllExpenseRequiredThenSync()
    {
    	int totalexpenseCount = DatabaseHelper.getConnection(getActivity()).getTotalExpenseCount();
    	
    	if(totalexpenseCount == 0)
    	 callWebServiceForGetALLServerExpenses();
    	else
    	{
    		showAlertDialog("Expenses Already synced"); 
    	}
    }
    
    
   private class SyncExpensesTask extends AsyncTask<Void, Void, Boolean> {

		private ProgressDialog progressDialog;
		ArrayList<ExpenseBillDocumentMultipartUpload>  mExpenseBillDocumentMultipartUploadList;
		
		SyncExpensesTask(ArrayList<ExpenseBillDocumentMultipartUpload>  inExpenseBillDocumentMultipartUploadList)
		{
			mExpenseBillDocumentMultipartUploadList  = inExpenseBillDocumentMultipartUploadList;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {

				progressDialog = SSCProgressDialog.ctor(getActivity());
			} else {

				progressDialog = new ProgressDialog(getActivity());
				progressDialog.setProgress(0);
				progressDialog.setMax(100);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setMessage("Sync Documents");
				progressDialog.setCancelable(false);
			}

			if (!getActivity().isFinishing()) {
				progressDialog.show();
			}

		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try 
			{
				
				for(int i = 0 ; i < mExpenseBillDocumentMultipartUploadList.size() ; i++)
				{
					ExpenseBillDocumentMultipartUpload expenseBillDocumentMultipartUpload = mExpenseBillDocumentMultipartUploadList.get(i);
					
					if(expenseBillDocumentMultipartUpload.mDocumentFilePath != null && !TextUtils.isEmpty(expenseBillDocumentMultipartUpload.mDocumentFilePath) )
					    multipartExpenseDocumentUploadRepeater(expenseBillDocumentMultipartUpload);
				
					/*if (!isSuccesse) {
						return false;
					}*/
					
				}
				
				
				// callWebServiceForGetALLServerExpenses();
				
						
			}
			catch (Exception e)		
			{
			}
			finally
			{
				callWebServiceForGetALLServerExpenses();
			}
				
			
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (progressDialog != null) {
				progressDialog.dismiss();		
				
			}

	     	}
		}
   
  
   ArrayList<ExpenseBillDocumentMultipartUpload> processExpenseSubmitResponse(String result)
   {
	   ArrayList<ExpenseBillDocumentMultipartUpload>  expenseBillDocumentMultipartUploadList = null;

		if (result != null) {

			ResponseDto response = new FetchingdataParser(getActivity()).getResponseResult(result.toString());

			if (response.isSuccess()) {

				try {
					JSONArray expenseResultJSONArray = new JSONArray(
							response.getResult());

					int count = expenseResultJSONArray
							.length();
					 
                    ArrayList<ExpenseSubmitResponse> expenseSubmitResponseList = new ArrayList<ExpenseSubmitResponse>(); 
					
					for (int i = 0; i < count; i++) {
						
						ExpenseSubmitResponse expenseSubmitResponse = new ExpenseSubmitResponse();

						JSONObject expenseResultJsonObject = expenseResultJSONArray
								.getJSONObject(i);
											
						expenseSubmitResponse.miEMSExpenseDetailIDClient = expenseResultJsonObject
								.getInt("EMSExpenseDetailIDClient");

						expenseSubmitResponse.miEMSExpenseDetailIDServer = expenseResultJsonObject
								.getInt("EMSExpenseDetailIDServer");
											
						
						ArrayList<BillSubmitResponse> billSubmitResponseList = new ArrayList<BillSubmitResponse>();
						
						JSONArray emsBillDetailsArray =  expenseResultJsonObject.getJSONArray("EMSBillDetails");
						
						for(int j = 0; j < emsBillDetailsArray.length() ; j++)
						{
							BillSubmitResponse billSubmitResponse = new BillSubmitResponse();
							
							JSONObject emsBillDetailsJsonObject   =  emsBillDetailsArray.getJSONObject(j);
							
							billSubmitResponse.miEMSBillDetailIDClient = emsBillDetailsJsonObject.getInt("EMSBillDetailIDClient");
							
							billSubmitResponse.miEMSBillDetailIDServer = emsBillDetailsJsonObject.getInt("EMSBillDetailIDServer");
							
							
							ArrayList<BillDocumentSubmitResponse> billDocumentSubmitResponseList = new ArrayList<BillDocumentSubmitResponse>();
							
							JSONArray emsBillDocumentDetailsArray =  emsBillDetailsJsonObject.getJSONArray("EMSBillDocumentDetails"); 
							
							for(int k = 0; k < emsBillDocumentDetailsArray.length() ; k++)
							{
								BillDocumentSubmitResponse billDocumentSubmitResponse = new BillDocumentSubmitResponse();
								
								JSONObject emsBillDocumentDetailsJsonObject   = emsBillDocumentDetailsArray.getJSONObject(k);
								
								billDocumentSubmitResponse.miEMSBillDocumentDetailIDClient = emsBillDocumentDetailsJsonObject.getInt("EMSBillDocumentDetailIDClient");
								
								billDocumentSubmitResponse.miEMSBillDocumentDetailIDServer = emsBillDocumentDetailsJsonObject.getInt("EMSBillDocumentDetailIDServer");
							   
								billDocumentSubmitResponseList.add(billDocumentSubmitResponse);
							
							}
							
							billSubmitResponse.mBillDocumentSubmitResponseList = billDocumentSubmitResponseList;
							
							billSubmitResponseList.add(billSubmitResponse);
						
						}					
						
						expenseSubmitResponse.mBillSubmitResponseList =  billSubmitResponseList;
						
						expenseSubmitResponseList.add(expenseSubmitResponse);
					}

					 expenseBillDocumentMultipartUploadList = DatabaseHelper.getConnection(getActivity()).updateExpenseServerKeyAndGetUploadMultipartDocument(expenseSubmitResponseList);

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		}

		return expenseBillDocumentMultipartUploadList;
		 //callWebServiceForGetALLServerExpenses();
	
   }
    
   private void callWebServiceForGetALLServerExpenses()
   {
	   try {
	     
		    JSONObject expensesListJsonObject = new JSONObject();
		    
		    expensesListJsonObject.put("userID", String.valueOf(Helper.getStringValuefromPrefs(getActivity(), SharedPreferencesKey.PREF_USERID)));
	  
		    expensesListJsonObject.put("emsExpenseDetailID", null);
			    
		    PostDataToNetwork dataToNetwork = new PostDataToNetwork(
					getActivity(), "Sync Expenses",
					new GetDataCallBack() {

						@Override
						public void processResponse(Object result) {
							
							SQLiteDatabase db = null;
							 
							if (result != null) {
								
								
							try {
						
								db = DatabaseHelper.getConnection(getActivity()).getWritableDatabase();
								
								ResponseDto response = new FetchingdataParser(getActivity()).getResponseResult(result.toString());

								if (response.isSuccess()) {
									
									int userID = Integer.parseInt(Helper.getStringValuefromPrefs(getActivity(), SharedPreferencesKey.PREF_USERID)); // getExpenseListJsonObject.getInt("userID");
									
									
									
									JSONArray expensejSONArray = new JSONArray(
											response.getResult());
									
								//JSONObject getExpenseListJsonObject = new JSONObject(result.toString());
									
									
								//JSONArray expensejSONArray = getExpenseListJsonObject.getJSONArray("emsExpenseDetail");
									
								 if(expensejSONArray != null && expensejSONArray.length() > 0)
								 {
									
									//String roleID =  getExpenseListJsonObject.getString("roleID");
										
										 synchronized (db) {
											
											 
											 db.delete(TableNames.TABLE_EMS_EXPENSE_DETAIL, EMSExpenseDetailTableColumns.KEY_EMSEXPENSE_DETAIL_ID_SERVER + "<>?", new String[] { String.valueOf("0") });	
												
											 db.delete(TableNames.TABLE_EMS_BILL_DETAIL, EMSBillDetailTableColumns.KEY_EMS_BILL_DETAIL_ID_SERVER + "<>?", new String[] { String.valueOf("0") });	
											
											 db.delete(TableNames.TABLE_EMS_BILL_DOCUMENT_DETAIL, EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DOCUMENT_DETAIL_ID_SERVER + "<>?", new String[] { String.valueOf("0") });	
											  
											 
											 
											/* db.delete(TableNames.TABLE_EMS_EXPENSE_DETAIL, null, null);
											 db.delete(TableNames.TABLE_EMS_BILL_DETAIL, null, null);	
											 db.delete(TableNames.TABLE_EMS_BILL_DOCUMENT_DETAIL, null, null);	*/
											 
											/*db.execSQL("DELETE * FROM "+ TableNames.TABLE_EMS_EXPENSE_DETAIL);
											db.execSQL("DELETE * FROM "+ TableNames.TABLE_EMS_BILL_DETAIL);
											db.execSQL("DELETE * FROM "+ TableNames.TABLE_EMS_BILL_DOCUMENT_DETAIL);*/
				
										}
								
								}
																	
									
									
									for(int i = 0; i <expensejSONArray.length() ; i++)
									{		
										
										JSONObject expenseJsonObject = expensejSONArray.getJSONObject(i);
													
										 ContentValues emsExpenseDetailValues = new ContentValues();
										 
										 emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMSEXPENSE_DETAIL_ID_SERVER, expenseJsonObject.getLong("EMSExpenseDetailID")); // EMSExpenseDetailIDServer
										 emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_TYPE_MASTER_ID,expenseJsonObject.getInt("EMSExpenseTypeMasterID"));									 
										 emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_PENDING_WITH,expenseJsonObject.getString("PendingWith"));					 
								 
										 emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_SUBMITTED_DATE, Helper.dateStringToLong(expenseJsonObject.getString("ExpenseDate")));
						
										// emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_ID,expenseJsonObject.getLong("EMSExpenseDetailIDClient"));					 
										 						 									
										 emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_BILLABLE, expenseJsonObject.getBoolean("Billable"));	
										 										 
										 emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_BILLABLE_TO,expenseJsonObject.getString("BillableTo"));
										 
									     emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_COMMENT, expenseJsonObject.getString("Comment"));
										 
										 emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_STATUS, expenseJsonObject.getInt("Status"));
									 
										 
										 int assignedToUserID = EMSConstants.ASSIGNED_STATUS_NOT_ASSIGNED;

										 if(expenseJsonObject.has("AssignedToUser") && !expenseJsonObject.isNull("AssignedToUser") ) 
										    assignedToUserID = expenseJsonObject.getInt("AssignedToUser");
										 
										 emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_ASSIGNED_TO_USER, assignedToUserID);
										
										 int assignedStatus = expenseJsonObject.getInt("AssignedStatus");
										 
										 emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_ASSIGNED_STATUS, assignedStatus);
											
										 int createdBy = expenseJsonObject.getInt("CreatedBy");
										 
										 emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_CREATED_BY, createdBy);
											
							
										 emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_IS_DELETED, 0);
										 emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_IS_EXPENSE_UPDATED, 0);
										 emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_IS_ACTIVE, 1);	
					
										 
										 if(userID == createdBy) 
										 {


										//	 if(assignedStatus == EMSConstants.ASSIGNED_STATUS_PENDING || assignedStatus == EMSConstants.ASSIGNED_STATUS_APPROVAL_NOT_REQUIRED || assignedStatus == EMSConstants.ASSIGNED_STATUS_CANCELD || assignedToUserID == EMSConstants.ASSIGNED_STATUS_NOT_ASSIGNED)
										 //   {
												emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_TAB_ID, EMSConstants.EXPENSE_TAB_SUBMITTED);
										//    }
										 }
										 else if(userID == assignedToUserID)
											 {
												   
											      if(assignedStatus == EMSConstants.ASSIGNED_STATUS_PENDING || assignedStatus == EMSConstants.ASSIGNED_STATUS_CONSENT)
											      {
											    		 emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_TAB_ID, EMSConstants.EXPENSE_TAB_PENDING);
															
											      }
											      else if(assignedStatus == EMSConstants.ASSIGNED_STATUS_APPROVED || assignedStatus == EMSConstants.ASSIGNED_STATUS_REJECTED)
											      {
											    		 emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_TAB_ID, EMSConstants.EXPENSE_TAB_APPROVED);						
											      }
											      else if(assignedStatus == EMSConstants.ASSIGNED_STATUS_NOTIFICATIONS)
											      {
											    		 emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_TAB_ID, EMSConstants.EXPENSE_TAB_NOTIFICATIONS);
															
											      }
											 }
											 

													
										 long emsExpenseDetailID = db.insert(TableNames.TABLE_EMS_EXPENSE_DETAIL, null, emsExpenseDetailValues);						
										
                                        JSONArray billJSONArray = expenseJsonObject.getJSONArray("EMSBillDetails");
                                         
                                        for(int j = 0; j < billJSONArray.length() ; j++)
     									{
                                        	JSONObject billJsonObject = billJSONArray.getJSONObject(j);


											/*if( billJsonObject.getBoolean("IsDeleted") && !billJsonObject.isNull("EMSBillDetailID"))
												continue;*/
                                           	
                                        	ContentValues emsBillDetailValues = new ContentValues();
                    						emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_EMS_BILL_DETAIL_ID_SERVER, billJsonObject.getLong("EMSBillDetailID"));   //EMSBillDetailIDServer
                    						
                    					//	emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_EMS_BILL_DETAIL_ID, billJsonObject.getLong("EMSBillDetailIDClient"));         						
                    						
                    						emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_ID, emsExpenseDetailID);
                    						                 						
                    						emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_BILL_DATE, Helper.dateStringToLong(billJsonObject.getString("BillDateStr")));
                    						emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_BILL_NO, billJsonObject.getString("BillNo"));
                    						emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_DESCRIPTION,billJsonObject.getString("Description"));						
                    						emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_AMOUNT, Double.parseDouble(billJsonObject.getString("Amount")));
                    						
                    						emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_IS_ACTIVE, billJsonObject.getBoolean("IsActive"));


                    						emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_IS_DELETED, billJsonObject.getBoolean("IsDeleted"));
                    						
                    						
                    						 Uri billURI = getActivity().getContentResolver().insert(ProviderContract.URI_EMS_BILL_DETAIL, emsBillDetailValues);
                    						
                    						 long emsBillDetailID = ContentUris.parseId(billURI);
                      	
                                        	 JSONArray billDocumentJSONArray = billJsonObject.getJSONArray("EMSBillDocumentDetails");
                                        	
                                        	 ContentValues[] emsBillDocumentListValues = new ContentValues[billDocumentJSONArray.length()];



                                        	for( int k = 0;k < billDocumentJSONArray.length() ; k++)
                                        	{
                                        	     JSONObject billDocumentJSONObject =  billDocumentJSONArray.getJSONObject(k);
                                        			  
                                        		 ContentValues emsBillDocumentValues = new ContentValues();
              									
              									emsBillDocumentValues.put(EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DOCUMENT_DETAIL_ID_SERVER, billDocumentJSONObject.getLong("EMSBillDocumentDetailID")); // EMSBillDocumentDetailIDServer
              									
              									// emsBillDocumentValues.put(EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DOCUMENT_DETAIL_ID, billDocumentJSONObject.getLong("EMSBillDocumentDetailIDClient"));
              									
              									
              									emsBillDocumentValues.put(EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DETAIL_ID, emsBillDetailID);
              									
              									emsBillDocumentValues.put(EMSBillDocumentDetailTableColumns.KEY_DOCUMENT_NAME, billDocumentJSONObject.getString("DocumentName"));					
              								
              									// emsBillDocumentValues.put(EMSBillDocumentDetailTableColumns.KEY_DOCUMENT_PATH, "Document_BASE_URL/"+billDocumentJSONObject.getString("DocumentName"));
              									
              									emsBillDocumentValues.put(EMSBillDocumentDetailTableColumns.KEY_IS_ACTIVE, billDocumentJSONObject.getBoolean("IsActive"));
              									emsBillDocumentValues.put(EMSBillDocumentDetailTableColumns.KEY_IS_DELETED,  billDocumentJSONObject.getBoolean("IsDeleted"));
              									
              									emsBillDocumentListValues[k] = emsBillDocumentValues;
              								
                                         	}
                                        	
                                        	getActivity().getContentResolver().bulkInsert(
            										ProviderContract.URI_EMS_BILL_DOCUMENT_DETAIL,
            										emsBillDocumentListValues);
                                        	
     									}
									
									}
									
								}
								 
							}
							catch (SQLException e) {
								Helper.printStackTrace(e);
								e.printStackTrace();
							}   catch (JSONException e) {
									// TODO Auto-generated catch block
								    Helper.printStackTrace(e);
									e.printStackTrace();
								}
							catch (Exception e) {
								Helper.printStackTrace(e);
								e.printStackTrace();
							}
							
							finally {
								if (db != null && db.isOpen()) {
									db.close();
								}
												
								}
															
								
							}
						

							getExpensesDataFromDatabase();
						}
					});
			
			dataToNetwork.setConfig(getString(R.string.url),
					WebMethod.GET_EXPENSE_LIST);
			
			dataToNetwork.execute(expensesListJsonObject);
		    
	   
	   } catch (Exception e) {
			e.printStackTrace();
		}
   }
   
   
   boolean uploadExpensesData(ArrayList<EMSExpenseDetail> emsExpenseDetailArrayList  ) {

		try {
					
			JSONObject expensesListJsonObject = new JSONObject();
			expensesListJsonObject.put("userID", String.valueOf(Helper.getStringValuefromPrefs(getActivity(), SharedPreferencesKey.PREF_USERID)));
			expensesListJsonObject.put("roleID", String.valueOf(Helper.getIntValueFromPrefs(getActivity(), SharedPreferencesKey.PREF_ROLEID)));
			
			JSONArray emsExpenseDetailJsonArray = new JSONArray();
			
			for (int i = 0; i < emsExpenseDetailArrayList.size(); i++) {
				
				EMSExpenseDetail emsExpenseDetailObject = emsExpenseDetailArrayList.get(i);
				
				JSONObject expenseDetailJsonObject = new JSONObject();
				
				expenseDetailJsonObject.put("EMSExpenseDetailIDServer",emsExpenseDetailObject.miEMSExpenseDetailIDServer);
				expenseDetailJsonObject.put("EMSExpenseTypeMasterID",emsExpenseDetailObject.miEMSExpenseTypeMasterID);
				expenseDetailJsonObject.put("EMSExpenseDetailIDClient",emsExpenseDetailObject.miEMSExpenseDetailID);
				expenseDetailJsonObject.put("Billable",emsExpenseDetailObject.mbBillable);
				expenseDetailJsonObject.put("BillableTo",emsExpenseDetailObject.mBillableTo);
				expenseDetailJsonObject.put("Comment",emsExpenseDetailObject.mComment);
				
				if(emsExpenseDetailObject.miExpenseStatus == 0)
					expenseDetailJsonObject.put("Status",1);
				else
				     expenseDetailJsonObject.put("Status",emsExpenseDetailObject.miExpenseStatus);
				
				JSONArray emsBillDetailJsonArray = new JSONArray();
				
				for( int j = 0 ; j < emsExpenseDetailObject.mEMSBillDetailList.size();j++)
				{
					
					EMSBillDetail emsBillDetail = emsExpenseDetailObject.mEMSBillDetailList.get(j);	
					
					JSONObject emsBillDetailJsonObject = new JSONObject();
					
					emsBillDetailJsonObject.put("EMSBillDetailIDClient", emsBillDetail.mEMSBillDetailID);
					emsBillDetailJsonObject.put("EMSBillDetailIDServer", emsBillDetail.mEMSBillDetailIDServer);
					
					emsBillDetailJsonObject.put("EMSExpenseDetailIDServer", emsExpenseDetailObject.miEMSExpenseDetailIDServer);
					
					emsBillDetailJsonObject.put("BillDateStr", Helper.longDateToString(emsBillDetail.mBillDate));
					
					emsBillDetailJsonObject.put("BillNo", emsBillDetail.mBillNo);
					
					emsBillDetailJsonObject.put("Description", emsBillDetail.mDescription);
					
					emsBillDetailJsonObject.put("Amount", emsBillDetail.mAmount);
					
					emsBillDetailJsonObject.put("IsActive", emsBillDetail.mIsActive);
								
					emsBillDetailJsonObject.put("IsDeleted", emsBillDetail.mIsDeleted);
					
					JSONArray emsBillDocumentDetailJsonArray = new JSONArray();
								
					for( int k = 0 ; k < emsBillDetail.mEMSBillDocumentDetailList.size() ; k++)
					{
						EMSBillDocumentDetail emsBillDocumentDetail = emsBillDetail.mEMSBillDocumentDetailList.get(k);
						
						JSONObject emsBillDocumentDetailJsonObject = new JSONObject();
						
						emsBillDocumentDetailJsonObject.put("EMSBillDocumentDetailIDClient", emsBillDocumentDetail.mEMSBillDocumentDetailID);						
						emsBillDocumentDetailJsonObject.put("EMSBillDocumentDetailIDServer", emsBillDocumentDetail.mEMSBillDocumentDetailIDServer);					
						emsBillDocumentDetailJsonObject.put("EMSBillDetailIDServer", emsBillDetail.mEMSBillDetailIDServer);
						emsBillDocumentDetailJsonObject.put("DocumentName", emsBillDocumentDetail.mDocumentName);				
						emsBillDocumentDetailJsonObject.put("IsActive", emsBillDocumentDetail.mIsActive);				
						emsBillDocumentDetailJsonObject.put("IsDeleted", emsBillDocumentDetail.mIsDeleted);								
						
						emsBillDocumentDetailJsonArray.put(emsBillDocumentDetailJsonObject);
						
					}
						
					emsBillDetailJsonObject.put("EMSBillDocumentDetails", emsBillDocumentDetailJsonArray);
			
					emsBillDetailJsonArray.put(emsBillDetailJsonObject);
				}
				
				
				expenseDetailJsonObject.put("EMSBillDetails", emsBillDetailJsonArray);
				
				emsExpenseDetailJsonArray.put(expenseDetailJsonObject);
			}
	
			expensesListJsonObject.put("emsExpenseDetail",emsExpenseDetailJsonArray);
			
			
			PostDataToNetwork dataToNetwork = new PostDataToNetwork(
					getActivity(), "Sync Expenses",
					new GetDataCallBack() {

						@Override
						public void processResponse(Object result) {
							if (result != null) {
															
								//
								
								ArrayList<ExpenseBillDocumentMultipartUpload>  expenseBillDocumentMultipartUploadList = processExpenseSubmitResponse(result.toString());
								
								if(expenseBillDocumentMultipartUploadList != null && expenseBillDocumentMultipartUploadList.size() > 0)
								new SyncExpensesTask(expenseBillDocumentMultipartUploadList).execute();
								else
								{
									   callWebServiceForGetALLServerExpenses();
									//getExpensesDataFromDatabase();
								}
								
								//getExpensesDataFromDatabase();
							}

						//	callWebServiceForGetALLServerMOMs();
						}
					});
			
			dataToNetwork.setConfig(getString(R.string.url),
					WebMethod.GET_SUBMIT_EXPENSE_AND_BILL);
			
			dataToNetwork.execute(expensesListJsonObject);
			
				
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}

		return true;
	}
   
   
   
   boolean multipartExpenseDocumentUploadRepeater(ExpenseBillDocumentMultipartUpload expenseBillDocumentMultipartUpload) {

		InputStream fileInputStream = null;
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		try {
			
			String expenseSubmitURL = getString(R.string.url) + WebMethod.GET_UPLOAD_BILL_IMAGE_STREAM;
					
			conn = getServerConn(expenseSubmitURL, boundary);
			dos = new DataOutputStream(conn.getOutputStream());
			
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"userID\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(String.valueOf(Helper.getStringValuefromPrefs(getActivity(), SharedPreferencesKey.PREF_USERID))); //  HARD CODED CHANGE IT
			dos.writeBytes(lineEnd);
					
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"roleID\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(String.valueOf(Helper.getIntValueFromPrefs(getActivity(), SharedPreferencesKey.PREF_ROLEID)));  // //  HARD CODED CHANGE IT
			dos.writeBytes(lineEnd);		
			
		
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"EMSExpenseDetailIDServer\""
						+ lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(String.valueOf(expenseBillDocumentMultipartUpload.miEMSExpenseDetailIDServer));
				dos.writeBytes(lineEnd);
							
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"EMSBillDetailIDServer\""
							+ lineEnd);
					
					dos.writeBytes(lineEnd);
					dos.writeBytes(String.valueOf(expenseBillDocumentMultipartUpload.miEMSBillDetailIDServer));
					dos.writeBytes(lineEnd);	
										
						
						dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
						dos.writeBytes("Content-Disposition: form-data; name=\"EMSBillDocumentDetailIDServer\""
								+ lineEnd);
						dos.writeBytes(lineEnd);
						dos.writeBytes(String.valueOf(expenseBillDocumentMultipartUpload.miEMSBillDocumentDetailIDServer));
						dos.writeBytes(lineEnd);
								
						
						String filepath = expenseBillDocumentMultipartUpload.mDocumentFilePath;
						File sourceFile = new File(filepath);

						String sourcefileName = sourceFile.toString();
						
						if (!sourcefileName.equals("")) {

							if (sourceFile.exists()) {

								fileInputStream = getStream(sourceFile);

								dos.writeBytes(twoHyphens + boundary + twoHyphens
										+ lineEnd);
								dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\""
										+ sourcefileName + "\"" + lineEnd);
								dos.writeBytes(lineEnd);
								bytesAvailable = fileInputStream.available();
								bufferSize = Math.min(bytesAvailable, maxBufferSize);
								buffer = new byte[bufferSize];
								bytesRead = fileInputStream.read(buffer, 0, bufferSize);
								while (bytesRead > 0) {

									dos.write(buffer, 0, bufferSize);
									bytesAvailable = fileInputStream.available();
									bufferSize = Math
											.min(bytesAvailable, maxBufferSize);
									bytesRead = fileInputStream.read(buffer, 0,
											bufferSize);

								}
								
								dos.writeBytes(lineEnd);

								closeInputStream(fileInputStream);
							}
						}
							
			dos.writeBytes(twoHyphens + boundary + twoHyphens + twoHyphens
					+ lineEnd);

			int serverResponseCode = conn.getResponseCode();
			
			String serverResponseMessage = conn.getResponseMessage();

			if (!serverResponseMessage.equalsIgnoreCase("OK")) {

				//Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_LONG).show();			
				
				return false;

			} else {
				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);
				
				//Toast.makeText(getActivity(), "Upload Success HTTP Response is "+serverResponseCode, Toast.LENGTH_LONG).show();
			}
			dos.flush();
				
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}

		return true;
	}

}