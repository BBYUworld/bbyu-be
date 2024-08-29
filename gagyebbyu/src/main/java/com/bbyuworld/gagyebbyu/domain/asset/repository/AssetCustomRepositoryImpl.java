package com.bbyuworld.gagyebbyu.domain.asset.repository;

import com.bbyuworld.gagyebbyu.domain.asset.entity.Asset;
import com.bbyuworld.gagyebbyu.domain.asset.entity.QAsset;
import com.bbyuworld.gagyebbyu.domain.asset.enums.AssetType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.logging.Logger;

import static com.bbyuworld.gagyebbyu.domain.asset.entity.QAsset.asset;
import static com.bbyuworld.gagyebbyu.domain.couple.entity.QCouple.couple;

@Repository
@RequiredArgsConstructor
public class AssetCustomRepositoryImpl implements AssetCustomRepository {

    private final JPAQueryFactory queryFactory;
    private static final Logger logger = Logger.getLogger(AssetCustomRepositoryImpl.class.getName());

    @Override
    public Long findTotalAssetsForCouple(Long coupleId) {
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
        NumberExpression<Integer> averageAge = couple.user1.age.add(couple.user2.age).divide(2);
        BooleanExpression ageCondition = averageAge.between(startAge, endAge);

        NumberExpression<Long> averageIncome = couple.user1.monthlyIncome.add(couple.user2.monthlyIncome).divide(2);
        BooleanExpression incomeCondition = averageIncome.between(startIncome, endIncome);

        NumberExpression<Long> adjustedAmount = Expressions.cases()
            .when(asset.type.eq(AssetType.LOAN)).then(asset.amount.negate())
            .otherwise(asset.amount);

        List<Double> totalAssets = queryFactory
            .select(adjustedAmount.sum().doubleValue())
            .from(asset)
            .leftJoin(asset.couple, couple)
            .where(ageCondition, incomeCondition,
                asset.isHidden.isFalse())
            .groupBy(couple.coupleId)
            .fetch();

        // 결과가 없을 경우 로그를 남겨 디버깅에 도움이 되도록 합니다.
        if (totalAssets.isEmpty()) {
            logger.warning("No eligible couples found with the given criteria: Age range ["
                + startAge + "-" + endAge + "], Income range ["
                + startIncome + "-" + endIncome + "]");
        }

        return totalAssets.stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
    }
}
