package com.samsung.ssc;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.adapters.BeatCreationAdapter;
import com.samsung.ssc.adapters.BeatDetailsAdapter;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.constants.WebConfig.WebMethod;
import com.samsung.ssc.dto.BeatCoverageDto;
import com.samsung.ssc.dto.BeatCreationDto;
import com.samsung.ssc.dto.ResponseDto;
import com.samsung.ssc.dto.UserBeatDetailDto;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.io.VolleyGetDataCallBack;
import com.samsung.ssc.io.VolleyPostDataToNetwork;
import com.samsung.ssc.util.Helper;

/***
 * beat creations
 * 
 * @author Ashish
 * 
 */
public class BeatCreation extends BaseActivity implements OnClickListener {

	private ArrayList<String> workingDatestosend;
	private ArrayList<String> workingDatesdisplay;
	private ListView lvDealersBeatList;
	private Spinner beatDateSpinner;
	private Button save, cancel, submitbeat, editbeat;
	private ArrayList<BeatCreationDto> beatsListData;
	private BeatCreationAdapter adapterDealersBeatList;
	private ImageView leaveCalender;
	private String displayDate;
	private ArrayList<String> weekOffDates;
	private double month;
	private int year;
	private Dialog summaryDialog;
	private TextView tvTotalBeatCount;
	protected boolean isexceptionflag = false;
	private Spinner dealerTypeSpinner;
	private ArrayList<String> listallDealer;
	private ArrayList<String> tempList;
	private ArrayList<BeatCreationDto> listDealerBeatsDataSet;
	// private int position;
	// private int clickcount = 0;

	private boolean flagcheckcase = false;
	private EditText edit_text_view;

	@Override
	public void init() {
		super.init();
		setContentView(R.layout.beat_creation1);
		// setCentretext(getString(R.string.beatcreation));
		leaveCalender = (ImageView) findViewById(R.id.leaveCalendar);
		leaveCalender.setOnClickListener(BeatCreation.this);
		beatDateSpinner = (Spinner) findViewById(R.id.beatsDate);
		dealerTypeSpinner = (Spinner) findViewById(R.id.beatsdealer_type_spinner);
		tvTotalBeatCount = (TextView) findViewById(R.id.tvTotalBeatCount);
		edit_text_view = (EditText) findViewById(R.id.edit_text_view);

		workingDatestosend = new ArrayList<String>();
		workingDatesdisplay = new ArrayList<String>();
		listallDealer = new ArrayList<String>();
		listDealerBeatsDataSet = new ArrayList<BeatCreationDto>();
		tempList = new ArrayList<String>();
		weekOffDates = new ArrayList<String>();
		lvDealersBeatList = (ListView) findViewById(R.id.listView);
		LayoutInflater inflater = getLayoutInflater();

		LinearLayout listfooterView = (LinearLayout) inflater.inflate(
				R.layout.buttons, null);
		lvDealersBeatList.addFooterView(listfooterView);

		save = (Button) listfooterView.findViewById(R.id.submit);
		save.setOnClickListener(BeatCreation.this);

		cancel = (Button) listfooterView.findViewById(R.id.cancel);
		cancel.setOnClickListener(BeatCreation.this);

		edit_text_view
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {

							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									edit_text_view.getWindowToken(), 0);

							return true;
						}
						return false;
					}
				});
		edit_text_view.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int start, int before,
					int count) {

				int textlength = cs.length();
				ArrayList<BeatCreationDto> tempArrayList = new ArrayList<BeatCreationDto>();
				for (BeatCreationDto c : listDealerBeatsDataSet) {
					if (textlength <= c.getStoreName().length()) {

						if (c.getStoreName()
								.toLowerCase(Locale.ENGLISH)
								.startsWith(
										cs.toString().toLowerCase(
												Locale.ENGLISH))) {
							tempArrayList.add(c);
						}
					}
				}
				adapterDealersBeatList = new BeatCreationAdapter(
						BeatCreation.this, tempArrayList, year, month, tvTotalBeatCount);
				lvDealersBeatList.setAdapter(adapterDealersBeatList);
				adapterDealersBeatList.setSelectedSpinnerdata(workingDatestosend
						.get(beatDateSpinner.getSelectedItemPosition()));
				adapterDealersBeatList.setTotal(tvTotalBeatCount);
				adapterDealersBeatList.notifyDataSetChanged();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		if (Helper.isOnline(BeatCreation.this)) {

			checkCoverageWindow();

			storeList();
		} else {

			Helper.showAlertDialog(BeatCreation.this,
					SSCAlertDialog.ERROR_TYPE,
					getString(R.string.error1), getString(R.string.error2),
					getString(R.string.ok),
					new SSCAlertDialog.OnSDAlertDialogClickListener() {

						@Override
						public void onClick(SSCAlertDialog sdAlertDialog) {
							sdAlertDialog.dismiss();
							finish();

						}
					}, false, null, null);
		}

	}

	private void storeList() {
		PostDataToNetwork pdn = new PostDataToNetwork(BeatCreation.this,
				getString(R.string.loadingmessage), new GetDataCallBack() {
					@Override
					public void processResponse(Object result) {
						if (result != null) {
							beatsListData = new FetchingdataParser(
									BeatCreation.this).getStorelist(result
									.toString());
							if (beatsListData.size() > 0) {

								beatDateSpinner
										.setOnTouchListener(new OnTouchListener() {
											@Override
											public boolean onTouch(View v,
													MotionEvent event) {
												if (event.getAction() == MotionEvent.ACTION_UP
														&& workingDatesdisplay
																.size() == 0) {

													Helper.showAlertDialog(
															BeatCreation.this,
															SSCAlertDialog.WARNING_TYPE,
															getString(R.string.error3),
															getString(R.string.selectdatefirst),
															getString(R.string.ok),
															new SSCAlertDialog.OnSDAlertDialogClickListener() {

																@Override
																public void onClick(
																		SSCAlertDialog sdAlertDialog) {
																	sdAlertDialog
																			.dismiss();

																}
															}, false, null,
															null);

													return true;
												}
												return false;
											}
										});

								dealerTypeSpinner
										.setOnTouchListener(new OnTouchListener() {
											@Override
											public boolean onTouch(View v,
													MotionEvent event) {
												if (event.getAction() == MotionEvent.ACTION_UP
														&& tempList.size() == 0) {

													Helper.showAlertDialog(
															BeatCreation.this,
															SSCAlertDialog.WARNING_TYPE,
															getString(R.string.error3),
															getString(R.string.selectdatefirst),
															getString(R.string.ok),
															new SSCAlertDialog.OnSDAlertDialogClickListener() {

																@Override
																public void onClick(
																		SSCAlertDialog sdAlertDialog) {
																	sdAlertDialog
																			.dismiss();

																}
															}, false, null,
															null);

													return true;
												}
												return false;
											}
										});

								dealerTypeSpinner
										.setOnItemSelectedListener(new OnItemSelectedListener() {

											@Override
											public void onItemSelected(
													AdapterView<?> parent,
													View convertView,
													int position, long id) {
												edit_text_view.setText("");
												listDealerBeatsDataSet.clear();
												TextView tvSpinnerOption = (TextView) convertView;
												String channelText = tvSpinnerOption
														.getText().toString()
														.trim();

												for (BeatCreationDto beatCreationDto : beatsListData) {
													String channelType = beatCreationDto
															.getChannelType()
															.trim();
													if (channelType
															.equalsIgnoreCase(channelText)) {
														listDealerBeatsDataSet
																.add(beatCreationDto);
													}
												}
												adapterDealersBeatList = null;
												adapterDealersBeatList = new BeatCreationAdapter(
														BeatCreation.this,
														listDealerBeatsDataSet,
														year, month, tvTotalBeatCount);
												lvDealersBeatList
														.setAdapter(adapterDealersBeatList);
												adapterDealersBeatList
														.setSelectedSpinnerdata(workingDatestosend
																.get(beatDateSpinner
																		.getSelectedItemPosition()));
												adapterDealersBeatList
														.setTotal(tvTotalBeatCount);

												if (flagcheckcase) {

													for (int i = 0; i < listDealerBeatsDataSet
															.size(); i++) {
														listDealerBeatsDataSet
																.get(i)
																.setStatus(
																		false);
													}
												}
												flagcheckcase = false;

												adapterDealersBeatList
														.notifyDataSetChanged();

											}

											@Override
											public void onNothingSelected(
													AdapterView<?> arg0) {

											}
										});
								beatDateSpinner
										.setOnItemSelectedListener(new OnItemSelectedListener() {

											@Override
											public void onItemSelected(
													AdapterView<?> arg0,
													View arg1, int arg2,
													long arg3) {
												// BeatCreationAdapter.i=0;
												edit_text_view.setText("");
												adapterDealersBeatList = null;
												flagcheckcase = true;

												adapterDealersBeatList = new BeatCreationAdapter(
														BeatCreation.this,
														listDealerBeatsDataSet,
														year, month, tvTotalBeatCount);
												lvDealersBeatList
														.setAdapter(adapterDealersBeatList);
												adapterDealersBeatList
														.setSelectedSpinnerdata(workingDatestosend
																.get(beatDateSpinner
																		.getSelectedItemPosition()));
												adapterDealersBeatList
														.setTotal(tvTotalBeatCount);
												for (int i = 0; i < listDealerBeatsDataSet
														.size(); i++) {
													listDealerBeatsDataSet.get(
															i).setStatus(false);
												}
												adapterDealersBeatList
														.notifyDataSetChanged();

												// adapter = new
												// BeatCreationAdapter(
												// BeatCreation.this,
												// list, year, month,
												// total);
												// beatList.setAdapter(adapter);
												// adapter.setSelectedSpinnerdata(workingDatestosend.get(beatDateSpinner
												// .getSelectedItemPosition()));
												// adapter.setTotal(total);
												// for (int i = 0; i <
												// list.size(); i++) {
												// list.get(i)
												// .setStatus(false);
												// }
												// adapter.notifyDataSetChanged();

											}

											@Override
											public void onNothingSelected(
													AdapterView<?> arg0) {

											}
										});

							} else {

								Helper.showAlertDialog(
										BeatCreation.this,
										SSCAlertDialog.WARNING_TYPE,
										getString(R.string.error3),
										getString(R.string.datanotavailable),
										getString(R.string.ok),
										new SSCAlertDialog.OnSDAlertDialogClickListener() {

											@Override
											public void onClick(
													SSCAlertDialog sdAlertDialog) {
												sdAlertDialog.dismiss();
												finish();
											}
										}, false, null, null);
							}

						}

						else {
						
							
							Helper.showCustomToast(BeatCreation.this, R.string.networkserverbusy, 	Toast.LENGTH_LONG);
							
							
						}
					}
				});
		JSONObject jsonobj = new JSONObject();
		try {
			jsonobj.put(WebConfig.WebParams.USER_ID, Helper.getStringValuefromPrefs(
					BeatCreation.this, SharedPreferencesKey.PREF_USERID));
			Helper.printLog("result GetUserStores", jsonobj.toString());

		} catch (JSONException e) {
			Helper.printStackTrace(e);
		}
		pdn.setConfig(getString(R.string.url), "GetUserStores");
		pdn.execute(jsonobj);

		/*VolleyPostDataToNetwork pdn=new VolleyPostDataToNetwork(BeatCreation.this, getString(R.string.loadingmessage), new VolleyGetDataCallBack() {
			
			@Override
			public void processResponse(Object result) {
				if (result != null) {
					beatsListData = new FetchingdataParser(
							BeatCreation.this).getStorelist(result
							.toString());
					if (beatsListData.size() > 0) {

						beatDateSpinner
								.setOnTouchListener(new OnTouchListener() {
									@Override
									public boolean onTouch(View v,
											MotionEvent event) {
										if (event.getAction() == MotionEvent.ACTION_UP
												&& workingDatesdisplay
														.size() == 0) {

											Helper.showAlertDialog(
													BeatCreation.this,
													SmartDostAlertDialog.WARNING_TYPE,
													getString(R.string.error3),
													getString(R.string.selectdatefirst),
													getString(R.string.ok),
													new SmartDostAlertDialog.OnSDAlertDialogClickListener() {

														@Override
														public void onClick(
																SmartDostAlertDialog sdAlertDialog) {
															sdAlertDialog
																	.dismiss();

														}
													}, false, null,
													null);

											return true;
										}
										return false;
									}
								});

						dealerTypeSpinner
								.setOnTouchListener(new OnTouchListener() {
									@Override
									public boolean onTouch(View v,
											MotionEvent event) {
										if (event.getAction() == MotionEvent.ACTION_UP
												&& tempList.size() == 0) {

											Helper.showAlertDialog(
													BeatCreation.this,
													SmartDostAlertDialog.WARNING_TYPE,
													getString(R.string.error3),
													getString(R.string.selectdatefirst),
													getString(R.string.ok),
													new SmartDostAlertDialog.OnSDAlertDialogClickListener() {

														@Override
														public void onClick(
																SmartDostAlertDialog sdAlertDialog) {
															sdAlertDialog
																	.dismiss();

														}
													}, false, null,
													null);

											return true;
										}
										return false;
									}
								});

						dealerTypeSpinner
								.setOnItemSelectedListener(new OnItemSelectedListener() {

									@Override
									public void onItemSelected(
											AdapterView<?> parent,
											View convertView,
											int position, long id) {
										edit_text_view.setText("");
										listDealerBeatsDataSet.clear();
										TextView tvSpinnerOption = (TextView) convertView;
										String channelText = tvSpinnerOption
												.getText().toString()
												.trim();

										for (BeatCreationDto beatCreationDto : beatsListData) {
											String channelType = beatCreationDto
													.getChannelType()
													.trim();
											if (channelType
													.equalsIgnoreCase(channelText)) {
												listDealerBeatsDataSet
														.add(beatCreationDto);
											}
										}
										adapterDealersBeatList = null;
										adapterDealersBeatList = new BeatCreationAdapter(
												BeatCreation.this,
												listDealerBeatsDataSet,
												year, month, tvTotalBeatCount);
										lvDealersBeatList
												.setAdapter(adapterDealersBeatList);
										adapterDealersBeatList
												.setSelectedSpinnerdata(workingDatestosend
														.get(beatDateSpinner
																.getSelectedItemPosition()));
										adapterDealersBeatList
												.setTotal(tvTotalBeatCount);

										if (flagcheckcase) {

											for (int i = 0; i < listDealerBeatsDataSet
													.size(); i++) {
												listDealerBeatsDataSet
														.get(i)
														.setStatus(
																false);
											}
										}
										flagcheckcase = false;

										adapterDealersBeatList
												.notifyDataSetChanged();

									}

									@Override
									public void onNothingSelected(
											AdapterView<?> arg0) {

									}
								});
						beatDateSpinner
								.setOnItemSelectedListener(new OnItemSelectedListener() {

									@Override
									public void onItemSelected(
											AdapterView<?> arg0,
											View arg1, int arg2,
											long arg3) {
										// BeatCreationAdapter.i=0;
										edit_text_view.setText("");
										adapterDealersBeatList = null;
										flagcheckcase = true;

										adapterDealersBeatList = new BeatCreationAdapter(
												BeatCreation.this,
												listDealerBeatsDataSet,
												year, month, tvTotalBeatCount);
										lvDealersBeatList
												.setAdapter(adapterDealersBeatList);
										adapterDealersBeatList
												.setSelectedSpinnerdata(workingDatestosend
														.get(beatDateSpinner
																.getSelectedItemPosition()));
										adapterDealersBeatList
												.setTotal(tvTotalBeatCount);
										for (int i = 0; i < listDealerBeatsDataSet
												.size(); i++) {
											listDealerBeatsDataSet.get(
													i).setStatus(false);
										}
										adapterDealersBeatList
												.notifyDataSetChanged();

										// adapter = new
										// BeatCreationAdapter(
										// BeatCreation.this,
										// list, year, month,
										// total);
										// beatList.setAdapter(adapter);
										// adapter.setSelectedSpinnerdata(workingDatestosend.get(beatDateSpinner
										// .getSelectedItemPosition()));
										// adapter.setTotal(total);
										// for (int i = 0; i <
										// list.size(); i++) {
										// list.get(i)
										// .setStatus(false);
										// }
										// adapter.notifyDataSetChanged();

									}

									@Override
									public void onNothingSelected(
											AdapterView<?> arg0) {

									}
								});

					} else {

						Helper.showAlertDialog(
								BeatCreation.this,
								SmartDostAlertDialog.WARNING_TYPE,
								getString(R.string.error3),
								getString(R.string.datanotavailable),
								getString(R.string.ok),
								new SmartDostAlertDialog.OnSDAlertDialogClickListener() {

									@Override
									public void onClick(
											SmartDostAlertDialog sdAlertDialog) {
										sdAlertDialog.dismiss();
										finish();
									}
								}, false, null, null);
					}

				}

				else {
				
					
					Helper.showCustomToast(BeatCreation.this, R.string.networkserverbusy, 	Toast.LENGTH_LONG);
					
					
				}
				
			}
			
			@Override
			public void onError(VolleyError error) {
				// TODO Auto-generated method stub
				
			}
		});
		
		pdn.setRequestData(jsonobj);
		pdn.setConfig(getString(R.string.url), WebMethod.GET_USER_STORES);
		pdn.callWebService();
		*/
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK && requestCode == 1001) {

			if (data != null) {

				Bundle bundle = data.getBundleExtra("working_dates");
				workingDatestosend = bundle.getStringArrayList("workingDates");
				SimpleDateFormat oldformat = new SimpleDateFormat("dd MM yyyy");

				SimpleDateFormat newformat = new SimpleDateFormat(
						"EEE dd-MMM-yy ");
				DecimalFormat mFormat = new DecimalFormat("00");
				workingDatesdisplay.clear();
				for (String date : workingDatestosend) {
					try {
						String dates = date + " " + mFormat.format(month) + " "
								+ year;
						workingDatesdisplay.add(newformat.format(oldformat
								.parse(dates)));
					} catch (ParseException e) {
						Helper.printStackTrace(e);
					}
				}

				for (BeatCreationDto beatCreationDto : beatsListData) {
					listallDealer.add(beatCreationDto.getChannelType());
					for (String dupWord : listallDealer) {
						if (!tempList.contains(dupWord)) {
							tempList.add(dupWord);
						}
					}

				}

				edit_text_view.setVisibility(View.VISIBLE);

				if (workingDatesdisplay.size() > 0) {
					ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(
							BeatCreation.this,
							android.R.layout.simple_spinner_item,
							workingDatesdisplay);
					weekOffDates = bundle.getStringArrayList("weekOffDates");
					dateAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					beatDateSpinner.setAdapter(dateAdapter);
					if (tempList.size() > 0) {
						ArrayAdapter<String> dateAdapter1 = new ArrayAdapter<String>(
								BeatCreation.this,
								android.R.layout.simple_spinner_item, tempList);
						dateAdapter1
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						dealerTypeSpinner.setPrompt(getResources().getString(
								R.string.dealer_type_spinner));
						dealerTypeSpinner.setAdapter(dateAdapter1);
						// flagcheck = true;
					}

				} else {

					Helper.showAlertDialog(
							BeatCreation.this,
							SSCAlertDialog.WARNING_TYPE,
							getString(R.string.error3),
							getString(R.string.error13),
							getString(R.string.ok),
							new SSCAlertDialog.OnSDAlertDialogClickListener() {

								@Override
								public void onClick(
										SSCAlertDialog sdAlertDialog) {
									sdAlertDialog.dismiss();
									finish();
								}
							}, false, null, null);
				}

			}
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.leaveCalendar:

			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date(System.currentTimeMillis()));
			cal.getTime().getDate();

			Intent intent = new Intent(BeatCreation.this, CalendarView.class);
			intent.putExtra("server_date", "" + displayDate);
			intent.putExtra("is_exception", isexceptionflag);

			// intent.putExtra("current_or_next", serverDate);

			startActivityForResult(intent, 1001);
			if (adapterDealersBeatList != null) {
				adapterDealersBeatList.clearMap();
				adapterDealersBeatList.setTotal(tvTotalBeatCount);
				adapterDealersBeatList.notifyDataSetChanged();
			}
			
			break;

		case R.id.submitbeat:
			if (workingDatestosend.size() > 0) {
				if (adapterDealersBeatList.getdataMap().size() > 0) {

					createBeat();
				} else {

					Helper.showAlertDialog(
							BeatCreation.this,
							SSCAlertDialog.ERROR_TYPE,
							getString(R.string.error3),
							getString(R.string.error8),
							getString(R.string.ok),
							new SSCAlertDialog.OnSDAlertDialogClickListener() {

								@Override
								public void onClick(
										SSCAlertDialog sdAlertDialog) {
									sdAlertDialog.dismiss();

								}
							}, false, null, null);

				}

			} else {

				Helper.showAlertDialog(
						BeatCreation.this,
						SSCAlertDialog.ERROR_TYPE,
						getString(R.string.error3),
						getString(R.string.error8),
						getString(R.string.ok),
						new SSCAlertDialog.OnSDAlertDialogClickListener() {

							@Override
							public void onClick(
									SSCAlertDialog sdAlertDialog) {
								sdAlertDialog.dismiss();

							}
						}, false, null, null);

			}

			break;
		case R.id.cancel:
			Helper.showAlertDialog(
					BeatCreation.this,
					SSCAlertDialog.NORMAL_TYPE,
					getString(R.string.error10),
					getString(R.string.save_msg),
					getString(R.string.ok),
					new SSCAlertDialog.OnSDAlertDialogClickListener() {

						@Override
						public void onClick(SSCAlertDialog sdAlertDialog) {
							sdAlertDialog.dismiss();
							if (adapterDealersBeatList != null) {
								adapterDealersBeatList.clearMap();
							}
							finish();

						}
					}, true, getString(R.string.cancel),
					new SSCAlertDialog.OnSDAlertDialogClickListener() {

						@Override
						public void onClick(SSCAlertDialog sdAlertDialog) {
							sdAlertDialog.dismiss();
						}
					});

			break;

		case R.id.submit:
			if (workingDatestosend.size() > 0) {
				if (adapterDealersBeatList.getdataMap().size() > 0) {
					// summaryDialog(adapter.getdataMap(), list);
					summaryDialog(
							getStoredetails(adapterDealersBeatList
									.getdataMapinverse()),
							adapterDealersBeatList.getdataMap().size(),
							beatsListData.size());

					// SummaryTask summary=new SummaryTask();
					// summary.setdata(BeatCreation.this,
					// list,adapter.getdateMap());
					// summary.execute();
				} else {

					Helper.showAlertDialog(
							BeatCreation.this,
							SSCAlertDialog.ERROR_TYPE,
							getString(R.string.error3),
							getString(R.string.error8),
							getString(R.string.ok),
							new SSCAlertDialog.OnSDAlertDialogClickListener() {

								@Override
								public void onClick(
										SSCAlertDialog sdAlertDialog) {
									sdAlertDialog.dismiss();

								}
							}, false, null, null);

				}

			} else {

				Helper.showAlertDialog(
						BeatCreation.this,
						SSCAlertDialog.ERROR_TYPE,
						getString(R.string.error3),
						getString(R.string.error8),
						getString(R.string.ok),
						new SSCAlertDialog.OnSDAlertDialogClickListener() {

							@Override
							public void onClick(
									SSCAlertDialog sdAlertDialog) {
								sdAlertDialog.dismiss();

							}
						}, false, null, null);

			}
			break;

		case R.id.editbeat:
			summaryDialog.dismiss();
			break;

		}

	}

	private void createBeat() {

		PostDataToNetwork pdn = new PostDataToNetwork(BeatCreation.this,
				getString(R.string.loadingmessage), new GetDataCallBack() {
					@Override
					public void processResponse(Object result) {
						if (result != null) {
							ResponseDto obj = new FetchingdataParser(
									BeatCreation.this).getResponseResult(result
									.toString());
							if (obj.isSuccess()) {

								Helper.showAlertDialog(
										BeatCreation.this,
										SSCAlertDialog.SUCCESS_TYPE,
										getString(R.string.sucess),
										obj.getMessage(),
										getString(R.string.ok),
										new SSCAlertDialog.OnSDAlertDialogClickListener() {

											@Override
											public void onClick(
													SSCAlertDialog sdAlertDialog) {
												sdAlertDialog.dismiss();
												adapterDealersBeatList
														.clearMap();
												summaryDialog.dismiss();
												finish();

											}
										}, false, null, null);

							} else {

								Helper.showAlertDialog(
										BeatCreation.this,
										SSCAlertDialog.ERROR_TYPE,
										getString(R.string.error3),
										obj.getMessage(),
										getString(R.string.ok),
										new SSCAlertDialog.OnSDAlertDialogClickListener() {

											@Override
											public void onClick(
													SSCAlertDialog sdAlertDialog) {
												sdAlertDialog.dismiss();
											}
										}, false, null, null);

							}
						}

						else {

							Helper.showCustomToast(BeatCreation.this,
									R.string.networkserverbusy,
									Toast.LENGTH_LONG);

						}
					}
				});
		JSONObject jsonobj = new JSONObject();
		try {
			jsonobj.put(WebConfig.WebParams.USER_ID, Helper.getStringValuefromPrefs(
					BeatCreation.this, SharedPreferencesKey.PREF_USERID));
			jsonobj.put("userBeatCollection", new JSONArray(
					adapterDealersBeatList.getselecteddata()));
			jsonobj.put("CoverageType", "Planned");

			String mktOff = "";
			if (weekOffDates != null) {
				if (weekOffDates.size() > 0) {
					mktOff = weekOffDates.toString().replace("[", "")
							.replace("]", "").replace(", ", ",");

				} else {
					mktOff = "";

				}
			} else {
				mktOff = "";
			}
			jsonobj.put("MarketOffDays", mktOff);

		} catch (JSONException e) {
			Helper.printStackTrace(e);
		}
		pdn.setConfig(getString(R.string.url), "InsertUserBeatDetailsInfo");
		pdn.execute(jsonobj);
		
		
	/*	VolleyPostDataToNetwork pdn=new VolleyPostDataToNetwork(BeatCreation.this, getString(R.string.loadingmessage), new VolleyGetDataCallBack() {
			
			@Override
			public void processResponse(Object result) {
				if (result != null) {
					ResponseDto obj = new FetchingdataParser(
							BeatCreation.this).getResponseResult(result
							.toString());
					if (obj.isSuccess()) {

						Helper.showAlertDialog(
								BeatCreation.this,
								SmartDostAlertDialog.SUCCESS_TYPE,
								getString(R.string.sucess),
								obj.getMessage(),
								getString(R.string.ok),
								new SmartDostAlertDialog.OnSDAlertDialogClickListener() {

									@Override
									public void onClick(
											SmartDostAlertDialog sdAlertDialog) {
										sdAlertDialog.dismiss();
										adapterDealersBeatList
												.clearMap();
										summaryDialog.dismiss();
										finish();

									}
								}, false, null, null);

					} else {

						Helper.showAlertDialog(
								BeatCreation.this,
								SmartDostAlertDialog.ERROR_TYPE,
								getString(R.string.error3),
								obj.getMessage(),
								getString(R.string.ok),
								new SmartDostAlertDialog.OnSDAlertDialogClickListener() {

									@Override
									public void onClick(
											SmartDostAlertDialog sdAlertDialog) {
										sdAlertDialog.dismiss();
									}
								}, false, null, null);

					}
				}

				else {

					Helper.showCustomToast(BeatCreation.this,
							R.string.networkserverbusy,
							Toast.LENGTH_LONG);

				}
				
			}
			
			@Override
			public void onError(VolleyError error) {
			
				
			}
		});
		
		pdn.setConfig(getString(R.string.url), WebMethod.INSERT_USER_BEAT_DETAIL_INFO);
		pdn.setRequestData(jsonobj);
		pdn.callWebService();
		*/
	}

	private void checkCoverageWindow() {
		PostDataToNetwork pdn = new PostDataToNetwork(BeatCreation.this,
				getString(R.string.loadingmessage), new GetDataCallBack() {
					@Override
					public void processResponse(Object result) {
						if (result != null) {

							Helper.printLog("success", "" + result.toString());

							BeatCoverageDto obj = new FetchingdataParser(
									BeatCreation.this).getCoverageWindow(result
									.toString());

							if (obj.isSuccess()) {
								BeatCoverageDto.SingleResult result2 = obj
										.getSingleResult();

								displayDate = result2.getCoverageDate();
								isexceptionflag = result2.isExceptionFlag();
								if (Helper.isNullOrEmpty(displayDate)) {
									Calendar cal = Calendar.getInstance();
									year = cal.get(Calendar.YEAR);
									month = (cal.get(Calendar.MONTH) + 1);

								} else {
									@SuppressWarnings("deprecation")
									Date date = new Date(displayDate);
									Calendar cal = Calendar.getInstance();
									cal.setTime(date);
									year = cal.get(Calendar.YEAR);
									month = (cal.get(Calendar.MONTH) + 1);
								}

							} else {

								Helper.showAlertDialog(
										BeatCreation.this,
										SSCAlertDialog.ERROR_TYPE,
										getString(R.string.error3),
										obj.getMessage(),
										getString(R.string.ok),
										new SSCAlertDialog.OnSDAlertDialogClickListener() {

											@Override
											public void onClick(
													SSCAlertDialog sdAlertDialog) {
												sdAlertDialog.dismiss();
												finish();
											}
										}, false, null, null);

							}
						} else {

							Helper.showCustomToast(BeatCreation.this,
									R.string.networkserverbusy,
									Toast.LENGTH_LONG);

						}
					}
				});
		JSONObject jsonobj = new JSONObject();
		try {
			jsonobj.put(WebConfig.WebParams.USER_ID, Helper.getStringValuefromPrefs(
					BeatCreation.this, SharedPreferencesKey.PREF_USERID));
			
			jsonobj.put(WebConfig.WebParams.ROLE_ID_CAPS, Long.valueOf(Helper
					.getIntValueFromPrefs(BeatCreation.this,
							SharedPreferencesKey.PREF_ROLEID)));
			

		} catch (JSONException e) {
			Helper.printStackTrace(e);
		}
		pdn.setConfig(getString(R.string.url), "IsCoverageFirstWindow");
		pdn.execute(jsonobj);

	}

	@Override
	public void onBackPressed() {
		Helper.showAlertDialog(BeatCreation.this,
				SSCAlertDialog.WARNING_TYPE, getString(R.string.error10),
				getString(R.string.save_msg), getString(R.string.ok),
				new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {
						sdAlertDialog.dismiss();
						if (adapterDealersBeatList != null) {
							adapterDealersBeatList.clearMap();
						}
						finish();

					}
				}, true, getString(R.string.cancel),
				new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {
						sdAlertDialog.dismiss();
					}
				});

	}

	private void summaryDialog(HashMap<String, ArrayList<String>> map,
			int length, int totalAssignOutlet) {

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			summaryDialog = new Dialog(BeatCreation.this,
					AlertDialog.THEME_HOLO_LIGHT);
		} else {
			summaryDialog = new Dialog(BeatCreation.this);
		}

		summaryDialog = new Dialog(BeatCreation.this);

		summaryDialog.setCanceledOnTouchOutside(true);
		summaryDialog.setContentView(R.layout.beat_summary);
		summaryDialog.setTitle(getString(R.string.beatsummary));
		ListView beatListView = (ListView) summaryDialog
				.findViewById(R.id.beatlist);

		ArrayList<UserBeatDetailDto> beatList = new ArrayList<UserBeatDetailDto>();

		for (Entry<String, ArrayList<String>> entry : map.entrySet()) {
			UserBeatDetailDto userbeat = new UserBeatDetailDto();
			userbeat.setDateRange(entry.getKey());
			ArrayList<String> listdata = entry.getValue();
			if (listdata.size() > 0) {
				userbeat.setStoreName(entry.getValue().toString()
						.replace("[", "").replace("]", "").replace(", ", ","));
				beatList.add(userbeat);
			}
		}
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout listFooterView = (LinearLayout) inflater.inflate(
				R.layout.beatsummaryfooter, null);

		TextView beatleave = (TextView) listFooterView
				.findViewById(R.id.beatleave);
		TextView beattotalsummaary = (TextView) listFooterView
				.findViewById(R.id.beattotalsummaary);
		beatListView.addFooterView(listFooterView);
		ArrayList<String> weekOffDatesnew = new ArrayList<String>();
		SimpleDateFormat oldformat = new SimpleDateFormat("dd MM yyyy");
		SimpleDateFormat newformat = new SimpleDateFormat(" dd MMM EEE");
		DecimalFormat mFormat = new DecimalFormat("00");
		if (weekOffDates != null) {
			if (weekOffDates.size() > 0) {
				for (int i = 0; i < weekOffDates.size(); i++) {
					try {
						String dates = weekOffDates.get(i) + " "
								+ mFormat.format(month) + " " + year;
						weekOffDatesnew.add(newformat.format(oldformat
								.parse(dates)));
					} catch (ParseException e) {
						Helper.printStackTrace(e);
					}
				}
				Collections.sort(weekOffDatesnew);
			}
		}
		Collections.sort(beatList, new BeatComparator());
		beatleave.setText("Off Days: "
				+ weekOffDatesnew.toString().replace("[", "").replace("]", "")
						.replace(", ", ","));
		if (weekOffDates != null) {
			beattotalsummaary.setText("Total Assigned Outlet: "
					+ totalAssignOutlet + "\n\n" + "Total Working Days: "
					+ map.size() + "\n\n" + "Total Outlet Planned: " + length
					+ "\n\n" + "Total Offs: " + weekOffDates.size());

		} else {
			beattotalsummaary.setText("Total Assigned Outlet: "
					+ totalAssignOutlet + "\n\n" + "Total Working Days: "
					+ map.size() + "\n\n" + "Total Outlet Planned: " + length
					+ "\n\n" + "Total Offs: " + 0);

		}
		BeatDetailsAdapter adapter = new BeatDetailsAdapter(BeatCreation.this,
				beatList, true);
		beatListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		submitbeat = (Button) summaryDialog.findViewById(R.id.submitbeat);
		submitbeat.setOnClickListener(BeatCreation.this);
		editbeat = (Button) summaryDialog.findViewById(R.id.editbeat);
		editbeat.setOnClickListener(BeatCreation.this);
		if (beatList.size() > 0) {
			summaryDialog.show();
		} else {

			Helper.showAlertDialog(BeatCreation.this,
					SSCAlertDialog.ERROR_TYPE,
					getString(R.string.error3), getString(R.string.error8),
					getString(R.string.ok),
					new SSCAlertDialog.OnSDAlertDialogClickListener() {

						@Override
						public void onClick(SSCAlertDialog sdAlertDialog) {
							sdAlertDialog.dismiss();
						}
					}, false, null, null);

		}

	}

	public HashMap<String, ArrayList<String>> getStoredetails(
			HashMap<String, ArrayList<String>> mapdata) {
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		SimpleDateFormat oldformat = new SimpleDateFormat("dd MM yyyy");
		SimpleDateFormat newformat = new SimpleDateFormat(" dd MMM EEE");
		DecimalFormat mFormat = new DecimalFormat("00");

		for (Entry<String, ArrayList<String>> entry : mapdata.entrySet()) {
			String date = entry.getKey() + " " + mFormat.format(month) + " "
					+ year;
			String dates = null;
			try {
				dates = newformat.format(oldformat.parse(date));
			} catch (ParseException e) {
				Helper.printStackTrace(e);
			}
			ArrayList<String> storename = entry.getValue();
			ArrayList<String> storedetails = new ArrayList<String>();
			for (String storeid : storename) {
				for (BeatCreationDto data : beatsListData) {
					if (storeid.equalsIgnoreCase(data.getStoreId())) {
						storedetails.add(data.getStoreName() + "("
								+ data.getChannelType() + ")" + "-"
								+ data.getStoreCode() + "-" + data.getCity());
					}
				}

			}

			map.put(dates, storedetails);

		}

		return map;
	}
}
