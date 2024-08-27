package com.bbyuworld.gagyebbyu.domain.expense.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ExpenseMonthDto {
	private Long totalAmount;
	private Long targetAmount;
	private Long amountDifference;
	private List<ExpenseOverviewDto> expenses;
	private List<ExpenseDayDto> dayExpenses;
}
