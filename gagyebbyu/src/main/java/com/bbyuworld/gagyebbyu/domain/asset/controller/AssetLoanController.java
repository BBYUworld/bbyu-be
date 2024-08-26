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

/**
 * 대출 자산 관련 API를 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/api/asset-loans")
@RequiredArgsConstructor
public class AssetLoanController {

    private final AssetLoanService assetLoanService;

    /**
     * 사용자의 특정 loan 상품을 불러오기 위한 메서드
     *
     * @param assetId 조회할 자산의 ID
     * @return 특정 loan 상품 정보를 담은 ResponseEntity
     */
    @GetMapping("/target")
    @RequireJwtToken
    public ResponseEntity<AssetLoanDto> getUserTargetLoan(@Param("assetId") Long assetId) {
        return assetLoanService.getUserTargetLoan(UserContext.getUserId(), assetId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 사용자의 id에 맞는 모든 대출 상품을 출력하는 메서드
     *
     * @return 사용자가 가입한 대출 상품 목록을 담은 ResponseEntity
     */
    @GetMapping("/user")
    @RequireJwtToken
    public ResponseEntity<List<AssetLoanDto>> getUserLoans() {
        return ResponseEntity.ok(assetLoanService.getUserLoans(UserContext.getUserId()));
    }

    /**
     * 남은 대출금을 기준으로 내림차순 정렬하여 대출 상품 목록을 반환하는 메서드
     *
     * @return 갚을 대출액이 많은 순으로 정렬된 대출 상품 목록을 담은 ResponseEntity
     */
    @GetMapping("/order-remain-desc")
    @RequireJwtToken
    public ResponseEntity<List<AssetLoanDto>> getOrderByRemainAmountDesc() {
        return ResponseEntity.ok(assetLoanService.getOrderByRemainAmountDesc(UserContext.getUserId()));
    }

    /**
     * 남은 대출금을 기준으로 오름차순 정렬하여 대출 상품 목록을 반환하는 메서드
     *
     * @return 갚을 대출액이 적은 순으로 정렬된 대출 상품 목록을 담은 ResponseEntity
     */
    @GetMapping("/order-remain-asc")
    @RequireJwtToken
    public ResponseEntity<List<AssetLoanDto>> getOrderByRemainAmountAsc() {
        return ResponseEntity.ok(assetLoanService.getOrderByRemainAmountAsc(UserContext.getUserId()));
    }

    /**
     * 대출금 총액을 기준으로 내림차순 정렬하여 대출 상품 목록을 반환하는 메서드
     *
     * @return 대출금이 많은 순으로 정렬된 대출 상품 목록을 담은 ResponseEntity
     */
    @GetMapping("/order-amount-desc")
    @RequireJwtToken
    public ResponseEntity<List<AssetLoanDto>> getOrderByAmountDesc() {
        return ResponseEntity.ok(assetLoanService.getOrderByAmountDesc(UserContext.getUserId()));
    }

    /**
     * 대출금 총액을 기준으로 오름차순 정렬하여 대출 상품 목록을 반환하는 메서드
     *
     * @return 대출금이 적은 순으로 정렬된 대출 상품 목록을 담은 ResponseEntity
     */
    @GetMapping("/order-amount-asc")
    @RequireJwtToken
    public ResponseEntity<List<AssetLoanDto>> getOrderByAmountAsc() {
        return ResponseEntity.ok(assetLoanService.getOrderByAmountAsc(UserContext.getUserId()));
    }

    /**
     * 대출을 모두 갚은 경우 해당 대출의 상태를 업데이트하는 메서드
     *
     * @param assetId 업데이트할 자산의 ID
     * @param remainedAmount 남은 대출 금액
     * @return 업데이트된 레코드 수를 담은 ResponseEntity
     */
    @PostMapping("/update")
    @RequireJwtToken
    public ResponseEntity<Integer> updateIsEnded(@RequestParam Long assetId, @RequestParam Long remainedAmount) {
        return ResponseEntity.ok(assetLoanService.isEndedUpdate(assetId, remainedAmount, UserContext.getUserId()));
    }

    /**
     * 모두 갚은 대출 목록을 반환하는 메서드
     *
     * @return 완납된 대출 상품 목록을 담은 ResponseEntity
     */
    @GetMapping("/ended")
    @RequireJwtToken
    public ResponseEntity<List<AssetLoanDto>> getEndedLoans() {
        return ResponseEntity.ok(assetLoanService.getEndedLoans(UserContext.getUserId()));
    }
}