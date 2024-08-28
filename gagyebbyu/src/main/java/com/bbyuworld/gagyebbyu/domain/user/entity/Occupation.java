package com.bbyuworld.gagyebbyu.domain.user.entity;

import java.util.Locale;

public enum Occupation {
	공무원,
	대기업직원,
	중소기업직원,
	자영업자,
	프리랜서,
	무직,
	은퇴자;

	public static Region getOccupation(String occupation) {
		return Region.valueOf(occupation.toUpperCase(Locale.KOREAN));
	}
}
