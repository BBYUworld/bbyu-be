package com.bbyuworld.gagyebbyu.domain.analysis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnalysisAssetResultDto {
	private long startAge;
	private long startIncome;
	private long anotherCoupleAssetAvg;
	private long currentAsset;
	private long lastYearAsset;
}
