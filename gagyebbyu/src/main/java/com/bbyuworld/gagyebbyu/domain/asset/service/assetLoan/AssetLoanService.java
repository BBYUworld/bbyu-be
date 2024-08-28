package com.bbyuworld.gagyebbyu.domain.asset.service.assetLoan;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetLoanDto;

import java.util.List;
import java.util.Optional;

public interface AssetLoanService {
    Optional<AssetLoanDto> getUserTargetLoan(Long userId, Long assetId);
    List<AssetLoanDto> getUserLoans(Long userId);

    /* 남은 금액별 정렬 */
    List<AssetLoanDto> getOrderByRemainAmountDesc(Long userId);
    List<AssetLoanDto> getOrderByRemainAmountAsc(Long userId);

    /* 대출금 정렬 */
    List<AssetLoanDto> getOrderByAmountDesc(Long userId);
    List<AssetLoanDto> getOrderByAmountAsc(Long userId);

    int isEndedUpdate(Long assetId, Long remainedAmount,Long userId);

    List<AssetLoanDto> getEndedLoans(Long userId);

    AssetLoanDto getTargetAssetLoan(Long assetId);

    List<AssetLoanDto> getCoupleAssetLoans(Long userId);

    Long getUserRemainedAmount(Long userId);
}
