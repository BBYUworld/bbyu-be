package com.bbyuworld.gagyebbyu.domain.asset.repository;

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

    @EntityGraph(value = "Asset.withUser")
    Optional<AssetLoan> findByUser_UserIdAndAssetId(Long userId, Long assetId);

    /* 전체 대출 출력 -> 유저별 */
    @EntityGraph(value = "Asset.withUser")
    List<AssetLoan> findAllByUser_UserIdAndIsEndedFalse(Long userId);

    /*  */
    @Query("SELECT SUM(a.remainedAmount) FROM AssetLoan a WHERE a.user.userId = :userId AND a.isHidden = false")
    Long sumRemainedAmountByUser_UserIdAndIsHiddenFalse(Long userId);

    @EntityGraph(value = "Asset.withUser")
    List<AssetLoan> findAllByUser_UserIdAndIsHiddenFalse(Long userId);

    List<AssetLoan> findByUser_UserIdAndLoanType(Long userId, LoanType loanType);


    /* 남은 금액별 정렬 */
    @EntityGraph(value = "Asset.withUser")
    List<AssetLoan> findAllByUser_UserIdAndIsEndedFalseOrderByRemainedAmountDesc(Long userId);
    @EntityGraph(value = "Asset.withUser")
    List<AssetLoan> findAllByUser_UserIdAndIsEndedFalseOrderByRemainedAmountAsc(Long userId);

    /* 대출금 정렬 */
    @EntityGraph(value = "Asset.withUser")
    List<AssetLoan> findAllByUser_UserIdAndIsEndedFalseOrderByAmountDesc(Long userId);
    @EntityGraph(value = "Asset.withUser")
    List<AssetLoan> findAllByUser_UserIdAndIsEndedFalseOrderByAmountAsc(Long userId);

    /* 상환 완료된 대출 제거 */
    @Modifying
    @Query("UPDATE AssetLoan a SET a.remainedAmount = :remainedAmount, " +
            "a.isEnded = CASE WHEN :remainedAmount = 0 THEN true ELSE a.isEnded END " +
            "WHERE a.assetId = :assetId AND a.user.userId = :userId")
    int updateRemainedAmountAndCheckIsEnded(@Param("assetId") Long id, @Param("remainedAmount") Long remainedAmount,
                                            @Param("userId") Long userId);
    /* 상환 완료된 대출 보기 */
    @EntityGraph(value = "Asset.withUser")
    List<AssetLoan> findAllByUser_UserIdAndIsEndedTrue(Long userId);

    //특정 대출 정보 출력
    AssetLoan findByAssetId(long assetId);

    /* 부부의 대출 조회 */
    @EntityGraph(value = "Asset.withUser")
    List<AssetLoan> findAllByCouple_CoupleIdOrderByAmountDesc(@Param("coupleId")Long coupleId);

    /* 실제 사용 -> user Id에 맞는 대출 정보 출력 by type */
    @Query("SELECT SUM(al.amount) FROM AssetLoan al " +
            "WHERE al.loanType = :loanType AND al.user.userId = :userId")
    Long findTotalAmountByLoanTypeAndUser_UserId(@Param("loanType") LoanType loanType, @Param("userId") Long userId);

    /* test를 위한 코드 */
    @Query("SELECT SUM(al.amount) FROM AssetLoan al " +
            "WHERE al.loanType = :loanType")
    Long findTotalAmountByLoanType(@Param("loanType") LoanType loanType);

    @Query("SELECT SUM(al.amount) FROM AssetLoan al " +
            "WHERE al.user.userId = :userId")
    Long findTotalAmountByUser_UserId(@Param("userId") Long userId);

}