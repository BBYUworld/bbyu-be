package com.bbyuworld.gagyebbyu.domain.asset.repository;

import com.bbyuworld.gagyebbyu.domain.asset.entity.Asset;
import com.bbyuworld.gagyebbyu.domain.asset.entity.QAsset;
import com.bbyuworld.gagyebbyu.domain.asset.enums.AssetType; // AssetType Enum을 가져옵니다.
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bbyuworld.gagyebbyu.domain.asset.entity.QAsset.asset;
import static com.bbyuworld.gagyebbyu.domain.couple.entity.QCouple.couple;

@Repository
@RequiredArgsConstructor
public class AssetCustomRepositoryImpl implements AssetCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long findTotalAssetsForCouple(Long coupleId) {
        // AssetType에 따라 금액을 조정합니다.
        NumberExpression<Long> adjustedAmount = Expressions.cases()
                .when(asset.type.eq(AssetType.LOAN)).then(asset.amount.negate())
                .otherwise(asset.amount);

        return queryFactory
                .select(adjustedAmount.sum())
                .from(asset)
                .where(asset.couple.coupleId.eq(coupleId)
                        .and(asset.isHidden.isFalse()))
                .fetchOne();
    }

    @Override
    public double findAverageAssetsForEligibleCouples(int startAge, int endAge, long startIncome, long endIncome) {
        // 커플의 평균 나이를 계산하고 조건을 만듭니다.
        NumberExpression<Integer> averageAge = couple.user1.age.add(couple.user2.age).divide(2);
        BooleanExpression ageCondition = averageAge.between(startAge, endAge);

        // 커플의 평균 소득을 계산하고 조건을 만듭니다.
        NumberExpression<Long> averageIncome = couple.user1.monthlyIncome.add(couple.user2.monthlyIncome).divide(2);
        BooleanExpression incomeCondition = averageIncome.between(startIncome, endIncome);

        // AssetType에 따라 금액을 조정합니다.
        NumberExpression<Long> adjustedAmount = Expressions.cases()
                .when(asset.type.eq(AssetType.LOAN)).then(asset.amount.negate())
                .otherwise(asset.amount);

        // 각 커플별 총 자산을 계산하고, 그들의 평균을 구합니다.
        List<Double> totalAssets = queryFactory
                .select(adjustedAmount.sum().doubleValue())
                .from(asset)
                .join(asset.couple, couple)
                .where(ageCondition, incomeCondition,
                        asset.isHidden.isFalse())
                .groupBy(couple.coupleId)
                .fetch();

        // 총 자산의 평균을 계산합니다.
        return totalAssets.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }
}
