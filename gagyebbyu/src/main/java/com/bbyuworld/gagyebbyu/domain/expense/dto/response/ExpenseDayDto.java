package com.bbyuworld.gagyebbyu.domain.expense.dto.response;

import java.time.LocalDateTime;

import com.bbyuworld.gagyebbyu.domain.expense.entity.Category;
import com.bbyuworld.gagyebbyu.domain.expense.entity.Expense;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ExpenseDayDto {
	private String name;
	private Long amount;
	private Category category;
	private LocalDateTime date;
	private String memo;
	private String place;

	public static ExpenseDayDto from(Expense expense) {
		return new ExpenseDayDto(
			expense.getUser().getName(),
			expense.getAmount(),
			expense.getCategory(),
			expense.getDate(),
			expense.getMemo(),
			expense.getPlace()
		);
	}
}
