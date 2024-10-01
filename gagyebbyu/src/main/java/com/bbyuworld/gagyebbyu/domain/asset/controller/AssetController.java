package com.bbyuworld.gagyebbyu.domain.asset.controller;

import java.util.List;

import com.bbyuworld.gagyebbyu.domain.asset.enums.AssetType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetAccountDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetCardDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetLoanDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetResponseDto;
import com.bbyuworld.gagyebbyu.domain.asset.service.asset.AssetService;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;

import lombok.RequiredArgsConstructor;

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

	@GetMapping("/couple/main")
	@RequireJwtToken
	public AssetResponseDto getCoupleAssetsWithoutLoan() {
		return assetService.getCoupleAssetsWithoutLoan(UserContext.getUserId());
	}

	@GetMapping("/couple/sum")
	@RequireJwtToken
	public Long getCoupleAssetSum() {
		return assetService.getSumCoupleAssets(UserContext.getUserId());
	}

	@GetMapping("/couple/account/sum")
	@RequireJwtToken
	public Long getCoupleAssetAccountSum() {
		Long userId = UserContext.getUserId();
		return assetService.getSumCoupleAccountAssets(userId);
	}

	@GetMapping("/couple/account/{assetType}")
	@RequireJwtToken
	public ResponseEntity<List<AssetDto>> getCoupleAssetList(@PathVariable String assetType) {
		Long userId = UserContext.getUserId();
		AssetType type;
		try {
			type = AssetType.valueOf(assetType.toUpperCase());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(null);
		}
		List<AssetDto> assets = assetService.getCoupleAssetList(userId, type);
		return ResponseEntity.ok(assets);
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

}