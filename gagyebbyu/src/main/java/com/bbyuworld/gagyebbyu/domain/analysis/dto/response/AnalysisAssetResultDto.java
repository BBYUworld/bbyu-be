package com.bbyuworld.gagyebbyu.domain.analysis.dto.response;

import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnalysisAssetResultDto {
	private AssetType assetType;
	private long averageAge;
	private long monthlyIncome;
	private long anotherCoupleAssetAvg;
	private long coupleAssetAvg;
}
