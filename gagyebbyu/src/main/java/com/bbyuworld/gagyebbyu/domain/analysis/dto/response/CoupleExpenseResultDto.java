package com.bbyuworld.gagyebbyu.domain.analysis.dto.response;

import com.bbyuworld.gagyebbyu.domain.expense.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CoupleExpenseResultDto {
	private Category category;
	private long averageAge;
	private long monthlyIncome;
	private long anotherCoupleMonthExpenseAvg;
	private long coupleMonthExpense;
}
