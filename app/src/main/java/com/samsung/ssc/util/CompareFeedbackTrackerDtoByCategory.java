package com.samsung.ssc.util;

import java.util.Comparator;

import com.samsung.ssc.dto.FeedbackTrackerDto1;

public class CompareFeedbackTrackerDtoByCategory implements Comparator<FeedbackTrackerDto1> {

	private boolean isAscending;
	public CompareFeedbackTrackerDtoByCategory(boolean isAscending) {
		this.isAscending = isAscending;
	}
	@Override
	public int compare(FeedbackTrackerDto1 lhs, FeedbackTrackerDto1 rhs) {
		
		if (isAscending) {
			return lhs.getFeedbackCategoryName().compareToIgnoreCase(rhs.getFeedbackCategoryName());
		} else {
			return rhs.getFeedbackCategoryName().compareToIgnoreCase(lhs.getFeedbackCategoryName());
		}
		
		
	}

}
