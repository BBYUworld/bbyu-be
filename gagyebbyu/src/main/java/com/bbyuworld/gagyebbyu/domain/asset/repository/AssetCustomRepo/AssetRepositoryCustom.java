package com.bbyuworld.gagyebbyu.domain.asset.repository.AssetCustomRepo;

import com.bbyuworld.gagyebbyu.domain.asset.enums.AssetType;

public interface AssetRepositoryCustom {
    Long findCoupleAccount(Long userId, AssetType type);
}
