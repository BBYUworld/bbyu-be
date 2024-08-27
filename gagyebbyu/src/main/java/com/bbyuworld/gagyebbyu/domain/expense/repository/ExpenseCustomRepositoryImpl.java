package com.bbyuworld.gagyebbyu.domain.expense.repository;

import static com.bbyuworld.gagyebbyu.domain.couple.entity.QCouple.*;
import static com.bbyuworld.gagyebbyu.domain.expense.entity.QExpense.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.bbyuworld.gagyebbyu.domain.expense.entity.Category;
import com.bbyuworld.gagyebbyu.domain.expense.entity.Expense;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExpenseCustomRepositoryImpl implements ExpenseCustomRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Long findTotalExpenditureForMonth(Long coupleId) {
		return jpaQueryFactory
			.select(expense.amount.sum())
			.from(expense)
			.join(expense.couple, couple)
			.where(
				expense.couple.coupleId.eq(coupleId),
				getMonth(LocalDate.now().getMonth().getValue() - 1, LocalDate.now().getYear())  // 해당 달에 대한 날짜 필터링
			)
			.fetchOne();
	}

	@Override
	public List<Tuple> findCategoryWiseExpenditureForMonth(Long coupleId, Long totalAmount) {

		return jpaQueryFactory
			.select(
				expense.category,
				expense.amount.sum(),
				Expressions.numberTemplate(Double.class, "round(({0} / {1}) * 100, 1)", expense.amount.sum(),
					totalAmount).as("percentage")
			)
			.from(expense)
			.join(expense.couple, couple)
			.where(
				expense.couple.coupleId.eq(coupleId),
				getMonth(LocalDate.now().getMonth().getValue() - 1, LocalDate.now().getYear())
			)
			.groupBy(expense.category)
			.orderBy(expense.amount.sum().desc())
			.fetch();
	}

	@Override
	public Category findTopCategoryForCoupleLastMonth(Long coupleId) {
		return jpaQueryFactory
			.select(expense.category)
			.from(expense)
			.join(expense.couple, couple)
			.where(
				expense.couple.coupleId.eq(coupleId),
				getMonth(LocalDate.now().getMonth().getValue() - 1, LocalDate.now().getYear())
			)
			.groupBy(expense.category)
			.orderBy(expense.amount.sum().desc())
			.limit(1) // LIMIT 1 적용
			.fetchOne(); // 단일 결과를 가져옵니다.
	}

	@Override
	public Double findAverageExpenditureForEligibleCouples(int startAge, int endAge, long startIncome, long endIncome) {

		NumberExpression<Integer> averageAge = couple.user1.age.add(couple.user2.age).divide(2);
		BooleanExpression ageCondition = averageAge.between(startAge, endAge);
		System.out.println(ageCondition);

		NumberExpression<Long> averageIncome = couple.user1.monthlyIncome.add(couple.user2.monthlyIncome).divide(2);
		BooleanExpression incomeCondition = averageIncome.between(startIncome, endIncome);
		System.out.println(incomeCondition);

		return jpaQueryFactory
			.select(expense.amount.avg())
			.from(expense)
			.join(expense.couple, couple)
			.where(ageCondition, incomeCondition,
				getMonth(LocalDate.now().getMonth().getValue() - 1, LocalDate.now().getYear()))
			.fetchOne();
	}

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
		if (day == null) {
			return getMonth(month, year);
		}

		LocalDateTime startOfDay = LocalDateTime.of(year, month, day, 0, 0, 0); // 해당 일의 시작 시간
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

		return expense.date.between(startOfDay, endOfDay);
	}
}
