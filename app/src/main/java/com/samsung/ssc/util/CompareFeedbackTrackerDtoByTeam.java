package com.samsung.ssc.util;

import java.util.Comparator;

import com.samsung.ssc.dto.FeedbackTrackerDto1;

public class CompareFeedbackTrackerDtoByTeam implements
		Comparator<FeedbackTrackerDto1> {
	
	private boolean isAscending;

	public CompareFeedbackTrackerDtoByTeam(boolean isAscending) {
		this.isAscending = isAscending;
	}

	@Override
	public int compare(FeedbackTrackerDto1 lhs, FeedbackTrackerDto1 rhs) {
		
		if (isAscending) {
			return lhs.getTeamName().compareToIgnoreCase(rhs.getTeamName());
		} else {
			return rhs.getTeamName().compareToIgnoreCase(lhs.getTeamName());
		}

		
	}

}
