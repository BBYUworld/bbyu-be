package com.bbyuworld.gagyebbyu.domain.analysis.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.bbyuworld.gagyebbyu.domain.analysis.dto.response.AnalysisAssetCategoryDto;
import com.bbyuworld.gagyebbyu.domain.asset.entity.QAsset;
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
public class AnalysisService {

    private final UserRepository userRepository;
    private final CoupleRepository coupleRepository;
    private final JPAQueryFactory queryFactory;

    public List<AnalysisAssetCategoryDto> getAssetPercentage(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

        Couple couple = coupleRepository.findById(user.getCoupleId())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));

        QAsset asset = QAsset.asset;

        Integer totalAmount = queryFactory
                .select(asset.amount.sum().intValue())
                .from(asset)
                .where(asset.coupleId.eq(couple.getCoupleId()))
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
                .where(asset.coupleId.eq(couple.getCoupleId()))
                .groupBy(asset.type)
                .fetch().stream()
                .map(dto -> AnalysisAssetCategoryDto.builder()
                        .label(dto.getLabel())
                        .amount(dto.getAmount())
                        .percentage(dto.getPercentage())
                        .build())
                .collect(Collectors.toList());
    }
}
