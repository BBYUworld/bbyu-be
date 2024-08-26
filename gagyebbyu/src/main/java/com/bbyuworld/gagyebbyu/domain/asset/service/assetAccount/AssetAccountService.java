package com.bbyuworld.gagyebbyu.domain.asset.service.assetAccount;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetAccountDto;
import com.bbyuworld.gagyebbyu.domain.asset.enums.AccountType;

import java.util.List;

public interface AssetAccountService {

    List<AssetAccountDto> getAllAssetAccounts(Long userId);
    List<AssetAccountDto> getAssetAccountsByBank(Long userId, String bank);
    List<AssetAccountDto> getAssetAccountsByType(Long userId, AccountType accountType);
    List<AssetAccountDto> getAssetAccountByBankAndType(Long userId, String Bank, AccountType accountType);

}
