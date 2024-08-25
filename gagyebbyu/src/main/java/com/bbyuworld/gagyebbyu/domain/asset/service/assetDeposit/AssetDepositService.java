package com.bbyuworld.gagyebbyu.domain.asset.service.assetDeposit;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetDepositDto;

import java.util.List;

public interface AssetDepositService {

    List<AssetDepositDto> getAllAssetDeposit();
    List<AssetDepositDto> getAssetDepositByBank(String bank);
    List<AssetDepositDto> getAssetDepositByType(String type);
    List<AssetDepositDto> getAssetDepositByBankAndType(String Bank, String type);

    AssetDepositDto getMaxAssetDeposit();
    Long getCountByHidden();
    AssetDepositDto getAssetDepositByAssetId(Long assetId);

}
