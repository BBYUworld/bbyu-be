package com.bbyuworld.gagyebbyu.domain.analysis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnalysisAssetResultDto {
	private long averageAge;
	private long monthlyIncome;
	private long anotherCoupleAssetAvg;
	private long coupleAssetAvg;
}
