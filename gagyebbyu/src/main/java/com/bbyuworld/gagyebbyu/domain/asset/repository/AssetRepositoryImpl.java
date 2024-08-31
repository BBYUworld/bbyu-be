package com.bbyuworld.gagyebbyu.domain.asset.repository;

import com.bbyuworld.gagyebbyu.domain.asset.enums.AssetType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.bbyuworld.gagyebbyu.domain.asset.entity.QAsset.asset;
import static com.bbyuworld.gagyebbyu.domain.couple.entity.QCouple.couple;
import static com.bbyuworld.gagyebbyu.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class AssetRepositoryImpl implements AssetRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Long findCoupleAccount(Long userId, AssetType assetType) {
        return queryFactory
                .select(asset.amount.sum())
                .from(asset)
                .join(asset.user, user)
                .join(asset.couple, couple)
                .where(
                        user.userId.eq(userId)
                                .and(asset.type.eq(assetType))
                )
                .fetchOne();
    }
}
