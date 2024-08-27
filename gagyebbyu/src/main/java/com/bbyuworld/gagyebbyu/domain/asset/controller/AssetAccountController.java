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

@RestController
@RequestMapping("/api/asset-accounts")
@RequiredArgsConstructor
public class AssetAccountController {
    private final AssetAccountService assetAccountService;

    /**
     * 사용자 계좌 조회
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
    public ResponseEntity<List<AssetAccountDto>> getAssetAccountsByBank( @Param("bankName")
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
            @Param("accountType") AccountType accountType) {
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
            @Param("bankName") String bankName, @Param("accountType") AccountType accountType) {
        try {
            return ResponseEntity.ok(assetAccountService.getAssetAccountByBankAndType(UserContext.getUserId(), bankName, accountType));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}