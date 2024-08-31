package com.bbyuworld.gagyebbyu.domain.couple.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;

public interface CoupleRepository extends JpaRepository<Couple, Long>, CoupleRepositoryCustom {
}





