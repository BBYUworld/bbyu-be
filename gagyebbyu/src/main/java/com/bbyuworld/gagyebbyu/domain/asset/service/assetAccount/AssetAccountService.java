package com.bbyuworld.gagyebbyu.domain.asset.service.assetAccount;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetAccountDto;
import com.bbyuworld.gagyebbyu.domain.asset.enums.AccountType;

import java.util.List;

public interface AssetAccountService {

    List<AssetAccountDto> postAllAssetAccounts(Long userId);
    List<AssetAccountDto> postAssetAccountsByBank(Long userId, String bank);
    List<AssetAccountDto> postAssetAccountsByType(Long userId, AccountType accountType);
    List<AssetAccountDto> postAssetAccountByBankAndType(Long userId, String Bank, AccountType accountType);

    AssetAccountDto postMaxAssetAccount(Long userId);
    Long postCountByHidden(Long userId);

}
