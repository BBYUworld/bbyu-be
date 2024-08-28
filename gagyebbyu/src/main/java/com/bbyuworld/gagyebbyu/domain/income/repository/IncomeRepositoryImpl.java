package com.bbyuworld.gagyebbyu.domain.income.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.bbyuworld.gagyebbyu.domain.expense.entity.QExpense.expense;
import static com.bbyuworld.gagyebbyu.domain.income.entity.QIncome.income;

@Repository
@RequiredArgsConstructor
public class IncomeRepositoryImpl implements IncomeRepositoryCustom{
    private final JPAQueryFactory queryFactory;


    @Override
    public List<Tuple> findIncomesByMonth(Integer month, Integer year, long coupleId, String sort) {
        OrderSpecifier<?> orderSpecifier = getDateOrderSpecifier(sort);
        return queryFactory.select(
                income.date,
                income.couple,
                income.amount)
                .from(income)
                .where(
                        income.couple.coupleId.eq(coupleId),
                        getMonth(month, year)
                ).orderBy(orderSpecifier)
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
}
