package com.bbyuworld.gagyebbyu.domain.asset.service.assetCard;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetCardDto;
import com.bbyuworld.gagyebbyu.domain.asset.enums.CardType;

import java.util.List;

public interface AssetCardService {
    List<AssetCardDto> getAllAssetCards(Long userId);
    List<AssetCardDto> getAllAssetCardByCardType(CardType cardType, Long userId);
}
