package com.bbyuworld.gagyebbyu.domain.expense.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bbyuworld.gagyebbyu.domain.expense.entity.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>, ExpenseCustomRepository {
}
