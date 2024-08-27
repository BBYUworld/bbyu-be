package com.bbyuworld.gagyebbyu.domain.analysis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bbyuworld.gagyebbyu.domain.analysis.entity.AnnualAsset;
import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;

public interface AnnualAssetRepository extends JpaRepository<AnnualAsset, Long> {

	List<AnnualAsset> findByCouple_CoupleId(Long coupleId);

	Optional<AnnualAsset> findByCoupleAndYear(Couple couple, int year);

	// 특정 커플의 작년 자산 총합 조회
	@Query("SELECT a.totalAssets FROM AnnualAsset a WHERE a.couple.coupleId = :coupleId AND a.year = :lastYear")
	Long findTotalAssetsForCoupleLastYear(@Param("coupleId") Long coupleId, @Param("lastYear") int lastYear);

	// 특정 커플의 작년 자산 중 가장 많은 비중을 차지한 자산 타입 조회
	@Query("SELECT CASE WHEN a.cashAssets >= a.investmentAssets AND a.cashAssets >= a.realEstateAssets THEN 'CASH' " +
			"WHEN a.investmentAssets >= a.cashAssets AND a.investmentAssets >= a.realEstateAssets THEN 'INVESTMENT' " +
			"ELSE 'REAL_ESTATE' END " +
			"FROM AnnualAsset a WHERE a.couple.coupleId = :coupleId AND a.year = :lastYear")
	String findTopAssetTypeForCoupleLastYear(@Param("coupleId") Long coupleId, @Param("lastYear") int lastYear);

}
