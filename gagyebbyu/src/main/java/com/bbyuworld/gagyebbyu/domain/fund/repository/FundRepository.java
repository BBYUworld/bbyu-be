package com.bbyuworld.gagyebbyu.domain.fund.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bbyuworld.gagyebbyu.domain.fund.entity.Fund;

public interface FundRepository extends JpaRepository<Fund, Long> {
	Optional<Fund> findByCouple_CoupleIdAndIsEndedIsFalse(long coupleId);
}
