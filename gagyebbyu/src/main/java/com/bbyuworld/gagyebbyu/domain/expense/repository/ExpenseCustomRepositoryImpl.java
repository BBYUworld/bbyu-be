package com.bbyuworld.gagyebbyu.domain.expense.repository;

import static com.bbyuworld.gagyebbyu.domain.expense.entity.QExpense.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.bbyuworld.gagyebbyu.domain.expense.entity.Expense;
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
				getMonth(month, year)
			)
			.groupBy(Expressions.dateTemplate(LocalDate.class, "DATE({0})", expense.date),
				expense.couple)
			.orderBy(orderSpecifier)
			.fetch();
	}

	@Override
	public List<Expense> findExpenseByDay(Integer day, Integer month, Integer year, long coupleId, String sort) {

		OrderSpecifier<?> orderSpecifier = getDateOrderSpecifier(sort);

		// 날짜 범위를 설정합니다.
		LocalDateTime startOfDay = LocalDateTime.of(year, month, day, 0, 0, 0); // 해당 일의 시작 시간
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1); // 해당 일의 마지막 순간

		return jpaQueryFactory.selectFrom(expense)
			.where(
				expense.couple.coupleId.eq(coupleId),
				getDay(day, month, year)
			)
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

	private BooleanExpression getMonth(Integer month, Integer year) {
		LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
		LocalDateTime endDate = startDate.plusMonths(1).minusNanos(1);

		return expense.date.between(startDate, endDate);
	}

	private BooleanExpression getDay(Integer day, Integer month, Integer year) {
		LocalDateTime startOfDay = LocalDateTime.of(year, month, day, 0, 0, 0); // 해당 일의 시작 시간
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

		return expense.date.between(startOfDay, endOfDay);
	}
}
