package com.bbyuworld.gagyebbyu.domain.asset.repository;

import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetLoanRepository extends JpaRepository<AssetLoan, Long> {

    Optional<AssetLoan> findByUserIdAndAssetId(Long userId, Long assetId);

    /* 전체 대출 출력 -> 유저별 */
    List<AssetLoan> findAllByUserIdAndIsEndedFalse(Long userId);

    /* 남은 금액별 정렬 */
    List<AssetLoan> findAllByUserIdAndIsEndedFalseOrderByRemainedAmountDesc(Long userId);
    List<AssetLoan> findAllByUserIdAndIsEndedFalseOrderByRemainedAmountAsc(Long userId);

    /* 대출금 정렬 */
    List<AssetLoan> findAllByUserIdAndIsEndedFalseOrderByAmountDesc(Long userId);
    List<AssetLoan> findAllByUserIdAndIsEndedFalseOrderByAmountAsc(Long userId);

    /* 상환 완료된 대출 제거 */
    @Modifying
    @Query("UPDATE AssetLoan a SET a.remainedAmount = :remainedAmount, a.isEnded = CASE WHEN :remainedAmount = 0 THEN true ELSE a.isEnded END WHERE a.assetId = :assetId AND a.userId = :userId")
    int updateRemainedAmountAndCheckIsEnded(@Param("assetId") Long id, @Param("remainedAmount") Long remainedAmount, @Param("userId") Long userId);

    /* 상환 완료된 대출 보기 */
    List<AssetLoan> findAllByUserIdAndIsEndedTrue(Long userId);
}