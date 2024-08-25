package com.bbyuworld.gagyebbyu.domain.asset.repository;

import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssetCardRepository extends JpaRepository<AssetCard, Long> {
    @Query("SELECT ac FROM AssetCard ac WHERE ac.assetId IN (SELECT a.assetId FROM Asset a WHERE a.userId = :userId)")
    List<AssetCard> findByUserId(@Param("userId") Long userId);

    Optional<AssetCard> findByAssetId(Long assetId);
}
