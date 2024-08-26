package com.bbyuworld.gagyebbyu.domain.asset.controller;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetLoanDto;
import com.bbyuworld.gagyebbyu.domain.asset.service.assetLoan.AssetLoanService;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
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
     * @param assetId assetId
     * @return 특정 loan 상품
     */
    @GetMapping("/target")
    @RequireJwtToken
    public ResponseEntity<AssetLoanDto> getUserTargetLoan(@Param("assetId") Long assetId) {
        return assetLoanService.getUserTargetLoan(UserContext.getUserId(), assetId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 사용자의 id에 맞는 모든 대출 상품 출력
     *
     * @return 사용자가 가입한 대출 상품 list
     */
    @PostMapping("/user")
    @RequireJwtToken
    public ResponseEntity<List<AssetLoanDto>> getUserLoans() {
        return ResponseEntity.ok(assetLoanService.getUserLoans(UserContext.getUserId()));
    }

    /**
     * 남은 대출금 정렬 내림차순
     *
     * @return 갚을 대출액이 많은 순 정렬
     */
    @PostMapping("/order-remain-desc")
    @RequireJwtToken
    public ResponseEntity<List<AssetLoanDto>> getOrderByRemainAmountDesc() {
        return ResponseEntity.ok(assetLoanService.getOrderByRemainAmountDesc(UserContext.getUserId()));
    }

    /**
     * 남은 대출금 정렬 오름차순
     *
     * @return 갚을 대출액이 적은ㄴ 순 정렬
     */
    @PostMapping("/order-remain-asc")
    @RequireJwtToken
    public ResponseEntity<List<AssetLoanDto>> getOrderByRemainAmountAsc() {
        return ResponseEntity.ok(assetLoanService.getOrderByRemainAmountAsc(UserContext.getUserId()));
    }

    /**
     * 대출금이 많은순 정렬
     *
     * @return 대출 리스트
     */
    @PostMapping("/order-amount-desc")
    @RequireJwtToken
    public ResponseEntity<List<AssetLoanDto>> getOrderByAmountDesc() {
        return ResponseEntity.ok(assetLoanService.getOrderByAmountDesc(UserContext.getUserId()));
    }

    /**
     * 대출금이 적은 순 정렬
     *
     * @return 대출 리스트
     */
    @PostMapping("/order-amount-asc")
    @RequireJwtToken
    public ResponseEntity<List<AssetLoanDto>> getOrderByAmountAsc() {
        return ResponseEntity.ok(assetLoanService.getOrderByAmountAsc(UserContext.getUserId()));
    }

    /**
     * 대출 다 갚을 경우 hidden 변경
     *
     * @return hidden 을 변경
     */
    @PostMapping("/update")
    @RequireJwtToken
    public ResponseEntity<Integer> updateIsEnded(Long assetId, Long remainedAmount) {
        return ResponseEntity.ok(assetLoanService.isEndedUpdate(assetId, remainedAmount, UserContext.getUserId()));
    }

    /**
     * 다 갚은 대출 리스트 출력
     *
     * @return 대출 리스트
     */
    @PostMapping("/ended")
    @RequireJwtToken
    public ResponseEntity<List<AssetLoanDto>> getEndedLoans() {
        return ResponseEntity.ok(assetLoanService.getEndedLoans(UserContext.getUserId()));
    }
}