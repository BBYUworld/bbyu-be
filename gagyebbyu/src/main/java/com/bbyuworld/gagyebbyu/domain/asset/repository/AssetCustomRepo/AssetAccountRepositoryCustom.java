package com.bbyuworld.gagyebbyu.domain.asset.repository.AssetCustomRepo;

import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetAccount;

public interface AssetAccountRepositoryCustom {
    AssetAccount findAssetAccountByAccountNo(String accountNo);
}
