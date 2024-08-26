package com.bbyuworld.gagyebbyu.domain.analysis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public final class AnalysisAssetCategoryDto {
    private String label;
    private int amount;
    private double percentage;
}
