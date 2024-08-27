package com.bbyuworld.gagyebbyu.domain.asset.service.asset;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetAccountDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetCardDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetLoanDto;
import com.bbyuworld.gagyebbyu.domain.asset.entity.Asset;
import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssetService {
    List<AssetDto> getAssets(Long userId);

    Long getSumUserAssets(Long userId);

    List<AssetDto> getCoupleAssets(Long userId);

    Long getSumCoupleAssets(Long userId);

    void updateAssetVisibility(Long assetId, boolean isHidden);

    void updateUserAssetsToCouple(Couple couple, Long user1Id, Long user2Id);

    /* INSERT 이 있어야 함 */
    boolean insertNewLoan(AssetLoanDto assetLoanDtoDto, Long userId);
    boolean insertNewCard(AssetCardDto assetCardDto, Long userId);
    boolean insertNewAccount(AssetAccountDto assetAccountDto, Long userId);

    /* 상세정보 출력 */

}
