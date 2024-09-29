package com.bbyuworld.gagyebbyu.domain.asset.repository.AssetCustomRepo;

import com.bbyuworld.gagyebbyu.domain.analysis.repository.AnnualAssetRepository;
import com.bbyuworld.gagyebbyu.domain.asset.enums.AssetType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.logging.Logger;

import static com.bbyuworld.gagyebbyu.domain.asset.entity.QAsset.asset;
import static com.bbyuworld.gagyebbyu.domain.couple.entity.QCouple.couple;
import static com.bbyuworld.gagyebbyu.domain.analysis.entity.QAnnualAsset.*;

@Repository
@RequiredArgsConstructor
public class AssetCustomRepositoryImpl implements AssetCustomRepository {

    private final JPAQueryFactory queryFactory;
    private static final Logger logger = Logger.getLogger(AssetCustomRepositoryImpl.class.getName());
    private final AnnualAssetRepository annualAssetRepository;

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

        // 평균 나이를 계산하고, 나이 조건을 설정합니다.
        NumberExpression<Integer> averageAge = couple.user1.age.add(couple.user2.age).divide(2);
        BooleanExpression ageCondition = averageAge.between(startAge, endAge);

        // 평균 소득을 계산하고, 소득 조건을 설정합니다.
        NumberExpression<Long> averageIncome = couple.user1.monthlyIncome.add(couple.user2.monthlyIncome).divide(2);
        BooleanExpression incomeCondition = averageIncome.between(startIncome, endIncome);

        // 작년을 기준으로 데이터를 조회합니다.
        int lastYear = LocalDate.now().getYear() - 1;

        Double averageAssets = queryFactory
            .select(annualAsset.totalAssets.avg())  // 작년 자산에서 평균 자산 금액을 선택합니다.
            .from(annualAsset)
            .join(annualAsset.couple, couple)
            .where(ageCondition, incomeCondition, annualAsset.year.eq(lastYear))  // 나이, 소득 조건과 함께 작년 데이터를 필터링합니다.
            .fetchOne();

        // 평균 자산 금액이 null인 경우 0.0 반환
        return averageAssets != null ? averageAssets : 0.0;
    }


}
