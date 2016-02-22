package com.samsung.ssc.adapters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.samsung.ssc.CalendarView;
import com.samsung.ssc.R;

public class CalendarViewAdapter extends BaseAdapter {

	private Date[] calendarGrid;
	private Context mContext;
	private LayoutInflater inflater;
	private Calendar mCal, currentDate;
	private Date filterDate;
	private Calendar c;
	private Calendar c2;
	private int currentYear;
	private boolean exceptionFlag;
	private int a;

	private static final int ONE = 1;
	private static final int START_DAY_WEEK = 2;

	@SuppressWarnings("deprecation")
	public CalendarViewAdapter(Context context, Date date, boolean exceptionFlag) {
		this.exceptionFlag = exceptionFlag;
		mContext = context;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mCal = Calendar.getInstance();
		this.filterDate = date;
		a = filterDate.getDate();
		mCal.setTimeInMillis(filterDate.getTime());
		int totalDaysInMonth = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		initCalendar(mCal, totalDaysInMonth);

		currentDate = Calendar.getInstance();
		currentDate.setTimeZone(TimeZone.getDefault());
		currentDate.setTime(date);

	}

	@SuppressWarnings("deprecation")
	public void initCalendar(Calendar cal, int totalDaysInMonth) {
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getDefault());
		c.setTime(cal.getTime());
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), ONE);

		int startOfWeek = c.get(Calendar.DAY_OF_WEEK) - START_DAY_WEEK;
		startOfWeek = startOfWeek == -1 ? 6 : startOfWeek;
		c.add(Calendar.DATE, -startOfWeek);

		int totalRows = 6;
		if ((totalDaysInMonth + startOfWeek) > 35) {
			calendarGrid = new Date[6 * 7];
			totalRows = 6;
		} else {
			calendarGrid = new Date[5 * 7];
			totalRows = 5;
		}

		int gridCount = 0;
		for (int week = 0; week < totalRows; week++) {
			for (int day = 0; day < 7; day++) {
				Date dt = c.getTime();
				dt.setHours(0);
				dt.setMinutes(0);
				dt.setSeconds(0);
				calendarGrid[gridCount++] = dt;
				c.add(Calendar.DATE, ONE);
			}
		}

	}

	public void nextMonth() {
		if (mCal.get(Calendar.MONTH) == 0) {
			mCal.set(mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH) + ONE,
					28);
		} else {
			mCal.set(mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH) + ONE,
					mCal.get(Calendar.DATE));
		}
		int totalDaysInMonth = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		initCalendar(mCal, totalDaysInMonth);

		notifyDataSetChanged();
	}

	public void prevYear() {
		if (mCal.get(Calendar.MONTH) == 0) {
			mCal.set(mCal.get(Calendar.YEAR) - ONE, mCal.get(Calendar.MONTH),
					28);
		} else {
			mCal.set(mCal.get(Calendar.YEAR) - ONE, mCal.get(Calendar.MONTH),
					mCal.get(Calendar.DATE));
		}
		int totalDaysInMonth = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		initCalendar(mCal, totalDaysInMonth);

		notifyDataSetChanged();
	}

	public void nextYear() {
		if (mCal.get(Calendar.MONTH) == 0) {
			mCal.set(mCal.get(Calendar.YEAR) + ONE, mCal.get(Calendar.MONTH),
					28);
		} else {
			mCal.set(mCal.get(Calendar.YEAR) + ONE, mCal.get(Calendar.MONTH),
					mCal.get(Calendar.DATE));
		}
		int totalDaysInMonth = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		initCalendar(mCal, totalDaysInMonth);

		notifyDataSetChanged();
	}

	public void prevMonth() {

		if (mCal.get(Calendar.MONTH) == 0) {
			mCal.set(mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH) - ONE,
					28);
		} else {
			mCal.set(mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH) - ONE,
					mCal.get(Calendar.DATE));
		}

		int totalDaysInMonth = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		initCalendar(mCal, totalDaysInMonth);

		notifyDataSetChanged();
	}

	public Calendar getCurrentCalendar() {
		return mCal;
	}

	//
	@Override
	public int getCount() {
		return calendarGrid.length;
	}

	//
	@Override
	public Date getItem(int position) {
		return calendarGrid[position];
	}

	//
	@Override
	public long getItemId(int position) {
		return position;
	}

	//
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();

		convertView = inflater.inflate(R.layout.calendar_view_item, null);
		holder.txtDate = (TextView) convertView.findViewById(R.id.txtCalDate);

		holder.imgviewBulletin = (ImageView) convertView
				.findViewById(R.id.imageView_bulletinicon);
		holder.item = (LinearLayout) convertView
				.findViewById(R.id.calendar_item);

		holder.imgviewBulletin.setVisibility(View.GONE);

		Date date = getItem(position);

		if (c == null) {
			c = Calendar.getInstance();
			c.setTimeZone(TimeZone.getDefault());
		}
		c.setTime(date);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), ONE); // To get First
																	// day of
																	// week
		int x1 = c.get(Calendar.MONTH);

		Date date2 = new Date(getCurrentCalendar().getTimeInMillis());
		if (c2 == null) {
			c2 = Calendar.getInstance();
			c2.setTimeZone(TimeZone.getDefault());
		}
		c2.setTime(date2);
		c2.set(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), ONE); // To get
																	// First day
																	// of week
		int x2 = c2.get(Calendar.MONTH);

		String curr_date = String.format("%2d", date.getDate()).trim();
		String curr_month = "" + date.getMonth();

		holder.txtDate.setText(curr_date);

		if (x1 == x2) {
			if (curr_date.equals("" + currentDate.get(Calendar.DATE))
					&& curr_month.equals("" + currentDate.get(Calendar.MONTH))) {

				if (exceptionFlag) {

					if (date.getDate() < a) {

						holder.item.setBackgroundColor(Color
								.parseColor("#a9a9a9"));

					} else {
						holder.item.setBackgroundColor(Color.LTGRAY);//
					}
				} else {
					holder.item.setBackgroundColor(Color.LTGRAY);

				}

			} else {
				if (exceptionFlag) {

					if (date.getDate() < a) {

						holder.item.setBackgroundColor(Color
								.parseColor("#a9a9a9"));

					} else {
						holder.item.setBackgroundColor(Color.LTGRAY);
					}
				} else {
					holder.item.setBackgroundColor(Color.LTGRAY);

				}
			}

			holder.txtDate.setTextColor(Color.BLACK);

		} else {
			holder.item.setVisibility(View.INVISIBLE);
			holder.txtDate.setTextColor(Color.LTGRAY);
		}

		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		format.setTimeZone(TimeZone.getDefault());

		boolean isRepeated = false;
		// String key = curr_date + "-" + curr_month + "-" + curr_year;
		String key = "D:" + curr_date + "M:" + curr_month;

		if (CalendarView.selectedDates.containsKey(key)) {

			isRepeated = true;
		} else {

			isRepeated = false;
		}

		if (isRepeated) {
			holder.imgviewBulletin.setVisibility(View.VISIBLE);

		} else {
			holder.imgviewBulletin.setVisibility(View.GONE);
		}

		return convertView;

	}

	/**
	 * @return the currentYear
	 */
	public int getCurrentYear() {
		return currentYear;
	}

	/**
	 * @param currentYear
	 *            the currentYear to set
	 */
	public void setCurrentYear(int currentYear) {
		this.currentYear = currentYear;
	}

	/**
	 * 
	 * @author vasingh
	 * 
	 */
	private class ViewHolder {
		private TextView txtDate;
		private LinearLayout item;
		private ImageView imgviewBulletin;
	}

}
