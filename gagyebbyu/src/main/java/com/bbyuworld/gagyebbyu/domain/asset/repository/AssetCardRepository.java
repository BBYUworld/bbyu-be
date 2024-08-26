package com.bbyuworld.gagyebbyu.domain.asset.repository;

import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetCard;
import com.bbyuworld.gagyebbyu.domain.asset.enums.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetCardRepository extends JpaRepository<AssetCard, Long> {

    /* 사용자 전체 카드 보기 */
    @Query("SELECT ac FROM AssetCard ac JOIN FETCH ac.user WHERE ac.user.userId = :userId AND ac.isHidden = false")
    List<AssetCard> findByUser_UserIdAndIsHiddenFalse(Long userId);

    /* 카드 타입별로 보기 */
    @Query("SELECT ac FROM AssetCard ac JOIN FETCH ac.user WHERE ac.user.userId = :userId AND ac.cardType = :cardType AND ac.isHidden = false")
    List<AssetCard> findAllByUser_UserIdAndCardTypeAndIsHiddenFalse(Long userId, CardType cardType);
}
