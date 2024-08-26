package com.bbyuworld.gagyebbyu.domain.asset.service.asset;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetAccountDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetCardDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetLoanDto;
import com.bbyuworld.gagyebbyu.domain.asset.entity.Asset;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetAccount;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetCard;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetLoan;

import java.util.List;

public interface AssetService {
    List<AssetDto> getAssets(Long userId);

    Long getSumUserAssets(Long userId);

    List<AssetDto> getCoupleAssets(Long coupleId);

    Long getSumCoupleAssets(Long userId);

    void updateAssetVisibility(Long assetId, boolean isHidden);

    void updateCoupleIdForUsers(Long coupleId, List<Long> userIds);

    /* INSERT 이 있어야 함 */
    boolean insertNewLoan(AssetLoanDto assetLoanDtoDto);
    boolean insertNewCard(AssetCardDto assetCardDto);
    boolean insertNewAccount(AssetAccountDto assetAccountDto);

}
