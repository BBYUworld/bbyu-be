package com.bbyuworld.gagyebbyu.domain.asset.repository.AssetLoanRepo;

import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetLoan;
import com.bbyuworld.gagyebbyu.domain.asset.enums.LoanType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetLoanRepository extends JpaRepository<AssetLoan, Long> {

    // 사용자 ID와 자산 ID로 대출 정보 조회
    @EntityGraph(value = "AssetLoan.withUserAndAsset")
    Optional<AssetLoan> findByUser_UserIdAndAssetId(Long userId, Long assetId);

    // 사용자의 종료되지 않은 모든 대출 조회
    @EntityGraph(value = "AssetLoan.withUserAndAsset")
    List<AssetLoan> findAllByUser_UserIdAndIsEndedFalse(Long userId);

    // 사용자의 숨겨지지 않은 대출의 초기 금액 합계 조회
    @Query("SELECT SUM(a.initialAmount) FROM AssetLoan a WHERE a.user.userId = :userId AND a.isHidden = false")
    Long sumInitialAmountByUser_UserIdAndIsHiddenFalse(Long userId);

    // 사용자의 숨겨지지 않은 모든 대출 조회
    @EntityGraph(value = "AssetLoan.withUserAndAsset")
    List<AssetLoan> findAllByUser_UserIdAndIsHiddenFalse(Long userId);

    // 사용자 ID와 대출 유형으로 대출 조회
    List<AssetLoan> findByUser_UserIdAndLoanType(Long userId, LoanType loanType);

    // 사용자의 종료되지 않은 대출을 현재 금액 기준 내림차순 정렬
    @EntityGraph(value = "AssetLoan.withUserAndAsset")
    List<AssetLoan> findAllByUser_UserIdAndIsEndedFalseOrderByAmountDesc(Long userId);

    // 사용자의 종료되지 않은 대출을 현재 금액 기준 오름차순 정렬
    @EntityGraph(value = "AssetLoan.withUserAndAsset")
    List<AssetLoan> findAllByUser_UserIdAndIsEndedFalseOrderByAmountAsc(Long userId);

    // 사용자의 종료되지 않은 대출을 초기 금액 기준 내림차순 정렬
    @EntityGraph(value = "AssetLoan.withUserAndAsset")
    List<AssetLoan> findAllByUser_UserIdAndIsEndedFalseOrderByInitialAmountDesc(Long userId);

    // 사용자의 종료되지 않은 대출을 초기 금액 기준 오름차순 정렬
    @EntityGraph(value = "AssetLoan.withUserAndAsset")
    List<AssetLoan> findAllByUser_UserIdAndIsEndedFalseOrderByInitialAmountAsc(Long userId);

    // 대출의 현재 금액 업데이트 및 종료 여부 확인
    @Modifying
    @Query("UPDATE AssetLoan a SET a.amount = :amount, " +
            "a.isEnded = CASE WHEN :amount = 0 THEN true ELSE a.isEnded END " +
            "WHERE a.assetId = :assetId AND a.user.userId = :userId")
    int updateAmountAndCheckIsEnded(@Param("assetId") Long id, @Param("amount") Long amount,
                                    @Param("userId") Long userId);

    // 사용자의 종료된 모든 대출 조회
    @EntityGraph(value = "AssetLoan.withUserAndAsset")
    List<AssetLoan> findAllByUser_UserIdAndIsEndedTrue(Long userId);

    // 자산 ID로 대출 정보 조회
    AssetLoan findByAssetId(long assetId);

    // 커플의 모든 대출을 현재 금액 기준 내림차순 정렬
    @EntityGraph(value = "AssetLoan.withUserAndAsset")
    List<AssetLoan> findAllByCouple_CoupleIdOrderByAmountDesc(@Param("coupleId") Long coupleId);

    // 사용자의 대출 유형별 총 현재 금액 조회
    @Query("SELECT SUM(al.amount) FROM AssetLoan al " +
            "WHERE al.loanType = :loanType AND al.user.userId = :userId")
    Long findTotalAmountByLoanTypeAndUser_UserId(@Param("loanType") LoanType loanType, @Param("userId") Long userId);

    // 대출 유형별 총 현재 금액 조회
    @Query("SELECT SUM(al.amount) FROM AssetLoan al WHERE al.loanType = :loanType")
    Long findTotalAmountByLoanType(@Param("loanType") LoanType loanType);

    // 사용자의 총 현재 대출 금액 조회
    @Query("SELECT SUM(al.amount) FROM AssetLoan al WHERE al.user.userId = :userId")
    Long findTotalAmountByUser_UserId(@Param("userId") Long userId);
}