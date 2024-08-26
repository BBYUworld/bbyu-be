package com.bbyuworld.gagyebbyu.domain.asset.service.assetCard;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetCardDto;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetCard;
import com.bbyuworld.gagyebbyu.domain.asset.enums.CardType;
import com.bbyuworld.gagyebbyu.domain.asset.repository.AssetCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetCardServiceImpl implements AssetCardService {
    final AssetCardRepository assetCardRepository;
    /**
     * 사용자 전체 카드 보기
     *
     * @return userId에 맞으면서 hidden 이 false 인 모든 카드
     */
    @Override
    public List<AssetCardDto> getAllAssetCards(Long userId) {
        return assetCardRepository.findByUserIdAndIsHiddenFalse(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 카드 타입별로 보기
     *
     * @param cardType 카드 타입에 맞는 것 CREDIT 또는 CHECK
     * @return userId 에 맞으면서 hidden 이 false 이며 cardType 에 맞는 모든 것
     */
    @Override
    public List<AssetCardDto> getAllAssetCardByCardType(CardType cardType, Long userId) {
        return assetCardRepository.findAllByUserIdAndCardTypeAndIsHiddenFalse(cardType, userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    protected AssetCardDto convertToDto(AssetCard assetCard) {
        AssetCardDto assetCardDto = new AssetCardDto();
        AssetCardDto.builder()
                .assetId(assetCard.getAssetId())
                .userId(assetCard.getUserId())
                .bankName(assetCard.getBankName())
                .cardName(assetCard.getCardName())
                .cardNumber(assetCard.getCardNumber())
                .cardType(assetCard.getCardType().name())
                .build();

        return assetCardDto;

    }
}
