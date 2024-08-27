package com.bbyuworld.gagyebbyu.domain.analysis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bbyuworld.gagyebbyu.domain.analysis.entity.AnnualAsset;
import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;

public interface AnnualAssetRepository extends JpaRepository<AnnualAsset, Long> {

	List<AnnualAsset> findByCouple_CoupleId(Long coupleId);

	Optional<AnnualAsset> findByCoupleAndYear(Couple couple, int year); // 수정된 부분
}
