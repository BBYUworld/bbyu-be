package com.bbyuworld.gagyebbyu.domain.user.entity;

import java.util.Locale;

public enum Gender {
	MALE,
	FEMALE;

	public static Gender getGender(String gender) {
		return Gender.valueOf(gender.toUpperCase(Locale.ENGLISH));
	}
}
