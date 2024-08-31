package com.bbyuworld.gagyebbyu.domain.asset.service.asset;

import java.util.List;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetAccountDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetCardDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetLoanDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetResponseDto;
import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;

public interface AssetService {
	List<AssetDto> getAssets(Long userId);

	Long getSumUserAssets(Long userId);

	AssetResponseDto getCoupleAssetsWithoutLoan(Long userId);

	List<AssetDto> getCoupleAssets(Long userId);

	Long getSumCoupleAssets(Long userId);

	void updateAssetVisibility(Long assetId, boolean isHidden);

	void updateUserAssetsToCouple(Couple couple, Long user1Id, Long user2Id);

	Long getSumCoupleAccountAssets(Long userId);

	/* INSERT 이 있어야 함 */
	boolean insertNewLoan(AssetLoanDto assetLoanDtoDto, Long userId);

	boolean insertNewCard(AssetCardDto assetCardDto, Long userId);

	boolean insertNewAccount(AssetAccountDto assetAccountDto, Long userId);

	/* 주택담보대출의 총 액 반환 */
	long getSumMortgage(Long userId);
}
