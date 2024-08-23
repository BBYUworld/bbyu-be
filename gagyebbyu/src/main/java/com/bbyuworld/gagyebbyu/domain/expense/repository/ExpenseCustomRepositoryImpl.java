package com.bbyuworld.gagyebbyu.domain.expense.repository;

import static com.bbyuworld.gagyebbyu.domain.expense.entity.QExpense.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExpenseCustomRepositoryImpl implements ExpenseCustomRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Tuple> findExpenseByMonth(Integer month, Integer year, long coupleId, String sort) {
		OrderSpecifier<?> orderSpecifier = getDateOrderSpecifier(sort);

		return jpaQueryFactory.select(
				Expressions.dateTemplate(LocalDate.class, "DATE({0})", expense.date),
				expense.couple,
				expense.amount.sum())
			.from(expense)
			.where(
				expense.couple.coupleId.eq(coupleId),
				getDate(month, year)
			)
			.groupBy(Expressions.dateTemplate(LocalDate.class, "DATE({0})", expense.date),
				expense.couple)
			.orderBy(orderSpecifier)
			.fetch();
	}

	private OrderSpecifier<LocalDate> getDateOrderSpecifier(String sort) {
		if ("desc".equalsIgnoreCase(sort)) {
			return Expressions.dateTemplate(LocalDate.class, "DATE({0})", expense.date).desc();
		} else {
			return Expressions.dateTemplate(LocalDate.class, "DATE({0})", expense.date).asc();
		}
	}

	private BooleanExpression getDate(Integer month, Integer year) {
		LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
		LocalDateTime endDate = startDate.plusMonths(1).minusNanos(1);

		return expense.date.between(startDate, endDate);
	}
}
