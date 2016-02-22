package com.samsung.ssc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.samsung.ssc.dto.FeedbackTrackerDto1;

public class CompareFeedbackTrackerDtoByUpdatedOnDate implements
		Comparator<FeedbackTrackerDto1> {

	private boolean isAscending;

	public CompareFeedbackTrackerDtoByUpdatedOnDate(boolean isAscending) {
		this.isAscending = isAscending;
	}

	@Override
	public int compare(FeedbackTrackerDto1 lhs, FeedbackTrackerDto1 rhs) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"dd/MM/yyyy hh:mm:ss");

		Date lhs_date = null;
		Date rhs_date = null;
		try {
			lhs_date = simpleDateFormat.parse(lhs.getLastUpdatedOn());
			rhs_date = simpleDateFormat.parse(rhs.getLastUpdatedOn());
		} catch (ParseException e) {

			e.printStackTrace();
		}

		if (lhs_date != null && rhs_date != null) {
			long lhs_time = lhs_date.getTime();
			long rhs_time = rhs_date.getTime();

			if (isAscending) {

				if (lhs_time < rhs_time) {
					return 1;
				} else if (lhs_time > rhs_time) {
					return -1;
				} else {
					return 0;
				}

			} else {

				if (lhs_time < rhs_time) {
					return -1;
				} else if (lhs_time > rhs_time) {
					return 1;
				} else {
					return 0;
				}

			}
		}

		return 0;

	}

}
