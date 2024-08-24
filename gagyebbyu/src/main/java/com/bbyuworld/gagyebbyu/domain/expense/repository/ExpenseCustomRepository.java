package com.bbyuworld.gagyebbyu.domain.expense.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bbyuworld.gagyebbyu.domain.expense.entity.Expense;
import com.querydsl.core.Tuple;

@Repository
public interface ExpenseCustomRepository {

	List<Tuple> findExpenseByMonth(Integer month, Integer year, long coupeId, String sort);

	List<Expense> findExpenseByDay(Integer day, Integer month, Integer year, long coupeId, String sort);
}


