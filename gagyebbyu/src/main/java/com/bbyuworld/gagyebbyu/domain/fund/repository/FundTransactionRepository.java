package com.bbyuworld.gagyebbyu.domain.fund.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bbyuworld.gagyebbyu.domain.fund.entity.FundTransaction;

public interface FundTransactionRepository extends JpaRepository<FundTransaction, Long> {
	List<FundTransaction> findByFund_FundId(long fundId);
}
