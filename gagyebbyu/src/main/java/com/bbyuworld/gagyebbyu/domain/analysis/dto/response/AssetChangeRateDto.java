package com.bbyuworld.gagyebbyu.domain.analysis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AssetChangeRateDto {
	private final Long cashChangeRate;
	private final Long investmentChangeRate;
	private final Long realEstateChangeRate;
	private final Long loanChangeRate;
	private final Long totalChangeRate;

}
