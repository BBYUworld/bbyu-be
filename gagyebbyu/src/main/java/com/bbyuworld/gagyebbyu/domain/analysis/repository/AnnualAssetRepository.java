package com.bbyuworld.gagyebbyu.domain.analysis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bbyuworld.gagyebbyu.domain.analysis.entity.AnnualAsset;

public interface AnnualAssetRepository extends JpaRepository<AnnualAsset, Long> {
	List<AnnualAsset> findByCouple_CoupleId(Long coupleId);

}
