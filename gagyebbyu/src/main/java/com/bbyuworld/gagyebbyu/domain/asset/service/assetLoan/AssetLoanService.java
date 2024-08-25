package com.bbyuworld.gagyebbyu.domain.asset.service.assetLoan;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetLoanDto;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetLoan;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssetLoanService {
    Optional<AssetLoanDto> postUserTargetLoan(Long userId,Long assetId);
    List<AssetLoanDto> postUserLoans(Long userId);

    /* 남은 금액별 정렬 */
    List<AssetLoanDto> postOrderByRemainAmountDesc(Long userId);
    List<AssetLoanDto> postOrderByRemainAmountAsc(Long userId);

    /* 대출금 정렬 */
    List<AssetLoanDto> postOrderByAmountDesc(Long userId);
    List<AssetLoanDto> postOrderByAmountAsc(Long userId);

    int isEndedUpdate(Long assetId, Long remainedAmount,Long userId);

    List<AssetLoanDto> postEndedLoans(Long userId);
}
