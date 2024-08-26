package com.bbyuworld.gagyebbyu.domain.expense.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ExpenseOverviewDto {
	private Long coupleId;
	private LocalDate date;
	private Long totalAmount;
}
