package com.samsung.ssc.util;

import java.util.Comparator;

import com.samsung.ssc.dto.FeedbackTrackerDto1;

public class CompareFeedbackTrackerDtoByStatus implements
		Comparator<FeedbackTrackerDto1> {

	private boolean isAscending;

	public CompareFeedbackTrackerDtoByStatus(boolean isAscending) {
		this.isAscending = isAscending;
	}

	@Override
	public int compare(FeedbackTrackerDto1 lhs, FeedbackTrackerDto1 rhs) {
		if (isAscending) {
			return ((Integer) lhs.getCurrentStatusID()).compareTo(rhs
					.getCurrentStatusID());
		} else {
			return ((Integer) rhs.getCurrentStatusID()).compareTo(lhs
					.getCurrentStatusID());
		}
	}

}
