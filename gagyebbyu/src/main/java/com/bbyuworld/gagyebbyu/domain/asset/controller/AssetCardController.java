package com.bbyuworld.gagyebbyu.domain.asset.controller;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetAccountDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetCardDto;
import com.bbyuworld.gagyebbyu.domain.asset.enums.AccountType;
import com.bbyuworld.gagyebbyu.domain.asset.enums.CardType;
import com.bbyuworld.gagyebbyu.domain.asset.service.assetCard.AssetCardService;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/asset-cards")
public class AssetCardController {
    private final AssetCardService assetCardService;

    @GetMapping
    @RequireJwtToken
    public ResponseEntity<List<AssetCardDto>> getAllAssetAccounts() {
        return ResponseEntity.ok(assetCardService.getAllAssetCards(UserContext.getUserId()));
    }

    @GetMapping("/type")
    @RequireJwtToken
    public ResponseEntity<List<AssetCardDto>> getAssetAccountsByType(
            CardType cardType) {
        try {
            return ResponseEntity.ok(assetCardService.getAllAssetCardByCardType(cardType, UserContext.getUserId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
