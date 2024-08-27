package com.bbyuworld.gagyebbyu.domain.user.entity;

import java.util.Locale;

public enum Region {
	서울,
	경기,
	인천,
	지방대도시,
	기타지방;

	public static Region getRegion(String region) {
		return Region.valueOf(region.toUpperCase(Locale.KOREAN));
	}
}
