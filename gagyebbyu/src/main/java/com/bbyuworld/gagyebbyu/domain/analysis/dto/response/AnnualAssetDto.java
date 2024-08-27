package com.bbyuworld.gagyebbyu.domain.analysis.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AnnualAssetDto {
	private final int year;
	private final Long cashAssets;
	private final Long investmentAssets;
	private final Long realEstateAssets;
	private final Long loanAssets;
	private final Long totalAssets;

	@Builder
	public AnnualAssetDto(int year, Long cashAssets, Long investmentAssets, Long realEstateAssets, Long loanAssets, Long totalAssets) {
		this.year = year;
		this.cashAssets = cashAssets;
		this.investmentAssets = investmentAssets;
		this.realEstateAssets = realEstateAssets;
		this.loanAssets = loanAssets;
		this.totalAssets = totalAssets;
	}
}
