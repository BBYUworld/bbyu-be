package com.bbyuworld.gagyebbyu.domain.expense.entity;

import java.util.Locale;

public enum Category {
	교육,
	교통_자동차,
	기타소비,
	대형마트,
	미용,
	배달,
	보험,
	생필품,
	생활서비스,
	세금_공과금,
	쇼핑몰,
	여행_숙박,
	외식,
	의료_건강,
	주류_펍,
	취미_여가,
	카페,
	통신,
	편의점;

	public static Category getCategory(String category) {
		return Category.valueOf(category.toUpperCase(Locale.KOREAN));
	}
}