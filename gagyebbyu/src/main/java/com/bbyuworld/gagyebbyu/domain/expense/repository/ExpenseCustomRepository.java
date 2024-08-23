package com.bbyuworld.gagyebbyu.domain.expense.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;

@Repository
public interface ExpenseCustomRepository {

	List<Tuple> findExpenseByMonth(Integer month, Integer year, long coupeId, String sort);
}


