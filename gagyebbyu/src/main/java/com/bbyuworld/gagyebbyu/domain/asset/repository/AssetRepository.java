package com.bbyuworld.gagyebbyu.domain.asset.repository;

import com.bbyuworld.gagyebbyu.domain.asset.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long>, AssetRepositoryCustom {
}
