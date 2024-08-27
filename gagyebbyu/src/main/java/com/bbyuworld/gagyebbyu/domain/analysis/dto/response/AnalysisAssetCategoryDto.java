package com.bbyuworld.gagyebbyu.domain.analysis.dto.response;

import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public final class AnalysisAssetCategoryDto {
    private AssetType label;
    private int amount;
    private double percentage;
}
