package com.bbyuworld.gagyebbyu.domain.asset.repository;

import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetAccount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.bbyuworld.gagyebbyu.domain.asset.entity.QAssetAccount.assetAccount;

@Repository
@RequiredArgsConstructor
public class AssetAccountRepositoryImpl implements AssetAccountRepositoryCustom{
    private final JPAQueryFactory queryFactory;


    @Override
    public AssetAccount findAssetAccountByAccountNo(String accountNo) {
        return queryFactory.selectFrom(assetAccount)
                .where(assetAccount.accountNumber.eq(accountNo))
                .fetchOne();
    }
}
