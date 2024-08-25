package com.bbyuworld.gagyebbyu.domain.couple.entity;

import java.util.Locale;

public enum Status {
	WAIT,
	CONNECT;

	public static Status getStatus(String status) {
		return Status.valueOf(status.toUpperCase(Locale.ENGLISH));
	}
}
