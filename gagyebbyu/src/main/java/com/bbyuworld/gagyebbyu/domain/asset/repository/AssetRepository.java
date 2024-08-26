package com.bbyuworld.gagyebbyu.domain.asset.repository;

import com.bbyuworld.gagyebbyu.domain.asset.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    /* 사용자의 전체 자산 내역 정보 제공 */
    List<Asset> findAllByUserIdAndIsHiddenFalse(Long userId);

    /* 사용자의 전체 자산 총합 제공 */
    Long sumAmountByUserIdAndIsHiddenFalse(Long userId);

    /* 커플에 맞는 전체 자산 정보 제공 */
    List<Asset> findAllByCoupleIdAndIsHiddenFalse(Long coupleId);

    /* 커플의 전체 자산 총합 제공 */
    Long sumAmountByCoupleIdAndIsHiddenFalse(Long coupleId);

    /* 자산 추가 -> type 에 맞는 asset_ 하위 테이블에도 추가 save 써서 넘어가기 */

    /* 자산 보이기 변경 -> is_hidden 변경 -> false: 자산 보임 / true: 자산 숨김& 삭제 */
    @Modifying
    @Query("UPDATE Asset a SET a.isHidden = :isHidden WHERE a.assetId = :assetId")
    void updateAssetVisibility(Long assetId, boolean isHidden);

    /* 커플 연결됐을 때, update_assetCouple 변경해줘야 함 userid 2개, coupleid 1개 */
    @Modifying
    @Query("UPDATE Asset a SET a.coupleId = :coupleId WHERE a.userId IN :userIds")
    void updateCoupleIdForUsers(Long coupleId, List<Long> userIds);





}
