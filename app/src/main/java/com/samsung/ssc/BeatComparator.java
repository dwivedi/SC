package com.samsung.ssc;

import java.util.Comparator;

import com.samsung.ssc.dto.UserBeatDetailDto;

public class BeatComparator implements Comparator<UserBeatDetailDto> {

	@Override
	public int compare(UserBeatDetailDto u1, UserBeatDetailDto u2) {
		
		return u1.getDateRange().compareTo(u2.getDateRange());
	}

}
