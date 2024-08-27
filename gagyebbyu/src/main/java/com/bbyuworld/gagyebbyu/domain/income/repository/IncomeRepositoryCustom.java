package com.bbyuworld.gagyebbyu.domain.income.repository;

import com.querydsl.core.Tuple;

import java.util.List;

public interface IncomeRepositoryCustom {
    public List<Tuple> findIncomesByMonth(Integer month, Integer year, long coupleId, String sort);
}
