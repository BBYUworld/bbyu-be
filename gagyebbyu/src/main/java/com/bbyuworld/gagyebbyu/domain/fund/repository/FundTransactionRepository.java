package com.bbyuworld.gagyebbyu.domain.fund.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bbyuworld.gagyebbyu.domain.fund.entity.FundTransaction;

public interface FundTransactionRepository extends JpaRepository<FundTransaction, Long> {
}
