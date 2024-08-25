package com.bbyuworld.gagyebbyu.domain.asset.controller;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetAccountDto;
import com.bbyuworld.gagyebbyu.domain.asset.enums.AccountType;
import com.bbyuworld.gagyebbyu.domain.asset.service.assetAccount.AssetAccountService;
import lombok.RequiredArgsConstructor;
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
    @PostMapping
    public ResponseEntity<List<AssetAccountDto>> getAllAssetAccounts(@RequestBody Long userId) {
        return ResponseEntity.ok(assetAccountService.postAllAssetAccounts(userId));
    }

    /**
     * 은행별 자산을 반환
     *
     * @param request 은행명과 사용자 id
     * @return List 형식으로 해당 은행의 자산 목록 반환
     */
    @PostMapping("/bank-name")
    public ResponseEntity<List<AssetAccountDto>> getAssetAccountsByBank(
            @RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        String bank = (String) request.get("bankName");
        return ResponseEntity.ok(assetAccountService.postAssetAccountsByBank(userId, bank));
    }

    /**
     * 입출금 계좌, 예금, 적금, (주식?) 의 자산 목록을 반환
     *
     * @param request 위에 적힌 3~4개의 타입과 사용자 Id
     * @return List 형식의 예적금, 입출금, 주식 계좌들
     */
    @PostMapping("/type")
    public ResponseEntity<List<AssetAccountDto>> getAssetAccountsByType(
            @RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        String accountTypeStr = (String) request.get("accountType");
        try {
            AccountType accountType = AccountType.valueOf(accountTypeStr.toUpperCase());
            return ResponseEntity.ok(assetAccountService.postAssetAccountsByType(userId, accountType));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 은행별 자산의 종류에 따른 자산 목록 반환
     *
     * @param request 은행명 자산종류
     * @return List 형식의 자산 내역
     */
    @PostMapping("/bank/type")
    public ResponseEntity<List<AssetAccountDto>> getAssetAccountsByBankAndType(
            @RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        String bankName = (String) request.get("bankName");
        String accountTypeStr = (String) request.get("accountType");
        try {
            AccountType accountType = AccountType.valueOf(accountTypeStr.toUpperCase());
            return ResponseEntity.ok(assetAccountService.postAssetAccountByBankAndType(userId, bankName, accountType));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 최대 자산이 들어있는 계좌를 반환
     *
     * @return AssetAccount 최대 금액이 들어있는 계좌 (여기서 type 써서 세분화해도 좋을 듯?)
     */
    @PostMapping("/max")
    public ResponseEntity<AssetAccountDto> getMaxAssetAccount(@RequestBody Long userId) {
        return ResponseEntity.ok(assetAccountService.postMaxAssetAccount(userId));
    }

    /**
     * 숨겨져 있는 계좌가 "n"개 있습니다! 에서 n을 보여주려고 만듦
     *
     * @return Long 몇 개
     */
    @PostMapping("/count")
    public ResponseEntity<Long> getCountByHidden(@RequestBody Long userId) {
        return ResponseEntity.ok(assetAccountService.postCountByHidden(userId));
    }
}