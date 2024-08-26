package com.bbyuworld.gagyebbyu.domain.asset.controller;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetAccountDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetCardDto;
import com.bbyuworld.gagyebbyu.domain.asset.enums.AccountType;
import com.bbyuworld.gagyebbyu.domain.asset.enums.CardType;
import com.bbyuworld.gagyebbyu.domain.asset.service.assetCard.AssetCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/asset-cards")
public class AssetCardController {
    private final AssetCardService assetCardService;

    @PostMapping
    public ResponseEntity<List<AssetCardDto>> getAllAssetAccounts(@RequestBody Long userId) {
        return ResponseEntity.ok(assetCardService.getAllAssetCards(userId));
    }

    @PostMapping("/type")
    public ResponseEntity<List<AssetCardDto>> getAssetAccountsByType(
            @RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        String cardTypeStr = (String) request.get("cardType");
        try {
            CardType cardType = CardType.valueOf(cardTypeStr.toUpperCase());
            return ResponseEntity.ok(assetCardService.getAllAssetCardByCardType(cardType, userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
