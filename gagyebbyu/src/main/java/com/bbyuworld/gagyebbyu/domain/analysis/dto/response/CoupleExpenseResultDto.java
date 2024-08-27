package com.bbyuworld.gagyebbyu.domain.analysis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CoupleExpenseResultDto {
	private String category;
	private long averageAge;
	private long monthlyIncome;
	private long anotherCoupleMonthExpenseAvg;
	private long coupleMonthExpense;
}
