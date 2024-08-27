package com.bbyuworld.gagyebbyu.domain.analysis.service;

import com.bbyuworld.gagyebbyu.domain.analysis.dto.response.AnalysisAssetResultDto;
import com.bbyuworld.gagyebbyu.domain.analysis.dto.response.AnnualAssetDto;
import com.bbyuworld.gagyebbyu.domain.analysis.dto.response.AssetChangeRateDto;
import com.bbyuworld.gagyebbyu.domain.analysis.entity.AnnualAsset;
import com.bbyuworld.gagyebbyu.domain.analysis.repository.AnnualAssetRepository;
import com.bbyuworld.gagyebbyu.domain.asset.enums.AssetType;
import com.bbyuworld.gagyebbyu.domain.asset.entity.QAsset;
import com.bbyuworld.gagyebbyu.domain.asset.repository.AssetRepository;
import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.bbyuworld.gagyebbyu.domain.analysis.dto.response.AnalysisAssetCategoryDto;
import com.bbyuworld.gagyebbyu.domain.couple.repository.CoupleRepository;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisAssetService {

    private final UserRepository userRepository;
    private final CoupleRepository coupleRepository;
    private final JPAQueryFactory queryFactory;
    private final AnnualAssetRepository annualAssetRepository;
    private final AssetRepository assetRepository;

    public List<AnalysisAssetCategoryDto> getAssetPercentage(long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

        Long coupleId = user.getCoupleId();
        if (coupleId == null) {
            throw new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND);
        }

        QAsset asset = QAsset.asset;

        Integer totalAmount = queryFactory
            .select(asset.amount.sum().intValue())
            .from(asset)
            .where(asset.couple.coupleId.eq(coupleId))
            .fetchOne();

        if (totalAmount == null || totalAmount == 0) {
            throw new DataNotFoundException(ErrorCode.ASSET_NOT_FOUND);
        }

        return queryFactory
            .select(Projections.constructor(AnalysisAssetCategoryDto.class,
                asset.type.stringValue(),
                asset.amount.sum().intValue(),
                asset.amount.sum().doubleValue().divide(totalAmount).multiply(100.0)))
            .from(asset)
            .where(asset.couple.coupleId.eq(coupleId))
            .groupBy(asset.type)
            .fetch().stream()
            .map(dto -> AnalysisAssetCategoryDto.builder()
                .label(dto.getLabel())
                .amount(dto.getAmount())
                .percentage(roundToOneDecimalPlace(dto.getPercentage()))
                .build())
            .collect(Collectors.toList());
    }

    private double roundToOneDecimalPlace(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    public List<AnnualAssetDto> getAnnualAsset(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

        Long coupleId = user.getCoupleId();
        if (coupleId == null) {
            throw new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND);
        }

        return annualAssetRepository.findByCouple_CoupleId(coupleId).stream()
            .map(asset -> new AnnualAssetDto(
                asset.getYear(),
                asset.getCashAssets(),
                asset.getInvestmentAssets(),
                asset.getRealEstateAssets(),
                asset.getLoanAssets(),
                asset.getTotalAssets()
            ))
            .collect(Collectors.toList());
    }


    public AssetChangeRateDto getAssetChangeRate(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
        Couple couple = coupleRepository.findById(user.getCoupleId())
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));

        int lastYear = LocalDate.now().getYear() - 1;

        AnnualAsset lastYearAsset = annualAssetRepository.findByCoupleAndYear(couple, lastYear)
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.LAST_YEAR_ASSET_NOT_FOUND));

        QAsset asset = QAsset.asset;

        Map<AssetType, Long> currentAssets = queryFactory
            .select(asset.type, asset.amount.sum())
            .from(asset)
            .where(asset.couple.coupleId.eq(couple.getCoupleId()))
            .groupBy(asset.type)
            .fetch().stream()
            .collect(Collectors.toMap(
                tuple -> tuple.get(asset.type),
                tuple -> tuple.get(asset.amount.sum())
            ));

        Long currentCashAssets = currentAssets.getOrDefault(AssetType.ACCOUNT, 0L);
        Long currentInvestmentAssets = currentAssets.getOrDefault(AssetType.STOCK, 0L);
        Long currentRealEstateAssets = currentAssets.getOrDefault(AssetType.REAL_ESTATE, 0L);
        Long currentLoanAssets = currentAssets.getOrDefault(AssetType.LOAN, 0L);
        Long currentTotalAssets = currentCashAssets + currentInvestmentAssets + currentRealEstateAssets - currentLoanAssets;

        // 증감율 계산
        Long cashChangeRate = calculateChangeRate(lastYearAsset.getCashAssets(), currentCashAssets);
        Long investmentChangeRate = calculateChangeRate(lastYearAsset.getInvestmentAssets(), currentInvestmentAssets);
        Long realEstateChangeRate = calculateChangeRate(lastYearAsset.getRealEstateAssets(), currentRealEstateAssets);
        Long loanChangeRate = calculateChangeRate(lastYearAsset.getLoanAssets(), currentLoanAssets);
        Long totalChangeRate = calculateChangeRate(lastYearAsset.getTotalAssets(), currentTotalAssets);

        return AssetChangeRateDto.builder()
            .cashChangeRate(cashChangeRate)
            .investmentChangeRate(investmentChangeRate)
            .realEstateChangeRate(realEstateChangeRate)
            .loanChangeRate(loanChangeRate)
            .totalChangeRate(totalChangeRate)
            .build();
    }

    private Long calculateChangeRate(Long previousValue, Long currentValue) {
        if (previousValue == 0) {
            return (currentValue > 0) ? 100L : 0L; // 이전 값이 0이면 현재 값이 0 이상인 경우 100% 상승, 그렇지 않으면 0%
        }
        return ((currentValue - previousValue) * 100) / previousValue;
    }

    public AnalysisAssetResultDto getAssetResult(long userId) {
        // 사용자 조회: 주어진 userId로 사용자 정보를 userRepository에서 조회합니다. 사용자가 존재하지 않으면 DataNotFoundException을 던집니다.
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

        // 커플 정보 조회: 사용자가 속한 커플 정보를 coupleRepository에서 조회합니다. 커플 정보가 없으면 DataNotFoundException을 던집니다.
        Couple couple = coupleRepository.findById(user.getCoupleId())
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));

        // 평균 나이 계산: 커플의 두 구성원의 나이를 더한 후 2로 나누어 평균 나이를 계산합니다.
        int avgAge = (couple.getUser1().getAge() + couple.getUser2().getAge()) / 2;

        // 연령대 계산: 평균 나이를 기반으로 연령대(예: 30-39세)를 계산합니다.
        int startAge = avgAge / 10 * 10;
        int endAge = startAge + 9;

        // 평균 수입 계산: 커플의 두 구성원의 월 소득을 더한 후 2로 나누어 평균 수입을 계산합니다.
        long avgIncome = (couple.getUser1().getMonthlyIncome() + couple.getUser2().getMonthlyIncome()) / 2;

        // 수입 범위 계산: 평균 수입을 기반으로 수입 범위를 계산합니다. 예를 들어, 100만 원 단위로 범위를 설정합니다.
        long startIncome = avgIncome / 100000 * 100000 - 1000000;
        long endIncome = startIncome + 1000000;

        // 다른 커플의 평균 자산 조회: 비슷한 나이대와 수입 범위를 가진 다른 커플들의 평균 자산을 조회합니다.
        double anotherCoupleAverageAssets = assetRepository.findAverageAssetsForEligibleCouples(
            startAge, endAge, startIncome, endIncome);

        // 최고 자산 타입 조회: 커플의 작년 자산에서 가장 많이 사용된 타입을 조회합니다.
        AssetType topAssetType = assetRepository.findTopAssetTypeForCoupleLastYear(couple.getCoupleId());

        // 커플의 총 자산 조회: 커플의 작년의 총 자산을 조회합니다.
        long coupleTotalAssets = assetRepository.findTotalAssetsForCoupleLastYear(couple.getCoupleId());

        // 결과 반환: 조회된 데이터를 바탕으로 AssetResultDto 객체를 생성하여 반환합니다.
        return new AnalysisAssetResultDto(topAssetType, startAge, startIncome + 1000000,
            (long)anotherCoupleAverageAssets, coupleTotalAssets);
    }


}
