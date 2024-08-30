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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class  AnalysisAssetService {

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

        List<AnnualAssetDto> annualAssets = annualAssetRepository.findByCouple_CoupleId(coupleId).stream()
            .map(asset -> new AnnualAssetDto(
                asset.getYear(),
                asset.getAccountAssets(),
                asset.getStockAssets(),
                asset.getRealEstateAssets(),
                asset.getLoanAssets(),
                asset.getTotalAssets()
            ))
            .collect(Collectors.toList());

        QAsset asset = QAsset.asset;

        Map<AssetType, Long> currentAssets = queryFactory
            .select(asset.type, asset.amount.sum())
            .from(asset)
            .where(asset.couple.coupleId.eq(coupleId))
            .groupBy(asset.type)
            .fetch().stream()
            .collect(Collectors.toMap(
                tuple -> tuple.get(asset.type),
                tuple -> tuple.get(asset.amount.sum())
            ));

        Long currentAccountAssets = currentAssets.getOrDefault(AssetType.ACCOUNT, 0L);
        Long currentStockAssets = currentAssets.getOrDefault(AssetType.STOCK, 0L);
        Long currentRealEstateAssets = currentAssets.getOrDefault(AssetType.REAL_ESTATE, 0L);
        Long currentLoanAssets = currentAssets.getOrDefault(AssetType.LOAN, 0L);
        Long currentTotalAssets = currentAccountAssets + currentStockAssets + currentRealEstateAssets - currentLoanAssets;

        AnnualAssetDto currentYearAssetDto = new AnnualAssetDto(
            LocalDate.now().getYear(),
            currentAccountAssets,
            currentStockAssets,
            currentRealEstateAssets,
            currentLoanAssets,
            currentTotalAssets
        );

        annualAssets.add(currentYearAssetDto);
        annualAssets.sort(Comparator.comparingInt(AnnualAssetDto::getYear));

        return annualAssets;
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

        Long cashChangeRate = calculateChangeRate(lastYearAsset.getAccountAssets(), currentCashAssets);
        Long investmentChangeRate = calculateChangeRate(lastYearAsset.getStockAssets(), currentInvestmentAssets);
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
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

        Couple couple = coupleRepository.findById(user.getCoupleId())
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));

        int avgAge = (couple.getUser1().getAge() + couple.getUser2().getAge()) / 2;

        int startAge = avgAge / 10 * 10;
        int endAge = startAge + 9;

        long avgIncome = (couple.getUser1().getMonthlyIncome() + couple.getUser2().getMonthlyIncome()) / 2;

        long startIncome = avgIncome / 100000 * 100000 - 1000000;
        long endIncome = startIncome + 1000000;

        System.out.println("Calculated startAge: " + startAge);
        System.out.println("Calculated endAge: " + endAge);
        System.out.println("Calculated startIncome: " + startIncome);
        System.out.println("Calculated enedIncome: " + endIncome);


        double anotherCoupleAverageAssets = assetRepository.findAverageAssetsForEligibleCouples(
            startAge, endAge, startIncome, endIncome);

        System.out.println("Another Couple Average Assets: " + anotherCoupleAverageAssets);

        long coupleTotalAssets = assetRepository.findTotalAssetsForCouple(couple.getCoupleId());

        int lastYear = LocalDate.now().getYear() - 1;
        long lastYearAssets = annualAssetRepository.findTotalAssetsForCoupleLastYear(couple.getCoupleId(), lastYear);

        return new AnalysisAssetResultDto(
            startAge,
            startIncome,
            (long) anotherCoupleAverageAssets,
            coupleTotalAssets,
            lastYearAssets
        );
    }

}
