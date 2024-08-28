package com.bbyuworld.gagyebbyu.domain.income.repository;

import com.bbyuworld.gagyebbyu.domain.income.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income, Long>, IncomeRepositoryCustom {
}
