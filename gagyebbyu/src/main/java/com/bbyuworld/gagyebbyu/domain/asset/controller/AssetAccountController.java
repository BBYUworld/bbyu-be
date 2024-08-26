package com.bbyuworld.gagyebbyu.domain.asset.controller;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetAccountDto;
import com.bbyuworld.gagyebbyu.domain.asset.enums.AccountType;
import com.bbyuworld.gagyebbyu.domain.asset.service.assetAccount.AssetAccountService;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/asset-accounts")
@RequiredArgsConstructor
public class AssetAccountController {
    private final AssetAccountService assetAccountService;

    /**
     * hidden이 0인 값들을 불러오는 거 1: 숨김 2: 보이기
     *
     * @return List<AssetAccountDto> 형태의 사용자가 보이게 설정한 자산 리스트
     */
    @GetMapping
    @RequireJwtToken
    public ResponseEntity<List<AssetAccountDto>> getAllAssetAccounts() {
        return ResponseEntity.ok(assetAccountService.getAllAssetAccounts(UserContext.getUserId()));
    }

    /**
     * 은행별 자산을 반환
     *
     * @param bankName 은행명
     * @return List 형식으로 해당 은행의 자산 목록 반환
     */
    @GetMapping("/bank-name")
    @RequireJwtToken
    public ResponseEntity<List<AssetAccountDto>> getAssetAccountsByBank(
        String bankName) {
        return ResponseEntity.ok(assetAccountService.getAssetAccountsByBank(UserContext.getUserId(), bankName));
    }

    /**
     * 입출금 계좌, 예금, 적금, (주식?) 의 자산 목록을 반환
     *
     * @param accountType 위에 적힌 3~4개의 타입
     * @return List 형식의 예적금, 입출금, 주식 계좌들
     */
    @GetMapping("/type")
    @RequireJwtToken
    public ResponseEntity<List<AssetAccountDto>> getAssetAccountsByType(
            AccountType accountType) {
        try {
            return ResponseEntity.ok(assetAccountService.getAssetAccountsByType(UserContext.getUserId(), accountType));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 은행별 자산의 종류에 따른 자산 목록 반환
     *
     * @param bankName 은행명 자산종류
     * @param accountType 타입 번호
     * @return List 형식의 자산 내역
     */
    @GetMapping("/bank/type")
    @RequireJwtToken
    public ResponseEntity<List<AssetAccountDto>> getAssetAccountsByBankAndType(
            String bankName, AccountType accountType) {
        try {
            return ResponseEntity.ok(assetAccountService.getAssetAccountByBankAndType(UserContext.getUserId(), bankName, accountType));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 최대 자산이 들어있는 계좌를 반환
     *
     * @return AssetAccount 최대 금액이 들어있는 계좌 (여기서 type 써서 세분화해도 좋을 듯?)
     */
    @GetMapping("/max")
    @RequireJwtToken
    public ResponseEntity<AssetAccountDto> getMaxAssetAccount() {
        return ResponseEntity.ok(assetAccountService.getMaxAssetAccount(UserContext.getUserId()));
    }

    /**
     * 숨겨져 있는 계좌가 "n"개 있습니다! 에서 n을 보여주려고 만듦
     *
     * @return Long 몇 개
     */
    @GetMapping("/count")
    @RequireJwtToken
    public ResponseEntity<Long> getCountByHidden() {
        return ResponseEntity.ok(assetAccountService.getCountByHidden(UserContext.getUserId()));
    }
}