package com.bbyuworld.gagyebbyu.domain.asset.controller;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetLoanDto;
import com.bbyuworld.gagyebbyu.domain.asset.service.assetLoan.AssetLoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/asset-loans")
@RequiredArgsConstructor
public class AssetLoanController {

    private final AssetLoanService assetLoanService;

    /**
     * 사용자의 특정 loan 상품을 불러오기 위함
     *
     * @param request 모든건 사용자 id 기반
     * @return 특정 loan 상품
     */
    @PostMapping("/target")
    public ResponseEntity<AssetLoanDto> getUserTargetLoan(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        Long assetId = request.get("assetId");
        return assetLoanService.postUserTargetLoan(userId, assetId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 사용자의 id에 맞는 모든 대출 상품 출력
     *
     * @param userId 모든건 사용자 id 기반
     * @return 사용자가 가입한 대출 상품 list
     */
    @PostMapping("/user")
    public ResponseEntity<List<AssetLoanDto>> getUserLoans(@RequestBody Long userId) {
        return ResponseEntity.ok(assetLoanService.postUserLoans(userId));
    }

    /**
     * 남은 대출금 정렬 내림차순
     *
     * @param userId 모든건 사용자 id 기반
     * @return 갚을 대출액이 많은 순 정렬
     */
    @PostMapping("/order-remain-desc")
    public ResponseEntity<List<AssetLoanDto>> getOrderByRemainAmountDesc(@RequestBody Long userId) {
        return ResponseEntity.ok(assetLoanService.postOrderByRemainAmountDesc(userId));
    }

    /**
     * 남은 대출금 정렬 오름차순
     *
     * @param userId 모든건 사용자 id 기반
     * @return 갚을 대출액이 적은ㄴ 순 정렬
     */
    @PostMapping("/order-remain-asc")
    public ResponseEntity<List<AssetLoanDto>> getOrderByRemainAmountAsc(@RequestBody Long userId) {
        return ResponseEntity.ok(assetLoanService.postOrderByRemainAmountAsc(userId));
    }

    /**
     * 대출금이 많은순 정렬
     *
     * @param userId 모든건 사용자 id 기반
     * @return 대출 리스트
     */
    @PostMapping("/order-amount-desc")
    public ResponseEntity<List<AssetLoanDto>> getOrderByAmountDesc(@RequestBody Long userId) {
        return ResponseEntity.ok(assetLoanService.postOrderByAmountDesc(userId));
    }

    /**
     * 대출금이 적은 순 정렬
     * @param userId 모든건 사용자 id 기반
     * @return 대출 리스트
     */
    @PostMapping("/order-amount-asc")
    public ResponseEntity<List<AssetLoanDto>> getOrderByAmountAsc(@RequestBody Long userId) {
        return ResponseEntity.ok(assetLoanService.postOrderByAmountAsc(userId));
    }

    /**
     * 대출 다 갚을 경우 hidden 변경
     *
     * @param request 벼ㅑㄴ수들
     * @return hidden 을 변경
     */
    @PostMapping("/update")
    public ResponseEntity<Integer> updateIsEnded(@RequestBody Map<String, Long> request) {
        Long id = request.get("id");
        Long remainedAmount = request.get("remainedAmount");
        Long userId = request.get("userId");
        return ResponseEntity.ok(assetLoanService.isEndedUpdate(id, remainedAmount, userId));
    }

    /**
     * 다 갚은 대출 리스트 출력
     *
     * @param userId 모든건 사용자 id 기반
     * @return 대출 리스트
     */
    @PostMapping("/ended")
    public ResponseEntity<List<AssetLoanDto>> getEndedLoans(@RequestBody Long userId) {
        return ResponseEntity.ok(assetLoanService.postEndedLoans(userId));
    }
}