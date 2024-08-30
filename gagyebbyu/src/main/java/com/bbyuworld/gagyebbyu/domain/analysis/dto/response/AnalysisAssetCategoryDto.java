package com.bbyuworld.gagyebbyu.domain.analysis.dto.response;

import java.util.Locale;

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

    public AnalysisAssetCategoryDto(String label, int amount, double percentage) {
        this.label = AssetType.valueOf(label.toUpperCase(Locale.ENGLISH));
        this.amount = amount;
        this.percentage = percentage;
    }
}

