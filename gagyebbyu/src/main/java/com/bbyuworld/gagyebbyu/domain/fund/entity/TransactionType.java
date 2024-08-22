package com.bbyuworld.gagyebbyu.domain.fund.entity;

import java.util.Locale;

public enum TransactionType {
	PLUS,
	MINUS;

	public static TransactionType getType(String type) {
		return TransactionType.valueOf(type.toUpperCase(Locale.ENGLISH));
	}
}
