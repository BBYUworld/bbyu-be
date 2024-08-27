package com.bbyuworld.gagyebbyu.domain.expense.dto.request;

import java.time.LocalDateTime;

import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.expense.entity.Category;
import com.bbyuworld.gagyebbyu.domain.expense.entity.Expense;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class ExpenseCreateDto {

	private long amount;
	private String place;
	private LocalDateTime date;

	public Expense toEntity(User user, Couple couple, String category) {
		return Expense.builder()
			.user(user)
			.couple(couple)
			.amount(amount)
			.category(Category.getCategory(category))
			.place(place)
			.date(date == null ? LocalDateTime.now() : date)
			.build();
	}
}
