package com.samsung.ssc.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.samsung.ssc.R;
import com.samsung.ssc.constants.FileDirectory;
import com.samsung.ssc.constants.QuestionType;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.dto.Option;
import com.samsung.ssc.dto.Question;

public class ViewComponentsUtil {

	private Context context;
	private long mActivityID;
	private long mStoreID;

	public ViewComponentsUtil(Context context, long mStoreID, long activityID) {
		this.context = context;
		this.mActivityID = activityID;
		this.mStoreID = mStoreID;

	}

	/**
	 * Method to create a template of question with LableView.
	 * 
	 * @param question
	 * @return View
	 */
	public View LableView(Question question) {

		// container layout
		LinearLayout bodylinearLayout = new LinearLayout(context);
		bodylinearLayout.setTag(question);
		bodylinearLayout.setOrientation(LinearLayout.VERTICAL);
		bodylinearLayout.setPadding(15, 20, 15, 10);

		// inner view components
		TextView questionTextView = new TextView(context);
		questionTextView.setTextColor(context.getResources().getColor(
				R.color.text_color));

		// set question to textview
		questionTextView.setText(question.getQuestion());

		// adding all components to bodylinearLayout
		bodylinearLayout.addView(questionTextView, 0);

		return bodylinearLayout;
	}

	/**
	 * Method to create a template of question with {@link RatingBar}.
	 * 
	 * @param question
	 * @return View
	 */
	public View RatingView(Question question) {

		String userResponce = question.getUserResponse();
		if (userResponce.equals("")) {
			userResponce = "0";
		}

		LinearLayout bodylinearLayout = new LinearLayout(context);

		bodylinearLayout
				.setBackgroundResource(question.isMandatory() ? R.drawable.questionire_item_background_mandatory
						: R.drawable.questionire_item_background);

		bodylinearLayout.setTag(question);
		bodylinearLayout.setOrientation(LinearLayout.VERTICAL);
		if (question.getDependentOptionID() == 0) {
			bodylinearLayout.setPadding(20, 10, 5, 10);
		} else {
			bodylinearLayout.setPadding(20, 10, 0, 10);
		}

		LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		// inner view components
		TextView questionTextView = new TextView(context);
		questionTextView.setTextColor(context.getResources().getColor(
				R.color.text_color));

		RatingBar ratingBar = new RatingBar(context);
		ratingBar.setNumStars(5);
		ratingBar.setRating(Float.parseFloat(userResponce));
		if (!userResponce.equals("0")) {
			bodylinearLayout.setTag(R.id.tagUserResponseExist,
					Boolean.valueOf(true));
		}
		ratingBar.setLayoutParams(layoutparams);
		// set question to textview
		questionTextView.setText(question.getQuestion());

		// adding all components to bodylinearLayout
		bodylinearLayout.addView(questionTextView, 0);
		bodylinearLayout.addView(ratingBar, 1);
		return bodylinearLayout;
	}

	/**
	 * Method to create a template of question with {@link DatePicker}
	 * 
	 * @param question
	 * 
	 * @return View
	 */
	public View CanlendarView(Question question) {

		String userResponce = question.getUserResponse();
		if (userResponce.equals("")) {
			userResponce = "DD/MM/YYYY";
		}

		// container layout
		LinearLayout bodylinearLayout = new LinearLayout(context);
		bodylinearLayout
				.setBackgroundResource(question.isMandatory() ? R.drawable.questionire_item_background_mandatory
						: R.drawable.questionire_item_background);

		bodylinearLayout.setTag(question);
		bodylinearLayout.setOrientation(LinearLayout.VERTICAL);
		if (question.getDependentOptionID() == 0) {
			bodylinearLayout.setPadding(20, 10, 5, 10);
		} else {
			bodylinearLayout.setPadding(20, 10, 0, 10);
		}

		// inner view components
		TextView questionTextView = new TextView(context);
		questionTextView.setTextColor(context.getResources().getColor(
				R.color.text_color));

		final EditText editText = new EditText(context);
		editText.setKeyListener(null);
		editText.setFocusable(false);
		editText.setFocusableInTouchMode(false);
		editText.setText(userResponce);
		if (!userResponce.equals("DD/MM/YYYY")) {
			bodylinearLayout.setTag(R.id.tagUserResponseExist,
					Boolean.valueOf(true));
		}
		editText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();
				int year = calendar.get(Calendar.YEAR);
				int monthOfYear = calendar.get(Calendar.MONTH);
				int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
				// TODO Auto-generated method stub
				Dialog dialog = new DatePickerDialog(v.getContext(),
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {

								editText.setText(dayOfMonth + "/"
										+ (monthOfYear + 1) + "/" + year);

							}
						}, year, monthOfYear, dayOfMonth);
				dialog.show();
			}
		});

		questionTextView.setText(question.getQuestion());

		// adding all components to bodylinearLayout
		bodylinearLayout.addView(questionTextView, 0);
		bodylinearLayout.addView(editText, 1);

		return bodylinearLayout;
	}

	public View RepeatorView(final Question question) {

		String userResponce = question.getUserResponse();

		LinearLayout bodylinearLayout = new LinearLayout(context);
		bodylinearLayout
				.setBackgroundResource(question.isMandatory() ? R.drawable.questionire_item_background_mandatory
						: R.drawable.questionire_item_background);

		bodylinearLayout.setTag(question);
		bodylinearLayout.setOrientation(LinearLayout.VERTICAL);
		if (question.getDependentOptionID() == 0) {
			bodylinearLayout.setPadding(20, 10, 5, 10);
		} else {
			bodylinearLayout.setPadding(20, 10, 0, 10);
		}

		TextView questionTextView = new TextView(context);
		questionTextView.setTextColor(context.getResources().getColor(
				R.color.text_color));
		questionTextView.setText(question.getQuestion());
		final LinearLayout repeateLayoutContainer = new LinearLayout(context);
		repeateLayoutContainer.setOrientation(LinearLayout.VERTICAL);

		final Spinner spinner = new Spinner(context);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				int repeactCount = (Integer) parent.getItemAtPosition(position);
				spinner.setTag((Integer) parent.getItemAtPosition(position));
				drawRepeateQuestion(repeateLayoutContainer, repeactCount,
						question);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				spinner.setTag(0);
			}
		});
		int repeateCount = question.getRepeatMaxTimes();
		Integer[] values = new Integer[repeateCount + 1];
		for (int i = 0; i <= repeateCount; i++) {
			values[i] = i;
		}

		spinner.setAdapter(new ArrayAdapter<Integer>(context,
				android.R.layout.simple_dropdown_item_1line, values));

		if (!userResponce.equals("")) {
			int selectedIndex = Integer.parseInt(userResponce);

			spinner.setSelection(selectedIndex);
			bodylinearLayout.setTag(R.id.tagUserResponseExist,
					Boolean.valueOf(true));
		} else {
			spinner.setSelection(0);
		}

		// adding all components to bodylinearLayout
		bodylinearLayout.addView(questionTextView, 0);
		bodylinearLayout.addView(spinner, 1,
				new LayoutParams(
						android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
						Helper.dpToPixel(context, 50)));
		bodylinearLayout.addView(repeateLayoutContainer, 2);
		return bodylinearLayout;
	}

	protected void drawRepeateQuestion(LinearLayout repeateLayoutContainer,
			int repeactCount, Question rootQuestion) {

		repeateLayoutContainer.removeAllViews();

		DatabaseHelper db = DatabaseHelper.getConnection(context);
		SQLiteDatabase openedDatabase = db.getReadableDatabase();

		for (int i = 0; i < repeactCount; i++) {

			Question question = new Question();
			question.setQuestionTypeId(rootQuestion.getRepeaterTypeID());
			question.setQuestion(rootQuestion.getRepeaterText() + " " + (i + 1));
			question.setMandatory(rootQuestion.isMandatory());
			question.setTextLength(rootQuestion.getTextLength());
			question.setSubQuestionID(String.valueOf(rootQuestion
					.getSurveyQuestionId()) + "_" + i);
			question.setSurveyQuestionId(-1);
			String subSubQuestionID = question.getSubQuestionID();
			if (!subSubQuestionID.equals("")) {

				String userResponse = db.getSurveyQuestionResponseData(
						openedDatabase, question.getSubQuestionID(),
						mActivityID);
				question.setUserResponse(userResponse);
				if (!userResponse.equals("")) {
					question.setUserResponse(userResponse);
				} else {
					if (question.getQuestionTypeId() == QuestionType.SURVEY_QUESTION_TYPE_PICTUREBOX) {

						/*
						 * String imagePath = Environment
						 * .getExternalStorageDirectory() + "/STORE_IMAGES/" +
						 * question.getSubQuestionID()+"_"+mStoreID+".png";
						 */
						String imagePath = FileDirectory.DIRECTORY_SURVEY_QUESTION_IMAGES
								+ "/"
								+ String.valueOf(mStoreID)
								+ "/"
								+ question.getSubQuestionID()
								+ "_"
								+ mStoreID
								+ ".png";

						File file = new File(imagePath);
						if (file.exists()) {
							question.setUserResponse(imagePath);
						}
					}
				}
			} else {
				question.setUserResponse("");
			}

			View view = drawdynamicComponentsRepeaterType(
					rootQuestion.getSurveyQuestionId(), question);
			if (view != null) {
				repeateLayoutContainer.addView(view);
			}

		}

		openedDatabase.close();

	}

	/**
	 * Method to create a template of question with TExtView and EditText.
	 * 
	 * @param question
	 * 
	 * @return View
	 */
	public View ToggalView(Question question) {

		String userResponce = question.getUserResponse();

		if (userResponce.equals("")) {
			userResponce = "OFF";
		}

		// container layout
		LinearLayout bodylinearLayout = new LinearLayout(context);
		bodylinearLayout
				.setBackgroundResource(question.isMandatory() ? R.drawable.questionire_item_background_mandatory
						: R.drawable.questionire_item_background);

		bodylinearLayout.setTag(question);
		bodylinearLayout.setOrientation(LinearLayout.VERTICAL);
		if (question.getDependentOptionID() == 0) {
			bodylinearLayout.setPadding(20, 10, 5, 10);
		} else {
			bodylinearLayout.setPadding(20, 10, 0, 10);
		}

		// inner view components
		TextView questionTextView = new TextView(context);
		questionTextView.setTextColor(context.getResources().getColor(
				R.color.text_color));

		ToggleButton toggleButton = new ToggleButton(context);

		toggleButton.setChecked(userResponce.equalsIgnoreCase("ON") ? true
				: false);
		if (!userResponce.equals("")) {
			bodylinearLayout.setTag(R.id.tagUserResponseExist,
					Boolean.valueOf(true));
		}

		// set question to textview
		questionTextView.setText(question.getQuestion());

		// adding all components to bodylinearLayout
		bodylinearLayout.addView(questionTextView, 0);
		bodylinearLayout.addView(toggleButton, 1);

		return bodylinearLayout;
	}

	/**
	 * Method to create a template of question with TExtView and EditText.
	 * 
	 * @param question
	 * @return View
	 */
	public View DropdownView(Question question) {

		String userResponce = question.getUserResponse();

		// container layout
		LinearLayout bodylinearLayout = new LinearLayout(context);
		bodylinearLayout
				.setBackgroundResource(question.isMandatory() ? R.drawable.questionire_item_background_mandatory
						: R.drawable.questionire_item_background);

		bodylinearLayout.setTag(question);
		bodylinearLayout.setOrientation(LinearLayout.VERTICAL);
		if (question.getDependentOptionID() == 0) {
			bodylinearLayout.setPadding(20, 10, 5, 10);
		} else {
			bodylinearLayout.setPadding(20, 10, 0, 10);
		}

		// inner view components
		TextView questionTextView = new TextView(context);
		questionTextView.setTextColor(context.getResources().getColor(
				R.color.text_color));
		List<Option> options = question.getOptions();
		List<String> values = new ArrayList<String>();
		for (Option option : options) {
			values.add(option.getOptionValue());
		}

		int selectedIndex = values.indexOf(userResponce);
		if (!userResponce.equals("")) {
			bodylinearLayout.setTag(R.id.tagUserResponseExist,
					Boolean.valueOf(true));
		}
		final Spinner spinner = new Spinner(context);

		spinner.setAdapter(new ArrayAdapter<String>(context,
				android.R.layout.simple_dropdown_item_1line, values));
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				spinner.setTag((String) arg0.getItemAtPosition(arg2));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				spinner.setTag((String) arg0.getItemAtPosition(0));
			}
		});

		spinner.setSelection(selectedIndex == -1 ? 0 : selectedIndex);
		// set question to textview
		questionTextView.setText(question.getQuestion());

		// adding all components to bodylinearLayout
		bodylinearLayout.addView(questionTextView, 0);
		bodylinearLayout.addView(spinner, 1,
				new LayoutParams(
						android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
						Helper.dpToPixel(context, 50)));
		return bodylinearLayout;
	}

	/**
	 * Method to create a template of question with TExtView and EditText.
	 * 
	 * @param question
	 * @return View
	 */
	public View TextboxView(Question question) {
		String userResponce = question.getUserResponse();

		// container layout
		LinearLayout bodylinearLayout = new LinearLayout(context);
		bodylinearLayout
				.setBackgroundResource(question.isMandatory() ? R.drawable.questionire_item_background_mandatory
						: R.drawable.questionire_item_background);

		bodylinearLayout.setTag(question);
		bodylinearLayout.setOrientation(LinearLayout.VERTICAL);
		if (question.getDependentOptionID() == 0) {
			bodylinearLayout.setPadding(20, 10, 5, 10);
		} else {
			bodylinearLayout.setPadding(20, 10, 0, 10);
		}

		// inner view components
		TextView questionTextView = new TextView(context);
		questionTextView.setTextColor(context.getResources().getColor(
				R.color.text_color));
		EditText answerEditText = new EditText(context);
		answerEditText.setBackgroundResource(R.drawable.img_edittext);

		// set question to textview
		questionTextView.setText(question.getQuestion());
		answerEditText.setText(userResponce);
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(
				question.getTextLength() == 0 ? 20 : question.getTextLength());
		answerEditText.setFilters(FilterArray);
		// adding all components to bodylinearLayout
		bodylinearLayout.addView(questionTextView, 0);
		bodylinearLayout.addView(answerEditText, 1);

		if (!userResponce.equals("")) {
			bodylinearLayout.setTag(R.id.tagUserResponseExist,
					Boolean.valueOf(true));
		}
		return bodylinearLayout;
	}

	/**
	 * Method to create a template of question with TExtView and EditText.
	 * 
	 * @param question
	 * @return View
	 */
	public View TextboxViewNumaric(Question question) {

		String userResponce = question.getUserResponse();

		// container layout
		LinearLayout bodylinearLayout = new LinearLayout(context);
		bodylinearLayout
				.setBackgroundResource(question.isMandatory() ? R.drawable.questionire_item_background_mandatory
						: R.drawable.questionire_item_background);

		bodylinearLayout.setTag(question);
		bodylinearLayout.setOrientation(LinearLayout.VERTICAL);
		if (question.getDependentOptionID() == 0) {
			bodylinearLayout.setPadding(20, 10, 5, 10);
		} else {
			bodylinearLayout.setPadding(20, 10, 0, 10);
		}

		TextView questionTextView = new TextView(context);
		questionTextView.setTextColor(context.getResources().getColor(
				R.color.text_color));
		EditText answerEditText = new EditText(context);
		answerEditText.setBackgroundResource(R.drawable.img_edittext);
		try {
			answerEditText.setText(userResponce);
			if (!userResponce.equals("")) {
				bodylinearLayout.setTag(R.id.tagUserResponseExist,
						Boolean.valueOf(true));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		answerEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
		questionTextView.setText(question.getQuestion());

		bodylinearLayout.addView(questionTextView, 0);
		bodylinearLayout.addView(answerEditText, 1);

		return bodylinearLayout;
	}

	/**
	 * Method to create a template of question with TExtView and EditText.
	 * 
	 * @param question
	 * @return View
	 */
	public View PictureBoxView(final Question question,
			final int orginalRootQuestionID) {
		String userResponce = question.getUserResponse();

		// container layout
		LinearLayout bodylinearLayout = new LinearLayout(context);
		bodylinearLayout
				.setBackgroundResource(question.isMandatory() ? R.drawable.questionire_item_background_mandatory
						: R.drawable.questionire_item_background);
		try {
			bodylinearLayout.setOrientation(LinearLayout.VERTICAL);

			if (question.getDependentOptionID() == 0) {
				bodylinearLayout.setPadding(20, 10, 5, 10);
			} else {
				bodylinearLayout.setPadding(20, 10, 0, 10);
			}
			bodylinearLayout.setTag(question);

			TextView questionTextView = new TextView(context);
			questionTextView.setTextColor(context.getResources().getColor(
					R.color.text_color));
			ImageView clickImageView = new ImageView(context);

			clickImageView.setTag(null);

			try {

				if (!TextUtils.isEmpty(userResponce)) {

					File imgFile = new File(userResponce);
					Bitmap bitmap = null;
					if (imgFile.exists()) {

						bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory
								.decodeFile(imgFile.getAbsolutePath()), 120,
								120);
					}

					if (bitmap == null) {
						clickImageView.setTag(null);
						clickImageView.setImageResource(R.drawable.camera);
					} else {
						clickImageView.setImageBitmap(bitmap);
						clickImageView.setTag(userResponce);
					}
					bodylinearLayout.setTag(R.id.tagUserResponseExist,
							Boolean.valueOf(true));
				} else {
					clickImageView.setTag(null);
					clickImageView.setImageResource(R.drawable.camera);
				}

			} catch (Exception e) {
				e.printStackTrace();
				clickImageView.setTag(null);
				clickImageView.setImageResource(R.drawable.camera);
			}

			clickImageView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {

					int surveyQuestionID = question.getSurveyQuestionId();
					String fileName;
					if (surveyQuestionID == -1) {
						fileName = question.getSubQuestionID() + "_" + mStoreID
								+ ".png";
					} else {
						fileName = question.getSurveyQuestionId() + ".png";
					}

					/*
					 * File folder = new File(Environment
					 * .getExternalStorageDirectory() + "/STORE_IMAGES");
					 */
					File folder = new File(
							FileDirectory.DIRECTORY_SURVEY_QUESTION_IMAGES
									+ "/" + mStoreID);

					if (!folder.exists()) {
						folder.mkdirs();
					}

					File questionImageFile = new File(folder, fileName);
					if (!questionImageFile.exists()) {
						try {
							questionImageFile.createNewFile();
						} catch (Exception e) {
						}
					}

					Intent cameraActivity = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);

					Uri uri = Uri.fromFile(questionImageFile);
					cameraActivity.putExtra(
							android.provider.MediaStore.EXTRA_OUTPUT, uri);

					((Activity) context).startActivityForResult(cameraActivity,
							orginalRootQuestionID);

				}
			});

			questionTextView.setText(question.getQuestion());

			// adding all components to bodylinearLayout
			bodylinearLayout.addView(questionTextView, 0);
			bodylinearLayout.addView(clickImageView, 1);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bodylinearLayout;
	}

	/**
	 * Method to create a template of question with TExtView and EditText.
	 * 
	 * @param question
	 * @return View
	 */
	public View RadioButtonView(final Question question) {
		String userResponce = question.getUserResponse();

		// container layout
		final LinearLayout bodylinearLayout = new LinearLayout(context);
		bodylinearLayout
				.setBackgroundResource(question.isMandatory() ? R.drawable.questionire_item_background_mandatory
						: R.drawable.questionire_item_background);

		bodylinearLayout.setOrientation(LinearLayout.VERTICAL);

		final LinearLayout subBodylinearLayout = new LinearLayout(context);
		subBodylinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		subBodylinearLayout.setGravity(Gravity.CENTER_VERTICAL);
		bodylinearLayout.setTag(question);
		if (question.getDependentOptionID() == 0) {
			bodylinearLayout.setPadding(20, 10, 5, 10);
		} else {
			bodylinearLayout.setPadding(20, 10, 0, 10);
		}

		TextView questionTextView = new TextView(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
		params.setMargins(10, 0, 0, 0);
		questionTextView.setLayoutParams(params);
		questionTextView.setTextColor(context.getResources().getColor(
				R.color.text_color));
		final RadioGroup radioGroup = new RadioGroup(context);
		ArrayList<LinearLayout> dependentViewsList = new ArrayList<LinearLayout>();

		if (question.getOptions() != null && (question.getOptions().size() > 0)) {

			for (int i = 0; i < question.getOptions().size(); i++) {
				RadioButton radioButton = new RadioButton(context);
				radioButton.setTextColor(context.getResources().getColor(
						R.color.text_color));
				radioButton.setId(i);
				radioButton.setText(question.getOptions().get(i)
						.getOptionValue());
				radioButton.setTag(question.getOptions().get(i));
 				if (userResponce.equals(question.getOptions().get(i)
						.getOptionValue())) {
					radioGroup.setOnCheckedChangeListener(null);
					radioButton.setChecked(true);
					bodylinearLayout.setTag(R.id.tagUserResponseExist,
							Boolean.valueOf(true));
				}

				radioGroup.addView(radioButton);

				/***
				 * Create the Root View for all the dependent Questions of this
				 * option and Add the Views to the ArrayList so that these Views
				 * can be added after radio group.
				 */
				Option option = question.getOptions().get(i);
				ArrayList<Question> dependentQuestions = DatabaseHelper
						.getConnection(context).getQuestionsData2(
								question.getModuleId(),
								option.getSurveyOptionId(), mActivityID);
				if (dependentQuestions.size() > 0) {
					LinearLayout bodylinearLayoutChild = new LinearLayout(
							context);
					bodylinearLayoutChild.setOrientation(LinearLayout.VERTICAL);
					bodylinearLayoutChild.setTag(option.getSurveyOptionId());
					bodylinearLayoutChild.setPadding(0, 0, 0, 0);

					ArrayList<Integer> selectedUserResponseQuestions = new ArrayList<Integer>();
					for (Question ques : dependentQuestions) {
						LinearLayout childView = (LinearLayout) drawdynamicComponents(ques);
						if (childView != null) {
							bodylinearLayoutChild.addView(childView);
							Boolean obj = (Boolean) childView
									.getTag(R.id.tagUserResponseExist);
							if (obj != null && obj.booleanValue()) {
								selectedUserResponseQuestions.add(ques
										.getSurveyQuestionId());
							}
						}
					}
					bodylinearLayoutChild.setTag(R.id.tagVisbileQuestionList,
							selectedUserResponseQuestions);
					dependentViewsList.add(bodylinearLayoutChild);
				}

				radioGroup
						.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(RadioGroup group,
									int checkedId) {
								RadioButton buttonView = (RadioButton) group
										.findViewById(checkedId);

								Option selectedOption = (Option) buttonView
										.getTag();

								int childCount = group.getChildCount();

								for (int j = 0; j < childCount; j++) {
									RadioButton rBtn = (RadioButton) group
											.getChildAt(j);
									Option opt = (Option) rBtn.getTag();

									if (selectedOption.getSurveyOptionId() == opt
											.getSurveyOptionId()) {
										LinearLayout bodylinearLayoutChild = (LinearLayout) bodylinearLayout
												.findViewWithTag(selectedOption
														.getSurveyOptionId());
										if (bodylinearLayoutChild != null
												&& bodylinearLayoutChild
														.getVisibility() == View.GONE) {
											bodylinearLayoutChild
													.setVisibility(View.VISIBLE);
										}
									} else {
										LinearLayout bodylinearLayoutChild = (LinearLayout) bodylinearLayout
												.findViewWithTag(opt
														.getSurveyOptionId());
										if (bodylinearLayoutChild != null
												&& bodylinearLayoutChild
														.getVisibility() == View.VISIBLE) {
											bodylinearLayoutChild
													.setVisibility(View.GONE);
										}
									}
								}
							}
						});
			}
		}

		questionTextView.setText(question.getQuestion());

		String qImage = question.getSurveyQuestionImage();
		final ImageView imageView = new ImageView(context);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				Helper.dpToPixel(context, 50), Helper.dpToPixel(context, 50));
		imageView.setLayoutParams(layoutParams);

		if (qImage != null && !qImage.equalsIgnoreCase("null")) {

			ImageLoader.getInstance(context).displayImageRound(
					context.getResources().getString(
							R.string.url_file_processor_consolebase)
							+ "id=" + qImage + "&type=QuestionIcon", imageView);
		} else {
			imageView.setVisibility(View.GONE);
		}
		imageView.setOnClickListener(new View.OnClickListener() {
			private Point p;

			@Override
			public void onClick(View v) {
				int[] location = new int[2];
				imageView.getLocationOnScreen(location);
				p = new Point();
				p.x = location[0];
				p.y = location[1];
				showPopup(context, imageView, p);
			}
		});

		subBodylinearLayout.addView(imageView);
		subBodylinearLayout.addView(questionTextView);
		bodylinearLayout.addView(subBodylinearLayout, 0);
		bodylinearLayout.addView(radioGroup, 1);

		if (dependentViewsList.size() > 0) {
			for (LinearLayout llDependentChild : dependentViewsList) {
				bodylinearLayout.addView(llDependentChild);
				@SuppressWarnings("unchecked")
				ArrayList<Integer> selectedUserResponseQuestions = (ArrayList<Integer>) llDependentChild
						.getTag(R.id.tagVisbileQuestionList);
				if (selectedUserResponseQuestions != null
						&& selectedUserResponseQuestions.size() > 0) {
					for (int i = 0; i < llDependentChild.getChildCount(); i++) {
						LinearLayout childRootView = (LinearLayout) llDependentChild
								.getChildAt(i);
						if (selectedUserResponseQuestions
								.contains(((Question) childRootView.getTag())
										.getSurveyQuestionId())) {
							childRootView.setVisibility(View.VISIBLE);
						} else {
							childRootView.setVisibility(View.VISIBLE);
						}
					}
				} else {
					llDependentChild.setVisibility(View.GONE);
				}
			}
		}
		// End

		return bodylinearLayout;
	}

	/**
	 * Method to create a template of question with TExtView and EditText.
	 * 
	 * @param question
	 * @return View
	 */
	public View CheckBoxView(Question question) {

		List<String> userResponce = null;
		boolean isUserResponcePresent = false;
		String text = question.getUserResponse();
		if (!text.equals("")) {
			userResponce = Arrays.asList(text.split("@"));
			isUserResponcePresent = true;
		}

		LinearLayout bodylinearLayout = new LinearLayout(context);
		bodylinearLayout
				.setBackgroundResource(question.isMandatory() ? R.drawable.questionire_item_background_mandatory
						: R.drawable.questionire_item_background);

		bodylinearLayout.setOrientation(LinearLayout.VERTICAL);

		bodylinearLayout.setTag(question);
		if (question.getDependentOptionID() == 0) {
			bodylinearLayout.setPadding(20, 10, 5, 10);
		} else {
			bodylinearLayout.setPadding(20, 10, 0, 10);
		}

		TextView questionTextView = new TextView(context);
		questionTextView.setTextColor(context.getResources().getColor(
				R.color.text_color));
		LinearLayout checklinearLayout = new LinearLayout(context);
		checklinearLayout.setOrientation(LinearLayout.VERTICAL);
		if (question.getOptions() != null && (question.getOptions().size() > 0)) {
			for (int i = 0; i < question.getOptions().size(); i++) {
				CheckBox checkBox = new CheckBox(context);
				checkBox.setTextColor(context.getResources().getColor(
						R.color.text_color));
				checkBox.setId(i);
				checkBox.setText(question.getOptions().get(i).getOptionValue());
				checkBox.setTag(question.getOptions().get(i));

				try {
					if (isUserResponcePresent) {
						if (userResponce.contains(question.getOptions().get(i)
								.getOptionValue())) {
							checkBox.setChecked(true);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				checklinearLayout.addView(checkBox);
			}
		}

		// set question to textview
		questionTextView.setText(question.getQuestion());

		// adding all components to bodylinearLayout
		bodylinearLayout.addView(questionTextView, 0);
		bodylinearLayout.addView(checklinearLayout, 1);
		if (isUserResponcePresent) {
			bodylinearLayout.setTag(R.id.tagUserResponseExist,
					Boolean.valueOf(true));
		}

		return bodylinearLayout;
	}

	public byte[] getByteArray(Context context, Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
		byte[] byteArray = stream.toByteArray();
		try {
			stream.flush();
			stream.close();
		} catch (IOException e) {
			Helper.printStackTrace(e);
		}
		return byteArray;
	}

	public Bitmap getBitmap(Uri uri) {

		InputStream in = null;
		try {
			final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
			in = context.getContentResolver().openInputStream(uri);

			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, o);
			in.close();

			int scale = 1;
			while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
				scale++;
			}
			Bitmap b = null;
			in = context.getContentResolver().openInputStream(uri);
			if (scale > 1) {
				scale--;
				// scale to max possible inSampleSize that still yields an image
				// larger than target
				o = new BitmapFactory.Options();
				o.inSampleSize = scale;
				b = BitmapFactory.decodeStream(in, null, o);

				// resize to desired dimensions
				int height = b.getHeight();
				int width = b.getWidth();
				double y = Math.sqrt(IMAGE_MAX_SIZE
						/ (((double) width) / height));
				double x = (y / height) * width;

				Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
						(int) y, true);
				b.recycle();
				b = scaledBitmap;

				System.gc();
			} else {
				b = BitmapFactory.decodeStream(in);
			}
			in.close();

			return b;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Method to draw view components based on option type
	 * 
	 * @param questions
	 */
	public View drawdynamicComponents(Question question) {

		try {
			switch (question.getQuestionTypeId()) {
			case QuestionType.SURVEY_QUESTION_TYPE_CALENDAR:
				return CanlendarView(question);
			case QuestionType.SURVEY_QUESTION_TYPE_CHECKBOX:
				return CheckBoxView(question);
			case QuestionType.SURVEY_QUESTION_TYPE_DROPDOWN:
				return DropdownView(question);
			case QuestionType.SURVEY_QUESTION_TYPE_LABEL:
				return LableView(question);
			case QuestionType.SURVEY_QUESTION_TYPE_NUMERICTEXT:
				return TextboxViewNumaric(question);
			case QuestionType.SURVEY_QUESTION_TYPE_PICTUREBOX:
				return PictureBoxView(question, question.getSurveyQuestionId());
			case QuestionType.SURVEY_QUESTION_TYPE_RADIOBUTTON:
				return RadioButtonView(question);
			case QuestionType.SURVEY_QUESTION_TYPE_RATING:
				return RatingView(question);
			case QuestionType.SURVEY_QUESTION_TYPE_TEXTBOX:
				return TextboxView(question);
			case QuestionType.SURVEY_QUESTION_TYPE_TOGGLEBUTTON:
				return ToggalView(question);
			case QuestionType.SURVEY_QUESTION_TYPE_REPEATOR:
				return RepeatorView(question);

			}
		} catch (Exception e) {
			Helper.printStackTrace(e);
		}
		return null;
	}

	private View drawdynamicComponentsRepeaterType(int surveyQuestionID,
			Question question) {
		try {
			switch (question.getQuestionTypeId()) {
			case QuestionType.SURVEY_QUESTION_TYPE_CALENDAR:
				return CanlendarView(question);
			case QuestionType.SURVEY_QUESTION_TYPE_NUMERICTEXT:
				return TextboxViewNumaric(question);
			case QuestionType.SURVEY_QUESTION_TYPE_PICTUREBOX:
				return PictureBoxView(question, surveyQuestionID);
			case QuestionType.SURVEY_QUESTION_TYPE_RATING:
				return RatingView(question);
			case QuestionType.SURVEY_QUESTION_TYPE_TEXTBOX:
				return TextboxView(question);
			case QuestionType.SURVEY_QUESTION_TYPE_TOGGLEBUTTON:
				return ToggalView(question);
			default:
				break;
			}

		} catch (Exception e) {
		}

		return null;
	}

	@SuppressWarnings("deprecation")
	private void showPopup(final Context context, ImageView imageView, Point p) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.custom_fullimage_dialog,
				null);
		ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				300, 300);
		image.setLayoutParams(layoutParams);
		image.setScaleType(ScaleType.FIT_CENTER);
		image.setScaleType(ScaleType.FIT_XY);
		image.setImageBitmap((Bitmap) imageView.getTag());
		final PopupWindow popup = new PopupWindow(context);
		popup.setContentView(layout);
		popup.setWidth(LayoutParams.MATCH_PARENT);
		popup.setHeight(LayoutParams.MATCH_PARENT);
		popup.setFocusable(true);
		popup.setBackgroundDrawable(new BitmapDrawable());
		popup.showAtLocation(layout, Gravity.CENTER, 0, 0);
		popup.setTouchInterceptor(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				popup.dismiss();
				return false;
			}
		});
	}
}
