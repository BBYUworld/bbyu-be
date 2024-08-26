package com.bbyuworld.gagyebbyu.domain.asset.repository;

import com.bbyuworld.gagyebbyu.domain.asset.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    /* 사용자의 전체 자산 내역 정보 제공 */
    List<Asset> findAllByUser_UserIdAndIsHiddenFalse(Long userId);

    /* 커플에 맞는 전체 자산 정보 제공 */
    List<Asset> findAllByCouple_CoupleIdAndIsHiddenFalse(Long coupleId);

    /* 사용자의 전체 자산 총합 제공 */
    @Query("SELECT SUM(a.amount) FROM Asset a WHERE a.user.userId = :userId AND a.isHidden = false")
    Long sumAmountByUser_UserIdAndIsHiddenFalse(@Param("userId") Long userId);

    /* 커플의 전체 자산 총합 제공 */
    @Query("SELECT SUM(a.amount) FROM Asset a WHERE a.couple.coupleId = :coupleId AND a.isHidden = false")
    Long sumAmountByCouple_CoupleIdAndIsHiddenFalse(@Param("coupleId") Long coupleId);


    /* 자산 추가 -> type 에 맞는 asset_ 하위 테이블에도 추가 save 써서 넘어가기 */

    /* 자산 보이기 변경 -> is_hidden 변경 -> false: 자산 보임 / true: 자산 숨김& 삭제 */
    @Modifying
    @Query("UPDATE Asset a SET a.isHidden = :isHidden WHERE a.assetId = :assetId")
    void updateAssetVisibility(Long assetId, boolean isHidden);

    /* 커플 연결됐을 때, 자산 업데이트 */
    @Modifying
    @Query("UPDATE Asset a SET a.couple.coupleId = :coupleId WHERE a.user.userId = :user1Id OR a.user.userId = :user2Id")
    void updateAssetsByCouple_CoupleId(@Param("coupleId") Long coupleId, @Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

}
