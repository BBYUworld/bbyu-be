package com.bbyuworld.gagyebbyu.domain.recommend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bbyuworld.gagyebbyu.domain.recommend.entity.Deposit;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
}