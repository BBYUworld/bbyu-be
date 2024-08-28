package com.bbyuworld.gagyebbyu.domain.asset.controller;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetAccountDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetCardDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetLoanDto;
import com.bbyuworld.gagyebbyu.domain.asset.service.asset.AssetService;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assets")
public class AssetController {
    private final AssetService assetService;

    @GetMapping("/user")
    @RequireJwtToken
    public List<AssetDto> getUserAssets() {
        return assetService.getAssets(UserContext.getUserId());
    }

    @GetMapping("/user/sum")
    @RequireJwtToken
    public Long getUserAssetSum() {
        return assetService.getSumUserAssets(UserContext.getUserId());
    }


    @GetMapping("/couple")
    @RequireJwtToken
    public List<AssetDto> getCoupleAssets() {
        return assetService.getCoupleAssets(UserContext.getUserId());
    }

    @GetMapping("/couple/sum")
    @RequireJwtToken
    public Long getCoupleAssetSum() {
        return assetService.getSumCoupleAssets(UserContext.getUserId());
    }

    @PatchMapping("/{assetId}/visibility")
    public void updateAssetVisibility(@PathVariable Long assetId, @RequestParam("isHidden") boolean isHidden) {
        assetService.updateAssetVisibility(assetId, isHidden);
    }

    @PostMapping("/add-loan")
    @RequireJwtToken
    public boolean addNewLoan(@RequestBody AssetLoanDto assetLoanDto) {
        return assetService.insertNewLoan(assetLoanDto, UserContext.getUserId());
    }

    @PostMapping("/add-account")
    @RequireJwtToken
    public boolean addNewAccount(@RequestBody AssetAccountDto assetAccountDto) {
        return assetService.insertNewAccount(assetAccountDto, UserContext.getUserId());
    }

    @PostMapping("/add-card")
    @RequireJwtToken
    public boolean addNewCard(@RequestBody AssetCardDto assetCardDto) {
        return assetService.insertNewCard(assetCardDto, UserContext.getUserId());
    }

//    @GetMapping("/sum-user")
//    @RequireJwtToken
//    public Long getSumUserAssets(@Param("type") String type) {
//        return assetService.getSumUserAssets(UserContext.getUserId(), type);
//    }

}