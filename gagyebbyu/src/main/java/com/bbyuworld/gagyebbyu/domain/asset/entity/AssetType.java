package com.bbyuworld.gagyebbyu.domain.asset.entity;

import java.util.Locale;

import com.bbyuworld.gagyebbyu.domain.fund.entity.TransactionType;

public enum AssetType {
	CARD,
	ACCOUNT,
	LOAN,
	STOCK,
	REAL_ESTATE;

	public static AssetType getAssetType(final String type) {
		return AssetType.valueOf(type.toUpperCase(Locale.ENGLISH));
	}
}
