package com.samsung.ssc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.samsung.ssc.adapters.CalendarViewAdapter;
import com.samsung.ssc.util.Helper;

public class CalendarView extends BaseActivity implements OnClickListener {
	private GridView calendar;
	private CalendarViewAdapter adapter;
	private TextView txtDate;
	public static String selectedDate;
	public static HashMap<String, String> selectedDates= new HashMap<String, String>();
	public ArrayList<String> workingDates;
	public ArrayList<String> weekOffDates;

	int position;
	private SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");

	private Button closeBtn, saveBtn, cancelBtn;
	private String filterDate;
	private int totalDaysInMonth;
	private int a;
	private boolean exceptionFlag = false;

	/** Called when the activity is first created. */
	@SuppressWarnings("deprecation")
	@Override
	public void init() {
		super.init();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.my_calendar);

		filterDate = getIntent().getStringExtra("server_date");
		exceptionFlag = getIntent().getBooleanExtra("is_exception", false);

		
		workingDates = new ArrayList<String>();
		weekOffDates = new ArrayList<String>();

		findViews();
		Date datenew1 = new Date(filterDate);
		a = datenew1.getDate();

		Date datenew = new Date(filterDate);
		adapter = new CalendarViewAdapter(this, datenew, exceptionFlag);
		calendar.setAdapter(adapter);

		Calendar cal = Calendar.getInstance();
		cal.setTime(datenew);
		totalDaysInMonth = cal.getActualMaximum(Calendar.DATE);

		SimpleDateFormat dateTitleformat = new SimpleDateFormat("MMM-yyyy");
		txtDate.setText(dateTitleformat.format(datenew));

	}

	public void findViews() {

		calendar = (GridView) findViewById(R.id.gridCalendar);
		txtDate = (TextView) findViewById(R.id.txtDate);
		closeBtn = (Button) findViewById(R.id.closeBtn);
		saveBtn = (Button) findViewById(R.id.saveBtn);
		cancelBtn = (Button) findViewById(R.id.calenderCancelBtn);

		closeBtn.setOnClickListener(this);
		saveBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);

		calendar.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				String key = "D:" + adapter.getItem(position).getDate() + "M:"
						+ adapter.getItem(position).getMonth();
				if (exceptionFlag) {

					if (adapter.getItem(position).getDate() < a) {
						arg1.setClickable(false);

					} else {
						arg1.setClickable(true);
						if (selectedDates.containsKey(key)) {
							selectedDates.remove(key);
						} else {
							selectedDates.put(key,
									"" + adapter.getItem(position).getDate());
						}

					}
				} else {

					arg1.setClickable(true);
					if (selectedDates.containsKey(key)) {
						selectedDates.remove(key);
					} else {
						selectedDates.put(key, ""
								+ adapter.getItem(position).getDate());
					}
				}
				adapter.notifyDataSetChanged();

			}

		});

	}

	public void btnPrevClick(View v) {
		adapter.prevMonth();
		adapter.notifyDataSetChanged();
		txtDate.setText(format.format(adapter.getCurrentCalendar().getTime()));
	}

	public void btnNextClick(View v) {

		adapter.nextMonth();
		adapter.notifyDataSetChanged();

		txtDate.setText(format.format(adapter.getCurrentCalendar().getTime()));
	}

	public void btnPrevYearClick(View v) {
		adapter.prevYear();
		adapter.notifyDataSetChanged();
		txtDate.setText(format.format(adapter.getCurrentCalendar().getTime()));
	}

	public void btnNextYearClick(View v) {
		adapter.nextYear();
		adapter.notifyDataSetChanged();
		txtDate.setText(format.format(adapter.getCurrentCalendar().getTime()));
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();

		switch (id) {

		case R.id.saveBtn:

			if (exceptionFlag) {

				for (int i = a; i <= totalDaysInMonth; i++) {
					workingDates.add("" + i);
				}

			} else {

				for (int i = 1; i <= totalDaysInMonth; i++) {
					workingDates.add("" + i);
				}
			}

			if (selectedDates.size() > 0) {

				for (String key : selectedDates.keySet()) {
					Helper.printLog("Selected Dates:", "key:" + key + " "
							+ selectedDates.get(key));

					try {
						weekOffDates.add(selectedDates.get(key).toString());
					} catch (Exception e) {

					}

					workingDates.remove(selectedDates.get(key));

				}
			}

			Bundle data = new Bundle();
			data.putStringArrayList("workingDates", workingDates);
			data.putStringArrayList("weekOffDates", weekOffDates);

			Intent intent = new Intent(CalendarView.this, BeatManagement.class);
			intent.putExtra("working_dates", data);
			setResult(RESULT_OK, intent);
			selectedDates.clear();
			finish();

			// for(int i=0; i<workingDates.size(); i++){
			// Helper.printLog("Working Dates:", "" + workingDates.get(i));
			// }

			break;

		case R.id.calenderCancelBtn:
			if (exceptionFlag) {

				for (int i = a; i <= totalDaysInMonth; i++) {
					workingDates.add("" + i);
				}
			} else {

				for (int i = 1; i <= totalDaysInMonth; i++) {
					workingDates.add("" + i);
				}
			}

			Bundle data1 = new Bundle();
			data1.putStringArrayList("workingDates", workingDates);

			Intent intentt = new Intent(CalendarView.this, BeatManagement.class);
			intentt.putExtra("working_dates", data1);
			setResult(RESULT_OK, intentt);
			selectedDates.clear();
			finish();
			break;

		case R.id.closeBtn:
			finish();
			break;

		}
	}

	/**
	 * @param monthNumber
	 *            Month Number starts with 0. For <b>January</b> it is <b>0</b>
	 *            and for <b>December</b> it is <b>11</b>.
	 * @return
	 */
	public int getDaysInMonthInPresentYear(int monthNumber) {
		int days = 0;
		if (monthNumber >= 0 && monthNumber < 12) {
			try {
				Calendar calendar = Calendar.getInstance();
				int date = 1;
				int year = calendar.get(Calendar.YEAR);
				calendar.set(year, monthNumber, date);
				days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			} catch (Exception e) {
				if (e != null) {
					Helper.printStackTrace(e);
				}
			}
		}
		return days;
	}
}
