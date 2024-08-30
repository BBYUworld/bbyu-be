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

	/**
	 * 특정 커플의 작년 자산 총합 조회()
	 * @param coupleId 커플 ID
	 * @param lastYear 작년
	 * @return
	 */
	@Query("SELECT a.totalAssets FROM AnnualAsset a WHERE a.couple.coupleId = :coupleId AND a.year = :lastYear")
	Long findTotalAssetsForCoupleLastYear(@Param("coupleId") Long coupleId, @Param("lastYear") int lastYear);

}
