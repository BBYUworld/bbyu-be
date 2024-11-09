package com.bbyuworld.gagyebbyu.domain.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bbyuworld.gagyebbyu.domain.analysis.entity.AnnualAsset;

@Repository
public interface MonthlyExpenseRepository extends JpaRepository<AnnualAsset, Long> {

}
