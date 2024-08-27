package com.bbyuworld.gagyebbyu.domain.analysis.service;

import com.bbyuworld.gagyebbyu.domain.analysis.dto.response.AnnualAssetDto;
import com.bbyuworld.gagyebbyu.domain.analysis.entity.AnnualAsset;
import com.bbyuworld.gagyebbyu.domain.analysis.repository.AnnualAssetRepository;
import com.bbyuworld.gagyebbyu.domain.asset.entity.QAsset;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.bbyuworld.gagyebbyu.domain.analysis.dto.response.AnalysisAssetCategoryDto;
import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.couple.repository.CoupleRepository;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisAssetService {

    private final UserRepository userRepository;
    private final CoupleRepository coupleRepository;
    private final JPAQueryFactory queryFactory;
    private final AnnualAssetRepository annualAssetRepository;

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

}
