package com.bbyuworld.gagyebbyu.domain.analysis.dto.response;

import com.bbyuworld.gagyebbyu.domain.expense.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CoupleExpenseStatisticsDto {
	private Category label;
	private long amount;
	private double percentage;
}
